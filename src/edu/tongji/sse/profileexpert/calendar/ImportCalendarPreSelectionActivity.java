package edu.tongji.sse.profileexpert.calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import edu.tongji.sse.profileexpert.R;
import edu.tongji.sse.profileexpert.control.MyDateSpinner;
import edu.tongji.sse.profileexpert.util.MyConstant;

public class ImportCalendarPreSelectionActivity extends Activity 
{
	private MyDateSpinner my_date_spinner_from = null;
	private MyDateSpinner my_date_spinner_to = null;
	private Spinner sp_calendar_address = null;
	private Button bt_cancel = null;
	private Button bt_select = null;
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.import_calendar_pre_selction);
		
		// 根据id找到控件
		my_date_spinner_from = (MyDateSpinner) findViewById(R.id.my_date_spinner_from);
		my_date_spinner_to = (MyDateSpinner) findViewById(R.id.my_date_spinner_to);
		sp_calendar_address = (Spinner) findViewById(R.id.sp_calendar_address);
		bt_cancel = (Button) findViewById(R.id.bt_cancel);
		bt_select = (Button) findViewById(R.id.bt_select);
		
		initSpinner();

		// 设置监听器
		bt_cancel.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) {
				ImportCalendarPreSelectionActivity.this.finish();
			}
		});
		bt_select.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) {
				switchToImportCalenderActivity();
			}
		});
	}

	//切换到导入系统日历界面
	@SuppressLint("SimpleDateFormat")
	private void switchToImportCalenderActivity()
	{
		long selected_calendar_id = sp_calendar_address.getSelectedItemId();
		String date = (String) my_date_spinner_from.getSelectedItem();
		String time_from = date + " 00:00";
		date = (String) my_date_spinner_to.getSelectedItem();
		String time_to = date + " 24:00";
		long l_time_from = -1;
		long l_time_to = -1;
		
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			l_time_from = format.parse(time_from).getTime();
			l_time_to = format.parse(time_to).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if(l_time_from >= l_time_to)
		{
			Toast.makeText(this, getString(R.string.invalid_time), Toast.LENGTH_LONG).show();
			return;
		}
		
		Intent intent=new Intent();
		intent.putExtra("selected_calendar_id", selected_calendar_id);
		intent.putExtra("time_from", l_time_from);
		intent.putExtra("time_to", l_time_to);
		intent.setClass(ImportCalendarPreSelectionActivity.this, ImportCalendarActivity.class);
		startActivityForResult(intent, MyConstant.REQUEST_CODE_IMPORT_CALENDAR_EVENTS);
	}
	
	//初始化选择日历地址的spinner
	@SuppressWarnings("deprecation")
	private void initSpinner()
	{
		Cursor cursor = this.getContentResolver().query(
				Uri.parse(MyConstant.calanderURL),
				new String[]{"_id", "calendar_access_level", "name"},
				"calendar_access_level=? and name!=?",
				new String[]{"700","contact@localhost.localdomain"},//700 就是 OWNER_ACCESS
				null);
		
		if(cursor == null || cursor.getCount() <= 0)
		{
			Toast.makeText(this, R.string.error_no_calendar_account, Toast.LENGTH_LONG).show();
			this.finish();
			return;
		}
		
		startManagingCursor(cursor);
		SpinnerAdapter adapter = new SimpleCursorAdapter(
				this,
				android.R.layout.simple_spinner_dropdown_item,
				cursor,
				new String[]{"name"},
				new int[]{android.R.id.text1});
		sp_calendar_address.setAdapter(adapter);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == MyConstant.REQUEST_CODE_IMPORT_CALENDAR_EVENTS)
		{
			if(resultCode == RESULT_OK)
			{
				this.finish();
			}
		}
	}
}
