import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class GameLog {
	GameLog() {
	}

	private ArrayList<Team> league(Scanner s, int batsize) {
		ArrayList<Team> nat = new ArrayList<Team>();
		for (int i = 0; i < 15; i++) {
			Creation a = new Creation();
			nat.add(a.ret(s, batsize));
		}
		return nat;
	}

	public HashMap<String, Team> mlb() throws FileNotFoundException {
		Scanner s = new Scanner(new File("Match.txt"));
		HashMap<String, Team> mlb = new HashMap<String, Team>();
		ArrayList<Team> natleague = league(s, 9);
		ArrayList<Team> muricaleague = league(s, 10);
		for (Team i : natleague) {
			mlb.put(i.nam, i);
		}
		for (Team i : muricaleague) {
			mlb.put(i.nam, i);
		}
		s.close();
		return mlb;
	}

	private int[] inning(Team a, Team q, int order1, int order2, int qq, PrintWriter writer) {
		int count = 0;
		int out = 0;
		a.pit.get(0).ip++;
		q.pit.get(0).ip++;
		while (out < 3) {
			for (int i = 0; i < a.bat.size(); i++) {
				Simul x = new Simul();
				HashMap<String, Object> d = x.half_inning(a.bat, a.pit.get(0), a.bat.get(order1), count, out);
				order1++;
				order1 = order1 % a.bat.size();
				String b = ((String) d.get("print")).replaceAll("\\s+", " ");
				String[] split = b.split(" ");
				writer.print("T" + qq + " " + b);
				writer.println(d.get("base"));
				out = Integer.parseInt(split[split.length - 1]);
				if (out >= 3) {
					a.bat = x.reset(a.bat);
					break;
				}
				count = Integer.parseInt(split[split.length - 2]);
				a.bat = (ArrayList<Batter>) d.get("bat");
				a.bat.get(order1).batpunishone(split[split.length - 3]);
				q.pit.get(0).pitpunishone(split[split.length - 3]);
				a.bat.get(order1).batpunishsix();
				q.pit.get(0).pitpunishsix();
			}
		}
		a.score += count;
		writer.println("End of T" + qq + " " + a.score + " - " + q.score);
		out = 0;
		count = 0;
		writer.println("");
		while (out < 3) {
			for (int i = 0; i < q.bat.size(); i++) {
				Simul x = new Simul();
				HashMap<String, Object> d = x.half_inning(q.bat, a.pit.get(0), q.bat.get(order2), count, out);
				order2++;
				order2 = order2 % q.bat.size();
				String b = ((String) d.get("print")).replaceAll("\\s+", " ");
				String[] split = b.split(" ");
				writer.print("B" + qq + "" + " " + b);
				writer.println(d.get("base"));
				out = Integer.parseInt(split[split.length - 1]);
				if (out >= 3) {
					q.bat = x.reset(q.bat);
					break;
				}
				count = Integer.parseInt(split[split.length - 2]);
				q.bat = (ArrayList<Batter>) d.get("bat");
				q.bat.get(order2).batpunishone(split[split.length - 3]);
				q.bat.get(order2).batpunishsix();
				a.pit.get(0).pitpunishone(split[split.length - 3]);
				a.pit.get(0).pitpunishsix();
			}
			q.score += count;
			writer.println("End of B" + qq + " " + a.score + " - " + q.score);
			writer.println("");
		}
		out = 0;
		count = 0;
		int[] x = { order1, order2, q.score, a.score };
		return x;
	}

	public String run(Match thematch, PrintWriter writer) throws FileNotFoundException, UnsupportedEncodingException {
		Team a = thematch.one;
		Team q = thematch.two;
		Collections.shuffle(a.bat);
		Collections.shuffle(q.bat);
		int order1 = 0;
		int order2 = 0;
		int qpoint = 0;
		int apoint = 0;
		int overtimelimit = 0;
		writer.println(
				a.nam + " (" + a.win + " - " + a.lost + (")") + (" - ") + q.nam + " (" + q.win + " - " + q.lost + ")");
		for (int qq = 1; qq < 10; qq++) {
			int[] x = inning(a, q, order1, order2, qq, writer);
			order1 = x[0];
			order2 = x[1];
			qpoint = x[2];
			apoint = x[3];
		}
		q.point += qpoint;
		a.point += apoint;
		int num = 9;
		while (q.score == a.score && overtimelimit < 7) {
			num++;
			int[] x = inning(a, q, order1, order2, num, writer);
			order1 = x[0];
			order2 = x[1];
			overtimelimit++;
		}
		if (q.score == a.score) {
			Random rand = new Random();
			boolean cointoss = rand.nextBoolean();
			if (cointoss) {
				q.score++;
			}
			else {
				a.score++;
			}
		}
		writer.println("End of Game " + a.score + "-" + q.score);
		if (a.examine(a.score, q.score) == "win") {
			writer.println(a.nam + " " + "wins " + "(" + a.win + "-" + a.lost + ")");
		} else {
			writer.println(a.nam + " " + "lost " + "(" + a.win + "-" + a.lost + ")");
		}
		if (q.examine(q.score, a.score) == "win") {
			writer.println(q.nam + " " + "wins " + "(" + q.win + "-" + q.lost + ")");
		} else {
			writer.println(q.nam + " " + "lost " + "(" + q.win + "-" + q.lost + ")");
		}
		a.score = 0;
		q.score = 0;
		writer.println(a.nam + " " + "Stats");
		writer.print("Batter\t\tPos\tAB\tH\t2B\t3B\tHR\tR\tRBI\tBB\tK\tAvg");
		writer.println("");
		for (Batter d : a.bat) {
			writer.print(d.nam + "\t" + d.pos + "\t" + d.atbat + "\t" + d.hit + "\t" + d.b2 + "\t" + d.b3 + "\t" + d.hr
					+ "\t" + d.run + "\t" + d.rbi + "\t" + d.bb + "\t" + d.k + "\t");
			d.putinstat();
			d.avg = (int) ((double) d.stats.get("hit") / d.stats.get("atbat") * 100);
			writer.printf("%.2f", (double) d.avg / 100);
			d.stats.put("avg", d.avg);
			writer.println("");
		}
		writer.println("");
		writer.println("Pitcher\t\t\tIP\tH\t2B\t3B\tHR\tR\tERA\tBB\tK\tW-L");
		for (Pitcher p : a.pit) {
			String s;
			if (a.result == true) {
				s = "(W)";
			} else {
				s = "(L)";
			}
			writer.print(String.format("%-15s %-8s %-8s", p.nam, s, p.ip));
			writer.print(p.phit + "\t" + p.pb2 + "\t" + p.pb3 + "\t" + p.phr + "\t" + p.prun + "\t");
			if (p.pstats.get("ip") + p.ip == 0) {
				p.era = 0.00;
			} else {
				p.era = (double) (p.pstats.get("prun") + p.prun) / ((p.pstats.get("ip") + p.ip) / 9);
			}
			writer.printf("%.2f", p.era);
			writer.println("\t" + p.pbb + "\t" + p.pk + "\t" + p.win + "-" + p.loss);
			p.sortoutpitchstat();
		}
		writer.println("");
		writer.println(q.nam + " " + "Stats");
		writer.println("Batter\t\tPos\tAB\tH\t2B\t3B\tHR\tR\tRBI\tBB\tK\tAvg");
		for (Batter d : q.bat) {
			writer.print(d.nam + "\t" + d.pos + "\t" + d.atbat + "\t" + d.hit + "\t" + d.b2 + "\t" + d.b3 + "\t" + d.hr
					+ "\t" + d.run + "\t" + d.rbi + "\t" + d.bb + "\t" + d.k + "\t");
			d.putinstat();
			d.avg = (int) ((double) d.stats.get("hit") / d.stats.get("atbat") * 100);
			writer.printf("%.2f", (double) d.avg / 100);
			d.stats.put("avg", d.avg);
			writer.println("");
		}
		writer.println("");
		writer.println("Pitcher\t\t\tIP\tH\t2B\t3B\tHR\tR\tERA\tBB\tK\tW-L");
		for (Pitcher p : q.pit) {
			String s;
			if (a.result == true) {
				s = "(W)";
			} else {
				s = "(L)";
			}
			writer.print(String.format("%-15s %-8s %-8s", p.nam, s, p.ip));
			writer.print(p.phit + "\t" + p.pb2 + "\t" + p.pb3 + "\t" + p.phr + "\t" + p.prun + "\t");
			if (p.pstats.get("ip") + p.ip == 0) {
				p.era = 0.00;
			} else {
				p.era = (double) (p.pstats.get("prun") + p.prun) / ((p.pstats.get("ip") + p.ip) / 9);
			}
			writer.printf("%.2f", p.era);
			writer.println("\t" + p.pbb + "\t" + p.pk + "\t" + p.win + "-" + p.loss);
			p.sortoutpitchstat();
		}
		writer.println();
		a.shufflepit();
		q.shufflepit();
		thematch.onepoint = apoint;
		thematch.twopoint = qpoint;
		if (a.result == true) {
			return a.nam;
		}
		return q.nam;
	}
}