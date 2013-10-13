package edu.tongji.sse.profileexpert.main;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.widget.Toast;
import edu.tongji.sse.profileexpert.R;
import edu.tongji.sse.profileexpert.util.CallUtil;
import edu.tongji.sse.profileexpert.util.MyConstant;

public class SettingActivity extends PreferenceActivity implements OnPreferenceChangeListener 
{
	private CheckBoxPreference cbp_arm_status = null;
	//private ListPreference lp_switch_delay = null;
	private ListPreference lp_first_reminding_time = null;
	private ListPreference lp_second_reminding_time = null;
	private CheckBoxPreference cbp_reminding_enable = null;
	private CheckBoxPreference cbp_message_shortcut_enable = null;
	private EditTextPreference etp_message_shortcut_content_for_normal_profile = null;
	
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
		// 根据key值找到控件
		cbp_arm_status = (CheckBoxPreference) findPreference("arm_status");
		//lp_switch_delay = (ListPreference) findPreference("switch_delay");
		lp_first_reminding_time = (ListPreference) findPreference("first_reminding_time");
		lp_second_reminding_time = (ListPreference) findPreference("second_reminding_time");
		cbp_reminding_enable = (CheckBoxPreference) findPreference("reminding_enable");
		cbp_message_shortcut_enable = (CheckBoxPreference) findPreference("message_shortcut_enable");
		etp_message_shortcut_content_for_normal_profile = (EditTextPreference) findPreference("message_shortcut_content_for_normal_profile");
		
		// 设置监听器
		cbp_arm_status.setOnPreferenceChangeListener(this);
		//lp_switch_delay.setOnPreferenceChangeListener(this);
		lp_first_reminding_time.setOnPreferenceChangeListener(this);
		lp_second_reminding_time.setOnPreferenceChangeListener(this);
		cbp_message_shortcut_enable.setOnPreferenceChangeListener(this);
		etp_message_shortcut_content_for_normal_profile.setOnPreferenceChangeListener(this);
		
		//设置初值
		/*lp_switch_delay.setSummary(MyConstant.getDelayText(
				Integer.parseInt(lp_switch_delay.getValue())));*/
		lp_first_reminding_time.setSummary(MyConstant.getRemindingTimeText(
				Integer.parseInt(lp_first_reminding_time.getValue())));
		/*String value = lp_second_reminding_time.getValue();
		if(value != null)
		{
			lp_second_reminding_time.setSummary(MyConstant.getRemindingTimeText(
				Integer.parseInt(value)));
		}*/
		String message_content = etp_message_shortcut_content_for_normal_profile.getText();
		if(message_content != null && !message_content.equals(""))
		{
			etp_message_shortcut_content_for_normal_profile.setSummary(message_content);
		}
	}

	//preference改变监听函数
	public boolean onPreferenceChange(Preference preference, Object newValue)
	{
		/*if(preference == lp_switch_delay)
		{
			preference.setSummary(MyConstant.getDelayText(Integer.parseInt(newValue.toString())));
			return true;
		}
		else */if(preference == lp_first_reminding_time)
		{
			preference.setSummary(MyConstant.getRemindingTimeText(Integer.parseInt(newValue.toString())));
			MainActivity.rm.rearrange(this);
			return true;
		}/*
		else if(preference == lp_second_reminding_time)
		{
			preference.setSummary(MyConstant.getRemindingTimeText(Integer.parseInt(newValue.toString())));
			return true;
		}*/
		else if(preference == cbp_reminding_enable)
		{
			MainActivity.rm.rearrange(this);
			return true;
		}
		else if(preference == cbp_arm_status)
		{
			if(cbp_arm_status.isChecked())
			{
				Toast.makeText(
						this,
						getString(R.string.stop_changing),
						Toast.LENGTH_SHORT).show();
				MainActivity.rm.stopReminding();
			}
			else
			{
				Toast.makeText(
						this,
						this.getString(R.string.start_changing),
						Toast.LENGTH_SHORT).show();
				MainActivity.rm.startReminding(this);
			}
			return true;
		}
		else if(preference == etp_message_shortcut_content_for_normal_profile)
		{
			preference.setSummary(newValue.toString());
			return true;
		}
		else if(preference == cbp_message_shortcut_enable)
		{
			if(Boolean.parseBoolean(newValue.toString()))
			{
				Toast.makeText(this, getString(R.string.message_shortcut_enable_on), Toast.LENGTH_SHORT).show();
				CallUtil.registerReceiver(this);
			}
			else
			{
				Toast.makeText(this, getString(R.string.message_shortcut_enable_off), Toast.LENGTH_SHORT).show();
				CallUtil.unregisterReceiver();
			}
			
			return true;
		}
		return false;
	}
	
	
}
