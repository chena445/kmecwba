package cn.edu.hnu.rtgg.bean;

public class Result {

	private Double makespan = 0.00;
	private Double allE = 0.00;

	public Result() {
		this.makespan = 0.00;
		this.allE = 0.00;
	}

	public Result(Double makespan, Double allE) {
		this.makespan = makespan;
		this.allE = allE;
	}

	public Double getMakespan() {
		return makespan;
	}

	public void setMakespan(Double makespan) {
		this.makespan = makespan;
	}

	public Double getAllE() {
		return allE;
	}

	public void setAllE(Double allE) {
		this.allE = allE;
	}

}
