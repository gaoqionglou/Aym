package com.gao.aym.adapter;

import java.util.List;

import com.gao.aym.R;
import com.gao.aym.activity.PublishActivity;
import com.gao.aym.activity.SortActivity;
import com.gao.aym.bean.TopicInfo;
import com.gao.aym.util.HttpRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TopicAdapter extends BaseAdapter {
	private Context context;
	private List<TopicInfo> topics;
	private LayoutInflater inflater;
	private DisplayImageOptions options;
    private ViewHolder holder;
	public TopicAdapter(Context context, List<TopicInfo> topics) {
		super();
		this.context = context;
		this.topics = topics;
		this.inflater = LayoutInflater.from(context);
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.bg_pic_loading) // 设置图片在下载期间显示的图片
				.showImageForEmptyUri(R.drawable.bg_pic_loading)// 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.bg_pic_loading) // 设置图片加载/解码过程中错误时候显示的图片
				.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
				.cacheInMemory(true)// 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true)// 设置下载的图片是否缓存在SD卡中
				.displayer(new FadeInBitmapDisplayer(100))// 是否图片加载好后渐入的动画时间
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();// 构建完成
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return topics.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return topics.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		if(convertView==null){
			convertView=inflater.inflate(R.layout.view_topicitem, null);
			holder = new ViewHolder();
			holder.image=(ImageView)convertView.findViewById(R.id.topic_image);
			holder.tv_title=(TextView)convertView.findViewById(R.id.topic_title);
			holder.tv_introduction=(TextView)convertView.findViewById(R.id.topic_introduction);
			holder.tv_num=(TextView)convertView.findViewById(R.id.topic_num);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		holder.tv_title.setText(topics.get(position).getTitle());
		holder.tv_introduction.setText(topics.get(position).getIntroduction());
//		holder.tv_num.setText(topics.get(position).getNum()+"个帖子");
		ImageLoader imageloader = ImageLoader.getInstance();
		 imageloader.displayImage(HttpRequest.HOST+topics.get(position).getImageUri(),holder.image ,options, new AnimateFirstDisplayListener());
		convertView.setOnClickListener(new MyClickListener(position));
		 return convertView;
	}

	class ViewHolder {
		ImageView image;
		TextView tv_title, tv_introduction, tv_num;
	}
	
	class MyClickListener implements View.OnClickListener{
        int p;
		public MyClickListener(int position){
			this.p=position;
		}
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
		    //跳转到该分类页面
		    String topic = topics.get(p).getTitle();
		    Intent intent = new Intent(context,SortActivity.class);
		    intent.putExtra("topic", topic);
		    context.startActivity(intent);
		}
		
	}
}
