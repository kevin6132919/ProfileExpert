package edu.tongji.sse.profileexpert.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import edu.tongji.sse.profileexpert.R;

public class TempMatterActivity extends ListActivity
{
	private static final String HOUR_NUMBER_KEY = "hourNum";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.temp_matter);

		ListAdapter adapter = new SimpleAdapter(
				this,
				getHourNumMap(),
				R.layout.hour_as_list_item,
				new String[]{HOUR_NUMBER_KEY},
				new int[]{R.id.tv_hour_num});
		setListAdapter(adapter);
	}

	private List<HashMap<String, String>> getHourNumMap()
	{
		List<HashMap<String,String>> items = new ArrayList<HashMap<String,String>>();
		for(int i=0;i<24;i++)
		{
			HashMap<String,String> hm = new HashMap<String, String>();
			hm.put(HOUR_NUMBER_KEY, i+"");
			items.add(hm);
		}
		return items;
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
}
