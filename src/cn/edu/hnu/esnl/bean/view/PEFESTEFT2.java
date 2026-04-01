package cn.edu.hnu.esnl.bean.view;

import cn.edu.hnu.esnl.bean.Processor;

/**
 * @author Guoqi Xie E-mail:xgqman@126.com
 * @version JDAS 5.0 Create time��Jan 5, 2016 1:31:37 AM
 */
public class PEFESTEFT2 implements Comparable<PEFESTEFT2> {

	private Processor processor;

	private Double energy;
	
	private Double frequency;
	
	
	private Double est;
	
	private Double eft ;
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "processor:"+processor+ ",energy:"+energy+ ",frequency:"+frequency+ ", est="+est+", eft="+eft;
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

	public Processor getProcessor() {
		return processor;
	}

	public void setProcessor(Processor processor) {
		this.processor = processor;
	}

	public Double getEnergy() {
		return energy;
	}

	public void setEnergy(Double energy) {
		this.energy = energy;
	}

	public Double getFrequency() {
		return frequency;
	}

	public void setFrequency(Double frequency) {
		this.frequency = frequency;
	}

	public PEFESTEFT2(Processor processor, Double energy, Double frequency, Double est, Double eft) {
		super();
		this.processor = processor;
		this.energy = energy;
		this.frequency = frequency;
		this.est = est;
		this.eft = eft;
	}

	public PEFESTEFT2() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int compareTo(PEFESTEFT2 o) {
		Double x1 = this.getEnergy()*this.getEft();
		Double x2 = o.getEnergy()*o.getEft();
		return x1.compareTo(x2);
	}

	
	
	
	
}
