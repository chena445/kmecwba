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
import cn.edu.hnu.esnl.service.energy.EnergyService;
import cn.edu.hnu.esnl.system.SystemValue;
import cn.edu.hnu.esnl.util.JdasQueue;

/**
 * @author xgq 
 * E-mail:xgqman@126.com
 * @version ����ʱ�䣺Dec 30, 2014 3:41:26 AM
 * ��˵��:
 *
 */
public class HEFTCANScheduler {

	/**
	 * ��DAG����
	 * @param processorList
	 * @param g
	 */
	public void scheduling(Application g, List<Processor> commomProcessorList) {
		ApplicationServiceBean.initTaskPriorityQueue(g); //入口任务
		
		SystemValue.COMM_AVAIL=0D;
		int seq =1;
		while (!g.getTaskPriorityQueue().isEmpty()) {

			Collections.sort(g.getTaskPriorityQueue(), new Comparator<Task>() {
				public int compare(Task o1, Task o2) {
					return -o1.getRanku().compareTo(o2.getRanku()); // �������ȼ�����

				}
			});

			Task currentTask = g.getTaskPriorityQueue().out();
			currentTask.setSeq(seq);
			seq++;
			if(currentTask.getIsEntry())
				currentTask.setLevel(1);
			if(currentTask.getIsExit())
				g.setLevel(currentTask.getLevel());
			
			
			
			TaskServiceBean.taskAllocationLowerBoundCAN(currentTask, commomProcessorList); // Ϊ������䴦������HEFT����
			
			
			g.getScheduledSequence().add(currentTask);
			LinkedHashMap<Task, Integer> succTask$CommCostMap = currentTask.getSuccTask$CommCostMap();
			Set<Task> succTasks = succTask$CommCostMap.keySet();
			for (Task succTask : succTasks) {
				succTask.setLevel(currentTask.getLevel()+1);
				boolean isReady = true;
				Set<Task> predTasks = succTask.getPredTask$CommCostMap().keySet();
				for (Task predTask : predTasks) {

					if (predTask.getAssignedprocessor1() == null) {
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
		
		Double totalEnergy= 0d;
		Double totalCost =0d;
		for (Task currentTask : g.getScheduledSequence()) {

			double energyTask = EnergyService.getEnergy(currentTask,currentTask.getAssignedprocessor1(),currentTask.getAssignedprocessor1().getF_max() );
			
			currentTask.setEnergy(energyTask);
			if(SystemValue.isPrint)
			System.out.println(currentTask.getName()+" "+currentTask.getAssignedprocessor1()+" "+currentTask.getLower_bound_st()+" "+currentTask.getLower_bound_ft()+" "+currentTask.getEnergy());
			totalEnergy= totalEnergy + energyTask;
			
			double costTask =   (currentTask.getLower_bound_ft()- currentTask.getLower_bound_st())*currentTask.getAssignedprocessor1().getPrice();
			currentTask.setResourceCost(costTask);
			totalCost= totalCost + costTask;
		}
		g.setHEFTEnergy(totalEnergy);
		g.setTotalCost(totalCost);
	}

	
	

}
