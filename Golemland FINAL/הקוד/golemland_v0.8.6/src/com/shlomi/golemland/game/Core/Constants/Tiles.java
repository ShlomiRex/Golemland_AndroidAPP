package com.shlomi.golemland.game.Core.Constants;

import com.shlomi.golemland.game.Core.Textures;

import android.graphics.Bitmap;

/**
 * Tiles is enum that contains ALL available tiles , entities and monsters, each with char of their own..<br>
 * Important !
 * @author Shlomi
 *
 */
public enum Tiles implements ITileChars {

	SIDEWALK(CHAR_SIDEWALK), 
	TNT(CHAR_TNT), 
	PLAYER(CHAR_PLAYER), 
	WALL(CHAR_WALL), 
	BRICK(CHAR_BRICK), 
	CREEPER(CHAR_CREEPER), 
	CAKE(CHAR_CAKE), 
	TELEPORT(CHAR_TELEPORT), 
	ZOMBIE(CHAR_ZOMBIE), 
	SPIDER(CHAR_SPIDER), 
	SKELETON(CHAR_SKELETON);

	public char type;

	Tiles(char ch) {
		type = ch;
	}

	/**
	 * 
	 * @return Bitmap (texture) of this enum (Tiles)
	 */
	public Bitmap getTexture() {
		//return getTileByType(type).getTexture();
		
		switch (type) {
		case CHAR_BRICK:
			return Textures.brickWallBitmap;
		case CHAR_CAKE:
			return Textures.cakeBitmap;
		case CHAR_CREEPER:
			return Textures.creeperBitmap;
		case CHAR_PLAYER:
			return Textures.playerBitmap;
		case CHAR_SIDEWALK:
			return Textures.sidewalkBitmap;
		case CHAR_TELEPORT:
			return Textures.teleportBitmap;
		case CHAR_TNT:
			return Textures.tntBitmap;
		case CHAR_WALL:
			return Textures.wallBitmap;
		case CHAR_ZOMBIE:
			return Textures.zombieBitmap;
		case CHAR_SPIDER:
			return Textures.spiderBitmap;
		case CHAR_SKELETON:
			return Textures.skeletonBitmap;
		}

		return Textures.defaultBitmap;
		
	}
	
	/**
	 * 
	 * @param c Char of tile
	 * @return Tiles enum, that has this 'c' char as their type.
	 */
	public static Tiles getTileByType(char c) {
		for (Tiles t : values())
			if (c == t.type)
				return t;
		return null;
	}

}