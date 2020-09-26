import java.util.HashMap;
public class Batter implements Comparable {
	String pos;
	String nam;
	String team;
	int base = 0;
	int rbi = 0;
	int atbat = 0;
	int hit = 0;
	int b2 = 0;
	int b3 = 0;
	int k = 0;
	int hr = 0;
	int bb = 0;
	int run = 0;
	int avg = 0;
	int consout = 0;
	int conswalk = 0;
	int conssingle = 0;
	int consdouble = 0;
	int constriple = 0;
	int conshome = 0;
	int consground = 0;
	int consfly = 0;
	HashMap<String,Integer> stats = new HashMap<String,Integer>();
	boolean seasonpassed = false;
	HashMap<String, Double> bat = new HashMap<String, Double>();
	Batter(double[] a, String position, String name,String origin) {
		bat.put("strikeout", a[0]);
		bat.put("walk", a[1]);
		bat.put("single", a[2]);
		bat.put("double", a[3]);
		bat.put("triple", a[4]);
		bat.put("homerun", a[5]);
		bat.put("groundout", a[6]);
		bat.put("flyout", a[7]);
		stats.put("rbi", 0);
		stats.put("atbat", 0);
		stats.put("hit", 0);
		stats.put("b2", 0);
		stats.put("b3", 0);
		stats.put("k", 0);
		stats.put("bb", 0);
		stats.put("run", 0);
		stats.put("hr", 0);
		stats.put("avg", 0);
		pos = position;
		nam = name;
		team = origin;
	}
	public void putinstat()
	{
		stats.put("rbi", stats.get("rbi")+rbi);
		stats.put("atbat", stats.get("atbat")+atbat);
		stats.put("hit", stats.get("hit")+hit);
		stats.put("b2", stats.get("b2")+b2);
		stats.put("b3", stats.get("b3")+b3);
		stats.put("k", stats.get("k")+k);
		stats.put("bb", stats.get("bb")+bb);
		stats.put("run", stats.get("run")+run);
		stats.put("hr",stats.get("hr")+hr);
		stats.put("avg", avg);
		rbi = 0;
		atbat = 0;
		hit = 0;
		b2 = 0;
		b3 = 0;
		k = 0;
		bb = 0;
		run = 0;
		hr = 0;
	}
	public void batpunishone(String resb) {
		if (resb.equals("strikeout")) {
			bat.put("strikeout", bat.get("strikeout") - 0.1);
			consout = 0;
		} else {
			consout++;
		}
		if (resb.equals("walk")) {
			bat.put("walk", bat.get("walk") + 0.1);
			conswalk = 0;
		} else {
			conswalk++;
		}
		if (resb.equals("single")) {
			bat.put("single", bat.get("single") + 0.1);
			conssingle = 0;
		} else {
			conssingle++;
		}
		if (resb.equals("double")) {
			bat.put("double", bat.get("double") + 0.1);
			consdouble = 0;
		} else {
			consdouble++;
		}
		if (resb.equals("triple")) {
			bat.put("triple", bat.get("triple") + 0.1);
			constriple++;
		}
		if (resb.equals("homerun")) {
			bat.put("homerun", bat.get("homerun") + 0.1);
			conshome = 0;
		} else {
			conshome++;
		}
		if (resb.equals("groundout")) {
			bat.put("groundout", bat.get("groundout") - 0.1);
			consground = 0;
		} else {
			consground++;
		}
		if (resb.equals("flyout")) {
			bat.put("flyout", bat.get("flyout") - 0.1);
			consfly = 0;
		} else {
			consfly++;
		}
	}
	public void batpunishsix() {
		if (consout >= 6) {
			bat.put("strikeout", bat.get("strikeout") + 0.1);
		}
		if (conswalk >= 6) {
			bat.put("walk", bat.get("walk") - 0.1);
		}
		if (conssingle >= 6) {
			bat.put("single", bat.get("single") - 0.1);
		}
		if (conshome >= 6) {
			bat.put("homerun", bat.get("homerun") - 0.1);
		}
		if (consdouble >= 6) {
			bat.put("double", bat.get("double") - 0.1);
		}
		if (seasonpassed == true && constriple == 0) {
			bat.put("triple", bat.get("triple") - 0.1);
		}
		if (consfly >= 6) {
			bat.put("flyout", bat.get("flyout") + 0.1);
		}
		if (consground >= 6) {
			bat.put("groundout", bat.get("groundout") + 0.1);
		}
	}
	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
}