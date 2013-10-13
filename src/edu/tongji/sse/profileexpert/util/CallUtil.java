package edu.tongji.sse.profileexpert.util;

import android.content.Context;
import android.content.IntentFilter;
import edu.tongji.sse.profileexpert.call.MyCallReceiver;

public class CallUtil
{
	private static MyCallReceiver receiver = new MyCallReceiver();;
	private static IntentFilter filter = new IntentFilter();
	private static Context lastCtx = null;
	
	static{
        filter.addAction("android.intent.action.PHONE_STATE");  
        filter.setPriority(1000);  
	}

	public static void registerReceiver(Context ctx) {
		ctx.registerReceiver(receiver, filter);
		lastCtx = ctx;
	}
	
	public static void unregisterReceiver() {
		if(lastCtx != null)
		{
			lastCtx.unregisterReceiver(receiver);
			lastCtx = null;
		}
	}
}
