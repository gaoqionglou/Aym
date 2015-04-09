package com.gao.aym.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gao.aym.R;
import com.gao.aym.adapter.TopicAdapter;
import com.gao.aym.bean.TopicInfo;
import com.gao.aym.fragment.TopicFragment;
import com.gao.aym.util.HttpRequest;
import com.gao.aym.util.MyApplication;
import com.viewpagerindicator.CirclePageIndicator;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;

public class TopicActivity extends BaseActivity {
	private ImageButton btn_back;
	private ListView topic_list;
	private CirclePageIndicator indicator;
	private ViewPager viewPager;
	private MyFragmentAdapter adapter;
	private TopicAdapter topicAdapter;
	private ProgressBar loadingBar;
	private List<TopicInfo> topics;
    private int[] index={0,1};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		overridePendingTransition(R.anim.right_in, R.anim.left_out);
		MyApplication.getInstance().addActivity(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_topic);
		topics = new ArrayList<TopicInfo>();
		initView();
		new  GetTopicTask().execute();
	}

	private void initView() {
		// TODO Auto-generated method stub
		btn_back=(ImageButton)findViewById(R.id.btn_back);
		indicator = (CirclePageIndicator) findViewById(R.id.indicator);
		viewPager = (ViewPager) findViewById(R.id.pager);
		btn_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		adapter = new MyFragmentAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);
		indicator.setViewPager(viewPager);
		loadingBar =(ProgressBar)findViewById(R.id.loadingBar);
		topic_list=(ListView)findViewById(R.id.topic_listview);
		topic_list.setEmptyView(loadingBar);
	}
	
	class MyFragmentAdapter extends FragmentPagerAdapter {

		public MyFragmentAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int position) {
			// TODO Auto-generated method stub
			return TopicFragment.getInstance(index[position % index.length]);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return index.length;
		}

	}
	
	class GetTopicTask extends AsyncTask<String,String,String>{
        
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String result="";
			try {
				result=HttpRequest.getTopic();
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
			try {
				JSONArray jsonArray = new JSONArray(result);
				for(int i =0;i<jsonArray.length();i++){
					JSONObject jsonObject = (JSONObject) jsonArray.get(i);
					int id=jsonObject.getInt("id");
					String title=jsonObject.getString("title");
					String imageUri=jsonObject.getString("imageUri");
					String introduction=jsonObject.getString("introduction");
					int num = jsonObject.getInt("num");
					TopicInfo topic = new TopicInfo(id, title, imageUri, introduction, num); 
					topics.add(topic);
				}
				Log.i("topic", topics.toString());
				topicAdapter = new TopicAdapter(TopicActivity.this, topics);
				topic_list.setAdapter(topicAdapter);
				setListViewHeightBasedOnChildren(topic_list);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	/**
	 * 动态设置ListView的高度,listview的item布局需为线性布局
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) { 
	    if(listView == null) return;

	    TopicAdapter listAdapter = (TopicAdapter) listView.getAdapter(); 
	    if (listAdapter == null) { 
	        // pre-condition 
	        return; 
	    } 

	    int totalHeight = 0; 
	    for (int i = 0; i < listAdapter.getCount(); i++) { 
	        View listItem = listAdapter.getView(i, null, listView); 
	        listItem.measure(0, 0); 
	        totalHeight += listItem.getMeasuredHeight(); 
	    } 

	    ViewGroup.LayoutParams params = listView.getLayoutParams(); 
	    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)); 
	    listView.setLayoutParams(params); 
	}

}
