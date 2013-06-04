package edu.tongji.sse.profileexpert.main;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import edu.tongji.sse.profileexpert.R;
import edu.tongji.sse.profileexpert.entity.MyProfile;
import edu.tongji.sse.profileexpert.provider.MyProfileTable;
import edu.tongji.sse.profileexpert.util.MyConstant;

public class ProfileActivity extends ListActivity
{
	public static final String MY_PROFILE_KEY = "my_profile";
	public static final String EDIT_PROFILE_ID_KEY = "my_profile_id";

	private Button bt_new_profile = null;
	//private MyProfile my_profile = null;
	private Cursor cursor = null;
	private ListAdapter adapter = null;

	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_profile_display);

		//加载已创建的MyProfile
		cursor = this.getContentResolver().query(MyProfileTable.CONTENT_URI, null, null, null, null);
		startManagingCursor(cursor);
		adapter = new SimpleCursorAdapter(
				this,
				R.layout.profile_list_item,
				cursor,
				new String[]{MyProfileTable.NAME, MyProfileTable.DESCRIPTION},
				new int[]{R.id.profile_list_item_name, R.id.profile_list_item_discription});
		setListAdapter(adapter);

		bt_new_profile = (Button) findViewById(R.id.bt_add_profile);
		bt_new_profile.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) {
				//跳转到自定义模式界面
				Intent intent=new Intent();
				intent.setClass(ProfileActivity.this, CreateProfileActivity.class);
				startActivityForResult(intent, MyConstant.REQUEST_CODE_CREATE_PROFILE); 
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == MyConstant.REQUEST_CODE_CREATE_PROFILE)
		{
			if (resultCode == RESULT_OK)
			{
				MyProfile mp = (MyProfile) data.getParcelableExtra(MY_PROFILE_KEY);
				/*Toast.makeText(
                		ProfileActivity.this,
                		mp.toString(),
                		Toast.LENGTH_LONG).show();*/
				
				//将MyProfile的值填入ContentValues中
				ContentValues values = new ContentValues();
				values.put(MyProfileTable.NAME, mp.getName());
				values.put(MyProfileTable.ALLOW_CHANGING_VOLUME, mp.isAllowChangingVolume());
				if(mp.isAllowChangingVolume())
					values.put(MyProfileTable.VOLUME, mp.getVolume());
				values.put(MyProfileTable.VIBRATE_TYPE, mp.getVibrate_type());
				values.put(MyProfileTable.ALLOW_CHANGING_RINGTONE, mp.isAllowChangingRingtone());
				if(mp.isAllowChangingRingtone())
					values.put(MyProfileTable.RINGTONE, mp.getRingtone());
				values.put(MyProfileTable.MESSAGE_CONTENT, mp.getMessage_content());
				values.put(MyProfileTable.DESCRIPTION, mp.toString());
				getContentResolver().insert(MyProfileTable.CONTENT_URI, values);
			}
		}
		else if(requestCode == MyConstant.REQUEST_CODE_EDIT_PROFILE)
		{
			if (resultCode == RESULT_OK)
			{
				/*Toast.makeText(
						ProfileActivity.this,
						"编辑返回测试",
						Toast.LENGTH_SHORT).show();*/
				MyProfile mp = (MyProfile) data.getParcelableExtra(MY_PROFILE_KEY);
				long id = data.getLongExtra(EDIT_PROFILE_ID_KEY, -1);
				
				//将MyProfile的值填入ContentValues中
				ContentValues values = new ContentValues();
				values.put(MyProfileTable.NAME, mp.getName());
				values.put(MyProfileTable.ALLOW_CHANGING_VOLUME, mp.isAllowChangingVolume());
				if(mp.isAllowChangingVolume())
					values.put(MyProfileTable.VOLUME, mp.getVolume());
				values.put(MyProfileTable.VIBRATE_TYPE, mp.getVibrate_type());
				values.put(MyProfileTable.ALLOW_CHANGING_RINGTONE, mp.isAllowChangingRingtone());
				if(mp.isAllowChangingRingtone())
					values.put(MyProfileTable.RINGTONE, mp.getRingtone());
				values.put(MyProfileTable.MESSAGE_CONTENT, mp.getMessage_content());
				values.put(MyProfileTable.DESCRIPTION, mp.toString());
				
				getContentResolver().update(
						MyProfileTable.CONTENT_URI,
						values,
						MyProfileTable._ID + "=?",
						new String[]{""+id});
			}
		}

	}

	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		final long _id = id;
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
							editCorrespondingProfile(_id);
							break;
						case 1:
							delteCorrespondingProfile(_id);
							break;
						}
						dialog.dismiss();
					}
				})
				.setNegativeButton(getString(R.string.cancel), null)
				.show();
	}

	//编辑对应的模式
	protected void editCorrespondingProfile(long id)
	{
		//跳转到编辑模式界面
		Intent intent=new Intent();
		intent.setClass(ProfileActivity.this, EditProfileActivity.class);
		intent.putExtra(EDIT_PROFILE_ID_KEY, id);
		startActivityForResult(intent, MyConstant.REQUEST_CODE_EDIT_PROFILE); 
	}

	//删除对应的模式
	private void delteCorrespondingProfile(long id)
	{
		final long _id = id;

		new Builder(ProfileActivity.this)
		.setIcon(R.drawable.alerts_warning)
		.setMessage(getString(R.string.delete_profile_text))
		.setTitle(getString(R.string.tips))
		.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener(){
			@SuppressWarnings("deprecation")
			public void onClick(DialogInterface dialog, int which)
			{
				String msg = null;
				int result = getContentResolver().delete(
						MyProfileTable.CONTENT_URI,
						MyProfileTable._ID + "=?",
						new String[]{""+_id});
				if(result == 1)
					msg = getString(R.string.delete_profile_success);
				else
					msg = getString(R.string.delete_profile_fail);
				Toast.makeText(ProfileActivity.this, msg, Toast.LENGTH_SHORT).show();

				//刷新当前listView
				cursor.requery();
				dialog.dismiss();
			}})
			.setNegativeButton(getString(R.string.cancel), null)
			.show();
	}
}
