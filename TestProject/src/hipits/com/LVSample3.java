package hipits.com;

import hipits.com.LVSample3Adapter.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LVSample3 extends Activity implements OnClickListener {

	private Button btnSearch;
	private Button btnAdd;
	private Button btnDelete;

	private ListView lv;
	private List<LVSample3Item> dataSources;
	private ListAdapter adapter;

	private DialogInterface.OnClickListener deleteYesListener;
	private DialogInterface.OnClickListener deleteNoListener;
	DBAdapter dba = new DBAdapter(LVSample3.this);
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.numberlist);
		
		// For Buttons
		btnSearch = (Button) findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(LVSample3.this);

		btnAdd = (Button) findViewById(R.id.btnAdd);
		btnAdd.setOnClickListener(LVSample3.this);

		btnDelete = (Button) findViewById(R.id.btnDelete);
		btnDelete.setOnClickListener(LVSample3.this);

		// For Custom ListView
		dataSources = getDataSource();
		adapter = new LVSample3Adapter(dataSources, LVSample3.this);
		LVSample3.this.lv = (ListView) findViewById(R.id.listview);
		LVSample3.this.lv.setAdapter(adapter);
		LVSample3.this.lv.setItemsCanFocus(false);
		LVSample3.this.lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		// lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		LVSample3.this.lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapt, View view, int position, long id) {
				// TODO Auto-generated method stub
				TextView tvTitle = (TextView) view.findViewById(R.id.title);
				TextView tvSummary = (TextView) view.findViewById(R.id.summary);
				String message = "Title: " + tvTitle.getText() + "\n" + "Summary:" + tvSummary.getText();
				ShowMessageBox(LVSample3.this, message);
			}
		});
	}
	
	public void onClick(View view) {
		if (view.getId() == R.id.btnSearch) {
			// long[] checkedItems = this.lv.getCheckItemIds();
			Long[] checkedItems = ((LVSample3Adapter) adapter).getCheckItemIds();
			if (checkedItems == null || checkedItems.length == 0) {
				ShowMessageBox(LVSample3.this, "Selected Items is Nothing.");
				return;
			}

			String message = "";
			for (int index = 0; index < checkedItems.length; index++) {
				long pos = checkedItems[index];
				LVSample3Item item = dataSources.get((int) pos);
				message += String.format("%d[%s, %s]\n", pos, item.getTitle(), item.getNumber());
			}
			ShowMessageBox(LVSample3.this, message);
		} 
			else if (view.getId() == R.id.btnAdd) {
			/*aataSource(this.dataSources);
			((LVSample3Adapter) adapter).notifyDataSetChanged();
			ShowMessageBox(this, String.format("All items count is %d.", dataSources.size()));*/
			
			dba.open();
			Long[] checkedItems = ((LVSample3Adapter) adapter).getCheckItemIds();
			if (checkedItems == null || checkedItems.length == 0) {
				ShowMessageBox(LVSample3.this, "Selected Items is Nothing.");
				return;
			}
				String message = "";
				for (int index = 0; index < checkedItems.length; index++) {
					long pos = checkedItems[index];
					LVSample3Item item = dataSources.get((int) pos);
					//dba.insertEntry2(item.getTitle(), item.getNumber());
					Log.v("text", "title = "+item.getTitle()+ "number = "+item.getNumber());
					dba.insertEntry2(item.getTitle(), item.getNumber());
					message += String.format("%d[%s, %s] 추가\n", pos, item.getTitle(), item.getNumber());
				}
				ShowMessageBox(LVSample3.this, message);
				dba.close();
		} else if (view.getId() == R.id.btnDelete) {
			int id = 0;
			String name = null;
			Long[] checkedItems = ((LVSample3Adapter) adapter).getCheckItemIds();
			dba = new DBAdapter(LVSample3.this);
			dba.open();
			Cursor dbcurosr=dba.getAllEntries2();
			int indexid = dbcurosr.getColumnIndex("id");
			int indexname = dbcurosr.getColumnIndex("name");
			Log.v("dbin", "id="+indexid+" name="+indexname);
			dbcurosr.moveToFirst();
			
			while (!dbcurosr.isAfterLast()){
				id = dbcurosr.getInt(indexid);			
				name = dbcurosr.getString(indexname);
				Log.v("db", id+name);
				//dba.deleteEntry(id);
				if (checkedItems == null || checkedItems.length == 0) {
					ShowMessageBox(LVSample3.this, "Selected Items is Nothing.");
					return;
				}
				String message = "";
				for (int index = 0; index < checkedItems.length; index++) {
					long pos = checkedItems[index];
					LVSample3Item item = dataSources.get((int) pos);
					Log.v("db2", name);
					Log.v("item", item.getTitle());
					if (item.getTitle().equals(name)){
						dba.deleteEntry2(id);
						Log.v("dbtest", id+name);
					}					
				}
				dbcurosr.moveToNext();
				}
			dbcurosr.close();
			dba.close();
			//long[] checkedItems = this.lv.getCheckItemIds();
		}
	}

	private List<LVSample3Item> getDataSource() {
		List<LVSample3Item> lstItems = new ArrayList<LVSample3Item>();

	    String[] nameProjection = new String[] { 
	            ContactsContract.Contacts._ID,
	            ContactsContract.Contacts.DISPLAY_NAME,
	            ContactsContract.Contacts.HAS_PHONE_NUMBER 
	            };
		Cursor c = LVSample3.this.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, nameProjection, null, null, Contacts.DISPLAY_NAME + " ASC"); 
        c.moveToFirst();
        int contactId = c.getColumnIndex(ContactsContract.Contacts._ID);
        int nameCol = c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);         
        int pbNumCol = c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
                 
        do{             
        	String number = "";
        	String name = "";
            String strContactId = c.getString( contactId );    
            String hasPhone = c.getString( pbNumCol );
            //Log.d("revoltant","strContactId = " + strContactId + "hasPhone " + hasPhone);
           
           if (hasPhone.equals("1")) {
               name = c.getString(nameCol);
               Cursor clsPhoneCursor = getContentResolver().query(
                       ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                       /*numProjection*/null,
                       ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                               + " = " + strContactId, null, null);                
               int numCol = clsPhoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
               
               while (clsPhoneCursor.moveToNext()) {
                   //전화번호 저장  
                   number = clsPhoneCursor.getString(numCol);
               } 
               clsPhoneCursor.close();
				LVSample3Item item = new LVSample3Item(name, number);
				lstItems.add(item);
           }
        } while(c.moveToNext());
        
        c.close();
		return lstItems;
	}

	private static List<LVSample3Item> addDataSource(List<LVSample3Item> dataSources) {
		LVSample3Item item = new LVSample3Item("Added" + dataSources.size(), "This is a added mssage.");
		dataSources.add(item);

		return dataSources;
	}

	private static void ShowMessageBox(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	public static void ShowMessageYesNo(Context context, String title, String message,
			DialogInterface.OnClickListener YesListener, DialogInterface.OnClickListener NoListener) {
		AlertDialog.Builder alterBuilder = new AlertDialog.Builder(context);
		alterBuilder.setMessage(message).setCancelable(false).setPositiveButton(android.R.string.yes, YesListener)
				.setNegativeButton(android.R.string.no, NoListener);
		AlertDialog alert = alterBuilder.create();
		// Title for AlertDialog
		alert.setTitle(title);
		// Icon for AlertDialog
		alert.setIcon(R.drawable.ic_launcher);
		alert.show();
	}
}
