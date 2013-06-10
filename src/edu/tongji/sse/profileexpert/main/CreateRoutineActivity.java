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

public class CreateRoutineActivity extends Activity
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

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_routine);

		// 根据id找到控件
		bt_cancel = (Button) findViewById(R.id.bt_cancel);
		bt_save = (Button) findViewById(R.id.bt_save);
		et_title = (EditText) findViewById(R.id.et_title);
		et_explain = (EditText) findViewById(R.id.et_explain);
		sp_weekday_from = (Spinner) findViewById(R.id.sp_weekday_from);
		sp_weekday_to = (Spinner) findViewById(R.id.sp_weekday_to);
		my_time_spinner_from = (MyTimeSpinner) findViewById(R.id.my_time_spinner_from);
		my_time_spinner_to = (MyTimeSpinner) findViewById(R.id.my_time_spinner_to);
		sp_profile = (Spinner) findViewById(R.id.sp_profile);

		initSpinners();

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

	//保存当前日程
	protected void save()
	{
		String title = et_title.getText().toString();
		String time_from = (String) my_time_spinner_from.getSelectedItem();
		String time_to = (String) my_time_spinner_to.getSelectedItem();
		String explain = et_explain.getText().toString();
		long profile_id = sp_profile.getSelectedItemId();
		boolean is_same_day = sp_weekday_to.getSelectedItemPosition() == 0;

		if(title == null || title.equals(""))
		{
			Toast.makeText(this, getString(R.string.title_not_null), Toast.LENGTH_SHORT).show();
			return;
		}

		saveRoutine(title, weekday_selected, is_same_day, time_from, time_to, explain, profile_id);
	}

	//保存日程到数据库
	private void saveRoutine(String title, int weekdaySelected, boolean is_same_day,
			String time_from, String time_to, String explain, long profile_id)
	{
		ContentValues values = new ContentValues();
		values.put(RoutineTable.TITLE, title);

		if(is_same_day)
		{
			if(!checkTime(time_from, time_to))
				return;
		}

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
		
		getContentResolver().insert(RoutineTable.CONTENT_URI, values);
		setResult(RESULT_OK);
		
		back();
	}


	//检查输入时间 的合法性
	private boolean checkTime(String time_from, String time_to)
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
		return true;
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
		if(title.length() < length)
			return title;
		else
			return title.substring(0, length)+"...";
	}
	//后退
	private void back()
	{
		this.finish();
	}
	//初始化Spinner
	@SuppressWarnings("deprecation")
	private void initSpinners()
	{
		Intent intent = getIntent();
		weekday_selected = intent.getIntExtra(RoutineActivity.WEEKDAY_SELECTED, -1);

		if(weekday_selected == -1)
		{
			Toast.makeText(CreateRoutineActivity.this,
					getString(R.string.weekday_not_selected),
					Toast.LENGTH_SHORT).show();
			back();
		}

		//sp_weekday_from
		ArrayAdapter<CharSequence> sp_weekday_from_adapter = ArrayAdapter.createFromResource(
				this, R.array.sp_weekday_from_display,
				R.layout.weekday_select_spinner_item);
		sp_weekday_from.setAdapter(sp_weekday_from_adapter);
		sp_weekday_from.setSelection(weekday_selected);

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
		sp_profile.setAdapter(sp_profile_adapter);
	}
}
