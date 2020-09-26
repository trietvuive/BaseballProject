import java.awt.*;
import java.util.Timer;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

public class CGTemplate extends JFrame {
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int width = (int) screenSize.getWidth();
	int height = (int) screenSize.getHeight();
	public final static Color gren = new Color(255, 0, 0);
	public final static Color CLOSE = new Color(255, 255, 0);
	boolean rb = true;
	private DrawCanvas canvas;
	String winner;
	String loser = "Winner";
	String runnerup = "Runner up";
	TreeMap<Integer, JButton> bunchofbutton = new TreeMap<Integer, JButton>();
	TreeMap<Integer, PlayoffBlock> listmatch = new TreeMap<Integer, PlayoffBlock>();
	PlayoffBlock rootthis;
	PlayoffBlock rootopposite;
	HashMap<JButton, PlayoffBlock> pair = new HashMap<JButton, PlayoffBlock>();
	ArrayList<Team> loser1 = new ArrayList<Team>();
	boolean consolation = false;
	CGTemplate neighbor;
	PlayoffBlock otherroot;
	int[] consol = new int[] { 3, 6, 7 };
	boolean everythingfinish = false;
	ArrayList<Team> finalcontestant = new ArrayList<Team>();
	PlayoffEngine caller;
	CGTemplate me = this;
	boolean getout = false;
	boolean rununtilempty = false;
	JButton runrunrun, runall;
	boolean endit = false;
	boolean finalround = false;
	String stringtitle;
	ArrayList<Team> midlottery = new ArrayList<Team>();
	ArrayList<Team> lottery = new ArrayList<Team>();
	HashMap<String, Team> prepdraft = new HashMap<String, Team>();

	public CGTemplate(String string, TreeMap<Integer, PlayoffBlock> a,
			boolean con, boolean fin, PlayoffBlock otherroot,
			PlayoffEngine larger, String title) {
		stringtitle = string;
		consolation = con;
		finalround = fin;
		this.otherroot = otherroot;
		listmatch = a;
		canvas = new DrawCanvas();
		canvas.setPreferredSize(new Dimension(width, height));
		Container cp = getContentPane();
		cp.add(canvas);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setTitle("Playoff MLB");
		setVisible(true);
		fillbutton(canvas);
		this.getContentPane().setLayout(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		caller = larger;
		this.setTitle(title);
		this.setResizable(false);
		this.addWindowStateListener(new WindowStateListener() {
			public void windowStateChanged(WindowEvent e) {
				if ((e.getNewState() & Frame.ICONIFIED) == Frame.ICONIFIED) {
					repaint();
				} else if ((e.getNewState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH) {
					repaint();
				}
			}
		});
	}

	public void fillbutton(JPanel canvas) {
		int a = 0;
		int difference = (height - 50 - 150) / 8;
		if (!consolation) {
			for (int i = 1; i < 9; i++) {
				JButton but = new JButton("Play ");
				but.setBounds(50, difference * i, 80, 80);
				but.setBackground(Color.yellow);
				bunchofbutton.put(a, but);
				this.add(but);
				a++;
			}
		} else {
			for (int i : consol) {
				JButton but = new JButton("Play ");
				but.setBounds(50, difference * i, 80, 80);
				but.setBackground(Color.yellow);
				a = i - 1;
				bunchofbutton.put(a, but);
				this.add(but);
			}
			a = 8;
		}
		System.out.println("first " + a);
		for (int i = 1; i < 5; i++) {
			JButton but = new JButton("Play ");
			but.setBounds(50 + width / 5, difference * 2 * i - difference, 160,
					160);
			but.setBackground(Color.MAGENTA);
			bunchofbutton.put(a, but);
			this.add(but);
			a++;
		}
		System.out.println("Second " + a);
		for (int i = 1; i < 3; i++) {
			JButton but = new JButton("Play ");
			but.setBounds(50 + width * 2 / 5, difference * 4 * i - difference
					* 3, 160, 160);
			but.setBackground(Color.green);
			bunchofbutton.put(a, but);
			this.add(but);
			a++;
		}
		JButton but = new JButton("Play ");
		but.setBounds(50 + width * 3 / 5, difference, 240, 240);
		bunchofbutton.put(a, but);
		a++;
		but.setBackground(Color.orange);
		this.add(but);
		System.out.println("Third" + a);
		if (!consolation) {
			a++;
			System.out.println(listmatch.size());
			JButton but1 = new JButton("Play ");
			but1.setBackground(Color.cyan);
			but1.setBounds(50 + width * 3 / 5, difference * 8 - 150, 80, 80);
			bunchofbutton.put(a, but1);
			this.add(but1);
		}
		if (consolation) {
			System.out.println("LELELLLL" + bunchofbutton.size());
			System.out.println("LELELELE" + listmatch.size());
		}
		for (Map.Entry<Integer, JButton> entry : bunchofbutton.entrySet()) {
			System.out.println("Entry" + entry.getKey());
			PlayoffBlock b = listmatch.get(entry.getKey());
			b.setButton(entry.getValue());
			pair.put(entry.getValue(), b);
		}
		runall = new JButton("Run Round");
		runall.setBackground(Color.red);
		runall.setBounds(500, 0, 100, 100);
		this.add(runall);
		runall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for (Map.Entry<JButton, PlayoffBlock> entry : pair.entrySet()) {
					entry.getValue().rununtilempty();
					repaint();
				}
			}
		});
		runrunrun = new JButton("Run RUn RUN");
		runrunrun.setBackground(Color.green);
		runrunrun.setBounds(250, 0, 100, 100);
		this.add(runrunrun);
		runrunrun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rununtilempty = !rununtilempty;
				repaint();
			}
		});
	}

	void filter() {
		listmatch.remove(0);
	}

	void transfer() {
		PlayoffBlock another = otherroot;
		another.getNextMatch().two = loser1.get(0);
		another = another.getNext().getNext();
		another.one = loser1.get(4);
		another.two = loser1.get(3);
		another.fillStack();
		another = another.getNext().getNext().getNext();
		another.one = loser1.get(5);
		another.two = loser1.get(2);
		another.fillStack();
		another = another.getNext();
		another.one = loser1.get(6);
		another.two = loser1.get(1);
		another.fillStack();
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
			Color color2 = new Color(0, 0, 255);
			GradientPaint gp = new GradientPaint(0, 0, color1, width, height,
					color2);
			g2d.setPaint(gp);
			g2d.fillRect(0, 0, width, height);
			g.setColor(CLOSE);
			((Graphics2D) g).setStroke(new BasicStroke((float) 2.0));
			g.setFont(new Font("Simply Complicated", Font.PLAIN, 20));
			g.drawString(stringtitle, width * 2 / 5, 25);
			int a = 0;
			for (int i = 1; i < 5; i++) {
				int widthmul = (i - 1) * 2 + 1;
				g.drawString("Round " + i, width * widthmul / 10, 75);
			}
			g.drawString("Champion", width * 9 / 10, 75);
			int difference = (height - 50 - 150) / 8;
			for (int i = 1; i < 9; i++) {
				listmatch.get(a).y = difference - 25;
				a++;
			}
			if (!consolation) {
				for (int i = 1; i < 9; i++) {
					g.drawRect(50, difference * i, width / 5 - 50,
							difference - 25);
					g.drawLine(width / 5, difference * i + (difference - 25)
							/ 2, width / 5 + 50, difference * i
							+ (difference - 25) / 2);
				}
			} else {
				for (int i : consol) {
					g.drawRect(50, difference * i, width / 5 - 50,
							difference - 25);
					g.drawLine(width / 5, difference * i + (difference - 25)
							/ 2, width / 5 + 50, difference * i
							+ (difference - 25) / 2);
				}
			}
			for (int i = 1; i < 5; i++) {
				g.drawRect(50 + width / 5, difference * 2 * i - difference,
						width / 5 - 50, difference * 2 - 25);
				g.drawLine(width * 2 / 5, difference * 2 * i - 12,
						width * 2 / 5 + 50, difference * 2 * i - 12);
				listmatch.get(a).y = difference * 2 - 25;
				a++;
			}
			for (int i = 1; i < 3; i++) {
				g.drawRect(50 + width * 2 / 5, difference * 4 * i - difference
						* 3, width / 5 - 50, difference * 4 - 25);
				if (i != 2 || !consolation) {
					g.drawLine(width * 3 / 5, difference * 4 * i - difference
							- 12, width * 3 / 5 + 50, difference * 4 * i
							- difference - 12);
				}
				listmatch.get(a).y = difference * 4 - 25;
				a++;
			}
			g.drawRect(50 + width * 3 / 5, difference, width / 5 - 50,
					difference * 7 - 200);
			listmatch.get(a).y = difference * 7 - 200;
			a++;
			if (!consolation) {
				a++;
				listmatch.get(a).y = difference + 125;
				g.drawRect(50 + width * 3 / 5, difference * 8 - 150,
						width / 5 - 50, difference + 125);
				g.drawLine(width * 4 / 5, difference * 8 - 150
						+ (difference + 125) / 2, 100 + width * 4 / 5,
						difference * 8 - 150 + (difference + 125) / 2);
				loser = listmatch.get(listmatch.size() - 1).whowin().nam;
				g.drawString(loser, 150 + width * 4 / 5, difference * 8 - 150
						+ (difference + 125) / 2);
			}
			g.drawLine(width * 4 / 5, difference * 4 - 50, 100 + width * 4 / 5,
					difference * 4 - 50);
			winner = listmatch.get(listmatch.size() - 2).whowin().nam;
			g.drawString(winner, 150 + width * 4 / 5, difference * 4 - 50);
			if (!consolation) {
				if (listmatch.get(listmatch.size() - 3).complete())
					runnerup = listmatch.get(listmatch.size() - 3).wholose().nam;
				g.drawString(runnerup, 150 + width * 4 / 5, difference * 4 + 50);
			}
			everythingfinish = true;
			for (Map.Entry<JButton, PlayoffBlock> entry : pair.entrySet()) {
				int x = entry.getKey().getBounds().x;
				int y = entry.getKey().getBounds().y;
				int height = entry.getValue().y;
				g.setColor(Color.cyan);
				g.drawString(entry.getValue().one.nam, x + width / 5 - 200, y);
				g.drawString(entry.getValue().two.nam, x + width / 5 - 200, y
						+ height);
				if (entry.getValue().avaliable()) {
					entry.getKey().setEnabled(true);
				} else
					entry.getKey().setEnabled(false);
				if (entry.getValue().one.nam != "LOL"
						|| entry.getValue().two.nam != "LOL") {
					g.drawString(Integer.toString(entry.getValue().onewin), x
							+ width / 5 - 100, y + height / 2 - 25);
					g.drawString(Integer.toString(entry.getValue().twowin), x
							+ width / 5 - 100, y + height / 2 + 25);
				}
				for (ActionListener al : entry.getKey().getActionListeners()) {
					entry.getKey().removeActionListener(al);
				}
				if (entry.getValue().complete()) {
					entry.getKey().setEnabled(false);
					entry.getValue().advance();
					if (entry.getValue().round == 1 && !consolation)
						loser1.add(entry.getValue().wholose());
					repaint();
					if (!consolation && !finalround)
						neighbor.repaint();
				}
				if (!entry.getValue().getonlyonce()) {
					everythingfinish = false;
				}
				entry.getKey().addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (rununtilempty) {
							entry.getValue().rununtilempty();
							repaint();
						} else {
							entry.getValue().run();
							repaint();
						}
					}
				});
			}
			if (loser1.size() == 7) {
				Collections.sort(loser1, new PlayoffComparator());
				transfer();
				neighbor.repaint();
			}
			if (everythingfinish && !getout && !finalround) {
				System.out.println("everything done");
				finalcontestant.add(listmatch.get(listmatch.size() - 2)
						.whowin());
				if (!consolation) {
					finalcontestant.add(listmatch.get(listmatch.size() - 1)
							.wholose());
					finalcontestant.add(listmatch.get(listmatch.size() - 1)
							.whowin());
				}
				System.out.println(finalcontestant.size());
				caller.add(finalcontestant);
				getout = true;
				if (consolation) {
					lottery.add(listmatch.get(2).wholose());
					lottery.add(listmatch.get(5).wholose());
					lottery.add(listmatch.get(6).wholose());
					midlottery.add(listmatch.get(8).wholose());
					midlottery.add(listmatch.get(9).wholose());
					midlottery.add(listmatch.get(10).wholose());
					midlottery.add(listmatch.get(11).wholose());
					for (Map.Entry<String, Team> entry : prepdraft.entrySet()) {
						System.out.println(entry.getKey() + " "
								+ entry.getValue().nam);
					}
				}
				if (consolation) {
					prepdraft.put("S23", listmatch.get(12).wholose());
					prepdraft.put("S24", listmatch.get(13).wholose());
					prepdraft.put("S25", listmatch.get(14).wholose());
					for (Map.Entry<String, Team> entry : prepdraft.entrySet()) {
						System.out.println(entry.getKey() + " "
								+ entry.getValue().nam);
					}
				}
				if (!consolation && !finalround) {
					prepdraft.put("Third", listmatch.get(16).wholose());
				}
			}
			if (listmatch.get(14).getonlyonce() && finalround) {
				prepdraft.put("S26", listmatch.get(8).wholose());
				prepdraft.put("S27", listmatch.get(9).wholose());
				prepdraft.put("S28", listmatch.get(10).wholose());
				prepdraft.put("S29", listmatch.get(11).wholose());
				prepdraft.put("S30", listmatch.get(12).wholose());
				prepdraft.put("S31", listmatch.get(13).wholose());
				prepdraft.put("Runner up", listmatch.get(14).wholose());
				prepdraft.put("Champion", listmatch.get(14).whowin());
				for (Map.Entry<String, Team> entry : prepdraft.entrySet()) {
					System.out.println(entry.getKey() + " "
							+ entry.getValue().nam);
				}
				caller.dodraft();
			}
		}
	}
}