package cn.edu.hnu.esnl.schedule.assitant;

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
public class UnHEFTScheduler {

	/**
	 * ��DAG����
	 * @param processorList
	 * @param g
	 */
	public void scheduling(Application g,  List<Processor> commomProcessorList, Task startTask) {
		//ApplicationServiceBean.initTaskPriorityQueue(g);
		
		g.getTaskPriorityQueue().clear();
		
		g.getTaskPriorityQueue().in(startTask);

		while (!g.getTaskPriorityQueue().isEmpty()) {
			
			Collections.sort(g.getTaskPriorityQueue(), new Comparator<Task>() {
				public int compare(Task o1, Task o2) {
					return -o1.getRanku().compareTo(o2.getRanku()); // �������ȼ�����

				}
			});

			Task currentTask = g.getTaskPriorityQueue().out();
			
			
		

			TaskServiceBean.taskAllocationLowerBound(currentTask, commomProcessorList); // Ϊ������䴦������HEFT����
			
			System.out.println("��DAG���Ƚ��:" + currentTask.getRanku() + " " + currentTask.getName() + "  " + currentTask.getAssignedprocessor1() + " " + currentTask.getLower_bound_st() + " "
					+ currentTask.getLower_bound_ft());

			LinkedHashMap<Task, Integer> succTask$CommCostMap = currentTask.getSuccTask$CommCostMap();
			
			
			
			Set<Task> succTasks = succTask$CommCostMap.keySet();
			for (Task succTask : succTasks) {
				
				boolean isReady = true;
				Set<Task> predTasks = succTask.getPredTask$CommCostMap().keySet();
				for (Task predTask : predTasks) {

					if (predTask.getAssignedprocessor1() == null) {
						isReady = false;
						break;
					}

				}

				if (isReady) {
					if (!currentTask.getApplication().getTaskPriorityQueue().contains(succTask)){
						currentTask.getApplication().getTaskPriorityQueue().in(succTask);
						
					}
					
				}

			}

		}
	}

	
	

}
