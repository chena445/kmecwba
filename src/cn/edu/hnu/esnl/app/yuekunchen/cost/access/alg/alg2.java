/*
alg2.java
聚类后的预算分配算法
聚类后
cluster 1  1  3  7
cluster 2  2 4 5 6
任务存在依赖顺序 判断其所有父任务是否分配  即commMartrix[i][j] > 0 ，任务i是否分配
如果分配 则可以接着进行任务分配 否则换到另一个cluster
例如
依赖顺序
    2->4
1->        ->6
    3->5->7
先对cluster1的任务进行分配
任务1  可以分配
任务3  可以分配
任务7  其父任务5未分配  则换到另一个cluster
然后对cluster2的任务进行分配
任务2  可以分配
任务4  可以分配
任务5  可以分配
任务6  父任务7未分配 则跳过 换到cluster1
回到cluster1 分配任务7
回到cluster2 分配任务6
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


public class alg2 {

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

		// 聚类处理
		List<Task> taskList = g0.getScheduledSequence(); // 获取任务列表
		KMeansRun kRun = new KMeansRun(150, taskList); // k=2，对数据集进行聚类
		Set<Cluster> clusterSet = kRun.run();

		// // 输出聚类结果
		// System.out.println("单次迭代运行次数："+kRun.getIterTimes());
		// for (Cluster cluster : clusterSet) {
		// 	System.out.println(cluster);
		// }
		// for (Task currentTask : g0.getScheduledSequence()) {
		// 	System.out.println("原始序列TASK" + currentTask.getName() );
		// }
		// for (Cluster cluster : clusterSet) {
		// 	System.out.println("Cluster " + cluster.getId() + " 包含的任务有：");
		// 	for (Point point : cluster.getMembers()) {
		// 		System.out.println("聚类TASK" + point.getLocalTask().getName()+point.getLocalTask().getId());
		// 	}
		// }

		// cluster的预算
		for (Cluster cluster : clusterSet) {
			double clusterTotalTaskTime_TaskMin = 0;
			double totalTaskMinBudget = 0d;
			for (Point point : cluster.getMembers()) {
				double TaskTime_TaskMin = point.getLocalTask().getAvgW() * point.getLocalTask().getRMin();
				clusterTotalTaskTime_TaskMin = clusterTotalTaskTime_TaskMin + TaskTime_TaskMin;
				double taskMinBudget =point.getLocalTask().getRMin();
				totalTaskMinBudget  += taskMinBudget;
			}
			cluster.setClusterTotalTaskTime_TaskMin(clusterTotalTaskTime_TaskMin);
			double clusterCostBudget = (totalTaskMinBudget / appCostMin ) * appCostBudget;
			cluster.setClusterCostBudget(clusterCostBudget);
		}

		// // 输出每个cluster预算
		// for (Cluster cluster : clusterSet) {
		// 	System.out.println("cluster budget:" + DoubleUtil.format4(cluster.getClusterCostBudget()));
		// }

		//输出 gettaskid方法测试
		// for (Task currentTask : g0.getScheduledSequence()) {
		// 	System.out.println("TASK" + currentTask.getName() + " ID:" + currentTask.getId());
		// 	//输出矩阵commMartrix[currentTask.getId()][currentTask.getId()]
		// 	for (int j = 0; j < commMartrix.length; j++) {
		// 		if (commMartrix[currentTask.getId()][j] != null && commMartrix[currentTask.getId()][j] > 0) {
		// 			System.out.println(commMartrix[currentTask.getId()][j]);
		// 		}
		// 	}
		// }
		// 任务预算分配
		int totalTasks  = g0.getTaskOrigialList().size();
		int assignedTasks = 0;

		List<List<Point>> clusterPointLists = new ArrayList<>();
		for (Cluster cluster : clusterSet) {
			clusterPointLists.add(new ArrayList<>(cluster.getMembers()));
		}
		int[] clusterIndexes = new int[clusterPointLists.size()];
		for (int i = 0; i < clusterIndexes.length; i++) clusterIndexes[i] = 0;
		java.util.Map<Integer, Point> taskId2Point = new java.util.HashMap<>();
		for (List<Point> plist : clusterPointLists) {
			for (Point p : plist) {
				taskId2Point.put(p.getLocalTask().getId(), p);
			}
		}
		while (assignedTasks < totalTasks) {
			boolean progress = false;
			for (int cidx = 0; cidx < clusterPointLists.size(); cidx++) {
				List<Point> pointsInCluster = clusterPointLists.get(cidx);
				int pointCount = pointsInCluster.size();
				while (clusterIndexes[cidx] < pointCount) {
					Point currentPoint = pointsInCluster.get(clusterIndexes[cidx]);
					Task currentTask = currentPoint.getLocalTask();
					// 检查所有父任务是否已分配
					boolean allParentsAssigned = true;
					for (int parentId = 0; parentId < commMartrix.length; parentId++) {
						if (commMartrix[parentId][currentTask.getId()] != null && commMartrix[parentId][currentTask.getId()] > 0) {
							Point parentPoint = taskId2Point.get(parentId);
							if (parentPoint != null && !parentPoint.isAssigned()) {
								allParentsAssigned = false;
								break;
							}
						}
					}
					if (!allParentsAssigned) {
						break;
					}
					// 计算已分配成本
					double clusterAssignedCost = 0;
					for (int x = 0; x < clusterIndexes[cidx]; x++) {
						clusterAssignedCost += pointsInCluster.get(x).getLocalTask().getAR();
					}
					// 计算未分配成本
					double clusterUnassignedCost = 0d;
					for (int z = clusterIndexes[cidx] + 1; z < pointCount; z++) {
						Point futurePoint = pointsInCluster.get(z);
						Task futureTask = futurePoint.getLocalTask();
						double AA = (futureTask.getAvgW() * futureTask.getRMin()) / clusterSet.toArray(new Cluster[0])[cidx].getClusterTotalTaskTime_TaskMin();
						double costav = clusterSet.toArray(new Cluster[0])[cidx].getClusterCostBudget() * AA;
						clusterUnassignedCost += costav;
					}
					double taskCostBudget = clusterSet.toArray(new Cluster[0])[cidx].getClusterCostBudget() - clusterAssignedCost - clusterUnassignedCost;
					if (taskCostBudget > currentTask.getRMax())
						taskCostBudget = currentTask.getRMax();
					if (taskCostBudget < currentTask.getRMin())
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
					ESTEFT esteft = TaskServiceBean.computeESTEFT(currentTask, actualProcessor, "s", actualF);
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

					currentPoint.setAssigned(true);
					assignedTasks++;
					clusterIndexes[cidx]++;
					progress = true;

					if (print != null && print.length > 0 && print[0] == true) {
						System.out.println("task:" + currentTask.getName() + ", cost constraint:" + DoubleUtil.format4(taskCostBudget) + ", VM:" + currentTask.getAssignedprocessor1() + ", frequency:" + currentTask.getF() + ", AST: "
								+ DoubleUtil.format(currentTask.getLower_bound_st()) + ", AFT: " + DoubleUtil.format(currentTask.getLower_bound_ft()) + ",  actual cost:" + DoubleUtil.format4(currentTask.getAR()) + ", final energy:"
								+ DoubleUtil.format4(currentTask.getAE()));
						System.out.println("=========");
					}
					// 继续处理下一个点
				}
			}
			if (!progress) {
				//System.err.println("任务分配出现死锁，可能存在循环依赖！");
				break;
			}
		}

		double totalCost = 0;
		double sl = 0;
		double totalEnergy = 0;
		// 按 cluster 到 point 遍历
		for (Cluster cluster : clusterSet) {
			for (Point point : cluster.getMembers()) {
				Task currentTask = point.getLocalTask();
				if (currentTask.getAR() != null && currentTask.getAE() != null) {
					totalCost += currentTask.getAR();
					double energy = currentTask.getAE();
					totalEnergy += energy;
					if (currentTask.getLower_bound_ft() > sl)
						sl = currentTask.getLower_bound_ft();
				} else {
					System.err.println("任务 " + currentTask.getName() + " 未分配，AR 或 AE 为空！");
				}
			}
		}

		System.out.println(" alg2_K=150's  cost:" + DoubleUtil.format4(totalCost));
		System.out.println(" alg2_K=150's  energy:" + DoubleUtil.format4(totalEnergy));
		System.out.println(" alg2_K=150's  schedule length:" + DoubleUtil.format(sl));
		System.out.println ("");

		g0.setTotalCost(totalCost);
		g0.setTotalE(totalEnergy);
		g0.setLower_bound(sl);

		return g0;
	}
}









