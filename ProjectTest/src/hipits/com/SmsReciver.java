package hipits.com;

import java.sql.Date;
import java.text.SimpleDateFormat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReciver extends BroadcastReceiver {
	
	String str="";
	String number="";
	String message="";
	@Override
	public void onReceive(Context context, Intent intent) {
Log.d("MY_TAG", "BroadcastReceiver onReceive()");
		if(intent.getAction().equals("android.provider.Telephony.SEND_SMS")){
			Toast.makeText(context, "���� ���¼�", Toast.LENGTH_LONG).show();
		}
		if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
			StringBuilder stringBuilder=new StringBuilder();
			Bundle bundle = intent.getExtras();
			
			if(bundle != null){
				Object[] pdusObjects=(Object[]) bundle.get("pdus");
				
				SmsMessage[] messages = new SmsMessage[pdusObjects.length];
				for (int i =0; i < pdusObjects.length; i++){
					messages[i]=SmsMessage.createFromPdu((byte[]) pdusObjects[i]);
					number=messages[i].getDisplayOriginatingAddress();
					message=messages[i].getMessageBody();
				}

				long currentTime  =System.currentTimeMillis(); //���� �ð��� msec�� ���Ѵ�.
				Date date = new Date(currentTime);//���� �ð��� �����Ѵ�
				//�ð� �������� �����.
				SimpleDateFormat currentDateFormat = new SimpleDateFormat("yyyy/MM/dd");
				String currentDate = currentDateFormat.format(date);
				
				DBAdapter db = new DBAdapter((context));
				db.open();
				db.insertEntry(number, currentTime, message);
				db.close();
				for (SmsMessage currentMessage:messages){
					stringBuilder.append("���ڸ޼����� ���ŵǾ����ϴ�\n");
					stringBuilder.append("[�߽��� ��ȭ ��ȣ]\n");
					stringBuilder.append(currentMessage.getOriginatingAddress());
					stringBuilder.append("\n[���� �޼���]\n");
					stringBuilder.append(currentMessage.getMessageBody());
					stringBuilder.append("\n"+currentDate);
				}
			}
			Toast.makeText(context, stringBuilder.toString(), Toast.LENGTH_LONG).show();
		}
	}
}
