package edu.tongji.sse.profileexpert.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import edu.tongji.sse.profileexpert.R;

public class MainActivity extends Activity 
{
	private ImageButton ib_setting = null;
	private ImageButton ib_customeProfile = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ib_setting = (ImageButton) findViewById(R.id.ib_setting);
        ib_setting.setOnClickListener(new OnClickListener()
        {
			public void onClick(View v) {
				//跳转到设置界面
				Intent intent=new Intent();
	            intent.setClass(MainActivity.this, SettingActivity.class);
	            startActivity(intent);
				}
		});
        
        ib_customeProfile = (ImageButton) findViewById(R.id.ib_customeProfile);
        ib_customeProfile.setOnClickListener(new OnClickListener()
        {
			public void onClick(View v) {
				//跳转到自定义模式界面
				Intent intent=new Intent();
	            intent.setClass(MainActivity.this, ProfileActivity.class);
	            startActivity(intent);
			}
		});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
