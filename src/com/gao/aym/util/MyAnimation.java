package com.gao.aym.util;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

public class MyAnimation {
	// 图标的动画(入动画)
	public static void startAnimationsIn(View view) {
		Animation s_animation=null;
		s_animation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
	             Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		s_animation.setDuration(500);//设置动画持续时间 
		s_animation.setRepeatCount(0);//设置重复次数
		s_animation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        view.setAnimation(s_animation);
        view.startAnimation(s_animation);
	}

	// 图标的动画(出动画)
	public static void startAnimationsOut(View view) {
		Animation out_animation=null;
		out_animation = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
	             Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		out_animation.setDuration(500);//设置动画持续时间 
		out_animation.setRepeatCount(0);//设置重复次数
		out_animation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        view.setAnimation(out_animation);
        view.startAnimation(out_animation);
	
	}

}







