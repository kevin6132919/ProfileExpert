package edu.tongji.sse.profileexpert.help;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.view.WindowManager;
import edu.tongji.sse.profileexpert.R;

public class HelpActivity extends Activity
{
	private static final int ALBUM_COUNT = 8;
	
	private static final int ALBUM_RES[] = {
		R.drawable.help_1,R.drawable.help_2,R.drawable.help_3,
		R.drawable.help_4,R.drawable.help_5,R.drawable.help_6,
		R.drawable.help_7,R.drawable.help_8
	};
    
	private ViewPager viewPager;
	private ViewPagerAdapter viewPagerAdapter;
	private JSONArray jsonArray;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.help);
        
        setupViews();
    }
    
    private void setupViews(){    
    	//初始化JSonArray,给ViewPageAdapter提供数据源用.
    	jsonArray = new JSONArray();
    	for(int i = 0;i<ALBUM_COUNT; i++){
    		JSONObject object = new JSONObject();
    		try {
				object.put("resid", ALBUM_RES[i % ALBUM_RES.length]);
				object.put("name", (i+1) + "/" + ALBUM_COUNT);
	    		jsonArray.put(object);
			} catch (JSONException e) {
				e.printStackTrace();
			}
    		
    	}    	
    	viewPager = (ViewPager)findViewById(R.id.viewpager);
    	viewPagerAdapter = new ViewPagerAdapter(this, jsonArray);
    	viewPager.setAdapter(viewPagerAdapter);
    }
}  
