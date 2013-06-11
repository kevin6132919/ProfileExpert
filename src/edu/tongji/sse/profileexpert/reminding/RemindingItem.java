package edu.tongji.sse.profileexpert.reminding;

public class RemindingItem implements Comparable<RemindingItem>
{
	private long startTime = -1;
	private long endTime = -1;
	private String title = null;
	private String content = null;
	private long profileId = -1;
	private boolean happened = false;
	private boolean reminded = false;

	public RemindingItem(long startTime,long endTime,String title,String content, long profileId)
	{
		this.startTime = startTime;
		this.endTime = endTime;
		this.title = title;
		this.content = content;
		this.profileId = profileId;
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
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}

	public long getProfileId() {
		return profileId;
	}

	public void setProfileId(long profileId) {
		this.profileId = profileId;
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
}
