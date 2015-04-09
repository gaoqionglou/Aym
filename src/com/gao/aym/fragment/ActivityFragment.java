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
import com.gao.aym.adapter.ActivityAdapter;
import com.gao.aym.adapter.RecommendAdapter;
import com.gao.aym.bean.ActivityInfo;
import com.gao.aym.bean.ItemInfo;
import com.gao.aym.util.HttpRequest;
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
import android.widget.LinearLayout;
import android.widget.ListView;
 
public class ActivityFragment extends Fragment {
	private LinkedList<ActivityInfo> activityInfos;
	private LoadingDialog loadingDialog;
	private CustomDialog customDialog;
	private View view;
	private LayoutInflater inflater;
	private ListView act_listview;
	private ActivityAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		loadingDialog = new LoadingDialog(getActivity(), R.style.loading_dialog);
		customDialog = new CustomDialog(getActivity());
		inflater = LayoutInflater.from(getActivity());
		if(view==null){
			view = inflater.inflate(R.layout.view_activityfragment, null);
		}
		act_listview =(ListView)view.findViewById(R.id.act_listview);
		new InitData().execute();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ViewGroup parent = (ViewGroup)view.getParent();
		if(parent!=null){
			parent.removeAllViewsInLayout();
			Log.v("aym", "Af->ÒÑÒÆ³ý´æÔÚµÄview");
		}
		return view;
	}
	
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
			String type="all";
		    try {
				result=HttpRequest.getActivityList(type);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    Log.i("activity", " result ->"+result);
			return result;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dismissDialog();
			activityInfos=new LinkedList<ActivityInfo>();
			try {
				JSONArray jsonArray = new JSONArray(result);
				for(int i=0;i<jsonArray.length();i++){
					JSONObject jsonObject = (JSONObject) jsonArray.get(i);
					int id = jsonObject.getInt("id");
					String title = jsonObject.getString("title");
					String imageUri = jsonObject.getString("imageUri");
					String startTime =jsonObject.getString("startTime");
					String endTime =jsonObject.getString("endTime");
					int state =jsonObject.getInt("state");
					ActivityInfo infos = new ActivityInfo(id, title, imageUri, startTime, endTime, state);
					activityInfos.add(infos);
				}
				adapter = new ActivityAdapter(getActivity(), activityInfos);
				act_listview.setAdapter(adapter);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
