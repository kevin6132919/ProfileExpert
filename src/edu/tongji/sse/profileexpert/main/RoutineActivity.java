package edu.tongji.sse.profileexpert.main;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import edu.tongji.sse.profileexpert.R;

public class RoutineActivity extends Activity
{
	public static final String WEEKDAY_SELECTED = "weekday_selected"; 
	
	private TextView tv_1 = null;
	private TextView tv_2 = null;
	private TextView tv_3 = null;
	private TextView tv_4 = null;
	private TextView tv_5 = null;
	private TextView tv_6 = null;
	private TextView tv_7 = null;
	private Calendar c = Calendar.getInstance();
	private String[] weekdays = null;
	private int days[] = null;
	
	private int current_selected = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.routine);
		
		findViews();
		
		weekdays = new String[]{
				getString(R.string.Monday),//0
				getString(R.string.Tuesday),//1
				getString(R.string.Wednesday),//2
				getString(R.string.Thursday),//3
				getString(R.string.Friday),//4
				getString(R.string.Saturday),//5
				getString(R.string.Sunday)//6
		};
		
		initWeekdays();

		select(3);
	}

	//初始化周一至周日
	private void initWeekdays()
	{
		c.setTimeInMillis(System.currentTimeMillis());
		int today = c.get(Calendar.DAY_OF_WEEK);
		days = getDayOfWeek(today);
		
		for(int i=0;i<7;i++)
		{
			final int _i = i;
			TextView tv = getWeekdayTextView(i);
			tv.setText(weekdays[days[i]]);
			tv.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) {
					select(_i);
				}
			});
		}
	}

	private TextView getWeekdayTextView(int index)
	{
		switch(index)
		{
		case 0:
			return tv_1;
		case 1:
			return tv_2;
		case 2:
			return tv_3;
		case 3:
			return tv_4;
		case 4:
			return tv_5;
		case 5:
			return tv_6;
		default:
			return tv_7;
		}
	}
	
	private void select(int index)
	{
		if(current_selected == index)
			return;
		for(int i=0;i<7;i++)
		{
			getWeekdayTextView(i).setBackgroundColor(Color.TRANSPARENT);
		}
		getWeekdayTextView(index).setBackgroundColor(Color.rgb(196, 227, 183));
		current_selected = index;
	}

	private int[] getDayOfWeek(int dayOfWeek)
	{
		switch(dayOfWeek)
		{
		case Calendar.MONDAY:
			return new int[]{4,5,6,0,1,2,3};
		case Calendar.TUESDAY:
			return new int[]{5,6,0,1,2,3,4};
		case Calendar.WEDNESDAY:
			return new int[]{6,0,1,2,3,4,5};
		case Calendar.THURSDAY:
			return new int[]{0,1,2,3,4,5,6};
		case Calendar.FRIDAY:
			return new int[]{1,2,3,4,5,6,0};
		case Calendar.SATURDAY:
			return new int[]{2,3,4,5,6,0,1};
		default:
			return new int[]{3,4,5,6,0,1,2};
		}
	}
	
	private void findViews()
	{
		tv_1 = (TextView) findViewById(R.id.tv_1);
		tv_2 = (TextView) findViewById(R.id.tv_2);
		tv_3 = (TextView) findViewById(R.id.tv_3);
		tv_4 = (TextView) findViewById(R.id.tv_4);
		tv_5 = (TextView) findViewById(R.id.tv_5);
		tv_6 = (TextView) findViewById(R.id.tv_6);
		tv_7 = (TextView) findViewById(R.id.tv_7);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.routine, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item)
	{
		switch(item.getItemId())
		{
		case R.id.action_add_routine:
			//跳转到新增日程界面
			Intent intent=new Intent();
			intent.setClass(RoutineActivity.this, CreateRoutineActivity.class);
			intent.putExtra(WEEKDAY_SELECTED, days[current_selected]);
			startActivity(intent);
			return true;
		default:
			return false;
		}
	}
}
