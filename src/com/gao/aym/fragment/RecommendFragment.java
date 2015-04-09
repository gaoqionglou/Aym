package com.gao.aym.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gao.aym.R;
import com.gao.aym.adapter.RecommendAdapter;
import com.gao.aym.bean.ItemInfo;
import com.gao.aym.util.HttpRequest;
import com.gao.aym.view.CustomDialog;
import com.gao.aym.view.DisplayListView;
import com.gao.aym.view.DisplayListView.OnLoadDataListener;
import com.gao.aym.view.DisplayListView.OnRefreshListener;
import com.gao.aym.view.LoadingDialog;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup; 
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class RecommendFragment extends Fragment {
	
	private LinkedList<ItemInfo> items;
	private RecommendAdapter adapter;
	private LoadingDialog loadingDialog;
	private CustomDialog customDialog;
	private DisplayListView rec_listview;
	private View view;
	private LayoutInflater inflater;
    private static  int from; 
    private static int to;
    private static int max;
    private int temp;//用于临时存放max
    private GetMaxSizeTask getMaxSizeTask;
    private InitData initData;
    private String type="recommed";
	private String phonenum;
    public RecommendFragment() {
		// TODO Auto-generated constructor stub
		
	}
  
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		phonenum = getArguments().getString("phonenum");
		Log.i("RF", phonenum);
		loadingDialog = new LoadingDialog(getActivity(), R.style.loading_dialog);
		customDialog = new CustomDialog(getActivity());
		inflater = LayoutInflater.from(getActivity());
		if (view == null) {
			view = inflater.inflate(R.layout.view_recommendfragment,null);
		}
		rec_listview = (DisplayListView) view.findViewById(R.id.rec_listview);
		
		// 上拉加载更多
		rec_listview.setOnLoadDataListener(new OnLoadDataListener() {

			@Override
			public void onLoad() {
				// TODO Auto-generated method stub
                Log.v("上拉", "1");
				new GetMoreData().execute();
				Log.v("上拉", "2");
			}
		});
		// 下拉刷新
		rec_listview.setonRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				new LoadData().execute(type);
				
			}
		});
	
		getMaxSizeTask = new GetMaxSizeTask();
		getMaxSizeTask.execute(type);
//		new InitData().execute("1","10");
		
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		ViewGroup parent = (ViewGroup)view.getParent();
		if(parent!=null){
			parent.removeAllViewsInLayout();
			Log.v("aym", "rf->已移除存在的view");
		}
		
		return view;
	}
	
	/**
	 * @author Administrator
	 *
	 */
	class GetMaxSizeTask extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String result = "";
			try {
				 result = HttpRequest.getMaxSize(params[0]);
				temp= Integer.parseInt(result);
				Log.i("getmax", type+","+temp);
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
			}else{ //max不为0
			    int a = flag-max;
			    String msg = a>=0?"暂无新消息":"为你更新"+a+"条消息";
			    rec_listview.onRefreshComplete();
				Toast.makeText(getActivity(), msg,1).show();
			}
			to=max;
			from=to-4;
			new InitData().execute(Integer.toString(from),Integer.toString(to));
		}
	}
	
	
	/**
	 * @author Administrator
	 *
	 */
	class InitData extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {
        	// TODO Auto-generated method stub
        	super.onPreExecute();
        	loadingDialog.show(false);
        	
        }
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String result="";
			Map<String,String> data =new HashMap<String, String>();
			data.put("type", "recommed");
			data.put("location", "");
			data.put("from", params[0]);
			data.put("to", params[1]);
			
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
			dismissDialog();
			items = new LinkedList<ItemInfo>();
			Log.i("aa", "items->"+result);
			try {
				JSONArray jsonArray = new JSONArray(result);
				for(int i=0;i<jsonArray.length();i++){
					JSONObject jsonObject=(JSONObject) jsonArray.get(i);
					int id = jsonObject.getInt("id");
					String phonenum=jsonObject.getString("phonenum");
					String image_uri=jsonObject.getString("image_uri");
					String topic = jsonObject.getString("topic");
					String location=jsonObject.getString("location");
					String msg=jsonObject.getString("msg");
					String comment_num=jsonObject.getString("comment_num");
					String like_num =jsonObject.getString("like_num");
					String maxsize = jsonObject.getString("maxsize");
					ItemInfo item = new ItemInfo(id, phonenum, image_uri, topic, location, msg, comment_num, like_num);
					item.setMaxsize(maxsize);
					items.add(item);
				}
				adapter = new RecommendAdapter(getActivity(), items,phonenum);
				rec_listview.setAdapter(adapter);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	class GetMoreData extends AsyncTask<String, String, String>{
        
		@Override
        protected void onPreExecute() {
        	// TODO Auto-generated method stub
        	super.onPreExecute();
        	to=from-1;
        	from=to-4;
        	
        	if(from<0){
        		from=1;
        	}
        	Log.i("after", from+"->"+to);
        }
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String result="";
			Map<String,String> data =new HashMap<String, String>();
			data.put("type", "recommed");
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
				for(int i=0;i<jsonArray.length();i++){
					JSONObject jsonObject=(JSONObject) jsonArray.get(i);
					int id = jsonObject.getInt("id");
					String phonenum=jsonObject.getString("phonenum");
					String image_uri=jsonObject.getString("image_uri");
					String topic = jsonObject.getString("topic");
					String location=jsonObject.getString("location");
					String msg=jsonObject.getString("msg");
					String comment_num=jsonObject.getString("comment_num");
					String like_num =jsonObject.getString("like_num");
					String maxsize = jsonObject.getString("maxsize");
					ItemInfo item = new ItemInfo(id, phonenum, image_uri, topic, location,msg, comment_num, like_num);
					item.setMaxsize(maxsize);
					items.addLast(item);
					adapter.notifyDataSetChanged();
				}
                if(from==1){
                	rec_listview.onLoadComplete();
                	Toast.makeText(getActivity(), "最后一页", 1).show();
                }
			}catch (Exception e) {
					// TODO: handle exception
				}
			
		}
	}
	
	class LoadData extends AsyncTask<String,String,String>{
	    
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String result = "";
			try {
				 result = HttpRequest.getMaxSize(params[0]);
				temp= Integer.parseInt(result);
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
			    String msg = a==0?"暂无新消息":"为你更新"+a+"条消息";
			    max=flag;
			    to=max;
				from=to-4;
			    rec_listview.onRefreshComplete();
				Toast.makeText(getActivity(), msg,1).show();
				new InitData().execute(Integer.toString(from),Integer.toString(to));
			}
			
		}
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		loadingDialog.dismiss();
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		loadingDialog.dismiss();
	}
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		loadingDialog.dismiss();
	}
	
	void dismissDialog(){
		 if (getActivity()!=null&&!getActivity().isFinishing()) {
             loadingDialog.dismiss();
         }
	}
}
