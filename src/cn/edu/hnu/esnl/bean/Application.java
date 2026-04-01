package cn.edu.hnu.esnl.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import cn.edu.hnu.esnl.util.DeepCopyUtil;
import cn.edu.hnu.esnl.util.JdasQueue;

/**
 * @author xgq 
 * E-mail:xgqman@126.com
 * @version ����ʱ�䣺Nov 25, 2013 1:37:21 AM
 * ��˵��:
 *
 */
public class Application implements Comparable<Application>,  Serializable {

	private String name;

	private Integer criticality;

	private Task entryTask;

	private Task exitTask;

	private List<Task> taskList = new ArrayList<Task>(); //��һ������0
	
	private List<Task> taskOrigialList = new ArrayList<Task>(); //��һ������0

	private JdasQueue<Task> taskPriorityQueue = new JdasQueue<Task>(); ////��һ��������0

	private List<Task> taskPriorityList = new ArrayList<Task>(); //��һ������0
	
	
	private List<Task> scheduledSequence=  new ArrayList<Task>(); //��������������
	

	private List<Task> lftSequence=  new ArrayList<Task>(); //��������������
	

	
	

	private Double makespan  =0d;
	
	private Double arrivalTime =0d;
	
	private Double relativeDeadline = 0d ;
	
	private Double period =0d;
	
	
	private Double slowdown = 0d;  //slowdown
	
	

	
	private Double rankr; //  2012 Fairness�㷨
	
	
	//for periodic task graphs
	private Integer[][] comps;
	
	private Integer[][] comms;
	
	private Integer[][] commRs;
	
	private List<Processor> commonProcessorList;
	
	
	
	
	
	
	//for MTMD
	
	private Double unMakespan;
	
	private Double unAvailTime;
	
	private Double unRemainingTime;
	
	private Double urgency;
	
	
	
	
	
	
	
	
	private Double lower_bound_s0  =0d;
	
	private Double lower_bound_s1  =0d;
	
	private Double lower_bound_s2  =0d;
	
	private Double lower_bound_s3  =0d;
	
    private Double deadlinespan = 0d;
    
	
	
    private Integer level;
    
    
 
	
    
    private Boolean scheduled =false; //DPM algotihm
    
  
    private Double detaEnergy = 0d; // tsusc, lpdc
    
	
  
    private Double totalE=0d;   //fgcs, cpcs
    
    
    private Double maxEnergy;
    
    private Double minEnergy;
    
    private Double givenEnergy;
    
    
    
    private Double minReliability;
    
    private Double maxReliability;
    
    private Double goalReliability;
    
    
	private Double lower_bound  =0d;
	
	private Double totalCost= 0d;
	    
	private Double heftEnergy=0d;  
	
    
    private Double deadline;
    
    private Double reliability;
    
    
    
    private Double laxity;
    
    
    

    
    public Double getLaxity() {
		return laxity;
	}


	public void setLaxity(Double laxity) {
		this.laxity = laxity;
	}

	

	public Double getHeftEnergy() {
		return heftEnergy;
	}


	public void setHeftEnergy(Double heftEnergy) {
		this.heftEnergy = heftEnergy;
	}


	public Integer[][] getCommRs() {
		return commRs;
	}


	public void setCommRs(Integer[][] commRs) {
		this.commRs = commRs;
	}


	public Double getReliability() {
		return reliability;
	}


	public void setReliability(Double reliability) {
		this.reliability = reliability;
	}


	public List<Task> getLftSequence() {
		return lftSequence;
	}


	public void setLftSequence(List<Task> lftSequence) {
		this.lftSequence = lftSequence;
	}


	public List<Task> getTaskOrigialList() {
		return taskOrigialList;
	}


	public void setTaskOrigialList(List<Task> taskOrigialList) {
		this.taskOrigialList = taskOrigialList;
	}


	public Double getDeadline() {
		return deadline;
	}


	public void setDeadline(Double deadline) {
		this.deadline = deadline;
	}


	public Double getMinReliability() {
		return minReliability;
	}


	public void setMinReliability(Double minReliability) {
		this.minReliability = minReliability;
	}


	public Double getMaxReliability() {
		return maxReliability;
	}


	public void setMaxReliability(Double maxReliability) {
		this.maxReliability = maxReliability;
	}


	public Double getGoalReliability() {
		return goalReliability;
	}


	public void setGoalReliability(Double goalReliability) {
		this.goalReliability = goalReliability;
	}


	public Double getMaxEnergy() {
		return maxEnergy;
	}


	public void setMaxEnergy(Double maxEnergy) {
		this.maxEnergy = maxEnergy;
	}


	public Double getMinEnergy() {
		return minEnergy;
	}


	public void setMinEnergy(Double minEnergy) {
		this.minEnergy = minEnergy;
	}


	public Double getGivenEnergy() {
		return givenEnergy;
	}


	public void setGivenEnergy(Double givenEnergy) {
		this.givenEnergy = givenEnergy;
	}


	public Double getTotalE() {
		return totalE;
	}


	public void setTotalE(Double totalE) {
		this.totalE = totalE;
	}


	public Double getDetaEnergy() {
		return detaEnergy;
	}


	public void setDetaEnergy(Double detaEnergy) {
		this.detaEnergy = detaEnergy;
	}


	public Boolean getScheduled() {
		return scheduled;
	}


	public void setScheduled(Boolean scheduled) {
		this.scheduled = scheduled;
	}


	public Double getTotalCost() {
		return totalCost;
	}


	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}


	public Integer getLevel() {
		return level;
	}


	public void setLevel(Integer level) {
		this.level = level;
	}


	public Double getDeadlinespan() {
		return deadlinespan;
	}


	public void setDeadlinespan(Double deadlinespan) {
		this.deadlinespan = deadlinespan;
	}


	public Double getLower_bound_s0() {
		return lower_bound_s0;
	}


	public void setLower_bound_s0(Double lower_bound_s0) {
		this.lower_bound_s0 = lower_bound_s0;
	}


	public Double getLower_bound_s1() {
		return lower_bound_s1;
	}


	public void setLower_bound_s1(Double lower_bound_s1) {
		this.lower_bound_s1 = lower_bound_s1;
	}


	public Double getLower_bound_s2() {
		return lower_bound_s2;
	}


	public void setLower_bound_s2(Double lower_bound_s2) {
		this.lower_bound_s2 = lower_bound_s2;
	}


	public Double getLower_bound_s3() {
		return lower_bound_s3;
	}


	public void setLower_bound_s3(Double lower_bound_s3) {
		this.lower_bound_s3 = lower_bound_s3;
	}


	public Application(String name) {

		this.name = name;
		
	}

	
	public Application(String name, Integer maxCriticalityLevel) {

		this.name = name;
		this.criticality = maxCriticalityLevel;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.name;
	}
	
	
	
	

	

	public Double getHEFTEnergy() {
		return heftEnergy;
	}

	public void setHEFTEnergy(Double heftEnergy) {
		this.heftEnergy = heftEnergy;
	}

	public List<Task> getScheduledSequence() {
		return scheduledSequence;
	}

	public void setScheduledSequence(List<Task> scheduledSequence) {
		this.scheduledSequence = scheduledSequence;
	}

	public Double getUrgency() {
		return urgency;
	}

	public void setUrgency(Double urgency) {
		this.urgency = urgency;
	}


	public List<Processor> getCommonProcessorList() {
		return commonProcessorList;
	}

	public void setCommonProcessorList(List<Processor> commonProcessorList) {
		this.commonProcessorList = commonProcessorList;
	}

	public Integer[][] getComps() {
		return comps;
	}

	public void setComps(Integer[][] comps) {
		this.comps = comps;
	}

	public Integer[][] getComms() {
		return comms;
	}

	public void setComms(Integer[][] comms) {
		this.comms = comms;
	}

	
	public Double getRankr() {
		return rankr;
	}

	public void setRankr(Double rankr) {
		this.rankr = rankr;
	}

	public Double getSlowdown() {
		return slowdown;
	}

	public void setSlowdown(Double slowdown) {
		this.slowdown = slowdown;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

	

	public List<Task> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<Task> taskList) {
		this.taskList = taskList;
	}

	public Task getEntryTask() {
		return entryTask;
	}

	public void setEntryTask(Task entryTask) {
		this.entryTask = entryTask;
	}

	public Task getExitTask() {
		return exitTask;
	}

	public void setExitTask(Task exitTask) {
		this.exitTask = exitTask;
	}

	public JdasQueue<Task> getTaskPriorityQueue() {
		return taskPriorityQueue;
	}

	public void setTaskPriorityQueue(JdasQueue<Task> taskPriorityQueue) {
		this.taskPriorityQueue = taskPriorityQueue;
	}

	public List<Task> getTaskPriorityList() {
		return taskPriorityList;
	}

	public void setTaskPriorityList(List<Task> taskPriorityList) {
		this.taskPriorityList = taskPriorityList;
	}



	public Integer getCriticality() {
		return criticality;
	}



	public void setCriticality(Integer criticality) {
		this.criticality = criticality;
	}



	public Double getLower_bound() {
		return lower_bound;
	}

	public void setLower_bound(Double lower_bound) {
		this.lower_bound = lower_bound;
	}

	public Double getMakespan() {
		return makespan;
	}

	public void setMakespan(Double makespan) {
		this.makespan = makespan;
	}

	public Double getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(Double arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public Double getRelativeDeadline() {
		return relativeDeadline;
	}

	public void setRelativeDeadline(Double relativeDeadline) {
		this.relativeDeadline = relativeDeadline;
	}

	public Double getPeriod() {
		return period;
	}

	public void setPeriod(Double period) {
		this.period = period;
	}

	public Double getUnMakespan() {
		return unMakespan;
	}

	public void setUnMakespan(Double unMakespan) {
		this.unMakespan = unMakespan;
	}

	public Double getUnAvailTime() {
		return unAvailTime;
	}

	public void setUnAvailTime(Double unAvailTime) {
		this.unAvailTime = unAvailTime;
	}

	public Double getUnRemainingTime() {
		return unRemainingTime;
	}

	public void setUnRemainingTime(Double unRemainingTime) {
		this.unRemainingTime = unRemainingTime;
	}

	public int compareTo(Application o) {
	
		return this.getCriticality().compareTo(o.getCriticality()); //�������ȼ�����
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Application other = (Application) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}



	
	
	
}
