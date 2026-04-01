package cn.edu.hnu.esnl.bean.view;

import cn.edu.hnu.esnl.bean.Processor;

/**
 * @author Guoqi Xie E-mail:xgqman@126.com
 * @version JDAS 5.0 Create time：Apr 11, 2016 9:01:21 PM
 */
public class COST {
	private Processor processor;

	private Double cost;
	
	private Double est;
	
	private Double eft ;
	
	
	
	

	public COST(Processor processor, Double cost, Double est, Double eft) {
		super();
		this.processor = processor;
		this.cost = cost;
		this.est = est;
		this.eft = eft;
	}

	public Processor getProcessor() {
		return processor;
	}

	public void setProcessor(Processor processor) {
		this.processor = processor;
	}

	
	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public Double getEst() {
		return est;
	}

	public void setEst(Double est) {
		this.est = est;
	}

	public Double getEft() {
		return eft;
	}

	public void setEft(Double eft) {
		this.eft = eft;
	}
	
	
	
	
}
