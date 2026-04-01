package cn.edu.hnu.esnl.service.energy;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import cn.edu.hnu.esnl.bean.Processor;
import cn.edu.hnu.esnl.bean.Task;
import cn.edu.hnu.esnl.system.SystemValue;
import cn.edu.hnu.esnl.util.DoubleUtil;

/**
 * @author Guoqi Xie E-mail:xgqman@126.com
 * @version JDAS 5.0 Create time��Jan 6, 2016 12:05:42 PM
 */
public class EnergyService {

	
	/*
	public static double getEnergy(Processor p, double frequency, double est, double eft) {

		
		double e = (p.getP_ind() + p.getC_ef() * Math.pow(frequency, p.getA())) * (eft - est);
		e = Math.round(e * 10000) / 10000.0;
		
		return e;
	}
	*/
	  public static double getEnergy(   double wcet,Task currentTask, Processor p) {
			
			double w = currentTask.getProcessor$CompCostMap().get(p);
			
			double f = w*p.getF_max()/wcet;
			
			double e = (p.getP_ind() + p.getC_ef() * Math.pow(f, p.getA())) * (w) *p.getF_max() / f;
			
			return e;
		}
	


	public static double getEnergy(Task currentTask, Processor p, double f) {
	
		
		double w = currentTask.getProcessor$CompCostMap().get(p);
		double e = (p.getP_ind() + p.getC_ef() * Math.pow(f, p.getA())) * (w) *p.getF_max() / f;
		
		return e;
	}
	
	public static double getEnergy(double wik, Processor p, double f) {
	
		double e = (p.getP_ind() + p.getC_ef() * Math.pow(f, p.getA())) * (wik) *p.getF_max() / f;
		
		return e;
	}

	public static double getEnergyPlatform(Task currentTask, Processor p, double f) {
		
		//System.out.println("f:"+f);
		double pind = fpinds.get(f);
		double pd = fpds.get(f);
		
		double w = currentTask.getProcessor$CompCostMap().get(p);
		double e = (pind+ pd) * (w) *p.getF_max() / f;
		
		
		return e;
	}
	
	public static LinkedHashMap<Double,Double> fpinds = new LinkedHashMap<Double,Double>();
	public static LinkedHashMap<Double,Double> fpds = new LinkedHashMap<Double,Double>();
	static{
		fpinds.put(1.01, 0.15);
		fpinds.put(0.960, 0.15);
		fpinds.put(0.912, 0.125);
		fpinds.put(0.864, 0.10);
		fpinds.put(0.816, 0.10);
		fpinds.put(0.768, 0.10);
		fpinds.put(0.744, 0.075);
		fpinds.put(0.720, 0.075);
		fpinds.put(0.696, 0.075);
		fpinds.put(0.672, 0.05);
		fpinds.put(0.648, 0.05);
		fpinds.put(0.600, 0.05);
		fpinds.put(0.528, 0.035);
		fpinds.put(0.480, 0.035);
		fpinds.put(0.408, 0.035);
		
		fpds.put(1.01, 1.05);
		fpds.put(0.960, 0.90);
		fpds.put(0.912, 0.80);
		fpds.put(0.864, 0.70);
		fpds.put(0.816, 0.60);
		fpds.put(0.768, 0.50);
		fpds.put(0.744, 0.45);
		fpds.put(0.720, 0.40);
		fpds.put(0.696, 0.35);
		fpds.put(0.672, 0.30);
		fpds.put(0.648, 0.25);
		fpds.put(0.600, 0.20);
		fpds.put(0.528, 0.15);
		fpds.put(0.480, 0.15);
		fpds.put(0.408, 0.10);
	}
	
	
	
	public static double getPNEnergy(Task currentTask, Processor p, double frequency, double est, double eft) {

		
		double e1 = (p.getP_ind() + p.getC_ef() * Math.pow(frequency, p.getA())) * (eft - est) / frequency;

		LinkedHashMap<Task, Integer> predTask$comm = currentTask.getPredTask$CommCostMap();

		Set<Task> predTasks = predTask$comm.keySet();

		double e2 = 0d;
		for (Task predTask : predTasks) {

			if (!predTask.getAssignedprocessor1().equals(currentTask.getAssignedprocessor1()))

				e2 = e2 + SystemValue.POWER_COMM * predTask$comm.get(predTask);
		}

		// System.out.println(currentTask.getName()+" e1:"+e1);
		// System.out.println(currentTask.getName()+" e2:"+e2);
		// System.out.println(currentTask.getName()+" total:"+(e1+e2));
		// System.out.println("=============");
		return e1 + e2;
	}

	public static double getPNEnergy1(Task currentTask, Processor p, double frequency, double est, double eft) {

		// Ƶ���Ѿ����ȥ��

		double e1 = (p.getP_ind() + p.getC_ef() * Math.pow(frequency, p.getA())) * (eft - est) / frequency;

		return e1;
	}

	public static double getPNEnergy2(Task currentTask, Processor p, double frequency, double est, double eft) {

		// Ƶ���Ѿ����ȥ��

		LinkedHashMap<Task, Integer> predTask$comm = currentTask.getPredTask$CommCostMap();

		Set<Task> predTasks = predTask$comm.keySet();

		double e2 = 0d;
		for (Task predTask : predTasks) {

			if (!predTask.getAssignedprocessor1().equals(currentTask.getAssignedprocessor1()))

				e2 = e2 + SystemValue.POWER_COMM * predTask$comm.get(predTask);
		}

		return e2;
	}

	public static void setLowerboundEnergyandProcessor(Task currentTask, List<Processor> givenProcessors) {

		double lowerbound_energy = Double.MAX_VALUE;
		Processor lowerbound_energy_processor = null;
		for (int i = 1; i < givenProcessors.size(); i++) {
			Processor p = givenProcessors.get(i);

			double currentEnergy = (p.getP_ind() + p.getC_ef() * Math.pow(p.getF_ee(), p.getA())) * currentTask.getProcessor$CompCostMap().get(p) / p.getF_ee();

			if (currentEnergy < lowerbound_energy) {
				lowerbound_energy = currentEnergy;
				lowerbound_energy_processor = p;
			}
		}
		// currentTask.setLowerbound_energy(lowerbound_energy);
		// currentTask.setLowerbound_energy_processor(lowerbound_energy_processor);

	}

}
