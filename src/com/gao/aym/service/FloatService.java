package com.gao.aym.service;

import com.gao.aym.R;
import com.gao.aym.activity.MomentActivity;
import com.gao.aym.activity.PublishActivity;
import com.gao.aym.activity.TopicActivity;
import com.gao.aym.activity.WelcomeActivity;
import com.gao.aym.util.MyAnimation;
import com.gao.aym.util.MyApplication;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * 用于创建悬浮窗口的服务
 *
 */
public class FloatService extends Service {
    private final String TAG="FloatService";
	private WindowManager wm;
	private WindowManager.LayoutParams wParams;
	private View view;
	private RotateAnimation c_animation;
	private Animation click_anim;
	boolean isShow = false;
	ImageView iv_center, iv_vote, iv_edit;
	private MyApplication application;
	private String phonenum;
    public FloatService() {
		// TODO Auto-generated constructor stub
		
	}
    
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.i(TAG, " FloatService start");
		application =(MyApplication) getApplication();
		phonenum=application.getPhonenum();
		initFloatView();
		createFloatMenu();
	}
	
	 @Override
     public void onDestroy() {
             // TODO Auto-generated method stub
             super.onDestroy();
         	Log.i(TAG, " FloatService destory");
         	wm.removeView(view);
           
     }
	
	public void initFloatView() {
		wm = (WindowManager) getApplicationContext().getSystemService("window");
		wParams = new WindowManager.LayoutParams();
		wParams.type = LayoutParams.TYPE_SYSTEM_ALERT;
		wParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;
//				LayoutParams.FLAG_NOT_TOUCH_MODAL  
//                | LayoutParams.FLAG_NOT_FOCUSABLE;
		wParams.format = PixelFormat.RGBA_8888 ;
		wParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;
		wParams.x = 0;
		wParams.y = 10;
		// 设置悬浮窗口大小
		wParams.width = 250;
		wParams.height = 250;
	}

	public void createFloatMenu() {
		LayoutInflater inflater = LayoutInflater.from(this);
		view = inflater.inflate(R.layout.view_bot_menu, null);
		iv_center = (ImageView) view.findViewById(R.id.iv_center);
		// 为iv_center设置一个中心旋转动画
		c_animation = new RotateAnimation(0f, 90f, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		c_animation.setDuration(500);// 设置动画持续时间
		c_animation.setRepeatCount(0);// 设置重复次数
		c_animation.setFillAfter(true);// 动画执行完后是否停留在执行完的状态
		iv_center.setAnimation(c_animation);
		click_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.menuitem_anim_click);
		iv_vote = (ImageView) view.findViewById(R.id.iv_vote);
		iv_edit = (ImageView) view.findViewById(R.id.iv_edit);
		
		iv_vote.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iv_vote.setAnimation(click_anim);
				iv_vote.startAnimation(click_anim);
				Intent intent = new Intent(getApplicationContext(),TopicActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
		iv_edit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iv_edit.setAnimation(click_anim);
				iv_edit.startAnimation(click_anim);
				if(phonenum!=null&&!"".equals(phonenum)){
				Intent intent = new Intent(getApplicationContext(),PublishActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				}else{
					Toast.makeText(getApplicationContext(),"游客模式不能发表动态" , 1).show();
				}
			}
		});
         
		iv_center.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iv_center.startAnimation(c_animation);
				if (!isShow) {
					iv_vote.setVisibility(View.VISIBLE);
					iv_edit.setVisibility(View.VISIBLE);
					
					MyAnimation.startAnimationsIn(iv_vote);
					MyAnimation.startAnimationsIn(iv_edit);
					
					isShow = !isShow;
				} else {
					MyAnimation.startAnimationsOut(iv_vote);
					MyAnimation.startAnimationsOut(iv_edit);
				
					isShow = !isShow;
				}
			}
		});

		wm.addView(view, wParams);
	}
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
