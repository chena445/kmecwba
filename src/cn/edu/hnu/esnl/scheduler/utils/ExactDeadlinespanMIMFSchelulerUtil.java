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
import cn.edu.hnu.esnl.service.ApplicationServiceBean;
import cn.edu.hnu.esnl.service.TaskServiceBean;
import cn.edu.hnu.esnl.system.SystemValue;
import cn.edu.hnu.esnl.util.JdasQueue;

/**
 * @author xgq E-mail:xgqman@126.com
 * @version ����ʱ�䣺Feb 4, 2015 7:12:43 PM ��˵��:
 * 
 */
public class ExactDeadlinespanMIMFSchelulerUtil {

	public static void taskAllocation(String schedulingName, List<Application> apps, List<Processor> commomProcessorList, int currentTime) {

		while (true) {

			if (DeadlinespanMIMFScheluler.actualTaskSchedulingOrders.size()==0)
				return;

			//System.out.println("DeadlinespanMIMFScheluler最终调度顺序2：" + DeadlinespanMIMFScheluler.actualTaskSchedulingOrders);

			
			
			String currentTaskName = DeadlinespanMIMFScheluler.actualTaskSchedulingOrders.get(0);
			
			
			Application g =apps.get( apps.indexOf(new Application(currentTaskName.substring(0, currentTaskName.indexOf(".")))));
			
			
			Task currentTask = g.getTaskList().get(g.getTaskList().indexOf(new Task(currentTaskName)));
			
			currentTime = currentTask.getApplication().getArrivalTime().intValue();
			
			
			DeadlinespanMIMFScheluler.actualTaskSchedulingOrders.remove(0);

			if (currentTask != null) {
				
				
				TaskServiceBean.taskAllocationDynamic(currentTask, commomProcessorList, currentTime); // Ϊ������䴦������HEFT����

				

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
