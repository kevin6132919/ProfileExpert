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

public class RoutineProvider extends ContentProvider
{
	private MyDatabaseHelper dbHelper = null;
	private static UriMatcher uriMatcher = null;
	private static HashMap<String, String> tempMatterProjectionMap;
	
	private static final int ROUTINES = 1;
	private static final int ROUTINE_ID = 2;
	
	static
	
	{
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(RoutineTable.AUTHORITY, "routine", ROUTINES);
		uriMatcher.addURI(RoutineTable.AUTHORITY, "routine/#", ROUTINE_ID);
		
		tempMatterProjectionMap = new HashMap<String, String>();
		tempMatterProjectionMap.put(RoutineTable._ID, RoutineTable._ID);
		tempMatterProjectionMap.put(RoutineTable.TITLE, RoutineTable.TITLE);
		tempMatterProjectionMap.put(RoutineTable.START_DAY, RoutineTable.START_DAY);
		tempMatterProjectionMap.put(RoutineTable.IS_SAME_DAY, RoutineTable.IS_SAME_DAY);
		tempMatterProjectionMap.put(RoutineTable.TIME_FROM, RoutineTable.TIME_FROM);
		tempMatterProjectionMap.put(RoutineTable.TIME_TO, RoutineTable.TIME_TO);
		tempMatterProjectionMap.put(RoutineTable.DESCRIPTION, RoutineTable.DESCRIPTION);
		tempMatterProjectionMap.put(RoutineTable.SHOW_STRING, RoutineTable.SHOW_STRING);
		tempMatterProjectionMap.put(RoutineTable.PROFILE_ID, RoutineTable.PROFILE_ID);
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
		if (uriMatcher.match(uri) != ROUTINES) {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		long rowId = db.insert(RoutineTable.TABLE_NAME, RoutineTable.DESCRIPTION, values);
		
		if (rowId > 0)
		{
			Uri profileUri = ContentUris.withAppendedId(RoutineTable.CONTENT_URI, rowId);
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
		case ROUTINES:
			count = db.update(RoutineTable.TABLE_NAME, values, selection, selectionArgs);
			break;
		
		case ROUTINE_ID:
			String profileId = uri.getPathSegments().get(1);
			count = db.update(
					RoutineTable.TABLE_NAME,
					values,
					RoutineTable._ID + "=" + profileId + 
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
		case ROUTINES:
			count = db.delete(RoutineTable.TABLE_NAME, selection, selectionArgs);
			break;
		
		case ROUTINE_ID:
			String profileId = uri.getPathSegments().get(1);
			count = db.delete(
					RoutineTable.TABLE_NAME,
					RoutineTable._ID + "=" + profileId + 
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
		case ROUTINES:
			return RoutineTable.CONTENT_ITEM;

		case ROUTINE_ID:
			return RoutineTable.CONTENT_ITEM_TYPE;

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
		case ROUTINES:
			qb.setTables(RoutineTable.TABLE_NAME);
			qb.setProjectionMap(tempMatterProjectionMap);
			break;
			
		case ROUTINE_ID:
			qb.setTables(RoutineTable.TABLE_NAME);
			qb.setProjectionMap(tempMatterProjectionMap);
			qb.appendWhere(RoutineTable._ID + "=" + uri.getPathSegments().get(1));
			break;
			
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		
		String orderBy;
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = RoutineTable.DEFAULT_SORT_ORDER;
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
