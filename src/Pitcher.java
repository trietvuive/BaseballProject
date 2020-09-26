import java.util.HashMap;
public class Pitcher extends Batter{
	String nam;
	String pos;
	int pb2 = 0;
	int pb3 = 0;
	int phr = 0;
	int ip = 0;
	int phit = 0;
	int prun = 0;
	double era = 0;
	int pbb = 0;
	int pk = 0;
	int win = 0;
	int loss = 0;
	int conspout = 0;
	int conspwalk = 0;
	int conspsingle = 0;
	int conspdouble = 0;
	int consptriple = 0;
	int consphome = 0;
	int conspground = 0;
	int conspfly = 0;
	String team;
	HashMap<String, Double> pit = new HashMap<String, Double>();
	HashMap<String,Integer> pstats = new HashMap<String,Integer>();
	Pitcher(double[] a, double[] b, String position, String name,String origin) {
		super(a, position, name,origin);
		pit.put("pstrikeout", b[0]);
		pit.put("pwalk", b[1]);
		pit.put("psingle", b[2]);
		pit.put("pdouble", b[3]);
		pit.put("ptriple", b[4]);
		pit.put("phomerun", b[5]);
		pit.put("pgroundout", b[6]);
		pit.put("pflyout", b[7]);
		pstats.put("pb2", 0);
		pstats.put("pb3", 0);
		pstats.put("phr", 0);
		pstats.put("ip", 0);
		pstats.put("phit",0);
		pstats.put("prun", 0);
		pstats.put("pbb", 0);
		pstats.put("pk", 0);
		team = origin;
		pos = position;
		nam = name;
	}
	public void sortoutpitchstat()
	{
		pstats.put("phr", pstats.get("phr")+phr);
		pstats.put("ip", pstats.get("ip")+ip);
		pstats.put("phit", pstats.get("phit")+phit);
		pstats.put("pb2", pstats.get("pb2")+pb2);
		pstats.put("pb3", pstats.get("pb3")+pb3);
		pstats.put("pk", pstats.get("pk")+pk);
		pstats.put("pbb", pstats.get("pbb")+pbb);
		pstats.put("prun", pstats.get("prun")+prun);
		phr = 0;
		phit = 0;
		pb2 = 0;
		pk = 0;
		pbb = 0;
		prun = 0;
		pb3 = 0;
		ip = 0;
	}
	public void pitpunishone(String resp) {
		if (resp.equals("strikeout")) {
			pit.put("pstrikeout", pit.get("pstrikeout") + 0.1);
			conspout = 0;
		} else {
			conspout++;
			if (resp.equals("walk")) {
				pit.put("pwalk", pit.get("pwalk") - 0.1);
				conspwalk = 0;
			} else {
				conspwalk++;
			}
			if (resp.equals("single")) {
				pit.put("psingle", pit.get("psingle") - 0.1);
				conspsingle = 0;
			} else {
				conspsingle++;
			}
			if (resp.equals("double")) {
				pit.put("pdouble", pit.get("pdouble") - 0.1);
				conspdouble = 0;
			} else {
				conspdouble++;
			}
			if (resp.equals("triple")) {
				pit.put("ptriple", pit.get("ptriple") - 0.1);
				consptriple++;
			}
			if (resp.equals("homerun")) {
				pit.put("phomerun", pit.get("phomerun") - 0.1);
				consphome = 0;
			} else {
				consphome++;
			}
			if (resp.equals("groundout")) {
				pit.put("pgroundout", pit.get("pgroundout") + 0.1);
				conspground = 0;
			} else {
				conspground++;
			}
			if (resp.equals("flyout")) {
				pit.put("pflyout", pit.get("pflyout") + 0.1);
				conspfly = 0;
			}
			else {
				conspfly++;
			}
		}
	}
	public void pitpunishsix() {
		if (conspout >= 6) {
			pit.put("pstrikeout", pit.get("pstrikeout") - 0.1);
		}
		if (conspwalk >= 6) {
			pit.put("pwalk", pit.get("pwalk") - 0.1);
		}
		if (conspsingle >= 6) {
			pit.put("psingle", pit.get("psingle") - 0.1);
		}
		if (consphome >= 6) {
			pit.put("phomerun", pit.get("phomerun") - 0.1);
		}
		if (conspdouble >= 6) {
			pit.put("pdouble", pit.get("pdouble") - 0.1);
		}
		if (seasonpassed == true && consptriple == 0) {
			pit.put("ptriple", pit.get("ptriple") - 0.1);
		}
		if (conspfly >= 6) {
			pit.put("pflyout", pit.get("pflyout") + 0.1);
		}
		if (conspground >= 6) {
			pit.put("pgroundout", pit.get("pgroundout") + 0.1);
		}
	}
}