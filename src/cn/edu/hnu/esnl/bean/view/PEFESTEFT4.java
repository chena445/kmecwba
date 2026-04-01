package cn.edu.hnu.esnl.bean.view;

import cn.edu.hnu.esnl.bean.Processor;

/**
 * @author Guoqi Xie E-mail:xgqman@126.com
 * @version JDAS 5.0 Create time��Jan 5, 2016 1:31:37 AM
 */
public class PEFESTEFT4 implements Comparable<PEFESTEFT4> {

	private Processor processor;

	private Double energy1;
	
	private Double energy2;
	
	
	private Double energyt;
	private Double frequency;
	
	
	private Double est;
	
	private Double eft ;
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "processor:"+processor+ ",energy1:"+energy1+ ",energy2:"+energy2+ ",frequency:"+frequency+ ", est="+est+", eft="+eft;
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
	

	public Double getEnergyt() {
		return energyt;
	}

	public void setEnergyt(Double energyt) {
		this.energyt = energyt;
	}

	public Double getEnergy1() {
		return energy1;
	}

	public void setEnergy1(Double energy1) {
		this.energy1 = energy1;
	}

	public Double getEnergy2() {
		return energy2;
	}

	public void setEnergy2(Double energy2) {
		this.energy2 = energy2;
	}

	public Double getFrequency() {
		return frequency;
	}

	public void setFrequency(Double frequency) {
		this.frequency = frequency;
	}

	public PEFESTEFT4(Processor processor, Double energy1, Double energy2,Double frequency, Double est, Double eft) {
	
		this.processor = processor;
		this.energy1 = energy1;
		this.energy2 = energy2;
		this.energyt= energy1+ energy2;
		this.frequency = frequency;
		this.est = est;
		this.eft = eft;
	}

	public PEFESTEFT4() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int compareTo(PEFESTEFT4 o) {
		Double x1 = this.getEnergy1()+ this.getEnergy2();
		Double x2 = o.getEnergy1()+o.getEnergy2();
		return x1.compareTo(x2);
		
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((frequency == null) ? 0 : frequency.hashCode());
		result = prime * result + ((processor == null) ? 0 : processor.hashCode());
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
		PEFESTEFT4 other = (PEFESTEFT4) obj;
		if (frequency == null) {
			if (other.frequency != null)
				return false;
		} else if (!frequency.equals(other.frequency))
			return false;
		if (processor == null) {
			if (other.processor != null)
				return false;
		} else if (!processor.equals(other.processor))
			return false;
		return true;
	}

	
	
	
	
}
