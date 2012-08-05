package hipits.com;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;

public class ProjectTestActivity extends Activity {
	String number="", message="";
	long date;
	LinearLayout layout;
    /** Called when the activity is first created. */
	@Override
	public void onBackPressed() {
		String alertTitle = getResources().getString(R.string.app_name);
		String buttonMessage = getResources().getString(R.string.alert_msg_exit);
		String buttonYes = getResources().getString(R.string.button_yes);
		String buttonNo = getResources().getString(R.string.button_no);
		new AlertDialog.Builder(ProjectTestActivity.this)
	      .setTitle(alertTitle)
	      .setMessage(buttonMessage)
	      .setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
	    	  public void onClick(DialogInterface dialog, int which) {
	    		  finish();
	    	  }
	    	  }).setNegativeButton(buttonNo, null).show();
			//super.onBackPressed();
		}		    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		DBAdapter db = new DBAdapter(getApplicationContext());
		db.open();
        MyPhoneStateListener phoneListener=new MyPhoneStateListener(this); 
        TelephonyManager telephonyManager  =(TelephonyManager)getSystemService(TELEPHONY_SERVICE); 
        telephonyManager.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE);
		
		layout = (LinearLayout) findViewById(R.id.layout);
		layout.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				Intent intent = new Intent(ProjectTestActivity.this, spinnerSort.class);
				startActivity(intent);
				finish();
				return false;
			}
		});
		db.close();
    }
    }