package cn.edu.hnu.esnl.bean.view;

import cn.edu.hnu.esnl.bean.Processor;
import cn.edu.hnu.esnl.bean.Task;

/**
 * @author Guoqi Xie E-mail:xgqman@126.com
 * @version JDAS 5.0 Create time��Jan 5, 2016 1:31:37 AM
 */
public class ESTEFT {
	

	
	private Double est;
	
	private Double eft ;
	
	//求通信竞争的时候，需要新增这个参数
	private Integer cij;
	
	private Task criticalTask;

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

	

	public Task getCriticalTask() {
		return criticalTask;
	}

	public void setCriticalTask(Task criticalTask) {
		this.criticalTask = criticalTask;
	}

	public Integer getCij() {
		return cij;
	}

	public void setCij(Integer cij) {
		this.cij = cij;
	}
	public ESTEFT() {
	
		
		
	}
	public ESTEFT( Double est, Double eft) {
	
		
		this.est = est;
		this.eft = eft;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "est="+est+ "  eft="+eft;
	}
	
}
