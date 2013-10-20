package edu.tongji.sse.profileexpert.main;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.net.Uri;
import android.os.Bundle;
import edu.tongji.sse.profileexpert.R;

public class SendMessageConfirmDialog extends Activity
{
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		
		/*TextView tv = (TextView) findViewById(R.id.msg);
		tv.setText(getString(R.string.send_message_content));
		
		Button cancelButton = (Button) findViewById(R.id.bt_cancel);
		cancelButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				SendMessageConfirmDialog.this.finish();
			}
		});
		
		Button okButton = (Button) findViewById(R.id.bt_ok);
		final String incomingNumber = getIntent().getStringExtra("incomingNumber");
		okButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Uri smsToUri = Uri.parse("smsto:" + incomingNumber);
				Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);  
				intent.putExtra("sms_body", "∂Ã–≈ƒ⁄»›≤‚ ‘");  
				startActivity(intent);
				SendMessageConfirmDialog.this.finish();
			}
		});*/

		final String incomingNumber = getIntent().getStringExtra("incomingNumber");
		
		final String msg = getIntent().getStringExtra("messageContent");
		
		new Builder(this)
		.setIcon(R.drawable.alerts_warning)
		.setMessage(getString(R.string.send_message_content))
		.setTitle(getString(R.string.send_message_title))
		.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which)
			{
				Uri smsToUri = Uri.parse("smsto:" + incomingNumber);
				Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);  
				intent.putExtra("sms_body", msg);  
				startActivity(intent);
				dialog.dismiss();
				SendMessageConfirmDialog.this.finish();
			}})
			.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					SendMessageConfirmDialog.this.finish();
				}
			})
			.setOnCancelListener(new OnCancelListener()
			{
				public void onCancel(DialogInterface dialog)
				{
					SendMessageConfirmDialog.this.finish();
				}
			})
			.show();
	}
}
