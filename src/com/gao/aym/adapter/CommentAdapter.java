package com.gao.aym.adapter;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.gao.aym.R;
import com.gao.aym.bean.CommentInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CommentAdapter extends BaseAdapter {
	private Context context;
	private LinkedList<CommentInfo> comments;
	private LayoutInflater inflater;
	private Holder holder;
	private int[] res = { R.drawable.f1, R.drawable.f2, R.drawable.f3,
			R.drawable.f4, R.drawable.f5, R.drawable.f6, R.drawable.f7,
			R.drawable.f8, R.drawable.f9, R.drawable.f10, R.drawable.f11,
			R.drawable.f12, R.drawable.f13, R.drawable.f14, R.drawable.f15,
			R.drawable.f16, R.drawable.f17, R.drawable.f18, R.drawable.f19,
			R.drawable.f20 };

	public CommentAdapter(Context context, LinkedList<CommentInfo> comments) {
		this.context = context;
		this.comments = comments;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return comments.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return comments.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup group) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.view_comment_item, null);
			holder = new Holder();
			holder.com_icon=(ImageView)convertView.findViewById(R.id.com_icon);
			holder.com_date = (TextView) convertView.findViewById(R.id.com_date);
			holder.com_level = (TextView) convertView.findViewById(R.id.lou);
			holder.com_msg = (TextView) convertView.findViewById(R.id.com_msg);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		// 随机头像设置
       holder.com_icon.setBackgroundResource(res[getRandomIndex()]);
		// 发布日期
		holder.com_date.setText(comments.get(position).getDate() + "发布");
		// 楼层
		holder.com_level.setText(Integer.toString(position + 1) + "楼");
		// 评论内容
		String msg = comments.get(position).getMsg();
		if (!"".equals(msg)) {//
			holder.com_msg.setText(msg);
		}
		return convertView;
	}

	class Holder {
		ImageView com_icon;
		TextView com_msg;
		TextView com_date;
		TextView com_level;
	}
	
	private int getRandomIndex(){
		Random rd= new Random();
		int temp=Math.abs(rd.nextInt())%20;
		return temp;
	}
}
