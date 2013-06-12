package edu.tongji.sse.profileexpert.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import edu.tongji.sse.profileexpert.reminding.AlarmItem;
import edu.tongji.sse.profileexpert.reminding.ChangeProfileReceiver;
import edu.tongji.sse.profileexpert.reminding.NotificationReceiver;

public class AlarmUtil
{
	public static final int NOTIFICATION = 0;
	public static final int CHANGE_PROFILE = 1;

	//得到系统的闹钟服务
	private static AlarmManager getAlarmManager(Context ctx)
	{
		return (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
	}

	//定时执行
	public static void alarm(Context ctx, int type, AlarmItem ai)
	{
		AlarmManager am = getAlarmManager(ctx);
		Intent i = new Intent(ctx,
				type == NOTIFICATION? NotificationReceiver.class : ChangeProfileReceiver.class);
		i.putExtra(AlarmItem.OPEN_TYPE_KEY, ai.getOpen_type());
		i.putExtra(AlarmItem.MATTER_TYPE_KEY, ai.getMatter_type());
		i.putExtra(AlarmItem.ID_KEY, ai.getId());
		PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0, i, 0);
		am.set(AlarmManager.RTC, ai.getTime(), pendingIntent);
	}

	//取消定时执行 
	public static void cancelAlarm(Context ctx)
	{
		AlarmManager am = getAlarmManager(ctx);

		Intent i = new Intent(ctx, NotificationReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0, i, 0);
		am.cancel(pendingIntent);

		i = new Intent(ctx, ChangeProfileReceiver.class);
		pendingIntent = PendingIntent.getBroadcast(ctx, 0, i, 0);
		am.cancel(pendingIntent);
	}
}
