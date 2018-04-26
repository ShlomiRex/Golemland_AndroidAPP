package com.shlomi.golemland.game.Core.Mobs;

import android.util.Log;

import com.shlomi.golemland.Initialization.Settings;
import com.shlomi.golemland.Initialization.Activities.MainMenuActivity;
import com.shlomi.golemland.game.Core.Map;
import com.shlomi.golemland.game.Core.Constants.IDirection;
import com.shlomi.golemland.game.Core.Constants.Tiles;
import com.shlomi.golemland.game.Core.Entitys.Tile;

/**
 * Player class, only 1 can be on map.
 * @author Shlomi
 *
 */
public final class Player extends Mob implements IDirection {

	/**
	 * Money of player.
	 */
	public int money;
	/**
	 * Automove = Represent of the player will move automatically or not. (Stopped)
	 */
	public boolean autoMove;
	/**
	 * Steps that the player has taken.<br>
	 * Thats for position the "Camera" to center on the player.
	 */
	public int horizontalSteps, verticalSteps;
	private final String LOG_TAG = Player.class.getSimpleName();

	public Effect effect;
	/**
	 * Create new player.
	 * 
	 * @param horizontalIndex
	 *            is the horizontal index of the player's tile.
	 * @param verticalIndex
	 *            is the vertical index of the player's tile.
	 * @param view
	 *            is the view which the player will be drawn on.
	 * @param textureWidthAndHeight
	 *            is the dimentions of bitmaps. (Width and height must be the
	 *            same)
	 * @param map
	 *            get the map pointer.
	 * @param playerBitmap
	 *            the bitmap of the player.
	 */
	public Player(int horizontalIndex, int verticalIndex, Map map) {
		super(Tiles.PLAYER, horizontalIndex, verticalIndex, map);
		money = PlayerSettings.PLAYER_STARTING_MONEY;
	}// Constructor

	/**
	 * Automove the player. After we swipe to direction move the player untill
	 * he is blocked. This method is called every touch event.
	 * 
	 * @param dir
	 *            The direction to automatically move to.
	 */
	public void autoMoveToDirection(char dir) {
		autoMove = true;
		direction = dir;
	}// enableAutoMove

	/**
	 * Check if tile is a cake. If true, then destroy cake and increase money.
	 * This function does not return anything. It checks and executes commands.
	 * 
	 * @param tile
	 *            to check.
	 */
	private void isCake(Tile tile) {
		if (map.isPlayerOnCake()) {
			Log.d(LOG_TAG,"Player is on cake");
			money += 5;
		}
	}// isCake

	/**
	 * Withdraw money from player.
	 * 
	 * @param amount
	 *            to withdraw
	 */
	public void withdrawMoney(int amount) {
		money -= amount;
	}

	/**
	 * Method called if player can't move (up,down,left,right). Do something here!
	 */
	private void failedToMove() {
		this.direction = DIRECTION_OOPS;
		this.autoMove = false;
		Log.d(LOG_TAG,"Player stopped at: " + this.getLocationString());
	}
	
	@Override
	public boolean moveUp() {
		if (!super.moveUp()) {
			failedToMove();
			return false;
		}

		verticalSteps--;
		isCake(upTile);
		return true;
	}

	@Override
	public boolean moveDown() {
		if (!super.moveDown()) {
			failedToMove();
			return false;
		}

		verticalSteps++;
		isCake(belowTile);
		return true;
	}

	@Override
	public boolean moveRight() {
		if (!super.moveRight()) {
			failedToMove();
			return false;
		}

		horizontalSteps++;
		isCake(rightTile);
		return true;
	}

	@Override
	public boolean moveLeft() {
		if (!super.moveLeft()) {
			failedToMove();
			return false;
		}
		horizontalSteps--;
		isCake(leftTile);
		return true;
	}

	/**
	 * Called when player is damaged by tnt. <br>
	 * Damage is doubled.
	 */
	public void gotHitByTNT() {
		if(effect != null && effect.isOnEffect == true)
			return;
		this.drainHealth(Settings.PlayerSettings.PLAYER_HIT_DAMAGE * 2);
		MainMenuActivity.getSoundSystem().playPlayerHitTNT();
	}
	
	/**
	 * Called when player is damaged by monster.
	 */
	public void gotHitByMonster() {		
		if(effect != null && effect.isOnEffect == true)
			return;
		health -= Settings.PlayerSettings.PLAYER_HIT_DAMAGE;
		MainMenuActivity.getSoundSystem().playPlayerHit();
	}
	
	/**
	 * Class effect represent the "Power up" effect with ticks untill effect is gone.
	 * @author Shlomi
	 *
	 */
	public class Effect {
		/**
		 * Ticks to wear off the effect.
		 */
		public int ticksToWearOff;
		/**
		 * Is on effect?
		 */
		public boolean isOnEffect;

		/**
		 * New effect
		 */
		public Effect() {
			isOnEffect = false;
			this.ticksToWearOff = Settings.TickSettings.EFFECT_POWERUP_TICKS_TO_WEAR_OFF;
		}
		
		/**
		 * Each tick decreases the ticks to wear off time, until it reaches 0 or below, then the effect is off.
		 */
		public void tickEffect() {
			Log.d(LOG_TAG,"Effect tick. Ticks to wear off: " +ticksToWearOff );
			if(ticksToWearOff <= 0) {
				Log.d(LOG_TAG,"Stopping effect");
				stopEffect();
			}
			else if(isOnEffect) {
				ticksToWearOff --;
			}
		}
		
		/**
		 * Stop the effect.
		 */
		public void stopEffect() {
			isOnEffect = false;
			ticksToWearOff = Settings.TickSettings.EFFECT_POWERUP_TICKS_TO_WEAR_OFF;
		}
		
		/**
		 * Start the effect.
		 */
		public void startEffect() {
			isOnEffect = true;
			ticksToWearOff = Settings.TickSettings.EFFECT_POWERUP_TICKS_TO_WEAR_OFF;
		}
	}//Effect class
	
	/**
	 * Set effect as null (meanning no effect)
	 */
	public void wearOffEffect() {
		this.effect = null;
	}

	/**
	 * Set a new effect and start the effect
	 */
	public void startEffect() {
		this.effect = new Effect();
		effect.startEffect();
	}
}// class
