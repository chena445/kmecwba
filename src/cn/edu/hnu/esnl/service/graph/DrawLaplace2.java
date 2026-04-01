package cn.edu.hnu.esnl.service.graph;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class DrawLaplace2 extends JFrame {
	public DrawLaplace2(int n) {
		setTitle("Laplace");
		getContentPane().add(new LaplacePanel(n));
	}

	/** 主方法 */
	public static void main(String[] args) {
		int n = 5;
		DrawLaplace2 frame = new DrawLaplace2(n);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000, 800);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}
}

// 在面板上画弧形的类
class LaplacePanel extends JPanel {
	int n;
	int width;
	int height;
	int[][] matrix;

	public LaplacePanel(int n) {
		this.setBackground(Color.white);

		this.n = n;
		width = n;
		height = 2 * n - 1;

		this.matrix = new int[height][width];

		matrix = new int[height][n];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				matrix[i][j] = -1;
			}
		}
		int index = 0;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < (i + 1); j++) {
				matrix[i][j] = index++;
			}
		}
		for (int i = n; i < height; i++) {
			for (int j = 0; j < (height - i); j++) {
				matrix[i][j] = index++;
			}
		}

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				System.out.print(matrix[i][j] + " ");
				;
			}
			System.out.println();
		}
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// 得到当前的font metrics
		FontMetrics fm = g.getFontMetrics();

		g.setColor(Color.BLACK); // 设置弧形的颜色为黑色

		int panelWidth = getWidth();
		int panelHeight = getHeight();

		int subHeight = panelHeight / height;
		int subWidth = panelWidth / n;
		int ballSize = subHeight / 6 < subWidth / 6 ? subHeight / 6
				: subWidth / 6;

		int yOffset = subHeight / 2;

		// top part
		for (int i = 0; i < n - 1; i++) {
			int num = i + 1;
			int xOffset = (panelWidth - subWidth * num) / 2 + subWidth / 2;
			int centerY = i * subHeight + yOffset;
			for (int j = 0; j < num; j++) {
				int centerX = j * subWidth + xOffset;

				// 箭头
				int nextLayerXOffset = (panelWidth - subWidth * (num + 1)) / 2
						+ subWidth / 2;
				int leftX = j * subWidth + nextLayerXOffset;
				int leftY = (i + 1) * subHeight + yOffset;
				int rightX = (j + 1) * subWidth + nextLayerXOffset;
				int rightY = leftY;

				g.drawLine(centerX, centerY, leftX, leftY);
				g.drawLine(centerX, centerY, rightX, rightY);

				g.fillOval(centerX - ballSize, centerY - ballSize,
						ballSize * 2, ballSize * 2);
				g.setColor(new Color(151, 151, 151)); // 设置弧形的颜色为白色
				g.fillOval(centerX - ballSize + 1, centerY - ballSize + 1,
						ballSize * 2 - 2, ballSize * 2 - 2);
				g.setColor(Color.black); // 设置弧形的颜色为白色

				String text = String.valueOf(matrix[i][j]+1); //name
				int stringWidth = fm.stringWidth(text);
				int stringAscent = fm.getAscent();
				int xCoordinate = centerX - stringWidth / 2;
				int yCoordinate = centerY + stringAscent / 2;

				g.drawString(text, xCoordinate, yCoordinate);
			}
		}

		// bottom part
		for (int i = n - 1; i < height; i++) {
			int num = height - i;
			int xOffset = (panelWidth - subWidth * num) / 2 + subWidth / 2;
			int centerY = i * subHeight + yOffset;
			for (int j = 0; j < num; j++) {
				int centerX = j * subWidth + xOffset;

				// 箭头
				int nextLayerXOffset = (panelWidth - subWidth * (num - 1)) / 2
						+ subWidth / 2;

				if (i < (height - 1) && (j - 1) >= 0) {

					int leftX = (j - 1) * subWidth + nextLayerXOffset;
					int leftY = (i + 1) * subHeight + yOffset;
					g.drawLine(centerX, centerY, leftX, leftY);

				}
				if (i < (height - 1) && matrix[i + 1][j] != -1) {
					int rightX = j * subWidth + nextLayerXOffset;
					int rightY = (i + 1) * subHeight + yOffset;
					g.drawLine(centerX, centerY, rightX, rightY);
				}

				g.fillOval(centerX - ballSize, centerY - ballSize,
						ballSize * 2, ballSize * 2);
				g.setColor(new Color(151, 151, 151)); // 设置弧形的颜色为白色
				g.fillOval(centerX - ballSize + 1, centerY - ballSize + 1,
						ballSize * 2 - 2, ballSize * 2 - 2);
				g.setColor(Color.black); // 设置弧形的颜色为白色

				String text = String.valueOf( matrix[i][j]+1); //name
				int stringWidth = fm.stringWidth(text);
				int stringAscent = fm.getAscent();
				int xCoordinate = centerX - stringWidth / 2;
				int yCoordinate = centerY + stringAscent / 2;

				g.drawString(text, xCoordinate, yCoordinate);
			}
		}

	}
}