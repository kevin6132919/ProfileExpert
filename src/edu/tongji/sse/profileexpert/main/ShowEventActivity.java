package edu.tongji.sse.profileexpert.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import edu.tongji.sse.profileexpert.R;
import edu.tongji.sse.profileexpert.provider.MyProfileTable;
import edu.tongji.sse.profileexpert.provider.RoutineTable;
import edu.tongji.sse.profileexpert.provider.TempMatterTable;
import edu.tongji.sse.profileexpert.reminding.AlarmItem;
import edu.tongji.sse.profileexpert.reminding.RemindingItem;

public class ShowEventActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		Intent intent = getIntent();
		int type = intent.getIntExtra(RemindingItem.TYPE, -1);
		long id = intent.getLongExtra(RemindingItem.ID, -1);

		Cursor cursor = getCursor(type, id);

		if(cursor.moveToFirst())
		{
			String msg = "";

			if(type==AlarmItem.MATTER_TYPE_TEMP_MATTER)
			{
				String title = cursor.getString(cursor.getColumnIndex(TempMatterTable.TITLE));
				msg += "标题:" + title + "\n";

				String timeFrom = cursor.getString(cursor.getColumnIndex(TempMatterTable.TIME_FROM_STR));
				msg += "从:" + timeFrom + "\n";

				String timeTo = cursor.getString(cursor.getColumnIndex(TempMatterTable.TIME_TO_STR));
				msg += "至:" + timeTo + "\n";

				String content = cursor.getString(cursor.getColumnIndex(TempMatterTable.DESCRIPTION));
				msg += content + "\n";

				long profileId = cursor.getLong(cursor.getColumnIndex(TempMatterTable.PROFILE_ID));

				Cursor cursor2 = getContentResolver().query(
						MyProfileTable.CONTENT_URI,
						null,
						MyProfileTable._ID + "=?",
						new String[]{""+profileId},
						null);
				if(cursor2.moveToFirst())
				{
					String profileName = cursor2.getString(cursor2.getColumnIndex(MyProfileTable.NAME));
					String profileDesc = cursor2.getString(cursor2.getColumnIndex(MyProfileTable.DESCRIPTION));
					msg += "对应模式:" + profileName + "\n";
					msg += profileDesc;
				}
			}
			else
			{
				String title = cursor.getString(cursor.getColumnIndex(RoutineTable.TITLE));
				msg += "标题:" + title + "\n";

				int startDay = cursor.getInt(cursor.getColumnIndex(RoutineTable.START_DAY));
				String weekDay = RoutineActivity.weekdays[startDay];
				String timeFrom = cursor.getString(cursor.getColumnIndex(RoutineTable.TIME_FROM));
				msg += "从:" + weekDay + " " + timeFrom + "\n";

				boolean isSameDay = "1".equals(
						cursor.getString(cursor.getColumnIndex(RoutineTable.IS_SAME_DAY)));
				String str = isSameDay? "当天":"次日";
				String timeTo = cursor.getString(cursor.getColumnIndex(RoutineTable.TIME_TO));
				msg += "至:" + str + " " + timeTo + "\n";

				String content = cursor.getString(cursor.getColumnIndex(RoutineTable.DESCRIPTION));
				msg += content + "\n";

				long profileId = cursor.getLong(cursor.getColumnIndex(RoutineTable.PROFILE_ID));

				Cursor cursor2 = getContentResolver().query(
						MyProfileTable.CONTENT_URI,
						null,
						MyProfileTable._ID + "=?",
						new String[]{""+profileId},
						null);
				if(cursor2.moveToFirst())
				{
					String profileName = cursor2.getString(cursor2.getColumnIndex(MyProfileTable.NAME));
					String profileDesc = cursor2.getString(cursor2.getColumnIndex(MyProfileTable.DESCRIPTION));
					msg += "对应模式:" + profileName + "\n";
					msg += profileDesc;
				}
			}

			new AlertDialog.Builder(this)
			.setIcon(android.R.drawable.btn_star)
			.setTitle(getString(R.string.event))
			.setMessage(msg)
			.show();
		}
	}

	private Cursor getCursor(int type,long id)
	{
		if(type==AlarmItem.MATTER_TYPE_TEMP_MATTER)
		{
			return getContentResolver().query(
					TempMatterTable.CONTENT_URI,
					null,
					TempMatterTable._ID + "=?",
					new String[]{""+id},
					null);
		}
		else
		{
			return getContentResolver().query(
					RoutineTable.CONTENT_URI,
					null,
					RoutineTable._ID + "=?",
					new String[]{""+id},
					null);
		}
	}

}
