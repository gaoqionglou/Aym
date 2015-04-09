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
import android.widget.LinearLayout;

public class ImageFragment extends Fragment {
    public int index;
	public static ImageFragment getInstance(int index){
		ImageFragment imageFragment = new ImageFragment();
		imageFragment.index=index; 
		return imageFragment;
	}
	public ImageFragment() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ImageView image = new ImageView(getActivity());
		if(index==0){
			image.setBackgroundResource(R.drawable.guide_scroll_0);
		}else if(index==1){
			image.setBackgroundResource(R.drawable.guide_scroll_1);
		}else if(index==2){
			image.setBackgroundResource(R.drawable.guide_scroll_2);
		}
		LinearLayout linearLayout = new LinearLayout(getActivity());
		linearLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		linearLayout.setGravity(Gravity.CENTER);
		linearLayout.addView(image);
		return linearLayout;
	}
}
