import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;

public class Schedule {
	Schedule() {
	}

	ArrayList<Match> roundrobin(ArrayList<Team> a) throws FileNotFoundException, UnsupportedEncodingException {
		Collections.shuffle(a);
		ArrayList<Match> q = new ArrayList<Match>();
		Team blank = new Team(true);
		a.add(blank);
		ArrayList<Team> seriesone = new ArrayList<Team>();
		ArrayList<Team> seriestwo = new ArrayList<Team>();
		for (int i = 0; i < a.size() / 2; i++) {
			seriesone.add(a.get(i));
			seriestwo.add(a.get(i + a.size() / 2));
		}
		ArrayList<ArrayList<Match>> master = new ArrayList<ArrayList<Match>>();
		for (int i = 1; i < a.size(); i++) {
			ArrayList<Match> home = seriesblock(seriesone,seriestwo);
			ArrayList<Match> away = seriesblock(seriestwo,seriesone);
			master.add(home);
			master.add(away);
			rotate(seriesone, seriestwo);
		}
		Collections.shuffle(master);
		int count2 = 0;
		int day = 1;
		for (ArrayList<Match> i : master) {
			for(int count = 0;count<3;count++)
			{
				for(Match z:i)
				{
					Team one = z.one;
					Team two = z.two;
					Match newone = new Match(one,two,day);
					if(one.blank == true||two.blank == true)
					{
						newone.blank = true;
					}
					q.add(newone);
					count2++;
				}
				day++;
			}
			day++;
		}
		System.out.println(count2);
		return q;
	}

	ArrayList<Match> seriesblock(ArrayList<Team> one, ArrayList<Team> two) {
		ArrayList<Match> series = new ArrayList<Match>();
		for (int i = 0; i < one.size(); i++) {
			Match a = new Match(one.get(i),two.get(i));
			series.add(a);
		}
		return series;
	}

	void rotate(ArrayList<Team> one, ArrayList<Team> two) {
		two.add(0, one.get(1));
		Team temp = two.remove(two.size() - 1);
		one.remove(1);
		one.add(temp);
	}
}
