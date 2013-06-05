package edu.tongji.sse.profileexpert.control;

import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.graphics.Color;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
 
public class MyDateSpinner extends Spinner {
    public MyDateSpinner(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }
 
    public MyDateSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {
            return;
        }
        final Time time = new Time();
        time.setToNow();
        //为MyDateSpinner设置adapter，主要用于显示spinner的text值
        MyDateSpinner.this.setAdapter(new BaseAdapter() {
 
            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return 1;
            }
 
            @Override
            public Object getItem(int arg0) {
                // TODO Auto-generated method stub
                return null;
            }
 
            @Override
            public long getItemId(int arg0) {
                // TODO Auto-generated method stub
                return 0;
            }
 
            @Override
            public View getView(int arg0, View arg1, ViewGroup arg2) {
                // TODO Auto-generated method stub
                TextView text = new TextView(MyDateSpinner.this.getContext());
                text.setText(time.year
                        + "-"
                        + (time.month + 1)
                        + "-"
                        + time.monthDay
                        + MyDatePickerDialog.CaculateWeekDay(time.year,
                                time.month+1, time.monthDay));
                text.setTextColor(Color.BLACK);
                return text;
            }
        });
    }
 
    @Override
    public boolean performClick() {
        Time time = new Time();
        time.setToNow();
        MyDatePickerDialog tpd = new MyDatePickerDialog(getContext(),
                new OnDateSetListener() {
 
                    @Override
                    public void onDateSet(DatePicker view, final int year,
                            final int month, final int day) {
                        // TODO Auto-generated method stub
                        //为MyDateSpinner动态设置adapter，主要用于修改spinner的text值
                        MyDateSpinner.this.setAdapter(new BaseAdapter() {
 
                            @Override
                            public int getCount() {
                                // TODO Auto-generated method stub
                                return 1;
                            }
 
                            @Override
                            public Object getItem(int arg0) {
                                // TODO Auto-generated method stub
                                return null;
                            }
 
                            @Override
                            public long getItemId(int arg0) {
                                // TODO Auto-generated method stub
                                return 0;
                            }
 
                            @Override
                            public View getView(int arg0, View arg1,
                                    ViewGroup arg2) {
                                // TODO Auto-generated method stub
                                TextView text = new TextView(MyDateSpinner.this
                                        .getContext());
                                text.setText(year
                                        + "-"
                                        + (month + 1)
                                        + "-"
                                        + day
                                        + MyDatePickerDialog.CaculateWeekDay(
                                                year, month + 1, day));
                                text.setTextColor(Color.BLACK);
                                return text;
                            }
                        });
                    }
 
                }, time.year, time.month, time.monthDay);
        tpd.show();
        return true;
    }
}
