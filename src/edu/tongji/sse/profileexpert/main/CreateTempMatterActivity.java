package edu.tongji.sse.profileexpert.main;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import edu.tongji.sse.profileexpert.R;
import edu.tongji.sse.profileexpert.control.MyDateSpinner;
import edu.tongji.sse.profileexpert.control.MyTimeSpinner;
import edu.tongji.sse.profileexpert.provider.TempMatterTable;
import edu.tongji.sse.profileexpert.util.MyConstant;

@SuppressLint("SimpleDateFormat")
public class CreateTempMatterActivity extends Activity
{
	private Button bt_cancel = null;
	private Button bt_save = null;
	private EditText et_title = null;
	private EditText et_explain = null;
	private MyDateSpinner my_date_spinner_from = null;
	private MyDateSpinner my_date_spinner_to = null;
	private MyTimeSpinner my_time_spinner_from = null;
	private MyTimeSpinner my_time_spinner_to = null;
	
	/*private static String calanderURL = "";
	private static String calanderEventURL = "";
    
    private int gmailPosition = -1;
    //为了兼容不同版本的日历,2.2以后url发生改变
	static{
		if(Integer.parseInt(Build.VERSION.SDK) >= 8){
			calanderURL = "content://com.android.calendar/calendars";
			calanderEventURL = "content://com.android.calendar/events";
			//calanderRemiderURL = "content://com.android.calendar/reminders";

		}else{
			calanderURL = "content://calendar/calendars";
			calanderEventURL = "content://calendar/events";
			//calanderRemiderURL = "content://calendar/reminders";		
		}
	}*/

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_temp_matter);

        /*gmailPosition = getGmailPosition();*/

		// 根据id找到控件
		bt_cancel = (Button) findViewById(R.id.bt_cancel);
		bt_save = (Button) findViewById(R.id.bt_save);
		et_title = (EditText) findViewById(R.id.et_title);
		et_explain = (EditText) findViewById(R.id.et_explain);
		my_date_spinner_from = (MyDateSpinner) findViewById(R.id.my_date_spinner_from);
		my_date_spinner_to = (MyDateSpinner) findViewById(R.id.my_date_spinner_to);
		my_time_spinner_from = (MyTimeSpinner) findViewById(R.id.my_time_spinner_from);
		my_time_spinner_to = (MyTimeSpinner) findViewById(R.id.my_time_spinner_to);

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

		if(title == null || title.equals(""))
		{
			Toast.makeText(this, getString(R.string.title_not_null), Toast.LENGTH_SHORT).show();
			return;
		}

		saveTempMatter(title,time_from,time_to,explain);
	}

	//将新增的临时事项增加到数据库
	private void saveTempMatter(String title, String time_from, String time_to, String explain)
	{
		try {
			ContentValues values = new ContentValues();
			values.put(TempMatterTable.TITLE, title);

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
			long l_time_from = format.parse(time_from).getTime();
			long l_time_to = format.parse(time_to).getTime();

			if(l_time_from >= l_time_to)
			{
				Toast.makeText(this, getString(R.string.invalid_time), Toast.LENGTH_LONG).show();
				return;
			}
			
			String show_str = time_from.substring(11)
					+ "-" + time_to.substring(11)
					+ " " + title;
			
			values.put(TempMatterTable.TIME_FROM, l_time_from);
			values.put(TempMatterTable.TIME_TO, l_time_to);
			values.put(TempMatterTable.TIME_FROM_STR, time_from);
			values.put(TempMatterTable.TIME_TO_STR, time_to);
			values.put(TempMatterTable.DESCRIPTION, explain);
			values.put(TempMatterTable.SHOW_STRING, show_str);
			getContentResolver().insert(TempMatterTable.CONTENT_URI, values);
			setResult(MyConstant.REQUEST_CODE_CREATE_TEMP_MATTER);
			
			/*saveToGoogleCalendar(title,explain,l_time_from,l_time_to);*/
			
			back();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/*//同时将该事项保存到google自带Calendar中
	private void saveToGoogleCalendar(String title, String explain, long l_time_from, long l_time_to)
	{
		String calId = getGoogleAccountId();
		ContentValues event = new ContentValues();
		event.put("calendar_id",calId);
		event.put("title", title);
    	event.put("description", explain);
    	event.put("dtstart", l_time_from);
    	event.put("dtend", l_time_to);
    	getContentResolver().insert(Uri.parse(calanderEventURL), event);
	}

	//得到到对应的google账户对应的id
	private String getGoogleAccountId()
	{
		Cursor userCursor = getContentResolver().query(Uri.parse(calanderURL), null, 
				null, null, null);
		if(userCursor.getCount() > 0){
			if(gmailPosition != -1)
			{
				userCursor.moveToPosition(gmailPosition);
				return  userCursor.getString(userCursor.getColumnIndex("_id"));
			}
		}
		return null;
	}

	//得到gmail邮箱对应的user Offset
	private int getGmailPosition()
	{
		Cursor userCursor = getContentResolver().query(Uri.parse(calanderURL), null, 
				null, null, null);
		if(userCursor.getCount()>0)
		{
			for(int i=0;i<userCursor.getCount();i++)
			{
				userCursor.moveToPosition(i);
				String userName = userCursor.getString(userCursor.getColumnIndex("name"));
				if(userName.endsWith("@gmail.com"))
				{
					return i;
				}
			}
		}
		Toast.makeText(this, getString(R.string.account_not_exist), Toast.LENGTH_SHORT).show();
		return -1;
	}*/

	//后退
	private void back()
	{
		this.finish();
	}
}
