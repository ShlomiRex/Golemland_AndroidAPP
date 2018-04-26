package com.shlomi.golemland.game.Core.Entitys;

import java.util.ArrayList;

import com.shlomi.golemland.Initialization.Settings;
import com.shlomi.golemland.game.Core.Textures;
import com.shlomi.golemland.game.Core.Constants.ITileChars;

/**
 * TNT class is a tile, above Sidewalk tile. TNT Can explode and destroy tiles around itself. 
 * TNT Can damage the player.
 * @author Shlomi
 *
 */
public final class TNT extends Tile implements ITileChars, Settings {

	/**
	 * Surroundings of tnt.
	 */
	public Tile aboveTile, belowTile, rightTile, leftTile,onThis;
	public int tntTicks;
	
	public TNT(int horizontalIndex, int verticalIndex) {
		super(CHAR_TNT, horizontalIndex, verticalIndex, Textures.tntBitmap);
		tntTicks = 0;
	}// Constructor

	/**
	 * Set surroundings of TNT.
	 * @param above
	 * @param below
	 * @param right
	 * @param left
	 * @param onThis
	 */
	public void setSurroundings(Tile above, Tile below, Tile right, Tile left,Tile onThis) {
		this.aboveTile = above;
		this.belowTile = below;
		this.leftTile = left;
		this.rightTile = right;
		this.onThis = onThis;
	}// setSurroundings

	/**
	 * Return tiles in explosion radius.
	 * @return ArrayList of tiles that need to be destroyed.
	 */
	public ArrayList<Tile> getExplodedTiles() {
		ArrayList<Tile> tilesToDestroy = new ArrayList<Tile>();

		if (isDestroyTileAllowed(aboveTile))
			tilesToDestroy.add(aboveTile);

		if (isDestroyTileAllowed(belowTile))
			tilesToDestroy.add(belowTile);

		if (isDestroyTileAllowed(leftTile))
			tilesToDestroy.add(leftTile);

		if (isDestroyTileAllowed(rightTile))
			tilesToDestroy.add(rightTile);
		
		tilesToDestroy.add(onThis);

		return tilesToDestroy;
	}// explode

	/**
	 * Check if tnt can destroy the tile
	 * @param tile Tile to check
	 * @return True, can destroy. False, cannot.
	 */
	private boolean isDestroyTileAllowed(Tile tile) {
		// Not wall, tnt or teleport.
		if (tile.getType() != CHAR_WALL && tile.getType() != CHAR_TNT
				&& tile.getType() != CHAR_TELEPORT)
			return true;
		return false;
	}

	/**
	 * 
	 * @return True if tnt is exploding.
	 */
	public boolean tick() {
		if (tntTicks >= TickSettings.TNT_TICKS_TO_EXPLODE)
			return true;
		else
			tntTicks += 1;
		return false;
	}

	@Override
	public String toString() {
		String result = "TNT ";
		result += super.toString();
		return result;
	}
}// class
