import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class PlayoffTree {
	PlayoffBlock root;
	ArrayList<Team> allblock;
	int[] order2 = new int[] { 1, 8, 9, 10, 5, 4, 11, 12, 6, 3, 13, 14, 7, 2,
			15, 16 };
	int[] order = new int[] { 1, 16, 9, 8, 12, 5, 13, 4, 14, 3, 11, 6, 10, 7,
			15, 2 };
	private TreeMap<Integer, PlayoffBlock> mapallteam = new TreeMap<Integer, PlayoffBlock>();
	boolean consolation;
	PlayoffTree(ArrayList<Team> a, int nummatch) {
		allblock = a;
		while (allblock.size() != 7) {
			allblock.remove(0);
		}
		while (allblock.size() != 16) {
			allblock.add(new Team("LOL"));
		}
		addTree(order2, nummatch);
		print(root);
		consolation = true;
	}
	PlayoffTree(ArrayList<Team>a,String b, int nummatch){
		allblock = a;
		addTree(order, nummatch);
		advanceallroot(root);
	}
	PlayoffTree(ArrayList<Team> a, int nummatch, boolean cc) {
		allblock = a;
		allblock.add(new Team(""));
		addTree(order, nummatch);
		print(root);
		universalmap(root);
	}
	void advanceallroot(PlayoffBlock root){
		PlayoffBlock secondroot = root;
		while(secondroot!= null){
			secondroot.advance();
			secondroot = secondroot.getNext();
		}
	}
	TreeMap<Integer, PlayoffBlock> getmap() {
		universalmap(root);
		return mapallteam;
	}
	void universalmap(PlayoffBlock root) {
		PlayoffBlock another = root;
		PlayoffBlock advance = root;
		advance.advance();
		PlayoffBlock ret = root;
		PlayoffBlock manipulate = root.getNextMatch().getNextMatch().getLoserblock();
		int count = 0;
		while (another != null) {
			ret = another;
			while (ret != null) {
				mapallteam.put(count, ret);
				ret = ret.getNext();
				count++;
			}
			another = another.getNextMatch();
		}
		mapallteam.put(count,manipulate);
		consolation = false;
	}

	PlayoffBlock find(int a, int b, int nummatch) {
		return new PlayoffBlock(allblock.get(a - 1), allblock.get(b - 1),
				nummatch, null);
	}
	void print(PlayoffBlock root) {
		PlayoffBlock temp = root;
		while (root != null) {
			temp = root;
			while (temp != null) {
				System.out.print(temp.one.nam + " - " + temp.two.nam + "  "
						+ temp.getInheritance() + " ");
				temp = temp.getNextMatch();
			}
			System.out.println();
			root = root.getNext();
		}
		System.out.println();
	}
	private void addTree(int[] o, int nummatch) {
		for (int i = 0; i < o.length; i += 2) {
			root = add(root, find(o[i], o[i + 1], nummatch));
		}
		root = setNeighbor(root);
		root = setup(root);
	}

	private PlayoffBlock setup(PlayoffBlock root) {
		PlayoffBlock manipulate = root;
		PlayoffBlock header = root;
		PlayoffBlock ret = root;
		int count = 2;
		while (count < 4) {
			root = nextround(root, count);
			root = linkem(root);
			root.setNextMatch(setNeighbor(root.getNextMatch()));
			root = root.getNextMatch();
			count++;
		}
		String x = "Final ";
		PlayoffBlock a = new PlayoffBlock(new Team(x), new Team(x), 5, null);
		root.setNextMatch(a);
		root.getNeighbor().setNextMatch(a);
		root = inherit(header);
		manipulate = manipulate.getNextMatch().getNextMatch();
		PlayoffBlock b = new PlayoffBlock(new Team("Loser"), new Team("Loser"),5,null);
		manipulate.setLoserblock(b);
		manipulate.getNext().setLoserblock(b);
		manipulate = manipulate.getNextMatch();
		return ret;
	}

	private static PlayoffBlock setNeighbor(PlayoffBlock header) {
		PlayoffBlock ret = header;
		while (header != null) {
			header.setneighbor(header.getNext());
			header.getNext().setneighbor(header);
			header = header.getNext().getNext();
		}
		return ret;
	}

	private PlayoffBlock inherit(PlayoffBlock header) {
		PlayoffBlock holder = header;
		PlayoffBlock ret = header;
		boolean inheritance = true;
		int round = 1;
		while (header != null) {
			ret = header;
			while (ret != null) {
				if (inheritance == true)
					ret.setInheritance(1);
				if (inheritance == false)
					ret.setInheritance(2);
				inheritance = !inheritance;
				ret.round = round;
				ret = ret.getNext();
			}
			round++;
			header = header.getNextMatch();
		}
		return holder;
	}

	private static PlayoffBlock nextround(PlayoffBlock header, int i) {
		String x = "Round " + Integer.toString(i);
		PlayoffBlock ret = header;
		int c = 0;
		while (header != null) {
			PlayoffBlock a = new PlayoffBlock(
					new Team(x + Integer.toString(c)), new Team(x
							+ Integer.toString(c)), 5, null);
			header.setNextMatch(a);
			header.getNext().setNextMatch(a);
			header = header.getNext().getNext();
			c++;
		}
		return ret;
	}

	private static PlayoffBlock linkem(PlayoffBlock header) {
		PlayoffBlock ret = header;
		while (header.getNext().getNext() != null
				&& header.getNext().getNext().getNextMatch() != null) {
			header.getNextMatch().setNext(
					header.getNext().getNext().getNextMatch());
			header = header.getNext().getNext();
		}
		return ret;
	}

	private static PlayoffBlock add(PlayoffBlock header, PlayoffBlock a) {
		PlayoffBlock ret = header;
		if (header == null) {
			return a;
		}
		while ((header.getNext() != null)) {
			header = header.getNext();
		}
		header.setNext(a);
		return ret;
	}
}
