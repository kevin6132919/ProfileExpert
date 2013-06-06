package edu.tongji.sse.profileexpert.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import edu.tongji.sse.profileexpert.R;
import edu.tongji.sse.profileexpert.control.MyDateSpinner;
import edu.tongji.sse.profileexpert.control.MyTimeSpinner;

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

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_temp_matter);


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
			Toast.makeText(this, "标题不能为空!", Toast.LENGTH_SHORT).show();
			return;
		}
	}

	//后退
	private void back()
	{
		this.finish();
	}
}
