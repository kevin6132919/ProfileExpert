package edu.tongji.sse.profileexpert.control;

import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.graphics.Color;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import edu.tongji.sse.profileexpert.R;
 
public class MyTimeSpinner extends Spinner
{
    public MyTimeSpinner(Context context){
        super(context);
    }
 
    public MyTimeSpinner(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        if (isInEditMode()) {
            return;
        }
        final Time time = new Time();
        time.setToNow();
        //为MyDateSpinner设置adapter，主要用于显示spinner的text值
        MyTimeSpinner.this.setAdapter(new BaseAdapter()
        {
            @Override
            public int getCount() {
                return 1;
            }
 
            @Override
            public Object getItem(int arg0)
            {
                return formatHourAndMinute(time.hour,time.minute);
            }
 
            @Override
            public long getItemId(int arg0) {
                return 0;
            }
 
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                TextView text = new TextView(MyTimeSpinner.this.getContext());
                text.setTextSize(getResources().getDimension(R.dimen.time_text_size));
                text.setText(formatHourAndMinute(time.hour,time.minute));
                text.setTextColor(Color.BLACK);
                return text;
            }
        });
    }
 
    @Override
    public boolean performClick()
    {
        Time time = new Time();
        time.setToNow();
        TimePickerDialog tpd = new TimePickerDialog(
        		getContext(),
        		new OnTimeSetListener()
        		{
					@Override
					public void onTimeSet(TimePicker view, final int hourOfDay, final int minute)
					{
						//为MyDateSpinner设置adapter，主要用于显示spinner的text值
				        MyTimeSpinner.this.setAdapter(new BaseAdapter()
				        {
				            @Override
				            public int getCount() {
				                return 1;
				            }
				 
				            @Override
				            public Object getItem(int arg0)
				            {
				                return formatHourAndMinute(hourOfDay,minute);
				            }
				 
				            @Override
				            public long getItemId(int arg0) {
				                return 0;
				            }
				 
				            @Override
				            public View getView(int position, View convertView, ViewGroup parent)
				            {
				                TextView text = new TextView(MyTimeSpinner.this.getContext());
				                text.setTextSize(getResources().getDimension(R.dimen.time_text_size));
				                text.setText(formatHourAndMinute(hourOfDay,minute));
				                text.setTextColor(Color.BLACK);
				                return text;
				            }
				        });
					}
				},
        		time.hour,
        		time.minute,
        		true);
        tpd.show();
        return true;
    }
    
    public static String formatHourAndMinute(int hourOfDay, int minute)
    {
    	String str = "";
    	if(hourOfDay < 10)
    		str += "0"+hourOfDay;
    	else
    		str += hourOfDay;
    	str += ":";
    	if(minute < 10)
    		str += "0"+minute;
    	else
    		str += minute;
    	return str;
    }
}
