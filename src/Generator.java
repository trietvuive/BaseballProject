import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

public class Generator {
	Generator() {
	}
	Stack<String> name() throws FileNotFoundException {
		Set<String> first = new HashSet<String>();
		Stack<String> a = new Stack<String>();
		Scanner s = new Scanner(new File("baseballnames.txt"));
		while(s.hasNext())
		{
			first.add(s.nextLine());
		}
		for(String i: first)
		{
			a.add(i);
		}
		Collections.shuffle(a);
		s.close();
		return a;
	}
	Stack<String> all30() throws FileNotFoundException
	{
		Stack<String> team = new Stack<String>();
		Scanner s = new Scanner(new File("team.txt"));
		while(s.hasNext())
		{
			team.push(s.nextLine().trim());
		}
		s.close();
		return team;
	}
	ArrayList<Integer> randomize() {
		Random rand = new Random();
		ArrayList<Integer> shufflebag = new ArrayList<Integer>();
		ArrayList<Integer> a = new ArrayList<Integer>();
		for (int i = 0; i < 9; i++) {
			shufflebag.add(rand.nextInt(5) + 1);
			shufflebag.add(rand.nextInt(5) + 1);
			shufflebag.add(rand.nextInt(5) + 6);
		}
		Collections.shuffle(shufflebag);
		for (int i = 0; i < 8; i++) {
			a.add(shufflebag.get(i));
		}
		return a;
	}

	Stack<String> position() {
		Stack<String> position = new Stack<String>();
		position.push("C");
		position.push("1B");
		position.push("2B");
		position.push("3B");
		position.push("SS");
		position.push("RF");
		position.push("CF");
		position.push("LF");
		position.push("SP");
		return position;
	}
	ArrayList<String> league() {
		ArrayList<String> league = new ArrayList<String>();
		league.add("National League");
		league.add("American League");
		return league;
	}

	void write() throws FileNotFoundException, UnsupportedEncodingException {
		ArrayList<String> league = league();
		PrintWriter writer = new PrintWriter("Match.txt", "UTF-8");
		Stack<String> all = all30();
		Stack<String> playername = name();
		Collections.shuffle(all);
		for (int i = 0; i < 15; i++) {
			String name = all.pop();
			Stack<String> pos = position();
			oneteam(writer,league.get(0),pos,name,playername);
		}
		for(int i = 0;i<15;i++)
		{
			String name = all.pop();
			Stack<String> pos = position();
			pos.push("DH");
			oneteam(writer,league.get(1),pos,name,playername);
		}
		writer.close();
	}

	void oneteam(PrintWriter writer, String league, Stack<String> position,String team,Stack<String> name) throws FileNotFoundException, UnsupportedEncodingException {
		writer.println(league);
		writer.println(team);
		int numberofbat = position.size();
		for (int bat = 0; bat < numberofbat; bat++) {
			ArrayList<Integer> randomize = randomize();
			Collections.shuffle(position);
			writer.println(name.pop());
			for (int z = 0; z < randomize.size(); z++) {
				writer.print(randomize.get(z) + " ");
			}
			writer.println(position.pop());
		}
		for(int pit = 0;pit<5;pit++)
		{
			ArrayList<Integer> randomizebat = randomize();
			writer.println(name.pop());
			ArrayList<Integer> randomizepit = randomize();
			for(int count = 0;count<randomizebat.size();count++)
			{
				writer.print(randomizebat.get(count)+" ");
			}
			writer.print("P");
			writer.println("");
			for(int count = 0;count<randomizepit.size();count++)
			{
				writer.print(randomizepit.get(count)+" ");
			}
			writer.println("");
		}
	}

}