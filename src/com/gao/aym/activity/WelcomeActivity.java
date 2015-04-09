package com.gao.aym.activity;

import com.gao.aym.R;
import com.gao.aym.fragment.ImageFragment;
import com.gao.aym.util.MyApplication;
import com.viewpagerindicator.CirclePageIndicator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class WelcomeActivity extends BaseActivity implements OnClickListener {
	private CirclePageIndicator indicator;
	private ViewPager viewPager;
	private Button btn_scan, btn_login, btn_reg;
	private MyFragmentAdapter adapter;
	int[] index = { 0, 1, 2 };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		overridePendingTransition(R.anim.right_in, R.anim.left_out);
		MyApplication.getInstance().addActivity(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wel);
		initView();
		adapter = new MyFragmentAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);
		indicator.setViewPager(viewPager);
	}

	private void initView() {
		// TODO Auto-generated method stub
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_reg = (Button) findViewById(R.id.btn_reg);
		btn_scan = (Button) findViewById(R.id.btn_scan);
		indicator = (CirclePageIndicator) findViewById(R.id.indicator);
		viewPager = (ViewPager) findViewById(R.id.pager);
		btn_login.setOnClickListener(this);
		btn_reg.setOnClickListener(this);
		btn_scan.setOnClickListener(this);
	}

	class MyFragmentAdapter extends FragmentPagerAdapter {

		public MyFragmentAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int position) {
			// TODO Auto-generated method stub
			return ImageFragment.getInstance(index[position % index.length]);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return index.length;
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = null;
		switch (v.getId()) {
		case R.id.btn_login:
            intent = new Intent(WelcomeActivity.this,LoginActivity.class);
            startActivity(intent);
			break;
		case R.id.btn_reg:
            intent = new Intent(WelcomeActivity.this,RegisterActivity.class);
            startActivity(intent);
			break;
		case R.id.btn_scan:
            //游客模式登录
			 intent = new Intent(WelcomeActivity.this,MomentActivity.class);
	         intent.putExtra("phonenum", "");
			 startActivity(intent);
			break;
		default:
			break;
		}
	}

}
