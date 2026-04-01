package cn.edu.hnu.esnl.bean.view;

import cn.edu.hnu.esnl.bean.Processor;

/**
 * @author Guoqi Xie E-mail:xgqman@126.com
 * @version JDAS 5.0 Create time：Jun 7, 2016 4:49:40 PM
 */
public class TurnOFF implements Comparable<TurnOFF>{
	
	
	private Processor processor;
	
	private Long length;
	
	private Double energy;
	
	

	public TurnOFF(Processor processor, Long length, Double energy) {
		super();
		this.processor = processor;
		this.length = length;
		this.energy = energy;
	}

	public Processor getProcessor() {
		return processor;
	}

	public void setProcessor(Processor processor) {
		this.processor = processor;
	}

	public Long getLength() {
		return length;
	}

	public void setLength(Long length) {
		this.length = length;
	}

	public Double getEnergy() {
		return energy;
	}

	public void setEnergy(Double energy) {
		this.energy = energy;
	}

	public int compareTo(TurnOFF arg0) {
		int result = this.getLength().compareTo(arg0.getLength());
		if(result==0)
			 result = this.getEnergy().compareTo(arg0.getEnergy());
		return result;
	}
	
	
	
	
}
