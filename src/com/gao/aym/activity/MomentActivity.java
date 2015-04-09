package com.gao.aym.activity;

import java.util.ArrayList;
import java.util.List;

import com.gao.aym.R;
import com.gao.aym.fragment.AroundFragment;
import com.gao.aym.fragment.ActivityFragment;
import com.gao.aym.fragment.RecommendFragment;
import com.gao.aym.service.FloatService;
import com.gao.aym.util.MyAnimation;
import com.gao.aym.util.MyApplication;
import com.viewpagerindicator.TabPageIndicator;
import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.UnderlinePageIndicator;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.MonthDisplayHelper;
import android.util.TypedValue;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class MomentActivity extends BaseActivity {
	private static final String TAG = "MomentActivity";
	//布局组件
	private ImageButton  btn_settings;
	private TabPageIndicator indicator;
	
	private ViewPager viewpager;
	private int currentindex = 0; //viewpager默认第一个位置
	private View view;
	private String[] tabs = new String[] { "朋友圈", "活动", "附近" }; //tab
	
	//动画
	private RotateAnimation c_animation;
	private Animation click_anim;
	boolean isShow = false;
	ImageView iv_center, iv_pic, iv_vote, iv_edit;
	//手势
	private GestureDetector gestureDetector;
	private GestureDetector.OnGestureListener onGestureListener;
	//用户手机号
	private String phonenum;
	private static Intent service;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		overridePendingTransition(R.anim.right_in, R.anim.left_out);
		MyApplication.getInstance().addActivity(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_moment);
		service = new Intent(this,FloatService.class);
		startService(service);
		phonenum = getIntent().getStringExtra("phonenum");//游客模式传 ""
        Log.v(TAG, "现在登录手机号为->>"+phonenum);
		//实例化手势监听
		onGestureListener = new GestureDetector.OnGestureListener() {
			
			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void onShowPress(MotionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
					float distanceY) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void onLongPress(MotionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
					float velocityY) {
				// TODO Auto-generated method stub
				float x = e2.getX() - e1.getX();  
	            float y = e2.getY() - e1.getY();  
	  
	            if (x > 0) {  //右滑
//	               currentindex=currentindex-1;
//	               if(currentindex>0){ 
//	            	   viewpager.setCurrentItem(currentindex);
//	               }else{
//	            	   viewpager.setCurrentItem(0);
//	               }
	            } else if (x < 0) {   //左滑
//	            	   currentindex=currentindex+1;
//		               if(currentindex<2){
//		            	   viewpager.setCurrentItem(currentindex);
//		               }else{
//		            	   viewpager.setCurrentItem(2);
//		               };
	            }  
	            return true;  
			}
			
			@Override
			public boolean onDown(MotionEvent e) {
				// TODO Auto-generated method stub
				return false;
			}
		};
		gestureDetector = new GestureDetector(this, onGestureListener);


		indicator = (TabPageIndicator) findViewById(R.id.indicator);
		viewpager = (ViewPager) findViewById(R.id.pager);
		btn_settings=(ImageButton)findViewById(R.id.btn_settings);
        btn_settings.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MomentActivity.this,SettingActivity.class);
				intent.putExtra("phonenum", phonenum);
				startActivity(intent);
			}
		});
		MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
		viewpager.setOffscreenPageLimit(1);
		viewpager.setAdapter(pagerAdapter);
		//
		indicator.setViewPager(viewpager);
	}

	


	class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int position) {
			// TODO Auto-generated method stub
			if (position == 0) {
				RecommendFragment recommendFragment = new RecommendFragment();
				Bundle bundle = new Bundle();
				bundle.putString("phonenum", phonenum);
				recommendFragment.setArguments(bundle);
				return recommendFragment;
				
			}
			if (position == 1) {
	
				return new ActivityFragment();
			}
			if(position==2){
				AroundFragment aroundFragment= new AroundFragment();
				Bundle bundle = new Bundle();
				bundle.putString("phonenum", phonenum);
				aroundFragment.setArguments(bundle);
				return aroundFragment;
			}
			return null;
		}
	
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return tabs.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return tabs[position];
		}

	}
     
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		startService(service);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		stopService(service);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
        stopService(service);
	}
}
