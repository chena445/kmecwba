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
import cn.edu.hnu.esnl.scheduler.FairnessMIMFScheduler;
import cn.edu.hnu.esnl.scheduler.utils.FairnessMIMFUtil;
import cn.edu.hnu.esnl.scheduler.utils.WholePriorityUtil;
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
public class MixedPartialPriorityScheduler {

	/**
	 * ��DAG��ƽ����
	 * 
	 * @param commomProcessorList
	 * @param gs
	 */
	public double scheduling(List<Application> gs, List<Processor> commomProcessorList, Integer lastTime) {

		for (Application g : gs) {
			ApplicationServiceBean.initTaskPriorityQueue(g); // ��ʼ��
		}

		JdasQueue<Task> commomReadyQueue = new JdasQueue<Task>();
		int currentTime = 0;
		while (true) {

			if (lastTime != null && currentTime == lastTime){
				
				return ScheduleAssistantUtil.overallMakespan(gs);
			}
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


			List<Application> scheduling_apps = ScheduleAssistantUtil.schedulingApps(currentTime, gs); // ���ؼ�����

			Collections.sort(scheduling_apps);

			for (int i = 0; i < scheduling_apps.size(); i++) {
				
				Application higher_app = scheduling_apps.get(i);
				
				List<Application> fairness_apps = scheduling_apps.subList(i, scheduling_apps.size());
				if(fairness_apps.size()==1)
					break ;
				
				ScheduleAssistantUtil.dynamicArrivalCanceling(fairness_apps, commomProcessorList, currentTime, commomReadyQueue);
				
				
				while (higher_app.getMakespan() == null || higher_app.getMakespan() == 0) {

					boolean isFairness = true;
					List<Task> allocatedTasks = new ArrayList<Task>();
					//System.out.println("");
					FairnessMIMFUtil.taskAllocation("Ԥ����", fairness_apps, commomProcessorList,currentTime, commomReadyQueue, allocatedTasks);

					if (higher_app.getMakespan() > higher_app.getRelativeDeadline()) { // �������Ҫȡ��
						//System.out.println(higher_app.getName()+"  "+higher_app.getMakespan() );
						isFairness = false;
						ScheduleAssistantUtil.cancelPredictedScheduleResults(commomProcessorList, allocatedTasks);
					} else {
						break;
					}

					if (isFairness == false) {
						Task readyTask = higher_app.getTaskPriorityQueue().out();

						WholePriorityUtil.taskAllocation(readyTask, commomProcessorList, currentTime);
						// ��β�ˣ�����������deadline
						if (readyTask.getIsExit() && higher_app.getMakespan() > higher_app.getRelativeDeadline()) {
							//System.out.println("���˽�β�����ǲ�������");
							ScheduleAssistantUtil.dynamicArrivalCanceling(fairness_apps, commomProcessorList, currentTime, commomReadyQueue);
							FairnessMIMFUtil.taskAllocation("��ƽ", fairness_apps, commomProcessorList, currentTime, commomReadyQueue, allocatedTasks);
							break;
						}
					}
				}

			}
			currentTime++;
		}
		
	}

}
