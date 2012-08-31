package com.hipits.apps.business;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class TestAdapter extends BaseAdapter {
	ArrayList<String> num;
	Context context;
	LayoutInflater	layoutInflater;
	int	test;
	ArrayList<Integer> alb= new ArrayList<Integer>();
	private List<TestItem> itemList;
	
	public TestAdapter(ArrayList<TestItem> itemList,int test, Context context) {
		this.itemList = itemList;
		this.context = context;
		this.test = test;
		layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	public int getCount() {
		return itemList.size();
	}

	public Object getItem(int arg0) {
		return itemList.get(arg0);
	}

	public long getItemId(int arg0) {
		return arg0;
	}

	public View getView(final int postion, View arg1, ViewGroup arg2) {
		TestItem item = itemList.get(postion);	
		try {
			if(arg1==null) arg1=layoutInflater.inflate(test, arg2, false);
			TextView text_date = (TextView) arg1.findViewById(R.id.textView1);
			final CheckBox chek = (CheckBox) arg1.findViewById(R.id.checkBox1);
			chek.setChecked(itemList.get(postion).isCheck());
			chek.setOnClickListener(new OnClickListener() {				
				public void onClick(View arg0) {
					DBAdapter dba= new DBAdapter(context);
					itemList.get(postion).setCheck(!itemList.get(postion).isCheck());
					if(itemList.get(postion).isCheck()){
						String number;
						 dba.open();
						 if(itemList.get(postion).getNumber().length()>8){
								number = itemList.get(postion).getNumber().substring(3);
							}
							else{
								number = itemList.get(postion).getNumber();
							}
						dba.insertEntry2(postion, itemList.get(postion).getTitle(),number);
					    dba.close();
					}
					else{
						 dba.open();
						dba.deleteEntry2(postion);
					    dba.close();
					}
				}
			});
			text_date.setText(itemList.get(postion).getTitle());
			} catch (Exception e) {
			e.printStackTrace();
		}
		return arg1;
	}

}
