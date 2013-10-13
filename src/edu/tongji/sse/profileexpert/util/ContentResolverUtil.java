package edu.tongji.sse.profileexpert.util;

import android.content.Context;
import android.database.Cursor;
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
}
