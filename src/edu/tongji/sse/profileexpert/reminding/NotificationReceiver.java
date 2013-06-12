package edu.tongji.sse.profileexpert.reminding;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import edu.tongji.sse.profileexpert.main.MainActivity;
import edu.tongji.sse.profileexpert.util.NotificationUtil;

public class NotificationReceiver extends BroadcastReceiver
{
	public void onReceive(Context context, Intent intent)
	{
		int open_type = intent.getIntExtra(AlarmItem.OPEN_TYPE_KEY, -1);
		if(open_type == -1)
			return;
		
		//根据事项是否发生定制通知内容
		String content = "";
		if(open_type == AlarmItem.OPEN_TYPE_BEGIN)
		{
			content = "将切换到指定模式,点击进入详情";
		}
		else
		{
			content = "将切换回到原模式,点击进入详情";
		}
		
		//发出通知
		NotificationUtil.sendNotify(context, "模式切换预告", content,
				Notification.DEFAULT_VIBRATE);
		
		MainActivity.rm.notificationHappened();
	}
}


