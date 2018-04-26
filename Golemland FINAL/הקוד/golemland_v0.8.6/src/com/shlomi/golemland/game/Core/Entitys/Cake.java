package com.shlomi.golemland.game.Core.Entitys;

import android.graphics.Canvas;
import android.util.Log;

import com.shlomi.golemland.game.Core.Textures;
import com.shlomi.golemland.game.Core.Constants.Tiles;

/**
 * Cake is entity on map, that the player can pickup and earn money. The cake is
 * automatically and randomly generated based on location in the map (Text map)
 */
public class Cake extends Tile {

	private final String LOG_TAG = Cake.class.getSimpleName();
	/**
	 * Ticks ramaining untill this cake will spawn
	 */
	private int remainingTicksToSpawn;
	/**
	 * True of this cake is on screen. Else, false;
	 */
	public boolean isSpawned;

	public Cake(int horizontalIndex, int verticalIndex) {
		super(Tiles.CAKE.type, horizontalIndex, verticalIndex, Textures.cakeBitmap);
		remainingTicksToSpawn = TickSettings.CAKE_SPAWN_TICKS;
		isSpawned = true;
	}

	/**
	 * Tick cake will decrease the ticks requiered for a cake to respawn.
	 */
	public void tick() {
		Log.d(LOG_TAG,"Cake tick!");
		if(isSpawned == false) {
			//Cake is not spawned
			
			//Check if can spawn it this tick
			if(remainingTicksToSpawn == 0) {
				isSpawned = true;
				remainingTicksToSpawn = TickSettings.CAKE_SPAWN_TICKS;
				Log.d(LOG_TAG,"Cake ticks to respawn: " + remainingTicksToSpawn);
			}
			
			//Cake will not spawn this turn, and the cake is not spawned, decrease the ticks for spawn!
			else
				remainingTicksToSpawn --;
		}
	}

	/**
	 * Called when player eat this cake.
	 * This method will change the textrue of the cake, and it wont count as 'there is cake on this tile'
	 */
	public void eat() {
		this.isSpawned = false;
	}
	
	/**
	 * Draw cake
	 */
	@Override
	public void draw(Canvas canvas) {
		if(isSpawned)
			super.draw(canvas);
		
		//Else , do nothing! don't draw
	}

}
