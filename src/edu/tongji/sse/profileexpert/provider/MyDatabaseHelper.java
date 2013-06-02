package edu.tongji.sse.profileexpert.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper
{
	//数据库名
	public static final String 		       DATABASE_NAME = "my_profile.db";
	private static final int 			   DATABASE_VERSION = 1;
	//表名
	private static final String 		   TABLE_NAME = MyProfileTable.TABLE_NAME;
	//创建表SQL语句
	private static final String 		   CREATE_TABLE="CREATE TABLE "
														+ TABLE_NAME
														+ "(" + MyProfileTable._ID
														+ " INTEGER PRIMARY KEY,"
														+ MyProfileTable.NAME
														+ " TEXT,"
														+ MyProfileTable.ALLOW_CHANGING_VOLUME
														+ " BOOLEAN,"
														+ MyProfileTable.VOLUME
														+ " INTEGER,"
														+ MyProfileTable.VIBRATE_TYPE
														+ " INTEGER,"
														+ MyProfileTable.ALLOW_CHANGING_RINGTONE
														+ " BOOLEAN,"
														+ MyProfileTable.RINGTONE
														+ " TEXT,"
														+ MyProfileTable.MESSAGE_CONTENT
														+ " TEXT,"
														+ MyProfileTable.DESCRIPTION
														+ " TEXT" + ");";

	//构造函数-创建数据库
	public MyDatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	//创建表
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(CREATE_TABLE);
	}

	//更新数据库
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}
}
