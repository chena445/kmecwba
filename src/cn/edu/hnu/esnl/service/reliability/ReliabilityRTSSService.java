package cn.edu.hnu.esnl.service.reliability;

import java.util.Set;

import cn.edu.hnu.esnl.bean.Application;
import cn.edu.hnu.esnl.bean.Processor;
import cn.edu.hnu.esnl.bean.Task;

public class ReliabilityRTSSService {

	public static double NoError(double lamda, int w) {
		double c = Math.exp(-lamda * w);

		return c;
	}

	public static double NoError(Task task, Processor p) {
		
		double c = Math.exp(-p.getLamda() * task.getProcessor$CompCostMap().get(p));
		return c;
	}

	
	public static int pathNum(Task currentTask, Task endTask) {
		int num1 = 1;
		int num2 = 0;

		if (!currentTask.equals(endTask)) {

			Set<Task> succTasks = currentTask.getSuccTask$CommCostMap().keySet();
			
			num1 = succTasks.size();

			for (Task succTask : succTasks) {

				num2 = num2 + pathNum(succTask,endTask) - 1;
			}
		}

		return num1 + num2;

	}

	
	public static double reliability(Task currentTask, Processor currP, double lamdaComm) {
		
		double r1 = 1.0;
		
		double taskR =NoError(currentTask, currP);
		r1 = r1 * taskR;
		Set<Task> predTasks = currentTask.getPredTask$CommCostMap().keySet();
		for(Task predTask:predTasks){
			Processor predP = predTask.getAssignedprocessor1();
			r1 = r1* reliability(predTask,predP,lamdaComm);
			
			int cij =currentTask.getPredTask$CommCostMap().get(predTask);
			if(currP!=predP){
				r1 = r1*NoError(lamdaComm,cij);
			}
				
			
		}
		
		return r1;

	}

}

