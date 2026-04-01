package cn.edu.hnu.esnl.bean.view;
/**
 * @author Guoqi Xie E-mail:xgqman@126.com
 * @version JDAS 5.0 Create time：Apr 5, 2016 12:24:35 AM
 */
public class SafeEnergytResult {
	
	private Double energy;
	
	private Boolean result;

	public Double getEnergy() {
		return energy;
	}

	public void setEnergy(Double energy) {
		this.energy = energy;
	}

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	public SafeEnergytResult(Double energy, Boolean result) {
		super();
		this.energy = energy;
		this.result = result;
	}
	
	
}
