package cn.edu.hnu.esnl.realgraph;

import java.util.Random;

import cn.edu.hnu.esnl.bean.view.CompCommMartrixBean;
import cn.edu.hnu.esnl.service.graph.CBR;
import cn.edu.hnu.esnl.service.graph.FFT;
import cn.edu.hnu.esnl.service.graph.Gaussian;
import cn.edu.hnu.esnl.service.graph.LA;
import cn.edu.hnu.esnl.service.graph.Laplace;

/**
 * @author Guoqi Xie E-mail:xgqman@126.com
 * @version JDAS 5.0 Create time：Sep 24, 2016 8:40:14 PM
 */

public class CBRGenrattor {
	public static CompCommMartrixBean exe(int rho, int cost_lower, int cost_upper, int processor_number) {
		
		int task_number = ((Double) (Math.pow(2, rho) - 1)).intValue() + 2;
		
		System.out.println("rho="+rho + " The task number of the  complete binary tree  is  " + (task_number - 2));
		
		processor_number= processor_number+1;
		Integer[][] compMartrix = new Integer[task_number][processor_number];

		for (int i = 0; i < task_number; i++) {
			for (int j = 0; j < processor_number; j++) {

				compMartrix[i][j] = cost_lower + new Random().nextInt(cost_upper);

			}
		}

		Integer[][] commMartrix = CBR.cbr(rho, cost_lower, cost_upper);


		CompCommMartrixBean compCommMartrix = new CompCommMartrixBean(compMartrix,commMartrix);
		return compCommMartrix;
	}

}
