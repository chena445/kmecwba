package cn.edu.hnu.esnl.service.graph;

import java.util.Random;

/**
 * @author liqiang
 * @version 2016-3-28 下午1:39:58
 */
public class FFT {

	public static void main(String[] args) {
		int n = 4;
		int[][] matrix = fft(n,10,90);
		int width = matrix[0].length;
		int height = matrix.length;

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				System.out.print(matrix[y][x] + " ");
			}
			System.out.println();
		}
	}

	public static int[][] fft(int n,int cost_lower, int cost_upper) {
		int power = (int) (Math.log(n) / Math.log(2));
		int product = (int) Math.pow(2, power);
		if (power < 1 || product != n) {
			throw new IllegalArgumentException("n must be 2^p");
		}
		int top = product - 1;
		int layer = power + 1;
		int count = n * layer + top;
		int[][] matrix = new int[count][count];

		// 顶部二叉树
		for (int i = 0; i < n - 1; i++) {
			int leftChild = i * 2 + 1;
			int rightChild = i * 2 + 2;
			
			matrix[i][leftChild] = new Integer(cost_lower + new Random().nextInt(cost_upper));
			matrix[i][rightChild] = new Integer(cost_lower + new Random().nextInt(cost_upper));
		}

		// layer 层
		cross(matrix, count - n, count, n,cost_lower,cost_upper);

		return matrix;
	}

	private static void cross(int[][] matrix, int start, int end, int n,int cost_lower, int cost_upper) {
		if (end - start <= 1) {
			return;
		}

		int half = (end - start) / 2;
		// left half
		for (int i = start; i < start + half; i++) {
			matrix[i - n][i] = new Integer(cost_lower + new Random().nextInt(cost_upper));
			matrix[i - n + half][i] = new Integer(cost_lower + new Random().nextInt(cost_upper));
		}
		// right half
		for (int i = start + half; i < end; i++) {
			matrix[i - n][i] = new Integer(cost_lower + new Random().nextInt(cost_upper));
			matrix[i - n - half][i] = new Integer(cost_lower + new Random().nextInt(cost_upper));
		}

		cross(matrix, start - n, start + half - n, n,cost_lower,cost_upper);
		cross(matrix, start + half - n, end - n, n,cost_lower,cost_upper);
	}

}
