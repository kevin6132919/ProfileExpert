package edu.tongji.sse.profileexpert.main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.MonthDisplayHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import edu.tongji.sse.profileexpert.R;
import edu.tongji.sse.profileexpert.calendar.MyCalendarView;
import edu.tongji.sse.profileexpert.calendar.MyCell;
import edu.tongji.sse.profileexpert.calendar.OnCellTouchListener;
import edu.tongji.sse.profileexpert.provider.TempMatterTable;
import edu.tongji.sse.profileexpert.util.MyConstant;

@SuppressLint("SimpleDateFormat")
public class TempMatterActivity extends ListActivity implements OnCellTouchListener
{
	/*private static final String HOUR_NUMBER_KEY = "hourNum";*/
	private MyCalendarView mcv_calendar = null;
	private TextView tv_show_day = null;
	private Cursor cursor = null;
	private Calendar calendar = Calendar.getInstance();

	Paint paint = new Paint(Paint.SUBPIXEL_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
	public static final String EDIT_TEMP_MATTER_ID_KEY = "temp_matter_id";

	protected void onCreate(Bundle savedInstanceState)
	{
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.temp_matter);

			mcv_calendar = (MyCalendarView) findViewById(R.id.my_calendar);
			mcv_calendar.setOnCellTouchListener(this);

			checkSelected();

			updateShowDay();
			loadMatters();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	//加载已添加的当日的事项
	@SuppressWarnings("deprecation")
	private void loadMatters() throws ParseException
	{
		//得到选择的那一天的开始和结束时间
		String show_day = mcv_calendar.getShowDay();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		long showTime = format.parse(show_day).getTime();
		calendar.setTimeInMillis(showTime);
		
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		long from = calendar.getTimeInMillis();
		
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		long to = calendar.getTimeInMillis();
		
		cursor = this.getContentResolver().query(
				TempMatterTable.CONTENT_URI,
				null,
				"("+TempMatterTable.TIME_FROM + ">=? AND "
						+ TempMatterTable.TIME_FROM + "<=?)"
						+ " OR (" + TempMatterTable.TIME_TO + ">=? AND "
						+ TempMatterTable.TIME_TO + "<=?)",
				new String[]{""+from,""+to,""+from,""+to},
				TempMatterTable.TIME_FROM + " ASC");
		
		startManagingCursor(cursor);
		ListAdapter adapter = new SimpleCursorAdapter(
				this,
				R.layout.matter_list_item,
				cursor,
				new String[]{TempMatterTable.SHOW_STRING},
				new int[]{R.id.bt_matter});
		setListAdapter(adapter);

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, final long id)
	{
		new AlertDialog.Builder(this)
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
							editCorrespondingTempMatter(id);
							break;
						case 1:
							delteCorrespondingTempMatter(id);
							break;
						}
						dialog.dismiss();
					}
				})
				.setNegativeButton(getString(R.string.cancel), null)
				.show();
	}

	//删除对应的临时事项
	private void delteCorrespondingTempMatter(final long id)
	{
		new Builder(TempMatterActivity.this)
		.setIcon(R.drawable.alerts_warning)
		.setMessage(getString(R.string.delete_temp_matter_text))
		.setTitle(getString(R.string.tips))
		.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener(){
			@SuppressWarnings("deprecation")
			public void onClick(DialogInterface dialog, int which)
			{
				String msg = null;
				int result = getContentResolver().delete(
						TempMatterTable.CONTENT_URI,
						TempMatterTable._ID + "=?",
						new String[]{""+id});
				if(result == 1)
					msg = getString(R.string.delete_temp_matter_success);
				else
					msg = getString(R.string.delete_temp_matter_fail);
				Toast.makeText(TempMatterActivity.this, msg, Toast.LENGTH_SHORT).show();

				//刷新当前listView
				cursor.requery();
				dialog.dismiss();
			}})
			.setNegativeButton(getString(R.string.cancel), null)
			.show();
	}

	//编辑对应的临时事项
	private void editCorrespondingTempMatter(long id)
	{
		//跳转到编辑临时事项界面
		Intent intent=new Intent();
		intent.setClass(TempMatterActivity.this, EditTempMatterActivity.class);
		intent.putExtra(EDIT_TEMP_MATTER_ID_KEY, id);
		startActivityForResult(intent, MyConstant.REQUEST_CODE_EDIT_PROFILE); 
	}
	//绘制事项列表
	private void updateShowDay()
	{
		tv_show_day = (TextView) findViewById(R.id.tv_day);
		tv_show_day.setText(mcv_calendar.getShowDay());
		
		try {
			loadMatters();
		} catch (ParseException e) {
			e.printStackTrace();
		}
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
			mcv_calendar.setDate(year, month, day);
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
			startActivity(intent);
			return true;
		default:
			return false;
		}
	}

	@Override
	public void onTouch(MyCell cell)
	{
		MonthDisplayHelper helper = 
				new MonthDisplayHelper(mcv_calendar.getYear(), mcv_calendar.getMonth());
		int color = cell.getPaint().getColor();
		if(color == Color.GRAY)
			helper.previousMonth();
		else if(color == Color.LTGRAY)
			helper.nextMonth();

		int year = helper.getYear();
		int month = helper.getMonth();
		int day = cell.getDayOfMonth();
		mcv_calendar.setDate(year, month, day);
		updateShowDay();
	}
}
