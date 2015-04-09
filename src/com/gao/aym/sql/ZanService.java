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
      * ����һ�����޼�¼ ������
      */
	public void addRecord(String id,String user_phone,int record) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		String sql="insert into RecordZan( id,user_phone,record) values(?,?,?)";
		db.execSQL(sql, new Object[]{id,user_phone,record});

	}
	
	/**
	 * ͨ�����ݿ��¼�����Ƿ����� 
	 * @param user_phone �ֻ���
	 * @param id id
	 * @return 1 -��� 0-û���
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
	 * ���������Ƿ���ڸü�¼
	 * @param id
	 * @param user_phone
	 * @return Ҫô��1����Ҫô��0��
	 */
	public int getCount(String id,String user_phone){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		String sql = "select * from RecordZan where id=? and user_phone=?";
		Cursor cursor= db.rawQuery(sql, new String[]{id,user_phone});
		return cursor.getCount();
	}
}
