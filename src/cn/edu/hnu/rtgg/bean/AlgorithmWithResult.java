package cn.edu.hnu.rtgg.bean;

import cn.edu.hnu.rtgg.dag.system.Configuration;

public class AlgorithmWithResult implements Comparable<AlgorithmWithResult> {

	private String algorithm;
	private Result result;
	private Double tradeoff;

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}	

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}	

	public Double getTradeoff() {
		return tradeoff;
	}

	public void setTradeoff(Double tradeoff) {
		this.tradeoff = tradeoff;
	}

	public AlgorithmWithResult(String algorithm) {
		super();
		this.algorithm = algorithm;
	}

	public AlgorithmWithResult(String algorithm, Result result) {
		super();
		this.algorithm = algorithm;
		this.result = result;
	}

	public void evaluate() {
		this.tradeoff = this.result.getMakespan() * Configuration.ALPHA + this.result.getAllE()
				* Configuration.BETA;
	}

	
	public int compareTo(AlgorithmWithResult o) {
		return this.tradeoff.compareTo(o.getTradeoff());
	}

}
