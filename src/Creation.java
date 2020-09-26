import java.util.ArrayList;
import java.util.Scanner;

public class Creation {
	Creation() {
	}

	public Batter bat(String name, String[] bat, String team) {
		double[] b = new double[bat.length - 1];
		String pos = bat[bat.length - 1];
		for (int i = 0; i < bat.length - 1; i++) {
			b[i] = Double.parseDouble(bat[i]);
		}
		Batter i = new Batter(b, pos, name, team);
		return i;
	}

	public Pitcher pit(String name, String[] pit, String[] bat, String team) {
		double[] b = new double[bat.length - 1];
		double[] p = new double[pit.length];
		String pos = bat[bat.length - 1];
		for (int i = 0; i < bat.length - 1; i++) {
			b[i] = Double.parseDouble(bat[i]);
			p[i] = Double.parseDouble(pit[i]);
		}
		Pitcher i = new Pitcher(b, p, pos, name, team);
		return i;
	}

	public Team ret(Scanner s, int batsize) {
			ArrayList<Batter> bat = new ArrayList<Batter>();
			ArrayList<Pitcher> pit = new ArrayList<Pitcher>();
			String league = s.nextLine();
			String team1 = s.nextLine();
			int batt = 0;
			int pitt = 0;
			while (s.hasNext() && batt < batsize) {
				String name = s.nextLine().trim();
				String[] skill = s.nextLine().trim().split(" ");
				bat.add(bat(name, skill, team1));
				batt++;
			}
			while (s.hasNext() && pitt < 5) {
				String name = s.nextLine().trim();
				String[] skillb = s.nextLine().trim().split(" ");
				String[] skillp = s.nextLine().trim().split(" ");
				pit.add(pit(name, skillp, skillb, team1));
				pitt++;
			}
			Team xx = new Team(bat, pit, team1, league);
			return xx;
	}
}