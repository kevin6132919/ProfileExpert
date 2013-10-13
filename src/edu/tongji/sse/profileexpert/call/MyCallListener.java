package edu.tongji.sse.profileexpert.call;

import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import edu.tongji.sse.profileexpert.main.SendMessageConfirmDialog;


public class MyCallListener extends PhoneStateListener {
	private static int lastetState = TelephonyManager.CALL_STATE_IDLE; // 最后的状态
	private Context context;

	public MyCallListener(Context context) {
		super();
		this.context = context;
	}

	public void onCallStateChanged(int state, String incomingNumber)
	{
		// 如果当前状态为空闲,上次状态为响铃中的话,则认为是未接来电
		if(lastetState ==  TelephonyManager.CALL_STATE_RINGING 
				&& state == TelephonyManager.CALL_STATE_IDLE){
			sendSmgWhenMissedCall(incomingNumber);
		}

		// 最后的时候改变当前值
		lastetState = state;
	}

	private void sendSmgWhenMissedCall(final String incomingNumber)
	{
		// ... 进行未接来电处理
		Intent intent = new Intent(context, SendMessageConfirmDialog.class);
		intent.putExtra("incomingNumber", incomingNumber);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
				| Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);//关键的一步，设置启动模式
		context.startActivity(intent);
	}
}

