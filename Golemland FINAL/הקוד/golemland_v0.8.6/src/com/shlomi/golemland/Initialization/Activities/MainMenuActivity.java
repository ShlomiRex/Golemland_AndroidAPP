package com.shlomi.golemland.Initialization.Activities;

import com.shlomi.golemland.R;
import com.shlomi.golemland.Initialization.Settings;
import com.shlomi.golemland.Initialization.SoundSystem;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainMenuActivity extends Activity implements Settings {

	//Static variable so other classes can access it.
	private static SoundSystem soundSystem;
	private final String LOG_TAG = MainMenuActivity.class.getSimpleName();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//Create new sound system.
		soundSystem = new SoundSystem(getApplicationContext());
	}// onCreate

	/**
	 * Called when user clicks on levels.
	 * @param v
	 */
	public void onClickLevels(View v) {
		soundSystem.playButtonPush();

		Intent intent = new Intent(this, LevelsMenuAcitivty.class);
		startActivity(intent);
	}// onClickLevels

	/**
	 * Called when user clicks on credits.
	 * @param v
	 */
	public void onClickCredits(View v) {
		soundSystem.playButtonPush();
		// Create new builder
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("Credits");

		// Create multi choise menu
		builder.setMessage("Shlomi Domnenko\nCreated in Java\n\nThank you for playing my game :)");

		builder.setPositiveButton("Ok", null);

		// Show dialog
		builder.create().show();
	}// onClickCredits

	/**
	 * Return the sound system, so other classes can play music.
	 * @return
	 */
	public static SoundSystem getSoundSystem() {
		return soundSystem;
	}
	
	/**
	 * Called when the main activity done loading. It sets the "Cakes eaten" text to the highest score
	 * that is stored in memory.
	 */
	private void setHighestScoreText() {
		SharedPreferences pref = getSharedPreferences(Settings.SharedPreferncesSettings.SHARED_PREFERENCES_NAME, 0);
		int highestScore = pref.getInt(Settings.SharedPreferncesSettings.SHARED_PREFERENCES_KEY_SCORE, 0);
		
		Log.d(LOG_TAG,"Highest score: " + highestScore);
		
		TextView scoreText = (TextView)(findViewById(R.id.highScoreTextScore));
		scoreText.setText(""+highestScore);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.d(LOG_TAG,"onResume()");
		setHighestScoreText();
		soundSystem.playMusic();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		Log.d(LOG_TAG,"onPause()");
		soundSystem.stopMusic();
	}
}// class

