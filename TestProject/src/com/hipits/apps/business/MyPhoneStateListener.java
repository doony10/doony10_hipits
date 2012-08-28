package com.hipits.apps.business;

import java.sql.Date;
import java.text.SimpleDateFormat;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class MyPhoneStateListener extends PhoneStateListener {
	
	String comingNumber;
	private Context context;
	
	public MyPhoneStateListener (Context context) {
		this.context = context;		
	}
	@Override 
    public void onCallStateChanged(int state,String incomingNumber){ 
		DBAdapter db = new DBAdapter((context));

     if (state == TelephonyManager.CALL_STATE_IDLE) {
    	 Log.v("PhoneCallState", "STATE_IDLE : Incoming number "+incomingNumber);
    	 
     }
     else if (state == TelephonyManager.CALL_STATE_RINGING) {
            Log.v("PhoneCallState", "STATE_RINGING : Incoming number "+incomingNumber);
            if(incomingNumber.length()>8){
                comingNumber = incomingNumber.substring(3);
			}
			else{
	            comingNumber = incomingNumber;
			}

     }
     else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
            Log.v("PhoneCallState", "STATE_OFFHOOK : Incoming number "+incomingNumber);
     		long currentTime  =System.currentTimeMillis(); //현재 시간을 msec로 구한다.
            
    		db.open();
    		db.insertEntry(comingNumber, currentTime, "전화입니다.");
    		db.close();
     }
    }
}
