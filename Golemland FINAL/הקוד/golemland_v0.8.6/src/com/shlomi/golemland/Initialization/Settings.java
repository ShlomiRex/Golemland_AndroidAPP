package com.shlomi.golemland.Initialization;

/**
 * Important interface that holds all the settings for the game, with sub-interfaces settings.
 * @author Shlomi
 *
 */
public abstract interface Settings {

	/**
	 * Swipe left, right up or down radius. (Less = less distance to move finger
	 * to move player)
	 */
	public final int MOVE_RADIUS = 60;

	/**
	 * Super important! Only change this if you change the whole textures to
	 * diffirent resolution.
	 */
	public final int TILE_WIDTH_AND_HEIGHT = 32;

	/**
	 * Scale the texture by factor, only X
	 */
	public final double TEXTURE_X_SCALE = 1.25;

	/**
	 * Scale the texture by factor, only Y
	 */
	public final double TEXTURE_Y_SCALE = 1.25;
	
	/**
	 * Memory settings
	 * @author Shlomi
	 *
	 */
	public abstract interface SharedPreferncesSettings {
		/**
		 * Name of SharedPrefrences that store values.
		 */
		public final String SHARED_PREFERENCES_NAME = "MyShare";
		/**
		 * Key code of the high score.
		 */
		public final String SHARED_PREFERENCES_KEY_SCORE = "HighScore";
	}

	/**
	 * Every tick / update settings
	 * @author Shlomi
	 *
	 */
	public abstract interface TickSettings {
		/**
		 * For every number of ticks, the cake will spawn randomlly based on
		 * this number. <br>
		 * <br>
		 * Greater number = longer spawn<br>
		 * Lower number = shorter spawn<br>
		 * <br>
		 * Note: This number must be above 0 ! If its 0 then the cake will never
		 * despawn, since it will spawn in the next update
		 */
		public final int CAKE_SPAWN_TICKS = 30;

		/**
		 * Every tick monster has change to stay in place. Increase this value
		 * for rare events. Decrease this value for more often waiting. <br>
		 * <br>
		 * WARNING DO NOT PUT THIS VALUE BELOW 0
		 */
		public final int MONSTER_CHANCE_TO_WAIT = 5;

		/**
		 * Amount for ticks for tnt to explode.<br>
		 * <br>
		 * Greater number = longer to explode<br>
		 * Lower number = shorter to explode
		 */
		public final int TNT_TICKS_TO_EXPLODE = 6;

		/**
		 * Ticks to update the game. (Lower = faster)
		 */
		public final int GAME_THREAD_TICKS = 350;

		/**
		 * Ticks to wear off the power up effect
		 */
		public final int EFFECT_POWERUP_TICKS_TO_WEAR_OFF = 30;
	}

	/**
	 * Player settings, like starting health, starting money and ect.
	 * @author Shlomi
	 *
	 */
	public abstract interface PlayerSettings {

		/**
		 * Starting health of player
		 */
		public final int PLAYER_STARTING_HEALTH = 99;

		/**
		 * Amount of damage absorbed by player when hit creeper
		 */
		public final int PLAYER_HIT_DAMAGE = 1;

		/**
		 * Starting money for player. <br>
		 * Must be positive.
		 */
		public final int PLAYER_STARTING_MONEY = 50;
	}

	/**
	 * Settings of mobs.
	 */
	public abstract interface MobSettings {
		/**
		 * Damage when mob got hit by TNT.
		 */
		public final int DAMAGE_TNT = PlayerSettings.PLAYER_HIT_DAMAGE * 2;
		/**
		 * Starting health of any mob.
		 */
		public final int MOB_HEALTH = 4;
	}
	
	/**
	 * Sound settings, like volume 
	 * @author Shlomi
	 *
	 */
	public abstract interface SoundSettings {
		/**
		 * Multiplier music volume, between 0.0f(No sound) to 1.0f(Max volume)
		 */
		public final float MUSIC_VOLUME_MULTIPLIER = 0.3f;

		/**
		 * Multiplier sounds volume, between 0.0f(No sound) to 1.0f(Max volume)
		 */
		public final float SOUNDS_VOLUME_MULTIPLIER = 1f;
	}

	/**
	 * Shop prices.
	 * @author Shlomi
	 *
	 */
	public abstract interface ShopSettings {
		/**
		 * Price (in the shop) for hat
		 */
		public final int PRICE_HAT = 5;
		
		/**
		 * Price (in the shop) for tnt
		 */
		public final int PRICE_TNT = 10;
		
		/**
		 * Price (in the shop) for power up
		 */
		public final int PRICE_POWERUP = 50;
		
		public final int ID_NO_ITEM = -1;
		public final int ID_HAT = 0;
		public final int ID_TNT = 1;
		public final int ID_POWERUP = 2;
		
	}
	
	/**
	 * Inventory status : Put tnt, do nothing, use powerup, cannot place tnt...
	 */	
	public abstract interface InventoryStatus {
		/**
		 * Return this value in Inventory.slotClicked and return the result.
		 */
		public final int DO_NOTHING = -1,PLACE_TNT = 0,USE_POWERUP = 1,CANNOT_PLACE_TNT = 2;
	}

}
