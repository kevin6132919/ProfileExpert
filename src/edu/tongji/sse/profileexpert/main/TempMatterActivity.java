package edu.tongji.sse.profileexpert.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
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
}
