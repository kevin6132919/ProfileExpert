package edu.tongji.sse.profileexpert.util;

import edu.tongji.sse.profileexpert.entity.MyRingerSetting;
import edu.tongji.sse.profileexpert.main.MainActivity;
import android.content.Context;
import android.media.AudioManager;

public class ProfileUtil
{
	private static AudioManager audio = null;

	@SuppressWarnings("deprecation")
	public static void switchToProfile(Context ctx ,boolean allowChangingVolume, int volume, int vibrate_type)
	{
		audio = (AudioManager)ctx.getSystemService(android.content.Context.AUDIO_SERVICE);
		if(allowChangingVolume)
		{
			if(volume == 0)
				audio.setRingerMode(AudioManager.RINGER_MODE_SILENT);
			else
			{
				int max = audio.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
				int index = max * volume / 100;
				audio.setStreamVolume(AudioManager.STREAM_SYSTEM, index, 0);
				
				max = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
				index = max * volume / 100;
				audio.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
				
				max = audio.getStreamMaxVolume(AudioManager.STREAM_RING);
				index = max * volume / 100;
				audio.setStreamVolume(AudioManager.STREAM_RING, index, 0);
			}
		}
		switch(vibrate_type)
		{
		case MyConstant.VIBRATE_SETTING_CHANGE_TO_CLOSE:
			audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
					AudioManager.VIBRATE_SETTING_OFF);
			audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
					AudioManager.VIBRATE_SETTING_OFF);
		case MyConstant.VIBRATE_SETTING_CHANGE_TO_OPEN:
			audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
					AudioManager.VIBRATE_SETTING_ON);
			audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
					AudioManager.VIBRATE_SETTING_ON);
		case MyConstant.VIBRATE_SETTING_UNCHANGED:
			break;
		}
	}

	@SuppressWarnings("deprecation")
	public static void switchBack(Context ctx)
	{
		MyRingerSetting mrs = MainActivity.mrs;
		audio = (AudioManager)ctx.getSystemService(android.content.Context.AUDIO_SERVICE);
		audio.setRingerMode(mrs.getRintone());
		
		audio.setStreamVolume(AudioManager.STREAM_SYSTEM,
				mrs.getStream_system_volume(), 0);
		audio.setStreamVolume(AudioManager.STREAM_MUSIC,
				mrs.getStream_music_volume(), 0);
		audio.setStreamVolume(AudioManager.STREAM_RING,
				mrs.getStream_ring_volume(), 0);
		
		audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
				mrs.getVibrate_type_ringer());
		audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
				mrs.getVibrate_type_notification());
	}
	
	@SuppressWarnings("deprecation")
	public static MyRingerSetting getCurrentRingtone(Context ctx)
	{
		audio = (AudioManager)ctx.getSystemService(android.content.Context.AUDIO_SERVICE);
		MyRingerSetting mrs = new MyRingerSetting();
		
		int rintone = audio.getRingerMode();
		
		int stream_system_volume = audio.getStreamVolume(AudioManager.STREAM_SYSTEM);
		int stream_music_volume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
		int stream_ring_volume = audio.getStreamVolume(AudioManager.STREAM_RING);

		int vibrate_type_notification = audio.getVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION);
		int vibrate_type_ringer = audio.getVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER);
		
		mrs.setRintone(rintone);
		
		mrs.setStream_music_volume(stream_music_volume);
		mrs.setStream_ring_volume(stream_ring_volume);
		mrs.setStream_system_volume(stream_system_volume);
		
		mrs.setVibrate_type_notification(vibrate_type_notification);
		mrs.setVibrate_type_ringer(vibrate_type_ringer);
		
		return mrs;
	}
}
