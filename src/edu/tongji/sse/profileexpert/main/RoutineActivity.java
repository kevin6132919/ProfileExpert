package edu.tongji.sse.profileexpert.main;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import edu.tongji.sse.profileexpert.R;
import edu.tongji.sse.profileexpert.entity.DrawableRoutine;
import edu.tongji.sse.profileexpert.provider.RoutineTable;
import edu.tongji.sse.profileexpert.util.MyConstant;

public class RoutineActivity extends Activity
{
	public static final String WEEKDAY_SELECTED = "weekday_selected";
	public static final String EDIT_ROUTINE_ID_KEY = "edit_routine_id";

	private TextView tv_1 = null;
	private TextView tv_2 = null;
	private TextView tv_3 = null;
	private TextView tv_4 = null;
	private TextView tv_5 = null;
	private TextView tv_6 = null;
	private TextView tv_7 = null;
	private LinearLayout ll_routine = null;
	private Calendar c = Calendar.getInstance();
	private Cursor cursor = null;
	private String[] weekdays = null;
	List<DrawableRoutine> routineList = null;
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

		/*TextView tv = new TextView(this);
		tv.setText("test");
		tv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1));
		ll_routine.addView(tv);*/
		//ll_routine.postInvalidate();
		select(3);
	}

	//绘制日程到屏幕
	private void drawRoutine()
	{
		getCursor();

		routineList = new ArrayList<DrawableRoutine>();
		ll_routine.removeAllViews();
		while(cursor.moveToNext())
		{
			long id = cursor.getLong(cursor.getColumnIndex(RoutineTable._ID));
			String time_from = cursor.getString(cursor.getColumnIndex(RoutineTable.TIME_FROM));
			String time_to = cursor.getString(cursor.getColumnIndex(RoutineTable.TIME_TO));
			boolean is_same_day = "1".equals(cursor.getString(
					cursor.getColumnIndex(RoutineTable.IS_SAME_DAY)));
			int start_day = cursor.getInt(cursor.getColumnIndex(RoutineTable.START_DAY));
			String showString = cursor.getString(cursor.getColumnIndex(RoutineTable.SHOW_STRING));
			DrawableRoutine dr = getRoutine(id, time_from, time_to,
					is_same_day, start_day, showString);

			routineList.add(dr);
		}

		drawByList();
	}

	private void drawByList()
	{
		Collections.sort(routineList);
		int nowDrawing = DrawableRoutine.START_MINUTES;
		for(int i=0;i<routineList.size();i++)
		{
			DrawableRoutine dr = routineList.get(i);
			if(dr.getStatus() != DrawableRoutine.NORMAL)
				continue;
			else
			{
				if(dr.getStart() > nowDrawing)
				{
					ll_routine.addView(newBlankTextView(dr.getStart() - nowDrawing));
					ll_routine.addView(newRoutineButton(dr));
					nowDrawing = dr.getEnd();
				}
				else
				{
					ll_routine.addView(newRoutineButton(dr));
					nowDrawing = dr.getEnd();
				}
			}
		}

		if(nowDrawing < DrawableRoutine.END_MINUTES)
		{
			ll_routine.addView(newBlankTextView(DrawableRoutine.END_MINUTES - nowDrawing));
		}

		//ll_routine.postInvalidate();
	}

	private Button newRoutineButton(final DrawableRoutine dr)
	{
		Button bt = new Button(this);
		int interval = dr.getEnd() - dr.getStart();
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, 0, interval);
		lp.gravity = Gravity.CENTER;
		bt.setLayoutParams(lp);
		bt.setText(dr.getShowString());
		//bt.setTextAppearance(this, android.R.attr.textAppearanceSmall);
		if(interval>60)
			bt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		else
			bt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
		bt.setBackgroundResource(R.drawable.routine_style);
		bt.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				showSelectDialog(dr.getId());
			}
		});
		return bt;
	}

	private void showSelectDialog(final long id)
	{
		new AlertDialog.Builder(RoutineActivity.this)
		.setIcon(android.R.drawable.ic_menu_info_details)
		.setTitle(getString(R.string.select))
		.setItems(
				new String[] { getString(R.string.edit), getString(R.string.delete) },
				new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						switch(which)
						{
						case 0:
							editCorrespondingRoutine(id);
							break;
						case 1:
							delteCorrespondingRoutine(id);
							break;
						}
						dialog.dismiss();
					}
				})
				.setNegativeButton(getString(R.string.cancel), null)
				.show();
	}

	private void delteCorrespondingRoutine(final long id)
	{
		new Builder(this)
		.setIcon(R.drawable.alerts_warning)
		.setMessage(getString(R.string.delete_routine_text))
		.setTitle(getString(R.string.tips))
		.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which)
			{
				String msg = null;
				int result = getContentResolver().delete(
						RoutineTable.CONTENT_URI,
						RoutineTable._ID + "=?",
						new String[]{""+id});
				if(result == 1)
				{
					MainActivity.rm.rearrange(RoutineActivity.this);
					msg = getString(R.string.delete_routine_success);
				}
				else
					msg = getString(R.string.delete_routine_fail);
				Toast.makeText(RoutineActivity.this, msg, Toast.LENGTH_SHORT).show();

				drawRoutine();
				dialog.dismiss();
			}})
			.setNegativeButton(getString(R.string.cancel), null)
			.show();
	}

	private void editCorrespondingRoutine(long id)
	{
		//跳转到编辑临时事项界面
		Intent intent=new Intent();
		intent.setClass(RoutineActivity.this, EditRoutineActivity.class);
		intent.putExtra(EDIT_ROUTINE_ID_KEY, id);
		startActivityForResult(intent, MyConstant.REQUEST_CODE_EDIT_ROUTINE); 
	}
	
	private TextView newBlankTextView(int interval)
	{
		TextView tv = new TextView(this);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, 0, interval);
		tv.setLayoutParams(lp);
		return tv;
	}

	private DrawableRoutine getRoutine(long id, String time_from, String time_to,
			boolean is_same_day, int start_day, String showString)
	{
		int hour = -1, minute = -1;
		hour = Integer.parseInt(time_from.substring(0,2));
		minute = Integer.parseInt(time_from.substring(3));
		int from = hour * 60 + minute;

		hour = Integer.parseInt(time_to.substring(0,2));
		minute = Integer.parseInt(time_to.substring(3));
		int to = hour * 60 + minute;
		int start = -1, end = -1 ,status = DrawableRoutine.NORMAL;

		if(days[current_selected] != start_day)
		{
			if(to > DrawableRoutine.START_DRAW_MINUTES)
			{
				if(to>DrawableRoutine.END_MINUTES)
				{
					start = DrawableRoutine.START_MINUTES;
					end = DrawableRoutine.END_MINUTES;
				}
				else
				{
					start = DrawableRoutine.START_MINUTES;
					end = to;
				}
			}
			else
			{
				start = 0;
				end = DrawableRoutine.START_MINUTES;
				status = DrawableRoutine.TOP;
			}
		}
		else if(from < DrawableRoutine.START_MINUTES)
		{
			if(to > DrawableRoutine.START_DRAW_MINUTES)
			{
				if(to>DrawableRoutine.END_MINUTES || !is_same_day)
				{
					start = DrawableRoutine.START_MINUTES;
					end = DrawableRoutine.END_MINUTES;
				}
				else
				{
					start = DrawableRoutine.START_MINUTES;
					end = to;
				}
			}
			else
			{
				start = from;
				end = to;
				status = DrawableRoutine.TOP;
			}
		}
		else if(to > DrawableRoutine.END_MINUTES)
		{
			if(from < DrawableRoutine.END_DRAW_MINUTES)
			{
				start = from;
				end = DrawableRoutine.END_MINUTES;
			}
			else
			{
				start = from;
				end = to;
				status = DrawableRoutine.BOTTOM;
			}
		}
		else if(from > DrawableRoutine.END_DRAW_MINUTES)
		{
			start = from;
			if(is_same_day)
				end = to;
			else
				end = DrawableRoutine.REAL_END_MINUTES;
			status = DrawableRoutine.BOTTOM;
		}
		else if(!is_same_day)
		{
			start = from;
			end = DrawableRoutine.END_MINUTES;
		}
		else
		{
			start = from;
			end = to;
		}

		return new DrawableRoutine(id, start, end, status, showString);
	}

	private void getCursor()
	{
		int today = days[current_selected];
		cursor = this.getContentResolver().query(
				RoutineTable.CONTENT_URI,
				null,
				"("+RoutineTable.START_DAY + "=? "
						+ " OR (" + RoutineTable.IS_SAME_DAY + "=? AND "+ RoutineTable.START_DAY + "=?" +
						")" +
						")",
						new String[]{""+today,""+0,""+getYesterday(today)},
						RoutineTable.START_DAY + " ASC ," + RoutineTable.TIME_FROM + " ASC");
	}

	//得到前一天
	private int getYesterday(int today)
	{
		if(today > 0)
			return today - 1;
		else
			return 6;
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
		drawRoutine();
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

	private void showRoutineListInDialog()
	{
		String[] items = getRoutineItems();

		if(items.length == 0)
		{
			Toast.makeText(RoutineActivity.this, getString(R.string.no_routine), Toast.LENGTH_SHORT).show();
			return;
		}

		new AlertDialog.Builder(this)
		.setIcon(R.drawable.list_item_black)
		.setTitle(weekdays[days[current_selected]])
		.setItems(
				items,
				new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						showSelectDialog(routineList.get(which).getId());
						dialog.dismiss();
					}
				})
				.setNegativeButton(getString(R.string.cancel), null)
				.show();
	}

	private String[] getRoutineItems()
	{
		String[] items = new String[routineList.size()];
		for(int i=0;i<items.length;i++)
		{
			items[i] = routineList.get(i).getShowString();
		}
		return items;
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
		ll_routine = (LinearLayout) findViewById(R.id.ll_routine);
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
			startActivityForResult(intent,MyConstant.REQUEST_CODE_CREATE_ROUTINE);
			return true;
		case R.id.action_routine_list:
			showRoutineListInDialog();
			return true;
		default:
			return false;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == MyConstant.REQUEST_CODE_CREATE_ROUTINE)
		{
			if (resultCode == RESULT_OK)
			{
				MainActivity.rm.rearrange(this);
				drawRoutine();
			}
		}
		else if(requestCode == MyConstant.REQUEST_CODE_EDIT_ROUTINE)
		{
			if (resultCode == RESULT_OK)
			{
				MainActivity.rm.rearrange(this);
				drawRoutine();
			}
		}
	}
}
