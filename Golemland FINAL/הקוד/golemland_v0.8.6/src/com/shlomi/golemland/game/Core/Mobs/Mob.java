package com.shlomi.golemland.game.Core.Mobs;

import java.util.Random;
import com.shlomi.golemland.game.Core.Map;
import com.shlomi.golemland.game.Core.Constants.IDirection;
import com.shlomi.golemland.game.Core.Constants.Tiles;
import com.shlomi.golemland.game.Core.Entitys.Tile;

/**
 * Mob is movable entity. <br>
 * Mob object has surroundings, direction and health.<br>
 * All moveable entityes extends this class.
 * 
 * @author Shlomi
 *
 */
public abstract class Mob extends Tile implements IDirection {

	public Tile upTile, belowTile, leftTile, rightTile, onThisTile;
	public char direction;
	private char oppositeDirection;
	protected Map map;
	/**
	 * only when the mob's health is below 1 then this pointer changes to false.<br>
	 * Used to get rid off the mob off the map.
	 */
	public boolean isAlive = true;
	/**
	 * Hit points - Health of mob. <br> Currently used ONLY for Player class
	 * @see Player
	 */
	public int health;

	/**
	 * Create new Mob.
	 * 
	 * @param tile Tiles enum that contains the mob's texture and his type. (For example creeper, zombie, spider).
	 * @param horizontalIndex Horizontal index of location.       
	 * @param verticalIndex Vertical index of location.        
	 * @param map Map pointer.
	 */
	public Mob(Tiles tile, int horizontalIndex, int verticalIndex, Map map) {
		super(tile.type, horizontalIndex, verticalIndex, tile.getTexture());
		direction = DIRECTION_OOPS;
		oppositeDirection = DIRECTION_OOPS;
		this.map = map;
		onThisTile = this;
		health = 100;
	}// Constructor

	/**
	 * Set the Mob's surroundings. That means the tiles that are North, East,
	 * South and West to the Mob's position.
	 * 
	 * @param above
	 *            The tile above to Mob.
	 * @param below
	 *            The tile below to Mob.
	 * @param left
	 *            The tile left to Mob.
	 * @param right
	 *            The tile right to Mob.
	 */
	public void setSurroundings(Tile above, Tile below, Tile left, Tile right) {
		this.upTile = above;
		this.belowTile = below;
		this.leftTile = left;
		this.rightTile = right;
	}// setCreeperSurroundings

	/**
	 * Return if the tile can be walked on(By killer (Mob,player)).
	 * 
	 * @param tile to check.
	 * @return True if can walk on. Else, false.
	 */
	private boolean isTileWalkable(Tile tile) {
		// Checking if player can move to that tile. (Canno't move to a wall or
		// brick wall.
		
		if (tile.getType() == CHAR_WALL || tile.getType() == CHAR_BRICK)
			return false;
		
		return true;
	}// isTileWalkable

	/**
	 * Get the opposite direction of the creeper current direction.
	 * 
	 * @return Opposite direction, according to the Mob's current direction.
	 */
	private char getOppositeDirection() {
		switch (direction) {
		case DIRECTION_UP:
			return DIRECTION_DOWN;
		case DIRECTION_DOWN:
			return DIRECTION_UP;
		case DIRECTION_LEFT:
			return DIRECTION_RIGHT;
		case DIRECTION_RIGHT:
			return DIRECTION_LEFT;
		}

		return DIRECTION_OOPS;
	}// getOppositeDirection

	/**
	 * Get random direction
	 * (DIRECTION_UP/DIRECTION_DOWN/DIRECTION_LEFT/DIRECTION_RIGHT)
	 * 
	 * @return Random direction
	 */
	private char getRandomDirection() {
		String walkableDirectionString = "";
		// Now we calculate how much walkable paths are there.
		// For each walkable path we put it in a string for later use.

		if (isTileWalkable(upTile) && oppositeDirection != DIRECTION_UP)
			walkableDirectionString += DIRECTION_UP;

		if (isTileWalkable(belowTile) && oppositeDirection != DIRECTION_DOWN)
			walkableDirectionString += DIRECTION_DOWN;

		if (isTileWalkable(leftTile) && oppositeDirection != DIRECTION_LEFT)
			walkableDirectionString += DIRECTION_LEFT;

		if (isTileWalkable(rightTile) && oppositeDirection != DIRECTION_RIGHT)
			walkableDirectionString += DIRECTION_RIGHT;

		// If we "stuck" in a corner, and the only way is back, so we go back.
		if (walkableDirectionString.length() == 0)
			return oppositeDirection;
		
		Random rnd = new Random();
		// Now, after we have all the chars of walkable direction, we get random
		// direction from the string, via char.
		return walkableDirectionString.charAt(rnd
				.nextInt(walkableDirectionString.length()));
	}// getRandomDirection

	/**
	 * Move Mob up
	 * 
	 * @return True if move was successful. False if move was a failure.
	 */
	public boolean moveUp() {
		// Return true if the move was successful.
		// Return false if the move was a failure.
		if (!isTileWalkable(upTile))
			return false;

		setVerticalIndex(this.getVerticalIndex() - 1);
		setY(this.getY() - TILE_WIDTH_AND_HEIGHT);
		onThisTile = upTile;

		return true;
	}

	/**
	 * Move Mob down
	 * 
	 * @return True if move was successful. False if move was a failure.
	 */
	public boolean moveDown() {
		// Return true if the move was successful.
		// Return false if the move was a failure.
		if (!isTileWalkable(belowTile))
			return false;

		this.setVerticalIndex(this.getVerticalIndex() + 1);
		this.setY(this.getY() + TILE_WIDTH_AND_HEIGHT);
		onThisTile = belowTile;

		return true;
	}

	/**
	 * Move Mob right
	 * 
	 * @return True if move was successful. False if move was a failure.
	 */
	public boolean moveRight() {
		// Return true if the move was successful.
		// Return false if the move was a failure.
		if (!isTileWalkable(rightTile))
			return false;

		this.setHorizontalIndex(this.getHorizontalIndex() + 1);
		this.setX(this.getX() + TILE_WIDTH_AND_HEIGHT);
		onThisTile = rightTile;

		return true;
	}

	/**
	 * Move Mob left
	 * 
	 * @return True if move was successful. False if move was a failure.
	 */
	public boolean moveLeft() {
		// Return true if the move was successful.
		// Return false if the move was a failure.
		if (!isTileWalkable(leftTile))
			return false;

		this.setHorizontalIndex(this.getHorizontalIndex() - 1);
		this.setX(this.getX() - TILE_WIDTH_AND_HEIGHT);
		onThisTile = leftTile;

		return true;
	}

	/**
	 * Tick the Mob. Tick will move the Mob once, and do calculations, movement,
	 * detection, and so on.
	 */
	public void tick() {
		// Set self surroundings to move.
		map.setMobSurroundings(this);

		// Randomally decide where to move.

		// If this mob is player, don't move randomlly! :O
		if (this instanceof Player == false) {
			direction = getRandomDirection();
			oppositeDirection = getOppositeDirection();
		}

		// We check direction.
		switch (direction) {
		case DIRECTION_UP:
			// up
			moveUp();
			break;

		case DIRECTION_DOWN:
			// down
			moveDown();
			break;

		case DIRECTION_RIGHT:
			// right
			moveRight();
			break;

		case DIRECTION_LEFT:
			// left
			moveLeft();
			break;
		case DIRECTION_OOPS:
			// Oops! Don't move!
			break;
		}// switch
	}// tick

	/**
	 * Check if mob's position and tile position are the same (intersects)<br>
	 * Check if tile is same vertical and horizontal indexes.
	 * 
	 * @param tile
	 *            Tile to check.
	 * @return True if the tile is on this tile. Else, false;
	 */
	public boolean on(Tile tile) {
		if (tile.getVerticalIndex() == getVerticalIndex()
				&& tile.getHorizontalIndex() == getHorizontalIndex())
			return true;
		return false;
	}
	
	/**
	 * Drain health from the mob. Important: Drain is doing minus the amountToDrain and does 
	 * not add amountToDrain to health. <bf>
	 * If you want to add health to the mob, then in the amountToDrain must be below 0.
	 * @param amountToDrain
	 */
	public void drainHealth(int amountToDrain) {
		this.health -= amountToDrain;
	}

}// class
