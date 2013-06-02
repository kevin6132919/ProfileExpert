package edu.tongji.sse.profileexpert.entity;

import android.os.Parcel;
import android.os.Parcelable;
import edu.tongji.sse.profileexpert.util.MyConstant;

public class MyProfile implements Parcelable
{
	//private Context ctx = null;

	private String name = null;
	private boolean allowChangingVolume = false;
	private int volume = 0;
	private int vibrate_type = MyConstant.VIBRATE_SETTING_UNCHANGED;
	private boolean allowChangingRingtone = false;
	private String ringtone = null;
	private String message_content = null;

	public MyProfile()
	{
	}

	public MyProfile(String name, boolean allowChangingVolume, int volume, int vibrate_type,
			boolean allowChangingRingtone, String ringtone, String message_content)
	{
		this.name = name;
		this.allowChangingVolume = allowChangingVolume;
		this.volume = volume;
		this.vibrate_type = vibrate_type;
		this.allowChangingRingtone = allowChangingRingtone;
		this.ringtone = ringtone;
		this.message_content = message_content;
	}

	//Getters and setters
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isAllowChangingVolume() {
		return allowChangingVolume;
	}
	public void setAllowChangingVolume(boolean allowChangingVolume) {
		this.allowChangingVolume = allowChangingVolume;
	}
	public int getVolume() {
		return volume;
	}
	public void setVolume(int volume) {
		this.volume = volume;
	}
	public int getVibrate_type() {
		return vibrate_type;
	}
	public void setVibrate_type(int vibrate_type) {
		this.vibrate_type = vibrate_type;
	}
	public boolean isAllowChangingRingtone() {
		return allowChangingRingtone;
	}
	public void setAllowChangingRingtone(boolean allowChangingRingtone) {
		this.allowChangingRingtone = allowChangingRingtone;
	}
	public String getRingtone() {
		return ringtone;
	}
	public void setRingtone(String ringtone) {
		this.ringtone = ringtone;
	}
	public String getMessage_content() {
		return message_content;
	}
	public void setMessage_content(String message_content) {
		this.message_content = message_content;
	}

	@Override
	public int describeContents() 
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags)
	{
		out.writeString(name);
		out.writeBooleanArray(new boolean[]{allowChangingVolume});
		out.writeInt(volume);
		out.writeInt(vibrate_type);
		out.writeBooleanArray(new boolean[]{allowChangingRingtone});
		out.writeString(ringtone);
		out.writeString(message_content);
	}

	public static final Parcelable.Creator<MyProfile> CREATOR
	= new Parcelable.Creator<MyProfile>()
	{
		public MyProfile createFromParcel(Parcel in)
		{  
			MyProfile mp = new MyProfile();
			mp.setName(in.readString());
			boolean[] b = new boolean[1];
			in.readBooleanArray(b);
			if(b != null && b.length > 0)
				mp.setAllowChangingVolume(b[0]);
			mp.setVolume(in.readInt());
			mp.setVibrate_type(in.readInt());
			b = new boolean[1];
			in.readBooleanArray(b);
			if(b != null && b.length > 0)
				mp.setAllowChangingRingtone(b[0]);
			mp.setRingtone(in.readString());
			mp.setMessage_content(in.readString());

			return mp;
		}

		public MyProfile[] newArray(int size) {  
			return new MyProfile[size];  
		}
	};

	//为获取string.xml文件里的值,在使用toString方法前必须设置context
	/*public void SetContext(Context context)
	{
		this.ctx = context;
	}*/

	@Override
	public String toString()
	{
		String str = "";

		if(allowChangingVolume)
			str += MyConstant.RINGTONE + ":" + volume + "%,";
		else
			str += MyConstant.RINGTONE + ":" + MyConstant.DONT_CHANGE + ",";

		String vibrate = MyConstant.getVibtareText(vibrate_type);
		
		str += MyConstant.VIBRATE + ":" +
				vibrate.substring(vibrate.length()-2) + ",";
		
		return str;
	}
}
