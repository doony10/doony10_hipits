package com.hipits.apps.business;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import net.daum.adam.publisher.AdView;
import net.daum.adam.publisher.AdView.AnimationType;
import net.daum.adam.publisher.AdView.OnAdClickedListener;
import net.daum.adam.publisher.AdView.OnAdClosedListener;
import net.daum.adam.publisher.AdView.OnAdFailedListener;
import net.daum.adam.publisher.AdView.OnAdLoadedListener;
import net.daum.adam.publisher.AdView.OnAdWillLoadListener;
import net.daum.adam.publisher.impl.AdError;

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
import android.view.View;
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
	private AdView adView = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.numberinfomation);
		
		initAdam();
		
		text_name = (TextView) findViewById(R.id.textView_name);
		text_number = (TextView) findViewById(R.id.textView_number);
		text_date = (TextView) findViewById(R.id.textView_date);
		text_massage = (TextView) findViewById(R.id.textView_massage);
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
		Cursor mCursor = mDatabase.rawQuery("SELECT * "+"FROM "+ "manager where number="+numbers+";", null);
		int indexdate = mCursor.getColumnIndex("time");
		int indexmessage = mCursor.getColumnIndex("message");
		mCursor.moveToFirst();
		aNumberList = new ArrayList<Integer>();
		defaultDate = new ArrayList<Integer>();
		numbers2 = new ArrayList<Integer>();
		
		long currentMonth  =System.currentTimeMillis(); //현재 시간을 msec로 구한다.
		Date dateMonth = new Date(currentMonth);//현재 시간을 저장한다
		//시간 포멧으로 만든다.
		SimpleDateFormat currentMonthFormat = new SimpleDateFormat("MM");
		String currentMonthString = currentMonthFormat.format(dateMonth);
		String currentMouth = currentMonthFormat.format(dateMonth);
		
		month = currentMonthString.trim();
		this.currentMonth = Integer.parseInt(month);
		
		while (!mCursor.isAfterLast()){
				ydate = mCursor.getLong(indexdate);
				Date saveDate = new Date(ydate);//저장된 시간을 저장한다
				
				//시간 포멧으로 만든다.
				//SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
				String MonthString = currentMonthFormat.format(saveDate);
				monthInt = Integer.parseInt(MonthString);
				SimpleDateFormat currentDateFormat = new SimpleDateFormat("dd");
//				if(this.currentMonth == monthInt){
				if(currentMonthFormat.format(saveDate).equals(currentMonthFormat.format(dateMonth))){
					currentDate = currentDateFormat.format(saveDate);
					aNumberList.add(Integer.parseInt(currentDate));
				}
//				}
				message = mCursor.getString(indexmessage);
				text_massage.setText(message);
			mCursor.moveToNext();
	    }        
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
	private void initAdam() {
		adView = (AdView) findViewById(R.id.adview_info);
		adView.setRequestInterval(5);
		adView.setClientId("38e8Z8cT13980171560");

		adView.setRequestInterval(12);
		adView.setAnimationType(AnimationType.FLIP_HORIZONTAL);

		adView.setVisibility(View.VISIBLE);
	}
}
