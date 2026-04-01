package cn.edu.hnu.esnl.bean.view;

import cn.edu.hnu.esnl.bean.Processor;

/**
 * @author Guoqi Xie E-mail:xgqman@126.com
 * @version JDAS 5.0 Create time：Apr 11, 2016 9:01:21 PM
 */
public class RELIABILITY {
	private Processor processor;

	private Double reliability;
	
	private Double est;
	
	private Double eft ;
	
	
	
	

	public RELIABILITY(Processor processor, Double reliability, Double est, Double eft) {
		super();
		this.processor = processor;
		this.reliability = reliability;
		this.est = est;
		this.eft = eft;
	}

	public Processor getProcessor() {
		return processor;
	}

	public void setProcessor(Processor processor) {
		this.processor = processor;
	}

	public Double getReliability() {
		return reliability;
	}

	public void setReliability(Double reliability) {
		this.reliability = reliability;
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
