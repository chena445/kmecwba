package cn.edu.hnu.rtgg.bean.view;

public class RunningTime implements Comparable<RunningTime> {

	private Double startTime;
	private Double endTime;

	public Double getStartTime() {
		return startTime;
	}

	public void setStartTime(Double startTime) {
		this.startTime = startTime;
	}

	public Double getEndTime() {
		return endTime;
	}

	public void setEndTime(Double endTime) {
		this.endTime = endTime;
	}

	public RunningTime(Double startTime, Double endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}

	
	public int compareTo(RunningTime o) {
		return this.getStartTime().compareTo(o.getStartTime());
	}

}
