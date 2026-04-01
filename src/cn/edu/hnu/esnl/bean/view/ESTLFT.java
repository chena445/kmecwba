package cn.edu.hnu.esnl.bean.view;

import cn.edu.hnu.esnl.bean.Processor;

/**
 * @author Guoqi Xie E-mail:xgqman@126.com
 * @version JDAS 5.0 Create time��Jan 5, 2016 1:31:37 AM
 */
public class ESTLFT {
	

	
	private Double est;
	
	private Double lft ;
	

	private Double wik;
	
	private Double span;
	
	
	
	

	public Double getSpan() {
		return span;
	}

	public void setSpan(Double span) {
		this.span = span;
	}

	public Double getEst() {
		return est;
	}

	public void setEst(Double est) {
		this.est = est;
	}

	
	

	

	public Double getLft() {
		return lft;
	}

	public void setLft(Double lft) {
		this.lft = lft;
	}

	

	public Double getWik() {
		return wik;
	}

	public void setWik(Double wik) {
		this.wik = wik;
	}

	public ESTLFT( Double est, Double lft) {
	
		this.est = est;
		this.lft = lft;
	}
	public ESTLFT( Double est, Double lft, double wik) {
		
		this.est = est;
		this.lft = lft;
		this.wik = wik;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "est="+est+ "  eft="+lft+"   wik="+wik;
	}
	
}
