package edu.tongji.sse.profileexpert.util;

public class MyConstant
{
	//震动设置 
	public static final int VIBRATE_SETTING_UNCHANGED = 0;
	public static final int VIBRATE_SETTING_CHANGE_TO_OPEN = 1;
	public static final int VIBRATE_SETTING_CHANGE_TO_CLOSE = 2;

	public static final String STRING_VIBRATE_SETTING_UNCHANGED = "保持不变";
	public static final String STRING_VIBRATE_SETTING_CHANGE_TO_OPEN = "切换至开启";
	public static final String STRING_VIBRATE_SETTING_CHANGE_TO_CLOSE = "切换至关闭";
	
	public static String getVibtareText(int type)
	{
		switch(type)
		{
		case VIBRATE_SETTING_UNCHANGED:
			return STRING_VIBRATE_SETTING_UNCHANGED;
		case VIBRATE_SETTING_CHANGE_TO_OPEN:
			return STRING_VIBRATE_SETTING_CHANGE_TO_OPEN;
		case VIBRATE_SETTING_CHANGE_TO_CLOSE:
			return STRING_VIBRATE_SETTING_CHANGE_TO_CLOSE;
		}
		return null;
	}

	//切换延迟 
	public static final int SEITCHING_DELAY_1 = 1;
	public static final int SEITCHING_DELAY_2 = 2;
	public static final int SEITCHING_DELAY_5 = 5;
	public static final int SEITCHING_DELAY_10 = 10;
	public static final int SEITCHING_DELAY_15 = 15;
	
	public static final String MINUTE = "分钟";
	public static final String SECOND = "秒";
	
	public static String getDelayText(int type)
	{
		return type + MINUTE;
	}

	//提醒时间
	public static final int REMING_TIME_30 = 30;
	public static final int REMING_TIME_1 = 1;
	public static final int REMING_TIME_2 = 2;
	public static final int REMING_TIME_5 = 5;
	public static final int REMING_TIME_10 = 10;
	public static final int REMING_TIME_15 = 15;
	
	public static String getRemindingTimeText(int type)
	{
		if(type == 30)
			return type + MINUTE;
		else
			return type + SECOND;
	}
	
	//activity之间的传值类型
	public static final int REQUEST_CODE_CREATE_PROFILE = 1;
	public static final int REQUEST_CODE_EDIT_PROFILE = 2;
	public static final int REQUEST_CODE_CREATE_TEMP_MATTER = 3;
	public static final int REQUEST_CODE_CREATE_ROUTINE = 4;
	
	//MyProfile中toString()方法里使用到的文字
	public static final String RINGTONE = "铃声";
	public static final String DONT_CHANGE = "不变";
	public static final String VIBRATE = "震动";
	
	//星期几
	public static final String Monday = "周一";
	public static final String Tuesday = "周二";
	public static final String Wednesday = "周三";
	public static final String Thursday = "周四";
	public static final String Friday = "周五";
	public static final String Saturday = "周六";
	public static final String Sunday = "周日";
	
	public static String getDayOfWeek(int dayOfWeek)
	{
		switch(dayOfWeek)
		{
		case 1: return Monday;
		case 2: return Tuesday;
		case 3: return Wednesday;
		case 4: return Thursday;
		case 5: return Friday;
		case 6: return Saturday;
		default : return Sunday;
		}
	}
}
