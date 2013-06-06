package edu.tongji.sse.profileexpert.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
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
		TextView tv_7 = (TextView) findViewById(R.id.tv_hour_num_7);
		
		Canvas canvas = new Canvas();
		paint.setAntiAlias(true);                       //设置画笔为无锯齿  
	    paint.setColor(Color.BLACK);                    //设置画笔颜色  
	    canvas.drawColor(Color.WHITE);                  //白色背景  
	    paint.setStrokeWidth((float) 3.0);              //线宽  
	    paint.setStyle(Style.STROKE);                   //空心效果  
	    Rect r1=new Rect();                         //Rect对象  
	    r1.left=tv_5.getLeft();                                 //左边  
	    r1.top=tv_5.getTop();                                  //上边  
	    r1.right=tv_7.getRight();                                   //右边  
	    r1.bottom=tv_7.getBottom();                              //下边  
	    canvas.drawRect(r1, paint);                 //绘制矩形  

	    LinearLayout ll = (LinearLayout) findViewById(R.id.ll_hour_list);
	    canvas.drawText("100,100", 100, 100, paint);
	    canvas.drawText("200,200", 200, 200, paint);
	    canvas.drawText("300,300", 300, 300, paint);
	    canvas.drawText("300,200", 300, 200, paint);
	    canvas.drawText("400,200", 400, 200, paint);
	    canvas.drawText("500,400", 500, 400, paint);
	    ll.draw(canvas);
		/*ListAdapter adapter = new SimpleAdapter(
				this,
				getHourNumMap(),
				R.layout.hour_as_list_item,
				new String[]{HOUR_NUMBER_KEY},
				new int[]{R.id.tv_hour_num});
		
		setListAdapter(adapter);
		
		draw();*/
	}
/*
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
	}*/


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
		Canvas canvas = new Canvas();
		paint.setAntiAlias(true);                       //设置画笔为无锯齿  
	    paint.setColor(Color.BLACK);                    //设置画笔颜色  
	    canvas.drawColor(Color.WHITE);                  //白色背景  
	    paint.setStrokeWidth((float) 3.0);              //线宽  
	    paint.setStyle(Style.STROKE);                   //空心效果  
	    Rect r1=new Rect();                         //Rect对象  
	    r1.left=50;                                 //左边  
	    r1.top=50;                                  //上边  
	    r1.right=450;                                   //右边  
	    r1.bottom=250;                              //下边  
	    canvas.drawRect(r1, paint);                 //绘制矩形  
	    RectF r2=new RectF();                           //RectF对象  
	    r2.left=50;                                 //左边  
	    r2.top=400;                                 //上边  
	    r2.right=450;                                   //右边  
	    r2.bottom=600;                              //下边  
	    canvas.drawRoundRect(r2, 10, 10, paint);        //绘制圆角矩形
	    
	    canvas.drawText("100,100", 100, 100, paint);
	    canvas.drawText("200,200", 200, 200, paint);
	    canvas.drawText("300,300", 300, 300, paint);
	    canvas.drawText("300,200", 300, 200, paint);
	    canvas.drawText("400,200", 400, 200, paint);
	    canvas.drawText("500,400", 500, 400, paint);
		
	}
}
