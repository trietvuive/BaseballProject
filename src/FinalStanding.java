import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FinalStanding {
	HashMap<String, Team> league = new HashMap<String, Team>();
	ArrayList<Team> murica = new ArrayList<Team>();
	ArrayList<Team> national = new ArrayList<Team>();

	FinalStanding(HashMap<String, Team> all, ArrayList<Team> mur,
			ArrayList<Team> nat) {
		league = all;
		murica = mur;
		national = nat;
	}

	FinalStanding() {
	}

	ArrayList<Batter> top10bat(String stats, ArrayList<Team> league) {
		ArrayList<Batter> top = new ArrayList<Batter>();
		ArrayList<Batter> bat = new ArrayList<Batter>();
		for (Team i : league) {
			if (i.blank == false) {
				for (Batter a : i.bat) {
					bat.add(a);
				}
			}
		}
		Batter[] entire = bat.toArray(new Batter[bat.size()]);
		sortb(entire, 0, entire.length - 1, stats);
		for (int i = 0; i < 10; i++) {
			top.add(entire[i]);
		}
		return top;
	}

	ArrayList<Pitcher> top10pit(String stats, ArrayList<Team> league) {
		ArrayList<Pitcher> top = new ArrayList<Pitcher>();
		ArrayList<Pitcher> pit = new ArrayList<Pitcher>();
		for (Team i : league) {
			if (i.blank == false) {
				for (Pitcher a : i.pit) {
					pit.add(a);
				}
			}
		}
		Pitcher[] entire = pit.toArray(new Pitcher[pit.size()]);
		sortp(entire, 0, entire.length - 1, stats);
		for (int i = 0; i < 10; i++) {
			top.add(entire[i]);
		}
		return top;
	}

	int partitionb(Batter arr[], int low, int high, String stat) {
		Batter pivot = arr[high];
		int i = (low - 1);
		for (int j = low; j < high; j++) {
			if (arr[j].stats.get(stat) >= pivot.stats.get(stat)) {
				i++;
				Batter temp = arr[i];
				arr[i] = arr[j];
				arr[j] = temp;
			}
		}
		Batter temp = arr[i + 1];
		arr[i + 1] = arr[high];
		arr[high] = temp;
		return i + 1;
	}

	private void sortb(Batter arr[], int low, int high, String stat) {
		if (low < high) {
			int pi = partitionb(arr, low, high, stat);
			sortb(arr, low, pi - 1, stat);
			sortb(arr, pi + 1, high, stat);
		}
	}

	private int partitionp(Pitcher arr[], int low, int high, String stat) {
		Pitcher pivot = arr[high];
		int i = (low - 1);
		for (int j = low; j < high; j++) {
			if (arr[j].pstats.get(stat) >= pivot.pstats.get(stat)) {
				i++;
				Pitcher temp = arr[i];
				arr[i] = arr[j];
				arr[j] = temp;
			}
		}
		Pitcher temp = arr[i + 1];
		arr[i + 1] = arr[high];
		arr[high] = temp;
		return i + 1;
	}

	private void sortp(Pitcher arr[], int low, int high, String stat) {
		if (low < high) {
			int pi = partitionp(arr, low, high, stat);
			sortp(arr, low, pi - 1, stat);
			sortp(arr, pi + 1, high, stat);
		}
	}

	ArrayList<Team> listonwin(ArrayList<Team> a) {
		Team[] league = a.toArray(new Team[a.size()]);
		Arrays.sort(league);
		ArrayList<Team> win = new ArrayList<Team>();
		for (int i = 0;i<league.length;i++) {
			win.add(league[i]);
		}
		return win;
	}
}