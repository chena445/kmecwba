package cn.edu.hnu.esnl.service.graph;
import java.awt.Color;
import java.util.Random;

/**
 * @author liqiang
 * @version 2016-6-15 上午10:51:58
 */
public class CyberShake {

    /**
     * @param args
     */
    public static void main(String[] args) {
    	int[][] matrix = laplace(3,1,1);
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }

    }
    
    public static int[][] laplace(int n,int cost_lower, int cost_upper) {
        
        int width = n;
        int height = 2 * n - 1;
        
        int[][] flag = new int[height][width];
        
        flag = new int[height][n];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                flag[i][j] = -1;
            }
        }
        int index = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < (i + 1); j++) {
                flag[i][j] = index++;
            }
        }
        for (int i = n; i < height; i++) {
            for (int j = 0; j < (height - i); j++) {
                flag[i][j] = index++;
            }
        }
        
        int count = n * n;
        int[][] matrix = new int[count][count];
       
        
        for (int i = 0; i < n - 1; i++) {
            int num = i + 1;
            for (int j = 0; j < num; j++) {
                int start = flag[i][j];
                int end1 = flag[i + 1][j];
                int end2 = flag[i + 1][j + 1];
                matrix[start][end1] =  new Integer(cost_lower + new Random().nextInt(cost_upper));;
                matrix[start][end2] =  new Integer(cost_lower + new Random().nextInt(cost_upper));;
            }
        }
        
      //bottom part
        for (int i = n - 1; i < height - 1; i++) {
            int num = height - i;
            for (int j = 0; j < num; j++) {
                int start = flag[i][j];
                if (j - 1 >= 0) {
                    int end1 = flag[i + 1][j - 1];
                    matrix[start][end1] =  new Integer(cost_lower + new Random().nextInt(cost_upper));;
                }
                if (flag[i + 1][j] != -1) {
                    int end2 = flag[i + 1][j];                
                    matrix[start][end2] =  new Integer(cost_lower + new Random().nextInt(cost_upper));;
                }
            }
        }
        return matrix;
    }

}
