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
public class 固定处理器大例子 {

	public static void main(String[] args) throws IOException, ClassNotFoundException {

		for (int i = 1; i <= 1; i++) {

			test();
		}

	}

	public static void test() throws IOException, ClassNotFoundException {

		List<Processor> givenProcessorList = new ArrayList<Processor>();

		Integer processor_number = 100;
		for (int i = 0; i < processor_number; i++) {
			double p_ind = 0.01+0.01*new Random().nextInt(8); //0.05
			double f_max = 1.0+0.1*new Random().nextInt(16); //0.05
			double c_ef= 0.6+0.1*new Random().nextInt(10);   //1
			double a = 2.5+0.1*new Random().nextInt(10);
			Processor p = new Processor("u_" + i, f_max, 1.0, p_ind, c_ef, a);
			
			
			givenProcessorList.add(p);
		}
		
		
		
		Integer task_number = 400; // +15

		int cost_lower = 100;

		int cost_upper = 300;
		
		//task_number = 15 +new Random().nextInt(task_number);

		Integer[][] compMartrix = new Integer[task_number][processor_number];

		for (int i = 0; i < task_number; i++) {
			for (int j = 0; j < processor_number; j++) {

				compMartrix[i][j] = cost_lower + new Random().nextInt(cost_upper);

			}
		}

		Integer[][] commMartrix = new Integer[task_number][task_number];
		LinkedHashMap<Integer, Integer> ssMap = new LinkedHashMap<Integer, Integer>();

		for (int i = 0; i < task_number; i++) {
			for (int j = 0; j < task_number; j++) {

				commMartrix[i][j] = 0;
				
				

				if (i < j && (j - i <= 10)) {
					int r = new Random().nextInt(cost_upper);

					Integer v = ssMap.get(i);
					if (v == null) {
						commMartrix[i][j] = cost_lower + new Random().nextInt(cost_upper);
						ssMap.put(i, 1);
					} else {
						int num = ssMap.get(i);
						if (num < 3 && ((r % 3 == 0) || (j + 1 == task_number))) {

							commMartrix[i][j] = cost_lower + new Random().nextInt(cost_lower);
							ssMap.put(i, num + 1);
						} else {

							commMartrix[i][j] = 0;

						}
					}

				}

				if (i == task_number - 1) {
					commMartrix[i][j] = 0;
				}

			}
		}

		
		Application g0 = new Application("G", Criticality.S3);
		ApplicationServiceBean.init(g0, givenProcessorList, compMartrix, commMartrix, 0d, 90d); // 69
		new HEFTScheduler().scheduling(g0, DeepCopyUtil.copy(givenProcessorList)); // 80
		double energy = 0d;
		for (Task currentTask : g0.getScheduledSequence()) {

			double energyTask = EnergyService.getEnergy(currentTask.getAssignedprocessor1(), 1, currentTask.getLower_bound_st(), currentTask.getLower_bound_ft());
			energy = energy + energyTask;
		}
		System.out.println();
		TOTAL_ENERGY total1 = null;
	

		System.out.println("HEFT算法：" + g0.getLower_bound() + " energy:" + energy);
		Double deadline = g0.getLower_bound()+g0.getLower_bound()/40; 
		System.out.println("lowerbound:" + g0.getLower_bound() + " deadline:" + deadline);

		
		
		total1 = ME_SLC(3,DeepCopyUtil.copy(givenProcessorList), g0.getScheduledSequence(), g0.getLower_bound(), deadline);
		System.out.println("DSDEC算法调度结果EFT1,makespan=" + total1.getMakespan() + "  energy=" + total1.getEnergy());
		
		
		
		Application g1 = new Application("G", Criticality.S3);
		ApplicationServiceBean.init(g1, givenProcessorList, compMartrix, commMartrix, 0d, 90d); // 69
		new HEFTScheduler().scheduling(g1, DeepCopyUtil.copy(givenProcessorList)); // 80
		total1 = ME_SLC2(DeepCopyUtil.copy(givenProcessorList), g1.getScheduledSequence(), g1.getLower_bound(), deadline);
		System.out.println("DSDEC算法调度结果固定处理器,makespan=" + total1.getMakespan() + "  energy=" + total1.getEnergy());
			
		
		
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
				//System.out.println("出现不满足的情况:"+type);
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

		
			currentTask.setDownward_frequency(selected_pefesteft.getFrequency());
			currentTask.setLower_bound_st(selected_pefesteft.getEst());
			currentTask.setLower_bound_ft(selected_pefesteft.getEft());

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
