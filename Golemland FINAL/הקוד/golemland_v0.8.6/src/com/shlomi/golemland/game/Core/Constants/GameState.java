package com.shlomi.golemland.game.Core.Constants;

/**
 * Game state interface indicates the state of the game. Game quit, or game over.
 * @author Shlomi
 *
 */
public abstract interface GameState {
	/**
	 * ID
	 */
	public final int GAME_OVER = 10;
	/**
	 * ID
	 */
	public final int GAME_QUIT = 11;
	/**
	 * This value MUST NOT BE -1 or 0 or 1.
	 */
	public final int GAME_REQUEST_CODE = 12;
	
	/**
	 * Key of the value of highest cakes eaten
	 */
	public final String STATE_KEY = "GameState";
}
