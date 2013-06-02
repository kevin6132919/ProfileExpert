package edu.tongji.sse.profileexpert.provider;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class MyProfileProvider extends ContentProvider
{
	private MyDatabaseHelper dbHelper = null;
	private static UriMatcher uriMatcher = null;
	private static HashMap<String, String> myProfileProjectionMap;
	
	private static final int MY_PROFILES = 1;
	private static final int MY_PROFILE_ID = 2;
	
	static
	{
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(MyProfileTable.AUTHORITY, "myprofile", MY_PROFILES);
		uriMatcher.addURI(MyProfileTable.AUTHORITY, "myprofile/#", MY_PROFILE_ID);
		
		myProfileProjectionMap = new HashMap<String, String>();
		myProfileProjectionMap.put(MyProfileTable._ID, MyProfileTable._ID);
		myProfileProjectionMap.put(MyProfileTable.NAME, MyProfileTable.NAME);
		myProfileProjectionMap.put(MyProfileTable.ALLOW_CHANGING_VOLUME, MyProfileTable.ALLOW_CHANGING_VOLUME);
		myProfileProjectionMap.put(MyProfileTable.VOLUME, MyProfileTable.VOLUME);
		myProfileProjectionMap.put(MyProfileTable.VIBRATE_TYPE, MyProfileTable.VIBRATE_TYPE);
		myProfileProjectionMap.put(MyProfileTable.ALLOW_CHANGING_RINGTONE, MyProfileTable.ALLOW_CHANGING_RINGTONE);
		myProfileProjectionMap.put(MyProfileTable.RINGTONE, MyProfileTable.RINGTONE);
		myProfileProjectionMap.put(MyProfileTable.MESSAGE_CONTENT, MyProfileTable.MESSAGE_CONTENT);
		myProfileProjectionMap.put(MyProfileTable.DISCRIPTION, MyProfileTable.DISCRIPTION);
	}
	
	@Override
	public boolean onCreate()
	{
		dbHelper = new MyDatabaseHelper(getContext());
		return true;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values)
	{
		if (uriMatcher.match(uri) != MY_PROFILES) {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		long rowId = db.insert(MyProfileTable.TABLE_NAME, MyProfileTable.MESSAGE_CONTENT, values);
		
		if (rowId > 0)
		{
			Uri profileUri = ContentUris.withAppendedId(MyProfileTable.CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(profileUri, null);
			return profileUri;
		}
		
		throw new SQLException("Failed to insert row into" + uri);
	}

	@Override
	public int update(Uri uri, ContentValues values,
			String selection, String[] selectionArgs)
	{
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int count;
		
		switch (uriMatcher.match(uri))
		{
		case MY_PROFILES:
			count = db.update(MyProfileTable.TABLE_NAME, values, selection, selectionArgs);
			break;
		
		case MY_PROFILE_ID:
			String profileId = uri.getPathSegments().get(1);
			count = db.update(
					MyProfileTable.TABLE_NAME,
					values,
					MyProfileTable._ID + "=" + profileId + 
						(!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),
					selectionArgs);
			break;
			
		default:
			throw new IllegalArgumentException("Unknow URI " + uri);
		}
		
		return count;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs)
	{
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int count;
		
		switch (uriMatcher.match(uri))
		{
		case MY_PROFILES:
			count = db.delete(MyProfileTable.TABLE_NAME, selection, selectionArgs);
			break;
		
		case MY_PROFILE_ID:
			String profileId = uri.getPathSegments().get(1);
			count = db.delete(
					MyProfileTable.TABLE_NAME,
					MyProfileTable._ID + "=" + profileId + 
						(!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),
					selectionArgs);
			break;
			
		default:
			throw new IllegalArgumentException("Unknow URI " + uri);
		}
		
		return count;
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
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs,	String sortOrder)
	{
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		
		switch (uriMatcher.match(uri)) {
		case MY_PROFILES:
			qb.setTables(MyProfileTable.TABLE_NAME);
			qb.setProjectionMap(myProfileProjectionMap);
			break;
			
		case MY_PROFILE_ID:
			qb.setTables(MyProfileTable.TABLE_NAME);
			qb.setProjectionMap(myProfileProjectionMap);
			qb.appendWhere(MyProfileTable._ID + "=" + uri.getPathSegments().get(1));
			break;
			
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		
		String orderBy;
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = MyProfileTable.DEFAULT_SORT_ORDER;
		}else {
			orderBy = sortOrder;
		}
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);
		//为Cursor对象注册一个观察数据变化的URI
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

}
