package edu.tongji.sse.profileexpert.entity;


public class MyRingerSetting
{
	int rintone = -1;
	
	int stream_system_volume = -1;
	int stream_music_volume = -1;
	int stream_ring_volume = -1;

	int vibrate_type_notification = -1;
	int vibrate_type_ringer = -1;
	
	public int getRintone() {
		return rintone;
	}

	public void setRintone(int rintone) {
		this.rintone = rintone;
	}

	public int getStream_system_volume() {
		return stream_system_volume;
	}

	public void setStream_system_volume(int stream_system_volume) {
		this.stream_system_volume = stream_system_volume;
	}

	public int getStream_music_volume() {
		return stream_music_volume;
	}

	public void setStream_music_volume(int stream_music_volume) {
		this.stream_music_volume = stream_music_volume;
	}

	public int getStream_ring_volume() {
		return stream_ring_volume;
	}

	public void setStream_ring_volume(int stream_ring_volume) {
		this.stream_ring_volume = stream_ring_volume;
	}

	public int getVibrate_type_notification() {
		return vibrate_type_notification;
	}

	public void setVibrate_type_notification(int vibrate_type_notification) {
		this.vibrate_type_notification = vibrate_type_notification;
	}

	public int getVibrate_type_ringer() {
		return vibrate_type_ringer;
	}

	public void setVibrate_type_ringer(int vibrate_type_ringer) {
		this.vibrate_type_ringer = vibrate_type_ringer;
	}

	public MyRingerSetting()
	{
		
	}
}
