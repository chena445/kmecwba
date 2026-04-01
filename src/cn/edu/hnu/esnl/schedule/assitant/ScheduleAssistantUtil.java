package cn.edu.hnu.esnl.schedule.assitant;

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
import cn.edu.hnu.esnl.service.ApplicationServiceBean;
import cn.edu.hnu.esnl.service.TaskServiceBean;
import cn.edu.hnu.esnl.system.SystemValue;
import cn.edu.hnu.esnl.util.JdasQueue;

/**
 * @author xgq E-mail:xgqman@126.com
 * @version ����ʱ�䣺Feb 4, 2015 7:12:43 PM ��˵��:
 * 
 */
public class ScheduleAssistantUtil {

	public static boolean isEnded(List<Application> gs) {
		int completeCount = 0;
		for (Application g : gs) {
			if (g.getMakespan() != null && g.getMakespan() != 0)
				completeCount++;
			else {
				// System.out.println("δ������ɵ�DAG��" + g.getName());
			}
		}
		if (completeCount == gs.size())
			return true;
		else {

			return false;
		}
	}

	public static int unendNumber(List<Application> gs) {
		int completeCount = 0;
		for (Application g : gs) {
			if (g.getMakespan() != null && g.getMakespan() != 0)
				completeCount++;
			else {
				// System.out.println("δ������ɵ�DAG��" + g.getName() +
				// "  "+g.getCriticality());
			}
		}
		return gs.size() - completeCount;
	}

	public static boolean isEnded(Application g) {
		int completeCount = 0;

		if (g.getMakespan() != null && g.getMakespan() != 0)
			completeCount++;

		if (completeCount == 1)
			return true;
		else
			return false;
	}

	public static double overallMakespan(List<Application> gs) {
		double overallMakespan = 0;
		for (Application g : gs) {
			double makesoan = g.getMakespan();
			if (makesoan > overallMakespan)
				overallMakespan = makesoan;

		}
		return overallMakespan;
	}

	public static Application arrivalApp(int currentTime, List<Application> gs) {

		for (Application g : gs) {
			if (currentTime == g.getArrivalTime())
				return g;
			
			
			if (g.getPeriod()!=null&&g.getPeriod()!=0&&currentTime%g.getPeriod() == 0){
				//System.out.println("currentTime:"+currentTime);
				//System.out.println("g.getPeriod():"+g.getPeriod());
				Application g1 = new Application(g.getName()+"J", g.getCriticality());
				ApplicationServiceBean.init(g1, g.getCommonProcessorList(), g.getComps(), g.getComms(), currentTime, g.getRelativeDeadline(),g.getPeriod()); // 69
				
				//System.out.println(g1.getTaskList());
				
				gs.add(g1);
				return g;
			}

		}
		return null;
	}

	public static List<Application> schedulingApps(int currentTime, List<Application> gs) {

		List<Application> apps = new ArrayList<Application>();
		for (Application g : gs) {
			if (currentTime >= g.getArrivalTime() && (g.getMakespan() == 0 || currentTime <= g.getMakespan())) {

				apps.add(g);

			}
		}
		return apps;
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
			 System.out.println("取消因动态到达的任务:" + task.getName() + "  " +
				 task.getAssignedprocessor2() + " " + task.getMakespan_st() +
			" " + task.getMakespan_ft());
				task.setMakespan_st(0d);
				task.setMakespan_ft(0d);

				task.getApplication().setMakespan(0d);
				task$startEndTimeMap.remove(task); // ȡ�����
				task.getApplication().getTaskPriorityQueue().in(task); // �������޼�����
				commomReadyQueue.clear(); // ���common
				task$availTimeMap.remove(task);
				StartEndTime se = task$startEndTimeMap.get(task);
				startEndTime$taskMap.remove(se);
				
				

			}

		}

	}

	public static void cancelPredictedScheduleResults(List<Processor> commomProcessorList, List<Task> allocatedTasks) {

		for (Task task : allocatedTasks) { // ��ʽɾ��

			// System.out.println("ȡ��Ԥ���Ƚ��:" + task.getName() + "  " +
			// task.getAssignedprocessor2() + " " + task.getMakespan_st() + " "
			// + task.getMakespan_ft());

			for (Processor p : commomProcessorList) {

				LinkedHashMap<Task, StartEndTime> task$startEndTimeMap = p.getTask$startEndTimeMap();
				LinkedHashMap<StartEndTime, Task> startEndTime$taskMap = p.getStartEndTime$TaskMap();

				LinkedHashMap<Task, Double> task$availTimeMap = p.getTask$availTimeMap();
				task$startEndTimeMap.remove(task);
				task$availTimeMap.remove(task);
				StartEndTime se = task$startEndTimeMap.get(task);
				startEndTime$taskMap.remove(se);

			}

			task.setMakespan_st(0d);
			task.setMakespan_ft(0d);
			task.getApplication().setMakespan(0d);
			task.getApplication().getTaskPriorityQueue().add(task); // ���������������ȼ�����

			Collections.sort(task.getApplication().getTaskPriorityQueue(), new Comparator<Task>() {
				public int compare(Task o1, Task o2) {
					return -o1.getRanku().compareTo(o2.getRanku()); // �������ȼ�����
				}
			});

		}
	}


	public static LinkedHashMap<Integer, List<Application>> orderAppByCriticality(List<Application> gs) {
		Collections.sort(gs);
		LinkedHashMap<Integer, List<Application>> criticality$ApplicationsMap = new LinkedHashMap<Integer, List<Application>>();
		for (Application g : gs) {
			List<Application> apps = criticality$ApplicationsMap.get(g.getCriticality());
			if (apps == null) {
				apps = new ArrayList<Application>();

			}
			apps.add(g);
			criticality$ApplicationsMap.put(g.getCriticality(), apps);

		}
		return criticality$ApplicationsMap;
	}

}
