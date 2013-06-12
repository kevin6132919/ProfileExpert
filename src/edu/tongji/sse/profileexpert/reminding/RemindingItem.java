package edu.tongji.sse.profileexpert.reminding;

public class RemindingItem implements Comparable<RemindingItem>
{
	private long startTime = -1;
	private long endTime = -1;
	private long id = -1;
	private int type = -1;
	private boolean happened = false;
	private boolean reminded = false;

	public RemindingItem(long startTime,long endTime, long id, int type)
	{
		this.startTime = startTime;
		this.endTime = endTime;
		this.id = id;
		this.type = type;
	}
	
	@Override
	public int compareTo(RemindingItem another)
	{
		return (int)(this.startTime-another.getStartTime());
	}
	
	//getters and setters
	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isHappened() {
		return happened;
	}

	public void happen()
	{
		this.happened = true;
	}
	
	public boolean isReminded() {
		return reminded;
	}

	public void remind()
	{
		this.reminded = true;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
