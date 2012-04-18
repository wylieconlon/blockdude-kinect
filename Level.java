/** Level
 *  by Wylie Conlon
 *  
 *  Represents a game level, loaded from a file
 **/

import java.io.*;
import java.awt.*;

public class Level {
	int width;
	int height;
	
	int[][] tiles;
	int columns;

	// display parameters
	int tileSize;
	int visibleCols;
	int offset;
	
	// board state
	int scroll = 0;
	int vScroll = 0;

	// player state
	int player = 0;
	int pHeight = 0;
	boolean carrying = false;

	public Level(File file, int width, int height) {
		this.width  = width;
		this.height = height;

		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			// count number of lines using technique from:
			// http://stackoverflow.com/questions/1277880/
			int lines = 0;
			while(reader.readLine() != null) lines++;

			tiles = new int[lines][];

			tileSize = height / lines;
			visibleCols = this.width / tileSize;
			offset = visibleCols / 2;

			// read data into each line using technique modified from:
			// http://stackoverflow.com/questions/5697763/
			
			reader = new BufferedReader(new FileReader(file));
			for(int i=0; i<lines; i++) {
				String line = reader.readLine();

				String[] numstrs = line.split("\\s+"); // split by white space
				int[] nums = new int[numstrs.length];

				if(columns == 0) {
					columns = nums.length;
				}

				for(int j = 0; j < nums.length; j++) {
					int tile = Integer.parseInt(numstrs[j]);
					
					nums[j] = tile;

					if(tile == 7) { // set starting column
						player = j;
						setScroll(j);
					}
				}

				tiles[i] = nums;
			}
		} catch(Exception e) {}
	}

	// calculates number of columns to offset display
	private void setScroll(int coord) {
		if(coord < offset) {
			scroll = 0;
		} else if(coord > columns-offset) {
			scroll = columns - offset*2;
		} else {
			scroll = coord - offset;
		}
	}

	// calculates lowest non-empty tile in a column
	private int lowestObstacle(int col) {
		for(int i=0; i<tiles.length; i++) {
			int el = tiles[i][col];
			if(el != 0 && el != 1 && el != 7) {
				return i;
			}
		}

		return tiles.length;
	}
	private void setPlayerHeight() {
		pHeight = lowestObstacle(player);
	}

	public void draw(Graphics2D g2d) {
		for(int i=0; i<tiles.length; i++) {
			// only iterate over visible columns
			for(int j=scroll; j < scroll + visibleCols; j++) {
				int tile = tiles[i][j];
				
				int offsetX = (j - scroll) * tileSize,
					offsetY = i * tileSize;

				switch(tile) {
					case 0: // AIR
						break;
					case 1: // DOOR
						g2d.setPaint(Color.RED);
						g2d.fillRect(offsetX, offsetY, tileSize, tileSize);
						
						break;
					case 3: // MOVABLE BLOCK
						g2d.setPaint(Color.GRAY);
						g2d.fillRect(offsetX, offsetY, tileSize, tileSize);
						
						break;
					case 7: // INITIAL PLAYER
						break;
					case 8: // GROUND
						g2d.setPaint(Color.WHITE);
						g2d.fillRect(offsetX, offsetY, tileSize, tileSize);
						
						g2d.setPaint(Color.BLACK);
						g2d.drawRect(offsetX, offsetY, tileSize, tileSize);

						break;
					default:
						break;
				}
			}
		}

		drawPlayer(g2d);
	}

	// draw player image at correct screen position
	private void drawPlayer(Graphics2D g2d) {
		setPlayerHeight();

		int offsetX = (player - scroll) * tileSize,
			offsetY = pHeight * tileSize - tileSize*2;

		g2d.setPaint(Color.BLUE);
		g2d.fillRect(offsetX, offsetY, tileSize, tileSize*2);

		if(carrying) {
			g2d.setPaint(Color.GRAY);
			g2d.fillRect(offsetX, offsetY - tileSize, tileSize, tileSize);
		}
	}

	// movement methods called by GameRunner when it gets skeleton position
	public void moveRight() {
		if(player < columns-1) {
			// check if player is able to move right one space
			// by hypothetically moving player, then resetting if wrong

			int prevHeight = pHeight;
			
			player++;
			setPlayerHeight();
			
			// if there's a wall at new position, player will jump
			if(prevHeight - pHeight > 1) {
				System.out.println("Can't go there, would jump up "+(pHeight-prevHeight));
				pHeight = prevHeight;
				player--; // reset
			}

			setScroll(player);
		}
	}
	public void moveLeft() {
		if(player > 1) {
			// check if player is able to move left one space
			// by hypothetically moving player, then resetting if wrong

			int prevHeight = pHeight;
			
			player--;
			setPlayerHeight();
			
			// if there's a wall at new position, player will jump
			if(prevHeight - pHeight > 1) {
				System.out.println("Can't go there, would jump up "+(pHeight-prevHeight));
				pHeight = prevHeight;
				player++; // reset
			}

			setScroll(player);
		}
	}

	// game actions called by GameRunner when lifting/placing blocks
	//
	// lifting: can only lift blocks directly next to player
	public void liftBlockRight() {
		System.out.println("Lifting right block");

		if(!carrying && player < columns-1) {
			if(tiles[pHeight-2][player+1] == 3) {
				// found a movable block
				tiles[pHeight-2][player+1] = 0;
				carrying = true;
			} else if(tiles[pHeight-1][player+1] == 3) {
				// found a movable block
				tiles[pHeight-1][player+1] = 0;
				carrying = true;
			}
		}
	}
	public void liftBlockLeft() {
		System.out.println("Lifting left block");
		
		if(!carrying && player > 1) {
			if(tiles[pHeight-2][player-1] == 3) {
				// found a movable block
				tiles[pHeight-2][player-1] = 0;
				carrying = true;
			} else if(tiles[pHeight-1][player-1] == 3) {
				// found a movable block
				tiles[pHeight-1][player-1] = 0;
				carrying = true;
			}
		}
	}

	// placing: can only place if the floor is low enough
	public void placeBlockRight() {
		System.out.println("Placing right block");
		
		if(carrying && player < columns-1) {
			int ground = lowestObstacle(player+1);
			
			// block always goes to lowest possible spot
			if(ground > pHeight-1) {
				tiles[ground-1][player+1] = 3;
				carrying = false;
			}
		}
	}
	public void placeBlockLeft() {
		System.out.println("Placing left block");

		if(carrying && player > 1) {
			int ground = lowestObstacle(player-1);
			
			// block always goes to lowest possible spot
			if(ground > pHeight-1) {
				tiles[ground-1][player-1] = 3;
				carrying = false;
			}
		}
	}
}
