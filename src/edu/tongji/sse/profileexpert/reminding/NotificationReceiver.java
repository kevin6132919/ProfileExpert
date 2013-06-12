package edu.tongji.sse.profileexpert.reminding;

import java.util.Calendar;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import edu.tongji.sse.profileexpert.R;
import edu.tongji.sse.profileexpert.main.MainActivity;
import edu.tongji.sse.profileexpert.provider.MyProfileTable;
import edu.tongji.sse.profileexpert.provider.RoutineTable;
import edu.tongji.sse.profileexpert.provider.TempMatterTable;
import edu.tongji.sse.profileexpert.util.MyConstant;
import edu.tongji.sse.profileexpert.util.NotificationUtil;

public class NotificationReceiver extends BroadcastReceiver
{
	private Calendar c = Calendar.getInstance();
	private boolean isCurrentItem = true;
	public void onReceive(Context context, Intent intent)
	{
		RemindingItem ri = MainActivity.rm.getCurrentItem();

		if(ri.isReminded())
		{
			int advanced_time = Integer.parseInt(
					MainActivity.preference.getString("first_reminding_time", "3"));

			if(advanced_time != 30)
				advanced_time *= 60;
			c.setTimeInMillis(ri.getEndTime());
			c.add(Calendar.SECOND, -advanced_time);
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);
			
			
			c.setTimeInMillis(System.currentTimeMillis());
			if(c.get(Calendar.HOUR_OF_DAY) != hour
					|| c.get(Calendar.MINUTE) != minute)
			{
				ri = MainActivity.rm.getNextItem();
				isCurrentItem = false;
			}
		}
		
		String reminding_time = MainActivity.preference.getString("first_reminding_time", "3");
		String time = MyConstant.getRemindingTimeText(Integer.parseInt(reminding_time));

		Cursor cursor = getCursor(context, ri.getType(),ri.getId());
		
		String title = null;
		if(cursor.moveToFirst())
		{
			long profileId = cursor.getLong(cursor.getColumnIndex(TempMatterTable.PROFILE_ID));
			title = getProfileTitle(context, profileId);
		}

		//根据事项是否发生定制通知内容
		String content = "";
		if(!ri.isReminded())
		{

			content = "将在" + time + "后切换到模式" + ":" + title
					+ "," + "点击进入详情";
		}
		else
		{
			content = "将在" + time + "切换回到原模式,点击进入详情";
		}

		//发出通知
		NotificationUtil.sendNotify(context, "模式切换预告", content,
				Notification.DEFAULT_VIBRATE);

		MainActivity.rm.notificationHappened(isCurrentItem);
	}

	private Cursor getCursor(Context ctx,int type,long id)
	{
		if(type==AlarmItem.MATTER_TYPE_TEMP_MATTER)
		{
			return ctx.getContentResolver().query(
					TempMatterTable.CONTENT_URI,
					null,
					TempMatterTable._ID + "=?",
					new String[]{""+id},
					null);
		}
		else
		{
			return ctx.getContentResolver().query(
					RoutineTable.CONTENT_URI,
					null,
					RoutineTable._ID + "=?",
					new String[]{""+id},
					null);
		}
	}
	
	//由profile的id得到其标题
	private String getProfileTitle(Context ctx,long profile_id)
	{
		Cursor cursor = ctx.getContentResolver().query(
				MyProfileTable.CONTENT_URI,
				null,
				MyProfileTable._ID + "=?",
				new String[]{""+profile_id},
				null);

		if(!cursor.moveToFirst())
		{
			return ctx.getString(R.string.show_profile_not_exist);
		}
		else return cursor.getString(cursor.getColumnIndex(MyProfileTable.NAME));
	}
}


