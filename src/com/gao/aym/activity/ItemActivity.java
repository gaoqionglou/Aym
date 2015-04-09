package com.gao.aym.activity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.SimpleFormatter;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.gao.aym.R;
import com.gao.aym.adapter.AnimateFirstDisplayListener;
import com.gao.aym.adapter.CommentAdapter;
import com.gao.aym.bean.CommentInfo;
import com.gao.aym.bean.ItemInfo;
import com.gao.aym.sql.ZanService;
import com.gao.aym.util.HttpRequest;
import com.gao.aym.util.MyApplication;
import com.gao.aym.view.CustomDialog;
import com.gao.aym.view.LoadingDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ItemActivity extends BaseActivity implements OnClickListener {
	private ImageView image, iv_share, iv_comment, iv_zan;
	private TextView tv_like_num, tv_comment_num,tv_msg,empty_view;
	private ListView comment_list;
	private EditText ed_com;
	private Button btn_send;
	private ImageButton btn_back;
	private ViewStub vs_loading;
	private ItemInfo item;
	private DisplayImageOptions options;
    private ImageLoader imageloader;
    private String user_phonenum;
    private LinkedList<CommentInfo> comments;
    private CommentAdapter adapter;
    private GetComment getCommentTask;
    private String com_content;
    private LoadingDialog loadingDialog;
    private ZanService zanService;
    private CustomDialog dialog;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		overridePendingTransition(R.anim.right_in, R.anim.left_out);
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_item);
		// �������뷨
		InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		item = (ItemInfo) getIntent().getSerializableExtra("ItemInfo");
	    user_phonenum=getIntent().getStringExtra("user_phonenum");
	    comments=new LinkedList<CommentInfo>();
	    loadingDialog = new LoadingDialog(this, R.style.loading_dialog);
	    imageloader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.bg_pic_loading) // ����ͼƬ�������ڼ���ʾ��ͼƬ
				.showImageForEmptyUri(R.drawable.bg_pic_loading)// ����ͼƬUriΪ�ջ��Ǵ����ʱ����ʾ��ͼƬ
				.showImageOnFail(R.drawable.bg_pic_loading) // ����ͼƬ����/��������д���ʱ����ʾ��ͼƬ
				.bitmapConfig(Bitmap.Config.RGB_565)// ����ͼƬ�Ľ�������//
				.cacheInMemory(true)// �������ص�ͼƬ�Ƿ񻺴����ڴ���
				.cacheOnDisc(true)// �������ص�ͼƬ�Ƿ񻺴���SD����
				.displayer(new FadeInBitmapDisplayer(100))// �Ƿ�ͼƬ���غú���Ķ���ʱ��
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();// �������
		initView();
		getCommentTask = new GetComment(item.getId());
		getCommentTask.execute();
	}

	// ��ʼ��
	private void initView() {
		
		//ͼƬ
		image = (ImageView) findViewById(R.id.item_image);
		if("".equals(item.getImage_uri())||item.getImage_uri()==null) {
			image.setVisibility(View.GONE);
		}else{
		imageloader.displayImage(HttpRequest.HOST+item.getImage_uri(),image ,options, new AnimateFirstDisplayListener()); }
		//����
		iv_share = (ImageView) findViewById(R.id.item_iv_share);
		iv_share.setOnClickListener(new ShareListener());
		//����
		tv_msg=(TextView)findViewById(R.id.item_tv_msg);
		tv_msg.setText(item.getMsg());
		//����
		iv_comment = (ImageView) findViewById(R.id.item_iv_comment);// �������ü���
		//������
		tv_like_num = (TextView) findViewById(R.id.item_like_num);
		//����
		iv_zan = (ImageView) findViewById(R.id.item_iv_like);
		iv_zan.setOnClickListener(new ZanListener(tv_like_num, Integer.toString(item.getId()), user_phonenum, item));
		//������
		tv_comment_num = (TextView) findViewById(R.id.item_comment_num);
		if (Integer.parseInt(item.getComment_num()) != 0) {
			tv_comment_num.setText(item.getComment_num());
		} else {
			tv_comment_num.setText("");
		}
		
		if (Integer.parseInt(item.getLike_num()) != 0) {
			tv_like_num.setTextColor(Color.RED);
			tv_like_num.setText(item.getLike_num());
		} else {
			tv_like_num.setText("");
		}
		empty_view=(TextView)findViewById(R.id.empty_view);
		
		comment_list=(ListView)findViewById(R.id.comment_list);
		comment_list.setEmptyView(empty_view);
		comment_list.setOnItemClickListener(new CommentItemListener());
		ed_com = (EditText) findViewById(R.id.ed_comment);
		btn_send = (Button) findViewById(R.id.btn_send);
		btn_back=(ImageButton)findViewById(R.id.btn_back);
		btn_send.setOnClickListener(this);
		btn_back.setOnClickListener(this);
	}
	
	class ShareListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			ShareSDK.initSDK(ItemActivity.this);
			 OnekeyShare oks = new OnekeyShare();
			 //�ر�sso��Ȩ
			 oks.disableSSOWhenAuthorize(); 
			 
			// ����ʱNotification��ͼ�������
			 oks.setNotification(R.drawable.ic_launcher, ItemActivity.this.getString(R.string.app_name));
			 // title���⣬ӡ��ʼǡ����䡢��Ϣ��΢�š���������QQ�ռ�ʹ��
			 oks.setTitle(ItemActivity.this.getString(R.string.share));
			 // titleUrl�Ǳ�����������ӣ�������������QQ�ռ�ʹ��
			 oks.setTitleUrl("http://sharesdk.cn");
			 // text�Ƿ����ı�������ƽ̨����Ҫ����ֶ�
			 oks.setText(item.getMsg());
			 // imagePath��ͼƬ�ı���·����Linked-In�����ƽ̨��֧�ִ˲���
			 if(!"".equals(item.getImage_uri())){
				 oks.setImageUrl(HttpRequest.HOST+item.getImage_uri());
				 }
			 // url����΢�ţ��������Ѻ�����Ȧ����ʹ��
//			 oks.setUrl("http://sharesdk.cn");
			 // comment���Ҷ�������������ۣ�������������QQ�ռ�ʹ��
//			 oks.setComment("���ǲ��������ı�");
			 // site�Ƿ�������ݵ���վ���ƣ�����QQ�ռ�ʹ��
			 oks.setSite(ItemActivity.this.getString(R.string.app_name));
			 // siteUrl�Ƿ�������ݵ���վ��ַ������QQ�ռ�ʹ��
			 oks.setSiteUrl("http://sharesdk.cn");
			 
			// ��������GUI
			 oks.show(ItemActivity.this);
		}
		
	}
	
	class CommentItemListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,long arg3) {
			// TODO Auto-generated method stub
			position=position+1;
			ed_com.append("�ظ�"+position+"¥:");
		}
		
	}
	
	/**
	 *  ���޼���
	 */
	class ZanListener implements View.OnClickListener{
        TextView like_num;
		String id;
        String user_phone;
        ItemInfo item;
		private ZanListener( TextView like_num, String id,String user_phonenum,ItemInfo item){
			this.like_num=like_num;
			this.id=id;
			this.user_phone=user_phonenum;
			this.item=item;
			zanService = new ZanService(ItemActivity.this);
		}
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
		  //1-�ж��ֻ����Ƿ�Ϊ����
		  //2-�ֻ��Ų�Ϊ�գ����ұ������ݿ��Ƿ���ڸü�¼���оͲ����Ƿ���ֻ����ڴ�id�����,����1����0
		  //3-�����1��˵���ѵ������ʾ���Ѿ��޹��������0���������ݿ���record=1
			if(!"".equals(user_phone)){
				int count = zanService.getCount(id, user_phone);
			    if(count==0){//�����ڸü�¼��
			    	zanService.addRecord(id,user_phone,1);//�������޵ļ�¼
			    	String tx_num=like_num.getText().toString();
			    	int num="".equals(tx_num)?0:Integer.parseInt(tx_num);
			    	System.out.println(num);
			    	like_num.setText(Integer.toString(num+1));
			    	new UpdateDianZan().execute(Integer.toString(item.getId()),Integer.toString(num+1));
			    }else{
			    	int record=zanService.findRecord(user_phone, id); //�����Ƿ�����
			    	if(record==1){
			    		Toast.makeText(ItemActivity.this, "���ѵ������", Toast.LENGTH_SHORT).show();
			    	}
			    }
			}else{
		    	Toast.makeText(ItemActivity.this, "�ο���ݲ��ܵ��ޣ����ȵ�¼", Toast.LENGTH_SHORT).show();
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
				Toast.makeText(ItemActivity.this, "���޳ɹ�", 1).show();
			}else{
				Toast.makeText(ItemActivity.this, "����ʧ��", 1).show();
			}
		}
	}
	
	/**
	 *   ��ȡ�����б�
	 */
	class GetComment extends AsyncTask<String,String,String>{
        private int id; 
		public GetComment(int id){
        	 this.id=id;
         }
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String result="";
			try {
				result= HttpRequest.getCommentList(id);
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
					int no = jsonObject.getInt("no");
					int id=jsonObject.getInt("id");
					String phonenum=jsonObject.getString("phonenum");
					String msg=jsonObject.getString("msg");
					String date = jsonObject.getString("date");
					CommentInfo coment = new CommentInfo(no, id, phonenum, msg, date);
					comments.add(coment);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(comments.size()==0) empty_view.setText("��û�������ۣ��Ͻ���ɳ����~");
			adapter = new CommentAdapter(ItemActivity.this, comments);
			comment_list.setAdapter(adapter);
			setListViewHeightBasedOnChildren(comment_list);
		}
	}
	
	/**
	 *  ��������
	 */
	class SendCommentTask extends AsyncTask<String,String,String>{
        String date;
        String com_content;
		public SendCommentTask(String com_content) {
			// TODO Auto-generated constructor stub
			this.com_content=com_content;
		}
      
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			date = sdf.format(new Date());
			loadingDialog.setLoadingTip("�����ύ...");
			loadingDialog.show(false);
		}
		
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String result="";
			try {
				result = HttpRequest.sendComment(item.getId(), item.getPhonenum(),com_content,date);
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
			loadingDialog.dismiss();
			if("1".equals(result)){
//			    Toast.makeText(ItemActivity.this, "���۳ɹ�", 1).show();	
			    CommentInfo comment = new CommentInfo(item.getId(), item.getPhonenum(), com_content, date) ;
			    System.out.println(comments.toString());
			    comments.addLast(comment);
			    adapter.notifyDataSetChanged();
			    setListViewHeightBasedOnChildren(comment_list);
			    loadingDialog.setLoadingImage(R.drawable.success);
			    loadingDialog.setLoadingTip("���۳ɹ�");
			    loadingDialog.show(true);
			    ed_com.setText("");
			}
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if(loadingDialog.isShowing()) loadingDialog.dismiss();
				}
			}, 1800);
		}
	}
	
	/**
	 * ��̬����ListView�ĸ߶�,listview��item������Ϊ���Բ���
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) { 
	    if(listView == null) return;

	    CommentAdapter listAdapter = (CommentAdapter) listView.getAdapter(); 
	    if (listAdapter == null) { 
	        // pre-condition 
	        return; 
	    } 

	    int totalHeight = 0; 
	    for (int i = 0; i < listAdapter.getCount(); i++) { 
	        View listItem = listAdapter.getView(i, null, listView); 
	        listItem.measure(0, 0); 
	        totalHeight += listItem.getMeasuredHeight(); 
	    } 

	    ViewGroup.LayoutParams params = listView.getLayoutParams(); 
	    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)); 
	    listView.setLayoutParams(params); 
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.btn_back:
			onBackPressed();
			break;
		case R.id.btn_send:
			com_content=ed_com.getText().toString();
			if(!"".equals(com_content)){ //��Ϊ��
				new SendCommentTask(com_content).execute();
			}else{
				Toast.makeText(this, "�������ݲ���Ϊ��", Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}
	
	
}
