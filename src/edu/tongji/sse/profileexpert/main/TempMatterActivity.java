package edu.tongji.sse.profileexpert.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.MonthDisplayHelper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import edu.tongji.sse.profileexpert.R;
import edu.tongji.sse.profileexpert.calendar.MyCalendarView;
import edu.tongji.sse.profileexpert.calendar.MyCell;
import edu.tongji.sse.profileexpert.calendar.OnCellTouchListener;
import edu.tongji.sse.profileexpert.util.MyConstant;

public class TempMatterActivity extends /*List*/Activity implements OnCellTouchListener
{
	/*private static final String HOUR_NUMBER_KEY = "hourNum";*/
	private MyCalendarView calendar = null;
	
	Paint paint = new Paint(Paint.SUBPIXEL_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.temp_matter);

		calendar = (MyCalendarView) findViewById(R.id.my_calendar);
		calendar.setOnCellTouchListener(this);
		
		checkSelected();
		
		drawMatterList();
		
	}
	
	//绘制事项列表
	private void drawMatterList()
	{
		TextView tv_day = (TextView) findViewById(R.id.tv_day);
		TextView tv_5 = (TextView) findViewById(R.id.tv_hour_num_5); 
		TextView tv_8 = (TextView) findViewById(R.id.tv_hour_num_8);
		TextView tv_hour_num_6  = (TextView) findViewById(R.id.tv_hour_num_6);

		RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl_hour_list);
		
		tv_day.setText(calendar.getShowDay());
		
		TextView tv = new TextView(this);
		tv.setText("子丑寅卯辰巳午未申酉戌亥");
		
		RelativeLayout.LayoutParams lp
			= new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		lp.addRule(RelativeLayout.BELOW,  tv_5.getId());
		lp.addRule(RelativeLayout.ABOVE,  tv_8.getId());
		lp.addRule(RelativeLayout.RIGHT_OF,  tv_hour_num_6.getId());
		lp.leftMargin = 5;
		lp.rightMargin = 5;
		lp.topMargin = 5;
		lp.bottomMargin = 5;
		tv.setLayoutParams(lp);
		
		tv.setBackgroundResource(R.drawable.red_rectangle_background);
		tv.setGravity(Gravity.CENTER);
		
		rl.addView(tv);
	}

	//检查是否已选择日期
	private void checkSelected()
	{
		Intent intent = getIntent();
		boolean selected = intent.getBooleanExtra("selected", false);
		if(selected)
		{
			int year = intent.getIntExtra("selected_year", -1);
			int month = intent.getIntExtra("selected_month", -1);
			int day = intent.getIntExtra("selected_day", -1);
			if(year==-1 || month==-1 || day==-1)
				return;
			calendar.setDate(year, month, day);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.temp_matter, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item)
	{
		switch(item.getItemId())
		{
		case R.id.action_add_temp_matter:
			//跳转到设置界面
			Intent intent=new Intent();
			intent.setClass(TempMatterActivity.this, CreateTempMatterActivity.class);
			startActivityForResult(intent, MyConstant.REQUEST_CODE_CREATE_TEMP_MATTER);
			return true;
		default:
			return false;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == MyConstant.REQUEST_CODE_CREATE_TEMP_MATTER)
		{
			if (resultCode == RESULT_OK)
			{
				draw();
			}
		}
	}

	private void draw()
	{
	}

	@Override
	public void onTouch(MyCell cell)
	{
		MonthDisplayHelper helper = 
				new MonthDisplayHelper(calendar.getYear(), calendar.getMonth());
		int color = cell.getPaint().getColor();
		if(color == Color.GRAY)
			helper.previousMonth();
		else if(color == Color.LTGRAY)
			helper.nextMonth();

		int year = helper.getYear();
		int month = helper.getMonth();
		int day = cell.getDayOfMonth();
		calendar.setDate(year, month, day);
		drawMatterList();
	}
}
