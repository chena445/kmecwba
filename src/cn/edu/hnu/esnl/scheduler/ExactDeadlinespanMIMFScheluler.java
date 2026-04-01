package cn.edu.hnu.esnl.scheduler;


import java.util.List;


import cn.edu.hnu.esnl.bean.Application;
import cn.edu.hnu.esnl.bean.Criticality;
import cn.edu.hnu.esnl.bean.Processor;
import cn.edu.hnu.esnl.bean.Task;

import cn.edu.hnu.esnl.scheduler.utils.ExactDeadlinespanMIMFSchelulerUtil;

import cn.edu.hnu.esnl.service.ApplicationServiceBean;

import cn.edu.hnu.esnl.util.JdasQueue;

/**
 * @author xgq 
 * E-mail:xgqman@126.com
 * @version ����ʱ�䣺Dec 30, 2014 3:41:26 AM
 * ��˵��:
 *
 */
public class ExactDeadlinespanMIMFScheluler {

	
	public static void scheduling(List<Application> gs, List<Processor> commomProcessorList) {

		int currentTime = 0;
		
		for (Application g : gs) {
			ApplicationServiceBean.initTaskPriorityQueue(g);
		}

		
		
		ExactDeadlinespanMIMFSchelulerUtil.taskAllocation("ExactDeadlinespanMIMFScheluler调度",gs, commomProcessorList, currentTime);
			

	}
	
	
	
	
	

}
