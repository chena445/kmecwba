package k_means;
import java.util.ArrayList;
import java.util.List;

import cn.edu.hnu.esnl.bean.Processor;

public class Cluster {
    private int id;// 标识
    private double clusterCostBudget; //预算
    private double clusterTotalTaskTime_TaskMin; //总的 w * RMin
	private Point center;// 中心
    private List<Point> members = new ArrayList<Point>();// 成员

    public Cluster(int id, Point center) {
        this.id = id;
        this.center = center;
    }

    public Cluster(int id, Point center, List<Point> members) {
        this.id = id;
        this.center = center;
        this.members = members;
    }

    public void addPoint(Point newPoint) {
        if (!members.contains(newPoint)){
            members.add(newPoint);
        }else{
            System.out.println("样本数据点 {"+newPoint.toString()+"} 已经存在！");
        }
    }
    
    public double getClusterCostBudget() {
		return clusterCostBudget;
	}

	public void setClusterCostBudget(double clusterCostBudget) {
		this.clusterCostBudget = clusterCostBudget;
	}
	
	public double getClusterTotalTaskTime_TaskMin() {
		return clusterTotalTaskTime_TaskMin;
	}

	public void setClusterTotalTaskTime_TaskMin(double clusterTotalTaskTime_TaskMin) {
		this.clusterTotalTaskTime_TaskMin = clusterTotalTaskTime_TaskMin;
	}

    public int getId() {
        return id;
    }

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    public List<Point> getMembers() {
        return members;
    }

    @Override
    public String toString() {
        String toString = "Cluster \n" + "Cluster_id=" + this.id + ", center:{" + this.center.toString()+"}";
        for (Point point : members) {
            toString+="\n"+point.toString();
        }
        return toString+"\n";
    }
    
    //增加字段
 // Cluster.java 中新增
    private Processor bindProcessor;
    private double bindFrequency;

    public Processor getBindProcessor() {
        return bindProcessor;
    }
    public void setBindProcessor(Processor bindProcessor) {
        this.bindProcessor = bindProcessor;
    }

    public double getBindFrequency() {
        return bindFrequency;
    }
    public void setBindFrequency(double bindFrequency) {
        this.bindFrequency = bindFrequency;
    }

    
}