package com.shlomi.golemland.game.Core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import com.shlomi.golemland.Initialization.Settings;
import com.shlomi.golemland.Initialization.Activities.MainMenuActivity;
import com.shlomi.golemland.game.Core.Constants.*;
import com.shlomi.golemland.game.Core.Entitys.*;
import com.shlomi.golemland.game.Core.Mobs.*;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

/**
 * Map class is the core of the game. It calls ticks, updates and do stuff on
 * map.
 * 
 * @author Shlomi
 * 
 */
public final class Map implements ITileChars, Settings {

	/**
	 * All the 'static tiles' of the map, like Brick and Brick wall, every tile that cannot move is in the map.
	 */
	private ArrayList<ArrayList<Tile>> map;
	/**
	 * The player. NOTE: Only 1 is allowed in a map.
	 */
	public Player player;
	/**
	 * Array of monsters.
	 */
	private ArrayList<Monster> monsterArray;
	/**
	 * TNT That is on the map. It can be null .
	 */
	private TNT tntOnMap;
	/**
	 * Array of cakes.
	 */
	private ArrayList<Cake> cakeArray;
	private final String LOG_TAG = Map.class.getSimpleName();
	public boolean isThereTNTOnMap = false;
	/**
	 * Score of this current game.
	 */
	public int highScore = 0;
	/**
	 * New map object.
	 * 
	 * @param gameActivity The game activity.
	 * @param mapToOpenPath Map to open string.
	 */
	public Map(Activity gameActivity, String mapToOpenPath) {
		// Initialize the final class Textures !
		new Textures(gameActivity.getAssets());

		// Initialize simple parametes
		map = new ArrayList<ArrayList<Tile>>();
		monsterArray = new ArrayList<Monster>();
		tntOnMap = null;
		cakeArray = new ArrayList<Cake>();

		// Load line
		BufferedReader reader = null;
		try {
			// LOAD THIS MAP!!
			reader = new BufferedReader(new InputStreamReader(
					gameActivity.getAssets().open(mapToOpenPath)));

			String line;
			int numberOfLine = 0;

			// Now we scan and load each line.
			while ((line = reader.readLine()) != null) {
				// Loop to number of lines.
				map.add(loadLine(line, numberOfLine));
				numberOfLine++;
			}
			// Done scanning
		}// try
		catch (IOException e) {
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}// finally

		// Done initializing the map.

		//Set monster surroundings for the first time
		for(Monster m : monsterArray)
			setMobSurroundings(m);

		//Set player surroundings for the first time
		setMobSurroundings(player);
	}// Constructor

	/**
	 * Check if player is intersects with monster.
	 * @return Tiles enum, represent 'who touched the player'. Return null if no touch!
	 */
	public Tiles isPlayerOnMonster() {
		for (Monster m : monsterArray)
			if (m.on(player.onThisTile)) 
				return m.getMonsterTile();
		
		return null;
	}

	/**
	 * Load a line. Condition: Line is not null.
	 * 
	 * @param line
	 *            String of line to load.
	 * @param numberOfLine
	 *            The number of line to load.
	 * @return Array of tiles. represent 1 horizontal line.
	 */
	private ArrayList<Tile> loadLine(String line, int numberOfLine) {

		if (line == null || line.length() == 0)
			return null;

		ArrayList<Tile> tmpArrayList = new ArrayList<Tile>();

		char ch;
		int horizontalIndex;
		int verticalIndex;

		// Scanning line
		for (int i = 0; i < line.length(); i++) {
			ch = line.charAt(i);

			horizontalIndex = i;
			verticalIndex = numberOfLine;
			
			switch(ch) {
			
			case CHAR_PLAYER:
				player = new Player(horizontalIndex, verticalIndex,this);
				ch = CHAR_SIDEWALK;
				break;
				
			case CHAR_CREEPER:
				monsterArray.add(new Monster(Tiles.CREEPER, horizontalIndex, verticalIndex, this));
				ch = CHAR_SIDEWALK;
				break;
				
			case CHAR_SPIDER:
				monsterArray.add(new Monster(Tiles.SPIDER, horizontalIndex, verticalIndex, this));
				ch = CHAR_SIDEWALK;
				break;
				
			case CHAR_ZOMBIE:
				monsterArray.add(new Monster(Tiles.ZOMBIE, horizontalIndex, verticalIndex, this));
				ch = CHAR_SIDEWALK;
				break;
				
			case CHAR_CAKE:
				cakeArray.add(new Cake(horizontalIndex, verticalIndex));
				ch = CHAR_SIDEWALK;
				break;
				
			case CHAR_SKELETON:
				monsterArray.add(new Monster(Tiles.SKELETON, horizontalIndex, verticalIndex, this));
				ch = CHAR_SIDEWALK;
				break;
				
			}
			
			//Check if char represent a tile, if not then make the texture black and pink
			Bitmap texture;
			if(Tiles.getTileByType(ch) != null)
				texture = Tiles.getTileByType(ch).getTexture();
			else
				texture = Textures.defaultBitmap;
			
			tmpArrayList.add(new Tile(ch, horizontalIndex, verticalIndex, texture));
		}// for
		return tmpArrayList;
	}// loadLine

	/**
	 * Draw the map.
	 * 
	 * @param canvas
	 */
	public void drawMap(Canvas canvas) {
		//Draw all lines.
		for (ArrayList<Tile> lineArray : map)
			drawHorizontalTiles(canvas, lineArray);
		
		//Draw all cakes.
		for(Cake c : cakeArray)
			c.draw(canvas);

		// Draw player.
		player.draw(canvas);

		// Draw monsters.
		for (Monster m : monsterArray)
			m.draw(canvas);

		// Draw tnt on map
		if(tntOnMap != null)
			tntOnMap.draw(canvas);
	}// draw

	/**
	 * Draw single array list of tiles.
	 * 
	 * @param canvas
	 * @param arr
	 *            Array of tiles to draw.
	 */
	private void drawHorizontalTiles(Canvas canvas, ArrayList<Tile> arr) {
		for (int i = 0; i < arr.size(); i++) {
			arr.get(i).draw(canvas);
		}
	}// drawSingleArrayList

	/**
	 * Set Mob's surroundings.
	 * 
	 * @param Mob
	 *            to set surroundings for
	 */
	public void setMobSurroundings(Mob Mob) {
		int creeperHorizontalIndex = Mob.getHorizontalIndex();
		int creeperVerticalIndex = Mob.getVerticalIndex();

		ArrayList<Tile> surroundings = getSurroundingsOfLocation(
				creeperVerticalIndex, creeperHorizontalIndex);

		Tile above = surroundings.get(0);
		Tile below = surroundings.get(1);
		Tile left = surroundings.get(2);
		Tile right = surroundings.get(3);

		Mob.setSurroundings(above, below, left, right);
	}// setmobArrayurroundings

	/**
	 * Tick the map (player,mobArray,tnts...)<br>
	 * This is the main game loop / updater.
	 */
	public void tick() {
		Log.d(LOG_TAG, "Tick!");
		
		//Tick tnt
		if(tntOnMap != null) {
			Log.d(LOG_TAG, tntOnMap + " is ticking!");
			
			// This will tell us if the tnt is exploded.
			if (tntOnMap.tick())
				explodeTNT(tntOnMap);
		}
		
		for(Cake c : cakeArray)
			c.tick();

		// Tick player
		player.tick();

		// Tick mobs
		for (Monster m : monsterArray) {
			if(m.isAlive != false)
				m.tick();
			
			else {
				m = null;
				Log.d(LOG_TAG,"Mob " + m + " is not alive!");
			}
		}

		// Check if player in on monster
		Tiles t = isPlayerOnMonster();
		if (t != null) 
			player.gotHitByMonster();
		
		
	}// tick

	/**
	 * Method is called from gameActiviy. <br>
	 * This method executes and puts TNT on the map. <br>
	 * This is called when the user press on the inventory and selects tnt.
	 * 
	 * @return True if the tnt successfuly added to map. Else, false.
	 */
	public boolean placeTNT() {
		int horizontalIndex = player.getHorizontalIndex();
		int verticalIndex = player.getVerticalIndex();

		TNT tntToPlace = new TNT(horizontalIndex, verticalIndex);

		ArrayList<Tile> surroundings = getSurroundingsOfLocation(verticalIndex, horizontalIndex);

		Tile above = surroundings.get(0);
		Tile below = surroundings.get(1);
		Tile left = surroundings.get(2);
		Tile right = surroundings.get(3);
		Tile onThis = surroundings.get(4);

		tntToPlace.setSurroundings(above, below, right, left,onThis);

		//Give a new refrence to tnt.
		tntOnMap = tntToPlace;
		Log.d("TNT", "Place tnt at " + verticalIndex + " , " + horizontalIndex);
		isThereTNTOnMap = true;
		return true;
	}

	/**
	 * When tnt explodes do this! ( This will be the final method of tnt
	 * lifecycle. )
	 * 
	 * @param tnt TNT to explode.
	 */
	public void explodeTNT(TNT tnt) {
		Log.d(LOG_TAG, tnt + " explodes!");
		ArrayList<Tile> tilesToDestroy = tnt.getExplodedTiles();

		//For each tile in tilesToDestroy check ...
		for (Tile tileToDestroy : tilesToDestroy) {
			tileToDestroy.destroyTile();
			
			//Check player if got hit by tnt.
			if(player.on(tileToDestroy)) {
				Log.d(LOG_TAG,"Player got hit by tnt!");
				player.gotHitByTNT();
			}
			
			//Check each mob if its got hit by a tnt.
			for(Monster m : monsterArray)
				if(m.on(tileToDestroy))
					m.drainHealth(Settings.MobSettings.DAMAGE_TNT);
		}
		
		//Set tnt on map = null because it exploded
		tntOnMap = null;
		//Pointer
		isThereTNTOnMap = false;

		//Play tnt sound
		MainMenuActivity.getSoundSystem().playTNTExplodeSound();
	}

	/**
	 * Return array list that defines 4 tiles near the tile parameter.
	 * 
	 * @return Return array list of surroundings. Array list is: <br><br>
	 *         Up,Down,Left,Right,OnThis;
	 */
	public ArrayList<Tile> getSurroundingsOfLocation(int verticalIndex, int horizontalIndex) {
		Tile above = map.get((verticalIndex - 1)).get((horizontalIndex));
		Tile below = map.get((verticalIndex + 1)).get((horizontalIndex));
		Tile left = map.get((verticalIndex)).get((horizontalIndex - 1));
		Tile right = map.get((verticalIndex)).get((horizontalIndex + 1));
		Tile onThis = map.get(verticalIndex).get(horizontalIndex);

		ArrayList<Tile> surroundings = new ArrayList<Tile>();
		surroundings.add(above);
		surroundings.add(below);
		surroundings.add(left);
		surroundings.add(right);
		surroundings.add(onThis);

		return surroundings;
	}

	/**
	 * Check if player is on cake.
	 * @param  Tile to check if cake.
	 * @return True or false.
	 */
	public boolean isPlayerOnCake() {
		for(Cake c : cakeArray)
			if(player.on(c) && c.isSpawned == true) {
				//Do something before telling player that he ate!
				c.eat();
				
				//Increase cake count
				highScore ++;
				
				//Tell player that he eat cake. This will increase his money.
				return true;
			}
		return false;
	}
}// class