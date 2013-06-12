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
import edu.tongji.sse.profileexpert.R;
import edu.tongji.sse.profileexpert.entity.MyRoutine;
import edu.tongji.sse.profileexpert.main.MainActivity;
import edu.tongji.sse.profileexpert.provider.RoutineTable;
import edu.tongji.sse.profileexpert.provider.TempMatterTable;
import edu.tongji.sse.profileexpert.util.AlarmUtil;
import edu.tongji.sse.profileexpert.util.NotificationUtil;

public class RemindingManager
{
	private Context ctx = null;
	private ContentResolver contentResolver = null;
	private List<RemindingItem> itemList = null;
	private static RemindingItem currentItem = null;
	private Calendar c = Calendar.getInstance();
	private List<AlarmItem> notificationList = null;
	private List<AlarmItem> changeProfileList = null;
	private static RemindingItem nextItem = null;
	
	public RemindingManager(Context context, ContentResolver contentResolver)
	{
		this.ctx = context;
		this.contentResolver = contentResolver;
		itemList = new ArrayList<RemindingItem>();
		notificationList = new ArrayList<AlarmItem>();
		changeProfileList = new ArrayList<AlarmItem>();
	}
	
	public void startReminding(Context context)
	{
		if(context == null)
			context = ctx;
		itemList.clear();
		getLatestItems();
		if(currentItem != null)
		{
			checkNow(context);
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
		addItemToChangeProfileList(currentItem);
		addItemToChangeProfileList(nextItem);
		//Collections.sort(changeProfileList);
		
		alarmChangeProfile();
	}

	private void alarmChangeProfile()
	{
		if(changeProfileList.size()>0)
		{
			AlarmUtil.alarm(ctx, AlarmUtil.CHANGE_PROFILE, changeProfileList.get(0));
			changeProfileList.remove(0);
		}
	}

	private void addItemToChangeProfileList(RemindingItem ri)
	{
		if(ri == null)
			return;
		
		AlarmItem ai_start = new AlarmItem(
				ri.getStartTime(),
				AlarmItem.OPEN_TYPE_BEGIN,
				ri.getType(),
				ri.getId());
		changeProfileList.add(ai_start);
		
		AlarmItem ai_end = new AlarmItem(
				ri.getEndTime(),
				AlarmItem.OPEN_TYPE_END,
				ri.getType(),
				ri.getId());
		changeProfileList.add(ai_end);
	}

	private void setNotificationAlarm()
	{
		if(!MainActivity.preference.getBoolean("reminding_enable", false))
			return;
		
		addItemToNotificationList(currentItem);
		addItemToNotificationList(nextItem);
		
		Collections.sort(notificationList);
		
		alarmNofitication();
	}

	private void addItemToNotificationList(RemindingItem ri)
	{
		if(ri == null)
			return;
		
		int advanced_time = Integer.parseInt(
				MainActivity.preference.getString("first_reminding_time", "3"));
		
		if(advanced_time != 30)
		{
			advanced_time *= 60;
		}
		
		c.setTimeInMillis(ri.getStartTime());
		c.add(Calendar.SECOND, -advanced_time);
		AlarmItem ai_start = new AlarmItem(
				c.getTimeInMillis(),
				AlarmItem.OPEN_TYPE_BEGIN,
				ri.getType(),
				ri.getId());
		notificationList.add(ai_start);
		
		c.setTimeInMillis(ri.getEndTime());
		c.add(Calendar.SECOND, -advanced_time);
		AlarmItem ai_end = new AlarmItem(
				c.getTimeInMillis(),
				AlarmItem.OPEN_TYPE_END,
				ri.getType(),
				ri.getId());
		notificationList.add(ai_end);
	}

	private void alarmNofitication()
	{
		if(notificationList.size()>0)
		{
			AlarmUtil.alarm(ctx, AlarmUtil.NOTIFICATION, notificationList.get(0));
			notificationList.remove(0);
		}
	}

	private void checkNow(Context context)
	{
		if(currentItem.getStartTime()<System.currentTimeMillis())
			showChangeNowConfirmDialog(context);
		else
			setAlarm();
	}

	private void showChangeNowConfirmDialog(Context context)
	{
		String msg = ctx.getString(R.string.change_now_confirm_text1) + ":" + ","
				+ ctx.getString(R.string.change_now_confirm_text2) + ":" + ","
				+ ctx.getString(R.string.change_now_confirm_text3) + ".";
		
		new Builder(context)
		.setIcon(R.drawable.alerts_warning)
		.setMessage(msg)
		.setTitle(ctx.getString(R.string.change_now_confirm_tips))
		.setPositiveButton(ctx.getString(R.string.yes),
			new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which)
				{
					setAlarm();
					dialog.dismiss();
				}
			})
		.setNegativeButton(ctx.getString(R.string.no), 
			new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which)
				{
					itemList.remove(0);
					currentItem = null;
					nextItem = null;
					if(itemList.size()>0)
						currentItem = itemList.get(0);
					if(itemList.size()>1)
						nextItem = itemList.get(1);
					setAlarm();
					dialog.dismiss();
				}
		})
		.setCancelable(false)
		.show();
	}

	private void getLatestItems()
	{
		getLatestTempMatters();
		
		getLatestRoutines();
		
		Collections.sort(itemList);
		
		if(itemList.size()>0)
			currentItem = itemList.get(0);
		
		if(itemList.size()>1)
			nextItem = itemList.get(1);
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
					my.getId(),
					AlarmItem.MATTER_TYPE_ROUTINE);
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
			
			long id = cursor.getInt(
					cursor.getColumnIndex(TempMatterTable._ID));
			
			RemindingItem ri = new RemindingItem(startTime, endTime,
					id, AlarmItem.MATTER_TYPE_TEMP_MATTER);
			
			itemList.add(ri);
			
			i++;
		}
	}

	public void stopReminding()
	{
		clear();
		NotificationUtil.cancelAll(ctx);
		AlarmUtil.cancelAlarm(ctx);
	}
	
	private void clear()
	{
		itemList.clear();
		notificationList.clear();
		changeProfileList.clear();
		currentItem = null;
		nextItem = null;
	}

	public void rearrange(Context context)
	{
		this.stopReminding();
		if(MainActivity.preference.getBoolean("arm_status", false))
			this.startReminding(context);
	}

	public RemindingItem getCurrentItem()
	{
		return currentItem;
	}
	
	public RemindingItem getNextItem()
	{
		return nextItem;
	}

	public void notificationHappened(boolean isCurrentItem)
	{
		if(isCurrentItem)
		{
			if(!currentItem.isReminded())
				currentItem.remind();
		}
		else
		{
			if(!nextItem.isReminded())
				nextItem.remind();
		}
		alarmNofitication();
	}

	private void resetNextItem()
	{
		itemList.clear();
		getLatestTempMatters();
		
		getLatestRoutines();
		
		Collections.sort(itemList);
		
		if(currentItem != null || itemList.size()>1 )
		if(currentItem.getId() != itemList.get(0).getId())
			rearrange(null);
		else
			nextItem = itemList.get(1);
		
		addItemToNotificationList(nextItem);
		addItemToChangeProfileList(nextItem);
		
		Collections.sort(notificationList);
		//Collections.sort(changeProfileList);
	}

	public void changeModeHappened()
	{
		if(!currentItem.isHappened())
		{
			currentItem.happen();
		}
		else
		{
			currentItem = nextItem;
			resetNextItem();
		}
		alarmChangeProfile();
	}
}
