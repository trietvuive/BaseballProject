import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import javax.swing.JButton;

public class PlayoffBlock {
	JButton button;
	private PlayoffBlock neighbor = null;
	private PlayoffBlock next = null;
	private PlayoffBlock nextmatch = null;
	private PlayoffBlock loserblock = null;
	boolean davoid = false;
	Team homefield, one, two;
	int onewin, twowin = 0;
	int rectsize;
	Stack<Match> allmatch = new Stack<Match>();
	private int inheritance;
	private int nummatch;
	private boolean onlyonce = false;
	int round;
	int y;

	PlayoffBlock(Team a, Team b, int nummatch, PlayoffBlock neighbormatch) {
		nextmatch = new PlayoffBlock();
		this.nummatch = nummatch;
		neighbor = neighbormatch;
		one = a;
		two = b;
		Team c, d;
		if (random() == true) {
			c = a;
			d = b;
		} else {
			c = b;
			d = a;
		}
		for (int i = 0; i < nummatch; i++) {
			Team temp = c;
			allmatch.push(new Match(c, d));
			c = d;
			d = temp;
		}
		setLoserblock(new PlayoffBlock());
		if (one == null)
			one = new Team("");
		if (two == null)
			two = new Team("");
	}

	PlayoffBlock() {
		one = new Team("Final");
		two = new Team("Final");
		onlyonce = false;
	}

	PlayoffBlock(Team winner) {
		onlyonce = false;
		one = winner;
		two = winner;
	}

	PlayoffBlock(Team a, Team b, Team homefield, int nummatch, PlayoffBlock neighbormatch) {
		neighbor = neighbormatch;
		one = a;
		two = b;
		Team c, d;
		if (a.equals(homefield)) {
			c = a;
			d = b;
		} else {
			c = b;
			d = a;
		}
		for (int i = 0; i < nummatch; i++) {
			Team temp = c;
			allmatch.push(new Match(c, d));
			c = d;
			d = temp;
		}
		setLoserblock(new PlayoffBlock());
		if (one == null)
			one = new Team("");
		if (two == null)
			two = new Team("");
	}

	void fillStack() {
		allmatch.removeAll(allmatch);
		Team c, d;
		if (random() == true) {
			c = one;
			d = two;
		} else {
			c = two;
			d = one;
		}
		for (int i = 0; i < nummatch; i++) {
			Team temp = c;
			allmatch.push(new Match(c, d));
			c = d;
			d = temp;
		}
	}

	boolean random() {
		Random rand = new Random();
		return rand.nextBoolean();
	}

	Match playmatch() {
		if (!allmatch.isEmpty())
			return allmatch.pop();
		return null;
	}

	void setNextMatch(PlayoffBlock next) {
		nextmatch = next;
	}

	boolean avaliable() {
		if (onlyonce) {
			return false;
		}
		if (one.blank == true || two.blank == true)
			return false;
		if (one == null || two == null)
			return false;
		if (complete())
			return false;
		return true;
	}

	PlayoffBlock getNextMatch() {
		return nextmatch;
	}

	void setButton(JButton getbut) {
		button = getbut;
	}

	void setNextNeighbor(PlayoffBlock a) {
		next = a;
		neighbor = a;
	}

	JButton getButton() {
		return button;
	}

	void setNext(PlayoffBlock getnext) {
		next = getnext;
	}

	PlayoffBlock getNext() {
		return next;
	}

	PlayoffBlock getNeighbor() {
		return neighbor;
	}

	void setneighbor(PlayoffBlock neighbormatch) {
		neighbor = neighbormatch;
	}

	public int getInheritance() {
		return inheritance;
	}

	public void setInheritance(int inherit) {
		this.inheritance = inherit;
	}

	void matchcomplete(String x) {
		if (x.equals(one.nam))
			onewin++;
		if (x.equals(two.nam))
			twowin++;
	}

	boolean complete() {
		if (onlyonce) {
			return false;
		}
		if (Math.abs((double) onewin - twowin) > allmatch.size())
			return true;
		if (allmatch.isEmpty())
			return true;
		return false;
	}

	Team whowin() {
		if (onewin > twowin)
			return one;
		if (twowin > onewin)
			return two;
		if (one.blank == true || one == null)
			return two;
		if (two.blank == true || one == null)
			return one;
		return new Team("Winner");
	}

	Team wholose() {
		if (one == whowin())
			return two;
		if(two == whowin())
		{
			return one;
		}
		return new Team("Loser");
	}

	void run() {
		if (!complete() && avaliable()) {
			try {
				FileWriter fw = new FileWriter(new File("playoff.txt"), true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw);
				GameLog x = new GameLog();
				String winner = x.run(allmatch.pop(), out);
				examine(winner);
				matchcomplete(winner);
				fw.close();
			} catch (IOException e) {
			}
		}
	}

	void rununtilempty() {
		if (!complete() && avaliable()) {
			try {
				FileWriter fw = new FileWriter(new File("playoff.txt"), true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw);
				GameLog x = new GameLog();
				while (!allmatch.isEmpty() && !complete()) {
					String winner = x.run(allmatch.pop(), out);
					examine(winner);
					matchcomplete(winner);
				}
				fw.close();
			} catch (IOException e) {
			}
		}
	}

	void examine(String s) {
		if (one.nam.equals(s)) {
			one.playoffwin++;
			two.playofflose++;
			one.win++;
			two.lost++;
		} else {
			one.lost++;
			one.playofflose++;
			two.playoffwin++;
			two.win++;
		}
	}

	void advance() {
		if (inheritance == 1)
			nextmatch.one = whowin();
		else
			nextmatch.two = whowin();
		nextmatch.fillStack();
		onlyonce = true;
		if (loserblock != null) {
			if (inheritance == 1)
				loserblock.one = wholose();
			else
				loserblock.two = wholose();
			loserblock.fillStack();
		}
	}

	boolean getonlyonce() {
		return onlyonce;
	}

	public PlayoffBlock getLoserblock() {
		return loserblock;
	}

	void setLoserblock(PlayoffBlock e) {
		this.loserblock = e;
	}
}
