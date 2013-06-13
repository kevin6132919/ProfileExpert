package edu.tongji.sse.profileexpert.entity;

import java.util.Calendar;

public class MyRoutine implements Comparable<MyRoutine>
{
	private long id = -1;
	private String title = null;
	private int startDay = -1;
	private boolean isSameDay = false;
	private String timeFrom = null;
	private String timeTo = null;
	private String content = null;
	private long profileId = -1;
	
	private long startTime = -1;
	private long endTime = -1;
	
	private Calendar c = Calendar.getInstance();
	
	public MyRoutine(long id, String title, int startDay, boolean isSameDay,
			String timeFrom ,String timeTo ,String content ,long profileId)
	{
		this.id = id;
		this.title = title;
		this.startDay = startDay;
		this.isSameDay = isSameDay;
		this.timeFrom = timeFrom;
		this.timeTo = timeTo;
		this.content = content;
		this.profileId = profileId;
		
		calculateRealTimeForNow();
	}

	//根据现在时间计算日程的真正开始和结束时间
	private void calculateRealTimeForNow()
	{
		c.setTimeInMillis(System.currentTimeMillis());
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 2;
		int dayInterval = calculateDayInterval(dayOfWeek);

		c.setTimeInMillis(System.currentTimeMillis());
		int hour = Integer.parseInt(timeFrom.substring(0,2));
		int minute = Integer.parseInt(timeFrom.substring(3));
		c.add(Calendar.DAY_OF_MONTH, dayInterval);
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, 0);
		startTime = c.getTimeInMillis();
		
		if(!isSameDay)
			c.add(Calendar.DAY_OF_MONTH, 1);
		
		hour = Integer.parseInt(timeTo.substring(0,2));
		minute = Integer.parseInt(timeTo.substring(3));
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, 0);
		endTime = c.getTimeInMillis();
	}

	//计算天数之差
	private int calculateDayInterval(int dayOfWeek)
	{
		c.setTimeInMillis(System.currentTimeMillis());
		int hour = Integer.parseInt(timeTo.substring(0,2));
		int minute = Integer.parseInt(timeTo.substring(3));
		if(dayOfWeek == startDay)
		{
			if((hour == c.get(Calendar.HOUR_OF_DAY) && minute <= c.get(Calendar.MINUTE))
					|| hour < c.get(Calendar.HOUR_OF_DAY))
			{
				return 7;
			}
			else
			{
				return 0;
			}
		}
		else if(startDay == getYesterday(dayOfWeek))
		{
			if(!isSameDay && ((hour > c.get(Calendar.HOUR_OF_DAY))
					|| hour == c.get(Calendar.HOUR_OF_DAY) && minute > c.get(Calendar.MINUTE)))
				return 0;	
			else
				return 6;
		}
		else
		{
			int temp = startDay - dayOfWeek;
			if(temp > 0)
				return temp;
			else
				return temp + 7;
		}
	}

	//得到前一天
	private int getYesterday(int today)
	{
		if(today > 0)
			return today - 1;
		else
			return 6;
	}

	@Override
	public int compareTo(MyRoutine another)
	{
		return (int) (this.getStartTime()-another.getStartTime());
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getStartDay() {
		return startDay;
	}
	public void setStartDay(int startDay) {
		this.startDay = startDay;
	}
	public boolean isSameDay() {
		return isSameDay;
	}
	public void setSameDay(boolean isSameDay) {
		this.isSameDay = isSameDay;
	}
	public String getTimeFrom() {
		return timeFrom;
	}
	public void setTimeFrom(String timeFrom) {
		this.timeFrom = timeFrom;
	}
	public String getTimeTo() {
		return timeTo;
	}
	public void setTimeTo(String timeTo) {
		this.timeTo = timeTo;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

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

	public long getProfileId() {
		return profileId;
	}

	public void setProfileId(long profileId) {
		this.profileId = profileId;
	}
}
