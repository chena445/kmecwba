package cn.edu.hnu.rtgg.bean;

public class AlgorithmWithEnergy implements Comparable<AlgorithmWithEnergy> {

	private String algorithm;
	private Double energy;

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public Double getEnergy() {
		return energy;
	}

	public void setEnergy(Double energy) {
		this.energy = energy;
	}
	
	public AlgorithmWithEnergy(String algorithm) {
		this.algorithm=algorithm;
	}

	
	public int compareTo(AlgorithmWithEnergy o) {
		return this.getEnergy().compareTo(o.getEnergy());
	}

}
