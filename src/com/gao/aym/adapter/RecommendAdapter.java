package com.gao.aym.adapter;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.gao.aym.R;
import com.gao.aym.R.color;
import com.gao.aym.activity.BaseActivity;
import com.gao.aym.activity.ItemActivity;
import com.gao.aym.bean.ItemInfo;
import com.gao.aym.sql.ZanService;
import com.gao.aym.util.HttpRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RecommendAdapter extends BaseAdapter {
	private Context ctx;
	private LinkedList<ItemInfo> items;
	private LayoutInflater inflater;
	private DisplayImageOptions options;
	private Holder holder;
    private String user_phonenum;
    private ZanService zanService;
	public RecommendAdapter(Context ctx, LinkedList<ItemInfo> items,String user_phonenum) {
		// TODO Auto-generated constructor stub
		this.ctx = ctx;
		this.items = items;
		this.user_phonenum=user_phonenum;
		zanService=new ZanService(ctx);
		inflater = LayoutInflater.from(ctx);
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
		return items.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return items.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.view_listitem, null);
			holder = new Holder();
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.iv_like=(ImageView)convertView.findViewById(R.id.iv_like);
			holder.iv_comment=(ImageView)convertView.findViewById(R.id.iv_comment);
			holder.iv_share=(ImageView)convertView.findViewById(R.id.iv_share);
			holder.tv_topic = (TextView) convertView.findViewById(R.id.tv_topic);
			holder.tv_location = (TextView) convertView.findViewById(R.id.tv_location);
			holder.tv_msg=(TextView)convertView.findViewById(R.id.tv_msg);
			holder.comment_num = (TextView) convertView.findViewById(R.id.comment_num);
			holder.like_num = (TextView) convertView.findViewById(R.id.like_num);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		//分享按钮监听器
		holder.iv_share.setOnClickListener(new ShareListener(position));
		//话题
		if("".equals(items.get(position).getTopic())){
			holder.tv_topic.setText("");
		}else{
			holder.tv_topic.setTextColor(Color.parseColor("#FF7F00"));
			holder.tv_topic.setText("#"+items.get(position).getTopic()+"#");
		}
		//地址
		if("".equals(items.get(position).getLocation())){
			holder.tv_location.setText("");
		}else{
			holder.tv_location.setText(items.get(position).getLocation());
		}
		//内容
		holder.tv_msg.setText(items.get(position).getMsg());
		//评论按钮监听器
		holder.iv_comment.setOnClickListener(new CommentListener(items.get(position).getId(), user_phonenum,position));
		//评论数
		if (Integer.parseInt(items.get(position).getComment_num()) != 0) {
			holder.comment_num.setText(items.get(position).getComment_num());
		} else {
			holder.comment_num.setText("");
		}
	    //点赞监听器
		holder.iv_like.setOnClickListener(new ZanListener(holder.like_num,Integer.toString(items.get(position).getId()), user_phonenum,position));
		//点赞数
		if (Integer.parseInt(items.get(position).getLike_num()) != 0) {
			holder.like_num.setTextColor(Color.RED);
			holder.like_num.setText(items.get(position).getLike_num());
		} else {
			holder.like_num.setTextColor(Color.RED);
			holder.like_num.setText("");
		}
		//图片
		//图片点击监听器
		holder.image.setOnClickListener(new ImageListener(user_phonenum,position));
		ImageLoader imageloader = ImageLoader.getInstance();
		if(!"".equals(items.get(position).getImage_uri())){//图片链接不为空
			holder.image.setVisibility(View.VISIBLE);
		    imageloader.displayImage(HttpRequest.HOST+items.get(position).getImage_uri(),holder.image ,options, new AnimateFirstDisplayListener());
		}else{//为空
//			Toast.makeText(ctx, position+"->"+items.get(position).getImage_uri(), 1).show();
//			Log.i("radapter", position+"->"+items.get(position).getImage_uri());
			holder.image.setVisibility(View.GONE);
		}
		return convertView;
	}

	class Holder {
		ImageView image,iv_like,iv_comment,iv_share;
		TextView tv_topic, tv_location, tv_msg,comment_num, like_num;
	}
	
	class ShareListener implements View.OnClickListener{
       int p;
		public ShareListener(int position){
    	   this.p=position;
       }
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			ShareSDK.initSDK(ctx);
			 OnekeyShare oks = new OnekeyShare();
			 //关闭sso授权
			 oks.disableSSOWhenAuthorize(); 
			 
			// 分享时Notification的图标和文字
			 oks.setNotification(R.drawable.ic_launcher, ctx.getString(R.string.app_name));
			 // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
			 oks.setTitle(ctx.getString(R.string.share));
			 // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
			 oks.setTitleUrl("http://sharesdk.cn");
			 // text是分享文本，所有平台都需要这个字段
			 oks.setText(items.get(p).getMsg());
			 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
			 if(!"".equals(items.get(p).getImage_uri())){
				 oks.setImageUrl(HttpRequest.HOST+items.get(p).getImage_uri());
				 }
			 // url仅在微信（包括好友和朋友圈）中使用
//			 oks.setUrl("http://sharesdk.cn");
			 // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//			 oks.setComment("我是测试评论文本");
			 // site是分享此内容的网站名称，仅在QQ空间使用
			 oks.setSite(ctx.getString(R.string.app_name));
			 // siteUrl是分享此内容的网站地址，仅在QQ空间使用
			 oks.setSiteUrl("http://sharesdk.cn");
			 
			// 启动分享GUI
			 oks.show(ctx);
		}
		
	}
	
	class CommentListener implements View.OnClickListener{
        private String phonenum;
        private int id;
        private int position;
        public CommentListener(int id,String phonenum,int position) {
			// TODO Auto-generated constructor stub
        	this.id=id;
        	this.phonenum=phonenum;
        	this.position=position;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(!"".equals(phonenum)){
				Intent intent = new Intent(ctx,ItemActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("ItemInfo", items.get(position));
				
				intent.putExtras(bundle);
				((BaseActivity)ctx).startActivityForResult(intent,0);
			}else{
				Toast.makeText(ctx, "对不起，游客身份不开放评论功能", Toast.LENGTH_SHORT).show();
				
			}
		}
		
	}
	
	class ImageListener implements View.OnClickListener{
//        private String id;
        private String user_phonenum;
        private int position;
		
        public ImageListener(String user_phonenum,int position) {
//			this.id = id;
			this.user_phonenum = user_phonenum;
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//传达参数,一个已经实现了Serializable接口的bean类
			Intent intent = new Intent(ctx,ItemActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("ItemInfo", items.get(position));
			intent.putExtras(bundle);
			intent.putExtra("user_phonenum", user_phonenum);
			ctx.startActivity(intent);
		}
		
	}
	class ZanListener implements View.OnClickListener{
        TextView like_num;
		String id;
        String user_phone;
        int position;
		private ZanListener( TextView like_num, String id,String user_phonenum,int position){
			this.like_num=like_num;
			this.id=id;
			this.user_phone=user_phonenum;
			this.position=position;
		}
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
		  //1-判断手机号是否为“”
		  //2-手机号不为空，查找本地数据库是否存在该记录，有就查找是否该手机号在此id点过赞,返回1或者0
		  //3-如果是1，说明已点过，提示你已经赞过，如果是0，更新数据库置record=1
			if(!"".equals(user_phone)){
				int count = zanService.getCount(id, user_phone);
			    if(count==0){//不存在该记录了
			    	zanService.addRecord(id,user_phone,1);//插入点过赞的记录
			    	String tx_num=like_num.getText().toString();
			    	int num="".equals(tx_num)?0:Integer.parseInt(tx_num);
			    	System.out.println(num);
			    	like_num.setText(Integer.toString(num+1));
			    	new UpdateDianZan().execute(Integer.toString(items.get(position).getId()),Integer.toString(num+1));
			    }else{
			    	int record=zanService.findRecord(user_phone, id); //查找是否点过赞
			    	if(record==1){
			    		Toast.makeText(ctx, "你已点过赞了", Toast.LENGTH_SHORT).show();
			    	}
			    }
			}else{
		    	Toast.makeText(ctx, "游客身份不能点赞，请先登录", Toast.LENGTH_SHORT).show();
		    }
		}	
	}
	
	class UpdateDianZan extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String result="";
			try {
				result= HttpRequest.updateDianzan(Integer.parseInt(params[0]), Integer.parseInt(params[1]));
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
			if(result.equals("1")){
				Toast.makeText(ctx, "点赞成功", 1).show();
			}else{
				Toast.makeText(ctx, "点赞失败", 1).show();
			}
		}
	}
}
