package edu.tongji.sse.profileexpert.provider;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class MyProfileProvider extends ContentProvider
{
	private MyDatabaseHelper dbHelper = null;
	private static UriMatcher uriMatcher = null;
	private static HashMap<String, String> myProfileMap;
	
	private static final int MY_PROFILES = 1;
	private static final int MY_PROFILE_ID = 2;
	
	static
	{
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(MyProfileTable.AUTHORITY, "myprofile", MY_PROFILES);
		uriMatcher.addURI(MyProfileTable.AUTHORITY, "myprofile/#", MY_PROFILE_ID);
		
		myProfileMap = new HashMap<String, String>();
		myProfileMap.put(MyProfileTable._ID, MyProfileTable._ID);
		myProfileMap.put(MyProfileTable.NAME, MyProfileTable.NAME);
	}
	
	@Override
	public boolean onCreate()
	{
		dbHelper = new MyDatabaseHelper(getContext());
		return true;
	}

	@Override
	public Uri insert(Uri arg0, ContentValues arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri)
	{
		switch (uriMatcher.match(uri))
		{
		case MY_PROFILES:
			return MyProfileTable.CONTENT_ITEM;

		case MY_PROFILE_ID:
			return MyProfileTable.CONTENT_ITEM_TYPE;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public Cursor query(Uri arg0, String[] arg1, String arg2, String[] arg3,
			String arg4) {
		// TODO Auto-generated method stub
		return null;
	}

}
