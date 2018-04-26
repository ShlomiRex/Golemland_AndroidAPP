package com.shlomi.golemland.game.Core.Entitys;

import com.shlomi.golemland.Initialization.Settings;
import com.shlomi.golemland.game.Core.Textures;
import com.shlomi.golemland.game.Core.Constants.ITileChars;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Tile class is the core of the game. Every map has some tiles. Each tile has vertical and
 * horizontal indexes. It also has X,Y coordinates
 * @author Shlomi
 *
 */
public class Tile implements ITileChars, Settings {
	// X and Y of top-left corner, in pixels.
	/**
	 * X and Y of top - left corner of tile.
	 */
	private int x,y;
	
	/**
	 * Horizontal index is on X axis
	 */
	private int horizontalIndex;
	
	/**
	 * Vertical index is on Y axis
	 */
	private int verticalIndex;
	/**
	 * The texture of the tile.
	 */
	private Bitmap bmp;
	/**
	 * The type of the tile.
	 * @see Tiles enum.
	 */
	private char type;

	/**
	 * Create a new tile object.
	 * 
	 * @param type
	 *            Type of object. (Get from ITileChars)
	 * @param horizontalIndex
	 *            Horizontal index of the tile. (Minimum: 0)
	 * @param verticalIndex
	 *            Vertical index of the tile. (Minimum: 0)
	 * @param tileImage
	 *            Tile bitmap
	 */
	public Tile(char type, int horizontalIndex, int verticalIndex, Bitmap tileImage) {
		super();
		this.x = horizontalIndex * TILE_WIDTH_AND_HEIGHT;
		this.y = verticalIndex * TILE_WIDTH_AND_HEIGHT;
		this.horizontalIndex = horizontalIndex;
		this.verticalIndex = verticalIndex;
		this.type = type;

		bmp = tileImage;
	}// Constructor

	/**
	 * @return Type of tile.
	 */
	public char getType() {
		return type;
	}// getType

	/**
	 * @return Horizontal index of tile location.
	 */
	public int getHorizontalIndex() {
		return horizontalIndex;
	}// getHorizontalIndex


	/**
	 * @return Vertical index of tile location.
	 */
	public int getVerticalIndex() {
		return verticalIndex;
	}// getVerticalIndex

	/**
	 * Sets the horizontal index of tile.
	 */
	public void setHorizontalIndex(int index) {
		this.horizontalIndex = index;
	}// setHorizontalIndex

	/**
	 * Sets the horizontal index of tile.
	 */
	public void setVerticalIndex(int index) {
		this.verticalIndex = index;
	}// setVerticalIndex

	/**
	 * 
	 * @return X of tile location (top-left corner) in pixels
	 */
	public int getX() {
		return x;
	}// getX

	/**
	 * 
	 * @return Y of tile location (top-left corner) in pixels
	 */
	public int getY() {
		return y;
	}// getY

	public void setType(char type) {
		this.type = type;
	}// setType
	
	/**
	 * Set the X of tile.
	 * @param x top left corner of tile.
	 */
	public void setX(int x) {
		this.x = x;
	}//setX
	
	/**
	 * Set the Y of tile.
	 * @param Y top left corner of tile.
	 */
	public void setY(int y) {
		this.y = y;
	}//setY

	/**
	 * Destroy the tile - turn the tile into sidewalk. <br>
	 * Usually called when TNT destroys a brick.
	 */
	public void destroyTile() {
		// This will transform the tile to sidewalk.

		type = CHAR_SIDEWALK;

		bmp = Textures.sidewalkBitmap;
	}// destroyTile

	/**
	 * Draw the tile. (Texture and X,Y of location)
	 * @param canvas
	 */
	public void draw(Canvas canvas) {
		if (bmp != null)
			canvas.drawBitmap(bmp, x, y, null);
	}//draw
	
	/**
	 * 
	 * @return String in format: '(Ver,Hor)'
	 */
	public String getLocationString() {
		return "("+verticalIndex+","+horizontalIndex+")";
	}//getLocationString

	/**
	 * Return string of this tile.<br>
	 * Format: type + getLocationString()
	 */
	@Override
	public String toString() {
		return type + getLocationString();
	}//toString
}//Tile class
