package cn.edu.hnu.rtgg.bean;

public class VRF {

	private Double voltage;
	private Double frequency;

	public Double getVoltage() {
		return voltage;
	}

	public void setVoltage(Double voltage) {
		this.voltage = voltage;
	}

	public Double getFrequency() {
		return frequency;
	}

	public void setFrequency(Double frequency) {
		this.frequency = frequency;
	}

	public VRF(Double voltage, Double frequency) {
		super();
		this.voltage = voltage;
		this.frequency = frequency;
	}

	@Override
	public String toString() {
		return this.getVoltage() + " " + this.getFrequency();
	}

}
