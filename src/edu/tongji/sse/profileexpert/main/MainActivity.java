package edu.tongji.sse.profileexpert.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import edu.tongji.sse.profileexpert.R;

public class MainActivity extends Activity 
{
	private ImageButton ib_setting = null;
	private ImageButton ib_customeProfile = null;
	private ImageButton ib_tempMatter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
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

		ib_tempMatter = (ImageButton) findViewById(R.id.ib_tempmatter);
		ib_tempMatter.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) {
				//跳转到自定义模式界面
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, TempMatterActivity.class);
				startActivity(intent);
			}
		});
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item)
	{
		switch(item.getItemId())
		{
		case R.id.action_settings:
			//跳转到设置界面
			Intent intent=new Intent();
			intent.setClass(MainActivity.this, SettingActivity.class);
			startActivity(intent);
			return true;
		case R.id.action_exit:
			//跳转到设置界面
			this.finish();
			return true;
		default:
			return false;
		}
	}
}
