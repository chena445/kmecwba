package cn.edu.hnu.esnl.service.graph;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class DrawFFT extends JFrame {
	public DrawFFT(int n) {
		setTitle("FFT");
		getContentPane().add(new FFTPanel(n));
	}

	/** 主方法 */
	public static void main(String[] args) {
		int n = 4;
		DrawFFT frame = new DrawFFT(n);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000, 800);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setBackground(Color.WHITE);
	}
}

// 在面板上画弧形的类
class FFTPanel extends JPanel {
	int n;
	int count;
	int ballLayer;
	int[][] matrix;

	public FFTPanel(int n) {
		this.setBackground(Color.white);

		this.n = n;
		int power = (int) (Math.log(n) / Math.log(2));
		int product = (int) Math.pow(2, power);
		if (power < 1 || product != n) {
			throw new IllegalArgumentException("n must be 2^p");
		}
		int top = product - 1;
		int layer = power + 1;
		ballLayer = power * 2 + 1;

		count = n * layer + top;

		this.matrix = new int[count][count];

		// 顶部二叉树
		for (int i = 0; i < n - 1; i++) {
			int leftChild = i * 2 + 1;
			int rightChild = i * 2 + 2;
			matrix[i][leftChild] = 1;
			matrix[i][rightChild] = 1;
		}

		// layer 层
		cross(matrix, count - n, count, n);
	}

	private void cross(int[][] matrix, int start, int end, int n) {
		if (end - start <= 1) {
			return;
		}

		int half = (end - start) / 2;
		// left half
		for (int i = start; i < start + half; i++) {
			matrix[i - n][i] = 1;
			matrix[i - n + half][i] = 1;
		}
		// right half
		for (int i = start + half; i < end; i++) {
			matrix[i - n][i] = 1;
			matrix[i - n - half][i] = 1;
		}

		cross(matrix, start - n, start + half - n, n);
		cross(matrix, start + half - n, end - n, n);
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// 得到当前的font metrics
		FontMetrics fm = g.getFontMetrics();

		g.setColor(Color.BLACK); // 设置弧形的颜色为黑色

		int width = getWidth();
		int height = getHeight();

		int subHeight = height / ballLayer;
		int subWidth = width / n;
		int ballSize = subHeight / 6 < subWidth / 6 ? subHeight / 6 : subWidth / 6;

		int yOffset = subHeight / 2;

		int[][] coors = new int[count][2];
		int index = 0;
		// top part
		int topLayer = ballLayer / 2;
		for (int i = 0; i < topLayer; i++) {
			int num = (int) Math.pow(2, i);
			int ballWidth = width / num;
			int xOffset = ballWidth / 2;
			int centerY = i * subHeight + yOffset;
			for (int j = 0; j < num; j++) {
				int centerX = j * ballWidth + xOffset;

				g.drawOval(centerX - ballSize, centerY - ballSize, ballSize * 2, ballSize * 2);

				coors[index][0] = centerX;
				coors[index][1] = centerY;

				index++;

				String text = ""+String.valueOf(index);
				text="";
				int stringWidth = fm.stringWidth(text);
				int stringAscent = fm.getAscent();
				int xCoordinate = centerX - stringWidth / 2;
				int yCoordinate = centerY + stringAscent / 2;

				g.drawString(text, xCoordinate, yCoordinate);

			}
		}

		for (int i = topLayer; i < ballLayer; i++) {
			int centerY = i * subHeight + yOffset;
			int xOffset = subWidth / 2;
			for (int j = 0; j < n; j++) {
				int centerX = j * subWidth + xOffset;

				g.drawOval(centerX - ballSize, centerY - ballSize, ballSize * 2, ballSize * 2);
				
				coors[index][0] = centerX;
				coors[index][1] = centerY;

				index++;

				String text = ""+String.valueOf(index);
				text="";
				int stringWidth = fm.stringWidth(text);
				int stringAscent = fm.getAscent();
				int xCoordinate = centerX - stringWidth / 2;
				int yCoordinate = centerY + stringAscent / 2;

				g.drawString(text, xCoordinate, yCoordinate);

			}
		}

		for (int i = 0; i < count; i++) {
			for (int j = 0; j < count; j++) {
				if (matrix[i][j] == 1) {
					int startX = coors[i][0];
					int startY = coors[i][1] + ballSize;
					int endX = coors[j][0];
					int endY = coors[j][1] - ballSize;

					g.drawLine(startX, startY, endX, endY);
				}
			}
		}
	}
}