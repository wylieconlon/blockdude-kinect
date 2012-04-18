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
	private boolean done = false;

	// timer to trigger movement while user is facing one direction
	boolean movingRight = false;
	boolean movingLeft = false;
	private Timer timer;
	int timerSpeed = 600;


	public GameRunner() {
		this.width  = 640;
		this.height = 480;

		timer = new Timer(timerSpeed, this);
		
		createLevels();
	}

	private void createLevels() {
		setBackground(Color.BLACK);

		// get all files from "levels" directory with .lvl extension
		// create and parse a new level for each
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
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
								RenderingHints.VALUE_ANTIALIAS_ON);

		if(!done) {
			levels[level].draw(g2d);
		} else {
			Font font = new Font("SansSerif", Font.BOLD, 32);
			FontMetrics fm = g2d.getFontMetrics(font);
			
			g2d.setFont(font);
			g2d.setPaint(Color.WHITE);

			String title = "Congratulations!";
			String subtitle = "You've beaten BlockDude.";
			int titleW = fm.stringWidth(title);
			int subtitleW = fm.stringWidth(subtitle);
			g2d.drawString(title, this.width/2 - titleW/2, this.height/2 - 50);
			g2d.drawString(subtitle, this.width/2 - subtitleW/2, this.height/2);

		}
	}
	public Dimension getPreferredSize() {
		return new Dimension(this.width, this.height);
	}


	public void nextLevel() {
		if(!done) {
			if(level < levels.length-1) {
				level++;
				timer.stop();
				repaint();
			} else {
				done = true;
				timer.stop();
				movingLeft = false;
				movingRight = false;
				repaint();
			}
		}
	}
	
	// called whenever a gesture is detected
	public void pose(int userID, GestureName gest, boolean isActivated) {
		if (!done && levels[level] != null && levels[level].checkWin()) {
			nextLevel();
			return;
		} else if(!done) {
			if (isActivated) {
				if(gest.equals(GestureName.TURN_LEFT)) { // MOVE LEFT
					System.out.println("Moving left");
					levels[level].moveLeft();
					if(levels[level].checkWin()) {
						nextLevel();
					} else {
						movingLeft = true;
						repaint();
						timer.restart();
					}
				} else if(gest.equals(GestureName.TURN_RIGHT)) { // MOVE RIGHT
					System.out.println("Moving right");
					levels[level].moveRight();
					if(levels[level].checkWin()) {
						nextLevel();
					} else {
						movingRight = true;
						repaint();
						timer.restart();
					}
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
			}
		}
	}
	
	// timer callback, used to continually move while user is turned to side
	public void actionPerformed(ActionEvent e) {
		if(!done && levels[level].checkWin()) {
			nextLevel();
			return;
		}

		if( !done ) {
			if( movingRight ) {
				levels[level].moveRight();
			} else if( movingLeft ) {
				levels[level].moveLeft();
			}

			repaint();
		}
	}

}
