package hipits.com;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
	//�����ͺ��̽� ���� (�����ͺ��̽� �̸�/����, ���̺� �̸�)
	//�����ͺ��̽� ������ ���߿� �����ͺ��̽��� ������Ʈ �� �� ������Ʈ ���θ� �����ϴ� ������ �ȴ�.
	private static final String DATABASE_NAME = "numbermanager.db";
	private static final String DATABASE_TABLE = "manager";
	private static final int DATABASE_VERSION = 1;
	
	//�����ͺ��̽� �ʱ�ȭ�� �ʿ��� SQL�����
	//�Ϲ����� ���̺� ���� �� �����ϴ� ������ ����.
	private static final String DATABASE_TABLE_CREATE =
			"CREATE TABLE " + DATABASE_TABLE + "(id INTEGER PRIMARY KEY AUTOINCREMENT, "+ "number TEXT NOT NULL, " + "time LONG NOT NULL,"+"message TEXT NOT NULL);";
	
	private static final String DATABASE_TABLE_DROP = "DROP TABLE IF EXISTS "+ DATABASE_TABLE;
	
	private SQLiteDatabase mDb;
	private DatabaseHelper mDbHelper;
	private Context context;
	
	public DBAdapter(Context _context){
		context = _context;
		
		//�����ͺ��̽��� �����Ѵ�.
		mDbHelper = new DatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	//���� Ŭ����:���̺� ���� �� ���׷��̵�
	class DatabaseHelper extends SQLiteOpenHelper{

		public DatabaseHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		//���̺� ����
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_TABLE_CREATE);
		}

		//���̺� ���׷��̵�
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w("MY_TAG", "Upgrading DB from version"+ oldVersion + "to"+newVersion + ", which will destroy all old data");
			
			db.execSQL(DATABASE_TABLE_DROP);
			onCreate(db);
		}		
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
	public long insertEntry (String name, long time, String message){
		ContentValues values = new ContentValues();
		values.put("number", name);
		values.put("time", time);
		values.put("message", message);
		return mDb.insert(DATABASE_TABLE, null, values);
	}
	
	//���̺� ���� ������ ����(update)
	public boolean updateEntry(long rowID, String name, long time, String message){
		ContentValues values = new ContentValues();
		values.put("number", name);
		values.put("time", time);
		values.put("message", message);
		return mDb.update(DATABASE_TABLE, values, "id=", null)>0;
	}
	
	//���̺� ���� ������ ����(delete)
	public boolean deleteEntry(long rowID){
		return mDb.delete(DATABASE_TABLE, "id="+rowID, null)>0;
	}
	
	//���̺� ���� ������ ����(query)
	public Cursor getAllEntries(){
		return mDb.query(DATABASE_TABLE, new String[]{"id", "number", "time", "message"}, null, null, null, null, null,null);
	}
	
	public Cursor getEntry(long rowID) throws SQLException{
		Cursor mCursor = mDb.query(DATABASE_TABLE, new String[]{"id","number", "time", "message"}, "id="+rowID, null, null, null, null,null);
		if (mCursor != null) mCursor.moveToFirst();
		return mCursor;
		
	}
}

