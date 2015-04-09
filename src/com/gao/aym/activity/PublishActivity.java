package com.gao.aym.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.IvParameterSpec;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.gao.aym.R;
import com.gao.aym.util.HttpRequest;
import com.gao.aym.util.MyApplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 发布
 */
public class PublishActivity extends BaseActivity {

	private ImageButton btn_back;// 后退按钮
	private Button btn_publish;// 发布按钮
	private EditText ed_publish;// 发布内容
	private String content;// 发布内容
	// @人，相机，相册
	private ImageButton btn_contact, btn_camera, btn_album;
	// 图片
	private ImageView image;
	// 定位相关
	private static BDLocation location = null;
	private ImageView ic_location;
	private ProgressBar progressBar;
	private TextView tv_location;
	private String address;
	private String city;
	// intent 照相，相册，裁剪
	private Intent take_pic, album_pic, crop_pic;
	private Uri mPhotoUriOnSDcard;
	private String mPhotoPath;
	private File mPhotoFile;
	private MyApplication application;
	private String photoname;
	private String phonenum;// 当前登录用户手机号
	private byte[] img_data;
	private LocationClient mLocationClient;
	private MyLocationListener mMyLocationListener;
	private String person = null;
	private String topic = null;
	private static final String TAG = "PublishActivity";
	private static final int CAMERA_TAKE_PIC = 1;
	private static final int CROP_PIC = 2;
	private static final int CHOOSE_ALBUM_PIC = 3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		overridePendingTransition(R.anim.right_in, R.anim.left_out);
		MyApplication.getInstance().addActivity(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish);
		application = (MyApplication) getApplication();
		phonenum = application.getPhonenum();
		topic = getIntent().getStringExtra("topic");
		person = getIntent().getStringExtra("person");
		Log.i(TAG, "当前登录用户 ->" + application.getPhonenum());
		mLocationClient = new LocationClient(this.getApplicationContext());
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		InitLocation();
		mLocationClient.start();
		if (mLocationClient.isStarted()) {
			mLocationClient.stop();
		}
		initView();
		setListener();
	}

	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// Receive Location
			city = location.getCity();
			progressBar.setVisibility(View.INVISIBLE);
			ic_location.setVisibility(View.VISIBLE);
			tv_location.setText(city);

		}

	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		// TODO Auto-generated method stub
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		btn_publish = (Button) findViewById(R.id.btn_publish);

		ed_publish = (EditText) findViewById(R.id.ed_publish);
		btn_contact = (ImageButton) findViewById(R.id.contact);
		btn_camera = (ImageButton) findViewById(R.id.camera);
		btn_album = (ImageButton) findViewById(R.id.album);
		image = (ImageView) findViewById(R.id.pic);
		ic_location = (ImageView) findViewById(R.id.iv_location);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		tv_location = (TextView) findViewById(R.id.tv_loaction);
		tv_location.setText("获取位置...");
	}

	private void InitLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度，默认值gcj02
		int span = 1000;
		option.setScanSpan(span);// 设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
	}

	/**
	 * 设置监听器
	 */
	private void setListener() {
		btn_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		btn_publish.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 发布
				boolean hasImageUri = false;
				if (img_data != null) {
					Log.i(TAG, "img_data");
					hasImageUri = true;
					new UploadImage().execute();
				}
				new PublishTask(hasImageUri).execute();
			}
		});
		btn_contact.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// @人
				finish();
				Intent contact = new Intent(PublishActivity.this,
						ContactActivity.class);
				startActivity(contact);
			}
		});
		btn_camera.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 打开相机截图，截大图
				take_pic = new Intent("android.media.action.IMAGE_CAPTURE");
				mPhotoPath = Environment.getExternalStorageDirectory()
						+ "/Aym/Camera" + getPhotoFileName();
				mPhotoFile = new File(mPhotoPath);
				if (!mPhotoFile.exists()) {
					try {
						mPhotoFile.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				mPhotoUriOnSDcard = Uri.fromFile(mPhotoFile);
				take_pic.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUriOnSDcard);
				startActivityForResult(take_pic, CAMERA_TAKE_PIC);
			}
		});
		btn_album.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 相册截图
				getPhotoFileName();
				album_pic = new Intent(Intent.ACTION_GET_CONTENT, null);
				album_pic.setType("image/*");
				album_pic.putExtra("crop", "true");
				album_pic.putExtra("aspectX", 2);
				album_pic.putExtra("aspectY", 1);
				album_pic.putExtra("outputX", 600);
				album_pic.putExtra("outputY", 400);
				album_pic.putExtra("scale", true);
				album_pic.putExtra("return-data", true);
				album_pic.putExtra("outputFormat",
						Bitmap.CompressFormat.PNG.toString());
				album_pic.putExtra("noFaceDetection", true); // no face
																// detection
				startActivityForResult(album_pic, CHOOSE_ALBUM_PIC);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case CAMERA_TAKE_PIC:
			cropImageUri(mPhotoUriOnSDcard, 800, 400, CROP_PIC);
			break;
		case CROP_PIC:
			if (mPhotoUriOnSDcard != null) {
				image.setVisibility(View.VISIBLE);
				Bitmap bitmap = decodeUriAsBitmap(mPhotoUriOnSDcard);
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.PNG, 50, outputStream);
				img_data = outputStream.toByteArray();
				image.setImageBitmap(bitmap);

			} else {
				Log.e(TAG, "CROP_PIC: data = " + data);
			}
			break;
		case CHOOSE_ALBUM_PIC:
			Log.d(TAG, "CHOOSE_ALBUM_PIC : data = " + data);// null ?
			if (data != null) {
				image.setVisibility(View.VISIBLE);
				Bitmap bitmap = data.getParcelableExtra("data");
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.PNG, 50, outputStream);
				img_data = outputStream.toByteArray();
				image.setImageBitmap(bitmap);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 裁剪
	 * 
	 * @param uri
	 * @param outputX
	 *            800
	 * @param outputY
	 *            400
	 * @param requestCode
	 */
	private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode) {
		crop_pic = new Intent("com.android.camera.action.CROP");
		crop_pic.setDataAndType(uri, "image/*");
		crop_pic.putExtra("crop", "true");
		crop_pic.putExtra("aspectX", 2);
		crop_pic.putExtra("aspectY", 1);
		crop_pic.putExtra("outputX", outputX);
		crop_pic.putExtra("outputY", outputY);
		crop_pic.putExtra("scale", true);
		crop_pic.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		crop_pic.putExtra("return-data", false);
		crop_pic.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
		crop_pic.putExtra("noFaceDetection", true); // no face detectionS
		startActivityForResult(crop_pic, requestCode);

	}

	/**
	 * @param uri
	 * @return
	 */
	private Bitmap decodeUriAsBitmap(Uri uri) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(getContentResolver()
					.openInputStream(uri));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

	/**
	 * @return
	 */
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_" + "'"
				+ phonenum + "'_" + "yyyyMMdd_HHmmss");
		photoname = dateFormat.format(date) + ".png";
		return photoname;
	}

	class UploadImage extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			try {
				HttpRequest.uploadImage(img_data, photoname);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

	}

	class PublishTask extends AsyncTask<String, String, String> {
		private boolean hasImageUri;

		public PublishTask(boolean hasImageUri) {
			this.hasImageUri = hasImageUri;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			Log.i(TAG, " into publish task");
		}

		@Override
		protected String doInBackground(String... params) {
			String result = "";

			content = ed_publish.getText().toString();

			Map<String, String> data = new HashMap<String, String>();
			data.put("phonenum", phonenum);
			if (hasImageUri) {
				data.put("image_uri", "images/" + photoname);
			} else {
				data.put("image_uri", "");
			}
			if (topic != null) {
				data.put("topic", topic);
			} else {
				data.put("topic", "");
			}
			data.put("msg", content);
			data.put("location", city);
			data.put("comment_num", "0");
			data.put("like_num", "0");
			Log.i(TAG, "请求参数->" + data.toString());
			try {
				result = HttpRequest.publish(data);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Log.i(TAG, "发布结果->" + result);
			if ("1".equals(result)) {
				Toast.makeText(PublishActivity.this, "发表成功", 1).show();
				finish();
			}
		}
	}

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		person = getIntent().getStringExtra("person");
		topic = getIntent().getStringExtra("topic");
		if (person != null) {
			ed_publish.append("@" + person + " ");
		}

	}
}
