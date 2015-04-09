package com.gao.aym.activity;

import com.gao.aym.util.ExitReceiver;
import com.gao.aym.util.MyApplication;
import com.gao.aym.view.CustomDialog;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


/**
 * @author gao
 * 所有的Activity的父类
 */
public class BaseActivity extends FragmentActivity {
    private CustomDialog customDialog;
    private ExitReceiver exitReceiver;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	
    }
	 
    /**
     * 关闭所有Activity
     */
    public void finishAll(){
    	Intent intent = new Intent("android.intent.action.EXIT");
    	sendBroadcast(intent);
    }
	//检查网络是否断开
	public boolean isOpenNetwork() {  
		boolean isOpen=false;
		ConnectivityManager connManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);  
		if(connManager!=null){
			NetworkInfo netinfo = connManager.getActiveNetworkInfo();
			if(netinfo!=null&&netinfo.isConnected()){
				if(netinfo.getState()==NetworkInfo.State.CONNECTED){
					isOpen=true;
					return isOpen;
				}
			}
		}
	   return isOpen;
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
//		super.onSaveInstanceState(outState);
	}
}
