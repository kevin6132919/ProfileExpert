package edu.tongji.sse.profileexpert.main;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.WindowManager;
import edu.tongji.sse.profileexpert.R;

public class SettingActivity extends PreferenceActivity 
{
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        //…Ë÷√»´∆¡œ‘ æ
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		addPreferencesFromResource(R.xml.preferences);
	}
	
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}*/
}
