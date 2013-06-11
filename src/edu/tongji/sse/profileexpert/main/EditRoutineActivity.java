package edu.tongji.sse.profileexpert.main;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import edu.tongji.sse.profileexpert.R;
import edu.tongji.sse.profileexpert.control.MyTimeSpinner;
import edu.tongji.sse.profileexpert.provider.MyProfileTable;
import edu.tongji.sse.profileexpert.provider.RoutineTable;

public class EditRoutineActivity extends Activity
{
	private Button bt_cancel = null;
	private Button bt_save = null;
	private EditText et_title = null;
	private EditText et_explain = null;
	private Spinner sp_weekday_from = null;
	private Spinner sp_weekday_to = null;
	private MyTimeSpinner my_time_spinner_from = null;
	private MyTimeSpinner my_time_spinner_to = null;
	private Spinner sp_profile = null;

	private int weekday_selected = -1;

	private Cursor cursor = null;
	private long id = -1;

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_routine);

		getCursor();

		findViews();

		initSpinners(cursor.getLong(cursor.getColumnIndex(RoutineTable.PROFILE_ID)));

		initValues();
		
		// 设置监听器
		bt_cancel.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) {
				back();
			}
		});
		bt_save.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) {
				save();
			}
		});
	}

	//初始化各控件的值
	private void initValues()
	{
		String title = cursor.getString(cursor.getColumnIndex(RoutineTable.TITLE));
		et_title.setText(title);
		
		int startDay = cursor.getInt(cursor.getColumnIndex(RoutineTable.START_DAY));
		sp_weekday_from.setSelection(startDay);
		
		int isSameDay = cursor.getInt(cursor.getColumnIndex(RoutineTable.IS_SAME_DAY));
		sp_weekday_to.setSelection(1-isSameDay);
		
		String timeFrom = cursor.getString(cursor.getColumnIndex(RoutineTable.TIME_FROM));
		my_time_spinner_from.setDefault(
				Integer.parseInt(timeFrom.substring(0, 2)),
				Integer.parseInt(timeFrom.substring(3)));
		
		String timeto = cursor.getString(cursor.getColumnIndex(RoutineTable.TIME_TO));
		my_time_spinner_to.setDefault(
				Integer.parseInt(timeto.substring(0, 2)),
				Integer.parseInt(timeto.substring(3)));
		
		String explain = cursor.getString(cursor.getColumnIndex(RoutineTable.DESCRIPTION));
		et_explain.setText(explain);
	}

	// 根据id找到控件
	private void findViews()
	{
		bt_cancel = (Button) findViewById(R.id.bt_cancel);
		bt_save = (Button) findViewById(R.id.bt_save);
		et_title = (EditText) findViewById(R.id.et_title);
		et_explain = (EditText) findViewById(R.id.et_explain);
		sp_weekday_from = (Spinner) findViewById(R.id.sp_weekday_from);
		sp_weekday_to = (Spinner) findViewById(R.id.sp_weekday_to);
		my_time_spinner_from = (MyTimeSpinner) findViewById(R.id.my_time_spinner_from);
		my_time_spinner_to = (MyTimeSpinner) findViewById(R.id.my_time_spinner_to);
		sp_profile = (Spinner) findViewById(R.id.sp_profile);
	}

	//拿到extra中的id并获取到Cursor
	private void getCursor()
	{
		Intent intent = getIntent();
		id = intent.getLongExtra(RoutineActivity.EDIT_ROUTINE_ID_KEY, -1);
		cursor = getContentResolver().query(
				RoutineTable.CONTENT_URI,
				null,
				RoutineTable._ID + "=?",
				new String[]{""+id},
				null);

		if(!cursor.moveToFirst())
		{
			Toast.makeText(EditRoutineActivity.this,
					getString(R.string.routine_not_exist),
					Toast.LENGTH_SHORT).show();
			this.finish();
		}
	}

	//保存当前日程
	protected void save()
	{
		String title = et_title.getText().toString();
		String time_from = (String) my_time_spinner_from.getSelectedItem();
		String time_to = (String) my_time_spinner_to.getSelectedItem();
		String explain = et_explain.getText().toString();
		weekday_selected = sp_weekday_from.getSelectedItemPosition();
		long profile_id = sp_profile.getSelectedItemId();
		boolean is_same_day = sp_weekday_to.getSelectedItemPosition() == 0;

		if(title == null || title.equals(""))
		{
			Toast.makeText(this, getString(R.string.title_not_null), Toast.LENGTH_SHORT).show();
			return;
		}

		upadteRoutine(title, weekday_selected, is_same_day, time_from, time_to, explain, profile_id);
	}

	//保存日程到数据库
	private void upadteRoutine(String title, int weekdaySelected, boolean is_same_day,
			String time_from, String time_to, String explain, long profile_id)
	{
		ContentValues values = new ContentValues();
		values.put(RoutineTable.TITLE, title);

		if(!checkTime(weekdaySelected, is_same_day, time_from, time_to))
			return;

		String show_str = time_from
				+ "-" + time_to
				+ "  " + shortString(title,5)
				+ "  " + shortString(getProfileTitle(profile_id),5);

		values.put(RoutineTable.START_DAY, weekdaySelected);
		values.put(RoutineTable.IS_SAME_DAY, is_same_day);
		values.put(RoutineTable.TIME_FROM, time_from);
		values.put(RoutineTable.TIME_TO, time_to);
		values.put(RoutineTable.DESCRIPTION, explain);
		values.put(RoutineTable.SHOW_STRING, show_str);
		values.put(RoutineTable.PROFILE_ID, profile_id);

		getContentResolver().update(
				RoutineTable.CONTENT_URI,
				values,
				RoutineTable._ID + "=?",
				new String[]{""+id});
		setResult(RESULT_OK);

		back();
	}


	//检查输入时间 的合法性
	private boolean checkTime(int weekdaySelected, boolean is_same_day,
			String time_from, String time_to)
	{
		//检查时间顺序是否混乱
		if(is_same_day)
		{
			int hour = -1, minute = -1;
			hour = Integer.parseInt(time_from.substring(0,2));
			minute = Integer.parseInt(time_from.substring(3));
			int from = hour * 60 + minute;

			hour = Integer.parseInt(time_to.substring(0,2));
			minute = Integer.parseInt(time_to.substring(3));
			int to = hour * 60 + minute;

			if(from>=to)
			{
				Toast.makeText(this, getString(R.string.invalid_time), Toast.LENGTH_LONG).show();
				return false;
			}
		}

		//检查是否与其它日程产生冲突
		Cursor cursor = null;
		if(is_same_day)
		{
			cursor = getContentResolver().query(
					RoutineTable.CONTENT_URI,
					null,
					"(" + RoutineTable._ID + "<>?" +
					") AND " +
					"(" +
						"(" +
							"(" + RoutineTable.START_DAY + "=? AND " + RoutineTable.IS_SAME_DAY + "=?" +
							") AND ("+"("+ RoutineTable.TIME_FROM + ">? AND " + RoutineTable.TIME_FROM + "<?" +
								      ") OR (" + RoutineTable.TIME_TO + ">? AND " + RoutineTable.TIME_TO + "<?" +
									       ") OR ("  + RoutineTable.TIME_FROM + "<? AND " + RoutineTable.TIME_TO + ">?" +
									       		")" +
								  ")" +
						") OR (" + RoutineTable.START_DAY + "=? AND " + RoutineTable.IS_SAME_DAY + "=? AND " +
								RoutineTable.TIME_FROM + "<?" +
							 ")" +
						"OR (" + RoutineTable.START_DAY + "=? AND " + RoutineTable.IS_SAME_DAY + "=? AND " +
								RoutineTable.TIME_TO + ">?" +
							")" +
					")"
						,
						new String[]{""+id,
							""+weekdaySelected, ""+1,
							time_from, time_to,
							time_from, time_to,
							time_from, time_to,
							""+weekdaySelected, ""+0,
							time_to,
							""+getYesterday(weekdaySelected), ""+0,
							time_from,
					},
					null);
		}
		else
		{
			cursor = getContentResolver().query(
					RoutineTable.CONTENT_URI,
					null,
					"(" + RoutineTable._ID + "<>?" +
					") AND " +
					"(" +
						"("
							+ RoutineTable.START_DAY + "=? AND " + RoutineTable.IS_SAME_DAY + "=? " +
						") OR " + 
						"("
						 	+ RoutineTable.START_DAY + "=? AND " + RoutineTable.IS_SAME_DAY + "=? AND " +
						 		RoutineTable.TIME_TO + ">?" +
						") OR " +
						"(" 
							+ RoutineTable.START_DAY + "=? AND " + RoutineTable.IS_SAME_DAY + "=? AND " +
					 			RoutineTable.TIME_TO + ">?" +
						") OR " +
						"(" 
							+ RoutineTable.START_DAY + "=? AND " +
								RoutineTable.TIME_FROM + "<?" +
						")" +
					")"
						,
						new String[]{""+id,
							""+weekdaySelected, ""+0,
							""+weekdaySelected, ""+1,
							time_from,
							""+getYesterday(weekdaySelected), ""+0,
							time_from,
							""+getTomorrow(weekdaySelected),
							time_to,
					},
					null);
		}

		if(cursor.moveToFirst())
		{
			//long _id = cursor.getLong(cursor.getColumnIndex(RoutineTable._ID));
			Toast.makeText(this, getString(R.string.conflict_time), Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}

	//得到后一天
	private int getTomorrow(int today)
	{
		if(today == 6)
			return 0;
		else
			return today + 1;
	}

	//得到前一天
	private int getYesterday(int today)
	{
		if(today > 0)
			return today - 1;
		else
			return 6;
	}

	//由profile的id得到其标题
	private String getProfileTitle(long profile_id)
	{
		Cursor cursor = getContentResolver().query(
				MyProfileTable.CONTENT_URI,
				null,
				MyProfileTable._ID + "=?",
				new String[]{""+profile_id},
				null);

		if(!cursor.moveToFirst())
		{
			return getString(R.string.show_profile_not_exist);
		}
		else return cursor.getString(cursor.getColumnIndex(MyProfileTable.NAME));
	}

	//剪短string
	private String shortString(String title, int length)
	{
		if(title.length() < length + 2)
			return title;
		else
			return title.substring(0, length)+"..";
	}

	//后退
	private void back()
	{
		this.finish();
	}
	//初始化Spinner
	@SuppressWarnings("deprecation")
	private void initSpinners(long profile_id)
	{
		//sp_weekday_from
		ArrayAdapter<CharSequence> sp_weekday_from_adapter = ArrayAdapter.createFromResource(
				this, R.array.sp_weekday_from_display,
				R.layout.weekday_select_spinner_item);
		sp_weekday_from_adapter.setDropDownViewResource(R.layout.weekday_select_spinner_dropdown_item);
		sp_weekday_from.setAdapter(sp_weekday_from_adapter);

		//sp_weekday_to
		ArrayAdapter<CharSequence> sp_weekday_to_adapter = ArrayAdapter.createFromResource(
				this, R.array.sp_weekday_to_display,
				R.layout.weekday_select_spinner_item);
		sp_weekday_to_adapter.setDropDownViewResource(R.layout.weekday_select_spinner_dropdown_item);
		sp_weekday_to.setAdapter(sp_weekday_to_adapter);

		//sp_profile
		Cursor cursor = this.getContentResolver().query(MyProfileTable.CONTENT_URI, null, null, null, null);
		startManagingCursor(cursor);
		SpinnerAdapter sp_profile_adapter = new SimpleCursorAdapter(
				this,
				R.layout.profile_list_item,
				cursor,
				new String[]{MyProfileTable.NAME, MyProfileTable.DESCRIPTION},
				new int[]{R.id.profile_list_item_name, R.id.profile_list_item_discription});
		
		int position = -1;
		while(cursor.moveToNext())
		{
			position = cursor.getPosition();
			long temp_id = cursor.getLong(cursor.getColumnIndex(RoutineTable._ID));
			if(temp_id == profile_id)
				break;
		}
		
		sp_profile.setAdapter(sp_profile_adapter);
		sp_profile.setSelection(position);
	}
}
