package com.shlomi.golemland.game.GameContainers;

import java.util.Random;
import com.shlomi.golemland.R;
import com.shlomi.golemland.Initialization.Settings;
import com.shlomi.golemland.Initialization.Activities.MainMenuActivity;
import com.shlomi.golemland.game.Core.Inventory;
import com.shlomi.golemland.game.Core.Map;
import com.shlomi.golemland.game.Core.Mobs.Player;
import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Class game handle the game.
 * @author Shlomi
 *
 */
public class Game implements Runnable, Settings {

	private final String LOG_TAG = Game.class.getSimpleName();
	/**
	 * player,map is a pointer only. isActivityStarted = flag that tells if the
	 * game activity is started or not. gameThread = the main loop which will
	 * tick the game
	 */
	// Pointers
	public Player player;
	
	/**
	 * Map variable handle map stuff.
	 */
	public final Map map;

	private Activity gameActivity;
	private Inventory inventory;

	protected Runnable uiRunnable;
	private boolean running = true;
	
	protected boolean isGameOver = false;
	
	private Random rnd;

	private boolean isBackgroundNeedUpdate = false;
	private boolean isPaused = false;

	/**
	 * Create a new game object.
	 * 
	 * @param gameActivity Activity to which connect.
	 * @param mapToOpenPath String of which map to open. (i.e.
	 */
	public Game(final Activity gameActivity, String mapToOpenPath) {
		this.gameActivity = gameActivity;
		
		rnd = new Random();
		
		map = new Map(gameActivity, mapToOpenPath);
		
		player = map.player;
		player.health = Settings.PlayerSettings.PLAYER_STARTING_HEALTH;

		new Thread(this).start();
		
		/*
		 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		 * This is UI Runnable and NOT the main game loop! Watch out!
		 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		 */
		uiRunnable = new Runnable() {
			
			@Override
			public void run() {
				if(player.effect != null && player.effect.isOnEffect) {
					updateBackgroundColor();
				}
				
				else if(isBackgroundNeedUpdate) {
					setGameBackgroundImageAsNormal();
					isBackgroundNeedUpdate = false;
				}
				
				inventory.tickInventory(gameActivity);
				updateMoneyText();
				updateHealthText();
				updateScoreText();
				
				if (player.health <= 0) {
					running = false;
					isGameOver = true;
				}
			}
		};
	}// Constructor

	/**
	 * Method is called after game activity has been initialized.
	 * It initialized HUD, money text and health text.
	 */
	public void activityStarted(GameActivity gameActivity, Inventory inventory) {
		this.inventory = inventory;
		updateMoneyText();
		updateHealthText();
		updateScoreText();
	}

	/**
	 * Update money text.
	 */
	public void updateMoneyText() {
		final TextView moneyTextView = (TextView) gameActivity.findViewById(R.id.moneyTextView);
		moneyTextView.setText(player.money + "$");
	}

	/**
	 * Update money text.
	 */
	public void updateHealthText() {
		final TextView healthTextView = (TextView) gameActivity.findViewById(R.id.healthTextView);
		healthTextView.setText("" + player.health + " HP");
	}
	
	/**
	 * Update score text.
	 */
	public void updateScoreText() {
		final TextView scoreTextView = (TextView) gameActivity.findViewById(R.id.scoreTextView);
		scoreTextView.setText("" + map.highScore);
	}
	
	/**
	 *	Update game backgorund to random color;
	 */
	public void updateBackgroundColor() {
		final RelativeLayout layout = (RelativeLayout)gameActivity.findViewById(R.id.gameLayout);
		if(layout != null) {
			int color = Color.rgb(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255));
			layout.setBackgroundColor(color);
		}
	}
	
	/**
	 *	Update game backgorund to the normal background.
	 */
	public void setGameBackgroundImageAsNormal() {
		final RelativeLayout layout = (RelativeLayout)gameActivity.findViewById(R.id.gameLayout);
		if(layout != null) 
			layout.setBackgroundResource(R.drawable.game_background);
	}
	/**
	 * Draw game.
	 * 
	 * @param canvas
	 */
	public void drawGame(Canvas canvas) {
		if(player.effect != null && player.effect.isOnEffect)
			updateBackgroundColor();
		map.drawMap(canvas);
	}// drawGame

	/**
	 * Main loop of the game. Handles ticks.
	 */
	@Override
	public void run() {
		//Only when game is pasued we skip this loop!
		while (running) {
			try {
				
				if(isPaused == true)
					continue;
				Thread.sleep(Settings.TickSettings.GAME_THREAD_TICKS);
				
				//If player has effect
				if(player.effect != null) {
					//Is player on effect?
					if(player.effect.isOnEffect) {
						Log.d(LOG_TAG,"Player is on effect! Tick effect");
						//Tick effect in order to decrease the ticks remaining until the effect wears off
						player.effect.tickEffect();
					}
					//The player is NOT on effect meanning this will execure only once, after the effect is gone, 
					//and then we set the player.effect to null!
					else {
						Log.d(LOG_TAG,"Player if not on effect.");
						//Set effect to null
						player.wearOffEffect();
						//Tell that we need to update the backgroudn back to image
						isBackgroundNeedUpdate = true;
						MainMenuActivity.getSoundSystem().stopPowerupMusic();
						MainMenuActivity.getSoundSystem().playMusic();
					}
				}
				
				//Tick map
				map.tick();
				//Tick UI thread
				gameActivity.runOnUiThread(uiRunnable);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		//Done running
		Log.d(LOG_TAG, "Done running!");
		
		gameActivity.finish();
	}// run

	/**
	 * Stop the game. Running = false.
	 */
	public void stopGame() {
		running = false;
	}

	/**
	 * Called when user uses power up
	 */
	public void usePowerup() {
		player.startEffect();
		MainMenuActivity.getSoundSystem().stopMusic();
		MainMenuActivity.getSoundSystem().playPowerupMusic();
	}

	/**
	 * Pause the game
	 */
	public void pause() {
		isPaused = true;
		MainMenuActivity.getSoundSystem().stopMusic();
	}

	/**
	 * Resume the game
	 */
	public void resume() {
		isPaused = false;
		MainMenuActivity.getSoundSystem().playMusic();
	}

}// class
