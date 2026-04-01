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
public class TaskEnergyServiceBean {

	public static void taskAllocationLowerBound(Task currentTask, List<Processor> commomProcessorList) {
		// 在每个处理器上的值
		Double eft = Double.MAX_VALUE;
		Double ast = Double.MAX_VALUE;
		Double aft = Double.MAX_VALUE;
		Processor allocatedProcessor = null;

		for (int i = 1; i < commomProcessorList.size(); i++) {

			Processor p = commomProcessorList.get(i);
			ESTEFT esteft = computeESTEFT(currentTask, p, "s"); // 在处理器上的最早开始时间

			// System.out.println("estP"+estP+" currentTime:"+currentTime);

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
		if(SystemValue.isPrint)
	 System.out.println("currentTask:"+currentTask.getName() +currentTask.getAssignedprocessor1().getName()+" " +currentTask.getLower_bound_st()
+" lowerbound:"+currentTask.getLower_bound_ft());
		if (currentTask.getIsExit()) {

			currentTask.getApplication().setLower_bound(aft);

		}

	}

	public static void taskAllocationRS(Task currentTask, List<Processor> commomProcessorList) {
		// 在每个处理器上的值
		

		Processor _p = commomProcessorList.get(1);
	
		Double _f = 1.0;

		for (int i = 1; i < commomProcessorList.size(); i++) {
			Processor p = commomProcessorList.get(i);
			for (Double f = p.getF_ee(); f <= p.getF_max(); f = f + SystemValue.FREQUENCY_PRECISION) {
				
				double rs = computeRS(currentTask, p, f, _p, _f);

				double _rs = computeRS(currentTask, _p, _f, p, f); // 在处理器上的最早开始时间
				
				if ( rs>_rs ) {

					_p = p;
					_f = f;

				}

			}

		}
	
		
		ESTEFT esteft = computeESTEFT(currentTask, _p, "s", _f); // 在处理器上的最早开始时间

		double ast = esteft.getEst();
		double aft = esteft.getEft();
	
		StartEndTime startEndTime = new StartEndTime(ast, aft);

		_p.getTask$startEndTimeMap().put(currentTask, startEndTime);
		_p.getStartEndTime$TaskMap().put(startEndTime, currentTask);
		_p.getTask$availTimeMap().put(currentTask, aft);
		currentTask.setAssignedprocessor1(_p);
		currentTask.setLower_bound_st(ast);
		currentTask.setLower_bound_ft(aft);
		currentTask.setF(_f);
		//System.out.println("currentTask:"+currentTask.getName()
		//		 +" ast:"+currentTask.getLower_bound_st()  +" aft:"+currentTask.getLower_bound_ft() +"=f "+ _f +" p= "+ _p.getName());
		if (currentTask.getIsExit()) {

			currentTask.getApplication().setLower_bound(aft);

		}

	}

	public static double computeRS(Task currentTask, Processor p, Double f, Processor _p, Double _f) {

		
		Double ed = EnergyService.getEnergy(currentTask,p, f);

		
		Double _ed = EnergyService.getEnergy(currentTask,_p, _f);

		ESTEFT esteft = computeESTEFT(currentTask, p, "s", f);
		ESTEFT _esteft = computeESTEFT(currentTask, _p, "s", _f);

		double a1 = (ed - _ed) / ed;

		double a2 = (esteft.getEft() - _esteft.getEft()) ;

		double a3 = esteft.getEft()-Math.min(esteft.getEst(), _esteft.getEst());
		return -(a1 + a2/a3);
	}

	public static Processor taskAllocationDynamic(Task currentTask, List<Processor> commomProcessorList, Integer currentTime) {
		// 在每个处理器上的值
		Double eft = Double.MAX_VALUE;
		Double ast = Double.MAX_VALUE;
		Double aft = Double.MAX_VALUE;
		Processor allocatedProcessor = null;

		for (int i = 1; i < commomProcessorList.size(); i++) {

			Processor p = commomProcessorList.get(i);

			ESTEFT esteft = computeESTEFT(currentTask, p, "m"); // 在处理器上的最早开始时间

			// System.out.println("estP"+estP+" currentTime:"+currentTime);

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
			}

		}

		StartEndTime startEndTime = new StartEndTime(ast, aft);

		allocatedProcessor.getStartEndTime$TaskMap().put(startEndTime, currentTask);
		allocatedProcessor.getTask$startEndTimeMap().put(currentTask, startEndTime);
		allocatedProcessor.getTask$availTimeMap().put(currentTask, aft);
		currentTask.setAssignedprocessor2(allocatedProcessor);
		currentTask.setMakespan_st(ast);
		currentTask.setMakespan_ft(aft);

	
		if (currentTask.getIsExit()) {

			currentTask.getApplication().setMakespan(aft); // 设置makesoan

		}
		return allocatedProcessor;

	}

	public static ESTEFT computeESTEFT(Task currentTask, Processor p, String mode) {

		Double estP = 0d;
		if (currentTask.getIsEntry())
			estP = 0d;
		Set<Task> predTasks = currentTask.getPredTask$CommCostMap().keySet();

		Double maxST = 0d;
		for (Task predTask : predTasks) {

			if (mode.equals("s")) {
				Double predAft = predTask.getLower_bound_ft();
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

		Double eftP = insertionBasedPolicy(currentTask, p, estP, mode); // 插入法

		estP = eftP - currentTask.getProcessor$CompCostMap().get(p);

		ESTEFT esteft = new ESTEFT(estP, eftP);

		return esteft;
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

		estP = eftP - currentTask.getProcessor$CompCostMap().get(p) / f;

		ESTEFT esteft = new ESTEFT(estP, eftP);

		return esteft;
	}

	public static ESTEFT computeLSTLFT(Task currentTask, Processor p, String mode, double f) {

		Double estP = 0d;
		if (currentTask.getIsEntry())
			estP = 0d;
		Set<Task> predTasks = currentTask.getSuccTask$CommCostMap().keySet();

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

		estP = eftP - currentTask.getProcessor$CompCostMap().get(p) / f;

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
		Double length = 0d;
		if (f != null && f.length > 0) {
			// System.out.println(currentTask.getProcessor$CompCostMap());

			length = currentTask.getProcessor$CompCostMap().get(p) / f[0]; // 计算开销长度
		} else
			length = (double) currentTask.getProcessor$CompCostMap().get(p);
		/*
		 * if(currentTask.getName().equals("G_2.n_4")){
		 * System.out.println("================");
		 * System.out.println(p.getName()+" "+sets);
		 * System.out.println(p.getName()+" "+spans);
		 * System.out.println("length:"+length);
		 * System.out.println("estP:"+estP); }
		 */

		for (int i = 0; i < spans.size(); i++) {
			StartEndTime span = spans.get(i);

			if (span.getStartTime().intValue() == span.getEndTime().intValue())
				continue;

			if (span.getStartTime() <= estP && (estP + length <= span.getEndTime())) { // 找小于的区间
				/*
				 * if(currentTask.getName().equals("G24")){
				 * System.out.println("满足条件："+span.getStartTime()+"  " +
				 * span.getEndTime()); }
				 */
				return estP + length;
			}
			if (span.getStartTime() >= estP && (span.getStartTime() + length <= span.getEndTime())) { // 找小于的区间
				/*
				 * if(currentTask.getName().equals("G24")){
				 * System.out.println("满足条件："+span.getStartTime()+"  " +
				 * span.getEndTime()); }
				 */
				return span.getStartTime() + length;
			}

		}

		if (spans.size() > 0)

			return spans.get(spans.size() - 1).getStartTime() + length;
		else
			return estP + length;

	}

}
