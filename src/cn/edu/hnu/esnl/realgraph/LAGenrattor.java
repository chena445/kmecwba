package cn.edu.hnu.esnl.realgraph;

import java.util.Random;

import cn.edu.hnu.esnl.bean.view.CompCommMartrixBean;
import cn.edu.hnu.esnl.service.graph.FFT;
import cn.edu.hnu.esnl.service.graph.Gaussian;
import cn.edu.hnu.esnl.service.graph.LA;

/**
 * @author Guoqi Xie E-mail:xgqman@126.com
 * @version JDAS 5.0 Create time：Sep 24, 2016 8:40:14 PM
 */

public class LAGenrattor {
	public static CompCommMartrixBean exe(int rho, int cost_lower, int cost_upper, int processor_number) {
		int task_number =rho*(rho+1)/2 + 2;
		System.out.println("rho="+rho+" The task number of the linear algebra is  " + (task_number - 2));
		
		processor_number= processor_number+1;
		Integer[][] compMartrix = new Integer[task_number][processor_number];

		for (int i = 0; i < task_number; i++) {
			for (int j = 0; j < processor_number; j++) {

				compMartrix[i][j] = cost_lower + new Random().nextInt(cost_upper);

			}
		}

		
		Integer[][] commMartrix = new Integer[task_number][task_number];
		
		int[][] commMartrix2 = LA.la(rho, cost_lower, cost_upper);
		
		
		////////////////////////////
		//移位
		for (int i = 0; i < task_number; i++) {
			for (int j = 0; j < task_number; j++) {

				commMartrix[i][j] = 0;
			}
		}
		
		
		for (int i = 1; i < task_number - 1; i++) {
			for (int j = 1; j < task_number - 1; j++) {

				commMartrix[i+1][j+1] = commMartrix2[i][j];
			}
		}
		
		

		CompCommMartrixBean compCommMartrix = new CompCommMartrixBean(compMartrix,commMartrix);
		return compCommMartrix;
	}

}
