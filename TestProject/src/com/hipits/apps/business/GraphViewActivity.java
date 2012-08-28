package com.hipits.apps.business;


import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.hipits.apps.business.R;
import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphView.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

public class GraphViewActivity extends Activity {
    /** Called when the activity is first created. */
	LinearLayout graph1,graph2;
	Date date;
	String currentDate;
	long ydate;
	ArrayList<Integer> aNumberList, defaultDate,numbers2;
	int i=0, dateNumber;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph);
		Log.v("errorNO", "1");
        SQLiteDatabase mDatabase=openOrCreateDatabase(
        		"numbermanager.db", Context.MODE_PRIVATE, null);
        Log.v("errorNO", "2");
		Cursor mCursor = mDatabase.rawQuery("SELECT * "+"FROM "+ "manager where number=4132173;", null);
		Log.v("errorNO", "3");
		int indexdate = mCursor.getColumnIndex("time");
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
			mCursor.moveToNext();
	    }
        Log.v("eee", "아놔1");
        
        for(int i = 0; i<30; i++){
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
			Log.v("numbers2", numbers2.get(i)+"");
			Log.v("errorNO", "7");
		}
		GraphViewData[] data = new GraphViewData[defaultDate.size()];
		
		for (int i=0; i<defaultDate.size(); i++) {
			data[i] = new GraphViewData(defaultDate.get(i), numbers2.get(i));
		}
        GraphView graphView;
        graph1 = (LinearLayout) findViewById(R.id.graph1);
        graph2 = (LinearLayout) findViewById(R.id.graph2);
        Log.v("eee", "아놔2");     
        
       graphView = new LineGraphView(GraphViewActivity.this, "GraphView");
       graphView.setViewPort(1, 11);
       graphView.setScrollable(true);
        graphView.addSeries(new GraphViewSeries(data));
        Log.v("eee", "아놔3");
        graph1.addView(graphView); 
        graphView = new BarGraphView(GraphViewActivity.this, "BarGraph");
        graphView.setViewPort(1, 11);
        graphView.setScrollable(true);
        graphView.addSeries(new GraphViewSeries(data));
        graph2.addView(graphView);
    }
}