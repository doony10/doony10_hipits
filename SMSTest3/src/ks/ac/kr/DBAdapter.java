package ks.ac.kr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class DBAdapter {
	//�����ͺ��̽� ���� (�����ͺ��̽� �̸�/����, ���̺� �̸�)
		//�����ͺ��̽� ������ ���߿� �����ͺ��̽��� ������Ʈ �� �� ������Ʈ ���θ� �����ϴ� ������ �ȴ�.
		private static final String DATABASE_NAME = "date_message.db";
		private static final String DATABASE_TABLE = "on_messages";
		private static final int DATABASE_VERSION = 1;
		
		//�����ͺ��̽� �ʱ�ȭ�� �ʿ��� SQL�����
		//�Ϲ����� ���̺� ���� �� �����ϴ� ������ ����.
		private static final String DATABASE_TABLE_CREATE =
				"CREATE TABLE " + DATABASE_TABLE + "(id INTEGER PRIMARY KEY AUTOINCREMENT, "+ "number TEXT NOT NULL, " + "date LONG NOT NULL, "+"message TEXT NOT NULL);";
		
		private static final String DATABASE_TABLE_DROP = "DROP TABLE IF EXISTS "+ DATABASE_TABLE;
		
		private SQLiteDatabase mDb;
		private DatabaseHelper mDbHelper;
		private Context context;
		
		public DBAdapter(Context _context){
			context = _context;
			
			//�����ͺ��̽��� �����Ѵ�.
			mDbHelper = new DatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		//������ ���̽� ����
		public DBAdapter open() throws SQLException{
			mDb = mDbHelper.getWritableDatabase();
			return this;		
		}
		
		//������ ���̽� �ݱ�
		public void close(){
			mDb.close();
		}
		
		//���̺� ���� ������ �߰�(insert)
		public long insertEntry (String number, long date, String message){
			ContentValues values = new ContentValues();
			values.put("number", number);
			values.put("date", date);
			values.put("message", message);
			return mDb.insert(DATABASE_TABLE, null, values);
		}
		
		//���̺� ���� ������ ����(update)
		public boolean updateEntry(long rowID, String number, long date, String message){
			ContentValues values = new ContentValues();
			values.put("number", number);
			values.put("date", date);
			values.put("message", message);
			return mDb.update(DATABASE_TABLE, values, "id="+rowID, null)>0;
		}
		
		//���̺� ���� ������ ����(delete)
		public boolean deleteEntry(long rowID){
			return mDb.delete(DATABASE_TABLE, "id="+rowID, null)>0;
		}
		
		//���̺� ���� ������ ����(query)
		public Cursor getAllEntries(){
			return mDb.query(DATABASE_TABLE, new String[]{"id", "number", "date", "message"}, null, null, null, null, null,null);
		}
		
		public Cursor getEntry(long rowID) throws SQLException{
			Cursor mCursor = mDb.query(DATABASE_TABLE, new String[]{"id","number", "date", "message"}, "id="+rowID, null, null, null, null,null);
			if (mCursor != null) mCursor.moveToFirst();
			return mCursor;
			
		}
}
