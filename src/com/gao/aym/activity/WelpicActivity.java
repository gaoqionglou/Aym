package com.gao.aym.activity;

import com.gao.aym.R;
import com.gao.aym.util.MyApplication;
import com.viewpagerindicator.TabPageIndicator;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Window;

public class WelpicActivity extends BaseActivity{
    
	public WelpicActivity() {
		// TODO Auto-generated constructor stub
	}
  @Override
public void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	overridePendingTransition(R.anim.right_in, R.anim.left_out);
	MyApplication.getInstance().addActivity(this);
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_welpic);
	new CountDownTimer(3000,1000) {
		
		@Override
		public void onTick(long millisUntilFinished) {
			// TODO Auto-generated method stub
			//什么都不做
		}
		
		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			Intent intent = new Intent(WelpicActivity.this,WelcomeActivity.class);
			startActivity(intent);
			WelpicActivity.this.finish();
		}
	}.start();
}
}
