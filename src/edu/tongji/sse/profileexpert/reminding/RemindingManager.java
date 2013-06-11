package edu.tongji.sse.profileexpert.reminding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import edu.tongji.sse.profileexpert.entity.MyRoutine;
import edu.tongji.sse.profileexpert.provider.RoutineTable;
import edu.tongji.sse.profileexpert.provider.TempMatterTable;

public class RemindingManager
{
	private Context ctx = null;
	private ContentResolver contentResolver = null;
	private List<RemindingItem> itemList = null;
	private static RemindingItem currentItem = null;
	private static RemindingItem nextItem = null;
	private Calendar c = Calendar.getInstance();
	
	public RemindingManager(Context context, ContentResolver contentResolver)
	{
		this.ctx = context;
		this.contentResolver = contentResolver;
		itemList = new ArrayList<RemindingItem>();
	}
	
	public void startReminding()
	{
		itemList.clear();
		getLatestItems();
	}
	
	private void getLatestItems()
	{
		getLatestTempMatters();
		
		getLatestRoutines();
		
		Collections.sort(itemList);
		
		if(itemList.size()>0)
			currentItem = itemList.get(0);
	}

	private void getLatestRoutines()
	{
		Cursor cursor = null;
		cursor = contentResolver.query(
				RoutineTable.CONTENT_URI,
				null,
				null,
				null,
				null);
		List<MyRoutine> routineList = new ArrayList<MyRoutine>();
		
		while(cursor.moveToNext())
		{
			long id = cursor.getLong(cursor.getColumnIndex(RoutineTable._ID));
			String title = cursor.getString(cursor.getColumnIndex(RoutineTable.TITLE));
			int startDay = cursor.getInt(cursor.getColumnIndex(RoutineTable.START_DAY));
			boolean isSameDay = "1".equals(cursor.getString(
					cursor.getColumnIndex(RoutineTable.IS_SAME_DAY)));
			String timeFrom = cursor.getString(cursor.getColumnIndex(RoutineTable.TIME_FROM));
			String timeTo = cursor.getString(cursor.getColumnIndex(RoutineTable.TIME_TO));
			String content = cursor.getString(cursor.getColumnIndex(RoutineTable.DESCRIPTION));
			long profileId = cursor.getInt(cursor.getColumnIndex(TempMatterTable.PROFILE_ID));
			
			MyRoutine my = new MyRoutine(id, title, startDay,
					isSameDay, timeFrom, timeTo, content, profileId);
			routineList.add(my);
		}
		Collections.sort(routineList);
		
		for(int i=0;i<routineList.size();i++)
		{
			if(i==2)
				break;
			MyRoutine my = routineList.get(i);
			RemindingItem ri = new RemindingItem(
					my.getStartTime(),
					my.getEndTime(),
					my.getTitle(),
					my.getContent(),
					my.getProfileId());
			itemList.add(ri);
		}
	}

	private void getLatestTempMatters()
	{
		Cursor cursor = null;
		cursor = contentResolver.query(
				TempMatterTable.CONTENT_URI,
				null,
				TempMatterTable.TIME_TO + ">?",
				new String[]{""+System.currentTimeMillis()},
				TempMatterTable.TIME_FROM + " ASC");
		
		int i = 0;
		while(cursor.moveToNext())
		{
			if(i==2)
				break;
			
			long startTime = cursor.getLong(
					cursor.getColumnIndex(TempMatterTable.TIME_FROM));
			
			long endTime = cursor.getLong(
					cursor.getColumnIndex(TempMatterTable.TIME_TO));
			
			String title = cursor.getString(
					cursor.getColumnIndex(TempMatterTable.TITLE));
			
			String content = cursor.getString(
					cursor.getColumnIndex(TempMatterTable.DESCRIPTION));
			
			long profileId = cursor.getInt(
					cursor.getColumnIndex(TempMatterTable.PROFILE_ID));
			
			RemindingItem ri = new RemindingItem(startTime, endTime, title, content ,profileId);
			itemList.add(ri);
			
			i++;
		}
	}

	public void stopReminding()
	{
		
	}
	
	public void rearrange()
	{
		
	}
}
