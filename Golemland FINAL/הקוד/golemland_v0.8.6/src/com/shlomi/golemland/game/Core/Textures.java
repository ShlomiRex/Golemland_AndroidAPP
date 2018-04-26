package com.shlomi.golemland.game.Core;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Class that holds all game textures, it must be initialize before use! All
 * classes can access these bitmaps.
 */
public final class Textures {

	/**
	 * This is pink + black texture.
	 */
	public static Bitmap defaultBitmap;
	public static Bitmap wallBitmap;
	public static Bitmap brickWallBitmap;
	public static Bitmap playerBitmap;
	public static Bitmap creeperBitmap;
	public static Bitmap sidewalkBitmap;
	public static Bitmap cakeBitmap;
	public static Bitmap tntBitmap;
	public static Bitmap teleportBitmap;
	public static Bitmap zombieBitmap;
	public static Bitmap spiderBitmap;
	public static Bitmap skeletonBitmap;

	/**
	 * Create a new texture object. Other classes can access this later.
	 * @param assets Assets from project.
	 */
	public Textures(AssetManager assets) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options = null;
		InputStream bitmapImage;
		String img = "";

		try {
			// This is wall.
			img = "Textures/wall.png";
			bitmapImage = assets.open(img);
			wallBitmap = BitmapFactory.decodeStream(bitmapImage, null, options);

			// This is brick.
			img = "Textures/brickWall.png";
			bitmapImage = assets.open(img);
			brickWallBitmap = BitmapFactory.decodeStream(bitmapImage, null,
					options);

			// This is player.
			img = "Textures/player.png";
			bitmapImage = assets.open(img);
			playerBitmap = BitmapFactory.decodeStream(bitmapImage, null,
					options);

			// This is sidewalk.
			img = "Textures/sidewalk.png";
			bitmapImage = assets.open(img);
			sidewalkBitmap = BitmapFactory.decodeStream(bitmapImage, null,
					options);

			// This is objective.
			img = "Textures/objective.png";
			bitmapImage = assets.open(img);
			cakeBitmap = BitmapFactory.decodeStream(bitmapImage, null, options);

			// This is tnt.
			img = "Textures/tnt.png";
			bitmapImage = assets.open(img);
			tntBitmap = BitmapFactory.decodeStream(bitmapImage, null, options);

			// This is creeper.
			img = "Textures/creeper.png";
			bitmapImage = assets.open(img);
			creeperBitmap = BitmapFactory.decodeStream(bitmapImage, null,
					options);

			// This is the teleport tile.
			img = "Textures/teleport.png";
			bitmapImage = assets.open(img);
			teleportBitmap = BitmapFactory.decodeStream(bitmapImage, null,
					options);

			// This is defautlt tile.
			img = "Textures/defaultTile.png";
			bitmapImage = assets.open(img);
			defaultBitmap = BitmapFactory.decodeStream(bitmapImage, null,
					options);

			// This is zombie.
			img = "Textures/zombie.png";
			bitmapImage = assets.open(img);
			zombieBitmap = BitmapFactory.decodeStream(bitmapImage, null,
					options);

			// This is spider.
			img = "Textures/spider.png";
			bitmapImage = assets.open(img);
			spiderBitmap = BitmapFactory.decodeStream(bitmapImage, null,
					options);
			
			// This is skeleton.
			img = "Textures/skeleton.png";
			bitmapImage = assets.open(img);
			skeletonBitmap = BitmapFactory.decodeStream(bitmapImage, null,
					options);

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}// Constructor
}// class
