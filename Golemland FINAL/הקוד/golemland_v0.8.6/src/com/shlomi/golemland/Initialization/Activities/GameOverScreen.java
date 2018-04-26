package com.shlomi.golemland.Initialization.Activities;

import com.shlomi.golemland.R;
import com.shlomi.golemland.Initialization.SoundSystem;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * When game is over, this activity will launch.
 * @author Shlomi
 *
 */
public class GameOverScreen extends Activity {
	
	private SoundSystem ss;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_over);
		
		ss = MainMenuActivity.getSoundSystem();
		ss.stopMusic();
		
		ss.playGameOverSound();
	}

	/**
	 * When clicked okey, exit activity and stop playing "Game Over" music
	 * @param view
	 */
	public void onClickOk(View view) {
		ss.playMusic();
		finish();
		
	}
}