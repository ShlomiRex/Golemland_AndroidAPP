package com.shlomi.golemland.game.GameContainers;

import java.util.ArrayList;
import com.shlomi.golemland.R;
import com.shlomi.golemland.Initialization.Settings;
import com.shlomi.golemland.game.Core.Inventory;
import com.shlomi.golemland.game.Core.Constants.GameState;
import com.shlomi.golemland.game.Core.Constants.IDirection;
import com.shlomi.golemland.game.Core.Mobs.Player;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Game activity handles all touch events and is the container of the game.<br>
 * It initializes the game components.
 * @author Shlomi
 *
 */
public class GameActivity extends Activity implements Settings, IDirection {

	// Touch parameters
	private int x1, x2, y1, y2, dx, dy;

	// Player parameters
	private char direction;
	private boolean isPlayerMoving;

	private final String LOG_TAG = GameActivity.class.getSimpleName();
	private GameView gameView;
	private Dialog shopDialog;
	private Inventory inventory;
	private Bundle bundle;
	private boolean isFirstTime;
	/**
	 * Create activity which will hold the game.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(LOG_TAG, "onCreate()");
		setContentView(R.layout.game);
		this.bundle = getIntent().getExtras();
		isFirstTime = true;
	}// onCreate

	/**
	 * After onCreate() method do this method.
	 */
	@Override
	public void onStart() {
		super.onStart();
		Log.d(LOG_TAG,"onStart()");
		// After activity done creating, its ready for the game!
		
		if(isFirstTime) {
			Log.d(LOG_TAG,"First time. Initializing the game");
			initGame();
			initHUD();
	
			gameView.game.activityStarted(this, inventory);
		}
		else {
			Log.d(LOG_TAG,"Not first time. Resuming game");
		}
	}

	/**
	 * Initialize the game. Initializes game view, game, map and etc...
	 */
	protected void initGame() {
		int levelToOpen = bundle.getInt("level_id");
		int widthOfScreen = this.getWindowManager().getDefaultDisplay()
				.getWidth();
		int heightOfScreen = this.getWindowManager().getDefaultDisplay()
				.getHeight();
		String mapToOpenPath = "Maps/map" + levelToOpen + ".txt";

		RelativeLayout gameLayout = (RelativeLayout) (findViewById(R.id.gameLayout));
		GameView gameView = new GameView(this, mapToOpenPath, widthOfScreen,
				heightOfScreen);
		gameLayout.addView(gameView);
		this.gameView = gameView;

		// Player param
		isPlayerMoving = false;

	}

	/**
	 * Initialize hud (Layout & inventory)
	 */
	protected void initHUD() {
		// Make HUD layout above game layout;
		LayoutInflater inflater = getLayoutInflater();
		addContentView(inflater.inflate(R.layout.hud, null),
				new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
						ViewGroup.LayoutParams.FILL_PARENT));

		// Give to every framelayout of slot a tag; then when clicked on frame
		// layout, get tag;
		FrameLayout slot0 = (FrameLayout) (findViewById(R.id.slot0));
		FrameLayout slot1 = (FrameLayout) (findViewById(R.id.slot1));
		FrameLayout slot2 = (FrameLayout) (findViewById(R.id.slot2));
		FrameLayout slot3 = (FrameLayout) (findViewById(R.id.slot3));
		FrameLayout slot4 = (FrameLayout) (findViewById(R.id.slot4));

		// Set slot tag (orginize)
		slot0.setTag(0);
		slot1.setTag(1);
		slot2.setTag(2);
		slot3.setTag(3);
		slot4.setTag(4);

		// Create F dialog
		shopDialog = initShopDialog().create();

		ArrayList<FrameLayout> slots = new ArrayList<FrameLayout>();
		slots.add(slot0);
		slots.add(slot1);
		slots.add(slot2);
		slots.add(slot3);
		slots.add(slot4);

		inventory = new Inventory(slots);
	}

	/**
	 * When clicked on inventory slot.
	 * 
	 * @param v
	 *            View clicked on.
	 */
	public void onClickSlot(View v) {
		int slotID = (Integer) v.getTag();
		
		int status = inventory.slotClicked(slotID);

		Log.d(LOG_TAG,"onClickSlot() status = " + status);
		
		//Place tnt
		if (status == Settings.InventoryStatus.PLACE_TNT) {
			Log.d(LOG_TAG,"Player press on tnt");
			if (gameView.game.map.isThereTNTOnMap)
				Log.d(LOG_TAG,
						"Player tried to deploy tnt while another is ticking.");
			else {
				gameView.game.map.placeTNT();
				inventory.removeItemFromSlot(slotID);
			}
		}//place tnt
		
		//Use power up
		else if(status == Settings.InventoryStatus.USE_POWERUP) {
			Log.d(LOG_TAG,"Player used powerup! !!!!~~~");
			gameView.game.usePowerup();
			inventory.removeItemFromSlot(slotID);
		}
	}//onClickSlot

	/**
	 * Initialize shop dialog. Item ids(they are constants): 0 = hat 1 = tnt
	 * 
	 * @return Alert dialog builder of shop dialog.
	 */
	protected AlertDialog.Builder initShopDialog() {
		// Build shop dialog
		final Player player = gameView.game.player;
		final String[] items = { 
				"Hat - " + ShopSettings.PRICE_HAT + "$",
				"TNT - " + ShopSettings.PRICE_TNT + "$",
				 "Power up - " + ShopSettings.PRICE_POWERUP + "$"};
		final int[] prices = new int[3];
		
		prices[0] = ShopSettings.PRICE_HAT;
		prices[1] = ShopSettings.PRICE_TNT;
		prices[2] = ShopSettings.PRICE_POWERUP;

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Shop");

		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int itemID) {
				
				//Check if player has more money than the item's price
				if (player.money >= prices[itemID]) {
					//Check if adding was successful
					if (inventory.addItemToInventory(itemID)) {
						//Decrease the money from the player
						player.withdrawMoney(prices[itemID]);
					} else {
						//No room in inventory
						Toast toast = Toast
								.makeText(
										getApplicationContext(),
										"No free slots! :( Please manage your inventory carefuly next time!",
										Toast.LENGTH_LONG);
						toast.show();
					}//else

				}//if
				else {
					Toast toast = Toast.makeText(getApplicationContext(),
							"You have insufficient funds.", Toast.LENGTH_LONG);
					toast.show();
				}
			}
		});//done set items for alert dialog builder

		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						// We dont need to do anything. Exit dialog.
					}
				});
		return builder;
	}// createShopDialog

	/**
	 * When clicked on shop
	 * 
	 * @param v
	 *            View clicked on.
	 */
	public void onClickedShop(View v) {
		Log.d("HUD", "Clicked on shop");
		shopDialog.show();
	}

	/**
	 * Activity manage touch events. Actions: Swipe left,right,up,down to move
	 * the player. Or maybe press the shop. Or maybe press the inventory slots.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {

		case MotionEvent.ACTION_DOWN:
			x1 = (int) event.getX();
			y1 = (int) event.getY();
			break;

		case MotionEvent.ACTION_UP:
			x2 = (int) event.getX();
			y2 = (int) event.getY();

			dx = (x2 - x1);
			dy = (y2 - y1);

			// Now we check direction of movement.
			// Check if Oops!
			if (Math.abs(dx) >= MOVE_RADIUS || Math.abs(dy) >= MOVE_RADIUS) {
				// Left or right
				if (Math.abs(dx) > Math.abs(dy)) {
					if (dx < 0)
						direction = DIRECTION_LEFT;
					else
						direction = DIRECTION_RIGHT;
				}
				// Up or down
				else {
					if (dy < 0)
						direction = DIRECTION_UP;
					else
						direction = DIRECTION_DOWN;
				}
			}// if check direction
		}// switch

		// Move player according to swipe
		if (!isPlayerMoving && direction != DIRECTION_OOPS) {
			// If no moving, and direction NOT Oops!, move automatically.

			isPlayerMoving = true;
			// Auto move player
			gameView.game.player.autoMoveToDirection(direction);
		}// if not moving

		// Check if play needs to stop automoving
		else if (isPlayerMoving && direction == DIRECTION_OOPS) {
			// If moving, and direction Oops!, stop moving automatically.
			isPlayerMoving = false;
			// Stop auto move player.
			gameView.game.player.autoMove = false;
			gameView.game.player.autoMoveToDirection(direction);
		}// else moving

		// Reset direction.
		direction = DIRECTION_OOPS;

		return true;
	}// onTouchEvent

	@Override
	public void onDestroy() {
		Log.d(LOG_TAG, "onDestroy()");
		
		SharedPreferences pref = getSharedPreferences(Settings.SharedPreferncesSettings.SHARED_PREFERENCES_NAME, 0);
		int highestScore = pref.getInt(Settings.SharedPreferncesSettings.SHARED_PREFERENCES_KEY_SCORE, 0);
		int thisGameScore = gameView.game.map.highScore;
		
		Log.d(LOG_TAG,"highestScore: " + highestScore + " , thisGameScore: " + thisGameScore);
		
		if(thisGameScore > highestScore) {
			Editor e = pref.edit();
			e.putInt(Settings.SharedPreferncesSettings.SHARED_PREFERENCES_KEY_SCORE, thisGameScore);
			e.commit();
			
			Log.d(LOG_TAG,"High score is now changed to " + pref.getInt(Settings.SharedPreferncesSettings.SHARED_PREFERENCES_KEY_SCORE, -1));
		}
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed() {
		Log.d(LOG_TAG,"onBackPressed()");
		super.onBackPressed();
	}

	/**
	 * When game is finished we return a result code, if the player quit or the game is over.
	 */
	@Override
	public void finish() {
		Log.d(LOG_TAG, "finish()");
		
		//If it was game over do something
		if(gameView.game.isGameOver == true) {
			Log.d(LOG_TAG,"Game finished. State: Game over");
			Intent i = getIntent();
			i.putExtra(GameState.STATE_KEY, GameState.GAME_OVER);
			
			setResult(GameState.GAME_REQUEST_CODE, i);
		}
		
		//It was probably quit game
		else {
			Log.d(LOG_TAG,"Game finished. State: Game quit");
			Intent i = getIntent();
			i.putExtra(GameState.STATE_KEY, GameState.GAME_QUIT);
			
			setResult(GameState.GAME_REQUEST_CODE, i);
		}
			
		gameView.game.stopGame();
		
		super.finish();
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d(LOG_TAG,"onResume()");
		gameView.game.resume();
	}
	
	@Override
	public void onRestart() {
		super.onRestart();
		Log.d(LOG_TAG,"onRestart()");
		
		isFirstTime = false;
	}
	
	@Override
	public void onPause() {
		super.onPause();
		Log.d(LOG_TAG,"onPause()");
		
		gameView.game.pause();
	}

}// class GameActivity