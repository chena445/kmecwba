package cn.edu.hnu.rtgg.bean.ecs;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import cn.edu.hnu.rtgg.bean.VRF;


public class RTGGApplication implements Comparable<RTGGApplication> {

	private String name;

	private Integer criticality;

	private RTGGTask entryTask;

	private RTGGTask exitTask;
	
	private LinkedList<RTGGTask> criticalTasks = new LinkedList<RTGGTask>();	
	
	private LinkedHashMap<Integer, Group> taskGroups = new LinkedHashMap<Integer, Group>();		
	
	private Double[][] vrf; // voltage and relative frequency
	
	private Double[][] computationMatrix;
	
	private Double[][] communicationMatrix;

	private List<RTGGTask> taskList = new ArrayList<RTGGTask>(); // the original task list

	private List<RTGGTask> taskPriorityList = new ArrayList<RTGGTask>(); // the priority task list in which each task sorted by ranku
	
	private List<DVSProcessor> processors = new ArrayList<DVSProcessor>();		
	
	private List<VRF> vrfs = new ArrayList<VRF>();

	private LinkedHashMap<RTGGTask, AssignedProcessor> originalSchedulingList = new LinkedHashMap<RTGGTask, AssignedProcessor>();
	
	private LinkedHashMap<RTGGTask, AssignedProcessor> miniEnergySchedulingList = new LinkedHashMap<RTGGTask, AssignedProcessor>();

	private LinkedHashMap<RTGGTask, AssignedProcessor> newSchedulingList = new LinkedHashMap<RTGGTask, AssignedProcessor>();

	private Double lowerbound = 0.00;

	private Double makespan = 0.00;
	
	private Double miniEnergyMakespan = 0.00;

	private Double arrivalTime = 0.00;
	
	private Double originalE = 0.00;
	
	private Double miniE = 0.00;

	private Double deadline = 0.00;
	
	/*
	 * the following parameters are used by ees BEGIN
	 */
	private Double newMakespan = 0.00;
	
	private List<RTGGTask> newTaskList=new ArrayList<RTGGTask>();	
	
	private List<RTGGTask> lftTaskPriorityList=new ArrayList<RTGGTask>();
	
	private LinkedHashMap<RTGGTask, AssignedProcessor> myOptimizedSchedulingList = new LinkedHashMap<RTGGTask, AssignedProcessor>();
	
	private Double originalDeadline = 0.00;
	/*
	 * parameters used by ees END
	 */
	
	/*
	 * the following parameters are used by DEWTS BEGIN
	 */
	
	private List<DVSProcessor> originalProcessors = new ArrayList<DVSProcessor>();
	
	private List<DVSProcessor> utilizationProcessorList = new ArrayList<DVSProcessor>();
	
	/*
	 * parameters used by DEWTS END
	 */
	
	/*
	 * the following parameters are used by XGQEES BEGIN
	 */
	private List<RTGGTask> asendingRankuTaskList = new ArrayList<RTGGTask>();
	/*
	 * parameters used by XGQEES END
	 */

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCriticality() {
		return criticality;
	}

	public void setCriticality(Integer criticality) {
		this.criticality = criticality;
	}

	public RTGGTask getEntryTask() {
		return entryTask;
	}

	public void setEntryTask(RTGGTask entryTask) {
		this.entryTask = entryTask;
	}

	public RTGGTask getExitTask() {
		return exitTask;
	}

	public void setExitTask(RTGGTask exitTask) {
		this.exitTask = exitTask;
	}	

	public LinkedList<RTGGTask> getCriticalTasks() {
		return criticalTasks;
	}

	public void setCriticalTasks(LinkedList<RTGGTask> criticalTasks) {
		this.criticalTasks = criticalTasks;
	}

	public LinkedHashMap<Integer, Group> getTaskGroups() {
		return taskGroups;
	}

	public void setTaskGroups(LinkedHashMap<Integer, Group> taskGroups) {
		this.taskGroups = taskGroups;
	}	

	public Double[][] getVrf() {
		return vrf;
	}

	public void setVrf(Double[][] vrf) {
		this.vrf = vrf;
	}

	public Double[][] getComputationMatrix() {
		return computationMatrix;
	}

	public void setComputationMatrix(Double[][] computationMatrix) {
		this.computationMatrix = computationMatrix;
	}

	public Double[][] getCommunicationMatrix() {
		return communicationMatrix;
	}

	public void setCommunicationMatrix(Double[][] communicationMatrix) {
		this.communicationMatrix = communicationMatrix;
	}

	public List<RTGGTask> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<RTGGTask> taskList) {
		this.taskList = taskList;
	}

	public List<RTGGTask> getTaskPriorityList() {
		return taskPriorityList;
	}

	public void setTaskPriorityList(List<RTGGTask> taskPriorityList) {
		this.taskPriorityList = taskPriorityList;
	}

	public List<DVSProcessor> getProcessors() {
		return processors;
	}

	public void setProcessors(List<DVSProcessor> processors) {
		this.processors = processors;
	}	

	public List<VRF> getVrfs() {
		return vrfs;
	}

	public void setVrfs(List<VRF> vrfs) {
		this.vrfs = vrfs;
	}
	
	public Double getLowerbound() {
		return lowerbound;
	}

	public void setLowerbound(Double lowerbound) {
		this.lowerbound = lowerbound;
	}

	public Double getMakespan() {
		return makespan;
	}

	public void setMakespan(Double makespan) {
		this.makespan = makespan;
	}

	public Double getMiniEnergyMakespan() {
		return miniEnergyMakespan;
	}

	public void setMiniEnergyMakespan(Double miniEnergyMakespan) {
		this.miniEnergyMakespan = miniEnergyMakespan;
	}
	
	public Double getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(Double arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public Double getOriginalE() {
		return originalE;
	}

	public void setOriginalE(Double originalE) {
		this.originalE = originalE;
	}

	public Double getMiniE() {
		return miniE;
	}

	public void setMiniE(Double miniE) {
		this.miniE = miniE;
	}

	public Double getDeadline() {
		return deadline;
	}

	public void setDeadline(Double deadline) {
		this.deadline = deadline;
	}

	public LinkedHashMap<RTGGTask, AssignedProcessor> getOriginalSchedulingList() {
		return originalSchedulingList;
	}

	public void setOriginalSchedulingList(
			LinkedHashMap<RTGGTask, AssignedProcessor> originalSchedulingList) {
		this.originalSchedulingList = originalSchedulingList;
	}

	public LinkedHashMap<RTGGTask, AssignedProcessor> getMiniEnergySchedulingList() {
		return miniEnergySchedulingList;
	}

	public void setMiniEnergySchedulingList(
			LinkedHashMap<RTGGTask, AssignedProcessor> miniEnergySchedulingList) {
		this.miniEnergySchedulingList = miniEnergySchedulingList;
	}

	public LinkedHashMap<RTGGTask, AssignedProcessor> getNewSchedulingList() {
		return newSchedulingList;
	}

	public void setNewSchedulingList(
			LinkedHashMap<RTGGTask, AssignedProcessor> newSchedulingList) {
		this.newSchedulingList = newSchedulingList;
	}

	public Double getNewMakespan() {
		return newMakespan;
	}

	public void setNewMakespan(Double newMakespan) {
		this.newMakespan = newMakespan;
	}

	public List<RTGGTask> getNewTaskList() {
		return newTaskList;
	}

	public void setNewTaskList(List<RTGGTask> newTaskList) {
		this.newTaskList = newTaskList;
	}

	public List<RTGGTask> getLftTaskPriorityList() {
		return lftTaskPriorityList;
	}

	public void setLftTaskPriorityList(List<RTGGTask> lftTaskPriorityList) {
		this.lftTaskPriorityList = lftTaskPriorityList;
	}	

	public LinkedHashMap<RTGGTask, AssignedProcessor> getMyOptimizedSchedulingList() {
		return myOptimizedSchedulingList;
	}

	public void setMyOptimizedSchedulingList(
			LinkedHashMap<RTGGTask, AssignedProcessor> myOptimizedSchedulingList) {
		this.myOptimizedSchedulingList = myOptimizedSchedulingList;
	}	

	public Double getOriginalDeadline() {
		return originalDeadline;
	}

	public void setOriginalDeadline(Double originalDeadline) {
		this.originalDeadline = originalDeadline;
	}

	public List<DVSProcessor> getUtilizationProcessorList() {
		return utilizationProcessorList;
	}

	public void setUtilizationProcessorList(
			List<DVSProcessor> utilizationProcessorList) {
		this.utilizationProcessorList = utilizationProcessorList;
	}

	public List<DVSProcessor> getOriginalProcessors() {
		return originalProcessors;
	}

	public void setOriginalProcessors(List<DVSProcessor> originalProcessors) {
		this.originalProcessors = originalProcessors;
	}

	public List<RTGGTask> getAsendingRankuTaskList() {
		return asendingRankuTaskList;
	}

	public void setAsendingRankuTaskList(List<RTGGTask> asendingRankuTaskList) {
		this.asendingRankuTaskList = asendingRankuTaskList;
	}

	public RTGGApplication(String name) {
		this.name = name;
	}

	
	public int compareTo(RTGGApplication o) {
		return 0;
	}
	
	public RTGGApplication copyTo(RTGGApplication b) {
		b.setEntryTask(this.getEntryTask());
		b.setExitTask(this.getExitTask());		
		b.setComputationMatrix(this.getComputationMatrix());
		b.setCommunicationMatrix(this.getCommunicationMatrix());
		b.setTaskList(this.getTaskList());
		b.setTaskPriorityList(this.getTaskPriorityList());
		b.setProcessors(this.getProcessors());		
		b.setOriginalSchedulingList(this.getOriginalSchedulingList());
		b.setNewSchedulingList(this.getNewSchedulingList());
		b.setLowerbound(this.getLowerbound());
		b.setMakespan(this.getMakespan());
		b.setArrivalTime(this.getArrivalTime());
		b.setOriginalE(this.getOriginalE());
		b.setDeadline(this.getDeadline());
		b.setNewTaskList(this.getNewTaskList());
		b.setLftTaskPriorityList(this.getLftTaskPriorityList());
		b.setMyOptimizedSchedulingList(this.getMyOptimizedSchedulingList());
		b.setOriginalDeadline(this.getOriginalDeadline());
		
		return b;
	}

}
