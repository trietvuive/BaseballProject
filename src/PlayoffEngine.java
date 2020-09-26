import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;

public class PlayoffEngine {
	ArrayList<Team> murica, muricabracket = new ArrayList<Team>();
	ArrayList<Team> national, nationalbracket = new ArrayList<Team>();
	ArrayList<Team> finalround = new ArrayList<Team>();
	CGTemplate natchamp, natcon, murchamp, murcon, finalr;
	PlayoffEngine dis = this;
	boolean done = false;
	boolean draftready = false;
	ArrayList<Team> sample = new ArrayList<Team>();
	ArrayList<Team> sortedlottery = new ArrayList<Team>();
	ArrayList<Team> sortedmidlottery = new ArrayList<Team>();

	public PlayoffEngine(ArrayList<Team> mur, ArrayList<Team> nation) {
		murica = mur;
		national = nation;
		print();
	}

	ArrayList<Team> noblank(ArrayList<Team> a) {
		ListIterator<Team> c = a.listIterator();
		while (c.hasNext())
			if (c.next().blank == true)
				c.remove();
		return a;
	}

	ArrayList<Team> bracket(ArrayList<Team> c, ArrayList<Team> b) {
		ArrayList<Team> top = noblank(c);
		ArrayList<Team> bottom = noblank(b);
		ArrayList<Team> a = new ArrayList<Team>();
		for (int i = 0; i < 8; i++) {
			a.add(top.get(i));
		}
		System.out.println(a.size());
		for (int i = 0; i < 7; i++) {
			a.add(bottom.get(bottom.size() - i - 1));
		}
		System.out.println(a.size());
		return a;
	}

	void gui() {
		Graphic e = new Graphic();
		System.out.println(e.screenSize);
		JButton lbl = new JButton("Team 1:");
		lbl.setPreferredSize(new Dimension(25, 25));
		lbl.setLocation(5, 5);
	}

	void activate() {
		natchamp.setVisible(true);
		natcon.setVisible(true);
		murchamp.setVisible(true);
		murcon.setVisible(true);
	}

	void print() {
		nationalbracket = bracket(national, murica);
		muricabracket = bracket(murica, national);
		PlayoffTree muricachampion = new PlayoffTree(muricabracket, 5, true);
		PlayoffTree nationalchampion = new PlayoffTree(nationalbracket, 5, true);
		PlayoffTree nationalconsolation = new PlayoffTree(nationalbracket, 5);
		PlayoffTree muricaconsolation = new PlayoffTree(muricabracket, 5);
		linkem(muricachampion.root, muricaconsolation.root);
		murcon = new CGTemplate("American Consolation Postseason", muricaconsolation.getmap(), true, false,
				muricachampion.root, this, "MurCon");
		murchamp = new CGTemplate("American Championship Postseason", muricachampion.getmap(), false, false,
				muricaconsolation.root, this, "MurChamp");
		murcon.neighbor = murchamp;
		murchamp.neighbor = murcon;
		linkem(nationalchampion.root, nationalconsolation.root);
		natcon = new CGTemplate("National Consolation Postseason", nationalconsolation.getmap(), true, false,
				nationalchampion.root, this, "NatCon");
		natchamp = new CGTemplate("National Championship Postseason", nationalchampion.getmap(), false, false,
				nationalconsolation.root, this, "NatChamp");
		natcon.neighbor = natchamp;
		natchamp.neighbor = natcon;
	}

	void linkem(PlayoffBlock rootofficial, PlayoffBlock rootconsolation) {
		PlayoffBlock official = rootofficial.getNextMatch();
		PlayoffBlock consolation = rootconsolation.getNextMatch();
		while (official != null || consolation != null) {
			official.setLoserblock(consolation);
			official = official.getNext();
			consolation = consolation.getNext();
		}
	}

	void add(ArrayList<Team> e) {
		finalround.addAll(e);
		if (finalround.size() >= 8) {
			new java.util.Timer().schedule(new java.util.TimerTask() {
				@Override
				public void run() {
					natchamp.setVisible(false);
					natcon.setVisible(false);
					murchamp.setVisible(false);
					murcon.setVisible(false);
					ArrayList<Team> lottery1 = murcon.lottery;
					ArrayList<Team> lottery2 = natcon.lottery;
					lottery1.addAll(lottery2);
					ArrayList<Team> midlottery1 = murcon.midlottery;
					ArrayList<Team> midlottery2 = natcon.midlottery;
					midlottery1.addAll(midlottery2);
					Draft x = new Draft();
					sortedlottery = x.printLottery(lottery1);
					sortedmidlottery = x.midlottery(midlottery1);
					ArrayList<Team> finalplz = new ArrayList<Team>();
					for (Team i : finalround) {
						finalplz.add(i);
						finalplz.add(new Team(""));
					}
					PlayoffTree finaltable = new PlayoffTree(finalplz, "LLLL", 5);
					finalr = new CGTemplate("World Series", finaltable.getmap(), false, true, finaltable.root, dis,
							"Final");
					System.out.println("Something");
				}
			}, 3000);

		}
	}

	void dodraft() {
		System.out.println("Drafting...");
		Draft x = new Draft();
		HashMap<String, Team> murcons = murcon.prepdraft;
		HashMap<String, Team> natcons = natcon.prepdraft;
		HashMap<String, Team> murchamps = murchamp.prepdraft;
		HashMap<String, Team> natchamps = natchamp.prepdraft;
		HashMap<String, Team> winner = finalr.prepdraft;
		x.finaldraft(sortedlottery, sortedmidlottery, murcons, murchamps, natcons, natchamps, winner);
	}

}
