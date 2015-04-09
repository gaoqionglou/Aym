package com.gao.aym.activity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import com.gao.aym.R;
import com.gao.aym.util.HttpRequest;
import com.gao.aym.util.MyApplication;
import com.gao.aym.util.PhoneUtil;
import com.gao.aym.view.CustomDialog;
import com.gao.aym.view.LoadingDialog;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends BaseActivity implements OnClickListener {
	private EditText ed_phonenum, ed_password;
	private String phonenum, password;
	private Button btn_login;
	private ImageView del_phonenum, del_password;
	private MyApplication application;
	private LoadingDialog loaddingDialog;
	private CustomDialog customDialog;
	private ImageButton btn_add,btn_back;
    private TextView tv_tip;
	public LoginActivity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		overridePendingTransition(R.anim.right_in, R.anim.left_out);
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_login);
		application = (MyApplication) getApplication();
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		loaddingDialog = new LoadingDialog(this, R.style.loading_dialog);
		customDialog = new CustomDialog(this);
		tv_tip=(TextView)findViewById(R.id.tv_modifycode);
		tv_tip.setText(Html.fromHtml("<u>" + "忘记or修改密码" + "</u>" ));
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_back=(ImageButton)findViewById(R.id.btn_back);
		btn_add=(ImageButton)findViewById(R.id.btn_add);
		ed_phonenum = (EditText) findViewById(R.id.ed_phonenum);
		
		// 如果缓存中有记录登录过账号 则写入这个账号
		if (!application.getPhonenum().equals("")) {
			String a = application.getPhonenum();
			ed_phonenum.setText(a);
		}
		ed_password = (EditText) findViewById(R.id.ed_password);
		del_password = (ImageView) findViewById(R.id.del_password);
		ed_phonenum.setText("18825048093");
		ed_password.setText("123456");
		del_phonenum = (ImageView) findViewById(R.id.del_phone);
		del_password.setOnClickListener(this);
		del_phonenum.setOnClickListener(this);
		btn_login.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		btn_add.setOnClickListener(this);
		tv_tip.setOnClickListener(this);
		ed_phonenum.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				//
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				String text = ed_phonenum.getText().toString();
				if (text.length() > 0) {
					del_phonenum.setVisibility(View.VISIBLE);
					if (!ed_phonenum.getText().toString().equals("")) {
						btn_login.setEnabled(true);
					} else {
						btn_login.setEnabled(false);
					}
				} else {
					del_phonenum.setVisibility(View.INVISIBLE);
					btn_login.setEnabled(false);
				}
			}
		});
		ed_password.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				String text = ed_password.getText().toString();
				if (text.length() > 0) {
					del_password.setVisibility(View.VISIBLE);
				} else {
					del_password.setVisibility(View.INVISIBLE);
				}
			}
		});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent=null;
		switch (v.getId()) {
		case R.id.del_phone:
			ed_phonenum.setText("");
			break;
		case R.id.del_password:
			ed_password.setText("");
			break;
		case R.id.btn_back:
			onBackPressed();
			break;
		case R.id.btn_add:
			 intent = new Intent();
			intent.setClass(LoginActivity.this, RegisterActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_login:
			// ，检测手机号码的合法性
			phonenum = ed_phonenum.getText().toString();
			password=ed_password.getText().toString();
			
			if (!phonenum.equals("")) {
				if (PhoneUtil.isValidMoblie(phonenum)) {
					// 进行网络请求
					if(isOpenNetwork()){
					new LoginTask().execute();
						
					}else{
						customDialog.setBtnLeftVisiblity(View.GONE);
						customDialog.setDialogMsg("网络已中断，请检查你的网络连接");
						customDialog.setBtnRght("我知道了", new OnClickListener() {
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								customDialog.dismiss();
							}
						});
						customDialog.show();
					}
				}else{
					Toast.makeText(LoginActivity.this, "抱歉，您的手机号有误，请重新输入",1).show();
				}
			} else {
				Toast.makeText(LoginActivity.this, "亲，您的手机号忘填了~",1).show();
			}
			break;
		case R.id.tv_modifycode:
		    intent = new Intent();
		    intent.setClass(LoginActivity.this, ModifyCodeActivity.class);
		    startActivity(intent);
			break;
		default:
			break;
		}
	}

	class LoginTask extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			loaddingDialog.show(false);

		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String result = "";
			Map<String, String> data = new HashMap<String, String>();
			data.put("phonenum", phonenum);
			data.put("password", password);
            application.setPhoneNum(phonenum);
            application.setPassword(password);
			try {
				result = HttpRequest.sendLoginRequest(data);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			//head 11-成功 10-密码错误 00-未注册用户
		   loaddingDialog.dismiss();
			JSONObject jsonObject =null;
		   try {
			jsonObject = new JSONObject(result);
			String head=jsonObject.getString("head");
			if("11".equals(head)){
				String phonenum=jsonObject.getString("phonenum");
				Intent intent = new Intent();
				intent.putExtra("phonenum", phonenum);
				intent.setClass(LoginActivity.this, MomentActivity.class);
				startActivity(intent);
			}else if("10".equals(head)){
				Toast.makeText(LoginActivity.this, "亲，你的密码好像不是这样的~", 1).show();
			}else if("00".equals(head)){
				Toast.makeText(LoginActivity.this, "别心急，请先去注册...", 1).show();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.v("onresume", "resume");
		Bundle bundle = getIntent().getExtras();
		if(bundle!=null){
			ed_phonenum.setText(bundle.getString("phonenum"));
			ed_password.setText(bundle.getString("password"));
		}
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.v("onpause", "pause");
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
//		outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");  
//		super.onSaveInstanceState(outState);
	}
}
