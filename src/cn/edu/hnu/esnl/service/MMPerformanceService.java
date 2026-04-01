package cn.edu.hnu.esnl.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import cn.edu.hnu.esnl.bean.Application;
import cn.edu.hnu.esnl.bean.Criticality;
import cn.edu.hnu.esnl.bean.Processor;
import cn.edu.hnu.esnl.bean.Task;
import cn.edu.hnu.esnl.bean.view.StartEndTime;

/**
 * @author Guoqi Xie E-mail:xgqman@126.com
 * @version JDAS 5.0 Create time��Nov 26, 2015 1:58:05 AM
 */
public class MMPerformanceService {
	
	public static void performance(List<Application> gs,List<Integer> criticalities, List<Processor> givenProcessorList1) {

		// ��ƽ��
		for (Application g : gs) {

			if (g.getMakespan() == 0)
				continue;

			Double slowdown = (double) g.getLower_bound() / g.getMakespan();
			g.setSlowdown(slowdown);

			// System.out.println("getLower_bound:"+g.getLower_bound());
			// System.out.println("getMakespan:"+g.getMakespan());

		}
		double averageSlowdown = 0d;
		for (Application g : gs) {

			if (g.getMakespan() == 0)
				continue;

			averageSlowdown += g.getSlowdown();

		}
		averageSlowdown = averageSlowdown / gs.size();

		double unfairness = 0d;
		for (Application g : gs) {
			if (g.getMakespan() == 0)
				continue;

			unfairness += Math.abs(g.getSlowdown() - averageSlowdown);

		}

		System.out.println("unfairness:" + unfairness);

		// ������ECU utilization
		int usedTime = 0;
		int totalMakespanTime = 0;

		LinkedHashMap<Processor, Double> pis = new LinkedHashMap<Processor, Double>();

		for (Processor p : givenProcessorList1) {

			LinkedHashMap<Task, StartEndTime> task$startEndTimeMap = p.getTask$startEndTimeMap();

			Set<Task> tasks = task$startEndTimeMap.keySet();

			Double makespan = 0d;

			for (Task task : tasks) {

				usedTime += task.getMakespan_ft() - task.getMakespan_st();

				if (task.getMakespan_ft() > makespan)
					makespan = task.getMakespan_ft();

			}
			pis.put(p, makespan);
		}

		for (Processor p : givenProcessorList1) {

			totalMakespanTime += pis.get(p);
		}
		double per = (double) usedTime / totalMakespanTime;
		
		System.out.println("ECU utilization:" + per);

		// DMR
		
		LinkedHashMap<Integer,Double> criticality$dmr = new LinkedHashMap<Integer,Double>();
		
		for(Integer criticality: criticalities){
				int missNumber = 0;
		
				for (Application g : gs) {
					if (g.getCriticality() != criticality)
						continue;
		
					if (g.getMakespan() > (g.getRelativeDeadline() + g.getArrivalTime())) {
						missNumber++;
					}
		
				}
		
				int size = 0;
				for (Application g : gs) {
					if (g.getCriticality() != criticality)
						continue;
					size++;
		
				}
		
			//	System.out.println("size:" + size);
				//System.out.println("DMR:" + (double) missNumber / size);
				
				criticality$dmr.put(criticality, (double) missNumber / size);
				//System.out.println(criticality+" missNumber:"+missNumber );
		}
		
		for(Integer criticality: criticalities){
			
			//System.out.println("DMR of criticality " +criticality+" is "+criticality$dmr.get(criticality));
			
		}
		
		int missNumber = 0;
		int lowMissNumber = 0;
		int lowNumber = 0;
		int highMissNumber = 0;
		int highNumber = 0;
		for (int i = 1;i<=gs.size();i++) {
			
			Application g = gs.get(i-1);
			
			if(i<=gs.size()/2)
				lowNumber++;
			else
				highNumber++;
			
			if (g.getCriticality() ==Criticality.S0)
				continue;

			if (g.getMakespan() > (g.getRelativeDeadline() + g.getArrivalTime())) {
				
				missNumber++;
				
				if(i<=gs.size()/2)
					lowMissNumber++;
				else
					highMissNumber++;
				
				
			}

		}

		int safeNum = 0;
		Double overMakespan = 0d;
		for (Application g : gs) {
			//System.out.println(g.getName() +" makespan:" + g.getMakespan());
			if(g.getMakespan()>overMakespan)
				overMakespan = g.getMakespan();
			
			if (g.getCriticality()  ==Criticality.S0)
				continue;
			safeNum++;

		}
		System.out.println("overMakespan:" + overMakespan);
		System.out.println("size:" + safeNum);
		System.out.println("Overall DMR:" + (double) missNumber / safeNum);
		System.out.println("Low DMR:" + (double) lowMissNumber / lowNumber);
		System.out.println("High DMR:" + (double) highMissNumber / highNumber);
	}
	
	
	
	
}
