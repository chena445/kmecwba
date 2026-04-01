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

/**
 * @author xgq E-mail:xgqman@126.com
 * 
 * 
 */
public class MECABP {

	public static Application exe(List<Processor> givenProcessorList, Integer[][] compMartrix, Integer[][] commMartrix, double appCostBudget, Boolean... print) throws IOException,
			ClassNotFoundException {

		List<Processor> givenProcessorList0 = DeepCopyUtil.copy(givenProcessorList);
		Application g0 = new Application("F_1", Criticality.S3);
		ApplicationServiceBean.init(g0, givenProcessorList0, compMartrix, commMartrix, 0d, 90d); // 69
		new HEFTScheduler().scheduling(g0, DeepCopyUtil.copy(givenProcessorList0)); // 80

		// set maxR and minR
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

		for (int y = 0; y < g0.getScheduledSequence().size(); y++) {
			double costAvailG = appCostBudget-appCostMin;
			
			Task unTask = g0.getScheduledSequence().get(y);
			double costAvailT = unTask.getRMin()/appCostMin*costAvailG+unTask.getRMin();
			
			//System.out.println(unTask.getName()+" costAvailT:"+DoubleUtil.format4(costAvailT));
		}
	
		// scheduling tasks
		for (int y = 0; y < g0.getScheduledSequence().size(); y++) {

			Task currentTask = g0.getScheduledSequence().get(y);

			double appAssignedCost = 0;
			for (int x = 0; x < y; x++) {

				appAssignedCost = appAssignedCost + g0.getScheduledSequence().get(x).getAR();

			}

			double appUnassignedCost = 0d;
			for (int z = y + 1; z < g0.getScheduledSequence().size(); z++) {
			
				double costAvailG = appCostBudget-appCostMin;
				
				Task unTask = g0.getScheduledSequence().get(z);
				
				//double costAvailT = unTask.getRMin()/appCostMin*costAvailG+unTask.getRMin();
				
				double costAvailT = unTask.getRMin()*appCostBudget/appCostMin;
			
				
				appUnassignedCost = appUnassignedCost + costAvailT;

			}

			double taskCostBudget = appCostBudget - appAssignedCost - appUnassignedCost;

			

			if (taskCostBudget > currentTask.getRMax())
				taskCostBudget = currentTask.getRMax();
			
			if (taskCostBudget < currentTask.getRMin())
				taskCostBudget = currentTask.getRMin();

	
			double actualST = Double.MAX_VALUE;
			double actualFT = Double.MAX_VALUE;
			double actualCost = 0d;
			double actualEnergy = Double.MAX_VALUE;
			double actualF = 0.00;
			Processor actualProcessor = null;

			for (int ii = 1; ii < givenProcessorList0.size(); ii++) {

				Processor p = givenProcessorList0.get(ii);
				double selectef = 0.00;

				for (double f = p.getF_ee(); f <= p.getF_max(); f = f + 0.01) {

					f = DoubleUtil.floor4(f);

					double cost = currentTask.getProcessor$CompCostMap().get(p) * p.getPrice() * p.getF_max() / f;

					if (cost <= taskCostBudget) {
						selectef = f;
						double energy = EnergyService.getEnergy(currentTask, p, selectef);

						if (energy < actualEnergy) {
							actualEnergy = energy;
							actualCost = cost;
							actualProcessor = p;

							actualF = selectef;
							break;
						}

					}

				}

			}
			ESTEFT esteft = TaskServiceBean.computeESTEFT(currentTask, actualProcessor, "s", actualF); //
			actualST = esteft.getEst();
			actualFT = esteft.getEft();
			StartEndTime startEndTime = new StartEndTime(actualST, actualFT);

			actualProcessor.getTask$startEndTimeMap().put(currentTask, startEndTime);
			actualProcessor.getStartEndTime$TaskMap().put(startEndTime, currentTask);
			actualProcessor.getTask$availTimeMap().put(currentTask, actualFT);

			currentTask.setAssignedprocessor1(actualProcessor);
			currentTask.setLower_bound_st(actualST);
			currentTask.setLower_bound_ft(actualFT);

			currentTask.setAR(actualCost);
			currentTask.setAE(actualEnergy);
			currentTask.setF(actualF);
			

			if (print!=null&&print[0]==true) {
				System.out.println("task:" + currentTask.getName()+", cost constraint:"+DoubleUtil.format4(taskCostBudget) + ", VM:" + currentTask.getAssignedprocessor1() + ", frequency:" + currentTask.getF() + ", AST: "
						+ DoubleUtil.format(currentTask.getLower_bound_st()) + ", AFT: " + DoubleUtil.format(currentTask.getLower_bound_ft())  + ",  actual cost:" + DoubleUtil.format4(currentTask.getAR())+ ", final energy:"
								+ DoubleUtil.format4(currentTask.getAE()));
				System.out.println("=========");
			}
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

		System.out.println("MECABP's  cost:" + DoubleUtil.format4(totalCost));
		System.out.println("MECABP's  energy:" + DoubleUtil.format4(totalEnergy));
		System.out.println("MECABP's  schedule length:" + DoubleUtil.format(sl));
		System.out.println("");
		
		g0.setTotalCost(totalCost);
		g0.setTotalE(totalEnergy);
		g0.setLower_bound(sl);

		return g0;
	}

}
