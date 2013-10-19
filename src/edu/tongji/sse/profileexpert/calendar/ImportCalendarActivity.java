package edu.tongji.sse.profileexpert.calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import edu.tongji.sse.profileexpert.R;
import edu.tongji.sse.profileexpert.entity.MyTempMatter;
import edu.tongji.sse.profileexpert.provider.MyProfileTable;
import edu.tongji.sse.profileexpert.util.ContentResolverUtil;
import edu.tongji.sse.profileexpert.util.MyConstant;

@SuppressLint("SimpleDateFormat")
public class ImportCalendarActivity extends ListActivity
{
	private long selected_calendar_id = -1;
	private long time_from = -1;
	private long time_to = -1;
	
	private Cursor cursor = null;
	private ListAdapter adapter = null;
	private Spinner sp_profile = null;
	private Button bt_back = null;
	private Button bt_import = null;
	
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.import_calendar_list);
		
		bt_back = (Button) findViewById(R.id.bt_back);
		bt_import = (Button) findViewById(R.id.bt_import);
		
		Intent intent = getIntent();
		selected_calendar_id = intent.getLongExtra("selected_calendar_id", -1);
		time_from = intent.getLongExtra("time_from", -1);
		time_to = intent.getLongExtra("time_to", -1);
		
		if(selected_calendar_id == -1 || time_from == -1 || time_to == -1)
		{
			Toast.makeText(this, R.string.import_selection_error, Toast.LENGTH_LONG).show();
			this.finish();
		}
		
		ProgressDialog progress = new ProgressDialog(this);
		progress.setTitle(getString(R.string.loading));
		progress.setMessage(getString(R.string.loading_calendar_events));
		progress.setCancelable(false);
		progress.show();
		
		//加载相应事件
		cursor = this.getContentResolver().query(
				Uri.parse(MyConstant.calanderEventURL),
				new String[]{"_id", "calendar_id", "title", "dtstart", "dtend", "description"},
				"calendar_id=? AND " +
						"((dtstart>? AND dtstart<?)" +
							" OR (dtend>? AND dtend<?)" +
							" OR (dtstart<? AND dtend>?))",
				new String[]{String.valueOf(selected_calendar_id),
					String.valueOf(time_from), String.valueOf(time_to),
					String.valueOf(time_from), String.valueOf(time_to),
					String.valueOf(time_from), String.valueOf(time_to)},
				"dtstart ASC");
		
		if(cursor.getCount() <= 0)
		{
			Toast.makeText(this, R.string.import_selection_no_events, Toast.LENGTH_LONG).show();
		}
		else
		{
			startManagingCursor(cursor);
			adapter = new SimpleCursorAdapter(
					this,
					R.layout.import_calendar_list_item,
					cursor,
					new String[]{"_id", "title", "dtstart", "dtend", "description"},
					new int[]{R.id.item_sp_profile, R.id.item_title, R.id.item_time_from, R.id.item_time_to, R.id.item_description});

			final Cursor profileCursor = this.getContentResolver().query(MyProfileTable.CONTENT_URI, null, null, null, null);
			startManagingCursor(profileCursor);

			((SimpleCursorAdapter) adapter).setViewBinder(new ViewBinder()
			{
				public boolean setViewValue(View aView, Cursor aCursor, int aColumnIndex)
				{
					if(aColumnIndex == 0)
					{
						sp_profile = (Spinner) aView;
						SpinnerAdapter adapter = new SimpleCursorAdapter(
								ImportCalendarActivity.this,
								android.R.layout.simple_spinner_dropdown_item,
								profileCursor,
								new String[]{MyProfileTable.NAME},
								new int[]{android.R.id.text1});
						sp_profile.setAdapter(adapter);
						return true;
					}
					else if (aColumnIndex == 3 || aColumnIndex == 4) {
						long date = aCursor.getLong(aColumnIndex);
						TextView textView = (TextView) aView;
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
						textView.setText(format.format(new Date(date)));
						return true;
					}
					return false;
				}
			});

			setListAdapter(adapter);
		}
		
		progress.dismiss();
		
		// 设置监听器
		bt_back.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) {
				ImportCalendarActivity.this.finish();
			}
		});
		bt_import.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) {
				getAndSaveCheckedEvents();
			}
		});
	}

	protected void getAndSaveCheckedEvents()
	{
		ProgressDialog progress = new ProgressDialog(this);
		progress.setTitle(getString(R.string.loading));
		progress.setMessage(getString(R.string.loading_calendar_events));
		progress.setCancelable(false);
		progress.show();
		
		List<MyTempMatter> importEventsList = new ArrayList<MyTempMatter>();
		ListView list = this.getListView();
		for(int i=0;i<list.getChildCount();i++)
		{
			ViewGroup group = (ViewGroup) list.getChildAt(i);
			CheckBox cb_select = (CheckBox) group.getChildAt(0);
			if(cb_select.isChecked())
			{
				TextView tv_title = (TextView) group.getChildAt(1);
				String title = (String) tv_title.getText();
				
				TextView tv_time_from = (TextView) group.getChildAt(2);
				String time_from = (String) tv_time_from.getText();
				
				TextView tv_time_to = (TextView) group.getChildAt(4);
				String time_to = (String) tv_time_to.getText();
				
				Spinner sp_profile = (Spinner) group.getChildAt(5);
				long profile_id = sp_profile.getSelectedItemId();
				
				TextView tv_description = (TextView) group.getChildAt(6);
				String description = (String) tv_description.getText();
				
				String show_string = ContentResolverUtil.getShowStringForTempMatter(
						this, time_from, time_to, title, profile_id);
				
				try {
					MyTempMatter event = new MyTempMatter(title, time_from, time_to, description, profile_id, show_string);
					importEventsList.add(event);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		
		if(importEventsList.size() == 0)
		{
			progress.dismiss();
			Toast.makeText(this, R.string.error_no_selected_item, Toast.LENGTH_SHORT).show();
			return;
		}
		else
		{
			for (MyTempMatter event : importEventsList)
			{
				event.save(this);
			}
			progress.dismiss();
			Toast.makeText(this, R.string.import_successful, Toast.LENGTH_LONG).show();
			setResult(RESULT_OK);
			this.finish();
		}
	}
}
