package ks.ac.kr;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SMSTestActivity extends Activity{
    /** Called when the activity is first created. */
	String number="", message="";
	long date;
	LinearLayout layout;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        SQLiteDatabase mDatabase=openOrCreateDatabase(
        		"date_message.db", Context.MODE_PRIVATE, null);
		mDatabase.execSQL("create table IF NOT EXISTS on_messages (_id INTEGER PRIMARY KEY AUTOINCREMENT, "+ "number TEXT NOT NULL, " + "date LONG NOT NULL, "+"message TEXT NOT NULL)");
		layout = (LinearLayout) findViewById(R.id.layout);
		layout.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				Intent intent = new Intent(SMSTestActivity.this, spinnerSort.class);
				startActivity(intent);
				return false;
			}
		});
    }
}