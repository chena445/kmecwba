package cn.edu.hnu.esnl.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

import cn.edu.hnu.esnl.bean.view.StartEndTime;

/**
 * @author xgq E-mail:xgqman@126.com
 * @version ����ʱ�䣺Nov 24, 2013 11:50:41 PM ��˵��:
 * 
 */
public class Processor implements Cloneable,Serializable {
	
	private String name ;
	
	private LinkedHashMap<Task,Double> task$availTimeMap = new LinkedHashMap<Task,Double>() ;
	private LinkedHashMap<StartEndTime,Task> startEndTime$TaskMap = new LinkedHashMap<StartEndTime,Task>();
	
	private LinkedHashMap<Task,StartEndTime> task$startEndTimeMap = new LinkedHashMap<Task,StartEndTime>();
	
	
	
	public void clear(){
		task$availTimeMap.clear();
		startEndTime$TaskMap.clear();
		task$startEndTimeMap.clear();
		
		processorSL= 0d;
		processorEnergy = 0d;
	}
	
	private double p_s ;
	
	private double p_ind ;
	
	private double c_ef;
	
	private double a =3 ;
	private double f_max ;
	
	private double f_ee ;
	
	
	// �������Ĺ��ϲ���
	private Double lamda ;
	
	
	private Double price=0d ;
	
	private Boolean open=true;
	
	private Double processorSL;
	
	private Double processorEnergy;
	
	private Double processorReliability;
	
	
	private Boolean inUsing =false;
	
	private double d ;
	
	
	
	public double getD() {
		return d;
	}

	public void setD(double d) {
		this.d = d;
	}

	public Double getProcessorReliability() {
		return processorReliability;
	}

	public void setProcessorReliability(Double processorReliability) {
		this.processorReliability = processorReliability;
	}

	public Boolean getInUsing() {
		return inUsing;
	}

	public void setInUsing(Boolean inUsing) {
		this.inUsing = inUsing;
	}

	public Double getProcessorEnergy() {
		return processorEnergy;
	}

	public void setProcessorEnergy(Double processorEnergy) {
		this.processorEnergy = processorEnergy;
	}

	public Double getProcessorSL() {
		return processorSL;
	}

	public void setProcessorSL(Double processorSL) {
		this.processorSL = processorSL;
	}

	public Boolean getOpen() {
		return open;
	}

	public void setOpen(Boolean open) {
		this.open = open;
	}

	public Double getLamda() {
		return lamda;
	}

	public void setLamda(Double lamda) {
		this.lamda = lamda;
	}

	public double getF_max() {
		return f_max;
	}

	public void setF_max(double f_max) {
		this.f_max = f_max;
	}

	public double getF_ee() {
		return f_ee;
	}

	public void setF_ee(double f_ee) {
		this.f_ee = f_ee;
	}

	public double getP_s() {
		return p_s;
	}

	public void setP_s(double p_s) {
		this.p_s = p_s;
	}

	public double getP_ind() {
		return p_ind;
	}

	public void setP_ind(double p_ind) {
		this.p_ind = p_ind;
	}

	public double getC_ef() {
		return c_ef;
	}

	public void setC_ef(double c_ef) {
		this.c_ef = c_ef;
	}

	public double getA() {
		return a;
	}

	public void setA(double a) {
		this.a = a;
	}

	
	private Double precision;
	
	
	
	public Double getPrecision() {
		return precision;
	}

	public void setPrecision(Double precision) {
		this.precision = precision;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		
		return (Processor)super.clone();
	}
	
	public Processor() {
		super();
		
		
	}
	
	
	public Processor(String name) {
		
		this.name = name;
	}
	
	public Processor(String name,  double lamda) {
		
		this.name = name;

		this.lamda = lamda;
		
	}
	
	public Processor(String name,  double lamda, double price) {
		
		this.name = name;
		this.lamda = lamda;
		this.price = price;
		
	}


	public Processor(String name, double f_max, double p_s, double p_ind, double c_ef, double a) {
		
		this.name = name;
		this.f_max = f_max ;
		this.p_s = p_s;
		this.p_ind = p_ind;
		this.c_ef = c_ef;
		this.a = a;
		
		double f_ee = Math.pow(p_ind/((a-1)*c_ef), 1/(double)a);
		f_ee= Math.round(f_ee* 100) / 100.0;
		
		this.f_ee = f_ee; 
		
	}
	public Processor(String name, double f_max, double p_s, double p_ind, double c_ef, double a, boolean ispresise, double precise) {
		
		this.name = name;
		this.f_max = f_max ;
		this.p_s = p_s;
		this.p_ind = p_ind;
		this.c_ef = c_ef;
		this.a = a;
		
		double f_ee = Math.pow(p_ind/((a-1)*c_ef), 1/(double)a);
		if(precise==0.01)
			f_ee= Math.round(f_ee* 100) / 100.0;
		if(precise==0.1)
			f_ee= Math.round(f_ee* 10) / 10.0;
		
		this.f_ee = f_ee; 
		
	}
	public Processor(double f_min,String name, double f_max, double p_s, double p_ind, double c_ef, double a) {
		
		this.name = name;
		this.f_max = f_max ;
		this.p_s = p_s;
		this.p_ind = p_ind;
		this.c_ef = c_ef;
		this.a = a;
		
		double f_ee = Math.pow(p_ind/((a-1)*c_ef), 1/(double)a);
		f_ee= Math.round(f_ee* 100) / 100.0;
		
		f_ee = Math.max(f_min, f_ee);
		
		this.f_ee = f_ee; 
		
	}
	
	public Processor(String name, double f_max, double p_s, double p_ind, double c_ef, double a, double lamda) {
		
		this.name = name;
		this.f_max = f_max ;
		this.p_s = p_s;
		this.p_ind = p_ind;
		this.c_ef = c_ef;
		this.a = a;
		
		double f_ee = Math.pow(p_ind/((a-1)*c_ef), 1/(double)a);
		f_ee= Math.round(f_ee* 100) / 100.0;
		this.f_ee = f_ee; 
		
		this.lamda = lamda;
		
	}
	

public Processor(String name, double f_max, double p_s, double p_ind, double c_ef, double a, double lamda, double  precision) {
		
		this.name = name;
		this.f_max = f_max ;
		this.p_s = p_s;
		this.p_ind = p_ind;
		this.c_ef = c_ef;
		this.a = a;
		this.precision = precision;
		
		
		double f_ee = Math.pow(p_ind/((a-1)*c_ef), 1/(double)a);
		if(precision==0.1)
			f_ee= Math.round(f_ee* 10) / 10.0;
		if(precision==0.01)
			f_ee= Math.round(f_ee* 100) / 100.0;
		this.f_ee = f_ee; 
		
		this.lamda = lamda;
		
	}
public Processor(String name, double f_max, double p_s, double p_ind, double c_ef, double a, double lamda, double  precision, double d) {
	
	this.name = name;
	this.f_max = f_max ;
	this.p_s = p_s;
	this.p_ind = p_ind;
	this.c_ef = c_ef;
	this.a = a;
	this.precision = precision;
	this.d = d;
	
	double f_ee = Math.pow(p_ind/((a-1)*c_ef), 1/(double)a);
	if(precision==0.1)
		f_ee= Math.round(f_ee* 10) / 10.0;
	if(precision==0.01)
		f_ee= Math.round(f_ee* 100) / 100.0;
	this.f_ee = f_ee; 
	
	this.lamda = lamda;
	
}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Processor other = (Processor) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public LinkedHashMap<Task, Double> getTask$availTimeMap() {
		return task$availTimeMap;
	}

	public void setTask$availTimeMap(LinkedHashMap<Task, Double> task$availTimeMap) {
		this.task$availTimeMap = task$availTimeMap;
	}

	public LinkedHashMap<StartEndTime, Task> getStartEndTime$TaskMap() {
		return startEndTime$TaskMap;
	}

	public void setStartEndTime$TaskMap(LinkedHashMap<StartEndTime, Task> startEndTime$TaskMap) {
		this.startEndTime$TaskMap = startEndTime$TaskMap;
	}

	public LinkedHashMap<Task, StartEndTime> getTask$startEndTimeMap() {
		return task$startEndTimeMap;
	}

	public void setTask$startEndTimeMap(LinkedHashMap<Task, StartEndTime> task$startEndTimeMap) {
		this.task$startEndTimeMap = task$startEndTimeMap;
	}

	
	
	

	

	
	
	
	
	
	
	
	
}
