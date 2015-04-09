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
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class ModifyCodeActivity extends BaseActivity implements OnClickListener {
	private ImageButton btn_back;
	private EditText ed_phonenum, ed_password, ed_password2;
	private ImageView del_phone, del_password, del_password2;
	private Button btn_modify;
	private LoadingDialog loadingDialog;
	private CustomDialog customDialog;
	private String phonenum, password;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		overridePendingTransition(R.anim.right_in, R.anim.left_out);
		MyApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_modify);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		loadingDialog = new LoadingDialog(this, R.style.loading_dialog);
		customDialog = new CustomDialog(this);
		ed_phonenum = (EditText) findViewById(R.id.ed_phonenum);
		ed_password = (EditText) findViewById(R.id.ed_password);
		ed_password2 = (EditText) findViewById(R.id.ed_password2);
		del_phone = (ImageView) findViewById(R.id.del_phone);
		del_password = (ImageView) findViewById(R.id.del_password);
		del_password2 = (ImageView) findViewById(R.id.del_password2);
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		btn_modify = (Button) findViewById(R.id.btn_modify);
		ed_phonenum.addTextChangedListener(new MyTextWatcher(ed_phonenum));
		ed_password.addTextChangedListener(new MyTextWatcher(ed_password));
		ed_password2.addTextChangedListener(new MyTextWatcher(ed_password2));
		btn_back.setOnClickListener(this);
		btn_modify.setOnClickListener(this);
	}

	class MyTextWatcher implements TextWatcher {

		private EditText ed;

		public MyTextWatcher(EditText ed) {
			this.ed = ed;
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			String text = ed.getText().toString();
			switch (ed.getId()) {
			case R.id.ed_phonenum:
				if (text.length() > 0) {
					del_phone.setVisibility(View.VISIBLE);
				} else {
					del_phone.setVisibility(View.INVISIBLE);
				}
				break;
			case R.id.ed_password:
				if (text.length() > 0) {
					del_password.setVisibility(View.VISIBLE);
				} else {
					del_password.setVisibility(View.INVISIBLE);
				}
				break;
			case R.id.ed_password2:
				if (text.length() > 0) {
					del_password2.setVisibility(View.VISIBLE);
				} else {
					del_password2.setVisibility(View.INVISIBLE);
				}
				break;
			default:
				break;
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_back:
			onBackPressed();
			break;
		case R.id.btn_modify:
			if (!isOpenNetwork()) {
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
			} else {

				phonenum = ed_phonenum.getText().toString();
				if (!phonenum.equals("")) {
					if (PhoneUtil.isValidMoblie(phonenum)) {
						if (!ed_password.getText().toString().equals("")
								|| !ed_password2.getText().toString()
										.equals("")) {
							if (ed_password.getText().toString()
									.equals(ed_password2.getText().toString())) {

								password = ed_password.getText().toString();
								new ModifyTask().execute();
							} else {
								Toast.makeText(ModifyCodeActivity.this,
										"两次输入的密码不一致！", 1).show();
							}
						} else {
							Toast.makeText(ModifyCodeActivity.this,
									"亲，别漏了密码哦~", 1).show();
						}
					} else {
						Toast.makeText(ModifyCodeActivity.this,
								"抱歉，您的手机号有误，请重新输入", 1).show();
					}
				} else {
					Toast.makeText(ModifyCodeActivity.this, "亲，您的手机号忘填了~", 1)
							.show();
				}
			}
			break;
		default:
			break;
		}
	}

	class ModifyTask extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			loadingDialog.setLoadingTip("正在提交~");
			loadingDialog.show(false);
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String result = "";
			Map<String, String> data = new HashMap<String, String>();
			data.put("phonenum", phonenum);
			data.put("password", password);
			try {
				result = HttpRequest.sendModifyCodeRequest(data);
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
			// 修改操作 head 111-修改成功 011-该用户不存在
			Log.v("reg", result);
			JSONObject jsonObject = null;
			try {
				jsonObject = new JSONObject(result);
				String head = jsonObject.getString("head");
				if ("111".equals(head)) {
					loadingDialog.setLoadingImage(R.drawable.ic_success);
					loadingDialog.setLoadingTip("修改成功！");
					new Handler().postDelayed(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							loadingDialog.dismiss();
						}
					}, 2000);
				
				} else if ("011".equals(head)) {
					loadingDialog.dismiss();
					Toast.makeText(ModifyCodeActivity.this, "该用户不存在！", 1).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
