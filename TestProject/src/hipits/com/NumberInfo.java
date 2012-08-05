package hipits.com;


import java.io.ObjectOutputStream.PutField;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import hipits.com.R;
import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LineGraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphView.GraphViewSeries;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NumberInfo extends Activity {
	TextView text_name, text_number, text_date, text_massage;
	LinearLayout graph;
	Date date;
	String currentDate, message, month;
	long ydate;
	ArrayList<Integer> aNumberList, defaultDate,numbers2;
	int i=0, dateNumber;
	int currentMonth, monthInt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.numberinfomation);
		
		text_name = (TextView) findViewById(R.id.textView_name);
		text_number = (TextView) findViewById(R.id.textView_number);
		text_date = (TextView) findViewById(R.id.textView_date);
		text_massage = (TextView) findViewById(R.id.textView_massage);
		
		 Log.v("errorNO", "0");
		 Intent intent = getIntent();
		 String number = intent.getStringExtra("number").trim();		
		 text_name.setText(intent.getStringExtra("name"));
		text_number.setText(intent.getStringExtra("number"));
		text_date.setText(intent.getStringExtra("date"));
		
		SQLiteDatabase mDatabase=openOrCreateDatabase(
        		"numbermanager.db", Context.MODE_PRIVATE, null);
        Log.v("errorNO", "2");
		Cursor mCursor = mDatabase.rawQuery("SELECT * "+"FROM "+ "manager where number="+number+";", null);
		Log.v("errorNO", "3");
		int indexdate = mCursor.getColumnIndex("time");
		int indexmessage = mCursor.getColumnIndex("message");
		Log.v("errorNO", "4");
		mCursor.moveToFirst();
		aNumberList = new ArrayList<Integer>();
		defaultDate = new ArrayList<Integer>();
		numbers2 = new ArrayList<Integer>();
		
		long currentMonth  =System.currentTimeMillis(); //현재 시간을 msec로 구한다.
		Date dateMonth = new Date(currentMonth);//현재 시간을 저장한다
		//시간 포멧으로 만든다.
		SimpleDateFormat currentMonthFormat = new SimpleDateFormat("MM");
		String currentMonthString = currentMonthFormat.format(dateMonth);
		month = currentMonthString.trim();
		this.currentMonth = Integer.parseInt(month);
		Log.v("currentMonth", month);
		while (!mCursor.isAfterLast()){
				ydate = mCursor.getLong(indexdate);
				Date saveDate = new Date(ydate);//저장된 시간을 저장한다
				
				//시간 포멧으로 만든다.
				//SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
				String MonthString = currentMonthFormat.format(saveDate);
				monthInt = Integer.parseInt(MonthString);
				Log.v("month", MonthString);
				SimpleDateFormat currentDateFormat = new SimpleDateFormat("dd");
				if(this.currentMonth == monthInt){
					currentDate = currentDateFormat.format(saveDate);
					aNumberList.add(Integer.parseInt(currentDate));
					Log.v("test", "no????");
				}
				message = mCursor.getString(indexmessage);
				text_massage.setText(message);
			mCursor.moveToNext();
	    }

        Log.v("eee", "아놔1");
        
        for(int i = 0; i<31; i++){
        	defaultDate.add(i+1);
        }
        
        for(int i=0; i <defaultDate.size(); i++){
		int k=0;
			for (int j = 0; j<aNumberList.size(); j++){
				if(defaultDate.get(i) == aNumberList.get(j)){
					k++;
				} 
			}
			numbers2.add(k);
		}
		GraphViewData[] data = new GraphViewData[defaultDate.size()];
		
		for (int i=0; i<defaultDate.size(); i++) {
			data[i] = new GraphViewData(defaultDate.get(i), numbers2.get(i));
		}
        GraphView graphView;
        graph = (LinearLayout) findViewById(R.id.graph);
        
        graphView = new BarGraphView(NumberInfo.this, month+"월");
        graphView.setViewPort(1, 30);
        graphView.setScrollable(true);
        graphView.addSeries(new GraphViewSeries(data));
        graph.addView(graphView);
	}
}
