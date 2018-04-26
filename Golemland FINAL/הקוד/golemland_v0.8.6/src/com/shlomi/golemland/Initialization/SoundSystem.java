package com.shlomi.golemland.Initialization;

import com.shlomi.golemland.R;
import com.shlomi.golemland.Initialization.Settings.SoundSettings;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

/**
 * Public class that others can use, user can access songs / play diffirent sounds.
 * @author Shlomi
 *
 */
public final class SoundSystem extends SoundPool implements SoundSettings {
	/**
	 * ID of sounds.
	 */
	private int player_hit, level_select_button_push, 
	tnt_explode, game_over_sound,player_hit_tnt, power_up;
	/**
	 * Media player is good for long songs - like music, unlike SoundPool that is good for quick sounds.
	 */
	private MediaPlayer backgroundMusicPlayer;
	/**
	 * Volume.
	 */
	private static float VOLUME_L = SOUNDS_VOLUME_MULTIPLIER, VOLUME_R = SOUNDS_VOLUME_MULTIPLIER;
	
	private int STREAM_POWER_UP;
	/**
	 * Create sound system
	 * 
	 * @param context
	 *            Load sound from context
	 */
	public SoundSystem(Context context) {
		super(5, AudioManager.STREAM_MUSIC, 0);
		
		// Initialize id's and load sounds to sound pool
		player_hit = load(context, R.raw.player_hit_monster, 1);
		level_select_button_push = load(context, R.raw.btn_push, 1);
		tnt_explode = load(context, R.raw.explosion, 1);
		game_over_sound = load(context, R.raw.game_over,1);
		player_hit_tnt = load(context,R.raw.player_hit_tnt,1);
		power_up = load(context, R.raw.super_mario_powerup_song,1);
		
		// Initialize background music media player.
		backgroundMusicPlayer = MediaPlayer.create(
				context.getApplicationContext(), R.raw.game_music);
		backgroundMusicPlayer.start();
		backgroundMusicPlayer.setLooping(true);
		backgroundMusicPlayer.setVolume(MUSIC_VOLUME_MULTIPLIER,
				MUSIC_VOLUME_MULTIPLIER);
	}

	/**
	 * Play player hit sound.<br>
	 * Music volume is reduced by half, because it it noicy.
	 */
	public void playPlayerHit() {
		play(player_hit, VOLUME_L / 2, VOLUME_R / 2, 0, 0, 1.0f);
	}
	
	/**
	 * Play player hit sound by tnt
	 */
	public void playPlayerHitTNT() {
		play(player_hit_tnt, VOLUME_L, VOLUME_R, 0, 0, 1.0f);
	}

	/**
	 * Play sound of button when pushed
	 */
	public void playButtonPush() {
		play(level_select_button_push, VOLUME_L, VOLUME_R, 0, 0, 1.0f);
	}

	/**
	 * Play tnt sound when explode
	 */
	public void playTNTExplodeSound() {
		play(tnt_explode, VOLUME_L, VOLUME_R, 0, 0, 1.0f);
	}
	
	/**
	 * Play game over sound music
	 */
	public void playGameOverSound() {
		play(game_over_sound, VOLUME_L, VOLUME_R, 0, 0, 1.0f);
	}
	
	/**
	 * Stop background music
	 */
	public void stopMusic() {
		backgroundMusicPlayer.pause();

	}

	/**
	 * Play the background music.
	 */
	public void playMusic() {
		backgroundMusicPlayer.start();
	}
	
	/**
	 * Play powerup music.
	 */
	public void playPowerupMusic() {
		STREAM_POWER_UP = play(power_up, VOLUME_L, VOLUME_R, 0, -1, 1.0f);
	}
	
	/**
	 * Stop playing powerup music.
	 */
	public void stopPowerupMusic() {
		stop(STREAM_POWER_UP);
	}
	
	

}
