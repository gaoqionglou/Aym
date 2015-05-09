package com.gao.aym.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts.Photo;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.gao.aym.R;
import com.gao.aym.adapter.ContactAdapter;
import com.gao.aym.bean.LetterContact;
import com.gao.aym.util.Hanyu;
import com.gao.aym.util.MyApplication;
import com.gao.aym.util.PinyinComparator;
import com.gao.aym.view.LetterView;
import com.gao.aym.view.LetterView.OnTouchingLetterChangedListener;

public class ContactActivity extends BaseActivity {

//	��ȡ��Phon���ֶ� 
	private static final String[] PHONES_PROJECTION = new String[] {
			Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID, Phone.CONTACT_ID };
//	 ��ϵ����ʾ���� 
	private static final int PHONES_DISPLAY_NAME_INDEX = 0;

//	 �绰���� 
//	private static final int PHONES_NUMBER_INDEX = 1;

//	ͷ��ID 
//	private static final int PHONES_PHOTO_ID_INDEX = 2;

//	 ��ϵ�˵�ID 
//	private static final int PHONES_CONTACT_ID_INDEX = 3;
	private ListView listview;
    private ContactAdapter adapter;
    private TextView tip;
    private LetterView letterView;
    private ImageButton btn_back;
    private List<LetterContact> contacts;
    private PinyinComparator pinyinComparator;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		overridePendingTransition(R.anim.right_in, R.anim.left_out);
		MyApplication.getInstance().addActivity(this);
		setContentView(R.layout.view_contact);
		contacts=new ArrayList<LetterContact>();
		getPhoneContact();
		initView();
	}


	private void initView() {
		// TODO Auto-generated method stub
		pinyinComparator=new PinyinComparator();
		tip=(TextView)findViewById(R.id.dialog);
		btn_back=(ImageButton)findViewById(R.id.btn_back);
		letterView=(LetterView)findViewById(R.id.letterview);
		listview=(ListView)findViewById(R.id.listview);
		letterView.setTextView(tip);
		letterView.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
			
			@Override
			public void onTouchingLetterChanged(String s) {
				//����ĸ�״γ��ֵ�λ��
				int position = adapter.getPositionOfLetterFirstShow(s);
				if(position != -1){
					listview.setSelection(position);
				}
			}
		});
		btn_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				Intent publish = new Intent(ContactActivity.this,PublishActivity.class);
				LetterContact contact= (LetterContact)adapter.getItem(position);
				publish.putExtra("person", contact.getName());
//				startActivityForResult(publish,99);
				setResult(99, publish);
				finish();
			}
		});
		adapter = new ContactAdapter(contacts, this);
		listview.setAdapter(adapter);
	}
	
	/**
	 * 
	 */
	private void getPhoneContact() {
		// TODO Auto-generated method stub
		Hanyu hanyu = new Hanyu();
		ContentResolver resolver = getContentResolver();
//		 ��ȡ�ֻ���ϵ�� �ڶ����� �����ֶε��ַ�������
//		content://icc/adn sim������ϵ��
		Uri uri = Uri.parse("content://com.android.contacts/data/phones"); 
		Cursor phoneCursor = resolver.query(uri,PHONES_PROJECTION, null, null, null);
		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {
				// �õ��ֻ�����
				// String phoneNumber =
				// phoneCursor.getString(PHONES_NUMBER_INDEX);
				// ���ֻ�����Ϊ�յĻ���Ϊ���ֶ� ������ǰѭ��
				// if (TextUtils.isEmpty(phoneNumber))
				// continue;

				// �õ���ϵ������
				String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);

				// �õ���ϵ��ID
				// Long contactid =
				// phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);

				// �õ���ϵ��ͷ��ID
				// Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);
				String firstLetter = hanyu.getFirstLetterOfStringPinYin(contactName);
				contacts.add(new LetterContact(firstLetter, contactName));

			}
			//����A-Z����
			Collections.sort(contacts, pinyinComparator);
			update(contacts);
			phoneCursor.close();
		}
	}
	
	
	 /** ���� ����A-Z����õ�����
	 * @param list
	 */
	private void update(List<LetterContact> list){
		int size = list.size();  
		for(int i=0;i<size;i++){
			  
			  if(i==0) { //��һ��Ϊ��һ�γ���
				  list.get(i).setIsFirstShow(1);
				  if(i+1<size){
					  LetterContact current = list.get(i);
					  LetterContact next = list.get(i+1);
					  if(!current.getSortletter().equals(next.getSortletter())){
						  next.setIsFirstShow(1);
					  }  
				  }
			  }else{ //�ǵ�һ��
				  if(i+1<size){//i+1�ڷ�Χ��
					  LetterContact current = list.get(i);
					  LetterContact next = list.get(i+1);
					  if(!current.getSortletter().equals(next.getSortletter())){
						  next.setIsFirstShow(1);
					  }
				  }
			  }
			  
		  }
	  }
	
}
