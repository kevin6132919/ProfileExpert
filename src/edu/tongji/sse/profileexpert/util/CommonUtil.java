package edu.tongji.sse.profileexpert.util;

public class CommonUtil
{
	//¼ô¶Ìstring
	public static String shortString(String str, int length)
	{
		if(str.length() < length + 2)
			return str;
		else
			return str.substring(0, length)+"..";
	}
}
