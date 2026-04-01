package cn.edu.hnu.rtgg.bean;

public class AlgorithmWithMakespan implements Comparable<AlgorithmWithMakespan> {

	private String algorithm;
	private Double makespan;

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public Double getMakespan() {
		return makespan;
	}

	public void setMakespan(Double makespan) {
		this.makespan = makespan;
	}
	
	public AlgorithmWithMakespan(String algorithm) {
		this.algorithm=algorithm;		
	}

	
	public int compareTo(AlgorithmWithMakespan o) {
		return this.getMakespan().compareTo(o.getMakespan());
	}

}
