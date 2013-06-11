package edu.tongji.sse.profileexpert.entity;

public class DrawableRoutine implements Comparable<DrawableRoutine>
{
	public static final int NORMAL = 0;
	public static final int TOP = -1;
	public static final int BOTTOM = -2;
	public static final int START_MINUTES = 360;//6*60
	public static final int END_MINUTES = 1320;//22*60
	public static final int START_DRAW_MINUTES = 380;//6*60+20
	public static final int END_DRAW_MINUTES = 1300;//22*60-20
	public static final int REAL_END_MINUTES = 1440;//24*40
	
	private long id;
	private int start;
	private int end;
	private int status;
	private String showString;
	
	public DrawableRoutine(long id, int start, int end, int status, String showString)
	{
		this.id = id;
		this.start = start;
		this.end = end;
		this.status = status;
		this.showString = showString;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getShowString() {
		return showString;
	}
	public void setShowString(String showString) {
		this.showString = showString;
	}

	@Override
	public int compareTo(DrawableRoutine another)
	{
		if(this.start < another.getStart())
			return -1;
		else if(this.start > another.getStart())
			return 1;
		return 0;
	}
}
