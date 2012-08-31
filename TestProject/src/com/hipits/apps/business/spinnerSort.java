package com.hipits.apps.business;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.hipits.apps.business.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

public class spinnerSort extends Activity {
	Spinner spinner_select;
	ListView listView_list;
	static final String[] CATEGORY = {"30일이상 연락 되지않은 사람들","15일~30일전에 연락한 사람들", "15일이내에 연락한 사람들"};
	long ydate;
	SpinnerSortAdapter adapter1;
	SpinnerSortAdapter adapter2;
	SpinnerSortAdapter adapter3;
	int flag;
	ArrayList<String> nameResult1, numberResult1, nameNumbers1;
	ArrayList<String> nameResult2, numberResult2, nameNumbers2;
	ArrayList<String> nameResult3, numberResult3, nameNumbers3;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = new MenuInflater(this);
		menuInflater.inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId()==R.id.nameinsert){
			Intent intent = new Intent(spinnerSort.this, NameInsert.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onBackPressed() {
		String alertTitle = getResources().getString(R.string.app_name);
		String buttonMessage = getResources().getString(R.string.alert_msg_exit);
		String buttonYes = getResources().getString(R.string.button_yes);
		String buttonNo = getResources().getString(R.string.button_no);
		new AlertDialog.Builder(spinnerSort.this)
	      .setTitle(alertTitle)
	      .setMessage(buttonMessage)
	      .setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
	    	  public void onClick(DialogInterface dialog, int which) {
	    		  finish();
	    	  }
	    	  }).setNegativeButton(buttonNo, null).show();
		}		    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.spinnersort);
		spinner_select = (Spinner) findViewById(R.id.spinner_select);
		listView_list = (ListView) findViewById(R.id.listView_list);
		ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, CATEGORY);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_select.setAdapter(spinner_adapter);

    	spinnerInsert();
		
		spinner_select.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (CATEGORY[(int) arg3].equals("30일이상 연락 되지않은 사람들")){
					listView_list.setAdapter(adapter1);
					flag = 0;
				}
				else if (CATEGORY[(int) arg3].equals("15일~30일전에 연락한 사람들")){
					listView_list.setAdapter(adapter2);
					flag = 1;
				}
				else {
					listView_list.setAdapter(adapter3);
					flag = 2;
				}
			}
				public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		
	    listView_list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(spinnerSort.this, NumberInfo.class);
				if (flag==2){
					//최근
					intent.putExtra("name", (String) nameResult3.get(arg2));
					intent.putExtra("number", (String) nameNumbers3.get(arg2));
					intent.putExtra("date", (String)  numberResult3.get(arg2));
				}
				else if(flag ==1){
					//15일이상
					intent.putExtra("name", (String) nameResult2.get(arg2));
					intent.putExtra("number", (String) nameNumbers2.get(arg2));
					intent.putExtra("date", (String) numberResult2.get(arg2));
				}
				else {
					//30일이상
					intent.putExtra("name", (String) nameResult1.get(arg2));
					intent.putExtra("number", (String) nameNumbers1.get(arg2));
					intent.putExtra("date", (String) numberResult1.get(arg2));
				}
				startActivity(intent);
			}
		});
	}
	@Override
	protected void onResume() {
		super.onResume();
		spinnerInsert();
		if (flag==0){
			listView_list.setAdapter(adapter1);
		}
		else if (flag==1){
			listView_list.setAdapter(adapter2);
		}
		else {
			listView_list.setAdapter(adapter3);
		}
	}
	public void spinnerInsert(){
		String number="";
        SQLiteDatabase mDatabase=openOrCreateDatabase(
        		"numbermanager.db", Context.MODE_PRIVATE, null);

		Cursor mCursor = mDatabase.rawQuery("SELECT * "+"FROM "+ "manager group by number;", null);
		int indexnumber = mCursor.getColumnIndex("number");
		int indextime = mCursor.getColumnIndex("time");
		mCursor.moveToFirst();
		
		nameResult1 = new ArrayList<String>();
		numberResult1 = new ArrayList<String>();
		nameNumbers1 = new ArrayList<String>();
		
		nameResult2 = new ArrayList<String>();
		numberResult2 = new ArrayList<String>();
		nameNumbers2 = new ArrayList<String>();
		
		nameResult3 = new ArrayList<String>();
		numberResult3 = new ArrayList<String>();
		nameNumbers3 = new ArrayList<String>();
		
      SQLiteDatabase dbDatabase=openOrCreateDatabase(
		"numbermanager.db", Context.MODE_PRIVATE, null);

      Cursor dbCursor = dbDatabase.rawQuery("SELECT * "+"FROM "+ "namemanager group by number;", null);
      int inName = dbCursor.getColumnIndex("name");
      int inNumber = dbCursor.getColumnIndex("number");   
		
		while (!mCursor.isAfterLast()){
			number = mCursor.getString(indexnumber);			
			ydate = mCursor.getLong(indextime);
			long currentTime  =System.currentTimeMillis(); //현재 시간을 msec로 구한다.
			long subTime = currentTime - ydate;
			Date date = new Date(ydate);
			SimpleDateFormat currentDateFormat = new SimpleDateFormat("yyyy/MM/dd");
			String currentDate = currentDateFormat.format(date);
			dbCursor.moveToFirst();
			while(!dbCursor.isAfterLast()){
				if (number.equals(dbCursor.getString(inNumber))){
					if (subTime >= 2592000000l){		//한달이상
						nameResult1.add(dbCursor.getString(inName));
						nameNumbers1.add(number);
						numberResult1.add(currentDate);
					}
					else if (subTime < 2592000000l && subTime >=1296000000){		//보름이상
						nameResult2.add(dbCursor.getString(inName));
						nameNumbers2.add(number);
						numberResult2.add(currentDate);
					}
					else if (subTime < 1296000000){		//최근
						nameResult3.add(dbCursor.getString(inName));
						nameNumbers3.add(number);
						numberResult3.add(currentDate);
					}
				}
				dbCursor.moveToNext();
			}
			mCursor.moveToNext();
		}
	    mCursor.close();
	    mDatabase.close();   	
	    dbCursor.close();
	    dbDatabase.close();
		adapter1 = new SpinnerSortAdapter(spinnerSort.this, R.layout.spinnersorttext, nameResult1, numberResult1);
		adapter2 = new SpinnerSortAdapter(spinnerSort.this, R.layout.spinnersorttext, nameResult2, numberResult2);
		adapter3 = new SpinnerSortAdapter(spinnerSort.this, R.layout.spinnersorttext, nameResult3, numberResult3);
	}
}
