package cn.edu.hnu.rtgg.bean;

public class VRS {

	private Double voltage;
	private Double factor;

	public Double getVoltage() {
		return voltage;
	}

	public void setVoltage(Double voltage) {
		this.voltage = voltage;
	}

	public Double getFactor() {
		return factor;
	}

	public void setFactor(Double factor) {
		this.factor = factor;
	}

	public VRS(Double voltage, Double factor) {
		this.voltage = voltage;
		this.factor = factor;
	}	
	
	@Override
	public String toString() {
		return this.getVoltage()+" "+this.getFactor();
	}

}
