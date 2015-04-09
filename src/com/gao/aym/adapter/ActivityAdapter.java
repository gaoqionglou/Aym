package com.gao.aym.adapter;

import java.util.LinkedList;
import java.util.List;

import com.gao.aym.R;
import com.gao.aym.bean.ActivityInfo;
import com.gao.aym.util.HttpRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
  
public class ActivityAdapter extends BaseAdapter {
    private LinkedList<ActivityInfo> activities;
    private Context context;
    private LayoutInflater inflater;
    private ViewHolder holder;
    private DisplayImageOptions options;
    public ActivityAdapter(Context context,LinkedList<ActivityInfo> activities ){
    	this.context=context;
    	this.activities=activities;
    	this.inflater =LayoutInflater.from(this.context);
    	this.options = new DisplayImageOptions.Builder()
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
		return activities.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return activities.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup group) {
		// TODO Auto-generated method stub
		if(convertView==null){
			convertView=inflater.inflate(R.layout.view_activities, null);
			holder=new ViewHolder();
			holder.start_time=(TextView)convertView.findViewById(R.id.startTime);
			holder.end_time=(TextView)convertView.findViewById(R.id.endTime);
			holder.image=(ImageView)convertView.findViewById(R.id.image);
			holder.title=(TextView)convertView.findViewById(R.id.title);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		holder.start_time.setText(activities.get(position).getStartTime());
		holder.end_time.setText(activities.get(position).getEndTime());
		
		holder.title.setText(activities.get(position).getTitle());
		holder.image.setOnClickListener(new ImageListener());
		ImageLoader imageloader = ImageLoader.getInstance();
	    imageloader.displayImage(HttpRequest.HOST+activities.get(position).getImageUri(),holder.image ,options, new AnimateFirstDisplayListener());
		return convertView;
	}
	
	private static class ViewHolder{
		TextView start_time;
		TextView end_time;
		TextView title;
		ImageView image;
	}
   
	class ImageListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
