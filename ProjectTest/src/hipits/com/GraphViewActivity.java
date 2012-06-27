package hipits.com;

import java.sql.Date;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

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
	int i=0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph);
        SQLiteDatabase mDatabase=openOrCreateDatabase(
        		"numbermanager.db", Context.MODE_PRIVATE, null);

		Cursor mCursor = mDatabase.rawQuery("SELECT * "+"FROM "+ "manager;", null);
		
		int indexnumber = mCursor.getColumnIndex("number");
		int indexdate = mCursor.getColumnIndex("time");
		mCursor.moveToFirst();
		
		while (!mCursor.isAfterLast()){
			String number = mCursor.getString(indexnumber);
			
			if (number.equals("01099472877")){
				i++;			
				ydate = mCursor.getLong(indexdate);
				Date date = new Date(ydate);//현재 시간을 저장한다
				//시간 포멧으로 만든다.
				SimpleDateFormat currentDateFormat = new SimpleDateFormat("yyyy/MM/dd");
				currentDate = currentDateFormat.format(date);
			}
			mCursor.moveToNext();
	    }
        Log.v("eee", "아놔1");
       /* GraphViewSeries gvs = new GraphViewSeries(new  GraphViewData[] {
        		new  GraphViewData(1, 2.6d),
        		new  GraphViewData(2, 1.5d),
        		new  GraphViewData(3, 2.5d),
        		new  GraphViewData(4, 1.0d),
        		new  GraphViewData(5, 3.0d)
        });*/
        int num = 150;
		GraphViewData[] data = new GraphViewData[num];
		double v=0;
		for (int i=0; i<num; i++) {
			v += 0.2;
			data[i] = new GraphViewData(i, Math.cos(v));
		}
        GraphView graphView;
        graph1 = (LinearLayout) findViewById(R.id.graph1);
        graph2 = (LinearLayout) findViewById(R.id.graph2);
        Log.v("eee", "아놔2");     
        
       graphView = new LineGraphView(GraphViewActivity.this, "GraphView");
       graphView.setViewPort(2, 40);
       graphView.setScrollable(true);
        graphView.addSeries(new GraphViewSeries(data));
        Log.v("eee", "아놔3");
        graph1.addView(graphView); 
        graphView = new BarGraphView(GraphViewActivity.this, "BarGraph");
        graphView.addSeries(new GraphViewSeries(data));
        graph2.addView(graphView);
    }
}