import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
public class Simul {
	ArrayList<Batter> batt;
	ArrayList<Pitcher> pitt;
	String[] d = { "strikeout", "walk", "single", "double", "triple", "homerun", "groundout", "flyout" };

	Simul(ArrayList<Batter> a, ArrayList<Pitcher> b) {
		batt = a;
		pitt = b;
	}

	Simul() {

	}

	public String simulation(HashMap<String, Double> pit, HashMap<String, Double> bat) {
		ArrayList<String> res = new ArrayList<String>();
		Random rand = new Random();
		if (pit.get("pstrikeout") - bat.get("strikeout") <= 0) {
			for (int chance = 0; chance < 4; chance++) {
				res.add(d[0]);
			}
		} else
			for (int i = 0; i < pit.get("pstrikeout") - bat.get("strikeout"); i++) {
				for (int chance = 0; chance < 4; chance++) {
					res.add(d[0]);
				}
			}
		if (bat.get("single") - pit.get("psingle") <= 0) {
			for (int chance = 0; chance < 2; chance++) {
				res.add(d[1]);
			}
		} else
			for (int i = 0; i < bat.get("single") - pit.get("psingle"); i++) {
				for (int chance = 0; chance < 2; chance++) {
					res.add(d[1]);
				}
			}
		if (bat.get("walk") - pit.get("pwalk") <= 0) {
			for (int chance = 0; chance < 2; chance++) {
				res.add(d[2]);
			}
		} else
			for (int i = 0; i < bat.get("walk") - pit.get("pwalk"); i++) {
				for (int chance = 0; chance < 2; chance++) {
					res.add(d[2]);
				}
			}
		if (bat.get("double") - pit.get("pdouble") <= 0) {
			for (int chance = 0; chance < 1; chance++) {
				res.add(d[3]);
			}
		} else
			for (int i = 0; i < bat.get("double") - pit.get("pdouble"); i++) {
				for (int chance = 0; chance < 1; chance++) {
					res.add(d[3]);
				}
				;
			}
		if (bat.get("triple") - pit.get("ptriple") <= 0) {
			for (int chance = 0; chance < 1; chance++) {
				res.add(d[4]);
			}
		} else
			for (int i = 0; i < bat.get("triple") - pit.get("ptriple"); i++) {
				for (int chance = 0; chance < 1; chance++) {
					res.add(d[4]);
				}
			}
		if (bat.get("homerun") - pit.get("phomerun") <= 0) {
			for (int chance = 0; chance < 1; chance++) {
				res.add(d[5]);
			}
		} else
			for (int i = 0; i < bat.get("homerun") - pit.get("phomerun"); i++) {
				for (int chance = 0; chance < 1; chance++) {
					res.add(d[5]);
				}
			}
		if (pit.get("pgroundout") - bat.get("groundout") <= 0) {
			for (int chance = 0; chance < 4; chance++) {
				res.add(d[6]);
			}
		} else
			for (int i = 0; i < pit.get("pgroundout") - bat.get("groundout"); i++) {
				for (int chance = 0; chance < 4; chance++) {
					res.add(d[6]);
				}
			}
		if (pit.get("pflyout") - bat.get("flyout") <= 0) {
			for (int chance = 0; chance <= 4; chance++) {
				res.add(d[7]);
			}
		} else
			for (int i = 0; i < pit.get("pflyout") - bat.get("flyout"); i++) {
				for (int chance = 0; chance <= 4; chance++) {
					res.add(d[7]);
				}
			}
		int d = rand.nextInt(res.size());
		return res.get(d);
	}
	public ArrayList<Batter> reset(ArrayList<Batter> bat)
	{
		for(Batter i: bat)
		{
			i.base = 0;
		}
		return bat;
	}
	public HashMap<String,Object> half_inning(ArrayList<Batter> bat, Pitcher pit, Batter i, int score, int out) {
		String d = simulation(pit.pit, i.bat);
		Batter[] batter = { bat.get(0), bat.get(1), bat.get(2), bat.get(3), bat.get(4), bat.get(5), bat.get(6),
				bat.get(7), bat.get(8) };
		boolean present1 = false;
		boolean present2 = false;
		if (d.equals("strikeout")) {
			i.k++;
			i.atbat++;
			out++;
			i.base = 0;
			pit.pk++;
		}
		if (d.equals("homerun")) {
			pit.phit++;
			pit.phr++;
			i.base += 4;
			i.atbat++;
			i.hit++;
			i.hr++;
			for (Batter a : batter) {
				if (a.base != 0) {
					a.base += 4;
				}
			}
		}
		if (d.equals("walk")) {
			for (int bb = 0; bb < batter.length; bb++) {
				if (batter[bb].base == 1) {
					present1 = true;
				}
				if (batter[bb].base == 2) {
					present2 = true;
				}
			}
			for (Batter a : batter) {
				if (a.base == 3 && present2 == true && present1 == true) {
					a.base++;
				}
				if (a.base == 2 && present1 == true) {
					a.base++;
				}
				if (a.base == 1) {
					a.base++;
				}
			}
			present1 = false;
			present2 = false;
			i.base++;
			i.bb++;
			pit.pbb++;
			pit.phit++;
		}
		if (d.equals("single")) {
			for (Batter a : batter) {
				if (a.base != 0) {
					a.base += 1;
				}
			}
			i.base++;
			i.hit++;
			i.atbat++;
			pit.phit++;
		}
		if (d.equals("double")) {
			for (Batter a : batter) {
				if (a.base != 0) {
					a.base += 2;
				}
			}
			i.base += 2;
			i.hit++;
			i.b2++;
			i.atbat++;
			pit.pb2++;
			pit.phit++;
		}
		if (d.equals("triple")) {
			for (Batter a : batter) {
				if (a.base != 0) {
					a.base += 3;
				}
			}
			i.base += 3;
			i.hit++;
			i.b3++;
			i.atbat++;
			pit.pb3++;
			pit.phit++;
		}
		if (d.equals("groundout")) {
			i.k++;
			i.atbat++;
			int coin = (int) (Math.random() * 2 + 1);
			if (coin == 1) {
				i.base = 0;
				out++;
				d = "groundout double play";
				for (Batter a : batter) {
					if (out >= 3) {
						bat = reset(bat);
						break;
					}
					if (a.base != 0) {
						a.base += 1;
					}
				}
			}
			if (coin == 2) {
				i.base = 0;
				out++;
				d = "groundout advance";
				for (Batter a : batter) {
					if (out >= 3) {
						bat = reset(bat);
						break;
					}
					if (a.base == 1) {
						present1 = true;
						a.base = 0;
						out++;
					}
					if (a.base == 2 && present1 == true) {
						a.base += 1;
						present2 = true;
						break;
					}
					if (a.base == 3 && present2 == true) {
						a.base += 1;
					}
				}
				present1 = false;
				present2 = false;
			}
		}
		if (d.equals("flyout")) {
			i.k++;
			i.atbat++;
			int coin = (int) (Math.random() * 2 + 1);
			if (coin == 1) {
				i.base = 0;
				out++;
				d = "flyout advance";
				for (Batter a : batter) {
					if (a.base == 2) {
						a.base += 1;
						break;
					}
					if (a.base == 3) {
						a.base += 1;
					}
				}
			}
			if (coin == 2) {
				i.base = 0;
				out++;
				d = "flyout no advance";
			}
		}
		StringBuilder base = new StringBuilder();
		for (Batter ddd : bat) {
			if (ddd.base != 0) {
				if (ddd.base == 1) {
					base.append(" " + ddd.nam + " @1B");
				}
				if (ddd.base == 2) {
					base.append(" " + ddd.nam + " @2B");
				}
				if (ddd.base == 3) {
					base.append(" " + ddd.nam + " @3B");
				}
				if (ddd.base >= 4) {
					base.append(" " + ddd.nam + " scores");
				}
			}
		}
		for (Batter a : batter) {
			if (a.base >= 4) {
				a.base = 0;
				score++;
				a.run++;
				pit.prun++;
				a.rbi++;
			}
		}
		HashMap<String,Object> y = new HashMap<String,Object>();
		y.put("print",i.nam + " " + d + " " + score + " " + out + "   ");
		y.put("bat",bat);
		y.put("base",base.toString());
		return y;
	}
}