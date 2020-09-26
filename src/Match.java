public class Match implements Comparable {
	Team one;
	Team two;
	Team home = one;
	int day;
	int onepoint;
	int twopoint;
	String win = "bla";
	boolean blank = false;
	Match(Team a, Team b,int d)
	{
		home = a;
		one = a;
		two = b;
		day = d;
	}
	Match(int d){
		blank = true;
		day = d;
	}
	Match(Team a){
		blank = true;
		one = a;
		two = new Team(true);
	}
	Match(Team a,Team b)
	{
		home = a;
		one = a;
		two = b;
	}
	boolean finish(){
		if(win.equals("bla")){
			return false;
		}
		return true;
	}
	public int compareTo(Object arg0) {
		Match a = (Match) arg0;
		if(this.day == a.day)
		{
			return this.one.nam.compareTo(a.one.nam);
		}
		return this.day-a.day;
	}
}
