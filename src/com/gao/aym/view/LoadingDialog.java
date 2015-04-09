package com.gao.aym.view;

import com.gao.aym.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

public class LoadingDialog extends Dialog {
	private ImageView iv_progress;
	private TextView loadding_tip;
	private Context ctx;

	public LoadingDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.ctx = context;
		setContentView(R.layout.view_loadingdialog);
		initView();
	}

	public LoadingDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.ctx = context;
		setContentView(R.layout.view_loadingdialog);
		initView();

	}

	public LoadingDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}

	private void initView() {
		// TODO Auto-generated method stub
		iv_progress = (ImageView) findViewById(R.id.iv_loadingProgress);
		loadding_tip = (TextView) findViewById(R.id.tv_loadingTips);
	}

	/**
	 * @param resoureID
	 */
	public void setLoadingImage(int resoureID) {
		iv_progress.setImageDrawable(ctx.getResources().getDrawable(resoureID));
//		iv_progress.setBackgroundResource(resoureID);
	}

	/**
	 * @param tip
	 */
	public void setLoadingTip(String tip) {
		loadding_tip.setText(tip);
	}

	/**
	 * @param isForResult
	 */
	public void show(boolean isForResult) {
		if (!isForResult) {
			iv_progress.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					AnimationDrawable animation = (AnimationDrawable) iv_progress
							.getBackground();
					animation.start();
				}
			});
		}
		super.show();
	}

}
