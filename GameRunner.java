/** GameRunner
 *  by Wylie Conlon
 *  
 *  Handles all game logic, representation, and display
 **/

import java.awt.*;
import java.io.*;
import javax.swing.*;

import org.OpenNI.*;

public class GameRunner extends JPanel implements GesturesWatcher {
	
	private UserGenerator userGen;
	
	private int width;
	private int height;

	private Level[] levels;

	private int level = 0;
	private int score = 0;

	public GameRunner() {
		this.width  = 640;
		this.height = 480;


		
		createLevels();
	}


	private void createLevels() {
		setBackground(Color.BLACK);

		// get all files from "levels" directory with .lvl extension
		File dir = new File("levels");
		File[] files = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".lvl");
			}
		});
		
		levels = new Level[files.length];

		for(int i=0; i<files.length; i++) {
			levels[i] = new Level(files[i], this.width, this.height);
		}
	}
		
	public void setUserGen(UserGenerator userGen) {
		this.userGen = userGen;
	}
	
	public void update(Graphics2D g2d) {
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		levels[level].draw(g2d);
	}
	public Dimension getPreferredSize() {
		return new Dimension(this.width, this.height);
	}

	
	// called whenever a gesture is detected
	public void pose(int userID, GestureName gest, boolean isActivated) {
		if (isActivated) {
			System.out.println(gest + " " + userID + " on");
		} else {
			System.out.println("						" + gest + " " + userID + " off");
		}
	}
}
