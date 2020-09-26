import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Team implements Comparable<Team> {
	ArrayList<Batter> bat;
	ArrayList<Pitcher> pit;
	int win = 0;
	int lost = 0;
	int point = 0;
	int score;
	String nam = "error";
	String league;
	boolean result;
	boolean blank = false;
	int i = 1;
	int playoffwin = 0;
	int playofflose = 0;
	int lotterywin = 0;
	int lotterylose = 0;
	HashMap<Integer, Match> num = new HashMap<Integer, Match>();

	Team(ArrayList<Batter> b, ArrayList<Pitcher> p, String name, String leag) {
		bat = b;
		pit = p;
		nam = name;
		league = leag;
	}
	public Team(String name) {
		this.blank = true;
		this.nam = name;
	}
	public Team(boolean a) {
		this.blank = true;
		this.nam = "";
	}

	public String examine(int result1, int result2) {
		if (result1 > result2) {
			win++;
			result = true;
			pit.get(0).win++;
			return "win";
		}
		if (result1 < result2) {
			lost++;
			result = false;
			pit.get(0).loss++;
			return "lost";
		}
		return "nah";
	}

	public void shufflepit() {
		pit.add(pit.get(0));
		pit.remove(0);
	}
	void lotteryexaminer(String i){
		if(this.nam.equals(i)){
			lotterywin++;
			win++;
		}
		else{
			lost++;
			lotterylose++;
		}
	}
	void dayschedule(ArrayList<Match> a) {
		for (Match x : a) {
			if (x.blank == false) {
				if (x.one.equals(this) || x.two.equals(this)) {
					num.put(x.day, x);
				}
			}
		}
		for (int i = 1; i < num.size(); i++) {
			if (num.containsKey(i) == false) {
				num.put(i, new Match(i));
			}
		}
	}

	boolean assignwin(int a, String b) {
		for (Map.Entry<Integer, Match> entry : num.entrySet()) {
			if (entry.getKey().equals(a)) {
				entry.getValue().win = b;
				return true;
			}
		}
		return false;
	}
	int series(Team a) {
		int thiswin = 0;
		int theirwin = 0;
		for (Map.Entry<Integer, Match> entry : num.entrySet()) {
			Match x = entry.getValue();
			if (x.blank != true) {
				if (x.one.equals(this) || x.two.equals(this)) {
					if (x.two.equals(a) || x.one.equals(a)) {
						if (x.win.equals(this.nam)) {
							thiswin++;
						}
						if (x.win.equals(a.nam)) {
							theirwin++;
						}
					}
				}
			}
		}
		return theirwin - thiswin;
	}
	@Override
	public int compareTo(Team arg0) {
		if (arg0.win == this.win) {
			if (arg0.lost == this.lost) {
				if (series(arg0) == 0) {
					if (arg0.point == this.point) {
						Random random = new Random();
						boolean isOne = random.nextBoolean();
						if (isOne)
							return 1;
						else
							return -1;
					}
					return arg0.point - this.point;
				}
				return series(arg0);
			}
			return this.lost-arg0.lost;
		}
		return arg0.win - this.win;
	}
}
class LotteryComparator implements Comparator<Team>{
	public int compare(Team a,Team b){
		if(a.lotterywin == b.lotterywin)
			return Integer.compare(a.lotterylose, b.lotterylose);
		return Integer.compare(a.lotterywin, b.lotterywin);
	}
}
class PlayoffComparator implements Comparator<Team>{
	public int compare(Team a,Team b){
		if(a.playoffwin == b.playoffwin)
			return Integer.compare(a.playofflose, b.playofflose);
		return Integer.compare(a.playoffwin, b.playoffwin);
	}
}