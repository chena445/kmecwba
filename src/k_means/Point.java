package k_means;


import cn.edu.hnu.esnl.bean.Task;

public class Point {
    private Task localTask; 
    private double[] localArray;
    private int id;
    private int clusterId;  // 标识属于哪个类中心。
    private double dist;    // 标识和所属类中心的距离。
    private boolean isAssigned = false; // 是否为类中心点

    
    public Point(int id, Task localTask) {
        this.id = id;
        this.localTask = localTask;
        // 使用Task的三个属性初始化数组
        this.localArray = new double[]{
            localTask.getAvgW(), 
            localTask.getRMax(), 
            localTask.getRMin()
        };
    }

    public Point(Task localTask) {
        this.id = -1; // 表示不属于任意一个类
        this.localTask = localTask;
        // 使用Task的三个属性初始化数组
        this.localArray = new double[]{
            localTask.getAvgW(),
            localTask.getRMax(),
            localTask.getRMin()
        };
    }

    public Point(double[] localArray) {
        this.id = -1; // 表示不属于任意一个类
        this.localArray = localArray;
        // localTask可以为null或需要额外处理
        this.localTask = null; // 或者根据数组值创建新的Task对象
    }

    public double[] getlocalArray() {
        return localArray;
    }

    public int getId() {
        return id;
    }

    public void setClusterId(int clusterId) {
        this.clusterId = clusterId;
    }

    public int getClusterid() {
        return clusterId;
    }

    public double getDist() {
        return dist;
    }

    public void setDist(double dist) {
        this.dist = dist;
    }

    public boolean isAssigned() {
        return isAssigned;
    }

    public void setAssigned(boolean assigned) {
        this.isAssigned = assigned;
    }

    @Override
    public String toString() {
        String result = "Point_id=" + id + "  [";
        for (int i = 0; i < localArray.length; i++) {
            result += localArray[i] + " ";
        }
        return result.trim()+"] clusterId: "+clusterId+" dist: "+dist;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass())
            return false;

        Point point = (Point) obj;
        if (point.localArray.length != localArray.length)
            return false;

        for (int i = 0; i < localArray.length; i++) {
            if (Double.compare(point.localArray[i], localArray[i]) != 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        double x = localArray[0];
        double y = localArray[localArray.length - 1];
        long temp = x != +0.0d ? Double.doubleToLongBits(x) : 0L;
        int result = (int) (temp ^ (temp >>> 32));
        temp = y != +0.0d ? Double.doubleToLongBits(y) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

	public Task getLocalTask() {
		return localTask;
	}
		
	
}




