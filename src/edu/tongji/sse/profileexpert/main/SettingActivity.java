package edu.tongji.sse.profileexpert.main;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import edu.tongji.sse.profileexpert.R;
import edu.tongji.sse.profileexpert.util.MyConstant;

public class SettingActivity extends PreferenceActivity implements OnPreferenceChangeListener 
{
	private ListPreference lp_switch_delay = null;
	private ListPreference lp_first_reminding_time = null;
	private ListPreference lp_second_reminding_time = null;
	
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
		// 根据key值找到控件
		lp_switch_delay = (ListPreference) findPreference("switch_delay");
		lp_first_reminding_time = (ListPreference) findPreference("first_reminding_time");
		lp_second_reminding_time = (ListPreference) findPreference("second_reminding_time");
		
		// 设置监听器
		lp_switch_delay.setOnPreferenceChangeListener(this);
		lp_first_reminding_time.setOnPreferenceChangeListener(this);
		lp_second_reminding_time.setOnPreferenceChangeListener(this);
		
		//设置初值
		lp_switch_delay.setSummary(MyConstant.getDelayText(
				Integer.parseInt(lp_switch_delay.getValue())));
		lp_first_reminding_time.setSummary(MyConstant.getRemindingTimeText(
				Integer.parseInt(lp_first_reminding_time.getValue())));
		String value = lp_second_reminding_time.getValue();
		if(value != null)
		{
			lp_second_reminding_time.setSummary(MyConstant.getRemindingTimeText(
				Integer.parseInt(value)));
		}
	}

	//preference改变监听函数
	public boolean onPreferenceChange(Preference preference, Object newValue)
	{
		if(preference == lp_switch_delay)
		{
			preference.setSummary(MyConstant.getDelayText(Integer.parseInt(newValue.toString())));
			return true;
		}
		else if(preference == lp_first_reminding_time)
		{
			preference.setSummary(MyConstant.getRemindingTimeText(Integer.parseInt(newValue.toString())));
			return true;
		}
		else if(preference == lp_second_reminding_time)
		{
			preference.setSummary(MyConstant.getRemindingTimeText(Integer.parseInt(newValue.toString())));
			return true;
		}
		return false;
	}
	
	
}
