package edu.tongji.sse.profileexpert.main;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import edu.tongji.sse.profileexpert.R;

public class ProfileActivity extends ListActivity
{
	private Button bt_new_profile = null;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_profile_display);
        
        bt_new_profile = (Button) findViewById(R.id.bt_add_profile);
        bt_new_profile.setOnClickListener(new OnClickListener() 
        {
			public void onClick(View v) {
				//跳转到自定义模式界面
				Intent intent=new Intent();
	            intent.setClass(ProfileActivity.this, CreateProfileActivity.class);
	            startActivity(intent);
			}
		});
	}

}
