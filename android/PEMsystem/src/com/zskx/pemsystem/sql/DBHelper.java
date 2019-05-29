package com.zskx.pemsystem.sql;


import com.zskx.pemsystem.util.StringsUtil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	private static String LAG = "DBHelper";
	private static int VERSION = 1;
	private static String userId = "1111";
	
	public DBHelper(Context context, String name, CursorFactory factory,int version) {
		super(context, name, factory, version);
	}
	public DBHelper(Context context, String name,int version) {
		super(context, name, null, version);
	}
	public DBHelper(Context context, String name) {
		super(context, name + ".db", null, VERSION);
	}
	public DBHelper(Context context) {
		super(context, userId + ".db", null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(LAG, "onCreate");
		
		db.execSQL("CREATE TABLE " + StringsUtil.Music_Playing_list + 
				"(id integer primary key autoincrement, musicId varchar(50) not null, " +
				"musicTitle varchar(50) not null, musicUrl varchar(200) not null, musicImg varchar(200) , " +
				"musicTypeDesc varchar(500) )");
		
		db.execSQL("CREATE TABLE " + StringsUtil.Music_Playing_position + 
				"(id integer primary key , listPosition int, musicPosition int , model int , isStartPlay int, isPos int, isFIFO int)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(LAG, "onUpgrade");
	}

}
