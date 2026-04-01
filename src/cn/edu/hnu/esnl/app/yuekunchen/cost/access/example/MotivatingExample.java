package cn.edu.hnu.esnl.app.yuekunchen.cost.access.example;

import java.io.FileWriter;
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
import cn.edu.hnu.esnl.app.yuekunchen.cost.access.alg.EHBCS;
import cn.edu.hnu.esnl.app.yuekunchen.cost.access.alg.MECABP;
import cn.edu.hnu.esnl.app.yuekunchen.cost.access.alg.MECBL;
import cn.edu.hnu.esnl.app.yuekunchen.cost.access.alg.MECWBA;
import cn.edu.hnu.esnl.app.yuekunchen.cost.access.alg.MECWS_AET_II_k;
import cn.edu.hnu.esnl.app.yuekunchen.cost.access.alg.KMECWBA;
import cn.edu.hnu.esnl.app.yuekunchen.cost.access.alg.KMECWBA;
import cn.edu.hnu.esnl.app.yuekunchen.cost.access.alg.alg2;

import cn.edu.hnu.esnl.bean.Application;
import cn.edu.hnu.esnl.bean.Criticality;
import cn.edu.hnu.esnl.bean.Processor;
import cn.edu.hnu.esnl.bean.Task;
import cn.edu.hnu.esnl.schedule.assitant.HEFTScheduler;
import cn.edu.hnu.esnl.schedule.assitant.RSScheduler;
import cn.edu.hnu.esnl.scheduler.*;
import cn.edu.hnu.esnl.service.ApplicationServiceBean;
import cn.edu.hnu.esnl.util.DeepCopyUtil;

/**
 * @author xgq E-mail:xgqman@126.com
 * 
 * 
 */
public class MotivatingExample {

	public static void main(String[] args) throws IOException, ClassNotFoundException {

		

			test();
		

	}

	public static void test() throws IOException, ClassNotFoundException {

		List<Processor> givenProcessorList = new ArrayList<Processor>();

		Integer processor_number = 3;
		for (int i = 0; i < processor_number + 1; i++) {
			double price = 1d;
			double p_ind = 0.03 + 0.01 * new Random().nextInt(5); // 0.05
			double f_max = 1.0; // 0.05
			double c_ef = 0.8 + 0.1 * new Random().nextInt(5); // =1
			double a = 2.5 + 0.1 * new Random().nextInt(6);

			double precision = 0.01;
			double p_s = 0.005d; 
			if (i == 1) {

				p_ind = 0.03;
				c_ef = 0.8;
				a = 2.9;
				price = 3; 

			}
			if (i == 2) {

				p_ind = 0.04;
				c_ef = 0.7;
				a = 2.5;
				price =5;
			}
			if (i == 3) {

				p_ind = 0.07;
				c_ef = 1.0; // 1.5
				a = 2.5;
				price = 4;
			}

			Processor p = new Processor("u_" + i, f_max, p_s, p_ind, c_ef, a, precision);
			p.setPrice(price);
			System.out.println("vm:" + p.getName());
			System.out.println("p_ind:" + p_ind);
			System.out.println("c_ef:" + c_ef);
			System.out.println("a:" + a);
			System.out.println("fee:" + p.getF_ee());
			System.out.println("price:" +p.getPrice());
			System.out.println(" ");
			givenProcessorList.add(p);
		}

		Integer[][] compMartrix = new Integer[][] { { 0, 0, 0, 0 }, { 0, 14, 16, 9 }, { 0, 13, 19, 18 }, { 0, 11, 13, 19 }, { 0, 13, 8, 17 }, { 0, 12, 13, 10 }, { 0, 13, 16, 9 }, { 0, 7, 15, 11 },
				{ 0, 5, 11, 14 }, { 0, 18, 12, 20 }, { 0, 21, 7, 16 } };

		Integer[][] commMartrix = new Integer[][] { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 18, 12, 9, 11, 14, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 19, 16, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 23, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 27, 23, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 13, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 15, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 17 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 11 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 13 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };

		List<Processor> givenProcessorList0 = DeepCopyUtil.copy(givenProcessorList);
		Application g0 = new Application("G", Criticality.S3);
		ApplicationServiceBean.init(g0, givenProcessorList0, compMartrix, commMartrix, 0d, 90d); // 69
		new HEFTScheduler().scheduling(g0, DeepCopyUtil.copy(givenProcessorList0)); // 80


		double appHeftCost = 0;

		for (Task currentTask : g0.getScheduledSequence()) {

			appHeftCost += currentTask.getProcessor$CompCostMap().get(currentTask.getAssignedprocessor1()) * currentTask.getAssignedprocessor1().getPrice();

		}

		// set maxR and minR
		for (Task currentTask : g0.getScheduledSequence()) {

			
			Double taskMinRC = Double.MAX_VALUE;
			Double taskMaxRC = 0d;

			for (int i = 1; i < givenProcessorList0.size(); i++) {
				Processor p = givenProcessorList0.get(i);

				double vmin = currentTask.getProcessor$CompCostMap().get(p)*p.getPrice();
				
				if (vmin < taskMinRC) {
					taskMinRC = vmin;

				}
				double vmax = currentTask.getProcessor$CompCostMap().get(p)*p.getPrice()*p.getF_max()/p.getF_ee();
				if (vmax > taskMaxRC) {
					taskMaxRC =vmax;
					
				}
			}
			currentTask.setRMin(taskMinRC);
			currentTask.setRMax(taskMaxRC);
			
			
		}

		double appMinCost = 0;
		double appMaxCost = 0;
		for (Task currentTask : g0.getScheduledSequence()) {

			appMinCost += currentTask.getRMin();
			appMaxCost += currentTask.getRMax();
		}

	
		double appCostBudget = 1000; // <=heft

		System.out.println("appMinCost:" + appMinCost + "  appMaxCost:" + appMaxCost + "  appHeftCost:" + appHeftCost + " givenCost:" + appCostBudget);
		
		
		//EHBCS.exe(givenProcessorList, compMartrix, commMartrix, appCostBudget, false);
		//
		//MECBL.exe(givenProcessorList, compMartrix, commMartrix, appCostBudget, false);
	
		//MECABP.exe(givenProcessorList, compMartrix, commMartrix, appCostBudget, false);
         
		//AA.exe(givenProcessorList, compMartrix, commMartrix, appCostBudget, false);
        
		//MECWS_AET_II.exe(givenProcessorList, compMartrix, commMartrix, appCostBudget, false);
		//MECWS_AET_II_k.exe(givenProcessorList, compMartrix, commMartrix, appCostBudget, false);
		BDCE.exe(givenProcessorList, compMartrix, commMartrix, appCostBudget, false);
		MECWBA.exe(givenProcessorList, compMartrix, commMartrix, appCostBudget, false);
		KMECWBA.exe(givenProcessorList, compMartrix, commMartrix, appCostBudget, false);

	System.out.println("================================================");

	}

}
