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
		
		adView = (AdView) findViewById(R.id.adview_info);
		Adams adams=new Adams(adView);
		adams.setDaemon(true);
		adams.start();
		
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
		
		long currentMonth  =System.currentTimeMillis(); //���� �ð��� msec�� ���Ѵ�.
		Date dateMonth = new Date(currentMonth);//���� �ð��� �����Ѵ�
		//�ð� �������� �����.
		SimpleDateFormat currentMonthFormat = new SimpleDateFormat("MM");
		String currentMonthString = currentMonthFormat.format(dateMonth);
		String currentMouth = currentMonthFormat.format(dateMonth);
		
		month = currentMonthString.trim();
		this.currentMonth = Integer.parseInt(month);
		
		while (!mCursor.isAfterLast()){
				ydate = mCursor.getLong(indexdate);
				Date saveDate = new Date(ydate);//����� �ð��� �����Ѵ�
				
				//�ð� �������� �����.
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
       //�׷��� ����� ���� �׷��� �Ӽ� ���� ��ü
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
      //��� ǥ�� ����� ���� ũ��        
        renderer.setChartTitle(currentMouth+"월 연락 횟수");
        renderer.setChartTitleTextSize(40);
        //�з� ���� �̸�
        String[] titles = new String[] {"일수 별 연락 횟수"};
        //�׸��� ǥ���ϴµ� ���� ����
        int[] colors = new int[] {Color.YELLOW};
        //�з�� ���� ũ�� �� �� ���� ����
        renderer.setLegendTextSize(15);
        int length = colors.length;
        for (int i =0; i<length; i++){
        	SimpleSeriesRenderer r = new SimpleSeriesRenderer();
        	r.setColor(colors[i]);
        	renderer.addSeriesRenderer(r);
        }
        //X,Y�� �׸��̸��� ���� ũ��
        renderer.setXTitle("일");
        renderer.setYTitle("연락횟수");
        renderer.setAxisTitleTextSize(20);
        
        //��ġ�� ���� ũ�� / x�� �ּ�,�ִ밪/ y�� �ּ�,�ִ밪
        renderer.setLabelsTextSize(15);
        renderer.setXAxisMin(0.5);
        renderer.setXAxisMax(31.5);
        renderer.setYAxisMin(0);
        renderer.setYAxisMax(9);
        
        //X,Y�� ���� ����
        renderer.setAxesColor(Color.WHITE);
        
        //�������, X,Y�� ����, ��ġ���� ���� ����
        renderer.setLabelsColor(Color.CYAN);
        //X,Y�� ���� ����
        renderer.setXLabelsAlign(Align.LEFT);
        renderer.setYLabelsAlign(Align.LEFT);
        
        //X,Y�� ��ũ�� ���� ON/OFF
        renderer.setPanEnabled(false, false);
        //ZOOM��� ON/OFF
        renderer.setZoomEnabled(false, false);
        //ZOOM����
        renderer.setZoomRate(1.0f);
        //���밣 ����
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
