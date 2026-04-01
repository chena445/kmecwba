package cn.edu.hnu.esnl.a.enegy1;

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



import cn.edu.hnu.esnl.bean.Application;
import cn.edu.hnu.esnl.bean.Criticality;
import cn.edu.hnu.esnl.bean.Processor;
import cn.edu.hnu.esnl.bean.Task;
import cn.edu.hnu.esnl.bean.view.ESTEFT;
import cn.edu.hnu.esnl.bean.view.PEFESTEFT;
import cn.edu.hnu.esnl.bean.view.PEFESTEFT2;
import cn.edu.hnu.esnl.bean.view.PEFESTEFT3;
import cn.edu.hnu.esnl.bean.view.StartEndTime;
import cn.edu.hnu.esnl.bean.view.TOTAL_ENERGY;
import cn.edu.hnu.esnl.schedule.assitant.HEFTScheduler;
import cn.edu.hnu.esnl.scheduler.*;
import cn.edu.hnu.esnl.service.ApplicationServiceBean;
import cn.edu.hnu.esnl.service.JSAPerformanceService;
import cn.edu.hnu.esnl.service.MMPerformanceService;
import cn.edu.hnu.esnl.service.PerformanceService;
import cn.edu.hnu.esnl.service.TaskServiceBean;
import cn.edu.hnu.esnl.service.enegery.EnergyService;
import cn.edu.hnu.esnl.util.DeepCopyUtil;

/**
 * @author xgq E-mail:xgqman@126.com
 * 
 * 
 */
public class 固定处理器MotivatingExample {

	public static void main(String[] args) throws IOException, ClassNotFoundException {

		for (int i = 1; i <= 100000; i++) {

			test();
			
		}

	}

	public static void test() throws IOException, ClassNotFoundException {

		List<Processor> givenProcessorList = new ArrayList<Processor>();

		Integer processor_number = 4;
		for (int i = 0; i < processor_number; i++) {
			double p_ind = 0.01+0.01*new Random().nextInt(8); //0.05
			double f_max = 1.0; //0.05
			double c_ef= 0.6+0.1*new Random().nextInt(10);   //1
			double a = 2.5+0.1*new Random().nextInt(10);
			Processor p = new Processor("u_" + i, f_max, 1.0, p_ind, c_ef, a);
			
			
			System.out.println("p:" + p.getName());
			System.out.println("p_ind:" + p_ind);
			System.out.println("c_ef:" + c_ef);
			System.out.println("a:" + a);
			System.out.println("fee:" + p.getF_ee());
			System.out.println(" ");
			givenProcessorList.add(p);
		}

		Integer[][] compMartrix = new Integer[][] { { 0, 0, 0, 0 }, { 0, 14, 16, 9 }, { 0, 13, 19, 18 }, { 0, 11, 13, 19 }, { 0, 13, 8, 17 }, { 0, 12, 13, 10 }, { 0, 13, 16, 9 }, { 0, 7, 15, 11 },
				{ 0, 5, 11, 14 }, { 0, 18, 12, 20 }, { 0, 21, 7, 16 } };

		Integer[][] commMartrix = new Integer[][] { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 18, 12, 9, 11, 14, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 19, 16, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 23, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 27, 23, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 13, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 15, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 17 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 11 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 13 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };

		Application g0 = new Application("G", Criticality.S3);
		ApplicationServiceBean.init(g0, givenProcessorList, compMartrix, commMartrix, 0d, 85d); // 69
		new HEFTScheduler().scheduling(g0, DeepCopyUtil.copy(givenProcessorList)); // 80
		double energy = 0d;
		for (Task currentTask : g0.getScheduledSequence()) {
			
			double energyTask = EnergyService.getEnergy(currentTask.getAssignedprocessor1(), 1, currentTask.getLower_bound_st(), currentTask.getLower_bound_ft());
			energy = energy + energyTask;
		}
		System.out.println();
		TOTAL_ENERGY total1 = null;
	

		System.out.println("HEFT算法调度结果：" + g0.getLower_bound() + " 消耗的总能量:" + energy);
		Double deadline = g0.getLower_bound()+10; 
		System.out.println("DAG的下界lowerbound=" + g0.getLower_bound() + "deadline=" + deadline);

		
		
		total1 = ME_SLC(0,DeepCopyUtil.copy(givenProcessorList), g0.getScheduledSequence(), g0.getLower_bound(), deadline);
		System.out.println("全局优化结果， 调度长度" + total1.getMakespan() + "  消耗的总能量=" + total1.getEnergy());
		
		
		Application g1 = new Application("G", Criticality.S3);
		ApplicationServiceBean.init(g1, givenProcessorList, compMartrix, commMartrix, 0d, 90d); // 69
		new HEFTScheduler().scheduling(g1, DeepCopyUtil.copy(givenProcessorList)); // 80
		total1 = ME_SLC2(DeepCopyUtil.copy(givenProcessorList), g1.getScheduledSequence(), g1.getLower_bound(), deadline);
		System.out.println("分区优化结果， 调度长度" + total1.getMakespan() + "  消耗的总能量=" + total1.getEnergy());
			
		
	
	}

	
	
	
	public static TOTAL_ENERGY ME_SLC(final int type ,List<Processor> givenProcessorList, List<Task> sequences, Double lowerbound, Double deadline) throws IOException, ClassNotFoundException {

		Double totalEnergy = 0d;
		Double makespan = 0d;

		Double deadline_span = deadline - lowerbound;
		for (int i = 0; i < sequences.size(); i++) {

			Task currentTask = sequences.get(i);

			if (currentTask.getIsExit())
				currentTask.setDeadline(currentTask.getLower_bound_ft() + deadline_span);
			// currentTask.setDeadline(currentTask.getLower_bound_ft()+verfySpan);
			else {
				currentTask.setDeadline(currentTask.getLower_bound_ft() + deadline_span);

			}

		}
		for (int i = 0; i < sequences.size(); i++) {

			Task currentTask = sequences.get(i);
			
			
			
			// System.out.println("按时间约束调度的任务：" + currentTask.getName());

			List<PEFESTEFT3> pefestefts = new ArrayList<PEFESTEFT3>();
			for (int ii = 1; ii < givenProcessorList.size(); ii++) {

				Processor p = givenProcessorList.get(ii);

				
				for (double f = p.getF_max(); f >= p.getF_ee(); f = f - 0.01) {

					f = Math.round(f * 100) / 100.0;
					
					
					ESTEFT esteft = TaskServiceBean.computeESTEFT(currentTask, p, "s", f);
					
					
					double energy = EnergyService.getEnergy(p, f, esteft.getEst(), esteft.getEft());

					PEFESTEFT3 pesesteft = new PEFESTEFT3(p, energy, f, esteft.getEst(), esteft.getEft());
					pefestefts.add(pesesteft);
				}

			}
			Collections.sort(pefestefts);

			PEFESTEFT3 selected_pefesteft = null;
			for (PEFESTEFT3 pefesteft : pefestefts) {

				if (pefesteft.getEft() <= currentTask.getDeadline()) {
					 
					 
					selected_pefesteft = pefesteft;
					break;

				}

			}

			if (selected_pefesteft == null) {
				System.out.println("出现不满足的情况:"+type);
				if(type==0)
					throw new RuntimeException("跳出队列"); ;
				Collections.sort(pefestefts, new Comparator<PEFESTEFT3>() {
					
					public int compare(PEFESTEFT3 arg0, PEFESTEFT3 arg1) {
						
						if(type==1){
						
							Double x1 = arg0.getEft();
							Double x2 = arg1.getEft();
							return x1.compareTo(x2);
						}
						else if(type==2){
							
							Double x1 = arg0.getEnergy();
							Double x2 = arg1.getEnergy();
							return x1.compareTo(x2);
						}
						else if(type==3){
							
							Double x1 = arg0.getEft()*arg0.getEnergy();
							Double x2 = arg1.getEft()*arg1.getEnergy();
							return x1.compareTo(x2);
						}
						else
							throw new RuntimeException("请设置参数");
					}
				});
				selected_pefesteft = pefestefts.get(0);

			}
			// System.out.println("");
			// System.out.println("task:"+currentTask.getName());
			// System.out.println("eft:"+selected_pefesteft.getEft());
			// System.out.println("deadline:"+currentTask.getDeadline());

			currentTask.setDownward_frequency(selected_pefesteft.getFrequency());
			currentTask.setLower_bound_st(selected_pefesteft.getEst());
			currentTask.setLower_bound_ft(selected_pefesteft.getEft());

			currentTask.setPre_energy(selected_pefesteft.getEnergy());

			StartEndTime startEndTime = new StartEndTime(selected_pefesteft.getEst(), selected_pefesteft.getEft());

			currentTask.setAssignedprocessor1(selected_pefesteft.getProcessor());
			
			selected_pefesteft.getProcessor().getTask$startEndTimeMap().put(currentTask, startEndTime);
			selected_pefesteft.getProcessor().getStartEndTime$TaskMap().put(startEndTime, currentTask);
			selected_pefesteft.getProcessor().getTask$availTimeMap().put(currentTask, selected_pefesteft.getEft());
			
			//System.out.println("任务分配结果："+currentTask.getName()+ "处理器："+currentTask.getAssignedprocessor1()+" ST:"+currentTask.getLower_bound_st()+" FT "+currentTask.getLower_bound_ft()+" deadline "+currentTask.getDeadline());

			totalEnergy = totalEnergy + currentTask.getPre_energy();

			makespan = currentTask.getLower_bound_ft();

		}

		TOTAL_ENERGY total = new TOTAL_ENERGY();
		total.setMakespan(makespan);
		total.setEnergy(totalEnergy);
		return total;
	}
	
	public static TOTAL_ENERGY ME_SLC2(List<Processor> givenProcessorList, List<Task> sequences, Double lowerbound, Double deadline) throws IOException, ClassNotFoundException {

		Double totalEnergy = 0d;
		Double makespan = 0d;

		Double deadline_span = deadline - lowerbound;
		for (int i = 0; i < sequences.size(); i++) {

			Task currentTask = sequences.get(i);

			if (currentTask.getIsExit())
				currentTask.setDeadline(currentTask.getLower_bound_ft() + deadline_span);
			// currentTask.setDeadline(currentTask.getLower_bound_ft()+verfySpan);
			else {
				currentTask.setDeadline(currentTask.getLower_bound_ft() + deadline_span);

			}

		}
		for (int i = 0; i < sequences.size(); i++) {

			Task currentTask = sequences.get(i);
			// System.out.println("按时间约束调度的任务：" + currentTask.getName());

			List<PEFESTEFT3> pefestefts = new ArrayList<PEFESTEFT3>();
			

				Processor p = currentTask.getAssignedprocessor1();
				
				p = givenProcessorList.get(givenProcessorList.indexOf(new Processor(p.getName())));
				
				for (double f = p.getF_max(); f >= p.getF_ee(); f = f - 0.01) {

					f = Math.round(f * 100) / 100.0;

					ESTEFT esteft = TaskServiceBean.computeESTEFT(currentTask, p, "s", f);

					double energy = EnergyService.getEnergy(p, f, esteft.getEst(), esteft.getEft());

					PEFESTEFT3 pesesteft = new PEFESTEFT3(p, energy, f, esteft.getEst(), esteft.getEft());
					pefestefts.add(pesesteft);
				}

			
			Collections.sort(pefestefts);

			PEFESTEFT3 selected_pefesteft = null;
			for (PEFESTEFT3 pefesteft : pefestefts) {

				if (pefesteft.getEft() <= currentTask.getDeadline()) {
					// System.out.println(pefestefts);
					/*
					 * System.out.println("=");
					 * System.out.println("currentTask:"+currentTask.getName());
					 * System.out.println("verfySpan:"+verfySpan);
					 * System.out.println("f:"+pefesteft.getFrequency()); System
					 * .out.println("pefesteft.getEst():"+pefesteft.getEst());
					 * System
					 * .out.println("pefesteft.getEft():"+pefesteft.getEft());
					 * System
					 * .out.println("currentTask.getAssignedprocessor1():"+
					 * currentTask.getAssignedprocessor1());
					 * System.out.println("currentTask.getDeadline():"
					 * +currentTask.getDeadline());
					 */
					selected_pefesteft = pefesteft;
					break;

				}

			}

		
			
			currentTask.setLower_bound_st(selected_pefesteft.getEst());
			currentTask.setLower_bound_ft(selected_pefesteft.getEft());
			currentTask.setDownward_frequency(selected_pefesteft.getFrequency());
			currentTask.setPre_energy(selected_pefesteft.getEnergy());

			StartEndTime startEndTime = new StartEndTime(selected_pefesteft.getEst(), selected_pefesteft.getEft());

			selected_pefesteft.getProcessor().getTask$startEndTimeMap().put(currentTask, startEndTime);
			selected_pefesteft.getProcessor().getStartEndTime$TaskMap().put(startEndTime, currentTask);
			selected_pefesteft.getProcessor().getTask$availTimeMap().put(currentTask, selected_pefesteft.getEft());
			
			totalEnergy = totalEnergy + currentTask.getPre_energy();

			makespan = currentTask.getLower_bound_ft();

		}

		TOTAL_ENERGY total = new TOTAL_ENERGY();
		total.setMakespan(makespan);
		total.setEnergy(totalEnergy);
		return total;
	}
	
}
