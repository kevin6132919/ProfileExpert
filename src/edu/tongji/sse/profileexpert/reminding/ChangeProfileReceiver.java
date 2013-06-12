package edu.tongji.sse.profileexpert.reminding;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import edu.tongji.sse.profileexpert.main.MainActivity;
import edu.tongji.sse.profileexpert.util.NotificationUtil;
import edu.tongji.sse.profileexpert.util.ProfileUtil;

public class ChangeProfileReceiver extends BroadcastReceiver
{
	public void onReceive(Context context, Intent intent)
	{
		NotificationUtil.sendNotify(context, "模式切换",	"模式已切换", Notification.DEFAULT_LIGHTS);
		int open_type = intent.getIntExtra(AlarmItem.OPEN_TYPE_KEY, -1);
		if(open_type == -1)
			return;
		
		if(open_type == AlarmItem.OPEN_TYPE_BEGIN)
		{
			ProfileUtil.switchToSilent(context);
		}
		else
		{
			ProfileUtil.switchBack(context);
		}
		
		MainActivity.rm.changeModeHappened();
	}
}
