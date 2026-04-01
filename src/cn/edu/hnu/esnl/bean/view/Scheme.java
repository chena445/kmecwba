package cn.edu.hnu.esnl.bean.view;

import java.util.ArrayList;
import java.util.List;

import cn.edu.hnu.esnl.bean.Processor;

/**
 * @author Guoqi Xie E-mail:xgqman@126.com
 * @version JDAS 5.0 Create time：Oct 18, 2016 9:14:06 PM
 * 	// 4个L1,  2个L1 1个L2，  2个L2，   1个L1， 1个L3
 */
public class Scheme {
	private Double developmentCost;
	
	private Double reliability; //可靠性没有考虑
	
	
	private List<String> levles = new ArrayList<String>();
	
	private List<Processor> processors = new ArrayList<Processor>();
	
	private List<Double> reliabilies = new ArrayList<Double>();
	
	private List<Double> dcs = new ArrayList<Double>();
	
	private Integer seq;
	
	private Integer number;
	
	
	private List<StartEndTime> startEndTimes= new ArrayList<StartEndTime>();
	
	
	
	

	public List<StartEndTime> getStartEndTimes() {
		return startEndTimes;
	}

	public void setStartEndTimes(List<StartEndTime> startEndTimes) {
		this.startEndTimes = startEndTimes;
	}

	
	public List<Double> getReliabilies() {
		return reliabilies;
	}

	public void setReliabilies(List<Double> reliabilies) {
		this.reliabilies = reliabilies;
	}

	public List<Double> getDcs() {
		return dcs;
	}

	public void setDcs(List<Double> dcs) {
		this.dcs = dcs;
	}

	
	public List<String> getLevles() {
		return levles;
	}

	public void setLevles(List<String> levles) {
		this.levles = levles;
	}

	public List<Processor> getProcessors() {
		return processors;
	}

	public void setProcessors(List<Processor> processors) {
		this.processors = processors;
	}

	public Double getDevelopmentCost() {
		return developmentCost;
	}

	public void setDevelopmentCost(Double totalDevelopmentCost) {
		this.developmentCost = totalDevelopmentCost;
	}

	public Double getReliability() {
		return reliability;
	}

	public void setReliability(Double reliability) {
		this.reliability = reliability;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}
	
	
	
	
}
