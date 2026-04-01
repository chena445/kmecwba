package cn.edu.hnu.esnl.bean.view;

import java.io.Serializable;

/**
 * @author xgq 
 * E-mail:xgqman@126.com
 * @version ����ʱ�䣺Dec 28, 2014 8:55:31 PM
 * ��˵��:
 *
 */
public class StartEndTime implements Comparable<StartEndTime>,Serializable {
	
	private Double startTime ;
	
	private Double endTime ;
	
	
	
	public StartEndTime(Double startTime, Double endTime) {
		
		this.startTime = startTime;
		this.endTime = endTime;
	}

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

	public int compareTo(StartEndTime o) {
		
		return this.getStartTime().compareTo(o.getStartTime());
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StartEndTime other = (StartEndTime) obj;
		if (endTime == null) {
			if (other.endTime != null)
				return false;
		} else if (!endTime.equals(other.endTime))
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		return true;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "["+startTime+","+endTime+"]";
	}
	
	
}
