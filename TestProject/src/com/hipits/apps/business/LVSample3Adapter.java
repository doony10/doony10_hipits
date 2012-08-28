package com.hipits.apps.business;


import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import com.hipits.apps.business.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LVSample3Adapter extends BaseAdapter {

	private List<LVSample3Item> itemList;
	private Context context;
	public Hashtable<Integer, View> hashConvertView = new Hashtable<Integer, View>();
	public LVSample3Adapter(List<LVSample3Item> itemList, Context context) {
		this.itemList = itemList;
		this.context = context;
	}

	public int getCount() {
		return itemList.size();
	}

	public LVSample3Item getItem(int position) {
		return itemList.get(position);
	}

	public long getItemId(int position) {
		return itemList.get(position).getId();
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		final LVSample3Item item = itemList.get(position);

		ViewHolder holder;
		if (hashConvertView.containsKey(position) == false) {
			convertView = (com.hipits.apps.business.CheckableRelativeLayout) LayoutInflater.from(context).inflate(
					R.layout.my_list_item3, parent, false);
			holder = new ViewHolder();
			holder.tvTitle = (TextView) convertView.findViewById(R.id.title);
			holder.tvSummary = (TextView) convertView.findViewById(R.id.summary);
			holder.chk = (CheckBox) convertView.findViewById(R.id.check);
			holder.linearLayout = (CheckableRelativeLayout) convertView.findViewById(R.id.checkliner);
			
			if(item.isCheck())
			{
				holder.chk.setChecked(true);
				
			}
			else
				holder.chk.setChecked(false);
		
			holder.chk.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					itemList.get(position).setCheck(true);
				}
			});
			holder.linearLayout.setOnClickListener(listener2);

			convertView.setTag(holder);
			hashConvertView.put(position, convertView);
		} else {
			convertView = (View) hashConvertView.get(position);
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvTitle.setText(item.getTitle());
		holder.tvSummary.setText(item.getNumber());
		return convertView;
	}
	private final OnClickListener listener2 = new OnClickListener() {
		public void onClick(View v) {
			ViewHolder vh = new ViewHolder();
			vh.chk = (CheckBox) v.findViewById(R.id.check);
			if (v instanceof CheckBox){
				vh.chk = (CheckBox) v;
				notifyDataSetChanged();
			}
		}
	};
	public Long[] getCheckItemIds() {
		if (hashConvertView == null || hashConvertView.size() == 0)
			return null;

        Integer key;
        View view;
		List<Long> lstIds = new ArrayList<Long>();
		Enumeration<Integer> e = hashConvertView.keys();
        long index = 0;
        while(e.hasMoreElements()) {
            key = (Integer)e.nextElement();
            view = (View)hashConvertView.get(key);
            ViewHolder holder = (ViewHolder) view.getTag();
            if (holder.chk.isChecked()){
            	lstIds.add(index);
            	Log.v("doony10", "doont");
            }
            index++;
        }
        
        Long[] arrLong = (Long[]) lstIds.toArray(new Long[0]);
        return arrLong;
	}

	public void clearChoices()
	{
		if (hashConvertView == null || hashConvertView.size() == 0)
			return;
		
        Integer key;
        View view;
		List<Long> lstIds = new ArrayList<Long>();
		Enumeration<Integer> e = hashConvertView.keys();
        long index = 0;
        while(e.hasMoreElements()) {
            key = (Integer)e.nextElement();
            view = (View)hashConvertView.get(key);
            ViewHolder holder = (ViewHolder) view.getTag();
            if (holder.chk.isChecked()){
            	holder.chk.setChecked(false);
            }
            index++;
        }		
	}
	
	static class ViewHolder {
		CheckBox chk;
		TextView tvTitle;
		TextView tvSummary;
		CheckableRelativeLayout linearLayout;
	}
}