package com.shlomi.golemland.game.GameContainers;

import com.shlomi.golemland.Initialization.Settings;
import com.shlomi.golemland.game.Core.Mobs.Player;
import android.app.Activity;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

/**
 * Game view show to the user the game.
 * @author Shlomi
 *
 */
public class GameView extends View {
	
	public Game game;

	private static float xScale, yScale;
	
	/**
	 * Used to scale the game to fit any device!
	 */
	private float widthOfScreen,heightOfScreen;

	// When the player is initialyzed, get the x and y of the start point.
	private int xStartPlayer, yStartPlayer;
	// Pointers
	public Player player;
	private final String LOG_TAG = GameView.class.getSimpleName();

	/**
	 * New GameView.
	 * @param gameActivity Activity of GameActivity
	 * @param mapToOpenPath Map to open string path like so: 
	 * @param widthOfScreen
	 * @param heightOfScreen
	 */
	public GameView(Activity gameActivity, String mapToOpenPath, int widthOfScreen, int heightOfScreen) {
		super(gameActivity.getApplicationContext());

		game = new Game(gameActivity, mapToOpenPath);
		player = game.player;

		xStartPlayer = player.getX();
		yStartPlayer = player.getY();

		this.widthOfScreen = widthOfScreen;
		this.heightOfScreen = heightOfScreen;

		//Here we initialize the game to fit the device's resolution - super important
		DisplayMetrics dm = new DisplayMetrics();
		gameActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		float density = dm.density;

		Log.d(LOG_TAG, "Density = " + density);
		xScale = density;
		yScale = density;

		xScale = (float) (xScale * Settings.TEXTURE_X_SCALE);
		yScale = (float) (yScale * Settings.TEXTURE_Y_SCALE);
	}// Constructor

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// Save the current state of the canvas
		canvas.save();

		// Translate to make the center of view to player
		canvas.translate(
				widthOfScreen / 2 - xStartPlayer - Settings.TILE_WIDTH_AND_HEIGHT - player.horizontalSteps * Settings.TILE_WIDTH_AND_HEIGHT, 
				heightOfScreen / 2 - yStartPlayer - Settings.TILE_WIDTH_AND_HEIGHT - player.verticalSteps * Settings.TILE_WIDTH_AND_HEIGHT);

		// Scale the view
		canvas.scale(xScale, yScale, player.getX(), player.getY());

		// Draw the game
		game.drawGame(canvas);

		// Restore canvas after changes
		canvas.restore();

		// Repeat drawing
		invalidate();
	}// onDraw

}// class