import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.awt.event.ActionEvent;

public class GuiApp1 implements ActionListener {
	JFrame f;
	JMenuBar mb;
	JMenu schedule, run, stats;
	JMenuItem selectAll, game, day, season, viewstat, viewsched, teamstanding,
			viewleaguesched, viewgame, head2head;
	JButton getschedule;
	JTextArea ta;
	int end = 0;
	String[] leaguetemp;
	JComboBox<String> teamstemp, leaguestemp;
	String tempah2h, tempbh2h;
	ArrayList<String> filename = new ArrayList<String>();
	ArrayList<Team> chosen;
	ArrayList<Team> murica, national, bigleague = new ArrayList<Team>();
	ArrayList<Match> murschedule, natschedule = new ArrayList<Match>();
	HashMap<String, Team> mlb = new HashMap<String, Team>();
	Stack<Match> mlbsched = new Stack<Match>();
	boolean team = false;
	boolean league = false;
	boolean head = false;
	PlayoffEngine engine = null;

	public GuiApp1(PrintWriter writer, ArrayList<Team> mur,
			ArrayList<Team> nat, HashMap<String, Team> league,
			ArrayList<Match> muricaa, ArrayList<Match> nationall)
			throws IOException {
		murica = mur;
		national = nat;
		mlb = league;
		bigleague.addAll(murica);
		bigleague.addAll(national);
		murschedule = muricaa;
		natschedule = nationall;
		ArrayList<Match> random = new ArrayList<Match>();
		random.addAll(nationall);
		random.addAll(muricaa);
		mlbsched = convertostack(random);
	}

	void doublegui(String title, JLabel p1l1, JComboBox<String> panel11,
			JLabel p1l2, JComboBox<String> panel12, JLabel p2l1,
			JComboBox<String> panel21, JLabel p2l2, JComboBox<String> panel22,
			JButton pone, JButton ptwo, JButton trade)
			throws FileNotFoundException, UnsupportedEncodingException {
		JFrame guiFrame = new JFrame();
		guiFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		guiFrame.setTitle(title);
		guiFrame.setSize(550, 550);
		guiFrame.setLocationRelativeTo(null);
		final JPanel panelone = new JPanel();
		panelone.setVisible(false);
		panelone.add(p1l1);
		panelone.add(panel11);
		panelone.add(p1l2);
		panelone.add(panel12);
		panelone.add(pone);
		final JPanel paneltwo = new JPanel();
		paneltwo.add(p2l1);
		paneltwo.add(panel21);
		paneltwo.add(p2l2);
		paneltwo.add(panel22);
		paneltwo.add(ptwo);
		trade.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				panelone.setVisible(!panelone.isVisible());
				paneltwo.setVisible(!paneltwo.isVisible());
			}
		});
		guiFrame.add(paneltwo, BorderLayout.NORTH);
		guiFrame.add(panelone, BorderLayout.CENTER);
		guiFrame.add(trade, BorderLayout.SOUTH);
		guiFrame.setVisible(true);
	}

	void head2headgui() throws FileNotFoundException,
			UnsupportedEncodingException {
		head = true;
		String[] murteam = converteam(murica);
		String[] natteam = converteam(national);
		String title = "Get Head 2 Head game";
		JComboBox<String> mur1 = new JComboBox<String>(murteam);
		JComboBox<String> mur2 = new JComboBox<String>(murteam);
		JComboBox<String> nat1 = new JComboBox<String>(natteam);
		JComboBox<String> nat2 = new JComboBox<String>(natteam);
		JButton natormur = new JButton("National or Murica League");
		JLabel p1t1 = new JLabel("Team1: ");
		JLabel p1t2 = new JLabel("Team2: ");
		JLabel p2t1 = new JLabel("Team1: ");
		JLabel p2t2 = new JLabel("Team2: ");
		JButton getmur = new JButton("Enter");
		JButton getnat = new JButton("Enter");
		doublegui(title, p1t1, mur1, p1t2, mur2, p2t1, nat1, p2t2, nat2,
				getmur, getnat, natormur);
		tempah2h = (String) mur1.getSelectedItem();
		tempbh2h = (String) mur2.getSelectedItem();
		getmur.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				chosen = murica;
				try {
					geth2h((String) mur1.getSelectedItem(),
							(String) mur2.getSelectedItem(), murica);
				} catch (FileNotFoundException | UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		});
		getnat.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				chosen = national;
				try {
					geth2h((String) nat1.getSelectedItem(),
							(String) nat2.getSelectedItem(), national);
				} catch (FileNotFoundException | UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		});
	}

	void geth2h(String a, String b, ArrayList<Team> c)
			throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(new File("head2head.txt"), "UTF-8");
		if (a.equals(b)) {
			writer.println("IS THAT A JOKE");
			writer.close();
			return;
		}
		Team one = stringtoteam(a, c);
		Team two = stringtoteam(b, c);
		ArrayList<Match> allmatch = collide(one, two);
		int totalplayed = allmatchplayed(allmatch);
		int difference = one.series(two);
		int onewin = (totalplayed - difference) / 2;
		int twowin = (totalplayed + difference) / 2;
		writer.println("Series between " + one.nam + " and " + two.nam + " "
				+ onewin + " - " + twowin);
		for (Match i : allmatch) {
			String atemp = a;
			String btemp = b;
			if (i.home.equals(one)) {
				atemp = "@ " + a;
			}
			if (i.home.equals(two)) {
				btemp = "@ " + b;
			}
			String winteam = "";
			if (i.win.equals(a)) {
				winteam = a + " win";
			}
			if (i.win.equals(b)) {
				winteam = b + " win";
			}
			if (i.onepoint == 0 && i.twopoint == 0) {
				writer.println(String.format("%-7s %-20s %-20s %-5s", "Day "
						+ i.day, atemp, btemp, winteam));
			} else {
				writer.println(String.format("%-7s %-20s %-20s %-5s", "Day "
						+ i.day, atemp + " " + i.onepoint, btemp + " "
						+ i.twopoint, winteam));
			}
		}
		writer.close();
	}

	int allmatchplayed(ArrayList<Match> a) {
		int i = 0;
		for (Match b : a) {
			if (b.win.equals(b.one.nam) || b.win.equals(b.two.nam)) {
				i++;
			}
		}
		return i;
	}

	Team stringtoteam(String a, ArrayList<Team> c) {
		for (Team i : c) {
			if (i.nam.equals(a)) {
				return i;
			}
		}
		return null;
	}

	ArrayList<Match> collide(Team a, Team b) {
		ArrayList<Match> ret = new ArrayList<Match>();
		for (Map.Entry<Integer, Match> entry : a.num.entrySet()) {
			Match match = entry.getValue();
			if (match.blank != true) {
				if (match.one.equals(a) || match.one.equals(b)) {
					if (match.two.equals(a) || match.two.equals(b)) {
						ret.add(match);
					}
				}
			}
		}
		return ret;
	}

	String[] converteam(ArrayList<Team> a) {
		ArrayList<String> b = new ArrayList<String>();
		for (Team i : a) {
			b.add(i.nam);
		}
		return b.toArray(new String[b.size()]);
	}

	void gui1() throws FileNotFoundException, UnsupportedEncodingException {
		String[] l = { "national", "murica", "mlb" };
		String[] batstat = batstat();
		String[] pitstat = pitstat();
		String title = "Get top 10";
		JComboBox<String> pitstats = new JComboBox<String>(pitstat);
		JComboBox<String> pitleague = new JComboBox<String>(l);
		JComboBox<String> batleague = new JComboBox<String>(l);
		JComboBox<String> batstats = new JComboBox<String>(batstat);
		JButton pitorbat = new JButton("Pitcher or Batter stats");
		JLabel pitLbl = new JLabel("Pitcher: ");
		JLabel daleague = new JLabel("League: ");
		JLabel batLbl = new JLabel("Batter:");
		JLabel league = new JLabel("League: ");
		JButton topbat = new JButton("Enter");
		JButton toppit = new JButton("Enter");
		doublegui(title, pitLbl, pitstats, daleague, pitleague, batLbl,
				batstats, league, batleague, toppit, topbat, pitorbat);
		toppit.addActionListener(new ActionListener() {
			PrintWriter writer = new PrintWriter("topplayer.txt", "UTF-8");

			public void actionPerformed(ActionEvent event) {
				FinalStanding x = new FinalStanding();
				ArrayList<Team> selectleague = new ArrayList<Team>();
				int ranking = 1;
				if (pitleague.getSelectedItem().equals("mlb")) {
					selectleague = bigleague;
				} else if (pitleague.getSelectedItem().equals("national")) {
					selectleague = national;
				} else if (pitleague.getSelectedItem().equals("murica")) {
					selectleague = murica;
				}
				ArrayList<Pitcher> s = x.top10pit(
						(String) pitstats.getSelectedItem(), selectleague);
				int topp = s.get(0).pstats.get(pitstats.getSelectedItem()) + 1;
				writer.println("Top 10 pitcher in "
						+ pitstats.getSelectedItem() + " in "
						+ pitleague.getSelectedItem());
				for (Pitcher i : s) {
					String rankingp = "";
					if (i.pstats.get(pitstats.getSelectedItem()) < topp) {
						rankingp = Integer.toString(ranking);
						topp = i.pstats.get(pitstats.getSelectedItem());
					}
					writer.printf("%-2s %-21s %-21s %-21s\n", rankingp, i.nam,
							i.team, i.pstats.get(pitstats.getSelectedItem()));
					writer.println();
					ranking++;
				}
				writer.close();
			}
		});
		topbat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				PrintWriter writer = null;
				try {
					writer = new PrintWriter("topplayer.txt", "UTF-8");
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				FinalStanding x = new FinalStanding();
				ArrayList<Team> selectleague = new ArrayList<Team>();
				int ranking = 1;
				if (batleague.getSelectedItem().equals("mlb")) {
					selectleague = bigleague;
				} else if (batleague.getSelectedItem().equals("national")) {
					selectleague = national;
				} else if (batleague.getSelectedItem().equals("murica")) {
					selectleague = murica;
				}
				ArrayList<Batter> s = x.top10bat(
						(String) batstats.getSelectedItem(), selectleague);
				int topb = s.get(0).stats.get(batstats.getSelectedItem()) + 1;
				writer.println("Top 10 batter in " + batstats.getSelectedItem()
						+ " in " + batleague.getSelectedItem());
				if (batstats.getSelectedItem().equals("avg")) {
					for (Batter i : s) {
						String rankingb = "";
						if (i.stats.get(batstats.getSelectedItem()) < topb) {
							rankingb = Integer.toString(ranking);
							topb = i.stats.get(batstats.getSelectedItem());
						}
						writer.printf("%-2s %-21s %-21s %-21s\n", rankingb,
								i.nam, i.team, (double) i.stats.get(batstats
										.getSelectedItem()) / 100);
						writer.println();
						ranking++;
					}
				} else {
					for (Batter i : s) {
						String rankingb = "";
						if (i.stats.get(batstats.getSelectedItem()) < topb) {
							rankingb = Integer.toString(ranking);
							topb = i.stats.get(batstats.getSelectedItem());
						}
						writer.printf("%-2s %-21s %-21s %-21s\n", rankingb,
								i.nam, i.team,
								i.stats.get(batstats.getSelectedItem()));
						writer.println();
						ranking++;
					}
				}
				writer.close();
			}
		});

	}

	void updategui2(JComboBox<String> teams) throws FileNotFoundException,
			UnsupportedEncodingException {
		team = true;
		PrintWriter writer = new PrintWriter(new File("teamschedule.txt"),
				"UTF-8");
		Team x = mlb.get(teams.getSelectedItem());
		writer.println(teams.getSelectedItem() + " schedule");
		for (Map.Entry<Integer, Match> entry : x.num.entrySet()) {
			String onescore = "", twoscore = "";
			if (entry.getValue().blank == false) {
				if (entry.getValue().win.equals(teams.getSelectedItem())) {
					writer.print(String.format("%-11s %-2s",
							"Match " + entry.getKey(), "W"));
					onescore = Integer.toString(entry.getValue().onepoint);
					twoscore = Integer.toString(entry.getValue().twopoint);
				} else if (entry.getValue().win.equals("bla")) {
					writer.print(String.format("%-10s %-1s",
							"Match " + entry.getKey(), ""));
				} else {
					writer.print(String.format("%-11s %-2s",
							"Match " + entry.getKey(), "L"));
					onescore = Integer.toString(entry.getValue().onepoint);
					twoscore = Integer.toString(entry.getValue().twopoint);
				}
				writer.println("@ " + entry.getValue().one.nam + " " + onescore
						+ " - " + entry.getValue().two.nam + " " + twoscore
						+ " ");
			} else {
				writer.println(String.format("%-10s %-1s",
						"Match " + entry.getKey(),
						" off for travel/rain/rest aka blank match"));
			}
		}
		writer.close();
	}

	void gui2() throws UnsupportedEncodingException, FileNotFoundException {
		JButton getschedule = new JButton("Get schedule");
		JLabel comboLbl = new JLabel("Team:");
		String[] team = team();
		JComboBox<String> teams = new JComboBox<String>(team);
		teamstemp = teams;
		String title = "Team Schedule";
		smallgui(title, getschedule, comboLbl, teams);
		getschedule.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					updategui2(teams);
				} catch (FileNotFoundException | UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void smallgui(String title, JButton getschedule, JLabel comboLbl,
			JComboBox<String> teams) {
		JFrame guiFrame = new JFrame();
		guiFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		guiFrame.setTitle(title);
		guiFrame.setSize(400, 400);
		guiFrame.setLocationRelativeTo(null);
		final JPanel comboPanel = new JPanel();
		comboPanel.add(comboLbl);
		comboPanel.add(teams);
		guiFrame.add(comboPanel, BorderLayout.NORTH);
		guiFrame.add(getschedule, BorderLayout.SOUTH);
		guiFrame.setVisible(true);
	}

	String[] team() {
		ArrayList<String> a = new ArrayList<String>();
		for (Team i : murica) {
			a.add(i.nam);
		}
		for (Team i : national) {
			a.add(i.nam);
		}
		return a.toArray(new String[a.size()]);
	}

	void leaguegui() throws FileNotFoundException, UnsupportedEncodingException {
		String title = "League Schedule";
		league = true;
		JButton getschedule = new JButton("Get schedule");
		JLabel comboLbl = new JLabel("League:");
		String[] league = { "Amurica", "National", "MLB" };
		JComboBox<String> leagues = new JComboBox<String>(league);
		leaguestemp = leagues;
		leaguetemp = league;
		smallgui(title, getschedule, comboLbl, leagues);
		getschedule.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					leaguerun(league, leagues);
				} catch (FileNotFoundException | UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		});

	}

	void leaguerun(String[] league, JComboBox<String> leagues)
			throws FileNotFoundException, UnsupportedEncodingException {
		Random rand = new Random();
		PrintWriter writer = new PrintWriter(new File("leaguesched.txt"),
				"UTF-8");
		ArrayList<Match> schedule = new ArrayList<Match>();
		String s = (String) leagues.getSelectedItem();
		int day = 0;
		if (s.equals(league[0])) {
			schedule.addAll(murschedule);
		} else if (s.equals(league[1])) {
			schedule.addAll(natschedule);
		} else {
			schedule.addAll(murschedule);
			schedule.addAll(natschedule);
		}
		schedule = noblank(schedule);
		Collections.sort(schedule);
		for (Match i : schedule) {
			boolean random = rand.nextBoolean();
			String days = "";
			while (i.day > day) {
				day++;
				days = "Day " + day;
				if (i.day > day) {
					writer.println(String.format("%-7s %-4s", days,
							"no match for today"));
				}
			}
			if (i.onepoint == 0 && i.twopoint == 0) {
				if (random == true)
					writer.println(String.format("%-7s %-4s", days, "@ "
							+ i.one.nam + " - " + i.two.nam));
				if (random == false)
					writer.println(String.format("%-7s %-4s", days, i.two.nam
							+ " - @ " + i.one.nam));
			} else {
				if (random == true)
					writer.println(String.format("%-7s %-4s", days, "@ "
							+ i.one.nam + " " + i.onepoint + " - " + i.two.nam
							+ " " + i.twopoint));
				if (random == false)
					writer.println(String.format("%-7s %-4s", days, i.two.nam
							+ " " + i.onepoint + " - @ " + i.one.nam + " "
							+ i.twopoint));
			}
		}
		writer.close();
	}

	ArrayList<Match> noblank(ArrayList<Match> schedule) {
		ListIterator<Match> iter = schedule.listIterator();
		while (iter.hasNext()) {
			Match a = iter.next();
			if (a.blank == true) {
				iter.remove();
			}
		}
		return schedule;
	}

	String[] batstat() {
		Set<String> key = national.get(0).bat.get(0).stats.keySet();
		return key.toArray(new String[key.size()]);
	}

	String[] pitstat() {
		Set<String> key = national.get(0).pit.get(0).pstats.keySet();
		return key.toArray(new String[key.size()]);
	}
	void engine() {
		if(engine == null) {
		FinalStanding x = new FinalStanding();
		engine =  new PlayoffEngine(x.listonwin(murica), x.listonwin(national));
		}
		else {
			engine.activate();
		}
	}

	void gui3() {
		String[] l = { "national", "mlb", "murica" };
		String title = "Get Team Standing";
		JLabel teamLbl = new JLabel("League:");
		JButton getstanding = new JButton("Get Standing");
		JComboBox<String> league = new JComboBox<String>(l);
		biggui(l, teamLbl, title, getstanding, league);
		getstanding.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				PrintWriter writer = null;
				try {
					writer = new PrintWriter(new File("teamstanding.txt"),
							"UTF-8");
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				ArrayList<Team> selectleague = new ArrayList<Team>();
				if (league.getSelectedItem().equals("mlb")) {
					selectleague = bigleague;
				}
				if (league.getSelectedItem().equals("murica")) {
					selectleague = murica;
				}
				if (league.getSelectedItem().equals("national")) {
					selectleague = national;
				}
				FinalStanding x = new FinalStanding();
				ArrayList<Team> win = x.listonwin(selectleague);
				writer.println("Standing in " + league.getSelectedItem());
				writer.printf("%-5s %-21s %-10s %-10s %-10s %-10s\n", "Rank",
						"Name", "Win", "Lose", "GCB", "Percentage");
				writer.println();
				int topwin = win.get(0).win;
				int rank = 1;
				for (Team i : win) {
					{
						if (i.blank == false) {
							double pct = 0.000;
							String gcb;
							if (topwin == i.win) {
								gcb = "--";
							} else {
								gcb = Integer.toString(topwin - i.win);
							}
							DecimalFormat format = new DecimalFormat("0.000");
							if (i.win + i.lost == 0) {
								pct = 0.000;
							} else {
								double percent = (double) i.win
										/ (i.win + i.lost);
								pct = (double) Math.round(percent * 1000.0) / 1000.0;
							}
							String formatted = format.format(pct);
							writer.printf(
									"%-5s %-21s %-10s %-10s %-10s %-10s\n",
									rank, i.nam, i.win, i.lost, gcb, formatted);
							writer.println();
							rank++;
						}
					}
				}

				writer.close();
			}
		});
	}

	Stack<Match> convertostack(ArrayList<Match> schedule) {
		Stack<Match> a = new Stack<Match>();
		schedule = noblank(schedule);
		Collections.sort(schedule);
		Collections.reverse(schedule);
		for (Match i : schedule) {
			a.push(i);
		}
		return a;
	}

	void biggui(String[] l, JLabel teamLbl, String title, JButton getsomething,
			JComboBox<String> league) {
		JFrame guiFrame = new JFrame();
		guiFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		guiFrame.setTitle(title);
		guiFrame.setSize(300, 250);
		guiFrame.setLocationRelativeTo(null);
		final JPanel teamPanel = new JPanel();
		teamPanel.add(teamLbl);
		teamPanel.add(league);
		guiFrame.add(teamPanel, BorderLayout.NORTH);
		guiFrame.add(getsomething, BorderLayout.SOUTH);
		guiFrame.setVisible(true);
	}

	void MainMenu() {
		f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				for (String name : filename) {
					try {
						Files.deleteIfExists(Paths.get(name));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		game = new JMenuItem("Run the next game");
		head2head = new JMenuItem("View head 2 head game: ");
		day = new JMenuItem("Run a number of day");
		season = new JMenuItem("Run 1 regular season");
		selectAll = new JMenuItem("selectAll");
		viewsched = new JMenuItem("View a team's schedule: ");
		viewleaguesched = new JMenuItem("View a league's schedule: ");
		viewgame = new JMenuItem("View a game's information");
		viewstat = new JMenuItem("View league's best 10 player based on: ");
		teamstanding = new JMenuItem("View team standing: ");
		game.addActionListener(this);
		day.addActionListener(this);
		season.addActionListener(this);
		selectAll.addActionListener(this);
		viewsched.addActionListener(this);
		viewstat.addActionListener(this);
		teamstanding.addActionListener(this);
		head2head.addActionListener(this);
		viewleaguesched.addActionListener(this);
		mb = new JMenuBar();
		schedule = new JMenu("Schedule");
		run = new JMenu("Run");
		stats = new JMenu("Stats");
		run.add(game);
		run.add(day);
		run.add(season);
		schedule.add(viewsched);
		schedule.add(head2head);
		schedule.add(viewleaguesched);
		stats.add(viewstat);
		stats.add(teamstanding);
		mb.add(schedule);
		mb.add(stats);
		mb.add(run);
		ta = new JTextArea();
		ta.setBounds(0, 0, 1800, 1800);
		f.add(mb);
		f.add(ta);
		f.setJMenuBar(mb);
		f.setLayout(null);
		f.setSize(1800, 1800);
		f.setVisible(true);
		f.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	boolean parseint(String a) {
		try {
			int b = Integer.parseInt(a);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == head2head) {
			try {
				head2headgui();
			} catch (FileNotFoundException | UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == viewsched) {
			try {
				gui2();
			} catch (UnsupportedEncodingException | FileNotFoundException e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == viewleaguesched) {
			try {
				leaguegui();
			} catch (FileNotFoundException | UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == viewstat) {
			try {
				gui1();
			} catch (FileNotFoundException | UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == teamstanding) {
			gui3();
		}
		if (e.getSource() == game) {
			PrintWriter write = null;
			PrintWriter newwrite = null;
			try {
				write = new PrintWriter(new FileWriter("result.txt", true));
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			Runner x = new Runner();
			try {
				Match mur = mlbsched.peek();
				String name = mur.one.nam + " vs " + mur.two.nam + ".txt";
				filename.add(name);
				x.runnextgame(mlbsched, name);
				if (team == true)
					updategui2(teamstemp);
				if (head == true)
					geth2h(tempah2h, tempbh2h, chosen);
				if (league == true)
					leaguerun(leaguetemp, leaguestemp);
				if (mlbsched.isEmpty())
					engine();
			} catch (FileNotFoundException | UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			write.flush();
			write.close();
		}
		if (e.getSource() == day) {
			String name = "";
			JPanel day = new JPanel();
			name = JOptionPane.showInputDialog(day,
					"How much day to run for? (Maximum of 119)", null);
			while(parseint(name) == false)
				name = JOptionPane.showInputDialog(day,
						"How much day to run for? (Maximum of 119)", null);
			while(Integer.parseInt(name)>119)
				name = JOptionPane.showInputDialog(day,
						"How much day to run for? (Maximum of 119)", null);
			int i = Integer.parseInt(name);
			PrintWriter write = null;
			try {
				write = new PrintWriter(new FileWriter("result.txt", true));
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			Runner x = new Runner();
			end += i;
			try {
				x.runeachleague(mlbsched, write, end);
				if (team == true)
					updategui2(teamstemp);
				if (head == true)
					geth2h(tempah2h, tempbh2h, chosen);
				if (league == true)
					leaguerun(leaguetemp, leaguestemp);
				if (mlbsched.isEmpty())
					engine();
			} catch (FileNotFoundException | UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			write.flush();
			write.close();
		}
		if (e.getSource() == season) {
			PrintWriter write = null;
			try {
				write = new PrintWriter(new FileWriter("result.txt", true));
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			Runner x = new Runner();
			int start = end + 1;
			end = 0;
			try {
				x.runeachleague(mlbsched, write, 119);
				if (team == true)
					updategui2(teamstemp);
				if (head == true)
					geth2h(tempah2h, tempbh2h, chosen);
				if (league == true)
					leaguerun(leaguetemp, leaguestemp);
				engine();
			} catch (FileNotFoundException | UnsupportedEncodingException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			write.flush();
			write.close();
		}
	}

}