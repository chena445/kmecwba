package cn.edu.hnu.rtgg.bean.ecs;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import cn.edu.hnu.rtgg.bean.VRF;
import cn.edu.hnu.rtgg.bean.view.RunningTime;


public class DVSProcessor implements Comparable<DVSProcessor> {

	private String name;
	
	private List<VRF> vrfs = new ArrayList<VRF>();

	private LinkedHashMap<RTGGTask, Double> task$AvailTimeMap = new LinkedHashMap<RTGGTask, Double>();
	
	private LinkedHashMap<RTGGTask, RunningTime> task$RunningTimeMap = new LinkedHashMap<RTGGTask, RunningTime>();
		
	/*
	 * the following parameters are used by EES BEGIN
	 */
	
	private LinkedList<RTGGTask> residentTasks = new LinkedList<RTGGTask>();
	/*
	 * parameters used by EES END
	 */
	
	/*
	 * the following parameters are used by DEWTS BEGIN
	 */		
	private Double busyTime = 0.00;		
	
	private boolean poweroff = false;
	
	private boolean sorted = false;
	
	private Double utilization = 0.00;
	
	private boolean locked = false;
	/*
	 * parameters used by DEWTS END
	 */

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}		

	public List<VRF> getVrfs() {
		return vrfs;
	}

	public void setVrfs(List<VRF> vrfs) {
		this.vrfs = vrfs;
	}

	public LinkedHashMap<RTGGTask, Double> getTask$AvailTimeMap() {
		return task$AvailTimeMap;
	}

	public void setTask$AvailTimeMap(LinkedHashMap<RTGGTask, Double> task$AvailTimeMap) {
		this.task$AvailTimeMap = task$AvailTimeMap;
	}	
	
	public LinkedHashMap<RTGGTask, RunningTime> getTask$RunningTimeMap() {
		return task$RunningTimeMap;
	}

	public void setTask$RunningTimeMap(
			LinkedHashMap<RTGGTask, RunningTime> task$RunningTimeMap) {
		this.task$RunningTimeMap = task$RunningTimeMap;
	}
	
	public LinkedList<RTGGTask> getResidentTasks() {
		return residentTasks;
	}

	public void setResidentTasks(LinkedList<RTGGTask> residentTasks) {
		this.residentTasks = residentTasks;
	}
	
	public Double getBusyTime() {
		return busyTime;
	}	

	public void setBusyTime(Double busyTime) {
		this.busyTime = busyTime;
	}

	public boolean isPoweroff() {
		return poweroff;
	}

	public void setPoweroff(boolean poweroff) {
		this.poweroff = poweroff;
	}
	
	public boolean isSorted() {
		return sorted;
	}

	public void setSorted(boolean sorted) {
		this.sorted = sorted;
	}
	
	public Double getUtilization() {
		return utilization;
	}

	public void setUtilization(Double utilization) {
		this.utilization = utilization;
	}	

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public DVSProcessor(String name) {
		this.name=name;
	}
	
	@Override
	public String toString() {
		return this.getName();
	}

	/*
	@Override
	public int compareTo(DVSProcessor o) {
		return this.getName().compareTo(o.getName());
	}
	*/
	
	
	public int compareTo(DVSProcessor o) {
		int result = -this.getUtilization().compareTo(o.getUtilization());
		return result;
	}
	
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DVSProcessor other = (DVSProcessor) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	public void print() {
		for(RTGGTask task:this.getTask$RunningTimeMap().keySet()) {
			//System.out.println("Processor="+this.getName()+",Task="+task+",RunningTime="+this.getTask$RunningTimeMap().get(task).getStartTime()+","+this.getTask$RunningTimeMap().get(task).getEndTime());
		}		
	}

}
