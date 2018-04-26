package com.shlomi.golemland.Initialization.Activities;

import com.shlomi.golemland.R;
import com.shlomi.golemland.Initialization.LevelButtonsAdapter;
import com.shlomi.golemland.game.Core.Constants.GameState;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

/**
 * Select levels activity.
 * @author Shlomi
 *
 */
public class LevelsMenuAcitivty extends Activity {
	private static int CURRENT_LEVEL_NUMBER = 0;
	private final String LOG_TAG = LevelsMenuAcitivty.class.getSimpleName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(LOG_TAG,"onCreate()");
		setContentView(R.layout.levels);
		final String[] items = new String[] { "1", "2", "3", "4", "5", "6" };

		final GridView gridView = (GridView) findViewById(R.id.gridLevels);
		LevelButtonsAdapter gridAdapter = new LevelButtonsAdapter(this, items);
		gridView.setAdapter(gridAdapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				MainMenuActivity.getSoundSystem().playButtonPush();
				loadLevel((String) (gridView.getItemAtPosition(position)));
			}
		});
	}// onCreate

	/**
	 * Load a level and start game activity
	 * @param mapID String representing the map id. ( 1-6 )
	 */
	private void loadLevel(String mapID) {
		// LOAD THE GAME !!! (With selected id)
		int levelToOpen = Integer.parseInt(mapID);
		if (CURRENT_LEVEL_NUMBER == 0)
			CURRENT_LEVEL_NUMBER = levelToOpen;

		System.out.println("LOADING LEVEL: " + levelToOpen);

		// New intent, give it parameter
		Intent intent = new Intent(this,
				com.shlomi.golemland.game.GameContainers.GameActivity.class);
		Bundle b = new Bundle();

		b.putInt("level_id", levelToOpen);
		b.putInt("screen_width", this.getWindow().getDecorView().getWidth());
		b.putInt("screen_height", this.getWindow().getDecorView().getHeight());

		intent.putExtras(b); // Put your id to your next Intent
		Log.d(LOG_TAG,"Starting activity with request code of " + GameState.GAME_REQUEST_CODE);
		startActivityForResult(intent, GameState.GAME_REQUEST_CODE);
	}// loadLevel

	/**
	 * When game activity activity is destroyed, it sends resultCode that tells if the game was over or the user quit.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d(LOG_TAG, "onActivityResult() REQUEST = " + requestCode + " RESULT = " + resultCode);

		int game_state = -1;
		if(data != null)
			game_state = data.getExtras().getInt(GameState.STATE_KEY);
		
		if(game_state == GameState.GAME_OVER){
			Log.d(LOG_TAG,"Game over");
			showGameOverScreen();
		}
		
		if(game_state == GameState.GAME_QUIT){
			Log.d(LOG_TAG,"Game quit");
		}

	}
	
	/**
	 * Show game over screen. (Open new a activity)
	 */
	private void showGameOverScreen() {
		GameOverScreen gos = new GameOverScreen();
		Intent intent = new Intent(this,GameOverScreen.class);
		startActivity(intent);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.d(LOG_TAG,"onResume()");
		MainMenuActivity.getSoundSystem().playMusic();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		Log.d(LOG_TAG,"onPause()");
		MainMenuActivity.getSoundSystem().stopMusic();
	}
}// class
