package cn.edu.hnu.esnl.scheduler.utils;

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
import cn.edu.hnu.esnl.schedule.assitant.ScheduleAssistantUtil;
import cn.edu.hnu.esnl.scheduler.DeadlinespanMIMFScheluler;
import cn.edu.hnu.esnl.scheduler.FairnessMIMFScheduler;
import cn.edu.hnu.esnl.service.ApplicationServiceBean;
import cn.edu.hnu.esnl.service.TaskServiceBean;
import cn.edu.hnu.esnl.system.SystemValue;
import cn.edu.hnu.esnl.util.JdasQueue;

/**
 * @author xgq E-mail:xgqman@126.com
 * @version ����ʱ�䣺Feb 4, 2015 7:12:43 PM ��˵��:
 * 
 */
public class FairnessMIMFUtil {

	
	public static void taskAllocation(String schedulingName, List<Application> apps, List<Processor> commomProcessorList, int currentTime, JdasQueue<Task> commomReadyQueue, List<Task> allocatedTasks) {

		
		while (true) {
		

			if (ScheduleAssistantUtil.isEnded(apps))
				return;

			//�빫������
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

			//����
			while (!commomReadyQueue.isEmpty()) {

				Collections.sort(commomReadyQueue, new Comparator<Task>() {
					public int compare(Task o1, Task o2) {
						return -o1.getRanku().compareTo(o2.getRanku()); // �������ȼ�����

					}
				});

				Task currentTask = commomReadyQueue.out();

				if (currentTask != null) {

					TaskServiceBean.taskAllocationDynamic(currentTask, commomProcessorList, currentTime); // Ϊ������䴦������HEFT����


					FairnessMIMFScheduler.actualTaskSchedulingOrders.add(currentTask.getName());
					
					
					if(SystemValue.isPrint)
					System.out.println("��DAG" + schedulingName + "���:" + currentTask.getRanku() + "  " + currentTask.getName() + "  " + currentTask.getAssignedprocessor2() + " "
					+ currentTask.getMakespan_st() + " " + currentTask.getMakespan_ft());

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
				

			}
		}
	}
	
	
	

	
	
	

}
