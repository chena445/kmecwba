package cn.edu.hnu.rtgg.bean.ecs;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import cn.edu.hnu.rtgg.bean.ecs.RTGGApplication;
import cn.edu.hnu.rtgg.bean.ecs.AssignedProcessor;
import cn.edu.hnu.rtgg.bean.ecs.DVSProcessor;


public class RTGGTask implements Comparable<RTGGTask> {

	private String name;

	private RTGGApplication application;

	private Boolean isEntry = false;

	private Boolean isExit = false;
	
	private Boolean isCritical = false;
	
	private Boolean isVirtual = false;
	
	private Boolean isProcessed = false;
	
	private Boolean isRescheduled = false;

	private LinkedHashMap<DVSProcessor, Double> processor$CompCostMap = new LinkedHashMap<DVSProcessor, Double>();

	LinkedHashMap<RTGGTask, Double> succTask$CommCostMap = new LinkedHashMap<RTGGTask, Double>();

	LinkedHashMap<RTGGTask, Double> predTask$CommCostMap = new LinkedHashMap<RTGGTask, Double>();
	
	LinkedHashMap<RTGGTask, AssignedProcessor> task$AssignedProcessor = new LinkedHashMap<RTGGTask, AssignedProcessor>();
	
	private Set<RTGGTask> predecessor = new HashSet<RTGGTask>();
	
	private Set<RTGGTask> successors = new HashSet<RTGGTask>();

	private Integer outd = 0; // the out degree

	private Double avgW = 0.0; // the average computation cost

	private Double ranku = 0.0; // upward rank value
	
	private Double rankd = 0.0; // downward rank value
	
	private Double rankSum = 0.0; // ranku+rand

	private Boolean isRank = false;

	private Double makespan_st;

	private Double makespan_ft;
	
	private Double makespan;
	
	private DVSProcessor processor;		
	
	/*
	 * the following parameters are used by ees BEGIN
	 */
	
	private Double newMakespan_st;
	
	private Double newMakespan_ft;
	
	private Double newMakespan = 0.00;
	
	private Double est;  // Time - earliest start time
	
	private Double lst;  // Time - latest started time 
	
	private Double lft;  // Time - latest finished time
	
	private Double slack;		
	
	private Boolean isOptimized = false;
	
	/*
	 * parameters used by ees END
	 */
	
	/*
	 * the following parameters are used by XGQEES BEGIN
	 */
	
	private Double ast = 0.00;
	
	private Double aft = 0.00;
	
	private Double aet = 0.00;
	
	/*
	 * parameters used by XGQEES END
	 */

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public RTGGApplication getApplication() {
		return application;
	}

	public void setApplication(RTGGApplication application) {
		this.application = application;
	}

	public Boolean getIsEntry() {
		return isEntry;
	}

	public void setIsEntry(Boolean isEntry) {
		this.isEntry = isEntry;
	}

	public Boolean getIsExit() {
		return isExit;
	}

	public void setIsExit(Boolean isExit) {
		this.isExit = isExit;
	}

	public Boolean getIsVirtual() {
		return isVirtual;
	}

	public void setIsVirtual(Boolean isVirtual) {
		this.isVirtual = isVirtual;
	}

	public Boolean getIsProcessed() {
		return isProcessed;
	}

	public void setIsProcessed(Boolean isProcessed) {
		this.isProcessed = isProcessed;
	}

	public Boolean getIsRescheduled() {
		return isRescheduled;
	}

	public void setIsRescheduled(Boolean isRescheduled) {
		this.isRescheduled = isRescheduled;
	}

	public LinkedHashMap<DVSProcessor, Double> getProcessor$CompCostMap() {
		return processor$CompCostMap;
	}

	public void setProcessor$CompCostMap(
			LinkedHashMap<DVSProcessor, Double> processor$CompCostMap) {
		this.processor$CompCostMap = processor$CompCostMap;
	}

	public LinkedHashMap<RTGGTask, Double> getSuccTask$CommCostMap() {
		return succTask$CommCostMap;
	}

	public void setSuccTask$CommCostMap(
			LinkedHashMap<RTGGTask, Double> succTask$CommCostMap) {
		this.succTask$CommCostMap = succTask$CommCostMap;
	}

	public LinkedHashMap<RTGGTask, Double> getPredTask$CommCostMap() {
		return predTask$CommCostMap;
	}

	public void setPredTask$CommCostMap(
			LinkedHashMap<RTGGTask, Double> predTask$CommCostMap) {
		this.predTask$CommCostMap = predTask$CommCostMap;
	}		

	public Set<RTGGTask> getPredecessor() {
		return predecessor;
	}

	public void setPredecessor(Set<RTGGTask> predecessor) {
		this.predecessor = predecessor;
	}

	public Set<RTGGTask> getSuccessors() {
		return successors;
	}

	public void setSuccessors(Set<RTGGTask> successors) {
		this.successors = successors;
	}

	public Integer getOutd() {
		return outd;
	}

	public void setOutd(Integer outd) {
		this.outd = outd;
	}

	public Double getAvgW() {
		return avgW;
	}

	public void setAvgW(Double avgW) {
		this.avgW = avgW;
	}

	public Double getRanku() {
		return ranku;
	}

	public void setRanku(Double ranku) {
		this.ranku = ranku;
	}

	public Boolean getIsRank() {
		return isRank;
	}

	public void setIsRank(Boolean isRank) {
		this.isRank = isRank;
	}

	public Double getMakespan_st() {
		return makespan_st;
	}

	public void setMakespan_st(Double makespan_st) {
		this.makespan_st = makespan_st;
	}

	public Double getMakespan_ft() {
		return makespan_ft;
	}

	public void setMakespan_ft(Double makespan_ft) {
		this.makespan_ft = makespan_ft;
	}

	public Double getMakespan() {
		return makespan;
	}

	public void setMakespan(Double makespan) {
		this.makespan = makespan;
	}	

	public DVSProcessor getProcessor() {
		return processor;
	}

	public void setProcessor(DVSProcessor processor) {
		this.processor = processor;
	}

	public Double getNewMakespan_st() {
		return newMakespan_st;
	}

	public void setNewMakespan_st(Double newMakespan_st) {
		this.newMakespan_st = newMakespan_st;
	}

	public Double getNewMakespan_ft() {
		return newMakespan_ft;
	}

	public void setNewMakespan_ft(Double newMakespan_ft) {
		this.newMakespan_ft = newMakespan_ft;
	}	

	public Double getEst() {
		return est;
	}

	public void setEst(Double est) {
		this.est = est;
	}

	public Double getLst() {
		return lst;
	}

	public void setLst(Double lst) {
		this.lst = lst;
	}

	public Double getLft() {
		return lft;
	}

	public void setLft(Double lft) {
		this.lft = lft;
	}

	public Double getSlack() {
		return slack;
	}

	public void setSlack(Double slack) {
		this.slack = slack;
	}

	public Double getNewMakespan() {
		return newMakespan;
	}

	public void setNewMakespan(Double newMakespan) {
		this.newMakespan = newMakespan;
	}	

	public Boolean getIsOptimized() {
		return isOptimized;
	}

	public void setIsOptimized(Boolean isOptimized) {
		this.isOptimized = isOptimized;
	}

	public Double getAst() {
		return ast;
	}

	public void setAst(Double ast) {
		this.ast = ast;
	}

	public Double getAft() {
		return aft;
	}

	public void setAft(Double aft) {
		this.aft = aft;
	}

	public Double getAet() {
		return aet;
	}

	public void setAet(Double aet) {
		this.aet = aet;
	}

	public Boolean getIsCritical() {
		return isCritical;
	}

	public void setIsCritical(Boolean isCritical) {
		this.isCritical = isCritical;
	}
	
	public LinkedHashMap<RTGGTask, AssignedProcessor> getTask$AssignedProcessor() {
		return task$AssignedProcessor;
	}

	public void setTask$AssignedProcessor(
			LinkedHashMap<RTGGTask, AssignedProcessor> task$AssignedProcessor) {
		this.task$AssignedProcessor = task$AssignedProcessor;
	}

	public Double getRankd() {
		return rankd;
	}

	public void setRankd(Double rankd) {
		this.rankd = rankd;
	}

	public Double getRankSum() {
		return rankSum;
	}

	public void setRankSum(Double rankSum) {
		this.rankSum = rankSum;
	}

	public RTGGTask(String name) {
		this.name = name;
	}

	public int compareTo(RTGGTask t) {
		int result = -this.getRanku().compareTo(t.getRanku()); //
		return result;
	}

	@Override
	public String toString() {
		return this.getName();
	}

}
