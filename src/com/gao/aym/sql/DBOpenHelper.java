package com.gao.aym.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper{
    
	private static final String DATABASENAME="DianZan.db";
   
	public DBOpenHelper(Context context,int version) {
		// TODO Auto-generated constructor stub
		super(context,DATABASENAME, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create table if not exists RecordZan (id varchar(20) primary key , user_phone varchar(20),record int)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
