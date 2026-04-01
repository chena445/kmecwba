package cn.edu.hnu.esnl.bean.view;

import java.io.Serializable;

import cn.edu.hnu.esnl.bean.Processor;
import cn.edu.hnu.esnl.bean.Task;

/**
 * @author Guoqi Xie E-mail:xgqman@126.com
 * @version JDAS 5.0 Create time：May 19, 2016 10:02:00 PM
 */
public class MSLRUF implements Comparable<MSLRUF>, Serializable{
	
	private Task currentTask ;
	
	private Processor processor;
	
	
	private Double neeror;
	
	private Double est;
	
	private Double eft;
	
	private Double wik;
	
	
	private Double mslf ;
	
	private Double mrcf ;

	private Boolean selected =false ;
	
	private Double possibleReliability=0d;
	
	private String criticalityLevel; //tii
	
	private Double f;
	
	private Double energy;
	
	
	
	public Double getEnergy() {
		return energy;
	}

	public void setEnergy(Double energy) {
		this.energy = energy;
	}

	public Double getF() {
		return f;
	}

	public void setF(Double f) {
		this.f = f;
	}

	public String getCriticalityLevel() {
		return criticalityLevel;
	}

	public void setCriticalityLevel(String criticalityLevel) {
		this.criticalityLevel = criticalityLevel;
	}

	public Double getPossibleReliability() {
		return possibleReliability;
	}

	public void setPossibleReliability(Double possibleReliability) {
		this.possibleReliability = possibleReliability;
	}

	public MSLRUF(Processor processor,Double neeror,Double est,  Double eft, Double wik, Boolean selected) {
		this.processor = processor;
		this.neeror = neeror;
		this.est = est;
		this.eft = eft;
		this.wik = wik;
		this.mslf = eft/neeror;
		this.mrcf = wik/neeror;
		this.selected = selected;
	}
	public MSLRUF(Processor processor,Double neeror,Double est,  Double eft, Double wik, String criticalityLevel, Boolean selected) {
		this.processor = processor;
		this.neeror = neeror;
		this.est = est;
		this.eft = eft;
		this.wik = wik;
		this.mslf = eft/neeror;
		this.mrcf = wik/neeror;
		this.selected = selected;
		this.criticalityLevel = criticalityLevel;
	}
	
	public MSLRUF(Task currentTask,Processor processor,Double neeror,Double est,  Double eft, Double wik, Boolean selected) {
		this.currentTask = currentTask;
		this.processor = processor;
		this.neeror = neeror;
		this.est = est;
		this.eft = eft;
		this.wik = wik;
		this.mslf = eft/neeror;
		this.mrcf = wik/neeror;
		this.selected = selected;
	}
	public MSLRUF(Task currentTask,Processor processor,Double neeror,Double est,  Double eft, Double wik, Double f, Boolean selected) {
		this.currentTask = currentTask;
		this.processor = processor;
		this.neeror = neeror;
		this.est = est;
		this.eft = eft;
		this.wik = wik;
		this.mslf = eft/neeror;
		this.mrcf = wik/neeror;
		this.selected = selected;
		this.f=f;
	}
	
	public MSLRUF(Task currentTask,Processor processor,Double neeror,Double est,  Double eft, Double wik, String criticalityLevel, Boolean selected) {
		this.currentTask = currentTask;
		this.processor = processor;
		this.neeror = neeror;
		this.est = est;
		this.eft = eft;
		this.wik = wik;
		this.mslf = eft/neeror;
		this.mrcf = wik/neeror;
		this.selected = selected;
		this.criticalityLevel = criticalityLevel;
	}
	
	public MSLRUF(Task currentTask,Processor processor,Double neeror,Double est,  Double eft, Double wik, Double f,String criticalityLevel, Double energy) {
		this.currentTask = currentTask;
		this.processor = processor;
		this.neeror = neeror;
		this.est = est;
		this.eft = eft;
		this.wik = wik;
		this.mslf = eft/neeror;
		this.mrcf = wik/neeror;
		this.f=f;
		this.criticalityLevel = criticalityLevel;
		this.energy = energy;
	}

	public Task getCurrentTask() {
		return currentTask;
	}





	public void setCurrentTask(Task currentTask) {
		this.currentTask = currentTask;
	}





	public Double getMrcf() {
		return mrcf;
	}





	public void setMrcf(Double mrcf) {
		this.mrcf = mrcf;
	}





	public Double getWik() {
		return wik;
	}





	public void setWik(Double wik) {
		this.wik = wik;
	}





	public Double getEst() {
		return est;
	}





	public void setEst(Double est) {
		this.est = est;
	}





	public Boolean getSelected() {
		return selected;
	}





	public void setSelected(Boolean selected) {
		this.selected = selected;
	}








	public Double getNeeror() {
		return neeror;
	}


	public void setNeeror(Double neeror) {
		this.neeror = neeror;
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


	public Double getMslf() {
		return mslf;
	}


	public void setMslf(Double mslf) {
		this.mslf = mslf;
	}





	public int compareTo(MSLRUF arg0) {
	
		return this.getMslf().compareTo(arg0.getMslf());
	}
	
	
	
	
}
