package com.gao.aym.activity;

import java.io.File;
import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.gao.aym.R;
import com.gao.aym.util.HttpRequest;
import com.gao.aym.util.HttpRequest.DownloadProgressListener;
import com.gao.aym.util.MyApplication;
import com.gao.aym.view.CustomDialog;
import com.gao.aym.view.LoadingDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.media.audiofx.EnvironmentalReverb;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends BaseActivity {
	private String TAG="SettingsActivity";
	private RelativeLayout binding;
	private TextView tv_bind, tv_clear, tv_check, tv_about;
	private Button btn_quit;
	private ImageButton btn_back;
	private MyApplication application;
	private String phonenum;
	private boolean isBind;
    private CustomDialog dialog;
    private ProgressDialog progressDialog;
    private File downloadFile;
    private Handler handler;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		overridePendingTransition(R.anim.right_in, R.anim.left_out);
		MyApplication.getInstance().addActivity(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		application = (MyApplication) getApplication();
		phonenum = application.getPhonenum();
		Log.i(TAG, "phonenum->"+phonenum);
		downloadFile=new File(Environment.getExternalStorageDirectory(), "aym.apk");
		handler = new Handler(){
			public void handleMessage(Message msg) {
				if(msg.what==1){
					long downloaded = msg.getData().getLong("downloaded");
					long total = msg.getData().getLong("total");
				
					long precent =downloaded*100/total;
					progressDialog.setProgress((int)precent);
					if(progressDialog.getProgress() == progressDialog.getMax()){
						Toast.makeText(getApplicationContext(), "���سɹ�", 1).show();
						progressDialog.dismiss();
						//������ɺ��apk��װ
						  Intent intent = new Intent();  
					        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
					        intent.setAction(android.content.Intent.ACTION_VIEW);  
					        intent.setDataAndType(Uri.fromFile(downloadFile), "application/vnd.android.package-archive");  
					        startActivity(intent);  
					
					}
				}
			};
		};
		initView();

	}

	private void initView() {
		// TODO Auto-generated method stub
		dialog = new CustomDialog(this);
		binding = (RelativeLayout) findViewById(R.id.rl);
		tv_bind = (TextView) findViewById(R.id.tv_bind);
		tv_clear = (TextView) findViewById(R.id.tv_clear);
		tv_check = (TextView) findViewById(R.id.tv_check);
		tv_about = (TextView) findViewById(R.id.tv_about);
		btn_quit = (Button) findViewById(R.id.btn_quit);
		btn_back=(ImageButton)findViewById(R.id.btn_back);
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("�������� ...");
		progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
		progressDialog.setProgress(0);
		progressDialog.setMax(100);
//		progressDialog.setCancelable(true);
//		progressDialog.setCanceledOnTouchOutside(false);
		if (phonenum != null &&! "".equals(phonenum)) {
			tv_bind.setText("�Ѱ�");
			isBind=true;
		} else {
			isBind=false;
			tv_bind.setText("δ��");
			tv_bind.setTextColor(Color.RED);
		}
		btn_quit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyApplication.getInstance().exit();;
			}
		});
		btn_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		binding.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!isBind){
				Intent intent = new Intent(SettingActivity.this,LoginActivity.class);
				startActivity(intent);
				}
			}
		});
		tv_clear.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cleanInternalCache(getApplicationContext());
				cleanDatabases(getApplicationContext());//��ζ�Ž����������
			}
		});
		tv_about.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SettingActivity.this,AboutActivity.class);
				startActivity(intent);
			}
		});
		tv_check.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//������
				try {
					String versionName=getApplicationContext().getPackageManager().getPackageInfo("com.gao.aym", 0).versionName;
					Log.i(TAG, "versionName->"+versionName);
					new CheckUpdate().execute(versionName);
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	class CheckUpdate extends AsyncTask<String,String,String>{

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String result="";
			try {
				result=HttpRequest.getUpdateUrl(arg0[0]);
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
			final String url =result;
			Log.i("update","resultUrl->"+ result);
			if(!result.equals("")){
				//��ʾ����
				dialog.setDialogTitle("��ʾ");
				dialog.setDialogMsg("��⵽�°汾���Ƿ��������£�");
				dialog.setBtnLeft("ȷ��", new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//����apk
						dialog.dismiss();
						new DownloadTask().execute(url);
						
					}
				});
				dialog.setBtnRght("ȡ��", new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				dialog.show();
			}else{
				//��ʾû�ø���
				dialog.setDialogTitle("��ʾ");
				dialog.setDialogMsg("�������°汾");
				dialog.setBtnLeftVisiblity(View.GONE);
				dialog.setBtnRght("ȷ��", new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				dialog.show();
			}
		}
	}
   
	class DownloadTask extends AsyncTask<String,String,String>{
       @Override
    protected void onPreExecute() {
    	// TODO Auto-generated method stub
    	super.onPreExecute();
    	progressDialog.show();
    }
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub 
			
			HttpRequest.download(downloadFile, arg0[0], new DownloadProgressListener() {
				
				@Override
				public void download(long downloaded, long max) {
					// TODO Auto-generated method stub
					Message msg = new Message();
					msg.what=1;
					Bundle bundle  = new Bundle();
					bundle.putLong("downloaded", downloaded);
					bundle.putLong("total", max);
					msg.setData(bundle);
					handler.sendMessage(msg);
				}
			});
			return null;
		}
		
	}
	
	/**
	 * �����Ӧ���ڲ�����(/data/data/com.xxx.xxx/cache)
	 * 
	 * @param context
	 */
	public static void cleanInternalCache(Context context) {
		deleteFilesByDirectory(context.getCacheDir());
	}

	/**
	 * �����Ӧ���������ݿ�(/data/data/com.xxx.xxx/databases)
	 * 
	 * @param context
	 */
	public static void cleanDatabases(Context context) {
		deleteFilesByDirectory(new File("/data/data/"+ context.getPackageName() + "/databases"));
	}

	/**
	 * ɾ������ ����ֻ��ɾ��ĳ���ļ����µ��ļ�����������directory�Ǹ��ļ�������������
	 * 
	 * @param directory
	 */
	private static void deleteFilesByDirectory(File directory) {
		if (directory != null && directory.exists() && directory.isDirectory()) {
			for (File item : directory.listFiles()) {
				item.delete();
			}
		}
	}
}
