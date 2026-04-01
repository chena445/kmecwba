package cn.edu.hnu.esnl.service.reliability;

import java.util.Set;

import cn.edu.hnu.esnl.bean.Application;
import cn.edu.hnu.esnl.bean.Processor;
import cn.edu.hnu.esnl.bean.Task;

public class ReliabilityModelService {

	public static double NoError(double lamda, double w) {
		double c = Math.exp(-lamda * w);

		return c;
	}

	
	
	/*
	public static int pathNum(Task task) {
	
		int num1 = 1;
		
		int num2 = 0;

		if (!task.getIsExit()) {

			Set<Task> succTasks = task.getSuccTask$CommCostMap().keySet();
			// System.out.println("succTasks:"+succTasks.size());
			num1 = succTasks.size();

			for (Task succTask : succTasks) {

				num2 = num2 + pathNum(succTask) - 1;
			}
		}

		return num1 + num2;
		
		return 1;
	}
*/
	/**
	 * 肖雄仁的算法
	 * 
	 * @param g
	 * @return
	 */
	public static double reliability(Application g) {

		double appR = 1.0;
		for (int i = 0; i < g.getTaskList().size(); i++) {
			Task task = g.getTaskList().get(i);
			double taskR = task.getAR();
			appR = appR * taskR;

		}
		return appR;

	}

	
	/**
	 * 与频率的结合问题
	 * 
	 * @param p
	 * @param f
	 * @return
	 */
	public static double lamdaF(Processor p, Double f) {

		double d = 1;

		double LamdaF = p.getLamda() * Math.pow(10, d * (p.getF_max() - f) / (p.getF_max() - p.getF_ee()));

		return LamdaF;
		
		

	}
	public static double lamdaF(Processor p, Double f, double d) {

	

		double LamdaF = p.getLamda() * Math.pow(10, d * (p.getF_max() - f) / (p.getF_max() - p.getF_ee()));

		return LamdaF;
		
		

	}

	/**
	 * 与频率的结合问题
	 * 
	 * @param task
	 * @param p
	 * @param f
	 * @return
	 */
	public static double NoError(Task task, Processor p, Double f) {

		
		Double LamdaF = lamdaF(p, f);

		double c = Math.exp(-LamdaF * task.getProcessor$CompCostMap().get(p) * p.getF_max() / f);
		return c;
	}
	public static double NoError(Task task, Processor p, Double f, double d) {

		
		Double LamdaF = lamdaF(p, f,d);

		double c = Math.exp(-LamdaF * task.getProcessor$CompCostMap().get(p) * p.getF_max() / f);
		return c;
	}
	
	public static double NoError(Double wik, Processor p, Double f) {

		
		Double LamdaF = lamdaF(p, f);

		double c = Math.exp(-LamdaF * wik * p.getF_max() / f);
		return c;
	}
	
	
	
	public static double NoError(Task task, Processor p) {
		double c = Math.exp(-p.getLamda()* task.getProcessor$CompCostMap().get(p));
		return c;
	}
	public static double NoError(Task task, Processor p, int span) {
		double c = Math.exp(-p.getLamda()* task.getProcessor$CompCostMap().get(p)-span);
		return c;
	}

}
