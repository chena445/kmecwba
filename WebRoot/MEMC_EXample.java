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
public class MEMC_EXample {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		
		for(int i =1;i<=1;i++){
			
			test() ;
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
		
		
		
		Integer task_number = 100; // +15

		int cost_lower = 100;

		int cost_upper = 300;
		
		task_number = 15 +new Random().nextInt(task_number);

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

		List<Integer> cijs = new ArrayList<Integer>();
		for (int i = 0; i < task_number; i++) {
			for (int j = 0; j < task_number; j++) {

				if (commMartrix[i][j] > 0)
					cijs.add(commMartrix[i][j]);

			}
		}
		
		
		Application g0 = new Application("G", Criticality.S3);
		ApplicationServiceBean.init(g0, givenProcessorList, compMartrix, commMartrix, 0d, 90d); // 69

		new HEFTScheduler().scheduling(g0, DeepCopyUtil.copy(givenProcessorList)); // 80

		double energy = 0d;
		for (Task currentTask : g0.getScheduledSequence()) {

		
			double energyTask = EnergyService.getEnergy(currentTask.getAssignedprocessor1(), 1, currentTask.getLower_bound_st(), currentTask.getLower_bound_ft());
			energy =energy +energyTask;
		}
		System.out.println();
		TOTAL_ENERGY total1 = null ;
		//出口任务的span伸缩性调整，加一个span-exit
		
		System.out.println("HEFT算法："+g0.getLower_bound()+" energy:"+energy);
		int deadline_span = (int)(g0.getLower_bound()*0.2);
		
		
		double deadline = deadline_span + g0.getLower_bound();
		
		
		System.out.println("deadline_span:"+deadline_span +" deadline:"+(g0.getLower_bound()+deadline_span));
		total1= MEMC(DeepCopyUtil.copy(givenProcessorList), g0.getScheduledSequence(),g0.getLower_bound(),(double)deadline_span,(double)deadline_span,(double)(deadline));
		System.out.println("本地：DSDEC算法,makespan="+total1.getMakespan()+"  energy="+total1.getEnergy());
		
		
		if(total1.getMakespan()<=g0.getLower_bound()+deadline_span){
			
			int count =1;
			Double lowerbound0 = g0.getEntryTask().getLower_bound_ft();
			Double max_span = deadline- lowerbound0;
			System.out.println("验证次数："+(max_span-deadline_span-1));
			for(int ver_span =deadline_span+1;ver_span<max_span.intValue();ver_span++){
				
				total1= MEMC(DeepCopyUtil.copy(givenProcessorList), g0.getScheduledSequence(),g0.getLower_bound(),(double)ver_span,(double)deadline_span,(double)(deadline));
				System.out.println(count+"往上走ISDEC算法：ver_span="+ver_span+" makespan"+total1.getMakespan()+"  energy="+total1.getEnergy());
				count++;
				if(total1.getMakespan()>g0.getLower_bound()+deadline_span){
					System.out.println("makespan越界");
					System.out.println("makespan:"+total1.getMakespan());
					break ;
				
					
				}
			}
			
		}
		
		
		
		
		
		
		
		
		//System.out.println("方法0：makespan"+g0.getLower_bound()+"   energy="+energy);
		//System.out.println("方法1：makespan"+total1.getMakespan()+"  energy="+total1.getEnergy());
		//System.out.println("方法2：makespan"+total2.getMakespan()+"  energy="+total2.getEnergy());
	}

	
	
	public static TOTAL_ENERGY MEMC(List<Processor> givenProcessorList, List<Task> sequences, Double g_makespan, Double verfySpan, Double deadlinespan,Double deadline) throws IOException, ClassNotFoundException {
		
		Double totalEnergy = 0d;
		
		Double makespan = 0d;
	
		
		for (int i = 0; i < sequences.size(); i++) {

			Task currentTask = sequences.get(i);
			
			if(currentTask.getIsExit())
				currentTask.setDeadline(currentTask.getLower_bound_ft()+deadlinespan);
				//currentTask.setDeadline(currentTask.getLower_bound_ft()+verfySpan);
			else
				currentTask.setDeadline(currentTask.getLower_bound_ft()+verfySpan);
			
			if(currentTask.getDeadline()>deadline)
				currentTask.setDeadline(deadline);
			
		}
		
		
		
		
		for (int i = 0; i < sequences.size(); i++) {

			Task currentTask = sequences.get(i);
			//System.out.println("按时间约束调度的任务：" + currentTask.getName());


			
			// 寻找最早完成时间的处理器
			List<PEFESTEFT3> pefestefts = new ArrayList<PEFESTEFT3>();
			for (int ii = 1; ii < givenProcessorList.size(); ii++) {

				Processor p = givenProcessorList.get(ii);

				for (double f = 1.0; f >= p.getF_ee(); f = f - 0.01) {

					
					
					f = Math.round(f * 100) / 100.0;
					//System.out.println("f:"+f);
					
					ESTEFT esteft = TaskServiceBean.computeESTEFT(currentTask, p, "s", f);
					
					
					//System.out.println("esteft:"+esteft);

					double energy = EnergyService.getEnergy(p, f, esteft.getEst(), esteft.getEft());

					PEFESTEFT3 pesesteft = new PEFESTEFT3(p, energy, f, esteft.getEst(), esteft.getEft());
					pefestefts.add(pesesteft);
				}

			}
			Collections.sort(pefestefts);
			
			PEFESTEFT3 selected_pefesteft = null;
			for (PEFESTEFT3 pefesteft : pefestefts) {

				
				if (pefesteft.getEft()<= currentTask.getDeadline()) {
					
					selected_pefesteft = pefesteft;
					break;

				}

			}
			if(selected_pefesteft == null){
				System.out.println("找不到这样的处理器");
				return null ;
			}
			

			currentTask.setDownward_frequency(selected_pefesteft.getFrequency());
			currentTask.setLower_bound_st(selected_pefesteft.getEst());
			currentTask.setLower_bound_ft(selected_pefesteft.getEft());
			currentTask.setAssignedprocessor1(selected_pefesteft.getProcessor());
			currentTask.setPre_energy(selected_pefesteft.getEnergy());
			
			totalEnergy = totalEnergy+ currentTask.getPre_energy();
			
			makespan = currentTask.getLower_bound_ft();

		}
		System.out.println();
		TOTAL_ENERGY total = new TOTAL_ENERGY();
		total.setMakespan(makespan);
		total.setEnergy(totalEnergy);
		return total;
	}

	
	
	
	
	
	
	
}
