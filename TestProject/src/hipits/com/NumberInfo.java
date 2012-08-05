package hipits.com;

import java.io.ObjectOutputStream.PutField;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

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
	String currentDate, message="없습니다.";
	long ydate;
	ArrayList<Integer> aNumberList, defaultDate,numbers2;
	int i=0, dateNumber;
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
		
		while (!mCursor.isAfterLast()){
				ydate = mCursor.getLong(indexdate);
				Date date = new Date(ydate);//현재 시간을 저장한다
				//시간 포멧으로 만든다.
				SimpleDateFormat currentDateFormat = new SimpleDateFormat("dd");
				currentDate = currentDateFormat.format(date);
				aNumberList.add(Integer.parseInt(currentDate));
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
        
        graphView = new BarGraphView(NumberInfo.this, "BarGraph");
        graphView.setViewPort(1, 30);
        graphView.setScrollable(true);
        graphView.addSeries(new GraphViewSeries(data));
        graph.addView(graphView);
	}
}
