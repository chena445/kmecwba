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

/**
 * @author xgq 
 * E-mail:xgqman@126.com
 * @version ����ʱ�䣺Feb 4, 2015 1:38:09 PM
 * ��˵��:
 *
 */
public class ApplicationServiceCANBean {
	
	public static void init(Application g, List<Processor> commonProcessorList, Integer[][] computingCostMartrix, Integer[][] communicationCostMartrix,double arrivalTime,Double deadline, Double ... period) {
		g.setCommonProcessorList(commonProcessorList);
		g.setComps(computingCostMartrix);
		g.setComms(communicationCostMartrix);
		if(period!=null&&period.length>0)
			g.setPeriod(period[0]);
		
		g.setArrivalTime(arrivalTime);
		if(deadline!=null)
			g.setRelativeDeadline(deadline);
		//��ʼ������ʹ�����
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

		//��ʼ�������ڲ�ͬ�������ϵļ��㿪��
		for (int t = 1; t < g.getTaskList().size(); t++) {
			Task task = g.getTaskList().get(t);
			LinkedHashMap<Processor, Integer> processor$compCostsMap = new LinkedHashMap<Processor, Integer>();
			Double totalW = 0.00;
			for (int p = 1; p < commonProcessorList.size(); p++) {
				
				processor$compCostsMap.put(commonProcessorList.get(p), computingCostMartrix[t][p]);
				totalW = totalW + computingCostMartrix[t][p];
			}
			task.setProcessor$CompCostMap(processor$compCostsMap);
			task.setAvgW(totalW / (commonProcessorList.size() - 1));
		}

		//��ʼ��ÿ������ĺ��
		for (int t = 1; t < g.getTaskList().size(); t++) {
			Task task = g.getTaskList().get(t);
			Integer[] comm = communicationCostMartrix[t];
			LinkedHashMap<Task, Integer> succTask$CommCostMap = task.getSuccTask$CommCostMap();
			for (int i = 1; i < comm.length; i++) {
				if (comm[i] != 0.000) {

					Task succTask = g.getTaskList().get(g.getTaskList().indexOf(new Task(g.getName() + ".n_" + i))); //ѭ���е����¼���
					succTask$CommCostMap.put(succTask, comm[i]);
					task.setOutd(task.getOutd() + 1); //��̸���

				}
			}
			task.setSuccTask$CommCostMap(succTask$CommCostMap);

		}
		//��ʼ��ÿ�������ǰ��
		for (int t = 1; t < g.getTaskList().size(); t++) {

			Task task = g.getTaskList().get(t);
			LinkedHashMap<Task, Integer> succTask$CommCostMap = task.getSuccTask$CommCostMap();
			for (Task succTask : succTask$CommCostMap.keySet()) {
				Integer commCost = succTask$CommCostMap.get(succTask);
				succTask = g.getTaskList().get(g.getTaskList().indexOf(succTask));
				succTask.getPredTask$CommCostMap().put(task, commCost);
			}

		}
		
		taskOrdering( g);
		
		//processorCP
		
		/*
		for (int p = 1; p < commonProcessorList.size(); p++) {
			Processor pro = commonProcessorList.get(p);
			double processorSL = 0d;
			for (int t = 1; t < g.getTaskList().size(); t++) {
				Task task = g.getTaskList().get(t);
				processorSL = processorSL+ task.getProcessor$CompCostMap().get(pro);
				
			}
			pro.setProcessorSL(processorSL);
			
			double processorEnergy = EnergyService.getEnergy(pro, 1d, 0d, processorSL);
			pro.setProcessorEnergy(processorEnergy);
		}
		*/
		

	}

	public static void initTaskPriorityQueue(Application g) {

		g.getTaskPriorityQueue().clear();
		
		g.getTaskPriorityQueue().in(g.getEntryTask());

	}
	
	private static  void taskOrdering(Application g) {

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

					if (r + c > tempR) {
						tempR = (r + c);
					}
				}
			}

			//�����ȻҪ���컨������������
			currentTask.setRanku(Math.ceil(currentTask.getAvgW() + tempR * 1) / 1.0); //HEFTģ�������
			//currentTask.setRanku(currentTask.getAvgW() + tempR);
		}

	}

	


	
	

}
