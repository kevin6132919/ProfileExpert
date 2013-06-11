package edu.tongji.sse.profileexpert.reminding;

import edu.tongji.sse.profileexpert.main.MainActivity;
import edu.tongji.sse.profileexpert.util.NotificationUtil;
import edu.tongji.sse.profileexpert.util.ProfileUtil;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ChangeProfileReceiver extends BroadcastReceiver
{
	public void onReceive(Context context, Intent intent)
	{
		Log.v("PrintOut","ChangeModeReceiver start!");
		
		NotificationUtil.sendNotify(context, "模式切换",	"模式已切换", Notification.DEFAULT_LIGHTS);
		RemindingItem currentItem = MainActivity.rm.getCurrentItem();
		
		if(!currentItem.isHappened())
		{
			//Log.v("PrintOut","ChangeModeReceiver switchToSilent!");
			ProfileUtil.switchToSilent(context);
			currentItem.happen();
		}
		else
		{
			//Log.v("PrintOut","ChangeModeReceiver switchBack!");
			ProfileUtil.switchBack(context);
		}
		
		RemindingManager.changeModeHappened();
		
		//Log.v("PrintOut","ChangeModeReceiver end!")
	}
}
