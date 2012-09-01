package com.hipits.apps.business;

import java.util.ArrayList;

import net.daum.adam.publisher.AdView;
import net.daum.adam.publisher.AdView.AnimationType;
import net.daum.adam.publisher.AdView.OnAdClickedListener;
import net.daum.adam.publisher.AdView.OnAdClosedListener;
import net.daum.adam.publisher.AdView.OnAdFailedListener;
import net.daum.adam.publisher.AdView.OnAdLoadedListener;
import net.daum.adam.publisher.AdView.OnAdWillLoadListener;
import net.daum.adam.publisher.impl.AdError;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.app.Activity;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class NameInsert extends Activity {
	ListView listPerson;
	ArrayList<Integer> positionSave;
	DBAdapter dba;
	private static final String LOGTAG = "BannerTypeXML1";
	private AdView adView = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.namemain);
        
        listPerson = (ListView)findViewById(R.id.listView_nameinsert);        
        getList();
        initAdam();
    }
    public void getList(){
    		dba= new DBAdapter(this);            	
    	        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
    	        String[] projection = new String[] {           
    	                ContactsContract.CommonDataKinds.Phone.NUMBER,
    	                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
    	        };
    	        String[] selectionArgs = null;
    	        //정렬
    	        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
    	        //조회해서 가져온다
    	        Cursor contactCursor = managedQuery(uri, projection, null, selectionArgs, sortOrder);
    	        //정보를 담을 array 설정
    	        final ArrayList<TestItem> persons = new ArrayList<TestItem>();
//    	        numberSave = new ArrayList<String>();
    	        positionSave= new ArrayList<Integer>();
    	        dba.open();
    	        Cursor mCursor = dba.getAllEntries2();
    	        int indexid= mCursor.getColumnIndex("id");
    	        int indexname = mCursor.getColumnIndex("name");
    	        mCursor.moveToFirst();
    	        while(!mCursor.isAfterLast()){
    	        	positionSave.add(mCursor.getInt(indexid));
    	        	mCursor.moveToNext();
    	        }
    	        mCursor.close();
    	        dba.close();
    	        if(contactCursor.moveToFirst()){       
    	            do{
    	            	TestItem person = new TestItem(contactCursor.getString(1),contactCursor.getString(0));
    	                persons.add(person);        
//    	                numberSave.add(contactCursor.getString(0));
    	            }while(contactCursor.moveToNext());
    	        }
    	        if(positionSave.size()!=0){
    	        	for(int i=0; i<positionSave.size();i++){
    	        		persons.get(positionSave.get(i)).setCheck(true);
    	        	}
    	        }
    	        //리스트에 연결할 adapter 설정      
    	        final TestAdapter adp = new TestAdapter(persons, R.layout.adapter, NameInsert.this);
    	        //리스트뷰에 표시
    	        listPerson.setAdapter(adp);
    	        listPerson.setItemsCanFocus(false);
    	        listPerson.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    	        listPerson.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						persons.get(arg2).setCheck(!persons.get(arg2).isCheck());
						if(persons.get(arg2).isCheck()){
							String number;
							 dba.open();
							 if(persons.get(arg2).getNumber().length()>8){
									number = persons.get(arg2).getNumber().substring(3);
								}
								else{
									number = persons.get(arg2).getNumber();
								}
							dba.insertEntry2(arg2, persons.get(arg2).getTitle(),number);
						    dba.close();
						}
						else{
							 dba.open();
							dba.deleteEntry2(arg2);
						    dba.close();
						}
						// 리스트뷰 목록 화면 갱신
						adp.notifyDataSetChanged();
					}
				});
    	    
    	    }
    private void initAdam() {
		adView = (AdView) findViewById(R.id.adview_nameinsert);
		adView.setRequestInterval(5);
		adView.setClientId("38e8Z8cT13980171560");

		adView.setRequestInterval(12);
		adView.setAnimationType(AnimationType.FLIP_HORIZONTAL);

		adView.setVisibility(View.VISIBLE);
	}
    
}
