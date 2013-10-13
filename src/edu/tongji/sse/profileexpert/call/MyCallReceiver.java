package edu.tongji.sse.profileexpert.call;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class MyCallReceiver extends BroadcastReceiver
{
	public void onReceive(Context context, Intent intent)
	{
		TelephonyManager telephony = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		MyCallListener customPhoneListener = new MyCallListener(context.getApplicationContext());

		telephony.listen(customPhoneListener,
				PhoneStateListener.LISTEN_CALL_STATE);

	}
}

