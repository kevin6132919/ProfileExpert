package edu.tongji.sse.profileexpert.main;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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
			this.finish();
		}
		
		ArrayAdapter<CharSequence> sp_weekday_from_adapter = ArrayAdapter.createFromResource(
                this, R.array.sp_weekday_from_display,
                R.layout.weekday_select_spinner_item);
		//sp_weekday_from_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_weekday_from.setAdapter(sp_weekday_from_adapter);
		sp_weekday_from.setSelection(weekday_selected);
		
		ArrayAdapter<CharSequence> sp_weekday_to_adapter = ArrayAdapter.createFromResource(
                this, R.array.sp_weekday_to_display,
                R.layout.weekday_select_spinner_item);
		sp_weekday_to_adapter.setDropDownViewResource(R.layout.weekday_select_spinner_dropdown_item);
		sp_weekday_to.setAdapter(sp_weekday_to_adapter);
		
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
