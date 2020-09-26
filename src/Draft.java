import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

public class Draft extends Schedule {
	boolean printing = true;

	Draft() {

	}

	ArrayList<Team> printLottery(ArrayList<Team> lottery1) {
		try {
			Files.deleteIfExists(Paths.get("Lottery.txt"));
			Files.deleteIfExists(Paths.get("LotteryNotes.txt"));
			Files.deleteIfExists(Paths.get("LotteryResult.txt"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Stack<Match> allmatch = new Stack<Match>();
		HashMap<Team, ArrayList<Team>> map = lottery(lottery1);
		try {
			PrintWriter printer = new PrintWriter(new File("Lottery.txt"),
					"UTF-8");
			printer.printf("%-21s %-21s %-21s %-21s %-21s %-21s\n", "Team",
					"Round 1", "Round 2", "Round 3", "Round 4", "Round 5");
			printer.println();
			for (Map.Entry<Team, ArrayList<Team>> entry : map.entrySet()) {
				Team one = entry.getKey();
				Team two = entry.getValue().get(0);
				Team three = entry.getValue().get(1);
				Team four = entry.getValue().get(2);
				Team five = entry.getValue().get(3);
				Team six = entry.getValue().get(4);
				printer.printf("%-21s %-21s %-21s %-21s %-21s %-21s\n",
						one.nam, two.nam, three.nam, four.nam, five.nam,
						six.nam);
				printer.println();
			}
			printer.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		for (Map.Entry<Team, ArrayList<Team>> entry : map.entrySet()) {
			for (Team i : entry.getValue()) {
				allmatch.push(new Match(entry.getKey(), i));
				allmatch.push(new Match(entry.getKey(), i));
			}
		}
		try {
			while (!allmatch.isEmpty()) {
				FileWriter fw = new FileWriter(new File("LotteryNotes.txt"),
						true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw);
				GameLog x = new GameLog();
				Match i = allmatch.pop();
				String winner = x.run(i, out);
				i.one.lotteryexaminer(winner);
				i.two.lotteryexaminer(winner);
				out.close();
			}
		} catch (IOException e) {
		}
		try {
			FileWriter fw = new FileWriter(new File("LotteryResult.txt"), true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter out = new PrintWriter(bw);
			GameLog x = new GameLog();
			out.printf("%-23s %-7s %-7s", "Team", "Win", "Lost");
			out.println();
			for (Team i : lottery1) {
				out.printf("%-23s %-7s %-7s", i.nam, i.lotterywin,
						i.lotterylose);
			}
			out.close();
		} catch (IOException e) {
		}
		Collections.sort(lottery1, new LotteryComparator());
		return lottery1;
	}

	HashMap<Team, ArrayList<Team>> lottery(ArrayList<Team> a) {
		Collections.shuffle(a);
		HashMap<Team, ArrayList<Team>> master = new HashMap<Team, ArrayList<Team>>();
		ArrayList<Team> seriesone = new ArrayList<Team>();
		ArrayList<Team> seriestwo = new ArrayList<Team>();
		for (int i = 0; i < a.size() / 2; i++) {
			seriesone.add(a.get(i));
			seriestwo.add(a.get(i + a.size() / 2));
		}
		for (int b = 1; b < a.size(); b++) {
			for (int i = 0; i < seriesone.size(); i++) {
				Team one = seriesone.get(i);
				Team two = seriestwo.get(i);
				if (!master.containsKey(one)) {
					ArrayList<Team> plz = new ArrayList<Team>();
					plz.add(two);
					master.put(one, plz);
				} else {
					ArrayList<Team> plz = master.get(one);
					plz.add(two);
					master.put(one, plz);
				}
				if (!master.containsKey(two)) {
					ArrayList<Team> plz = new ArrayList<Team>();
					plz.add(one);
					master.put(two, plz);
				} else {
					ArrayList<Team> plz = master.get(two);
					plz.add(one);
					master.put(two, plz);
				}
			}
			rotate(seriesone, seriestwo);
		}
		return master;
	}

	ArrayList<Team> midlottery(ArrayList<Team> midlottery1) {
		try {
			Files.deleteIfExists(Paths.get("MidLottery.txt"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		TreeMap<Integer, ArrayList<Match>> result = new TreeMap<Integer, ArrayList<Match>>();
		Collections.shuffle(midlottery1);
		ArrayList<ArrayList<Match>> master = new ArrayList<ArrayList<Match>>();
		ArrayList<ArrayList<Match>> masterresult = new ArrayList<ArrayList<Match>>();
		ArrayList<Team> seriesone = new ArrayList<Team>();
		ArrayList<Team> seriestwo = new ArrayList<Team>();
		for (int i = 0; i < midlottery1.size() / 2; i++) {
			seriesone.add(midlottery1.get(i));
			seriestwo.add(midlottery1.get(i + midlottery1.size() / 2));
		}
		for (int b = 1; b < midlottery1.size(); b++) {
			ArrayList<Match> home = new ArrayList<Match>();
			ArrayList<Match> away = new ArrayList<Match>();
			for (int i = 0; i < seriesone.size(); i++) {
				Team one = seriesone.get(i);
				Team two = seriestwo.get(i);
				home.add(new Match(one, two));
				away.add(new Match(two, one));
			}
			master.add(home);
			master.add(away);
			rotate(seriesone, seriestwo);
		}
		Collections.shuffle(master);
		for (int i = 0; i < 9; i++) {
			masterresult.add(master.get(i));
		}
		for (int i = 9; i < master.size(); i++) {
			ArrayList<Match> doublearray = master.get(i);
			ArrayList<Match> smallone = new ArrayList<Match>();
			ArrayList<Match> anothersmallone = new ArrayList<Match>();
			smallone.add(doublearray.get(0));
			smallone.add(doublearray.get(1));
			anothersmallone.add(doublearray.get(2));
			anothersmallone.add(doublearray.get(3));
			masterresult.add(smallone);
			masterresult.add(anothersmallone);
		}
		Collections.shuffle(masterresult);
		for (int i = 0; i < masterresult.size(); i++) {
			result.put(i + 1, masterresult.get(i));
		}
		PrintWriter out = null;
		try {
			FileWriter fw = new FileWriter(new File("MidLottery.txt"), true);
			BufferedWriter bw = new BufferedWriter(fw);
			out = new PrintWriter(bw);
		} catch (IOException e) {
		}
		printout(result);
		out.printf("%-4s %-15s", "Day", "Match schedule");
		out.println();
		for (Map.Entry<Integer, ArrayList<Match>> entry : result.entrySet()) {
			if (entry.getValue().size() == 2) {
				Match a = entry.getValue().get(0);
				Match b = entry.getValue().get(1);
				out.printf("%-4s %-43s %-43s", entry.getKey(), a.two.nam
						+ " @ " + a.one.nam, b.two.nam + " @ " + b.one.nam);
				out.println();
			}
			if (entry.getValue().size() == 4) {
				Match a = entry.getValue().get(0);
				Match b = entry.getValue().get(1);
				Match c = entry.getValue().get(2);
				Match d = entry.getValue().get(3);
				out.printf("%-4s %-43s %-43s %-43s %-43s", entry.getKey(),
						a.two.nam + " @ " + a.one.nam, b.two.nam + " @ "
								+ b.one.nam, c.two.nam + " @ " + c.one.nam,
						d.two.nam + " @ " + d.one.nam);
				out.println();
			}
		}
		try {
				FileWriter fw = new FileWriter(
						new File("MidLotteryResult.txt"), true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter outprint = new PrintWriter(bw);
				GameLog x = new GameLog();
				outprint.printf("%-23s %-7s %-7s", "Team", "Win", "Lost");
				outprint.println();
				for (Team i : midlottery1) {
					outprint.printf("%-23s %-7s %-7s", i.nam, i.lotterywin,
							i.lotterylose);
				}
				outprint.close();
		} catch (IOException e) {
		}
		Collections.sort(midlottery1, new LotteryComparator());
		out.close();
		return midlottery1;
	}

	void printout(TreeMap<Integer, ArrayList<Match>> a) {
		try {
			Files.deleteIfExists(Paths.get("MidLotteryNotes.txt"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Stack<Match> allmatch = new Stack<Match>();
		for (Map.Entry<Integer, ArrayList<Match>> entry : a.entrySet()) {
			for (Match i : entry.getValue()) {
				allmatch.push(i);
			}
		}
		try {
			FileWriter fw = new FileWriter(new File("MidLotteryNotes.txt"),
					true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter out = new PrintWriter(bw);
			GameLog x = new GameLog();
			while (!allmatch.isEmpty()) {
				Match i = allmatch.pop();
				String winner = x.run(i, out);
				i.one.lotteryexaminer(winner);
				i.two.lotteryexaminer(winner);
			}
			out.close();
		} catch (IOException e) {
		}
	}

	void finaldraft(ArrayList<Team> lottery, ArrayList<Team> midlottery,
			HashMap<String, Team> murcon, HashMap<String, Team> murchamp,
			HashMap<String, Team> natcon, HashMap<String, Team> natchamp,
			HashMap<String, Team> worldseries) {
		try {
			Files.deleteIfExists(Paths.get("DraftOrder.txt"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		PrintWriter out = null;
		try {
			FileWriter fw = new FileWriter(new File("DraftOrder.txt"), true);
			BufferedWriter bw = new BufferedWriter(fw);
			out = new PrintWriter(bw);
		} catch (IOException e) {
		}
		ArrayList<Team> series23 = combine(murcon, natcon, "S23");
		ArrayList<Team> series24 = combine(murcon, natcon, "S24");
		series23.addAll(series24);
		series23 = sortandreverse(series23);
		ArrayList<Team> series25 = combine(murcon, natcon, "S25");
		series25 = sortandreverse(series25);
		ArrayList<Team> third = combine(murchamp, natchamp, "Third");
		third = sortandreverse(third);
		ArrayList<Team> quarterfinal = new ArrayList<Team>();
		quarterfinal.add(worldseries.get("S26"));
		quarterfinal.add(worldseries.get("S27"));
		quarterfinal.add(worldseries.get("S28"));
		quarterfinal.add(worldseries.get("S29"));
		quarterfinal = sortandreverse(quarterfinal);
		ArrayList<Team> semifinals = new ArrayList<Team>();
		semifinals.add(worldseries.get("S30"));
		semifinals.add(worldseries.get("S31"));
		semifinals = sortandreverse(semifinals);
		Team runnerup = worldseries.get("Runner up");
		Team champ = worldseries.get("Champion");
		ArrayList<Team> master = new ArrayList<Team>();
		master.add(lottery.get(0));
		master.add(lottery.get(0));
		for (int i = 1; i < lottery.size(); i++) {
			master.add(lottery.get(i));
		}
		master.add(midlottery.get(0));
		master.add(midlottery.get(0));
		for (int i = 1; i < midlottery.size(); i++) {
			master.add(midlottery.get(i));
		}
		master.addAll(series23);
		master.addAll(series25);
		master.addAll(third);
		master.addAll(quarterfinal);
		master.addAll(semifinals);
		master.add(runnerup);
		master.add(champ);
		out.println("\t\t\t\tDraft Order");
		out.printf("%-10s %-23s", "Pick", "Team");
		out.println();
		int order = 1;
		if (printing) {
			for (Team i : master) {
				out.printf("%-10s %-23s", order, i.nam);
				out.println();
				order++;
			}
			printing = false;
		}
		out.close();
	}

	ArrayList<Team> combine(HashMap<String, Team> a, HashMap<String, Team> b,
			String c) {
		ArrayList<Team> combination = new ArrayList<Team>();
		combination.add(a.get(c));
		combination.add(b.get(c));
		return combination;
	}

	ArrayList<Team> sortandreverse(ArrayList<Team> a) {
		Collections.sort(a);
		Collections.reverse(a);
		return a;
	}
}
