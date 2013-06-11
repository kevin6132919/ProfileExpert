package edu.tongji.sse.profileexpert.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import edu.tongji.sse.profileexpert.reminding.ChangeProfileReceiver;
import edu.tongji.sse.profileexpert.reminding.NotificationReceiver;

public class AlarmUtil
{
	public static final int NOTIFICATION = 0;
	public static final int CHANGE_MODE = 1;
	
	//得到系统的闹钟服务
	private static AlarmManager getAlarmManager(Context ctx)
	{
		return (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
	}
	
	//定时执行
	public static void alarm(Context ctx, int type, long time)
	{
		AlarmManager am = getAlarmManager(ctx);
		Intent i = new Intent(ctx,
				type == NOTIFICATION? NotificationReceiver.class : ChangeProfileReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0, i, 0);
		am.set(AlarmManager.RTC, time, pendingIntent);
	}
}
