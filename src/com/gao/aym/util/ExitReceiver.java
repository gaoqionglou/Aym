package com.gao.aym.util;

import com.gao.aym.activity.BaseActivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ExitReceiver extends BroadcastReceiver {
//    private Context ctx;
    private static ExitReceiver receiver;
	public ExitReceiver() {
		// TODO Auto-generated constructor stub
		
	}
  public static ExitReceiver getInstance(){
	  if(receiver==null){
		  receiver = new ExitReceiver();
	  }
	  return receiver;
  }
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
       Log.v("ExitReceiver", "接收到退出广播");
		BaseActivity activity = (BaseActivity)context;
       activity.finish();
	}

}
