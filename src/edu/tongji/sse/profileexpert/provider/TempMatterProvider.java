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

public class TempMatterProvider extends ContentProvider
{
	private MyDatabaseHelper dbHelper = null;
	private static UriMatcher uriMatcher = null;
	private static HashMap<String, String> tempMatterProjectionMap;
	
	private static final int TEMP_MATTERS = 1;
	private static final int TEMP_MATTER_ID = 2;
	
	static
	{
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(TempMatterTable.AUTHORITY, "tempmatter", TEMP_MATTERS);
		uriMatcher.addURI(TempMatterTable.AUTHORITY, "tempmatter/#", TEMP_MATTER_ID);
		
		tempMatterProjectionMap = new HashMap<String, String>();
		tempMatterProjectionMap.put(TempMatterTable._ID, TempMatterTable._ID);
		tempMatterProjectionMap.put(TempMatterTable.TITLE, TempMatterTable.TITLE);
		tempMatterProjectionMap.put(TempMatterTable.TIME_FROM, TempMatterTable.TIME_FROM);
		tempMatterProjectionMap.put(TempMatterTable.TIME_TO, TempMatterTable.TIME_TO);
		tempMatterProjectionMap.put(TempMatterTable.TIME_FROM_STR, TempMatterTable.TIME_FROM_STR);
		tempMatterProjectionMap.put(TempMatterTable.TIME_TO_STR, TempMatterTable.TIME_TO_STR);
		tempMatterProjectionMap.put(TempMatterTable.DESCRIPTION, TempMatterTable.DESCRIPTION);
		tempMatterProjectionMap.put(TempMatterTable.SHOW_STRING, TempMatterTable.SHOW_STRING);
		tempMatterProjectionMap.put(TempMatterTable.PROFILE_ID, TempMatterTable.PROFILE_ID);
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
		if (uriMatcher.match(uri) != TEMP_MATTERS) {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		long rowId = db.insert(TempMatterTable.TABLE_NAME, TempMatterTable.DESCRIPTION, values);
		
		if (rowId > 0)
		{
			Uri profileUri = ContentUris.withAppendedId(TempMatterTable.CONTENT_URI, rowId);
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
		case TEMP_MATTERS:
			count = db.update(TempMatterTable.TABLE_NAME, values, selection, selectionArgs);
			break;
		
		case TEMP_MATTER_ID:
			String profileId = uri.getPathSegments().get(1);
			count = db.update(
					TempMatterTable.TABLE_NAME,
					values,
					TempMatterTable._ID + "=" + profileId + 
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
		case TEMP_MATTERS:
			count = db.delete(TempMatterTable.TABLE_NAME, selection, selectionArgs);
			break;
		
		case TEMP_MATTER_ID:
			String profileId = uri.getPathSegments().get(1);
			count = db.delete(
					TempMatterTable.TABLE_NAME,
					TempMatterTable._ID + "=" + profileId + 
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
		case TEMP_MATTERS:
			return TempMatterTable.CONTENT_ITEM;

		case TEMP_MATTER_ID:
			return TempMatterTable.CONTENT_ITEM_TYPE;

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
		case TEMP_MATTERS:
			qb.setTables(TempMatterTable.TABLE_NAME);
			qb.setProjectionMap(tempMatterProjectionMap);
			break;
			
		case TEMP_MATTER_ID:
			qb.setTables(TempMatterTable.TABLE_NAME);
			qb.setProjectionMap(tempMatterProjectionMap);
			qb.appendWhere(TempMatterTable._ID + "=" + uri.getPathSegments().get(1));
			break;
			
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		
		String orderBy;
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = TempMatterTable.DEFAULT_SORT_ORDER;
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
