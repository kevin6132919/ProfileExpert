package edu.tongji.sse.profileexpert.main;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import edu.tongji.sse.profileexpert.R;
import edu.tongji.sse.profileexpert.pe.MyProfile;
import edu.tongji.sse.profileexpert.util.MyConstant;

public class ProfileActivity extends ListActivity
{
	public static final String MY_PROFILE_KEY = "my_profile";
	
	private Button bt_new_profile = null;
	private MyProfile my_profile = null;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_profile_display);
        
        bt_new_profile = (Button) findViewById(R.id.bt_add_profile);
        bt_new_profile.setOnClickListener(new OnClickListener() 
        {
			public void onClick(View v) {
				//跳转到自定义模式界面
				Intent intent=new Intent();
	            intent.setClass(ProfileActivity.this, CreateProfileActivity.class);
	            intent.putExtra(MY_PROFILE_KEY, my_profile);
	            startActivityForResult(intent, MyConstant.REQUEST_CODE_MY_PROFILE); 
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == MyConstant.REQUEST_CODE_MY_PROFILE) {  
            if (resultCode == RESULT_OK) {  
                MyProfile mp = (MyProfile) data.getParcelableExtra(MY_PROFILE_KEY);
                Toast.makeText(
                		ProfileActivity.this,
                		mp.toString(),
                		Toast.LENGTH_LONG).show();
            }  
        }
	}

}
