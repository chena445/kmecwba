/*
alg.java
聚类后依据rank序列对每一个任务在其cluster内进行分配
例如：
聚类 结果为2个cluster  预算分为2部分 
cluster1  1  3  7
cluster2  2  4  5  6
rank序列 1 2 3 4 5 6 7

按照rank序列进行任务分配
1->cluster1  2->cluster2 3->cluster1 4->cluster2 5->cluster2 6->cluster2 7->cluster1
 */
package cn.edu.hnu.esnl.app.yuekunchen.cost.access.alg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.edu.hnu.esnl.bean.Application;
import cn.edu.hnu.esnl.bean.Criticality;
import cn.edu.hnu.esnl.bean.Processor;
import cn.edu.hnu.esnl.bean.Task;
import cn.edu.hnu.esnl.bean.view.ESTEFT;
import cn.edu.hnu.esnl.bean.view.StartEndTime;
import cn.edu.hnu.esnl.schedule.assitant.HEFTScheduler;
import cn.edu.hnu.esnl.service.ApplicationServiceBean;
import cn.edu.hnu.esnl.service.TaskServiceBean;
import cn.edu.hnu.esnl.service.energy.EnergyService;
import cn.edu.hnu.esnl.util.DeepCopyUtil;
import cn.edu.hnu.esnl.util.DoubleUtil;
import k_means.Cluster;
import k_means.KMeansRun;
import k_means.Point;


public class alg1 {

	public static Application exe(List<Processor> givenProcessorList, Integer[][] compMartrix, Integer[][] commMartrix, double appCostBudget, Boolean ...print) throws IOException, ClassNotFoundException {

		List<Processor> givenProcessorList0 = DeepCopyUtil.copy(givenProcessorList);
		Application g0 = new Application("F_1", Criticality.S3);
		ApplicationServiceBean.init(g0, givenProcessorList0, compMartrix, commMartrix, 0d, 90d); // 69
		new HEFTScheduler().scheduling(g0, DeepCopyUtil.copy(givenProcessorList0)); // 80

	
		double appCostMin =0;
		double appCostMax =0;
		for (Task currentTask : g0.getScheduledSequence()) {

			
			Double taskMinRC = Double.MAX_VALUE;
			Double taskMaxRC = 0d;

			for (int i = 1; i < givenProcessorList0.size(); i++) {
				Processor p = givenProcessorList0.get(i);

				double vmin = currentTask.getProcessor$CompCostMap().get(p)*p.getPrice();
				
				if (vmin < taskMinRC) {
					taskMinRC = vmin;

				}
				double vmax = currentTask.getProcessor$CompCostMap().get(p)*p.getPrice()*p.getF_max()/p.getF_ee();
				if (vmax > taskMaxRC) {
					taskMaxRC =vmax;
					
				}
			}
			currentTask.setRMin(taskMinRC);
			currentTask.setRMax(taskMaxRC);
			
			appCostMax = appCostMax + taskMaxRC;
			appCostMin = appCostMin + taskMinRC;
			
		}
		//聚类处理
		List<Task> taskList = g0.getScheduledSequence(); // 获取任务列表

		// 创建KMeansRun实例，直接传入Task列表
		KMeansRun kRun = new KMeansRun(50, taskList); // k=2，对数据集进行聚类
		Set<Cluster> clusterSet = kRun.run();

		// //输出我的point id 和 task name
		// for (Cluster cluster : clusterSet) {
		// 	for( Point point : cluster.getMembers()) {
		// 		System.out.println(point.getLocalTask().getName());
		// 	}
		// }
		
        // //输出聚类结果
        //     System.out.println("单次迭代运行次数："+kRun.getIterTimes()); //迭代次数
        //     for (Cluster cluster : clusterSet) {
        //    System.out.println(cluster);
        //     }
        
        //     for (Task currentTask : g0.getScheduledSequence()) {
        //     	 System.out.println("原始序列TASK" + currentTask.getName() );
        //     }
            
        //     for (Cluster cluster : clusterSet) {
        //     	 for (Point point : cluster.getMembers()) {
        //     		 System.out.println("point id:"+point.getId()+"聚类TASK" + point.getLocalTask().getName());
        //    	 }
        // }

 

/*cluster的预算*/ //  （sum(cluster里任务最小预算) / 应用最小预算 ）*应用总预算
/*计算当前cluster的预算*/ 
	  // 遍历所有 Cluster
	   for (Cluster cluster : clusterSet) {
		   double clusterTotalTaskTime_TaskMin = 0; // 总 工作时间 *任务最小预算
           double totalTaskMinBudget = 0d;
	       for (Point point : cluster.getMembers()) {
	    	 //计算当前cluster内的工作时间*任务最小预算
	    	  //System.out.println(point);
	    	   double TaskTime_TaskMin = point.getLocalTask().getAvgW() * point.getLocalTask().getRMin();
	    	   clusterTotalTaskTime_TaskMin = clusterTotalTaskTime_TaskMin + TaskTime_TaskMin;
               double taskMinBudget =point.getLocalTask().getRMin();
        		totalTaskMinBudget  += taskMinBudget;
	       }
	        cluster.setClusterTotalTaskTime_TaskMin(clusterTotalTaskTime_TaskMin);
        	double clusterCostBudget = (totalTaskMinBudget / appCostMin ) * appCostBudget;
        	cluster.setClusterCostBudget(clusterCostBudget);
	   }

       //输出每个cluster预算
    //   for (Cluster cluster : clusterSet) {
    //   	 System.out.println("cluster budget:" + DoubleUtil.format4(cluster.getClusterCostBudget()));
  	//  }
     
	    	  
    /*任务预算分配*/ //依照rank序列对任务 找到对应的cluster 分配对应的cluster预算
		// scheduling tasks
		for (int y = 0; y < g0.getScheduledSequence().size(); y++) {
            Task currentTask = g0.getScheduledSequence().get(y);
            // 找到当前任务所属的cluster
            Cluster taskCluster = null;
            for (Cluster cluster : clusterSet) {
                for (Point point : cluster.getMembers()) {
                    if (point.getLocalTask().equals(currentTask)) {
                        taskCluster = cluster;
                        break;
                    }
                }
                if (taskCluster != null) {
                    break;
                }
            }

            double clusterAssignedCost = 0;
            List<Point> pointsInCluster = new ArrayList<>(taskCluster.getMembers());
            int pointCount = pointsInCluster.size();
            // 计算已分配成本（该任务在cluster中的位置之前的所有任务的AR之和）
            for (int x = 0; x < pointCount; x++) {
                Point point = pointsInCluster.get(x);
                if (point.getLocalTask().equals(currentTask)) {
                    break;
                }
                clusterAssignedCost += point.getLocalTask().getAR();
            }
            // 计算未分配成本（该任务在cluster中的位置之后的所有任务的预期成本）
            double clusterUnassignedCost = 0d;
            for (int z = 0; z < pointCount; z++) {
                Point futurePoint = pointsInCluster.get(z);
                if (futurePoint.getLocalTask().equals(currentTask)) {
                    continue;
                }
                if (pointsInCluster.indexOf(futurePoint) < pointsInCluster.indexOf(new Point(currentTask))) {
                    continue;
                }
                Task futureTask = futurePoint.getLocalTask();
                // 计算AA：任务平均工作时间与cluster工作时间的比值
                double AA = (futureTask.getAvgW() * futureTask.getRMin()) /  taskCluster.getClusterTotalTaskTime_TaskMin();
                double costav = taskCluster.getClusterCostBudget() * AA;
                clusterUnassignedCost += costav;
            }
        
            double taskCostBudget = taskCluster.getClusterCostBudget() - clusterAssignedCost - clusterUnassignedCost;
            if(taskCostBudget>currentTask.getRMax())
                taskCostBudget = currentTask.getRMax();
            if(taskCostBudget<currentTask.getRMin())
                taskCostBudget = currentTask.getRMin();

			double actualST = Double.MAX_VALUE;
			double actualFT = Double.MAX_VALUE;
			double actualCost = 0d;
			double actualEnergy = Double.MAX_VALUE;
			double actualF = 0.00;
			Processor actualProcessor = null;

			for (int ii = 1; ii < givenProcessorList0.size(); ii++) {

				Processor p = givenProcessorList0.get(ii);
				double selectef = 0.00;

				for (double f = p.getF_ee(); f <= p.getF_max(); f = f + 0.01) {

					f = DoubleUtil.floor4(f);

					double cost = currentTask.getProcessor$CompCostMap().get(p) * p.getPrice() * p.getF_max() / f;

					if (cost <= taskCostBudget) {
						selectef = f;
						double energy = EnergyService.getEnergy(currentTask, p, selectef);

						if (energy < actualEnergy) {
							actualEnergy = energy;
							actualCost = cost;
							actualProcessor = p;

							actualF = selectef;
							break;
						}

					}

				}

			}
					
			ESTEFT esteft = TaskServiceBean.computeESTEFT(currentTask, actualProcessor, "s",actualF); //
			actualST = esteft.getEst();
			actualFT = esteft.getEft();
			StartEndTime startEndTime = new StartEndTime(actualST, actualFT);

			actualProcessor.getTask$startEndTimeMap().put(currentTask, startEndTime);
			actualProcessor.getStartEndTime$TaskMap().put(startEndTime, currentTask);
			actualProcessor.getTask$availTimeMap().put(currentTask, actualFT);

			currentTask.setAssignedprocessor1(actualProcessor);
			currentTask.setLower_bound_st(actualST);
			currentTask.setLower_bound_ft(actualFT);

			currentTask.setAR(actualCost);
			currentTask.setAE(actualEnergy);
			currentTask.setF(actualF);

			if (print!=null&&print[0]==true) {
				System.out.println("task:" + currentTask.getName()+", cost constraint:"+DoubleUtil.format4(taskCostBudget) + ", VM:" + currentTask.getAssignedprocessor1() + ", frequency:" + currentTask.getF() + ", AST: "
						+ DoubleUtil.format(currentTask.getLower_bound_st()) + ", AFT: " + DoubleUtil.format(currentTask.getLower_bound_ft())  + ",  actual cost:" + DoubleUtil.format4(currentTask.getAR())+ ", final energy:"
								+ DoubleUtil.format4(currentTask.getAE()));
				System.out.println("=========");
			}
		}
		double totalCost = 0;
		double sl = 0;
		double totalEnergy =0;
		for (int i = 0; i < g0.getScheduledSequence().size(); i++) {
			Task currentTask = g0.getScheduledSequence().get(i);
			totalCost = totalCost + currentTask.getAR();
			double energy = currentTask.getAE();
			totalEnergy = totalEnergy + energy;
			if(currentTask.getLower_bound_ft()>sl)
				sl = currentTask.getLower_bound_ft();
		}     


	 		System.out.println(" alg1_K=50's  cost:" + DoubleUtil.format4(totalCost));
	 		System.out.println(" alg1_K=50's  energy:" + DoubleUtil.format4(totalEnergy));
			System.out.println(" alg1_K=50's  schedule length:" + DoubleUtil.format(sl));
	 		System.out.println ("");
	 		g0.setTotalCost(totalCost);
	 		g0.setTotalE(totalEnergy);
	 		g0.setLower_bound(sl);
	   	
	 		return g0;
	   }
}




