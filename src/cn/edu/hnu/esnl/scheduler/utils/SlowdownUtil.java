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
import cn.edu.hnu.esnl.service.ApplicationServiceBean;
import cn.edu.hnu.esnl.service.TaskServiceBean;
import cn.edu.hnu.esnl.system.SystemValue;
import cn.edu.hnu.esnl.util.JdasQueue;

/**
 * @author xgq E-mail:xgqman@126.com
 * @version ����ʱ�䣺Feb 4, 2015 7:12:43 PM ��˵��:
 * 
 */
public class SlowdownUtil {

	

	public static void taskAllocation(String schedulingName, List<Application> apps, List<Processor> commomProcessorList, int currentTime, JdasQueue<Task> commomReadyQueue,
			List<Task> allocatedTasks) {

		Application maxG = null;
		double maxMakespan = 0;
		for (Application g : apps) {

			if (g.getLower_bound() > maxMakespan) {
				maxMakespan = g.getLower_bound();
				maxG = g;
			}
		}

		Application selectG = maxG;
		while (selectG != null) {

			Task currentTask = selectG.getTaskPriorityQueue().out(); // ����һ�����ľ�������
			if (currentTask != null) {

				TaskServiceBean.taskAllocationDynamic(currentTask, commomProcessorList, currentTime); // Ϊ������䴦������HEFT����

				if (allocatedTasks != null)
					allocatedTasks.add(currentTask);

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

				//System.out.println(selectG);
				//System.out.println(currentTask.getLower_bound_ft());
				//System.out.println(currentTask.getMakespan_ft());
				selectG.setSlowdown((double) currentTask.getLower_bound_ft() / currentTask.getMakespan_ft());

				selectG = null;
				if (ScheduleAssistantUtil.isEnded(apps))
					return;

				List<Application> apps2 = new ArrayList<Application>();
				for (Application app : apps) {

					if (app.getMakespan() != null && app.getMakespan() > 0) {

					} else {

						apps2.add(app);

					}

				}

				if (apps2.size() > 0) {
					Collections.sort(apps2, new Comparator<Application>() {
						public int compare(Application g1, Application g2) {
							int result = g1.getSlowdown().compareTo(g2.getSlowdown());
							if (result == 0)
								result = -g1.getTaskPriorityQueue().get(0).getLower_bound_ft().compareTo(g2.getTaskPriorityQueue().get(0).getLower_bound_ft());
							return result;
						}
					});
					selectG = apps2.get(0);
				}

			}

		}

	}
	
	

}
