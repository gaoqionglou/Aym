package com.gao.aym.view;



import com.gao.aym.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

/**
 * 状态处理对话框
 * 弹出条件：网络，用户退出
 * @author gao
 *
 */
public class CustomDialog extends Dialog {
   private TextView dialog_msg;
   private Button btnLeft,btnRight;
   private TextView title;
	
	public CustomDialog(Context context) {
		super(context,R.style.customdialog);
		// TODO Auto-generated constructor stub
		setDialogWindow(context);
		setContentView(R.layout.view_customdialog);
		initDialogView();
	}
	 
	public void initDialogView() {
		// TODO Auto-generated method stub
		dialog_msg = (TextView)findViewById(R.id.customdialog_msg);
		title=(TextView)findViewById(R.id.customdialog_title);
		btnLeft=(Button)findViewById(R.id.customdialog_btnLeft);
		btnRight=(Button)findViewById(R.id.customdialog_btnRight);
	}

	public void setDialogWindow(Context context){
		Window dialogWindow = getWindow();
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams windowLP = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);
		windowLP.width = display.getWidth() - 60;
//		windowLP.height=(int) (display.getHeight() * 0.2);
		dialogWindow.setAttributes(windowLP);
	}
    
	/** 设置左按钮
	 * @param txt 按钮文字
	 * @param listener 按钮点击事件
	 */
	public void setBtnLeft(String txt,android.view.View.OnClickListener listener){
		btnLeft.setText(txt);
		btnLeft.setOnClickListener(listener);
	}
	/**设置右按钮
	 * @param txt 按钮文字
	 * @param listener 按钮点击事件
	 */
	public void setBtnRght(String txt,android.view.View.OnClickListener listener){
		btnRight.setText(txt);
		btnRight.setOnClickListener(listener);
	}
	
	public void setBtnLeftVisiblity(int visible){
		btnLeft.setVisibility(View.GONE);
	}
	
	/** 设置标题文字
	 * @param txt
	 */
	public void setDialogTitle(String txt){
		title.setText(txt);
	}
	/** 设置对话框内容
	 * @param txt
	 */
	public void setDialogMsg(String txt){
		dialog_msg.setText(txt);
	}
}
