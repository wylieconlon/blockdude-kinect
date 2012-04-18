/** GameRunner
 *  by Wylie Conlon
 *  
 *  Handles all game logic, representation, and display
 **/

import java.awt.*;
import java.io.*;
import javax.swing.*;
import java.awt.event.*;

import org.OpenNI.*;

public class GameRunner extends JPanel implements GesturesWatcher, ActionListener {
	
	private UserGenerator userGen;
	
	private int width;
	private int height;

	private Level[] levels;

	private int level = 0;
	private int score = 0;

	// timer to trigger movement while user is facing one direction
	boolean movingRight = false;
	boolean movingLeft = false;
	private Timer timer;
	int timerSpeed = 500;


	public GameRunner() {
		this.width  = 640;
		this.height = 480;

		timer = new Timer(timerSpeed, this);
		
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
			if(gest.equals(GestureName.TURN_LEFT)) { // MOVE LEFT
				System.out.println("Moving left");
				movingLeft = true;
				levels[level].moveLeft();
				repaint();
				timer.restart();
			} else if(gest.equals(GestureName.TURN_RIGHT)) { // MOVE RIGHT
				System.out.println("Moving right");
				movingRight = true;
				levels[level].moveRight();
				repaint();
				timer.restart();
			} else if(gest.equals(GestureName.RH_LIFT)) { // LIFT RIGHT
				levels[level].liftBlockRight();
				repaint();
			} else if(gest.equals(GestureName.LH_LIFT)) { // LIFT LEFT
				levels[level].liftBlockLeft();
				repaint();
			} else if(gest.equals(GestureName.RH_EXTEND)) { // DROP RIGHT
				levels[level].placeBlockRight();
				repaint();
			} else if(gest.equals(GestureName.LH_EXTEND)) { // DROP LEFT
				levels[level].placeBlockLeft();
				repaint();
			} else {
				//System.out.println(gest + " " + userID + " on");
			}
		} else {
			if(gest.equals(GestureName.TURN_LEFT)) { // MOVE LEFT
				System.out.println("Stopped moving left");
				movingLeft = false;
				timer.stop();
			} else if(gest.equals(GestureName.TURN_RIGHT)) { // MOVE RIGHT
				System.out.println("Stopped moving right");
				movingRight = false;
				timer.stop();
			}
			//System.out.println("						" + gest + " " + userID + " off");
		}
	}
	
	// timer callback, used to continually move while user is turned to side
	public void actionPerformed(ActionEvent e) {
		if( movingRight ) {
			levels[level].moveRight();
		} else if( movingLeft ) {
			levels[level].moveLeft();
		}

		repaint();
	}

}
