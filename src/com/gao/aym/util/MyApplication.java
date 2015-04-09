package com.gao.aym.util;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.gao.aym.activity.PublishActivity.MyLocationListener;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import com.nostra13.universalimageloader.utils.StorageUtils;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class MyApplication extends Application {
	// ������¼һ����¼�˺ź�����
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;
	private String phonenum, password;
	public LocationClient mLocationClient;
	private MyLocationListener mMyLocationListener;
	public String city;
	private List<Activity> activityList = new LinkedList<Activity>();
	private static MyApplication instance;

	public MyApplication() {
		// TODO Auto-generated constructor stub
	}

	public static MyApplication getInstance() {
		if (instance == null) {
			instance = new MyApplication();
		}
		return instance;
	}

	// ���Activity��������
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	// ��������Activity��finish
	public void exit() {
		for (Activity activity : activityList) {
			activity.finish();
		}
		System.exit(0);
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// ���û���·��
		File cacheDir = StorageUtils.getOwnCacheDirectory(
				getApplicationContext(), "Aym/Cache");
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
				.memoryCacheExtraOptions(480, 800)
				// max width, max height���������ÿ�������ļ�����󳤿�
				.threadPoolSize(2)
				// �̳߳��ڼ��ص��������Ƽ�1-5��ֹOOM
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				// .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 *
				// 1024)) // You can pass your own memory cache
				// implementation/�����ͨ���Լ����ڴ滺��ʵ��
				.memoryCache(new WeakMemoryCache())
				.memoryCacheSize(2 * 1024 * 1024)
				.discCacheSize(50 * 1024 * 1024)
				// .discCacheFileNameGenerator(new
				// Md5FileNameGenerator())//�������ʱ���URI������MD5 ����
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.discCacheFileCount(100) // ������ļ�����
				.discCache(new UnlimitedDiscCache(cacheDir))// �Զ��建��·��
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.imageDownloader(
						new BaseImageDownloader(getApplicationContext(),
								5 * 1000, 30 * 1000)) // connectTimeout (5 s),
														// readTimeout (30
														// s)��ʱʱ��
				.writeDebugLogs() // Remove for release app
				.build();// ��ʼ����
		// Initialize ImageLoader with configuration
		ImageLoader.getInstance().init(config);

		mLocationClient = new LocationClient(this.getApplicationContext());
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		InitLocation();
		mLocationClient.start();
		if (mLocationClient.isStarted()) {
			mLocationClient.stop();
		}
	}

	private void InitLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// ���ö�λģʽ
		option.setCoorType("bd09ll");// ���صĶ�λ����ǰٶȾ�γ�ȣ�Ĭ��ֵgcj02
		int span = 1000;
		option.setScanSpan(span);// ���÷���λ����ļ��ʱ��Ϊ5000ms
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
	}

	/**
	 * ʵ��ʵλ�ص�����
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// Receive Location
			city = location.getCity();
		}

	}

	public String getCity() {
		return city;
	}

	public void setPhoneNum(String phonenum) {
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		editor = sharedPreferences.edit();
		editor.putString("phonenum", phonenum);
		editor.commit();
	}

	public void setPassword(String password) {
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		editor = sharedPreferences.edit();
		editor.putString("password", password);
		editor.commit();
	}

	public String getPhonenum() {
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		return sharedPreferences.getString("phonenum", "");
	}

	public String getPassword() {
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		return sharedPreferences.getString("password", "");
	}
}
