package k_means;

import cn.edu.hnu.esnl.bean.Task;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class KMeansRun {
    private int kNum;                             // 簇的个数
    private int iterMaxTimes = 100;               // 最大迭代次数
    private int iterRunTimes = 0;                 // 实际迭代次数
    private double disDiff = 1e-5;                // 收敛阈值
    private long randomSeed = 42L;                // 固定随机种子

    private List<Task> originalTasks = null;      // 原始数据
    private List<Point> pointList = null;         // 点集
    private DistanceCompute disC = new DistanceCompute();
    private final int DIMENSIONS = 3;             // 特征维度

    public KMeansRun(int k, List<Task> tasks) {
        this.kNum = k;
        this.originalTasks = tasks;
        check();
        init();
    }

    private void check() {
        if (kNum == 0) throw new IllegalArgumentException("k must be > 0");
        if (originalTasks == null || originalTasks.isEmpty()) {
            throw new IllegalArgumentException("Task list cannot be empty");
        }
    }

    private void init() {
        pointList = new ArrayList<>();
        for (int i = 0; i < originalTasks.size(); i++) {
            pointList.add(new Point(i, originalTasks.get(i)));
        }
    }

    // 改进的质心初始化方法
    private Set<Cluster> chooseCenterCluster() {
        Set<Cluster> clusterSet = new HashSet<>();
        Random random = new Random(randomSeed);
        
        // 1. 随机选择第一个中心点
        Point firstCenter = pointList.get(random.nextInt(pointList.size()));
        clusterSet.add(new Cluster(0, firstCenter));
        
        // 2. 选择剩余k-1个中心点，确保分布合理
        for (int i = 1; i < kNum; i++) {
            // 计算每个点到最近中心点的距离平方
            double[] distances = new double[pointList.size()];
            double sum = 0.0;
            
            for (int j = 0; j < pointList.size(); j++) {
                Point point = pointList.get(j);
                double minDist = Double.MAX_VALUE;
                
                for (Cluster cluster : clusterSet) {
                    double dist = disC.getEuclideanDis(point, cluster.getCenter());
                    minDist = Math.min(minDist, dist);
                }
                
                distances[j] = minDist * minDist;
                sum += distances[j];
            }
            
            // 轮盘赌选择下一个中心点
            double r = random.nextDouble() * sum;
            double cumsum = 0.0;
            int nextCenterIdx = 0;
            
            for (int j = 0; j < distances.length; j++) {
                cumsum += distances[j];
                if (cumsum >= r) {
                    nextCenterIdx = j;
                    break;
                }
            }
            
            clusterSet.add(new Cluster(i, pointList.get(nextCenterIdx)));
        }
        
        return clusterSet;
    }

    // 聚类分配
    public void cluster(Set<Cluster> clusterSet) {
        // 重置所有点的簇分配
        for (Point point : pointList) {
            point.setClusterId(-1);
            point.setDist(Double.MAX_VALUE);
        }
        
        // 分配每个点到最近的簇
        for (Cluster cluster : clusterSet) {
            for (Point point : pointList) {
                double dist = disC.getEuclideanDis(point, cluster.getCenter());
                if (dist < point.getDist()) {
                    point.setDist(dist);
                    point.setClusterId(cluster.getId());
                }
            }
        }
        
        // 更新簇成员
        for (Cluster cluster : clusterSet) {
            cluster.getMembers().clear();
            for (Point point : pointList) {
                if (point.getClusterid() == cluster.getId()) {
                    cluster.addPoint(point);
                }
            }
        }
    }

    // 计算新质心
    public boolean calculateCenter(Set<Cluster> clusterSet) {
        boolean ifNeedIter = false;
        
        for (Cluster cluster : clusterSet) {
            List<Point> members = cluster.getMembers();
            if (members.isEmpty()) {
                // 处理空簇：随机选择一个点作为新中心
                Random rand = new Random();
                Point newCenter = pointList.get(rand.nextInt(pointList.size()));
                cluster.setCenter(newCenter);
                ifNeedIter = true;
                continue;
            }
            
            double[] sum = new double[DIMENSIONS];
            for (Point point : members) {
                double[] coords = point.getlocalArray();
                for (int i = 0; i < DIMENSIONS; i++) {
                    sum[i] += coords[i];
                }
            }
            
            double[] newCenterCoords = new double[DIMENSIONS];
            for (int i = 0; i < DIMENSIONS; i++) {
                newCenterCoords[i] = sum[i] / members.size();
            }
            
            Point newCenter = new Point(newCenterCoords);
            double moveDist = disC.getEuclideanDis(cluster.getCenter(), newCenter);
            
            if (moveDist > disDiff) {
                ifNeedIter = true;
            }
            
            cluster.setCenter(newCenter);
        }
        
        return ifNeedIter;
    }

    public Set<Cluster> run() {
        Set<Cluster> clusterSet = chooseCenterCluster();
        iterRunTimes = 0;
        
        while (iterRunTimes < iterMaxTimes) {
            cluster(clusterSet);
            boolean needMoreIter = calculateCenter(clusterSet);
            iterRunTimes++;
            
            if (!needMoreIter) {
                break;
            }
            
            // 检查是否有空簇
            boolean hasEmptyCluster = false;
            for (Cluster cluster : clusterSet) {
                if (cluster.getMembers().isEmpty()) {
                    hasEmptyCluster = true;
                    break;
                }
            }
            if (hasEmptyCluster) {
                // 重新初始化空簇的中心点
                for (Cluster cluster : clusterSet) {
                    if (cluster.getMembers().isEmpty()) {
                        // 找到距离当前所有中心最远的点
                        Point farthest = null;
                        double maxDist = -1;
                        for (Point point : pointList) {
                            double minDist = Double.MAX_VALUE;
                            for (Cluster c : clusterSet) {
                                if (c != cluster) {
                                    double dist = disC.getEuclideanDis(point, c.getCenter());
                                    minDist = Math.min(minDist, dist);
                                }
                            }
                            if (minDist > maxDist) {
                                maxDist = minDist;
                                farthest = point;
                            }
                        }
                        if (farthest != null) {
                            cluster.setCenter(new Point(farthest.getlocalArray()));
                        }
                    }
                }
                needMoreIter = true;
            }
        }
        
        return clusterSet;
    }

    public int getIterTimes() {
        return iterRunTimes;
    }
}