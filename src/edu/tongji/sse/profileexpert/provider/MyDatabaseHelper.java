package edu.tongji.sse.profileexpert.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import edu.tongji.sse.profileexpert.util.DefaultProfile;

public class MyDatabaseHelper extends SQLiteOpenHelper
{
	//数据库名
	public static final String DATABASE_NAME = "profile_expert.db";
	private static final int DATABASE_VERSION = 2;
	//表名
	private static final String TABLE_NAME_1 = MyProfileTable.TABLE_NAME;
	//创建表SQL语句
	private static final String CREATE_TABLE_1 ="CREATE TABLE "
			+ TABLE_NAME_1
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


	private static final String TABLE_NAME_2 = TempMatterTable.TABLE_NAME;
	private static final String CREATE_TABLE_2 ="CREATE TABLE "
			+ TABLE_NAME_2
			+ "(" + TempMatterTable._ID
			+ " INTEGER PRIMARY KEY,"
			+ TempMatterTable.TITLE
			+ " TEXT,"
			+ TempMatterTable.TIME_FROM
			+ " LONG,"
			+ TempMatterTable.TIME_TO
			+ " LONG,"
			+ TempMatterTable.TIME_FROM_STR
			+ " TEXT,"
			+ TempMatterTable.TIME_TO_STR
			+ " TEXT,"
			+ TempMatterTable.SHOW_STRING
			+ " TEXT,"
			+ TempMatterTable.DESCRIPTION
			+ " TEXT,"
			+ TempMatterTable.PROFILE_ID
			+ " LONG" + ");";

	private static final String TABLE_NAME_3 = RoutineTable.TABLE_NAME;
	private static final String CREATE_TABLE_3 ="CREATE TABLE "
			+ TABLE_NAME_3
			+ "(" + RoutineTable._ID
			+ " INTEGER PRIMARY KEY,"
			+ RoutineTable.TITLE
			+ " TEXT,"
			+ RoutineTable.START_DAY
			+ " INTEGER,"
			+ RoutineTable.IS_SAME_DAY
			+ " BOOLEAN,"
			+ RoutineTable.TIME_FROM
			+ " TEXT,"
			+ RoutineTable.TIME_TO
			+ " TEXT,"
			+ RoutineTable.SHOW_STRING
			+ " TEXT,"
			+ RoutineTable.DESCRIPTION
			+ " TEXT,"
			+ RoutineTable.PROFILE_ID
			+ " LONG" + ");";
	
	//构造函数-创建数据库
	public MyDatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	//创建表
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(CREATE_TABLE_1);
		addDefaultProfile(db);
		db.execSQL(CREATE_TABLE_2);
		db.execSQL(CREATE_TABLE_3);
	}

	//在表中增加预定义的模式
	private void addDefaultProfile(SQLiteDatabase db)
	{
		db.execSQL(DefaultProfile.INSERT_PROFILE_NORMAL);
		db.execSQL(DefaultProfile.INSERT_PROFILE_WORK);
		db.execSQL(DefaultProfile.INSERT_PROFILE_CONFERENCE);
		db.execSQL(DefaultProfile.INSERT_PROFILE_BREAK);
	}

	//更新数据库
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_1);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_2);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_3);
		onCreate(db);
	}
}
