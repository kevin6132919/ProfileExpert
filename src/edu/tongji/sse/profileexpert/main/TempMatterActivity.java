package edu.tongji.sse.profileexpert.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import edu.tongji.sse.profileexpert.R;
import edu.tongji.sse.profileexpert.util.MyConstant;

public class TempMatterActivity extends /*List*/Activity
{
	/*private static final String HOUR_NUMBER_KEY = "hourNum";*/

	Paint paint = new Paint(Paint.SUBPIXEL_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.temp_matter);

		TextView tv_5 = (TextView) findViewById(R.id.tv_hour_num_5); 
		TextView tv_8 = (TextView) findViewById(R.id.tv_hour_num_8);
		TextView tv_hour_num_6  = (TextView) findViewById(R.id.tv_hour_num_6);

		RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl_hour_list);

		
		TextView tv = new TextView(this);
		tv.setText("◊”≥Û“˙√Æ≥ΩÀ»ŒÁŒ¥…Í”œ–Á∫•");
		
		RelativeLayout.LayoutParams lp
			= new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		lp.addRule(RelativeLayout.BELOW,  tv_5.getId());
		lp.addRule(RelativeLayout.ABOVE,  tv_8.getId());
		lp.addRule(RelativeLayout.RIGHT_OF,  tv_hour_num_6.getId());
		lp.leftMargin = 5;
		lp.rightMargin = 5;
		lp.topMargin = 5;
		lp.bottomMargin = 5;
		tv.setLayoutParams(lp);
		
		tv.setBackgroundResource(R.drawable.red_rectangle_background);
		tv.setGravity(Gravity.CENTER);
		
		rl.addView(tv);
		
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
			//Ã¯◊™µΩ…Ë÷√ΩÁ√Ê
			Intent intent=new Intent();
			intent.setClass(TempMatterActivity.this, CreateTempMatterActivity.class);
			startActivityForResult(intent, MyConstant.REQUEST_CODE_CREATE_TEMP_MATTER);
			return true;
		default:
			return false;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == MyConstant.REQUEST_CODE_CREATE_TEMP_MATTER)
		{
			if (resultCode == RESULT_OK)
			{
				draw();
			}
		}
	}

	private void draw()
	{
	}
}
