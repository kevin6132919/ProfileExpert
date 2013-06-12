package edu.tongji.sse.profileexpert.reminding;

import android.os.Parcel;
import android.os.Parcelable;

public class AlarmItem implements Comparable<AlarmItem> , Parcelable
{
	public static final String ALARM_ITEM_KEY = "alarm_item";
	
	public static final int OPEN_TYPE_BEGIN = 100;
	public static final int OPEN_TYPE_END = 200;
	
	public static final int MATTER_TYPE_TEMP_MATTER = 1;
	public static final int MATTER_TYPE_ROUTINE = 2;
	
	public static final String ID_KEY = "_id";
	
	private long time = -1;
	private int open_type = -1;
	private int matter_type = -1;
	private long id = -1;
	
	public AlarmItem(long time, int open_type, int matter_type, long id)
	{
		this.time = time;
		this.open_type = open_type;
		this.matter_type = matter_type;
		this.id = id;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getOpen_type() {
		return open_type;
	}

	public void setOpen_type(int open_type) {
		this.open_type = open_type;
	}

	public int getMatter_type() {
		return matter_type;
	}

	public void setMatter_type(int matter_type) {
		this.matter_type = matter_type;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public int compareTo(AlarmItem another)
	{
		return (int) (this.time - another.getTime());
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags)
	{
		out.writeLong(time);
		out.writeInt(open_type);
		out.writeInt(matter_type);
		out.writeLong(id);
	}
	
	public static final Parcelable.Creator<AlarmItem> CREATOR
		= new Parcelable.Creator<AlarmItem>()
	{
		public AlarmItem createFromParcel(Parcel in)
		{  
			long time = in.readLong();
			int open_type = in.readInt();
			int matter_type = in.readInt();
			long id = in.readLong();

			return new AlarmItem(time, open_type, matter_type, id);
		}

		public AlarmItem[] newArray(int size) {  
			return new AlarmItem[size];  
		}
	};
}
