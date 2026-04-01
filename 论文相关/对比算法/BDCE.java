package cn.edu.hnu.esnl.app.yuekunchen.cost.access.alg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import cn.edu.hnu.esnl.bean.Application;
import cn.edu.hnu.esnl.bean.Criticality;
import cn.edu.hnu.esnl.bean.Processor;
import cn.edu.hnu.esnl.bean.Task;
import cn.edu.hnu.esnl.bean.view.ESTEFT;
import cn.edu.hnu.esnl.bean.view.StartEndTime;
import cn.edu.hnu.esnl.schedule.assitant.HEFTScheduler;
import cn.edu.hnu.esnl.service.ApplicationServiceBean;
import cn.edu.hnu.esnl.service.TaskServiceBean;
import cn.edu.hnu.esnl.service.energy.EnergyService;
import cn.edu.hnu.esnl.service.reliability.ReliabilityModelService;
import cn.edu.hnu.esnl.system.SystemValue;
import cn.edu.hnu.esnl.util.DeepCopyUtil;
import cn.edu.hnu.esnl.util.DoubleUtil;
import cn.edu.hnu.esnl.bean.Level;


/**
 * @author xgq E-mail:xgqman@126.com
 * Energy-efficient workflow scheduling with
udget-deadline constraints for cloud
依靠level.java 类
BDCE
1.给任务分层级
2.计算每层完成时间 & 总完成时间 sigma
3.逐层分配预算，
每层预算为：上一层剩余预算 + （该层完成时间/总完成时间）* 总预算
4.层内任务按最早完成时间排序 lower_bound_ft()
5.遍历任务，分配预算，
*/

public class BDCE {

	public static Application exe(List<Processor> givenProcessorList, Integer[][] compMartrix, Integer[][] commMartrix, double appCostBudget, Boolean... print) throws IOException,
			ClassNotFoundException {

		List<Processor> givenProcessorList0 = DeepCopyUtil.copy(givenProcessorList);
		Application g0 = new Application("F_1", Criticality.S3);
		ApplicationServiceBean.init(g0, givenProcessorList0, compMartrix, commMartrix, 0d, 90d); // 69
		new HEFTScheduler().scheduling(g0, DeepCopyUtil.copy(givenProcessorList0)); // 80

		// set maxR and minR
		//计算任务的最小和最大执行成本
		double appCostMin = 0;
		double appCostMax = 0;

		for (Task currentTask : g0.getScheduledSequence()) {

			Double taskMinRC = Double.MAX_VALUE;
			Double taskMaxRC = 0d;

			for (int i = 1; i < givenProcessorList0.size(); i++) {
				Processor p = givenProcessorList0.get(i);

				double vmin = currentTask.getProcessor$CompCostMap().get(p) * p.getPrice();

				if (vmin < taskMinRC) {
					taskMinRC = vmin;

				}
				double vmax = currentTask.getProcessor$CompCostMap().get(p) * p.getPrice() * p.getF_max() / p.getF_ee();
				if (vmax > taskMaxRC) {
					taskMaxRC = vmax;

				}
			}
			currentTask.setRMin(taskMinRC);
			currentTask.setRMax(taskMaxRC);
			appCostMin = appCostMin + taskMinRC;
			appCostMax = appCostMax + taskMaxRC;
		}


 		// 1. 构建任务级别
               List<Level> levels = Level.buildTaskLevels(g0);

        // 2. 计算每层完成时间 & 总完成时间 sigma
		double sigma = 0;
		for (Level level : levels) {
			level.calculateLevelCompletionTime(g0, g0.getScheduledSequence());
			sigma += level.getLevelCompletionTime(); // 每层任务完成时间之和
		}



double totalConsumedBudget = 0d; // 全局累计已分配预算
double remainingBudget = 0d;     // 上一层剩余预算

for (int k = 0; k < levels.size(); k++) { 
    Level level = levels.get(k);
    double LCk = level.getLevelCompletionTime();

    // 当前层级预算 = 上一层剩余 + 本层预算
    double levelBudget = remainingBudget + (LCk * appCostBudget / sigma);
    double levelConsumedBudget = 0d; // 每层已消耗预算

    // 1️⃣ 获取该层 ready task（所有前置任务已完成），该level内所有任务均为ready task
    List<Task> readyTasks = new ArrayList<>();
	for (Task task : level.getTasks()) {
		readyTasks.add(task);
	}


    // 2️⃣ 按 Lower_bound_ft()升序排序 ready task 最早完成时间
    readyTasks.sort(Comparator.comparingDouble(Task::getLower_bound_ft));

    // 3️⃣ 遍历 ready task 进行预算分配和处理器选择
    for (Task currentTask : readyTasks) {
        // 任务原计划预算 = 当前层剩余预算
        double taskArrangedBudget = levelBudget - levelConsumedBudget;

        // 限制任务预算在 RMin / RMax 范围内
        double taskCostBudget = Math.min(Math.max(taskArrangedBudget, currentTask.getRMin()), currentTask.getRMax());

        double actualST = Double.MAX_VALUE;
        double actualFT = Double.MAX_VALUE;
        double actualCost = 0d;
        double actualEnergy = Double.MAX_VALUE;
        double actualF = 0d;
        Processor actualProcessor = null;

        // 遍历处理器寻找最优能耗方案
        for (int ii = 1; ii < givenProcessorList0.size(); ii++) {
            Processor p = givenProcessorList0.get(ii);
            double selectedF = 0d;

            for (double f = p.getF_ee(); f <= p.getF_max(); f += 0.01) {
                f = DoubleUtil.floor4(f);
                double cost = currentTask.getProcessor$CompCostMap().get(p) * p.getPrice() * p.getF_max() / f;

                if (cost <= taskCostBudget) {
                    selectedF = f;
                    double energy = EnergyService.getEnergy(currentTask, p, selectedF);

                    if (energy < actualEnergy) {
                        actualEnergy = energy;
                        actualCost = cost;
                        actualProcessor = p;
                        actualF = selectedF;
                        break;
                    }
                }
            }
        }

        // 计算 EST / EFT
        ESTEFT esteft = TaskServiceBean.computeESTEFT(currentTask, actualProcessor, "s", actualF);
        actualST = esteft.getEst();
        actualFT = esteft.getEft();

        // 更新处理器任务映射
        StartEndTime startEndTime = new StartEndTime(actualST, actualFT);
        actualProcessor.getTask$startEndTimeMap().put(currentTask, startEndTime);
        actualProcessor.getStartEndTime$TaskMap().put(startEndTime, currentTask);
        actualProcessor.getTask$availTimeMap().put(currentTask, actualFT);

        // 更新任务属性
        currentTask.setAssignedprocessor1(actualProcessor);
        currentTask.setLower_bound_st(actualST);
        currentTask.setLower_bound_ft(actualFT);
        currentTask.setAR(actualCost);
        currentTask.setAE(actualEnergy);
        currentTask.setF(actualF);

        // 打印日志
        if (print != null && print[0]) {
            System.out.println("Task: " + currentTask.getName() 
                    + ", Arranged Budget: " + DoubleUtil.format4(taskArrangedBudget)
                    + ", Actual Cost: " + DoubleUtil.format4(actualCost)
                    + ", VM: " + actualProcessor
                    + ", Freq: " + DoubleUtil.format4(actualF) 
                    + ", ST: " + DoubleUtil.format(actualST) 
                    + ", FT: " + DoubleUtil.format(actualFT)
                    + ", Energy: " + DoubleUtil.format4(actualEnergy));
        }

        // System.out.println("Level ID: " + level.getLevelId() 
        //         + ", Task: " + currentTask.getName()
        //         + ", Level Budget: " + DoubleUtil.format4(levelBudget)
        //         + ", Task Arranged Budget: " + DoubleUtil.format4(taskArrangedBudget)
        //         + ", Task Cost Budget: " + DoubleUtil.format4(actualCost));

        levelConsumedBudget += actualCost;
    }

    // 更新剩余预算 = 当前层剩余预算
    remainingBudget = levelBudget - levelConsumedBudget;
    totalConsumedBudget += levelConsumedBudget;
}



		double totalCost = 0;
		double sl = 0;
		double totalEnergy =0;
		for (int i = 0; i < g0.getScheduledSequence().size(); i++) {

			Task currentTask = g0.getScheduledSequence().get(i);

			totalCost = totalCost + currentTask.getAR();
			double energy = currentTask.getAE();
		
			totalEnergy = totalEnergy + energy;
			if(currentTask.getLower_bound_ft()>sl)
				sl = currentTask.getLower_bound_ft();

		}

		System.out.println("BDCE's  cost:" + DoubleUtil.format4(totalCost));
		System.out.println("BDCE's  energy:" + DoubleUtil.format4(totalEnergy));
		System.out.println("BDCE's  schedule length:" + DoubleUtil.format(sl));
		System.out.println("");
		
		g0.setTotalCost(totalCost);
		g0.setTotalE(totalEnergy);
		g0.setLower_bound(sl);

		return g0;
	}

}
