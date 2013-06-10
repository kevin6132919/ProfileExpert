package edu.tongji.sse.profileexpert.main;

import java.util.Calendar;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import edu.tongji.sse.profileexpert.R;

public class RoutineActivity extends Activity
{
	private TextView tv_1 = null;
	private TextView tv_2 = null;
	private TextView tv_3 = null;
	private TextView tv_4 = null;
	private TextView tv_5 = null;
	private TextView tv_6 = null;
	private TextView tv_7 = null;
	private Calendar c = Calendar.getInstance();
	private String[] weekdays = null;
		

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
		
	}

	//初始化周一至周日
	private void initWeekdays()
	{
		c.setTimeInMillis(System.currentTimeMillis());
		int today = c.get(Calendar.DAY_OF_WEEK);
		int[] days = getDayOfWeek(today);
		tv_1.setText(weekdays[days[0]]);
		tv_2.setText(weekdays[days[1]]);
		tv_3.setText(weekdays[days[2]]);
		tv_4.setText(weekdays[days[3]]);
		tv_5.setText(weekdays[days[4]]);
		tv_6.setText(weekdays[days[5]]);
		tv_7.setText(weekdays[days[6]]);
		
		tv_4.setBackgroundColor(Color.rgb(196, 227, 183));
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
	
}
