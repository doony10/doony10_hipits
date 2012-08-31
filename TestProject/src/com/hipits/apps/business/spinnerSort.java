package com.hipits.apps.business;


import java.io.ObjectOutputStream.PutField;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Parser;

import com.hipits.apps.business.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
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
import android.widget.Toast;

public class spinnerSort extends Activity {
	Spinner spinner_select;
	ListView listView_list;
	static final String[] CATEGORY = {"30일이상 연락 되지않은 사람들","15일~30일전에 연락한 사람들", "15일이내에 연락한 사람들"};
	long ydate;
	SpinnerSortAdapter adapter1;
	SpinnerSortAdapter adapter2;
	SpinnerSortAdapter adapter3;
	int flag;
	ArrayList nameResult1, numberResult1, nameNumbers1;
	ArrayList nameResult2, numberResult2, nameNumbers2;
	ArrayList nameResult3, numberResult3, nameNumbers3;
	Bundle saveBundle;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = new MenuInflater(this);
		menuInflater.inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId()==R.id.nameinsert){
			Log.v("start", "go");
			Intent intent = new Intent(spinnerSort.this, NameInsert.class);
			Log.v("start", "gogo");
			startActivity(intent);
//			finish();
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
		
		saveBundle = savedInstanceState;
		spinner_select = (Spinner) findViewById(R.id.spinner_select);
		listView_list = (ListView) findViewById(R.id.listView_list);
		ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, CATEGORY);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_select.setAdapter(spinner_adapter);
        Log.v("ddd", "no1");

    	String number="", message="";
        SQLiteDatabase mDatabase=openOrCreateDatabase(
        		"numbermanager.db", Context.MODE_PRIVATE, null);

		Cursor mCursor = mDatabase.rawQuery("SELECT * "+"FROM "+ "manager group by number;", null);
		int indexnumber = mCursor.getColumnIndex("number");
		int indextime = mCursor.getColumnIndex("time");
		mCursor.moveToFirst();
		
		final ArrayList result1= new ArrayList();
		final ArrayList result2= new ArrayList();
		final ArrayList result3= new ArrayList();
		final ArrayList result1_day = new ArrayList();
		final ArrayList result2_day = new ArrayList();
		final ArrayList result3_day = new ArrayList();
		
		nameResult1 = new ArrayList();
		numberResult1 = new ArrayList();
		nameNumbers1 = new ArrayList();
		
		nameResult2 = new ArrayList();
		numberResult2 = new ArrayList();
		nameNumbers2 = new ArrayList();
		
		nameResult3 = new ArrayList();
		numberResult3 = new ArrayList();
		nameNumbers3 = new ArrayList();
		
	    Log.v("ddd", "no4");
		while (!mCursor.isAfterLast()){
			number = mCursor.getString(indexnumber);			
			int num = Integer.parseInt(number);
			ydate = mCursor.getLong(indextime);
			long currentTime  =System.currentTimeMillis(); //현재 시간을 msec로 구한다.
			long subTime = currentTime - ydate;
			Date date = new Date(ydate);//현재 시간을 저장한다
			//시간 포멧으로 만든다.
			SimpleDateFormat currentDateFormat = new SimpleDateFormat("yyyy/MM/dd");
			String currentDate = currentDateFormat.format(date);
				if (subTime >= 2592000000l){		//한달이상
					result1.add(number);
					result1_day.add(currentDate);
				}
				else if (subTime < 2592000000l && subTime >=1296000000){		//보름이상
					result2.add(number);
					result2_day.add(currentDate);
				}
				else if (subTime < 1296000000){		//최근
					Log.v("testnumbers", number);
						result3.add(number);
						result3_day.add(currentDate);
				}
				mCursor.moveToNext();
			}
	    mCursor.close();
	    mDatabase.close();
	    
        SQLiteDatabase dbDatabase=openOrCreateDatabase(
        		"numbermanager.db", Context.MODE_PRIVATE, null);

		Cursor dbCursor = dbDatabase.rawQuery("SELECT * "+"FROM "+ "namemanager group by number;", null);
		int inName = dbCursor.getColumnIndex("name");
		int inNumber = dbCursor.getColumnIndex("number");   
		
	    for (int i = 0; i<result3.size();i++){
	    	String fors3 = (String) result3.get(i);
	    	String days3 = (String) result3_day.get(i);
	    	Log.v("fors3s", fors3);
			dbCursor.moveToFirst();
			while (!dbCursor.isAfterLast()){
				String nNumber = dbCursor.getString(inNumber);
				Log.v("fors3s", nNumber);
				if (fors3.equals(nNumber)){
					Log.v("nametest", dbCursor.getString(inName));
					nameResult3.add(dbCursor.getString(inName));
					nameNumbers3.add((String) result3.get(i));
					numberResult3.add(days3);
				}
				dbCursor.moveToNext();
			}
	    }
	    for (int i = 0; i<result1.size();i++){
	    	String fors1 = (String) result1.get(i);
	    	String days1 = (String) result1_day.get(i);
			dbCursor.moveToFirst();
			while (!dbCursor.isAfterLast()){
				String nNumber = dbCursor.getString(inNumber);
				if (fors1.equals(nNumber)){
					Log.v("nametest", dbCursor.getString(inName));
					nameResult1.add(dbCursor.getString(inName));
					nameNumbers1.add((String) result1.get(i));
					numberResult1.add(days1);
				}
				dbCursor.moveToNext();
			}
	    }
	    for (int i = 0; i<result2.size();i++){
	    	String fors2 = (String) result2.get(i);
	    	String days2 = (String) result2_day.get(i);
			dbCursor.moveToFirst();
			while (!dbCursor.isAfterLast()){
				Log.v("nametest", dbCursor.getString(inName));
				String nNumber = dbCursor.getString(inNumber);
				if (fors2.equals(nNumber)){
					nameResult2.add(dbCursor.getString(inName));
					nameNumbers2.add((String) result2.get(i));
					numberResult2.add(days2);
				}
				dbCursor.moveToNext();
			}
	    }
		dbCursor.close();
    	dbDatabase.close();
    	
    	
		adapter1 = new SpinnerSortAdapter(spinnerSort.this, R.layout.spinnersorttext, nameResult1, numberResult1);
		adapter2 = new SpinnerSortAdapter(spinnerSort.this, R.layout.spinnersorttext, nameResult2, numberResult2);
		adapter3 = new SpinnerSortAdapter(spinnerSort.this, R.layout.spinnersorttext, nameResult3, numberResult3);
		
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
				String tag="intent";
				Log.v(tag, "no1");
				Intent intent = new Intent(spinnerSort.this, NumberInfo.class);
				if (flag==2){
					//최근
					intent.putExtra("name", (String) nameResult3.get(arg2));
					intent.putExtra("number", (String) nameNumbers3.get(arg2));
					intent.putExtra("date", (String)  numberResult3.get(arg2));
					Log.v(tag, nameResult3.get(arg2)+"");
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
				Log.v(tag, "no2");
				startActivity(intent);
				Log.v(tag, "no3");
			}
		});
	}
	@Override
	protected void onResume() {
		super.onResume();
		onCreate(saveBundle);
	}
}
