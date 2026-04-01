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
import cn.edu.hnu.esnl.scheduler.utils.OWMStaticUtil;
import cn.edu.hnu.esnl.service.ApplicationServiceBean;
import cn.edu.hnu.esnl.service.TaskServiceBean;
import cn.edu.hnu.esnl.util.JdasQueue;

/**
 * @author xgq 
 * E-mail:xgqman@126.com
 * @version ����ʱ�䣺Dec 30, 2014 3:41:26 AM
 * ��˵��:
 *
 */
public class OWMStaticScheduler {

	
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

			List<Application> scheduledApps = ScheduleAssistantUtil.schedulingApps(currentTime, gs);

		//	ScheduleAssistantUtil.dynamicArrivalCanceling(scheduledApps, commomProcessorList, currentTime, commomReadyQueue);

			OWMStaticUtil.taskAllocation("��ƽ",scheduledApps, commomProcessorList, currentTime, commomReadyQueue,null);
			
			currentTime++;

		}
	}
	
	
	
	
	

}
