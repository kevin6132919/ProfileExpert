package edu.tongji.sse.profileexpert.reminding;

public class AlarmItem implements Comparable<AlarmItem>
{
	public static final String OPEN_TYPE_KEY = "open_type";
	public static final int OPEN_TYPE_BEGIN = 1;
	public static final int OPEN_TYPE_END = 2;

	public static final String MATTER_TYPE_KEY = "matter_type";
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
}
