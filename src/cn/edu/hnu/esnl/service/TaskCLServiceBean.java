package cn.edu.hnu.esnl.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import cn.edu.hnu.esnl.bean.Application;
import cn.edu.hnu.esnl.bean.Criticality;
import cn.edu.hnu.esnl.bean.Processor;
import cn.edu.hnu.esnl.bean.Task;
import cn.edu.hnu.esnl.bean.view.ESTEFT;
import cn.edu.hnu.esnl.bean.view.PEFESTEFT;
import cn.edu.hnu.esnl.bean.view.StartEndTime;
import cn.edu.hnu.esnl.service.energy.EnergyService;
import cn.edu.hnu.esnl.system.SystemValue;
import cn.edu.hnu.esnl.util.DoubleUtil;

/**
 * @author xgq E-mail:xgqman@126.com
 * @version 创建时间：Feb 4, 2015 1:59:58 PM 类说明:
 * 
 */
public class TaskCLServiceBean {

	public static void taskAllocationLowerBound(Task currentTask, List<Processor> commomProcessorList, int cl) {
		// 在每个处理器上的值
		Double eft = Double.MAX_VALUE;
		Double ast = Double.MAX_VALUE;
		Double aft = Double.MAX_VALUE;
		Processor allocatedProcessor = null;

		for (int i = 1; i < commomProcessorList.size(); i++) {

			Processor p = commomProcessorList.get(i);
			ESTEFT esteft = computeESTEFT(currentTask, p, "s", 1); // 在处理器上的最早开始时间

			int cl_span = currentTask.getApplication().getCriticality() - cl;

			esteft.setEft(esteft.getEft() - cl_span * SystemValue.CL_BASE); // 设置低关键级

			Double estP = esteft.getEst();

			Double eftP = esteft.getEft();

			if (eftP <= eft) {
				allocatedProcessor = p;
				eft = eftP;
				ast = estP;
				aft = eftP;
			}

		}

		StartEndTime startEndTime = new StartEndTime(ast, aft);

		allocatedProcessor.getTask$startEndTimeMap().put(currentTask, startEndTime);
		allocatedProcessor.getStartEndTime$TaskMap().put(startEndTime, currentTask);
		allocatedProcessor.getTask$availTimeMap().put(currentTask, aft);
		currentTask.setAssignedprocessor1(allocatedProcessor);

		currentTask.setLower_bound_st(ast);
		currentTask.setLower_bound_ft(aft);
		if (cl == 0) {

			currentTask.setLower_bound_st_s0(ast);
			currentTask.setLower_bound_ft_s0(aft);

			if (SystemValue.isPrint)
				System.out.println("currentTask:" + currentTask.getName() + " " + currentTask.getAssignedprocessor1().getName() + " " + currentTask.getLower_bound_st() + " lowerbound:"
						+ currentTask.getLower_bound_ft_s0());
		} else if (cl == 1) {

			currentTask.setLower_bound_st_s1(ast);
			currentTask.setLower_bound_ft_s1(aft);
		} else if (cl == 2) {
			currentTask.setLower_bound_st_s2(ast);
			currentTask.setLower_bound_ft_s2(aft);
		} else if (cl == 3) {
			currentTask.setLower_bound_st_s3(ast);
			currentTask.setLower_bound_ft_s3(aft);

		}

		if (currentTask.getIsExit()) {

			if (cl == 0) {

				currentTask.getApplication().setLower_bound_s0(aft);
			} else if (cl == 1) {

				currentTask.getApplication().setLower_bound_s1(aft);
			} else if (cl == 2) {
				currentTask.getApplication().setLower_bound_s2(aft);
			} else if (cl == 3) {
				currentTask.getApplication().setLower_bound_s3(aft);
			}

		}

	}

	public static Processor taskAllocationDynamic(Task currentTask, List<Processor> commomProcessorList, Integer currentTime) {
		// 在每个处理器上的值
		Double eft = Double.MAX_VALUE;
		Double ast = Double.MAX_VALUE;
		Double aft = Double.MAX_VALUE;
		Processor allocatedProcessor = null;
        Double minEnergy = Double.MAX_VALUE;
        double assingedFrequency= 1d;
		for (int i = 1; i < commomProcessorList.size(); i++) {

			Processor p = commomProcessorList.get(i);

			for (double f = p.getF_max(); f >= p.getF_ee()-0.0000001; f = f - SystemValue.FREQUENCY_PRECISION) {

			
				ESTEFT esteft = computeESTEFT(currentTask, p, "m", f);

				int cl_span = currentTask.getApplication().getCriticality() - SystemValue.system_criticality;

				esteft.setEft(esteft.getEft() - cl_span * SystemValue.CL_BASE / f); // 设置低关键级

				
				if (SystemValue.system_criticality == 0 && esteft.getEft() <= (currentTask.getLower_bound_ft_s0() + currentTask.getApplication().getDeadlinespan())) {

					Double estP = esteft.getEst();

					Double eftP = esteft.getEft();

					

					double span = eftP - estP;
					
					double energy = EnergyService.getEnergy(currentTask,p, f);
					
					estP = Math.max(estP, currentTime); // 动态环境下
					eftP = estP + span;
					

				
					
					if (energy <= minEnergy) {
						allocatedProcessor = p;
						eft = eftP;
						ast = estP;
						aft = eftP;
						minEnergy = energy;
						assingedFrequency = f;
					}
				}

				if (SystemValue.system_criticality == 1 && esteft.getEft() <= (currentTask.getLower_bound_ft_s1() + currentTask.getApplication().getDeadlinespan())) {

					Double estP = esteft.getEst();

					Double eftP = esteft.getEft();

					double energy = EnergyService.getEnergy(currentTask,p, f);
					
					
					
					double span = eftP - estP;
					estP = Math.max(estP, currentTime); // 动态环境下
					eftP = estP + span;
					if (energy <= minEnergy) {
						allocatedProcessor = p;
						eft = eftP;
						ast = estP;
						aft = eftP;
						minEnergy = energy;
						assingedFrequency = f;
					}
				}

				if (SystemValue.system_criticality == 2 && esteft.getEft() <= (currentTask.getLower_bound_ft_s2() + currentTask.getApplication().getDeadlinespan())) {

					Double estP = esteft.getEst();

					Double eftP = esteft.getEft();
					double energy = EnergyService.getEnergy(currentTask,p, f);
					
					double span = eftP - estP;
					estP = Math.max(estP, currentTime); // 动态环境下
					eftP = estP + span;
					if (energy <= minEnergy) {
						allocatedProcessor = p;
						eft = eftP;
						ast = estP;
						aft = eftP;
						minEnergy = energy;
						assingedFrequency = f;
					}
				}

				if (SystemValue.system_criticality == 3 && esteft.getEft() <= (currentTask.getLower_bound_ft_s3() + currentTask.getApplication().getDeadlinespan())) {

					Double estP = esteft.getEst();

					Double eftP = esteft.getEft();
					double energy = EnergyService.getEnergy(currentTask,p, f);
					
					double span = eftP - estP;
					estP = Math.max(estP, currentTime); // 动态环境下
					eftP = estP + span;
					if (energy <= minEnergy) {
						allocatedProcessor = p;
						eft = eftP;
						ast = estP;
						aft = eftP;
						minEnergy = energy;
						assingedFrequency = f;
					}
				}

			}

		}

		if (allocatedProcessor == null) {

			for (int i = 1; i < commomProcessorList.size(); i++) {

				Processor p = commomProcessorList.get(i);

				double f = 1d;
				ESTEFT esteft = computeESTEFT(currentTask, p, "m", f);
				int cl_span = currentTask.getApplication().getCriticality() - SystemValue.system_criticality;

				esteft.setEft(esteft.getEft() - cl_span * SystemValue.CL_BASE / f); // 设置低关键级

				Double estP = esteft.getEst();
				Double eftP = esteft.getEft();

				double span = eftP - estP;
				estP = Math.max(estP, currentTime); // 动态环境下
				eftP = estP + span;
				if (eftP <= eft) {
					allocatedProcessor = p;
					eft = eftP;
					ast = estP;
					aft = eftP;
					assingedFrequency = f;
				}

			}
		}

		StartEndTime startEndTime = new StartEndTime(ast, aft);

		allocatedProcessor.getStartEndTime$TaskMap().put(startEndTime, currentTask);
		allocatedProcessor.getTask$startEndTimeMap().put(currentTask, startEndTime);
		allocatedProcessor.getTask$availTimeMap().put(currentTask, aft);
		currentTask.setAssignedprocessor2(allocatedProcessor);
		currentTask.setMakespan_st(ast);
		currentTask.setMakespan_ft(aft);
		currentTask.setF(assingedFrequency);
		// System.out.println("currentTask:"+currentTask
		// +"currentTask.getIsExit():"+currentTask.getIsExit() +" ");

		if (currentTask.getIsExit()) {

			currentTask.getApplication().setMakespan(aft); // 设置makesoan

		}
		return allocatedProcessor;

	}

	public static ESTEFT computeESTEFT(Task currentTask, Processor p, String mode, double f) {

		Double estP = 0d;
		if (currentTask.getIsEntry())
			estP = 0d;
		Set<Task> predTasks = currentTask.getPredTask$CommCostMap().keySet();

		Double maxST = 0d;
		for (Task predTask : predTasks) {

			if (mode.equals("s")) {
				Double predAft = predTask.getLower_bound_ft(); // 下届完成时间

				Integer cij = 0;
				if (!predTask.getAssignedprocessor1().equals(p)) {

					cij = currentTask.getPredTask$CommCostMap().get(predTask);
				}

				Double ST = predAft + cij;
				if (ST >= maxST)
					maxST = ST;
			}
			if (mode.equals("m")) {
				Double predAft = predTask.getMakespan_ft();
				Integer cij = 0;
				if (!predTask.getAssignedprocessor2().equals(p)) {

					cij = currentTask.getPredTask$CommCostMap().get(predTask);
				}
				Double maxPostP = predAft + cij;
				if (maxPostP >= maxST)
					maxST = maxPostP;
			}

		}

		// 这里要获得最大的availTime,应该是最小空闲时间

		LinkedHashMap<Task, Double> task$availTimeMap = p.getTask$availTimeMap();

		Set<Task> keySet = task$availTimeMap.keySet();
		List<Double> times = new ArrayList<Double>();
		times.add(0d);
		for (Task key : keySet) {
			if (key.getApplication().equals(currentTask.getApplication()))
				times.add(task$availTimeMap.get(key));
		}
		Collections.sort(times);
		estP = Math.max(times.get(times.size() - 1), maxST);

		Double eftP = insertionBasedPolicy(currentTask, p, estP, mode, f); // 插入法

		double length = currentTask.getProcessor$CompCostMap().get(p) / f;

		estP = eftP - length;

		ESTEFT esteft = new ESTEFT(estP, eftP);

		return esteft;
	}

	public static Double insertionBasedPolicy(Task currentTask, Processor p, Double estP, String mode, Double... f) {

		Set<StartEndTime> startEndTimeSet = new HashSet<StartEndTime>();

		Set<Task> taskSet = p.getTask$startEndTimeMap().keySet();

		for (Task task : taskSet) {
			startEndTimeSet.add(p.getTask$startEndTimeMap().get(task));
		}

		List<StartEndTime> sets = new ArrayList<StartEndTime>();
		for (StartEndTime set : startEndTimeSet) {
			sets.add(set);
		}
		Collections.sort(sets);

		List<StartEndTime> spans = new ArrayList<StartEndTime>();

		if (sets.size() > 0) {

			StartEndTime span = new StartEndTime(0d, sets.get(0).getStartTime());
			spans.add(span);

		}

		for (int k = 0; k < sets.size() - 1; k++) {

			StartEndTime set1 = sets.get(k);
			StartEndTime set2 = sets.get(k + 1);

			StartEndTime span = new StartEndTime(set1.getEndTime(), set2.getStartTime());
			spans.add(span);
		}

		if (sets.size() > 0) {

			StartEndTime span = new StartEndTime(sets.get(sets.size() - 1).getEndTime(), Double.MAX_VALUE);
			spans.add(span);

		}

		double length = currentTask.getProcessor$CompCostMap().get(p);

		if (f != null && f.length > 0) {

			length = length / f[0]; // 计算开销长度
		} else {

		}

		for (int i = 0; i < spans.size(); i++) {
			StartEndTime span = spans.get(i);

			if (span.getStartTime().intValue() == span.getEndTime().intValue())
				continue;

			if (span.getStartTime() <= estP && (estP + length <= span.getEndTime())) { // 找小于的区间

				return estP + length;
			}
			if (span.getStartTime() >= estP && (span.getStartTime() + length <= span.getEndTime())) { // 找小于的区间

				return span.getStartTime() + length;
			}

		}

		if (spans.size() > 0)

			return spans.get(spans.size() - 1).getStartTime() + length;
		else
			return estP + length;

	}

}
