package cn.edu.hnu.esnl.scheduler.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import cn.edu.hnu.esnl.bean.Application;
import cn.edu.hnu.esnl.bean.Criticality;
import cn.edu.hnu.esnl.bean.Processor;
import cn.edu.hnu.esnl.bean.Task;
import cn.edu.hnu.esnl.bean.view.StartEndTime;
import cn.edu.hnu.esnl.schedule.assitant.HEFTScheduler;
import cn.edu.hnu.esnl.schedule.assitant.ScheduleAssistantUtil;
import cn.edu.hnu.esnl.schedule.assitant.UnHEFTScheduler;
import cn.edu.hnu.esnl.service.ApplicationServiceBean;
import cn.edu.hnu.esnl.service.TaskServiceBean;
import cn.edu.hnu.esnl.system.SystemValue;
import cn.edu.hnu.esnl.util.DeepCopyUtil;
import cn.edu.hnu.esnl.util.JdasQueue;

/**
 * 
 * @author Guoqi Xie E-mail:xgqman@126.com
 * @version JDAS 5.0 Create time��Nov 27, 2015 9:42:02 PM
 */

public class MTMDStaticUtil {

	public static void taskAllocation(String schedulingName, List<Application> apps, List<Processor> commomProcessorList, int currentTime, JdasQueue<Task> commomReadyQueue, List<Task> allocatedTasks)
			throws IOException, ClassNotFoundException {

		while (true) {

			if (ScheduleAssistantUtil.isEnded(apps))
				return;

			// �빫������
			for (Application g : apps) {

				Collections.sort(g.getTaskPriorityQueue(), new Comparator<Task>() {
					public int compare(Task o1, Task o2) {
						return -o1.getRanku().compareTo(o2.getRanku()); // �������ȼ�����
					}
				});
				
				Task currentTask = g.getTaskPriorityQueue().out(); // ����һ�����ľ�������

				if (currentTask != null) {
					commomReadyQueue.in(currentTask);
				}

			}

			// ����
			while (!commomReadyQueue.isEmpty()) {
				
				
				commomReadyQueue.remove(null);
				for (Task currentTask : commomReadyQueue) {
					System.out.println("================commomReadyQueue:"+commomReadyQueue);
					
					if(currentTask!=null)
					setAvailAndRemainningTime(currentTask.getApplication(), commomProcessorList);

				}
				double averageUnRemainingTime = averageUnRemainingTime(apps);

				for (Task currentTask : commomReadyQueue) {

					urgency(currentTask.getApplication(), averageUnRemainingTime);
				}
				Collections.sort(commomReadyQueue, new Comparator<Task>() {
					public int compare(Task o1, Task o2) {
						return -o1.getApplication().getUrgency().compareTo(o2.getApplication().getUrgency()); // �������ȼ�����

					}
				});

				Task currentTask = commomReadyQueue.out();

				if (currentTask != null) {

					TaskServiceBean.taskAllocationDynamic(currentTask, commomProcessorList, currentTime); // Ϊ������䴦������HEFT����

					if (allocatedTasks != null)
						allocatedTasks.add(currentTask);

					//System.out.println("��DAG" + schedulingName + "���:" + currentTask.getRanku() + "  " + currentTask.getName() + "  " + currentTask.getAssignedprocessor2() + " "
					//		+ currentTask.getMakespan_st() + " " + currentTask.getMakespan_ft());

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

				}
				if (currentTask != null) {
					currentTask = currentTask.getApplication().getTaskPriorityQueue().out();
					commomReadyQueue.in(currentTask);
				}

			}
		}
	}

	public static void setAvailAndRemainningTime(Application g, List<Processor> commomProcessorList) throws IOException, ClassNotFoundException {

		Application g1 = new Application(g.getName(), g.getCriticality());
		ApplicationServiceBean.init(g1, g.getCommonProcessorList(), g.getComps(), g.getComms(), 0, g.getRelativeDeadline(), g.getPeriod()); // 69

		List<Processor> commomProcessorList1 = DeepCopyUtil.copy(commomProcessorList);

		// ��Ҫ�����ʼ����
		
		Task startTask = g1.getTaskPriorityQueue().get(0);
		
		System.out.println("startTask:"+startTask);
		
		Task eTask = g1.getExitTask();
		System.out.println("eTask:"+eTask);
		
		new UnHEFTScheduler().scheduling(g1, commomProcessorList1, startTask); // 80

		double est = startTask.getLower_bound_st();
		double eft =eTask.getLower_bound_ft();

		double unMakespan = eft - est;

		double unAvailTime = g.getRelativeDeadline() - est;

		double unRemainingTime = g.getRelativeDeadline() - eft;

		g.setUnMakespan(unMakespan);
		g.setUnAvailTime(unAvailTime);
		g.setUnRemainingTime(unRemainingTime);

	}

	public static double averageUnRemainingTime(List<Application> gs) {

		double averageUnRemainingTime = 0d;
		for (int i = 0; i < gs.size(); i++) {
			Application g = gs.get(i);
			averageUnRemainingTime += g.getUnRemainingTime();

		}
		averageUnRemainingTime = averageUnRemainingTime / gs.size();

		return averageUnRemainingTime;
	}

	public static void urgency(Application g, double averageUnRemainingTime) {

		double urgency = 0d;

		urgency = g.getUnMakespan() / g.getUnAvailTime();

		urgency = averageUnRemainingTime / g.getUnRemainingTime();
		g.setUrgency(urgency);

	}

}
