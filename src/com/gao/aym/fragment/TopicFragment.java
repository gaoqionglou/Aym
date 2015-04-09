package com.gao.aym.fragment;

import com.gao.aym.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

public class TopicFragment extends Fragment {
    public int index;
    private ImageView image;
    private View view;
	private LayoutInflater inflater;
	public static TopicFragment getInstance(int index){
		TopicFragment imageFragment = new TopicFragment();
		imageFragment.index=index; 
		return imageFragment;
	}
	public TopicFragment() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		inflater = LayoutInflater.from(getActivity());
		if (view == null) {
			view = inflater.inflate(R.layout.view_topic,null);
		}
		image=(ImageView)view.findViewById(R.id.image);
		if(index==0){
			image.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.topic_1));
		}else if(index==1){
			image.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.topic_2));
		}
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
}
