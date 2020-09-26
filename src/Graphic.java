import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;

public class Graphic extends JFrame {
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int width = (int) screenSize.getWidth();
	int height = (int) screenSize.getHeight();
	public final static Color gren = new Color(0, 255, 0);
	public final static Color CLOSE = new Color(255, 0, 0);
	boolean rb = true;
	private DrawCanvas canvas;

	public Graphic() {
		canvas = new DrawCanvas();
		canvas.setPreferredSize(new Dimension(width, height));
		Container cp = getContentPane();
		cp.add(canvas);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setTitle("Playoff MLB");
		setVisible(true);
	}
	private class DrawCanvas extends JPanel {
		public void paintComponent(Graphics g) {
			GraphicsEnvironment ge = GraphicsEnvironment
					.getLocalGraphicsEnvironment();
			try {
				ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(
						"Simply Complicated.ttf")));
			} catch (FontFormatException | IOException e) {
				e.printStackTrace();
			}
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			Color color1 = gren;
			Color color2 = new Color(255,255,255);
			GradientPaint gp = new GradientPaint(0, 0, color1, width, height,
					color2);
			g2d.setPaint(gp);
			g2d.fillRect(0, 0, width, height);
			g.setColor(CLOSE);
			((Graphics2D) g).setStroke(new BasicStroke((float) 2.0));
			g.setFont(new Font("Simply Complicated", Font.PLAIN, 25));
			g.drawString("America League Post Season Tournament",
					width * 2 / 5, 25);
			for (int i = 1; i < 5; i++) {
				int widthmul = (i - 1) * 2 + 1;
				g.drawString("Round " + i, width * widthmul / 10, 75);
			}
			g.drawString("Champion", width * 9 / 10, 75);
			int difference = (height - 50 - 150) / 8;
			for (int i = 1; i < 9; i++) {
				g.drawRect(50, difference * i, width / 5 - 50, difference - 25);
				g.drawLine(width / 5, difference * i + (difference - 25) / 2,
						width / 5 + 50, difference * i + (difference - 25) / 2);
			}
			for (int i = 1; i < 5; i++) {
				g.drawRect(50 + width / 5, difference * 2 * i - difference,
						width / 5 - 50, difference * 2 - 25);
				g.drawLine(width * 2 / 5, difference * 2 * i - 12,
						width * 2 / 5 + 50, difference * 2 * i - 12);
			}
			for (int i = 1; i < 3; i++) {
				g.drawRect(50 + width * 2 / 5, difference * 4 * i - difference
						* 3, width / 5 - 50, difference * 4 - 25);
				g.drawLine(width * 3 / 5, difference * 4 * i - difference - 12,
						width * 3 / 5 + 50, difference * 4 * i - difference
								- 12);
			}
			g.drawRect(50 + width * 3 / 5, difference, width / 5 - 50,
					difference * 7 - 150);
			g.drawLine(width * 4 / 5, difference * 4 - 50, 100 + width * 4 / 5,
					difference * 4 - 50);
			g.drawRect(50 + width * 3 / 5, difference * 8 - 150,
					width / 5 - 50, difference + 125);
			g.drawLine(width * 4 / 5, difference * 8 - 150 + (difference + 125)
					/ 2, 100 + width * 4 / 5, difference * 8 - 150
					+ (difference + 125) / 2);
		}
	}
}