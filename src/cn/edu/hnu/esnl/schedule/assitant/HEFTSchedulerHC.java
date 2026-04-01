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
import cn.edu.hnu.esnl.service.TaskServiceBeanHC;
import cn.edu.hnu.esnl.service.energy.EnergyService;
import cn.edu.hnu.esnl.service.reliability.ReliabilityModelService;
import cn.edu.hnu.esnl.system.SystemValue;
import cn.edu.hnu.esnl.util.JdasQueue;

/**
 * @author xgq 
 * E-mail:xgqman@126.com
 * @version ����ʱ�䣺Dec 30, 2014 3:41:26 AM
 * ��˵��:
 *
 */
public class HEFTSchedulerHC {

	/**
	 * ��DAG����
	 * @param processorList
	 * @param g
	 */
	public void scheduling(Application g, List<String> opendProcessorNames, List<Processor> commomProcessorList, Boolean ... show) {
		ApplicationServiceBean.initTaskPriorityQueue(g); //入口任务
		
		
		g.getTaskList().remove(0);
		Collections.sort(g.getTaskList(), new Comparator<Task>() {
			public int compare(Task o1, Task o2) {
				return -o1.getRanku().compareTo(o2.getRanku()); // �������ȼ�����

			}
		});
		
		
		

		while (!g.getTaskPriorityQueue().isEmpty()) {

			Collections.sort(g.getTaskPriorityQueue(), new Comparator<Task>() {
				public int compare(Task o1, Task o2) {
					return -o1.getRanku().compareTo(o2.getRanku()); // �������ȼ�����

				}
			});

			Task currentTask = g.getTaskPriorityQueue().out();
			g.getTaskList().remove(currentTask); //delete
			
			if(currentTask.getIsEntry())
				currentTask.setLevel(1);
			
			if(currentTask.getIsExit())
				g.setLevel(currentTask.getLevel());
			
			
			
			TaskServiceBeanHC.taskAllocationLowerBound(currentTask,opendProcessorNames, commomProcessorList); // Ϊ������䴦������HEFT����
			
			
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
			if(g.getTaskPriorityQueue().isEmpty()&&g.getTaskList().size()>0){
				
				g.getTaskPriorityQueue().in(g.getTaskList().get(0));

			}

		}
		
		Double totalEnergy= 0d;
		Double totalCost =0d;
		Double totalReliability =1d;
		g.setLower_bound(0d);
		for (Task currentTask : g.getScheduledSequence()) {

			
			double currentTaskEnergy = EnergyService.getEnergy(currentTask,currentTask.getAssignedprocessor1(), currentTask.getAssignedprocessor1().getF_max());
		
			currentTask.setEnergy(currentTaskEnergy);
			if(show!=null&&show.length>0&&show[0]==true){
				//System.out.println(currentTask.getName()+" ranku:"+currentTask.getRanku()+" assinged processor:"+currentTask.getAssignedprocessor1()+" AST:"+currentTask.getLower_bound_st()+" AFT:"+currentTask.getLower_bound_ft()+" frequency:"+currentTask.getF()+" energy:"+currentTask.getEnergy());
				
			}
			totalEnergy= totalEnergy + currentTaskEnergy;
			double costTask =   (currentTask.getLower_bound_ft()- currentTask.getLower_bound_st())*currentTask.getAssignedprocessor1().getPrice();
			currentTask.setResourceCost(costTask);
			totalCost= totalCost + costTask;
			if(currentTask.getLower_bound_ft()>g.getLower_bound())
				g.setLower_bound(currentTask.getLower_bound_ft());
			double currentTaskReliability = 1;
			try{
				 currentTaskReliability = ReliabilityModelService.NoError(currentTask, currentTask.getAssignedprocessor1());
			}
			catch(Exception e){
				
			}
			totalReliability =  totalReliability * currentTaskReliability;
		}
		
		g.setHEFTEnergy(totalEnergy);
		g.setReliability(totalReliability);
		g.setTotalCost(totalCost);
	}

	
	

}
