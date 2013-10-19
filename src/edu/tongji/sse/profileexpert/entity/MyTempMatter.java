package edu.tongji.sse.profileexpert.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import edu.tongji.sse.profileexpert.provider.TempMatterTable;

@SuppressLint("SimpleDateFormat")
public class MyTempMatter
{
	private String title = null;
	private long timeFrom = -1;
	private long timeTo = -1;
	private String timeFromStr = null;
	private String timeToStr = null;
	private String showString = null;
	private String description = null;
	private long profileId = -1;
	
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	public MyTempMatter(String title, long timeFrom, long timeTo,
			String description, long profileId, String showString)
	{
		this.title = title;
		this.timeFrom = timeFrom;
		this.timeTo = timeTo;
		this.description = description;
		this.profileId = profileId;
		this.showString = showString;
		
		this.timeFromStr = format.format(new Date(timeFrom));
		this.timeToStr = format.format(new Date(timeTo));
	}
	
	public MyTempMatter(String title, String timeFromStr, String timeToStr,
			String description, long profileId, String showString) throws ParseException
	{
		this.title = title;
		this.timeFromStr = timeFromStr;
		this.timeToStr = timeToStr;
		this.description = description;
		this.profileId = profileId;
		this.showString = showString;
		
		this.timeFrom = format.parse(timeFromStr).getTime();
		this.timeTo = format.parse(timeToStr).getTime();
	}
	
	public void save(Context ctx)
	{
		ContentValues values = new ContentValues();
		values.put(TempMatterTable.TITLE, title);
		values.put(TempMatterTable.TIME_FROM, timeFrom);
		values.put(TempMatterTable.TIME_TO, timeTo);
		values.put(TempMatterTable.TIME_FROM_STR, timeFromStr);
		values.put(TempMatterTable.TIME_TO_STR, timeToStr);
		values.put(TempMatterTable.DESCRIPTION, description);
		values.put(TempMatterTable.SHOW_STRING, showString);
		values.put(TempMatterTable.PROFILE_ID, profileId);
		ctx.getContentResolver().insert(TempMatterTable.CONTENT_URI, values);
	}
}
