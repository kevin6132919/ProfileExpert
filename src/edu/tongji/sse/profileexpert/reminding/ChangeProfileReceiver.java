package edu.tongji.sse.profileexpert.reminding;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import edu.tongji.sse.profileexpert.R;
import edu.tongji.sse.profileexpert.main.MainActivity;
import edu.tongji.sse.profileexpert.provider.MyProfileTable;
import edu.tongji.sse.profileexpert.provider.RoutineTable;
import edu.tongji.sse.profileexpert.provider.TempMatterTable;
import edu.tongji.sse.profileexpert.util.NotificationUtil;
import edu.tongji.sse.profileexpert.util.ProfileUtil;

public class ChangeProfileReceiver extends BroadcastReceiver
{
	public void onReceive(Context context, Intent intent)
	{
		RemindingItem ri = MainActivity.rm.getCurrentItem();

		Cursor cursor = getCursor(context, ri.getType(),ri.getId());
		String title = null;
		boolean b = false;
		int volume = -1;
		int vibrate_type = -1;
		if(cursor.moveToFirst())
		{
			long profileId = cursor.getLong(cursor.getColumnIndex(TempMatterTable.PROFILE_ID));
			cursor = getProfileCursor(context, profileId);
			if(cursor.moveToFirst())
			{
				title =cursor.getString(cursor.getColumnIndex(MyProfileTable.NAME));
				b = cursor.getString(cursor.getColumnIndex(MyProfileTable.ALLOW_CHANGING_VOLUME))
						.equals("1");
				if(b)
				{
					volume = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MyProfileTable.VOLUME)));
				}
				vibrate_type = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MyProfileTable.VIBRATE_TYPE)));
			}
		}
		else
			return;

		if(!ri.isHappened())
		{
			NotificationUtil.sendNotify(context,
					context.getString(R.string.change_profile_text_1),
					context.getString(R.string.change_profile_text_2) + ":" + title
					, ri);
			ProfileUtil.switchToProfile(
					context,
					b,
					volume,
					vibrate_type);
		}
		else
		{
			NotificationUtil.sendNotify(context,
					context.getString(R.string.change_profile_text_1),
					context.getString(R.string.change_profile_text_3),
					ri);
			ProfileUtil.switchBack(context);
		}

		MainActivity.rm.changeModeHappened();
	}
	private Cursor getCursor(Context ctx,int type,long id)
	{
		if(type==AlarmItem.MATTER_TYPE_TEMP_MATTER)
		{
			return ctx.getContentResolver().query(
					TempMatterTable.CONTENT_URI,
					null,
					TempMatterTable._ID + "=?",
					new String[]{""+id},
					null);
		}
		else
		{
			return ctx.getContentResolver().query(
					RoutineTable.CONTENT_URI,
					null,
					RoutineTable._ID + "=?",
					new String[]{""+id},
					null);
		}
	}

	//由profile的id得到其cursor
	private Cursor getProfileCursor(Context ctx,long profile_id)
	{
		return ctx.getContentResolver().query(
				MyProfileTable.CONTENT_URI,
				null,
				MyProfileTable._ID + "=?",
				new String[]{""+profile_id},
				null);
	}
}
