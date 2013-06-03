package edu.tongji.sse.profileexpert.util;

import edu.tongji.sse.profileexpert.provider.MyProfileTable;

public class DefaultProfile
{
	public static final String INSERT_PROFILE_NORMAL = 
			"INSERT INTO " + MyProfileTable.TABLE_NAME
			+ " VALUES ('0','标准','1','75','0','0',null,"
			+ "'抱歉，我现在不方便接电话，稍后给您回电','铃声:75% 震动:不变')";
	
	public static final String INSERT_PROFILE_WORK = 
			"INSERT INTO " + MyProfileTable.TABLE_NAME
			+ " VALUES ('1','工作','1','0','0','0',null,"
			+ "'抱歉，我正在工作，有空立即给您回电','铃声:0% 震动:不变')";
	
	public static final String INSERT_PROFILE_CONFERENCE = 
			"INSERT INTO " + MyProfileTable.TABLE_NAME
			+ " VALUES ('2','会议','1','0','2','0',null,"
			+ "'抱歉，我正在开会，散会后给您回电','铃声:0% 震动:关闭')";
	
	public static final String INSERT_PROFILE_BREAK = 
			"INSERT INTO " + MyProfileTable.TABLE_NAME
			+ " VALUES ('3','休息','1','0','2','0',null,"
			+ "'抱歉，我正在休息，稍后后给您回电','铃声:0% 震动:关闭')";
}
