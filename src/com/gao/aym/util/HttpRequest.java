package com.gao.aym.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

/**
 * 用于进行网络请求的工具类
 * 
 * @author gao
 */
public class HttpRequest {
	
	public static final String HOST = "http://172.25.161.5:8080/AymServer/";
//	public static final String HOST = "http://192.168.155.5:8080/AymServer/";
	public static final String URI_LOGIN = HOST + "login.action";
	public static final String URI_REGISTER = HOST + "register.action";
	public static final String URI_MODIFYCODE = HOST + "modify.action";
	public static final String URI_SELECT=HOST+"select.action";
    public static final String URI_GETMAX=HOST+"getMaxsize.action";
    public static final String URI_DIANZAN=HOST+"dianzan.action";
    private static final String URI_GETCOMMENT=HOST+"getComment.action";
    private static final String URI_SENDCOMMENT=HOST+"sendComment.action";
    private static final String URI_GETACTIVITY=HOST+"getActivity.action";
    private static final String URI_UPLOAD=HOST+"uploadAction";
    private static final String URI_PUBLISH=HOST+"publish.action";
    private static final String URI_TOPIC=HOST+"getTopic.action";
    private static final String URI_ITEM=HOST+"getItem.action"; //废用
    private static final String URI_UPDATE=HOST+"update.action";
    
	public HttpRequest() {
		// TODO Auto-generated constructor stub
	}

	/** 登录请求
	 * @param data
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String sendLoginRequest(Map<String, String> data) throws ClientProtocolException, IOException {
		String result="";
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(URI_LOGIN);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("phonenum", data.get("phonenum")));
		params.add(new BasicNameValuePair("password", data.get("password")));
		post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		
		httpClient.getParams().setParameter(HttpConnectionParams.CONNECTION_TIMEOUT,
						new Integer(50 * 1000)); // 设置连接超时
		httpClient.getParams().setParameter(HttpConnectionParams.SO_TIMEOUT,
				new Integer(50 * 1000)); // 设置Socket连接超时
		HttpResponse response = null;
		response = httpClient.execute(post);
		if (response != null) {
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				result=stream2String(entity.getContent(), "UTF-8");
				httpClient.getConnectionManager().shutdown();
				System.out.println(result);
			}
		}
		return result;
	}

	
	/**注册请求
	 * @param data
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String sendRegisterRequest(Map<String, String> data) throws ClientProtocolException, IOException {
		String result="";
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(URI_REGISTER);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("phonenum", data.get("phonenum")));
		params.add(new BasicNameValuePair("password", data.get("password")));
		post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		
		httpClient.getParams().setParameter(HttpConnectionParams.CONNECTION_TIMEOUT,
						new Integer(50 * 1000)); // 设置连接超时
		httpClient.getParams().setParameter(HttpConnectionParams.SO_TIMEOUT,
				new Integer(50 * 1000)); // 设置Socket连接超时
		HttpResponse response = null;
		response = httpClient.execute(post);
		if (response != null) {
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				result=stream2String(entity.getContent(), "UTF-8");
				httpClient.getConnectionManager().shutdown();
				System.out.println(result);
			}
		}
		return result;
	}
	/**修改密码请求
	 * @param data
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String sendModifyCodeRequest(Map<String, String> data) throws ClientProtocolException, IOException {
		String result="";
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(URI_MODIFYCODE);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("phonenum", data.get("phonenum")));
		params.add(new BasicNameValuePair("password", data.get("password")));
		post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		
		httpClient.getParams().setParameter(HttpConnectionParams.CONNECTION_TIMEOUT,
						new Integer(50 * 1000)); // 设置连接超时
		httpClient.getParams().setParameter(HttpConnectionParams.SO_TIMEOUT,
				new Integer(50 * 1000)); // 设置Socket连接超时
		HttpResponse response = null;
		response = httpClient.execute(post);
		if (response != null) {
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				result=stream2String(entity.getContent(), "UTF-8");
				httpClient.getConnectionManager().shutdown();
				System.out.println(result);
			}
		}
		return result;
	}
	/**
	 * @param data
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String sendSelectListRequest(Map<String, String> data) throws ClientProtocolException, IOException {
		String result="";
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(URI_SELECT);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("type", data.get("type")));
		params.add(new BasicNameValuePair("location", data.get("location")));
		params.add(new BasicNameValuePair("from", data.get("from")));
		params.add(new BasicNameValuePair("to", data.get("to")));
		post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		
		httpClient.getParams().setParameter(HttpConnectionParams.CONNECTION_TIMEOUT,
						new Integer(50 * 1000)); // 设置连接超时
		httpClient.getParams().setParameter(HttpConnectionParams.SO_TIMEOUT,
				new Integer(50 * 1000)); // 设置Socket连接超时
		HttpResponse response = null;
		response = httpClient.execute(post);
		if (response != null) {
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				result=stream2String(entity.getContent(), "gb2312");
				httpClient.getConnectionManager().shutdown();
				System.out.println("SelectList ->"+response.getStatusLine().getStatusCode());
				System.out.println("SelectList ->"+data.toString());
				System.out.println("SelectList ->"+result);
			}else{
				System.out.println("SelectList ->"+response.getStatusLine().getStatusCode());
			}
		}
		return result;
	}
	/**获取数据库中表的最大行数
	 * @param data
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String getMaxSize(String type) throws ClientProtocolException, IOException {
		String result="";
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(URI_GETMAX);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("type", type));
		
		post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		
		httpClient.getParams().setParameter(HttpConnectionParams.CONNECTION_TIMEOUT,
						new Integer(50 * 1000)); // 设置连接超时
		httpClient.getParams().setParameter(HttpConnectionParams.SO_TIMEOUT,
				new Integer(50 * 1000)); // 设置Socket连接超时
		HttpResponse response = null;
		response = httpClient.execute(post);
		if (response != null) {
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				result=stream2String(entity.getContent(), "UTF-8");
				httpClient.getConnectionManager().shutdown();
				System.out.println("最大行数-->"+result);
			}
		}
		return result;
	}
	
	/**更新点赞的网络请求
	 * @param id
	 * @param like_num
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String updateDianzan(int id,int like_num) throws ClientProtocolException, IOException {
		String result="";
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(URI_DIANZAN);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", Integer.toString(id)));
		params.add(new BasicNameValuePair("like_num", Integer.toString(like_num)));
		post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		
		httpClient.getParams().setParameter(HttpConnectionParams.CONNECTION_TIMEOUT,
						new Integer(50 * 1000)); // 设置连接超时
		httpClient.getParams().setParameter(HttpConnectionParams.SO_TIMEOUT,
				new Integer(50 * 1000)); // 设置Socket连接超时
		HttpResponse response = null;
		response = httpClient.execute(post);
		if (response != null) {
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				result=stream2String(entity.getContent(), "UTF-8");
				httpClient.getConnectionManager().shutdown();
				System.out.println("点赞结果:"+result);
			}
		}
		return result;
	}
	/**
	 *  获取评论列表
	 * @param id
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String getCommentList(int id) throws ClientProtocolException, IOException{
		String result="";
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(URI_GETCOMMENT);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", Integer.toString(id)));
		post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		httpClient.getParams().setParameter(HttpConnectionParams.CONNECTION_TIMEOUT,new Integer(50 * 1000)); // 设置连接超时
		httpClient.getParams().setParameter(HttpConnectionParams.SO_TIMEOUT,new Integer(50 * 1000)); // 设置Socket连接超时
		HttpResponse response = null;
		response = httpClient.execute(post);
		if (response != null) {
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				result=stream2String(entity.getContent(), "GB2312");
				httpClient.getConnectionManager().shutdown();
				System.out.println("评论列表—>"+result);
			}
		}
		return result;
	}
	
	/**
	 *   发送评论/回复评论
	 * @param id
	 * @param phonenum
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String sendComment(int id,String phonenum,String msg,String date) throws ClientProtocolException, IOException{
		String result="";
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(URI_SENDCOMMENT);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", Integer.toString(id)));
		params.add(new BasicNameValuePair("phonenum", phonenum));
		params.add(new BasicNameValuePair("msg", msg));
		params.add(new BasicNameValuePair("date", date));
		post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		httpClient.getParams().setParameter(HttpConnectionParams.CONNECTION_TIMEOUT,new Integer(50 * 1000)); // 设置连接超时
		httpClient.getParams().setParameter(HttpConnectionParams.SO_TIMEOUT,new Integer(50 * 1000)); // 设置Socket连接超时
		HttpResponse response = null;
		response = httpClient.execute(post);
		if (response != null) {
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				result=stream2String(entity.getContent(), "UTF-8");
				httpClient.getConnectionManager().shutdown();
				System.out.println("发送/回复评论结果->"+result);
			}
		}
		return result;
	}
	
	/**
	 *  获取活动列表
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public static String getActivityList(String type) throws Exception{
		String result="";
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(URI_GETACTIVITY);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("type",type ));
	
		post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		httpClient.getParams().setParameter(HttpConnectionParams.CONNECTION_TIMEOUT,new Integer(50 * 1000)); // 设置连接超时
		httpClient.getParams().setParameter(HttpConnectionParams.SO_TIMEOUT,new Integer(50 * 1000)); // 设置Socket连接超时
		HttpResponse response = null;
		response = httpClient.execute(post);
		if (response != null) {
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				result=stream2String(entity.getContent(), "GB2312");
				httpClient.getConnectionManager().shutdown();
				System.out.println("活动列表->"+result);
			}
		}
		return result;
	}
	
	/** 发表动态
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String publish(Map<String,String> data)  throws Exception{
		String result="";
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(URI_PUBLISH);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("phonenum",data.get("phonenum")));
		params.add(new BasicNameValuePair("image_uri",data.get("image_uri")));
		params.add(new BasicNameValuePair("topic",data.get("topic")));
		params.add(new BasicNameValuePair("msg",data.get("msg")));
		params.add(new BasicNameValuePair("location",data.get("location")));
		params.add(new BasicNameValuePair("comment_num",data.get("comment_num")));
		params.add(new BasicNameValuePair("like_num",data.get("like_num")));
		post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		httpClient.getParams().setParameter(HttpConnectionParams.CONNECTION_TIMEOUT,new Integer(50 * 1000)); // 设置连接超时
		httpClient.getParams().setParameter(HttpConnectionParams.SO_TIMEOUT,new Integer(50 * 1000)); // 设置Socket连接超时
		HttpResponse response = null;
		response = httpClient.execute(post);
		if (response != null) {
			if (response.getStatusLine().getStatusCode() == 200) {
				if (response.getStatusLine().getStatusCode() == 200) {
					HttpEntity entity = response.getEntity();
					result=stream2String(entity.getContent(), "UTF-8");
					httpClient.getConnectionManager().shutdown();
					System.out.println("发布结果->"+result);
				}
			}
		}
		return result;
	}
	

	public static String getTopic() throws ClientProtocolException, IOException{
		String result="";
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(URI_TOPIC);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("type","all"));
		httpClient.getParams().setParameter(HttpConnectionParams.CONNECTION_TIMEOUT,new Integer(50 * 1000)); // 设置连接超时
		httpClient.getParams().setParameter(HttpConnectionParams.SO_TIMEOUT,new Integer(50 * 1000)); // 设置Socket连接超时
		HttpResponse response = null;
		response = httpClient.execute(post);
		if (response != null) {
			if (response.getStatusLine().getStatusCode() == 200) {
				if (response.getStatusLine().getStatusCode() == 200) {
					HttpEntity entity = response.getEntity();
					result=stream2String(entity.getContent(), "GB2312");
					httpClient.getConnectionManager().shutdown();
					System.out.println("getTopic->"+result);
				}
			}
		}
		return result;
	}
	 
	/**
	 *  不再使用
	 */
	public static String getItem(Map<String,String> data) throws ClientProtocolException, IOException{
		String result="";
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(URI_ITEM);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("from", data.get("from")));
		params.add(new BasicNameValuePair("to", data.get("to")));
		post.addHeader("Retry-After", "100");
		httpClient.getParams().setParameter(HttpConnectionParams.CONNECTION_TIMEOUT,new Integer(50 * 1000)); // 设置连接超时
		httpClient.getParams().setParameter(HttpConnectionParams.SO_TIMEOUT,new Integer(50 * 1000)); // 设置Socket连接超时
		HttpResponse response = null;
		response = httpClient.execute(post);
		if (response != null) {
			if (response.getStatusLine().getStatusCode() == 200) {
				if (response.getStatusLine().getStatusCode() == 200) {
					HttpEntity entity = response.getEntity();
					result=stream2String(entity.getContent(), "GB2312");
					httpClient.getConnectionManager().shutdown();
					System.out.println("getItem->"+result);
					System.out.println("getItem->"+response.getStatusLine().getStatusCode());
				}
			}else{
				System.out.println("getItem->"+response.getStatusLine().getStatusCode());
			}
		}
		return result;
	}
	
	public static String getUpdateUrl(String versionName) throws ClientProtocolException, IOException{
		String result="";
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(URI_UPDATE);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("versionName", versionName));
		
		post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		
		httpClient.getParams().setParameter(HttpConnectionParams.CONNECTION_TIMEOUT,
						new Integer(50 * 1000)); // 设置连接超时
		httpClient.getParams().setParameter(HttpConnectionParams.SO_TIMEOUT,
				new Integer(50 * 1000)); // 设置Socket连接超时
		HttpResponse response = null;
		response = httpClient.execute(post);
		if (response != null) {
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				result=stream2String(entity.getContent(), "UTF-8");
				httpClient.getConnectionManager().shutdown();
				System.out.println("updateUrl"+result);
			}
		}
		return result;
	}
	
	public static void download(File downloadFile,String downloadUrl,DownloadProgressListener listener) {
		try {
			int downloaded=0;
			BufferedInputStream in = null;
			FileOutputStream fos = null;
			BufferedOutputStream bout=null;
			URL url =new URL(downloadUrl);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");             
			int code=connection.getResponseCode();
			if(connection.getResponseCode()==200){
				long length=(long)connection.getContentLength();
				in = new BufferedInputStream(connection.getInputStream());
				fos= new FileOutputStream(downloadFile);
				bout = new BufferedOutputStream(fos, 1024);
				byte[] data = new byte[1024];
				int x = 0;
				while ((x = in.read(data, 0, 1024)) >= 0) {
				    bout.write(data, 0, x);
				    downloaded += x;
				    if(listener!=null) listener.download(downloaded,length);
				}
				bout.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/** 上传图片
	 * @param filebody
	 * @param filename
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static void uploadImage(byte[] filebody, String filename) throws ClientProtocolException, IOException{
		Log.i("photo", " in thread");
		Log.i("photo",filename);
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post =new HttpPost(URI_UPLOAD);
		MultipartEntity entity =new MultipartEntity();
		entity.addPart("myfile", new ByteArrayBody(filebody, filename));
		post.setEntity(entity);
		httpClient.execute(post);
	}
	
	
	
	/**
	 * 转换
	 * @param inputStream
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	private static String stream2String(InputStream inputStream, String charset) throws IOException{
		// TODO Auto-generated method stub
		if(inputStream == null){
			return "";
		}
		byte[] buffer  = new byte[1024];
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		int len = 0;
		while((len = inputStream.read(buffer)) != -1){
			bout.write(buffer, 0, len);
		}
		String result = new String(bout.toByteArray(),charset);
		inputStream.close();
		return result;
	}
	
	public interface DownloadProgressListener{
		public void download(long downloaded,long max);
	}
}
