package edu.tongji.sse.profileexpert.util;

import android.content.Context;
import android.database.Cursor;
import edu.tongji.sse.profileexpert.R;
import edu.tongji.sse.profileexpert.provider.MyProfileTable;
import edu.tongji.sse.profileexpert.provider.RoutineTable;
import edu.tongji.sse.profileexpert.provider.TempMatterTable;
import edu.tongji.sse.profileexpert.reminding.AlarmItem;

public class ContentResolverUtil
{
	public static Cursor getMatterCursor(Context ctx, int matterType, long matterId)
	{
		if(matterType==AlarmItem.MATTER_TYPE_TEMP_MATTER)
		{
			return ctx.getContentResolver().query(
					TempMatterTable.CONTENT_URI,
					null,
					TempMatterTable._ID + "=?",
					new String[]{""+matterId},
					null);
		}
		else
		{
			return ctx.getContentResolver().query(
					RoutineTable.CONTENT_URI,
					null,
					RoutineTable._ID + "=?",
					new String[]{""+matterId},
					null);
		}
	}
	
	public static Cursor getProfileCursor(Context ctx, long profileId)
	{
		return ctx.getContentResolver().query(
				MyProfileTable.CONTENT_URI,
				null,
				MyProfileTable._ID + "=?",
				new String[]{""+profileId},
				null);
	}
	
	//由profile的id得到其标题
	public static String getProfileTitle(Context ctx, long profile_id)
	{
		Cursor cursor = ctx.getContentResolver().query(
				MyProfileTable.CONTENT_URI,
				null,
				MyProfileTable._ID + "=?",
				new String[]{""+profile_id},
				null);

		if(!cursor.moveToFirst())
		{
			return ctx.getString(R.string.show_profile_not_exist);
		}
		else return cursor.getString(cursor.getColumnIndex(MyProfileTable.NAME));
	}
	
	public static String getShowStringForTempMatter(Context ctx, String time_from,
			String time_to, String title, long profile_id)
	{
		return time_from.substring(11)
				+ "-" + time_to.substring(11)
				+ "  " + CommonUtil.shortString(title,5)
				+ "  " + CommonUtil.shortString(getProfileTitle(ctx, profile_id),5);
	}
}
