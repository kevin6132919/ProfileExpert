package edu.tongji.sse.profileexpert.view;

import java.util.Calendar;

import edu.tongji.sse.profileexpert.R;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.MonthDisplayHelper;
import android.widget.ImageView;

public class MyCalendarView extends ImageView
{
	private Context context = null;
	private Calendar rightNow = null;
	MonthDisplayHelper mHelper = null;
	
    Drawable decoration = null;
    Drawable weekTitle = null;
	
	public MyCalendarView(Context context)
	{
		this(context, null);
	}
	public MyCalendarView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public MyCalendarView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		this.context = context;
		decoration = context.getResources().getDrawable(R.drawable.calendar_today);
		initCalendarView();
	}
	
	//≥ı ºªØ
	private void initCalendarView()
	{

		rightNow = Calendar.getInstance();
		// prepare static vars
		Resources res = getResources();
		
		// set background
		setImageResource(R.drawable.calendar_background);
		weekTitle = res.getDrawable(R.drawable.calendar_week);
		
		mHelper = 
				new MonthDisplayHelper(
						rightNow.get(Calendar.YEAR),
						rightNow.get(Calendar.MONTH),
						rightNow.getFirstDayOfWeek()
					);
	}
}
