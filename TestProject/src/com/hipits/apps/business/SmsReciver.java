package com.hipits.apps.business;

import java.sql.Date;
import java.text.SimpleDateFormat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class SmsReciver extends BroadcastReceiver {
	
	String str="";
	String number="";
	String message="";
	static String comingNumber="";
	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		
		if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
			StringBuilder stringBuilder=new StringBuilder();
			if(bundle != null){
				Object[] pdusObjects=(Object[]) bundle.get("pdus");
				
				SmsMessage[] messages = new SmsMessage[pdusObjects.length];
				for (int i =0; i < pdusObjects.length; i++){
					messages[i]=SmsMessage.createFromPdu((byte[]) pdusObjects[i]);
					if(messages[i].getOriginatingAddress().length()>8){
						number = messages[i].getOriginatingAddress().substring(3);
					}
					else{
						number = messages[i].getOriginatingAddress();
					}
					message=messages[i].getMessageBody();
				}

				long currentTime  =System.currentTimeMillis(); //현재 시간을 msec로 구한다.
				Date date = new Date(currentTime);//현재 시간을 저장한다
				//시간 포멧으로 만든다.
				SimpleDateFormat currentDateFormat = new SimpleDateFormat("yyyy/MM/dd");
				String currentDate = currentDateFormat.format(date);
				
				DBAdapter db = new DBAdapter((context));
				db.open();
				db.insertEntry(number, currentTime, message);
				db.close();
				for (SmsMessage currentMessage:messages){
					stringBuilder.append("문자메세지가 수신되었습니다\n");
					stringBuilder.append("[발신자 전화 번호]\n");
					stringBuilder.append(currentMessage.getOriginatingAddress());
					stringBuilder.append("\n[수신 메세지]\n");
					stringBuilder.append(currentMessage.getMessageBody());
					stringBuilder.append("\n"+currentDate);
				}
			}
			Toast.makeText(context, stringBuilder.toString(), Toast.LENGTH_LONG).show();
		}
		else if(intent.getAction().equals("android.intent.action.PHONE_STATE")){
			String state = bundle.getString(TelephonyManager.EXTRA_STATE);
			if(state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
             }
			else if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                if(bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER).length()>8){
                	comingNumber = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER).substring(3);
				}
				else{
					comingNumber = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
				}
			}
			else if(state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
				DBAdapter db = new DBAdapter((context));
				long currentTime  =System.currentTimeMillis(); //현재 시간을 msec로 구한다.
	            
	    		db.open();
	    		db.insertEntry(comingNumber, currentTime, "call");
	    		db.close();
			}
           }
		}
}
