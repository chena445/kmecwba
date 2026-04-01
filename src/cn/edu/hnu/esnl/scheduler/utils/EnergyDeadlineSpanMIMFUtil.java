package cn.edu.hnu.esnl.scheduler.utils;

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
import cn.edu.hnu.esnl.bean.view.StartEndTime;
import cn.edu.hnu.esnl.schedule.assitant.ScheduleAssistantUtil;
import cn.edu.hnu.esnl.scheduler.DeadlinespanMIMFScheluler;
import cn.edu.hnu.esnl.service.ApplicationServiceBean;
import cn.edu.hnu.esnl.service.TaskEnergyServiceBean;
import cn.edu.hnu.esnl.service.TaskServiceBean;
import cn.edu.hnu.esnl.system.SystemValue;
import cn.edu.hnu.esnl.util.JdasQueue;

/**
 * @author xgq E-mail:xgqman@126.com
 * @version ����ʱ�䣺Feb 4, 2015 7:12:43 PM ��˵��:
 * 
 */
public class EnergyDeadlineSpanMIMFUtil {

	public static void taskAllocation(String schedulingName, List<Application> apps, List<Processor> commomProcessorList, int currentTime, JdasQueue<Task> commomReadyQueue, List<Task> allocatedTasks,
			Integer span) {

		LinkedHashMap<Integer, LinkedHashMap<Task, Processor>> round$tasksMap = new LinkedHashMap<Integer, LinkedHashMap<Task, Processor>>();

		List<Application> completedApplications = new ArrayList<Application>();
		Integer round = 0;

		Application systemDAG = null;

		SystemValue.system_criticality = Criticality.S0;

		while (true) {

			if (ScheduleAssistantUtil.isEnded(apps))
				return;

			round++;
			// System.out.println("Round:" + round);

		
			for (Application g : apps) {

				Collections.sort(g.getTaskPriorityQueue(), new Comparator<Task>() {
					public int compare(Task o1, Task o2) {
						return -o1.getRanku().compareTo(o2.getRanku()); 
					}
				});

				if (g.getCriticality() < SystemValue.system_criticality) { 

					continue;
				}

				Task currentTask = g.getTaskPriorityQueue().out(); 
				
				if (currentTask != null) {
					commomReadyQueue.in(currentTask);
				}

			}

			// ����
			while (!commomReadyQueue.isEmpty()) {

				Collections.sort(commomReadyQueue, new Comparator<Task>() {
					public int compare(Task o1, Task o2) {
						return -o1.getRanku().compareTo(o2.getRanku()); // �������ȼ�����

					}
				});

				Task currentTask = commomReadyQueue.out();

				if (currentTask != null) {

					//基于能耗的任务分配
					Processor allocatedProcessor = TaskEnergyServiceBean.taskAllocationDynamic(currentTask, commomProcessorList, currentTime); //

					if (SystemValue.isPrint)
						System.out.println("分配的任务：" + currentTask.getName() + " " + allocatedProcessor + " " + currentTask.getMakespan_st() + " " + currentTask.getMakespan_ft());

					LinkedHashMap<Task, Processor> task$ProcessorMap = round$tasksMap.get(round);
					if (task$ProcessorMap == null) {

						task$ProcessorMap = new LinkedHashMap<Task, Processor>();

					}
					task$ProcessorMap.put(currentTask, allocatedProcessor);
					round$tasksMap.put(round, task$ProcessorMap);

					if (allocatedTasks != null)
						allocatedTasks.add(currentTask);

					DeadlinespanMIMFScheluler.actualTaskSchedulingOrders.add(currentTask.getName());
					
					if ((currentTask.getApplication().getCriticality() > SystemValue.system_criticality)
							&& (currentTask.getMakespan_ft() - currentTask.getLower_bound_ft() - currentTask.getApplication().getArrivalTime() > span)) {

						systemDAG = currentTask.getApplication();

						if (SystemValue.isPrint)
							System.out.println("发现当前任务超过deadline" + currentTask + " getLower_bound_ft:" + currentTask.getLower_bound_ft() + " getMakespan_st:" + currentTask.getMakespan_st()
									+ " getMakespan_ft:" + currentTask.getMakespan_ft() + " deadline:" + (currentTask.getLower_bound_ft() + span));

						LinkedHashMap<Task, Processor> task$ProcessorMap1 = round$tasksMap.get(round - 1);
						LinkedHashMap<Task, Processor> task$ProcessorMap2 = round$tasksMap.get(round);

						if (task$ProcessorMap1 == null)
							task$ProcessorMap1 = new LinkedHashMap<Task, Processor>();

						if (task$ProcessorMap2 == null)
							task$ProcessorMap2 = new LinkedHashMap<Task, Processor>();

						Set<Task> taskSet1 = task$ProcessorMap1.keySet();
						Set<Task> taskSet2 = task$ProcessorMap2.keySet();

						for (Task removedTask : taskSet1) { // ��ʽɾ��

							if (completedApplications.contains(removedTask.getApplication()))
								continue;

							Processor _p = task$ProcessorMap1.get(removedTask);

							LinkedHashMap<Task, StartEndTime> task$startEndTimeMap = _p.getTask$startEndTimeMap();
							LinkedHashMap<StartEndTime, Task> startEndTime$taskMap = _p.getStartEndTime$TaskMap();

							LinkedHashMap<Task, Double> task$availTimeMap = _p.getTask$availTimeMap();

							removedTask.setMakespan_st(0d);
							removedTask.setMakespan_ft(0d);

							removedTask.getApplication().setMakespan(0d);
							task$startEndTimeMap.remove(removedTask); // ȡ�����
							removedTask.getApplication().getTaskPriorityQueue().in(removedTask); // �������޼�����

							task$availTimeMap.remove(removedTask);
							StartEndTime se = task$startEndTimeMap.get(removedTask);
							startEndTime$taskMap.remove(se);
							if (SystemValue.isPrint)
								System.out.println("取消超过Deadline的任务1" + removedTask.getName() + "  " + removedTask.getAssignedprocessor2() + " " + removedTask.getMakespan_st() + " "
										+ removedTask.getMakespan_ft());
							DeadlinespanMIMFScheluler.actualTaskSchedulingOrders.remove(removedTask);
							

						}

						for (Task removedTask : taskSet2) { // ��ʽɾ��

							if (completedApplications.contains(removedTask.getApplication()))
								continue;

							Processor _p = task$ProcessorMap2.get(removedTask);

							LinkedHashMap<Task, StartEndTime> task$startEndTimeMap = _p.getTask$startEndTimeMap();
							LinkedHashMap<StartEndTime, Task> startEndTime$taskMap = _p.getStartEndTime$TaskMap();

							LinkedHashMap<Task, Double> task$availTimeMap = _p.getTask$availTimeMap();

							removedTask.setMakespan_st(0d);
							removedTask.setMakespan_ft(0d);

							removedTask.getApplication().setMakespan(0d);
							task$startEndTimeMap.remove(removedTask); // ȡ�����
							removedTask.getApplication().getTaskPriorityQueue().in(removedTask); // �������޼�����

							task$availTimeMap.remove(removedTask);
							StartEndTime se = task$startEndTimeMap.get(removedTask);
							startEndTime$taskMap.remove(se);
							if(SystemValue.isPrint)
							System.out.println("取消超过Deadline的任务2:" + removedTask.getName() + "  " + removedTask.getAssignedprocessor2() + " " + removedTask.getMakespan_st() + " "
									+ removedTask.getMakespan_ft());
							
							DeadlinespanMIMFScheluler.actualTaskSchedulingOrders.remove(removedTask);
						}

						while (!commomReadyQueue.isEmpty()) {
							Task returnTask = commomReadyQueue.out();
							returnTask.getApplication().getTaskPriorityQueue().in(returnTask);
						}
						// 3. ����ϵͳ�ؼ�
						// System.out.println("ԭ��ϵͳ�ؼ�" +
						// SystemValue.system_criticality);
						SystemValue.system_criticality = systemDAG.getCriticality();
						// System.out.println("������ϵͳ�ؼ�" +
						// SystemValue.system_criticality);
						commomReadyQueue.clear();

						// overApplications.add(currentTask.getApplication());

					} else {

						LinkedHashMap<Task, Integer> succCommCosts = currentTask.getSuccTask$CommCostMap();
						Set<Task> succTasks = succCommCosts.keySet();
						for (Task succTask : succTasks) {
							boolean isReady = true;
							Set<Task> predTasks = succTask.getPredTask$CommCostMap().keySet();
							for (Task predTask : predTasks) {
								if (predTask.getAssignedprocessor2() == null) {
									isReady = false;
									break;
								}
							}
							if (isReady) {
								if (!currentTask.getApplication().getTaskPriorityQueue().contains(succTask))
									currentTask.getApplication().getTaskPriorityQueue().in(succTask);
							}

						}
						// currentTask =
						// currentTask.getApplication().getTaskPriorityQueue().out();
						// // ����һ�����ľ�������

						// if (currentTask != null) {
						// commomReadyQueue.in(currentTask);
						// }

					}

					if (systemDAG != null && systemDAG.getMakespan() != null && systemDAG.getMakespan() != 0) {

						completedApplications.add(systemDAG);

						// System.out.println("����ϵͳ�ؼ�");
						while (!commomReadyQueue.isEmpty()) {

							Task returnTask = commomReadyQueue.out();

							returnTask.getApplication().getTaskPriorityQueue().in(returnTask);

						}

						SystemValue.system_criticality = Criticality.S0;
						systemDAG = null;
						commomReadyQueue.clear();

					}

				}

			}
		}

	}

}
