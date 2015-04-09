package com.gao.aym.adapter;

import java.util.LinkedList;
import java.util.List;

import com.gao.aym.R;
import com.gao.aym.bean.LetterContact;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ContactAdapter extends BaseAdapter {
    private List<LetterContact> list;
    private Context context; 
    private LayoutInflater inflater;
    
	public ContactAdapter(List<LetterContact> list, Context context) {
		this.list = list;
		this.context = context;
		this.inflater = LayoutInflater.from(context); 
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder =null;
		if(convertView==null){
			holder = new ViewHolder();
			convertView=inflater.inflate(R.layout.contact_item, null);
			holder.tv_letter=(TextView)convertView.findViewById(R.id.cataletter);
			holder.tv_name=(TextView)convertView.findViewById(R.id.name);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		int isFirstShow =list.get(position).getIsFirstShow();
		if(isFirstShow==1){
			holder.tv_letter.setVisibility(View.VISIBLE);
			holder.tv_letter.setText(list.get(position).getSortletter());
		}else{
			holder.tv_letter.setVisibility(View.GONE);
		}
		holder.tv_name.setText(list.get(position).getName());
		return convertView;
	}
  class ViewHolder{
	  TextView tv_letter;
	  TextView tv_name;
  }
 
  
  /** 需要在外部排序update之后调用
 * @param letter
 * @return
 */
public int getPositionOfLetterFirstShow(String letter){
	  int size=list.size();
	  for(int i=0;i<size;i++){
		  LetterContact contact = list.get(i);
		  if(contact.getSortletter().equals(letter) && contact.getIsFirstShow()==1){
			  return i;
		  }
	  }
	  return -1;
  }
}
