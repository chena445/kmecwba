package cn.edu.hnu.esnl.service.graph;

import java.util.LinkedHashMap;
import java.util.Random;

/**
 * @author liqiang
 * @version 2016-3-28 下午1:39:58
 */
public class PA {

	public static void main(String[] args) {
		int m = 3;
		
		Integer[][] matrix = pa(m,10,90);
		
		//2^i-1个
	}

	public static Integer[][] pa(int m,int cost_lower, int cost_upper) {
		
		int n1 = ((Double)(Math.pow(2, m)-1)).intValue();
		int n2= 2*n1+n1+1;
		
		//System.out.println("n1:"+n1);
		//System.out.println("n2:"+n2);
	//	System.out.println("total number:"+(n1+n2));
		Integer[][] commMartrix = new Integer[n2+2][n2+2];
		for (int x = 0; x <= n2+1; x++) {
			for (int y = 0; y <= n2+1; y++) {
				commMartrix[x][y]=0;
			}
			
		}
		
		for (int i = 1; i <= n1; i++) {
			
				int left = 2*i;
				int right = 2*i+1;
				commMartrix[i][left]=new Integer(cost_lower + new Random().nextInt(cost_upper));;
				commMartrix[i][right]=new Integer(cost_lower + new Random().nextInt(cost_upper));;
				
			
		}
		
		int count1 = -1 ;
		int count2 = -1 ;
		for (int i = n1+1; i <= n2; i++) {
			
				count1++;
				if(i%2==0){
					count2++;
					
				}
				
				//System.out.println("count1:"+count1+" count2:"+count2);
				
				int succ = 2*(i-count1) +count2; 
				
				commMartrix[i][succ]=new Integer(cost_lower + new Random().nextInt(cost_upper));;
				
				
			
		}

		for (int x = 0; x < n2+1; x++) {
			for (int y = 0; y < n2+1; y++) {
				//System.out.print(commMartrix[x][y] + " ");
			}
			//System.out.println();
		}

		return commMartrix;
		
	}

	

}
