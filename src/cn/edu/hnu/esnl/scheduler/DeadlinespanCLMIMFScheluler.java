package cn.edu.hnu.esnl.scheduler;

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
import cn.edu.hnu.esnl.scheduler.utils.DeadlineSpanCLMIMFUtil;
import cn.edu.hnu.esnl.scheduler.utils.DeadlineSpanMIMFUtil;
import cn.edu.hnu.esnl.service.ApplicationServiceBean;
import cn.edu.hnu.esnl.service.TaskServiceBean;
import cn.edu.hnu.esnl.system.SystemValue;
import cn.edu.hnu.esnl.util.DeepCopyUtil;
import cn.edu.hnu.esnl.util.JdasQueue;

/**
 * @author xgq E-mail:xgqman@126.com
 * @version ����ʱ�䣺Dec 30, 2014 3:41:26 AM ��˵��:
 * 
 */
public class DeadlinespanCLMIMFScheluler {
    
	public static List<String> actualTaskSchedulingOrders = new ArrayList<String>();
	
	public static double scheduling(List<Application> gs, List<Processor> commomProcessorList, Integer lastTime) {

		int currentTime = 0;
		
		for (Application g : gs) {
			ApplicationServiceBean.initTaskPriorityQueue(g);
		}

		JdasQueue<Task> commomReadyQueue = new JdasQueue<Task>();
		
		
		while (true) {
			if (lastTime != null && currentTime == lastTime)
				return ScheduleAssistantUtil.overallMakespan(gs);

			if (lastTime == null) {
				if (ScheduleAssistantUtil.isEnded(gs)) {
					for (Application g : gs) {

					}
					return ScheduleAssistantUtil.overallMakespan(gs);
				}

			}

			
			Application a_app = ScheduleAssistantUtil.arrivalApp(currentTime, gs);
			if (a_app == null) {
				currentTime++;
				continue;
			}

			System.out.println("到达的功能："+a_app.getName());
			
			List<Application> scheduledApps = ScheduleAssistantUtil.schedulingApps(currentTime, gs);

			dynamicArrivalCanceling(scheduledApps, commomProcessorList, currentTime, commomReadyQueue);

			DeadlineSpanCLMIMFUtil.taskAllocation("DeadlineSpanCLMIMFUtil调度结果",scheduledApps, commomProcessorList, currentTime, commomReadyQueue,null);
			
			
			currentTime++;

		}
	}

	
	
	
	public static void dynamicArrivalCanceling(List<Application> gs, List<Processor> commomProcessorList, int currentTime, JdasQueue<Task> commomReadyQueue) {

		for (Processor p : commomProcessorList) {

			LinkedHashMap<Task, Double> task$availTimeMap = p.getTask$availTimeMap();

			LinkedHashMap<Task, StartEndTime> task$startEndTimeMap = p.getTask$startEndTimeMap();

			LinkedHashMap<StartEndTime, Task> startEndTime$taskMap = p.getStartEndTime$TaskMap();

			Set<Task> taskSet = task$startEndTimeMap.keySet();
			Set<Task> needRemovedTask = new HashSet<Task>();

			for (Task task : taskSet) {

				if (gs.contains(task.getApplication())) {

					StartEndTime se = task$startEndTimeMap.get(task);

					if (se.getStartTime() >= currentTime) {
						needRemovedTask.add(task);
					}

				}

			}

			for (Task task : needRemovedTask) { // ��ʽɾ��
				if (SystemValue.isPrint)
					System.out.println("取消因动态到达的任务:" + task.getName() + "  " + task.getAssignedprocessor2() + " " + task.getMakespan_st() + " " + task.getMakespan_ft());
				task.setMakespan_st(0d);
				task.setMakespan_ft(0d);

				task.getApplication().setMakespan(0d);
				task$startEndTimeMap.remove(task); // ȡ�����
				task.getApplication().getTaskPriorityQueue().in(task); // �������޼�����
				commomReadyQueue.clear(); // ���common
				task$availTimeMap.remove(task);
				StartEndTime se = task$startEndTimeMap.get(task);
				startEndTime$taskMap.remove(se);

				actualTaskSchedulingOrders.remove(task);
			}

		}

	}

}
