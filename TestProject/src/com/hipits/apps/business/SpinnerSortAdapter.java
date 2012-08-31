package com.hipits.apps.business;


import java.util.ArrayList;

import com.hipits.apps.business.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SpinnerSortAdapter extends BaseAdapter {
	Context	context;
	int	spinnersorttext;
	ArrayList namelist, numberlist;
	LayoutInflater	layoutInflater;
	public SpinnerSortAdapter(Context context, int textviewId, ArrayList list1, ArrayList list2){
		this.context = context;
		this.spinnersorttext = textviewId;
		this.namelist = list1;
		this.numberlist=list2;
		layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	public int getCount() {
		// TODO Auto-generated method stub
		return namelist.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return namelist.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		try {
			if(convertView==null) convertView=layoutInflater.inflate(spinnersorttext, parent, false);
			TextView nametetext=(TextView) convertView.findViewById(R.id.spinnersort_text1);
			TextView numbertext = (TextView) convertView.findViewById(R.id.spinnersort_text2);
			nametetext.setText(namelist.get(position).toString());
			numbertext.setText(numberlist.get(position).toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}

}
