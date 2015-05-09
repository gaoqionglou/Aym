package com.gao.aym.activity;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gao.aym.R;
import com.gao.aym.adapter.SortAdapter;
import com.gao.aym.bean.ItemInfo;
import com.gao.aym.util.HttpRequest;
import com.gao.aym.util.MyApplication;
import com.gao.aym.view.DisplayListView;
import com.gao.aym.view.DisplayListView.OnLoadDataListener;
import com.gao.aym.view.DisplayListView.OnRefreshListener;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SortActivity extends BaseActivity {
	private TextView top_title;
	private DisplayListView sortlist;
	private ImageButton btn_back;
	private Button btn_publish;
	private String topic = null;
	private static int from;
	private static int to;
	private static int max;
	private int temp;// 用于临时存放max
	private LinkedList<ItemInfo> items;
	private SortAdapter adapter;
	private String phonenum;
	private MyApplication application;
   
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		overridePendingTransition(R.anim.right_in, R.anim.left_out);
		MyApplication.getInstance().addActivity(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sort);
		application = (MyApplication) getApplication();
		phonenum = application.getPhonenum();
		topic = getIntent().getStringExtra("topic");
		initView();
		new  GetMaxSizeTask().execute(topic);
	}

	private void initView() {
		// TODO Auto-generated method stub
		top_title = (TextView) findViewById(R.id.topbar_title);
		if (topic != null)
			top_title.setText(topic);
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		btn_publish=(Button)findViewById(R.id.btn_publish);
		btn_publish.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SortActivity.this,PublishActivity.class);
				intent.putExtra("topic", topic);
				startActivity(intent);
			}
		});
		sortlist = (DisplayListView) findViewById(R.id.sortlist);
		sortlist.setOnLoadDataListener(new OnLoadDataListener() {

			@Override
			public void onLoad() {
				// TODO Auto-generated method stub
				new GetMoreData().execute(topic);
			}
		});
		sortlist.setonRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				new LoadData().execute(topic);
			}
		});
	}

	class GetMaxSizeTask extends AsyncTask<String, String, String> {
		// 传topic参数获取 该topic最大行数
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String result = "";
			try {
				result = HttpRequest.getMaxSize(params[0]);
				temp = Integer.parseInt(result);
				Log.i("getmax", params[0] + "," + temp);
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
			int flag = Integer.parseInt(result);
			if (max == 0) {
				max = flag;
			} else { // max不为0
//				int a = flag - max;
//				String msg = a>= 0 ? "暂无新消息" : "为你更新" + a + "条消息";
				sortlist.onRefreshComplete();
//				Toast.makeText(SortActivity.this, msg, 1).show();
			}
			to = max;
			from = to - 4;
			new InitData().execute(topic, "1",Integer.toString(to));
		}
	}

	class InitData extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String result = "";
			Map<String, String> data = new HashMap<String, String>();
			data.put("type", params[0]);
			data.put("from", params[1]);
			data.put("to", params[2]);

			try {
				result = HttpRequest.sendSelectListRequest(data);

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
			items = new LinkedList<ItemInfo>();
			Log.i("aa", "items->" + result);
			try {
				JSONArray jsonArray = new JSONArray(result);
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = (JSONObject) jsonArray.get(i);
					int id = jsonObject.getInt("id");
					String phonenum = jsonObject.getString("phonenum");
					String image_uri = jsonObject.getString("image_uri");
					String topic = jsonObject.getString("topic");
					String location = jsonObject.getString("location");
					String msg = jsonObject.getString("msg");
					String comment_num = jsonObject.getString("comment_num");
					String like_num = jsonObject.getString("like_num");
					String maxsize = jsonObject.getString("maxsize");
					ItemInfo item = new ItemInfo(id, phonenum, image_uri,
							topic, location, msg, comment_num, like_num);
					item.setMaxsize(maxsize);
					items.add(item);
				}
				adapter = new SortAdapter(SortActivity.this, items, phonenum);
				sortlist.setAdapter(adapter);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	class GetMoreData extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			to = from - 1;
			from = to - 4;
			if (from < 0) {
				from = 1;
			}
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String result = "";
			Map<String, String> data = new HashMap<String, String>();
			data.put("type", params[0]);
			data.put("location", "");
			data.put("from", Integer.toString(from));
			data.put("to", Integer.toString(to));
			try {
				result = HttpRequest.sendSelectListRequest(data);
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
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = (JSONObject) jsonArray.get(i);
					int id = jsonObject.getInt("id");
					String phonenum = jsonObject.getString("phonenum");
					String image_uri = jsonObject.getString("image_uri");
					String topic = jsonObject.getString("topic");
					String location = jsonObject.getString("location");
					String msg = jsonObject.getString("msg");
					String comment_num = jsonObject.getString("comment_num");
					String like_num = jsonObject.getString("like_num");
					String maxsize = jsonObject.getString("maxsize");
					ItemInfo item = new ItemInfo(id, phonenum, image_uri,
							topic, location, msg, comment_num, like_num);
					item.setMaxsize(maxsize);
					items.addLast(item);
					adapter.notifyDataSetChanged();
				}
				if (from == 1) {
					sortlist.onLoadComplete();
					Toast.makeText(SortActivity.this, "最后一页", 1).show();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	class LoadData extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String result = "";
			try {
				result = HttpRequest.getMaxSize(params[0]);
				temp = Integer.parseInt(result);
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
			int flag =Integer.parseInt(result);
			if(max==0){
			max = flag;
			to=max;
			from=to-4;
			new InitData().execute(Integer.toString(from),Integer.toString(to));
			}else if(flag>=max){ //max不为0
			    int a = flag-max;
			    String msg = a>=0?"暂无新消息":"为你更新"+a+"条消息";
			    max=flag;
			    to=max;
				from=to-4;
			    sortlist.onRefreshComplete();
				Toast.makeText(SortActivity.this, msg,1).show();
				new InitData().execute(topic,Integer.toString(from),Integer.toString(to));
			}
		}
	}
}
