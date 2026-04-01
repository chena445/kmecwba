package cn.edu.hnu.esnl.app.yuekunchen.cost.access.exp;


import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import cn.edu.hnu.esnl.app.yuekunchen.cost.access.alg.BDCE;
import cn.edu.hnu.esnl.app.yuekunchen.cost.access.alg.DYNAMIC12;
import cn.edu.hnu.esnl.app.yuekunchen.cost.access.alg.ECS;
import cn.edu.hnu.esnl.app.yuekunchen.cost.access.alg.ECWS2025;
import cn.edu.hnu.esnl.app.yuekunchen.cost.access.alg.EHBCS;
import cn.edu.hnu.esnl.app.yuekunchen.cost.access.alg.KMECWBA;
import cn.edu.hnu.esnl.app.yuekunchen.cost.access.alg.MECABP;
import cn.edu.hnu.esnl.app.yuekunchen.cost.access.alg.MECBL;
import cn.edu.hnu.esnl.app.yuekunchen.cost.access.alg.alg1;
import cn.edu.hnu.esnl.app.yuekunchen.cost.access.alg.alg1_K8;
import cn.edu.hnu.esnl.app.yuekunchen.cost.access.alg.alg1_K4;
import cn.edu.hnu.esnl.app.yuekunchen.cost.access.alg.alg1_K12;
import cn.edu.hnu.esnl.app.yuekunchen.cost.access.alg.alg1_K16;
import cn.edu.hnu.esnl.app.yuekunchen.cost.access.alg.alg1_K20;
import cn.edu.hnu.esnl.app.yuekunchen.cost.access.alg.alg2;
import cn.edu.hnu.esnl.app.yuekunchen.cost.access.alg.MECWS_AET_I;
import cn.edu.hnu.esnl.app.yuekunchen.cost.access.alg.MECWBA;
import cn.edu.hnu.esnl.app.yuekunchen.cost.access.alg.MECWS_AET_II_k;
import cn.edu.hnu.esnl.app.yuekunchen.cost.access.alg.MECWS_BDAS;
import cn.edu.hnu.esnl.app.yuekunchen.cost.access.alg.MECWS_DERG;
import cn.edu.hnu.esnl.app.yuekunchen.cost.access.alg.MECWS_DynamicAdjust;
import cn.edu.hnu.esnl.app.yuekunchen.cost.access.alg.MECWS_NDERG;
import cn.edu.hnu.esnl.app.yuekunchen.cost.access.alg.MECWS_REWS;
import cn.edu.hnu.esnl.app.yuekunchen.cost.access.alg.MECWS_REWS2023;
import cn.edu.hnu.esnl.app.yuekunchen.cost.access.alg.Method_1;
import cn.edu.hnu.esnl.app.yuekunchen.cost.access.alg.Method_3;
import cn.edu.hnu.esnl.app.yuekunchen.cost.access.alg.Method_5;
import cn.edu.hnu.esnl.app.yuekunchen.cost.access.alg.Method_6;
import cn.edu.hnu.esnl.app.yuekunchen.cost.access.alg.Method_ave;
import cn.edu.hnu.esnl.app.yuekunchen.cost.access.alg.Method_max;
import cn.edu.hnu.esnl.app.yuekunchen.cost.access.alg.OrderedCluster;
import cn.edu.hnu.esnl.app.yuekunchen.cost.access.alg.RECFPAB2;
import cn.edu.hnu.esnl.app.yuekunchen.cost.access.alg.test1;
import cn.edu.hnu.esnl.bean.Application;
import cn.edu.hnu.esnl.bean.Criticality;
import cn.edu.hnu.esnl.bean.Processor;
import cn.edu.hnu.esnl.bean.Task;
import cn.edu.hnu.esnl.bean.view.CompCommMartrixBean;
import cn.edu.hnu.esnl.bean.view.ESTEFT;
import cn.edu.hnu.esnl.bean.view.PEFESTEFT;
import cn.edu.hnu.esnl.bean.view.PEFESTEFT2;
import cn.edu.hnu.esnl.bean.view.PEFESTEFT3;
import cn.edu.hnu.esnl.bean.view.StartEndTime;
import cn.edu.hnu.esnl.bean.view.TOTAL_ENERGY;
import cn.edu.hnu.esnl.realgraph.FFTGenrattor;
import cn.edu.hnu.esnl.realgraph.GEGenrattor;
import cn.edu.hnu.esnl.schedule.assitant.HEFTScheduler;
import cn.edu.hnu.esnl.schedule.assitant.RSScheduler;
import cn.edu.hnu.esnl.scheduler.*;
import cn.edu.hnu.esnl.service.ApplicationServiceBean;
import cn.edu.hnu.esnl.service.JSAPerformanceService;
import cn.edu.hnu.esnl.service.MMPerformanceService;
import cn.edu.hnu.esnl.service.PerformanceService;
import cn.edu.hnu.esnl.service.TaskServiceBean;
import cn.edu.hnu.esnl.service.energy.EnergyService;
import cn.edu.hnu.esnl.service.graph.FFT;
import cn.edu.hnu.esnl.service.graph.Gaussian;
import cn.edu.hnu.esnl.service.reliability.ReliabilityModelService;
import cn.edu.hnu.esnl.util.DeepCopyUtil;
import cn.edu.hnu.esnl.util.DoubleUtil;

/**
 * @author xgq E-mail:xgqman@126.com
 * 
 * 
 */
public class EX2_GE {
	private static double  appCostBudget =80;
	//private static double  lambda=1;
	private static int processor_number=20;
	//Integer processor_number = 64;

	public static void main(String[] args) throws IOException, ClassNotFoundException {

		for (int i = 1; i <= 5; i++) {

			//test(14*i);//工作流任务因子14、42、70
			test(14,i);
			//appCostBudget=appCostBudget+10;
			//RGoalG=RGoalG+0.01;
			processor_number=processor_number+20;
		}

	}

	public static void test(int index,int lambda) throws IOException, ClassNotFoundException {
//设置虚拟机参数//
		List<Processor> givenProcessorList = new ArrayList<Processor>();

		
		for (int i = 0; i < processor_number+1; i++) {
			
			double p_ind = 0.03 + 0.01 * new Random().nextInt(5); // 0.05
			double f_max = 1.0; // 0.05
			double c_ef = 0.8 + 0.1 * new Random().nextInt(5); // =1
			double a = 2.5 + 0.1 * new Random().nextInt(6);

			double precision = 0.01;
			double p_s = 0.005d; // ;+ 0.1 * new Random().nextInt(5);
			Processor p = new Processor("p_" + i, f_max, p_s, p_ind, c_ef, a, precision);
	
			p.setPrice(0.001 * (95+ new Random().nextInt(286)));
			givenProcessorList.add(p);

		}  //创建虚拟机

		int cost_lower = 1;
		int cost_upper = 128;
//调用高斯结构工作流//
		CompCommMartrixBean compCommMartrix = GEGenrattor.exe(index, cost_lower, cost_upper, processor_number);//GE的任务数量

		Integer[][] compMartrix = compCommMartrix.getCompMartrix();
		Integer[][] commMartrix = compCommMartrix.getCommMartrix();

		List<Processor> givenProcessorList0 = DeepCopyUtil.copy(givenProcessorList);
		Application g0 = new Application("F_1", Criticality.S3);
		ApplicationServiceBean.init(g0, givenProcessorList0, compMartrix, commMartrix, 0d, 90d); // 69
		new HEFTScheduler().scheduling(g0, DeepCopyUtil.copy(givenProcessorList0)); // 80
		double appHEFTCost = 0;
		System.out.println(g0.getLower_bound());
		for (Task currentTask : g0.getScheduledSequence()) {

			appHEFTCost += currentTask.getProcessor$CompCostMap().get(currentTask.getAssignedprocessor1()) * currentTask.getAssignedprocessor1().getPrice();

		}
		// set maxR and minR
		// set maxR and minR
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
			//System.out.println(currentTask.getName());
		
				//System.out.println(currentTask.getName() + "当前的minRC是： " + taskMinRC);

			
			currentTask.setRMin(taskMinRC);
			currentTask.setRMax(taskMaxRC);

		}

		double appMinCost = 0;
		double appMaxCost = 0;
		for (Task currentTask : g0.getScheduledSequence()) {
			appMinCost += currentTask.getRMin();
			appMaxCost += currentTask.getRMax();
		}

	//	appCostBudget=appMinCost;
		//for (int i = 0; i <= 0; i++) {

			//double appCostBudget =appMinCost*(1+0.2*lambda);//不同预算时进行实验
		
			double appCostBudget =appMinCost*1.5;//不同虚拟机数量的实验
			double ratio=appCostBudget/appMinCost;
			
			System.out.println("appMinCost:" + appMinCost + "  appMaxCost:" + appMaxCost + " appCostBudget:" + appCostBudget);
			System.out.println(" ratio:" +  ratio+"processor_number="+processor_number);
			System.out.println("================================================");
			// EHBCS.exe(givenProcessorList, compMartrix, commMartrix, appCostBudget, false);//2018
		    // MECBL.exe(givenProcessorList, compMartrix, commMartrix, appCostBudget, false);	//2018
			// MECABP.exe(givenProcessorList, compMartrix, commMartrix, appCostBudget, false);	//论文算法2018
			 MECWS_REWS.exe(givenProcessorList, compMartrix, commMartrix, appCostBudget, false);	  //REWS2023
			 MECWS_DERG.exe(givenProcessorList, compMartrix, commMartrix, appCostBudget, false);	 //不带补偿的考虑平均执行时间法1//2019
			 MECWS_NDERG.exe(givenProcessorList, compMartrix, commMartrix, appCostBudget, false);    //AApro带补偿的//2019
			 MECWS_BDAS.exe(givenProcessorList, compMartrix, commMartrix, appCostBudget, false);    //AApro带补偿的//2019
			 BDCE.exe(givenProcessorList, compMartrix, commMartrix, appCostBudget, false);	
			 System.out.println("以上是对比算法的实验结果--------------------------------------------------------------");
			
			 System.out.println(" 以下是提出的算法的实验结果--------------------------------------------------------------");
//			alg2.exe(givenProcessorList, compMartrix, commMartrix, appCostBudget, false);
		   // RECFPAB2.exe(givenProcessorList, compMartrix, commMartrix, appCostBudget, false);
    		MECWBA.exe(givenProcessorList, compMartrix, commMartrix, appCostBudget, false);	  //考虑平均执行时间法2
			// alg1_K4.exe(givenProcessorList, compMartrix, commMartrix, appCostBudget, false); 
			KMECWBA.exe(givenProcessorList, compMartrix, commMartrix, appCostBudget, false); 
			
			
			System.out.println("================================================");

	//	}

		System.out.println("========================================================================");

	}

}