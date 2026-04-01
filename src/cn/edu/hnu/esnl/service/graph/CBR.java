package cn.edu.hnu.esnl.service.graph;

import java.util.LinkedHashMap;
import java.util.Random;

/**
 * @author liqiang
 * @version 2016-3-28 下午1:39:58
 */
public class CBR {

	public static void main(String[] args) {
		int m =5;
		
		Integer[][] matrix = cbr(m,10,90);
		
		//2^i-1个
	}

	public static Integer[][] cbr(int m,int cost_lower, int cost_upper) {
		
		int n = ((Double)(Math.pow(2, m)-1)).intValue();
		//System.out.println("n:"+n);
		Integer[][] commMartrix = new Integer[n+2][n+2];
		for (int x = 0; x <= n+1; x++) {
			for (int y = 0; y <= n+1; y++) {
				commMartrix[x][y]=0;
			}
			
		}
		
		for (int i = 1; i <= n; i++) {
			if(2*i<=n){
				
				int left = 2*i;
				int right = 2*i+1;
			
				commMartrix[i][left]=new Integer(cost_lower + new Random().nextInt(cost_upper));;
				commMartrix[i][right]=new Integer(cost_lower + new Random().nextInt(cost_upper));;
				
				
			}else{
				
				commMartrix[i][n+1]=1;
				
			}
		}

		
		for (int x = 0; x <= n+1; x++) {
			for (int y = 0; y <= n+1; y++) {
			//	System.out.print(commMartrix[x][y] + " ");
			}
			//System.out.println();
		}

		return commMartrix;
		
	}

	

}
