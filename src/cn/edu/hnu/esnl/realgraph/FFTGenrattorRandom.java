package cn.edu.hnu.esnl.realgraph;

import java.util.Random;

import cn.edu.hnu.esnl.bean.view.CompCommMartrixBean;
import cn.edu.hnu.esnl.service.graph.FFT;
import cn.edu.hnu.rtgg.bean.DagComponent;
import cn.edu.hnu.rtgg.generator.DAGGenerator;

/**
 * @author Guoqi Xie E-mail:xgqman@126.com
 * @version JDAS 5.0 Create time：Sep 24, 2016 8:40:14 PM
 */

public class FFTGenrattorRandom {
	public static CompCommMartrixBean exe(int rho, int cost_lower, int cost_upper, int processor_number) {
		
		Double task_number1 = (2 * rho - 1 + rho * (Math.log(rho) / Math.log(2))+1 );
		int task_number = task_number1.intValue();
		
		
		
		
		
		System.out.println("rho="+rho+" The task number of fast Fourier transform is  " + (task_number-1 ));
		
		processor_number = processor_number+1;
		Integer[][] compMartrix = new Integer[task_number][processor_number];
		
		
		//基于异构因子产生的
		DagComponent comp = DAGGenerator.random(task_number-1,processor_number-1,(cost_lower+cost_upper)/2,1,1,1);
		for (int i = 1; i < task_number; i++) {
			for (int j = 1; j < processor_number; j++) {
				compMartrix[i][j] = comp.getComputationMatrix()[i-1][j-1].intValue();
			}
		}

		for (int i = 0; i < task_number; i++) {
			for (int j = 0; j < processor_number; j++) {

				compMartrix[i][j] = cost_lower + new Random().nextInt(cost_upper);
			}
		}
		
		

		Integer[][] commMartrix = new Integer[task_number][task_number];
		int[][] commMartrix2 = FFT.fft(rho, cost_lower, cost_upper);
		for (int i = 0; i < task_number; i++) {
			for (int j = 0; j < task_number; j++) {

				commMartrix[i][j] = 0;
			}
		}
		for (int i = 1; i < task_number - 1; i++) {
			for (int j = 1; j < task_number - 1; j++) {

				commMartrix[i][j] = commMartrix2[i - 1][j - 1];
			}
		}

		
		CompCommMartrixBean compCommMartrix = new CompCommMartrixBean(compMartrix,commMartrix);
		return compCommMartrix;
	}

}
