package cn.edu.hnu.esnl.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import cn.edu.hnu.esnl.bean.Application;
import cn.edu.hnu.esnl.bean.Processor;
import cn.edu.hnu.esnl.bean.Task;
import cn.edu.hnu.esnl.bean.view.StartEndTime;
import cn.edu.hnu.esnl.service.energy.EnergyService;
import cn.edu.hnu.esnl.util.DeepCopyUtil;
import cn.edu.hnu.esnl.util.DoubleUtil;

/**
 * @author xgq 
 * E-mail:xgqman@126.com
 * @version ����ʱ�䣺Feb 4, 2015 1:38:09 PM
 * ��˵��:
 *
 */
public class ApplicationServiceBeanHC {
	
	public static void init(Application g,List<String> opendProcessorNames, List<Processor> commonProcessorList, Integer[][] computingCostMartrix, Integer[][] communicationCostMartrix,double arrivalTime,Double deadline, Double ... period) {
		g.setCommonProcessorList(commonProcessorList);
		g.setComps(computingCostMartrix);
		g.setComms(communicationCostMartrix);
		if(period!=null&&period.length>0)
			g.setPeriod(period[0]);
		
		g.setArrivalTime(arrivalTime);
		if(deadline!=null)
			g.setRelativeDeadline(deadline);
	
		for (int i = 0; i < computingCostMartrix.length; i++) {

			Task t = new Task(g.getName() + ".n_" + i);
			if (i == 1) {
				t.setIsEntry(true);
				g.getTaskPriorityQueue().in(t);
				g.setEntryTask(t);
			}
			if (i == computingCostMartrix.length - 1) {
				t.setIsExit(true);
				g.setExitTask(t);
			}

			g.getTaskList().add(t);
			g.getTaskOrigialList().add(t);
			t.setApplication(g);
		}

	
		for (int t = 1; t < g.getTaskList().size(); t++) {
			Task task = g.getTaskList().get(t);
			LinkedHashMap<Processor, Integer> processor$compCostsMap = new LinkedHashMap<Processor, Integer>();
			Double totalW = 0.00;
			Integer maxW=0;
			for (int p = 1; p < commonProcessorList.size(); p++) {
				
				Processor pro = commonProcessorList.get(p);
				
				
				processor$compCostsMap.put(commonProcessorList.get(p), computingCostMartrix[t][p]);
				
				if(computingCostMartrix[t][p]>maxW)
					maxW = computingCostMartrix[t][p];
				
				if(!opendProcessorNames.contains(pro.getName()))
					continue;
				
				totalW = totalW + computingCostMartrix[t][p];
				
			}
			task.setProcessor$CompCostMap(processor$compCostsMap);
			task.setAvgW(totalW / (opendProcessorNames.size() ) );
			task.setMaxWi(maxW);
		}

		
		for (int t = 1; t < g.getTaskList().size(); t++) {
			Task task = g.getTaskList().get(t);
			Integer[] comm = communicationCostMartrix[t];
			LinkedHashMap<Task, Integer> succTask$CommCostMap = task.getSuccTask$CommCostMap();
			for (int i = 1; i < comm.length; i++) {
				if (comm[i] != 0.000) {

					Task succTask = g.getTaskList().get(g.getTaskList().indexOf(new Task(g.getName() + ".n_" + i))); //ѭ���е����¼���
					succTask$CommCostMap.put(succTask, comm[i]);
					task.setOutd(task.getOutd() + 1); 
				}
			}
			task.setSuccTask$CommCostMap(succTask$CommCostMap);

			
		}
		
		for (int t = 1; t < g.getTaskList().size(); t++) {

			Task task = g.getTaskList().get(t);
			LinkedHashMap<Task, Integer> succTask$CommCostMap = task.getSuccTask$CommCostMap();
			for (Task succTask : succTask$CommCostMap.keySet()) {
				Integer commCost = succTask$CommCostMap.get(succTask);
				succTask = g.getTaskList().get(g.getTaskList().indexOf(succTask));
				succTask.getPredTask$CommCostMap().put(task, commCost);
			}

		}
		
		setRanku(g);
		
		
		

	}
	
	
	
	
	
	
	
	public static void initSharedMemory(Application g, List<Processor> commonProcessorList, Integer[][] computingCostMartrix, Integer[][] communicationCostMartrix,double arrivalTime,Double deadline, Double ... period) {
		g.setCommonProcessorList(commonProcessorList);
		g.setComps(computingCostMartrix);
		g.setComms(communicationCostMartrix);
		if(period!=null&&period.length>0)
			g.setPeriod(period[0]);
		
		g.setArrivalTime(arrivalTime);
		if(deadline!=null)
			g.setRelativeDeadline(deadline);
		
		for (int i = 0; i < computingCostMartrix.length; i++) {

			Task t = new Task(g.getName() + ".n_" + i);
			if (i == 1) {
				t.setIsEntry(true);
				g.getTaskPriorityQueue().in(t);
				g.setEntryTask(t);
			}
			if (i == computingCostMartrix.length - 1) {
				t.setIsExit(true);
				g.setExitTask(t);
			}

			g.getTaskList().add(t);
			t.setApplication(g);
		}

	
		for (int t = 1; t < g.getTaskList().size(); t++) {
			Task task = g.getTaskList().get(t);
			LinkedHashMap<Processor, Integer> processor$compCostsMap = new LinkedHashMap<Processor, Integer>();
			Double totalW = 0.00;
			for (int p = 1; p < commonProcessorList.size(); p++) {
				
				processor$compCostsMap.put(commonProcessorList.get(p), computingCostMartrix[t][p]);
				totalW = totalW + computingCostMartrix[t][p];
			}
			task.setProcessor$CompCostMap(processor$compCostsMap);
			task.setAvgW(totalW / (commonProcessorList.size() - 1) );
		}

		
		for (int t = 1; t < g.getTaskList().size(); t++) {
			Task task = g.getTaskList().get(t);
			Integer[] comm = communicationCostMartrix[t];
			LinkedHashMap<Task, Integer> succTask$CommCostMap = task.getSuccTask$CommCostMap();
			for (int i = 1; i < comm.length; i++) {
				if (comm[i] != 0.000) {

					Task succTask = g.getTaskList().get(g.getTaskList().indexOf(new Task(g.getName() + ".n_" + i))); 
					succTask$CommCostMap.put(succTask, comm[i]);
					task.setOutd(task.getOutd() + 1); 

				}
			}
			task.setSuccTask$CommCostMap(succTask$CommCostMap);

		}
		
		for (int t = 1; t < g.getTaskList().size(); t++) {

			Task task = g.getTaskList().get(t);
			LinkedHashMap<Task, Integer> succTask$CommCostMap = task.getSuccTask$CommCostMap();
			for (Task succTask : succTask$CommCostMap.keySet()) {
				Integer commCost = succTask$CommCostMap.get(succTask);
				succTask = g.getTaskList().get(g.getTaskList().indexOf(succTask));
				succTask.getPredTask$CommCostMap().put(task, commCost);
			}

		}
		
		setRankuSharedMemory( g);
		
		
		for (int p = 1; p < commonProcessorList.size(); p++) {
			Processor pro = commonProcessorList.get(p);
			double processorSL = 0d;
			for (int t = 1; t < g.getTaskList().size(); t++) {
				Task task = g.getTaskList().get(t);
				processorSL = processorSL+ task.getProcessor$CompCostMap().get(pro);
				task.setF(pro.getF_max()); //设置最大频率
			}
			
		}
		

	}


	public static void initTaskPriorityQueue(Application g) {

		g.getTaskPriorityQueue().clear();
		
		g.getTaskPriorityQueue().in(g.getEntryTask());

	}
	
	private static  void setRanku(Application g) {

		for (int i = g.getTaskList().size() - 1; i >= 1; i--) {
			Task currentTask = g.getTaskList().get(i);
			LinkedHashMap<Task, Integer> succCommCosts = currentTask.getSuccTask$CommCostMap();
			Set<Task> succTasks = succCommCosts.keySet();

			double tempR = 0.00;
			if (succTasks != null && succTasks.size() > 0) {

				for (Task succTask : succTasks) {

					Double r = succTask.getRanku();
					if (r == null)
						r = 0.000;
					Integer c = currentTask.getSuccTask$CommCostMap().get(succTask); 
					if (c == null)
						c = 0;

					if (r + c > tempR) {
						tempR = (r + c);
					}
				}
			}
			if(g.getExitTask().getName().endsWith("n_10"))
				currentTask.setRanku(Math.ceil(currentTask.getAvgW() + tempR * 1) / 1.0); //HEFTģ�������
			else
				currentTask.setRanku(currentTask.getAvgW() + tempR);
		}

	}

	
	private static  void setOct(Application g, List<Processor> commonProcessorList) {

		for (int i = g.getTaskList().size() - 1; i >= 1; i--) {
			Task currentTask = g.getTaskList().get(i);
			
			double octAvg= 0;
			for(int k=1;k<commonProcessorList.size();k++){
				Processor p = commonProcessorList.get(k);
				if(currentTask.getIsExit())
					currentTask.getOctMap().put(p.getName(), 0d);
				else{
					
					LinkedHashMap<Task, Integer> succCommCosts = currentTask.getSuccTask$CommCostMap();
					Set<Task> succTasks = succCommCosts.keySet();
					
					double oct= 0;
					for(Task succTask:succTasks){
						double w = Double.MAX_VALUE;
						for(int kk=1;kk<commonProcessorList.size();kk++){
							Processor pp = commonProcessorList.get(kk);
							double octSucc = succTask.getOctMap().get(pp.getName());
							double wik = currentTask.getProcessor$CompCostMap().get(p);
							double cij=currentTask.getSuccTask$CommCostMap().get(succTask);
							
							if(p.getName().equals(pp.getName()))
								cij = 0;
							double ww = octSucc+wik+cij;
							if(ww<w)
								w= ww; 
						}
						if(w>oct)
							oct = w;
					}
					currentTask.getOctMap().put(p.getName(), oct);
					
				}
				octAvg += currentTask.getOctMap().get(p.getName());
				
			}
			currentTask.setOct(octAvg/(commonProcessorList.size()-1));
			
		}

	}
	

	private static  void setRankuSharedMemory(Application g) {

		for (int i = g.getTaskList().size() - 1; i >= 1; i--) {
			Task currentTask = g.getTaskList().get(i);
			LinkedHashMap<Task, Integer> succCommCosts = currentTask.getSuccTask$CommCostMap();
			Set<Task> succTasks = succCommCosts.keySet();

			double tempR = 0.00;
			if (succTasks != null && succTasks.size() > 0) {

				for (Task succTask : succTasks) {

					Double r = succTask.getRanku();
					if (r == null)
						r = 0.000;
					Integer c = currentTask.getSuccTask$CommCostMap().get(succTask); //��ǰ�������������ͨ�ſ���
					if (c == null)
						c = 0;
					c=0;
					if (r + c > tempR) {
						tempR = (r + c);
					}
				}
			}

		
		//	currentTask.setRanku(Math.ceil(currentTask.getAvgW() + tempR * 1) / 1.0); //HEFTģ�������
		currentTask.setRanku(currentTask.getAvgW() + tempR);
		}

	}

	
	

}
