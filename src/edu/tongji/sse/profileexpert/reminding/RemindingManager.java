package edu.tongji.sse.profileexpert.reminding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.widget.Toast;
import edu.tongji.sse.profileexpert.R;
import edu.tongji.sse.profileexpert.entity.MyRoutine;
import edu.tongji.sse.profileexpert.main.MainActivity;
import edu.tongji.sse.profileexpert.provider.RoutineTable;
import edu.tongji.sse.profileexpert.provider.TempMatterTable;
import edu.tongji.sse.profileexpert.util.AlarmUtil;

public class RemindingManager
{
	private Context ctx = null;
	private ContentResolver contentResolver = null;
	private List<RemindingItem> itemList = null;
	private static RemindingItem currentItem = null;
	private Calendar c = Calendar.getInstance();
	//private static RemindingItem nextItem = null;
	
	public RemindingManager(Context context, ContentResolver contentResolver)
	{
		this.ctx = context;
		this.contentResolver = contentResolver;
		itemList = new ArrayList<RemindingItem>();
	}
	
	public void startReminding()
	{
		Toast.makeText(
				ctx,
				ctx.getString(R.string.start_changing),
				Toast.LENGTH_SHORT).show();
		itemList.clear();
		getLatestItems();
		if(currentItem != null)
		{
			checkNow();
			setAlarm();
		}
	}
	
	private void setAlarm()
	{
		if(currentItem == null)
			return;
		
		setNotificationAlarm();
		setProfileChangeAlarm();
	}

	private void setProfileChangeAlarm()
	{
		if(!currentItem.isHappened())
			c.setTimeInMillis(currentItem.getStartTime());
		else
			c.setTimeInMillis(currentItem.getEndTime());
		
		
		AlarmUtil.alarm(ctx, AlarmUtil.CHANGE_MODE, c.getTimeInMillis());
	}

	private void setNotificationAlarm()
	{
		if(!MainActivity.preference.getBoolean("reminding_enable", false))
			return;
		
		int advanced_time = MainActivity.preference.getInt("first_reminding_time", 300);
		
		if(!currentItem.isHappened())
			c.setTimeInMillis(currentItem.getStartTime());
		else
			c.setTimeInMillis(currentItem.getEndTime());
		
		c.add(Calendar.MINUTE, -advanced_time);
		
		AlarmUtil.alarm(ctx, AlarmUtil.NOTIFICATION, c.getTimeInMillis());
	}

	private void checkNow()
	{
		if(currentItem.getStartTime()<System.currentTimeMillis())
			showChangeNowConfirmDialog();
	}

	private void showChangeNowConfirmDialog()
	{
		String msg = ctx.getString(R.string.change_now_confirm_text1) + ":" + ","
				+ ctx.getString(R.string.change_now_confirm_text2) + ":" + ","
				+ ctx.getString(R.string.change_now_confirm_text3) + ".";
		
		new Builder(ctx)
		.setIcon(R.drawable.alerts_warning)
		.setMessage(msg)
		.setTitle(ctx.getString(R.string.change_now_confirm_tips))
		.setPositiveButton(ctx.getString(R.string.yes),
			new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which)
				{
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			})
		.setNegativeButton(ctx.getString(R.string.no), 
			new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which)
				{
					// TODO Auto-generated method stub
					itemList.remove(0);
					setAlarm();
					dialog.dismiss();
				}
		})
		.show();
	}

	private void getLatestItems()
	{
		getLatestTempMatters();
		
		getLatestRoutines();
		
		Collections.sort(itemList);
		
		if(itemList.size()>0)
			currentItem = itemList.get(0);
		
		/*if(itemList.size()>1)
			nextItem = itemList.get(1);*/
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
		Toast.makeText(
				ctx,
				ctx.getString(R.string.stop_changing),
				Toast.LENGTH_SHORT).show();
	}
	
	public void rearrange()
	{
		
	}

	public RemindingItem getCurrentItem()
	{
		return currentItem;
	}

	public static void notificationHappened()
	{
		if(!currentItem.isReminded())
			;
		else
			currentItem.remind();
	}

	public static void changeModeHappened()
	{
		// TODO Auto-generated method stub
		if(!currentItem.isHappened())
			;
		else
			currentItem.happen();
	}
}
