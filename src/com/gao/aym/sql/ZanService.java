package com.gao.aym.sql;

import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ZanService {
	private DBOpenHelper dbOpenHelper;

	public ZanService(Context context) {
		// TODO Auto-generated constructor stub
		dbOpenHelper = new DBOpenHelper(context, 1);
	}

	/**
      * 保存一条点赞记录 参数有
      */
	public void addRecord(String id,String user_phone,int record) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		String sql="insert into RecordZan( id,user_phone,record) values(?,?,?)";
		db.execSQL(sql, new Object[]{id,user_phone,record});

	}
	
	/**
	 * 通过数据库记录查找是否点过赞 
	 * @param user_phone 手机号
	 * @param id id
	 * @return 1 -点过 0-没点过
	 */
	public int findRecord(String user_phone,String id){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		String sql = "select record from RecordZan where id=? and user_phone=?";
		Cursor cursor= db.rawQuery(sql, new String[]{id,user_phone});
		if(cursor.moveToFirst()){
			int record = cursor.getInt(cursor.getColumnIndex("record"));
			return record;
		}
		return 0;
	}
	
	public void updateRecord(String id,String user_phone){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		String sql = "update record=1 from RecordZan where id=? and user_phone=? ";
		db.execSQL(sql, new String[]{id,user_phone});
	}
	
	/**
	 * 查找数据是否存在该记录
	 * @param id
	 * @param user_phone
	 * @return 要么有1条，要么有0条
	 */
	public int getCount(String id,String user_phone){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		String sql = "select * from RecordZan where id=? and user_phone=?";
		Cursor cursor= db.rawQuery(sql, new String[]{id,user_phone});
		return cursor.getCount();
	}
}
