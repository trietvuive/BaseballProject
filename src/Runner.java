import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Runner {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		restart();
	}
	static void restart() throws IOException {
		try {
			Files.deleteIfExists(Paths.get("playoff.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		ArrayList<Object> a = new ArrayList<Object>();
		PrintWriter writer = new PrintWriter("result.txt", "UTF-8");
		Generator y = new Generator();
		y.write();
		GameLog x = new GameLog();
		HashMap<String, Team> league = x.mlb();
		ArrayList<Team> murica = new ArrayList<Team>();
		ArrayList<Team> national = new ArrayList<Team>();
		Schedule s = new Schedule();
		for (Map.Entry<String, Team> value : league.entrySet()) {
			if (value.getValue().league.equals("American League")) {
				murica.add(value.getValue());
			}
			if (value.getValue().league.equals("National League")) {
				national.add(value.getValue());
			}
		}
		ArrayList<Match> muricaschedule = s.roundrobin(murica);
		ArrayList<Match> natschedule = s.roundrobin(national);
		for (Team aaa : murica) {
			aaa.dayschedule(muricaschedule);
		}
		for (Team aaa : national) {
			aaa.dayschedule(natschedule);
		}
		GuiApp1 inter = new GuiApp1(writer,murica,national,league,muricaschedule,natschedule);
		Match test = new Match(national.get(0),murica.get(0));
		x.run(test, writer);
		writer.close();
		inter.MainMenu();
	}

	static void runeachleague(Stack<Match> mlbschedule,PrintWriter writer,
			int dayend)
			throws FileNotFoundException, UnsupportedEncodingException {
		GameLog x = new GameLog();
		while(!mlbschedule.isEmpty() && mlbschedule.peek().day<= dayend)
		{
			Match match = mlbschedule.pop();
			String n = x.run(match,writer);
			match.one.assignwin(match.day, n);
			match.two.assignwin(match.day, n);
		}
	}

	Stack<Match> runnextgame(Stack<Match> mlbsched,String name)
			throws FileNotFoundException, UnsupportedEncodingException {
		if(mlbsched.isEmpty())
			return null;
		GameLog x = new GameLog();
		Match murica = mlbsched.pop();
		PrintWriter writer = new PrintWriter(new File(name),"UTF-8");
		String n = x.run(murica, writer);
		murica.one.assignwin(murica.day, n);
		murica.two.assignwin(murica.day, n);
		writer.close();
		return mlbsched;
	}
}