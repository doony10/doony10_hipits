package com.hipits.apps.business;


import java.io.ObjectOutputStream.PutField;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import com.hipits.apps.business.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NumberInfo extends Activity {
	TextView text_name, text_number, text_date, text_massage;
	LinearLayout graph;
	Date date;
	String currentDate, message, month, numbers;
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
			if( intent.getStringExtra("number").trim().length() >8){
				numbers = intent.getStringExtra("number").trim().substring(3);
			}
			else{
				 numbers = intent.getStringExtra("number").trim();	
			}
		text_name.setText(intent.getStringExtra("name"));
		text_number.setText(intent.getStringExtra("number"));
		text_date.setText(intent.getStringExtra("date"));		

		SQLiteDatabase mDatabase=openOrCreateDatabase(
        		"numbermanager.db", Context.MODE_PRIVATE, null);
        Log.v("errorNO", "2");
		Cursor mCursor = mDatabase.rawQuery("SELECT * "+"FROM "+ "manager where number="+numbers+";", null);
		Log.v("errorNO", "3"+numbers);
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
//				if(this.currentMonth == monthInt){
					currentDate = currentDateFormat.format(saveDate);
					aNumberList.add(Integer.parseInt(currentDate));
					Log.v("test", "no????");
//				}
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
        ArrayList<double[]> values = new ArrayList<double[]>();
        double[] ddo = new double[31];
        for(int i = 0; i<31;i++){
        	ddo[i]=numbers2.get(i);
        }
        values.add(ddo);
       //그래프 출력을 위한 그래픽 속성 지정 객체
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		String currentMouth = currentMonthFormat.format(dateMonth);
      //상단 표시 제목과 글자 크기
        renderer.setChartTitle(currentMouth+"월 연락횟수 통계");
        renderer.setChartTitleTextSize(40);
        //분류에 대한 이름
        String[] titles = new String[] {"일수별 연락횟수"};
        //항목을 표시하는데 사용될 색상값
        int[] colors = new int[] {Color.YELLOW};
        //분류명 글자 크기 및 각 색상 지정
        renderer.setLegendTextSize(15);
        int length = colors.length;
        for (int i =0; i<length; i++){
        	SimpleSeriesRenderer r = new SimpleSeriesRenderer();
        	r.setColor(colors[i]);
        	renderer.addSeriesRenderer(r);
        }
        //X,Y축 항목이름과 글자 크기
        renderer.setXTitle("일");
        renderer.setYTitle("연락횟수");
        renderer.setAxisTitleTextSize(20);
        
        //수치값 글자 크기 / x축 최소,최대값/ y축 최소,최대값
        renderer.setLabelsTextSize(15);
        renderer.setXAxisMin(0.5);
        renderer.setXAxisMax(31.5);
        renderer.setYAxisMin(0);
        renderer.setYAxisMax(5);
        
        //X,Y축 라인 색상
        renderer.setAxesColor(Color.WHITE);
        
        //상단제목, X,Y축 제목, 수치값의 글자 색상
        renderer.setLabelsColor(Color.CYAN);
        //X,Y축 정렬 방향
        renderer.setXLabelsAlign(Align.LEFT);
        renderer.setYLabelsAlign(Align.LEFT);
        
        //X,Y축 스크롤 여부 ON/OFF
        renderer.setPanEnabled(false, false);
        //ZOOM기능 ON/OFF
        renderer.setZoomEnabled(false, false);
        //ZOOM비율
        renderer.setZoomRate(1.0f);
        //막대간 간격
        renderer.setBarSpacing(0.5f);
        
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        for (int i = 0; i< titles.length;i++){
        	CategorySeries series = new CategorySeries(titles[i]);
        	double[] v = values.get(i);
        	int seriesLength = v.length;
        	for (int k = 0; k<seriesLength;k++){
        		series.add(v[k]);
        	}
        	dataset.addSeries(series.toXYSeries());
        }
        
        GraphicalView gv = ChartFactory.getBarChartView(this, dataset, renderer, org.achartengine.chart.BarChart.Type.STACKED);
        LinearLayout llBody = (LinearLayout) findViewById(R.id.graph);
        llBody.addView(gv);
	}
}
