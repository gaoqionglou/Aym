package com.gao.aym.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gao.aym.R;
import com.gao.aym.adapter.RecommendAdapter;
import com.gao.aym.adapter.SortAdapter;
import com.gao.aym.bean.ItemInfo;
import com.gao.aym.util.HttpRequest;
import com.gao.aym.util.MyApplication;
import com.gao.aym.view.CustomDialog;
import com.gao.aym.view.DisplayListView;
import com.gao.aym.view.LoadingDialog;
import com.gao.aym.view.DisplayListView.OnLoadDataListener;
import com.gao.aym.view.DisplayListView.OnRefreshListener;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class AroundFragment extends Fragment {
	private String TAG="AroundFragment";
	private LinkedList<ItemInfo> items;
	private SortAdapter adapter;
	private LoadingDialog loadingDialog;
	private CustomDialog customDialog;
	private ListView listview;
	private View view;
	private LayoutInflater inflater;
	private String location; 
	private MyApplication application;
	private String phonenum;
	private TextView ev;
	public AroundFragment() {
		// TODO Auto-generated constructor stub
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		phonenum = getArguments().getString("phonenum");
		application=(MyApplication)getActivity().getApplication();
//		location=application.city;
		location="广州市";
		Log.i(TAG, "定位城市"+location);
		loadingDialog = new LoadingDialog(getActivity(), R.style.loading_dialog);
		customDialog = new CustomDialog(getActivity());
		inflater = LayoutInflater.from(getActivity());
		if (view == null) {
			view = inflater.inflate(R.layout.view_aroundfragment,null);
		}
		ev=(TextView)view.findViewById(R.id.empty_view);
		listview=(ListView)view.findViewById(R.id.listview);
		listview.setEmptyView(ev);
		new GetData().execute();
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
		ViewGroup parent = (ViewGroup)view.getParent();
		if(parent!=null){
			parent.removeAllViewsInLayout();
		}
		return view;
    }
    class GetData extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {
        	// TODO Auto-generated method stub
        	super.onPreExecute();
        	ev.setText("正在搜索"+location+"...");
//        	loadingDialog.show(false);
        }
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String result="";
			Map<String,String> data =new HashMap<String, String>();
			data.put("type", "");
			data.put("location", location);
			data.put("from", "");
			data.put("to", "");
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
//			loadingDialog.dismiss();
			items = new LinkedList<ItemInfo>();
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
					ItemInfo item = new ItemInfo(id, phonenum, image_uri, topic, location, msg, comment_num, like_num);
					items.add(item);
				}
				if(items.size()!=0) {
				adapter=new SortAdapter(getActivity(), items, phonenum);
				listview.setAdapter(adapter);
				}else {
					ev.setText("抱歉，无法完成搜索 >_< !");
				}
				Log.i(TAG, items.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
