package edu.tongji.sse.profileexpert.util;

import android.content.Context;
import android.media.AudioManager;

public class ProfileUtil
{
	private static AudioManager audio = null;

	@SuppressWarnings("deprecation")
	public static void switchToSilent(Context ctx)
	{
		audio = (AudioManager)ctx.getSystemService(android.content.Context.AUDIO_SERVICE);
		audio.setRingerMode(AudioManager.RINGER_MODE_SILENT);
		audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
				AudioManager.VIBRATE_SETTING_OFF);
		audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
				AudioManager.VIBRATE_SETTING_OFF);
	}

	@SuppressWarnings("deprecation")
	public static void switchBack(Context ctx)
	{
		audio = (AudioManager)ctx.getSystemService(android.content.Context.AUDIO_SERVICE);
		audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
				AudioManager.VIBRATE_SETTING_ON);
		audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
				AudioManager.VIBRATE_SETTING_ON);
	}
}
