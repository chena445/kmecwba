package cn.edu.hnu.esnl.a.enegy1;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
public class MMEC_EXample {

	public static void main(String[] args) throws IOException, ClassNotFoundException {

		List<Processor> givenProcessorList = new ArrayList<Processor>();
		for (int i = 0; i < 4; i++) {
			
			double p_ind = 0.01+0.01*new Random().nextInt(8); //0.05
			double f_max = 1.0+0.1*new Random().nextInt(16); //0.05
			double c_ef= 0.6+0.1*new Random().nextInt(10);   //1
			double a = 2.5+0.1*new Random().nextInt(10);
			Processor p = new Processor("u_" + i, f_max, 1.0, p_ind, c_ef, a);
			givenProcessorList.add(p);
		}

		Integer[][] compMartrix1 = new Integer[][] { { 0, 0, 0, 0 }, { 0, 14, 16, 9 }, { 0, 13, 19, 18 }, { 0, 11, 13, 19 }, { 0, 13, 8, 17 }, { 0, 12, 13, 10 }, { 0, 13, 16, 9 }, { 0, 7, 15, 11 },
				{ 0, 5, 11, 14 }, { 0, 18, 12, 20 }, { 0, 21, 7, 16 } };

		Integer[][] commMartrix1 = new Integer[][] { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 18, 12, 9, 11, 14, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 19, 16, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 23, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 27, 23, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 13, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 15, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 17 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 11 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 13 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };

		Application g0 = new Application("G", Criticality.S3);
		ApplicationServiceBean.init(g0, givenProcessorList, compMartrix1, commMartrix1, 0d, 90d); // 69

		new HEFTScheduler().scheduling(g0, DeepCopyUtil.copy(givenProcessorList)); // 80

		double energy = 0d;
		for (Task currentTask : g0.getScheduledSequence()) {

			// System.out.println("single DAG scheduling: name=" +
			// currentTask.getName() + ",rank_u= " + currentTask.getRanku() +
			// ",assigned_processor=" + currentTask.getAssignedprocessor1()
			// + ",lower_bound=" + currentTask.getLower_bound_st() + " " +
			// currentTask.getLower_bound_ft());
			double energyTask = EnergyService.getEnergy(currentTask.getAssignedprocessor1(), 1, currentTask.getLower_bound_st(), currentTask.getLower_bound_ft());
			energy =energy +energyTask;
		}
		
		
		TOTAL_ENERGY total1= MMEC1(DeepCopyUtil.copy(givenProcessorList), g0.getScheduledSequence());
	
		
		TOTAL_ENERGY total2= MMEC2(DeepCopyUtil.copy(givenProcessorList), g0.getScheduledSequence());
		
		System.out.println("方法0：makespan"+g0.getLower_bound()+"   energy="+energy);
		System.out.println("方法1：makespan"+total1.getMakespan()+"  energy="+total1.getEnergy());
		System.out.println("方法2：makespan"+total2.getMakespan()+"  energy="+total2.getEnergy());
	}

	
	
	public static TOTAL_ENERGY MMEC1(List<Processor> givenProcessorList, List<Task> sequences) throws IOException, ClassNotFoundException {
		
		Double totalEnergy = 0d;
		
		Double makespan = 0d;
		
		System.out.println("==========================================================");

		System.out.println("=====获得每个任务的能量下界======");
		double totalEnergyLowerbound = 0d;
		for (int i = 0; i < sequences.size(); i++) {

			Task currentTask = sequences.get(i);
			EnergyService.setLowerboundEnergyandProcessor(currentTask, givenProcessorList);

			System.out.println("=====每个任务的能量下界:" + currentTask.getName() + " " + currentTask.getLowerbound_energy() + " " + currentTask.getLowerbound_energy_processor());

			totalEnergyLowerbound += currentTask.getLowerbound_energy();
		}
		System.out.println("DAG总的能量下界：" + totalEnergyLowerbound);

		double maxEnergy = 114d;

		for (int i = 0; i < sequences.size(); i++) {

			Task currentTask = sequences.get(i);
			System.out.println("按能量优化调度的任务：" + currentTask.getName());

			// 获得他之间已经调度的任务的能量：
			double totalPreEnergy = 0d;
			for (int h = 0; h < i; h++) {
				Task preTask = sequences.get(h);
				//double pre_energy = EnergyService.getPreEnergy(preTask);
				//totalPreEnergy += pre_energy;
			}

			// 获得他之后未调度的任务的能量
			double totalPostEnergy = 0d;
			for (int j = i + 1; j < sequences.size(); j++) {
				Task postTask = sequences.get(j);

				totalPostEnergy += postTask.getLowerbound_energy();
			}

			double energy_span = maxEnergy - totalPreEnergy - totalPostEnergy;

			System.out.println("energy_span:" + energy_span);

			// 寻找最早完成时间的处理器
			List<PEFESTEFT> pefestefts = new ArrayList<PEFESTEFT>();
			for (int ii = 1; ii < givenProcessorList.size(); ii++) {

				Processor p = givenProcessorList.get(ii);

				for (double f = 1.0; f >= p.getF_ee(); f = f - 0.01) {

					f = Math.round(f * 100) / 100.0;

					ESTEFT esteft = TaskServiceBean.computeESTEFT(currentTask, p, "s", f);

					double energy = EnergyService.getEnergy(p, f, esteft.getEst(), esteft.getEft());

					PEFESTEFT pesesteft = new PEFESTEFT(p, energy, f, esteft.getEst(), esteft.getEft());
					pefestefts.add(pesesteft);
				}

			}
			Collections.sort(pefestefts);

			PEFESTEFT selected_pefesteft = null;
			for (PEFESTEFT pefesteft : pefestefts) {

				// System.out.println("pefesteft:" + pefesteft);
				if (pefesteft.getEnergy() <= energy_span) {
					// System.out.println("选定的pefesteft:" + pefesteft);
					selected_pefesteft = pefesteft;
					break;

				}

			}

			currentTask.setDownward_frequency(selected_pefesteft.getFrequency());
			//currentTask.setPre_energy_st(selected_pefesteft.getEst());
			//currentTask.setPre_energy_ft(selected_pefesteft.getEft());
			//currentTask.setPre_processor(selected_pefesteft.getProcessor());
			currentTask.setPre_energy(selected_pefesteft.getEnergy());
			
			totalEnergy += currentTask.getPre_energy();
			
			//makespan = currentTask.getPre_energy_ft();

		}
		
		TOTAL_ENERGY total = new TOTAL_ENERGY();
		total.setMakespan(makespan);
		total.setEnergy(totalEnergy);
		return total;
	}

	
	public static TOTAL_ENERGY MMEC2(List<Processor> givenProcessorList, List<Task> sequences) throws IOException, ClassNotFoundException {
		Double totalEnergy = 0d;
		
		Double makespan = 0d;
		System.out.println("==========================================================");

		System.out.println("=====获得每个任务的能量下界======");
		double totalEnergyLowerbound = 0d;
		for (int i = 0; i < sequences.size(); i++) {

			Task currentTask = sequences.get(i);
			EnergyService.setLowerboundEnergyandProcessor(currentTask, givenProcessorList);

			System.out.println("=====每个任务的能量下界:" + currentTask.getName() + " " + currentTask.getLowerbound_energy() + " " + currentTask.getLowerbound_energy_processor());

			totalEnergyLowerbound += currentTask.getLowerbound_energy();
		}
		System.out.println("DAG总的能量下界：" + totalEnergyLowerbound);

		double maxEnergy = 114d;

		for (int i = 0; i < sequences.size(); i++) {

			Task currentTask = sequences.get(i);
			System.out.println("按能量优化调度的任务：" + currentTask.getName());

			// 获得他之间已经调度的任务的能量：
			double totalPreEnergy = 0d;
			for (int h = 0; h < i; h++) {
				Task preTask = sequences.get(h);
				//double pre_energy = EnergyService.getPreEnergy(preTask);
				//totalPreEnergy += pre_energy;
			}

			// 获得他之后未调度的任务的能量
			double totalPostEnergy = 0d;
			for (int j = i + 1; j < sequences.size(); j++) {
				Task postTask = sequences.get(j);

				totalPostEnergy += postTask.getLowerbound_energy();
			}

			double energy_span = maxEnergy - totalPreEnergy - totalPostEnergy;

			System.out.println("energy_span:" + energy_span);

			// 寻找最早完成时间的处理器
			List<PEFESTEFT2> pefestefts2 = new ArrayList<PEFESTEFT2>();
			for (int ii = 1; ii < givenProcessorList.size(); ii++) {

				Processor p = givenProcessorList.get(ii);

				for (double f = 1.0; f >= p.getF_ee(); f = f - 0.01) {

					f = Math.round(f * 100) / 100.0;

					ESTEFT esteft = TaskServiceBean.computeESTEFT(currentTask, p, "s", f);

					double energy = EnergyService.getEnergy(p, f, esteft.getEst(), esteft.getEft());

					PEFESTEFT2 pesesteft2 = new PEFESTEFT2(p, energy, f, esteft.getEst(), esteft.getEft());
					pefestefts2.add(pesesteft2);
				}

			}
			Collections.sort(pefestefts2);

			PEFESTEFT2 selected_pefesteft = null;
			for (PEFESTEFT2 pefesteft2 : pefestefts2) {

				// System.out.println("pefesteft:" + pefesteft);
				if (pefesteft2.getEnergy() <= energy_span) {
					// System.out.println("选定的pefesteft:" + pefesteft);
					selected_pefesteft = pefesteft2;
					break;

				}

			}

			currentTask.setDownward_frequency(selected_pefesteft.getFrequency());
			//currentTask.setPre_energy_st(selected_pefesteft.getEst());
			//currentTask.setPre_energy_ft(selected_pefesteft.getEft());
			//currentTask.setPre_processor(selected_pefesteft.getProcessor());
			//currentTask.setPre_energy(selected_pefesteft.getEnergy());
			
			totalEnergy += currentTask.getPre_energy();
			
			//makespan = currentTask.getPre_energy_ft();
		}
		TOTAL_ENERGY total = new TOTAL_ENERGY();
		total.setMakespan(makespan);
		total.setEnergy(totalEnergy);
		
		return total;
		
	}

	
	
	
	
	
}
