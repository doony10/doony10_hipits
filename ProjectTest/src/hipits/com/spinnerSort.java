package hipits.com;

import java.io.ObjectOutputStream.PutField;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

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
	static final String[] CATEGORY = {"30���̻� ���� �������� �����","15��~30������ ������ �����", "15���̳��� ������ �����"};
	long ydate;
	ArrayAdapter adapter1;
	ArrayAdapter adapter2;
	ArrayAdapter adapter3;
	String number="", message="";
	long date;
	int flag;
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch(keyCode){
	     case KeyEvent.KEYCODE_BACK:
	      String alertTitle = getResources().getString(R.string.app_name);
	      String buttonMessage = getResources().getString(R.string.alert_msg_exit);
	      String buttonYes = getResources().getString(R.string.button_yes);
	      String buttonNo = getResources().getString(R.string.button_no);
	         
	      new AlertDialog.Builder(spinnerSort.this)
	      .setTitle(alertTitle)
	      .setMessage(buttonMessage)
	      .setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
	    	  
	       public void onClick(DialogInterface dialog, int which) {
	        // TODO Auto-generated method stub
	        moveTaskToBack(true);
	        finish();
	       }
	      })
	      .setNegativeButton(buttonNo, null)
	      .show();
	     }
	    return true;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater	menuInflater=new MenuInflater(this);
    	menuInflater.inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==R.id.nameinsert){
			Toast.makeText(this, "�ȴ�.", Toast.LENGTH_LONG);
		}
		return super.onOptionsItemSelected(item);
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
        Log.v("ddd", "no1");
        SQLiteDatabase mDatabase=openOrCreateDatabase(
        		"numbermanager.db", Context.MODE_PRIVATE, null);

		Cursor mCursor = mDatabase.rawQuery("SELECT * "+"FROM "+ "manager group by number;", null);
		int indexnumber = mCursor.getColumnIndex("number");
		int indextime = mCursor.getColumnIndex("time");
		mCursor.moveToFirst();
		
		final ArrayList result1= new ArrayList();
		final ArrayList result2= new ArrayList();
		final ArrayList result3= new ArrayList();
		
		while (!mCursor.isAfterLast()){
			number = mCursor.getString(indexnumber);			
			ydate = mCursor.getLong(indextime);
	        long currentTime  =System.currentTimeMillis(); //���� �ð��� msec�� ���Ѵ�.
	        long subTime = currentTime - ydate;
	        Date date = new Date(ydate);//���� �ð��� �����Ѵ�
			//�ð� �������� �����.
			SimpleDateFormat currentDateFormat = new SimpleDateFormat("yyyy/MM/dd");
			String currentDate = currentDateFormat.format(date);
			
			if (subTime >= 2592000000l){		//�Ѵ��̻�
				result1.add(number+"       "+currentDate);
				adapter1=new ArrayAdapter(spinnerSort.this, R.layout.listview_text, result1);
			}
			else if (subTime < 2592000000l && subTime >=1296000000){		//�����̻�
				result2.add(number+"       "+currentDate);
				adapter2=new ArrayAdapter(spinnerSort.this, R.layout.listview_text, result2);
			}
			else if (subTime < 1296000000){		//�ֱ�
				result3.add(number+"      "+currentDate);
				adapter3 = new ArrayAdapter(spinnerSort.this, R.layout.listview_text, result3);
			}
			mCursor.moveToNext();
			spinner_select.setOnItemSelectedListener(new OnItemSelectedListener() {

				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					if (CATEGORY[(int) arg3].equals("30���̻� ���� �������� �����")){
						listView_list.setAdapter(adapter1);
						flag = 0;
					}
					else if (CATEGORY[(int) arg3].equals("15��~30������ ������ �����")){
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
	    }
	    mCursor.close();
	    mDatabase.close();
	    listView_list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String tag="intent";
				String text;
				Log.v(tag, "no1");
				Intent intent = new Intent(spinnerSort.this, NumberInfo.class);
				if (flag==2){
					intent.putExtra("number", (String) result3.get(arg2));
					Log.v(tag, (String) result3.get(arg2));
				}
				else if(flag ==1){
					intent.putExtra("number", (String) result2.get(arg2));
					Log.v(tag, (String) result2.get(arg2));
				}
				else {
					intent.putExtra("number", (String) result1.get(arg2));
					Log.v(tag, (String) result1.get(arg2));
				}
				Log.v(tag, "no2");
				startActivity(intent);
				Log.v(tag, "no3");
			}
		});
	}
}
