package k_means;

import java.util.ArrayList;
import java.util.List;

import cn.edu.hnu.esnl.bean.Task;

/**
 * 顺序感知聚类（Sequence-aware clustering）
 * 保证不破坏调度序列顺序
 */
public class SequenceClustering {

    /**
     * 对调度序列进行顺序聚类
     * @param taskList 已调度好的任务序列
     * @return 按顺序排列的 Cluster 列表
     */
    public static List<Cluster> SequenceClustering(List<Task> taskList) {

        List<Cluster> clusterList = new ArrayList<>();

        if (taskList == null || taskList.isEmpty()) {
            return clusterList;
        }

        DistanceCompute distanceCompute = new DistanceCompute();

        // 1️⃣ 计算阈值
        double threshold = calcAdaptiveThreshold(taskList, distanceCompute);

        // 2️⃣ 顺序聚类
        int clusterId = 0;
        Cluster currentCluster = null;
        Point prevPoint = null;

        for (Task task : taskList) {

            Point currentPoint = new Point(task);

            if (prevPoint == null) {
                // 第一个任务，单独成簇
                currentCluster = new Cluster(clusterId++, currentPoint);
                currentCluster.addPoint(currentPoint);
                clusterList.add(currentCluster);
            } else {

                double dist = distanceCompute.getEuclideanDis(prevPoint, currentPoint);
                currentPoint.setDist(dist);

                if (dist <= threshold) {
                    // 相似 → 归入当前 cluster
                    currentCluster.addPoint(currentPoint);
                } else {
                    // 不相似 → 新建 cluster
                    currentCluster = new Cluster(clusterId++, currentPoint);
                    currentCluster.addPoint(currentPoint);
                    clusterList.add(currentCluster);
                }
            }

            prevPoint = currentPoint;
        }

        return clusterList;
    }

    /**
     * 计算相邻任务的平均欧氏距离作为阈值  threshold=μ+λσ  
     */
    private static double calcAdaptiveThreshold(List<Task> taskList,
            DistanceCompute distanceCompute) {

		int n = taskList.size();
		if (n < 2) {
		return Double.MAX_VALUE;
		}
		
		List<Double> distances = new ArrayList<>();
		
		// 1️⃣ 计算相邻任务欧氏距离
		for (int i = 0; i < n - 1; i++) {
		Point p1 = new Point(taskList.get(i));
		Point p2 = new Point(taskList.get(i + 1));
		distances.add(distanceCompute.getEuclideanDis(p1, p2));
		}
		
		// 2️⃣ 计算均值 μ
		double mean = 0.0;
		for (double d : distances) mean += d;
		mean /= distances.size();
		
		// 3️⃣ 计算标准差 σ
		double variance = 0.0;
		for (double d : distances) {
		variance += Math.pow(d - mean, 2);
		}
		variance /= distances.size();
		double std = Math.sqrt(variance);
		
		// 防止 σ = 0
		if (std == 0) {
		return mean;
		}
		
		//λ = 0.6
		return mean + 1.0 * std;
		}

}


