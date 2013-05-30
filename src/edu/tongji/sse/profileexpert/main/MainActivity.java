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
	private ImageButton bt_setting = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        bt_setting = (ImageButton) findViewById(R.id.bt_setting);
        bt_setting.setOnClickListener(new OnClickListener()
        {
			public void onClick(View v) {
				Intent intent=new Intent();
				//设置跳转新的activity，参数（当前对象，跳转到的class）
	            intent.setClass(MainActivity.this, SettingActivity.class);
	            //启动Activity 没有返回
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
