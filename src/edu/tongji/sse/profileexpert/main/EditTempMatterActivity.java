package edu.tongji.sse.profileexpert.main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import edu.tongji.sse.profileexpert.R;
import edu.tongji.sse.profileexpert.control.MyDateSpinner;
import edu.tongji.sse.profileexpert.control.MyTimeSpinner;
import edu.tongji.sse.profileexpert.provider.MyProfileTable;
import edu.tongji.sse.profileexpert.provider.TempMatterTable;
import edu.tongji.sse.profileexpert.util.CommonUtil;
import edu.tongji.sse.profileexpert.util.ContentResolverUtil;

@SuppressLint("SimpleDateFormat")
public class EditTempMatterActivity extends Activity
{
	private Button bt_cancel = null;
	private Button bt_save = null;
	private EditText et_title = null;
	private EditText et_explain = null;
	private MyDateSpinner my_date_spinner_from = null;
	private MyDateSpinner my_date_spinner_to = null;
	private MyTimeSpinner my_time_spinner_from = null;
	private MyTimeSpinner my_time_spinner_to = null;
	private Spinner sp_profile = null;

	private Cursor cursor = null;
	private long id = -1;

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_temp_matter);

		getCursor();

		findViews();

		initSpinner(cursor.getLong(cursor.getColumnIndex(TempMatterTable.PROFILE_ID)));
		
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
		String str = cursor.getString(cursor.getColumnIndex(TempMatterTable.TITLE));
		et_title.setText(str);
		
		Calendar c = Calendar.getInstance();
		long l_time_from = Long.parseLong(
				cursor.getString(cursor.getColumnIndex(TempMatterTable.TIME_FROM)));
		c.setTimeInMillis(l_time_from);
		my_date_spinner_from.setDefault(
				c.get(Calendar.YEAR),
				c.get(Calendar.MONTH),
				c.get(Calendar.DAY_OF_MONTH));
		my_time_spinner_from.setDefault(
				c.get(Calendar.HOUR_OF_DAY),
				c.get(Calendar.MINUTE));
		
		long l_time_to = Long.parseLong(
				cursor.getString(cursor.getColumnIndex(TempMatterTable.TIME_TO)));
		c.setTimeInMillis(l_time_to);
		my_date_spinner_to.setDefault(
				c.get(Calendar.YEAR),
				c.get(Calendar.MONTH),
				c.get(Calendar.DAY_OF_MONTH));
		my_time_spinner_to.setDefault(
				c.get(Calendar.HOUR_OF_DAY),
				c.get(Calendar.MINUTE));
		
		str = cursor.getString(cursor.getColumnIndex(TempMatterTable.DESCRIPTION));
		et_explain.setText(str);
	}

	// 根据id找到控件
	private void findViews()
	{
		bt_cancel = (Button) findViewById(R.id.bt_cancel);
		bt_save = (Button) findViewById(R.id.bt_save);
		et_title = (EditText) findViewById(R.id.et_title);
		et_explain = (EditText) findViewById(R.id.et_explain);
		my_date_spinner_from = (MyDateSpinner) findViewById(R.id.my_date_spinner_from);
		my_date_spinner_to = (MyDateSpinner) findViewById(R.id.my_date_spinner_to);
		my_time_spinner_from = (MyTimeSpinner) findViewById(R.id.my_time_spinner_from);
		my_time_spinner_to = (MyTimeSpinner) findViewById(R.id.my_time_spinner_to);
		sp_profile = (Spinner) findViewById(R.id.sp_profile);
	}

	//拿到extra中的id并获取到Cursor
	private void getCursor()
	{
		Intent intent = getIntent();
		id = intent.getLongExtra(TempMatterActivity.EDIT_TEMP_MATTER_ID_KEY, -1);
		cursor = getContentResolver().query(
				TempMatterTable.CONTENT_URI,
				null,
				TempMatterTable._ID + "=?",
				new String[]{""+id},
				null);

		if(!cursor.moveToFirst())
		{
			Toast.makeText(EditTempMatterActivity.this,
					getString(R.string.temp_matter_not_exist),
					Toast.LENGTH_SHORT).show();
			this.finish();
		}
	}

	//初始化选择情景模式的spinner
	@SuppressWarnings("deprecation")
	private void initSpinner(long profile_id)
	{
		Cursor cursor = this.getContentResolver().query(MyProfileTable.CONTENT_URI, null, null, null, null);
		startManagingCursor(cursor);
		SpinnerAdapter adapter = new SimpleCursorAdapter(
				this,
				R.layout.profile_list_item,
				cursor,
				new String[]{MyProfileTable.NAME, MyProfileTable.DESCRIPTION},
				new int[]{R.id.profile_list_item_name, R.id.profile_list_item_discription});
		
		int position = -1;
		while(cursor.moveToNext())
		{
			position = cursor.getPosition();
			long id = cursor.getLong(cursor.getColumnIndex(MyProfileTable._ID));
			if(id == profile_id)
				break;
		}
		
		sp_profile.setAdapter(adapter);
		sp_profile.setSelection(position);
	}

	//保存当前临时事项
	protected void save()
	{
		String title = et_title.getText().toString();
		String date = (String) my_date_spinner_from.getSelectedItem();
		String time = (String) my_time_spinner_from.getSelectedItem();
		String time_from = date + " " + time;
		date = (String) my_date_spinner_to.getSelectedItem();
		time = (String) my_time_spinner_to.getSelectedItem();
		String time_to = date + " " + time;
		String explain = et_explain.getText().toString();
		long profile_id = sp_profile.getSelectedItemId();

		if(title == null || title.equals(""))
		{
			Toast.makeText(this, getString(R.string.title_not_null), Toast.LENGTH_SHORT).show();
			return;
		}

		updateTempMatter(title,time_from,time_to,explain,profile_id);
	}

	//将新增的临时事项增加到数据库
	private void updateTempMatter(String title, String time_from, String time_to,
			String explain, long profile_id)
	{
		try {
			ContentValues values = new ContentValues();
			values.put(TempMatterTable.TITLE, title);

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			long l_time_from = format.parse(time_from).getTime();
			long l_time_to = format.parse(time_to).getTime();

			if(l_time_from >= l_time_to)
			{
				Toast.makeText(this, getString(R.string.invalid_time), Toast.LENGTH_LONG).show();
				return;
			}

			String show_str = time_from.substring(11)
					+ "-" + time_to.substring(11)
					+ "  " + CommonUtil.shortString(title,5)
					+ "  " + CommonUtil.shortString(ContentResolverUtil.getProfileTitle(this, profile_id),5);

			values.put(TempMatterTable.TIME_FROM, l_time_from);
			values.put(TempMatterTable.TIME_TO, l_time_to);
			values.put(TempMatterTable.TIME_FROM_STR, time_from);
			values.put(TempMatterTable.TIME_TO_STR, time_to);
			values.put(TempMatterTable.DESCRIPTION, explain);
			values.put(TempMatterTable.SHOW_STRING, show_str);
			values.put(TempMatterTable.PROFILE_ID, profile_id);
			getContentResolver().update(
					TempMatterTable.CONTENT_URI,
					values,
					MyProfileTable._ID + "=?",
					new String[]{""+id});
			setResult(RESULT_OK);
			//MainActivity.rm.rearrange(this);

			back();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	//后退
	private void back()
	{
		this.finish();
	}
}
