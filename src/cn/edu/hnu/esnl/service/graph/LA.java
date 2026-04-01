package cn.edu.hnu.esnl.service.graph;

import java.util.LinkedHashMap;
import java.util.Random;

/**
 * @author liqiang
 * @version 2016-3-28 下午1:39:58
 */
public class LA {

	public static void main(String[] args) {
		int m = 5;
		
		int[][] matrix = la(m,10,90);
		

	}

	public static int[][] la(int m,int cost_lower, int cost_upper) {
		int n = m*(m+1)/2;
		//System.out.println("n:"+n);
		int[][] commMartrix = new int[n+1][n+1];
		
		int step= m;
		for (int i = 1; i < n; i=i+step+1) {
			//System.out.println("i:"+i);
			for (int j = i+step; j <= i+step+step-2; j++) {
				//System.out.println("ij:"+i+","+j);
				commMartrix[i][j] = new Integer(cost_lower + new Random().nextInt(cost_upper));
			}
			step= step-1;
			if(step<0)
				break;
		}
	//	System.out.println("===========================");
		step= m;
		for (int i = 1; i < n; i=i+step+1) {
		//	System.out.println("i:"+i);
			for (int j = i+1; j <= i+step-1; j++) {
				//System.out.println("j:"+j);
				 int z = j+step-1;
				//	System.out.println("jz:"+j+","+z);
				 commMartrix[j][z] =new Integer(cost_lower + new Random().nextInt(cost_upper));
				
			}
			//System.out.println("==");
			step= step-1;
			if(step<0)
				break;
		}
		


		for (int x = 1; x < n+1; x++) {
			for (int y = 1; y < n+1; y++) {
				//System.out.print(commMartrix[x][y] + " ");
			}
			//System.out.println();
		}

		return commMartrix;
		
	}

	

}
