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
	//데이터베이스 정보 (데이터베이스 이름/버젼, 테이블 이름)
		//데이터베이스 버전은 나중에 데이터베이스를 업데이트 할 때 업데이트 여부를 결장하는 기준이 된다.
		private static final String DATABASE_NAME = "date_message.db";
		private static final String DATABASE_TABLE = "on_messages";
		private static final int DATABASE_VERSION = 1;
		
		//데이터베이스 초기화에 필요한 SQL문장들
		//일반적을 테이블 생성 및 삭제하는 문장이 들어간다.
		private static final String DATABASE_TABLE_CREATE =
				"CREATE TABLE " + DATABASE_TABLE + "(id INTEGER PRIMARY KEY AUTOINCREMENT, "+ "number TEXT NOT NULL, " + "date LONG NOT NULL, "+"message TEXT NOT NULL);";
		
		private static final String DATABASE_TABLE_DROP = "DROP TABLE IF EXISTS "+ DATABASE_TABLE;
		
		private SQLiteDatabase mDb;
		private DatabaseHelper mDbHelper;
		private Context context;
		
		public DBAdapter(Context _context){
			context = _context;
			
			//데이터베이스를 생성한다.
			mDbHelper = new DatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		//데이터 베이스 열기
		public DBAdapter open() throws SQLException{
			mDb = mDbHelper.getWritableDatabase();
			return this;		
		}
		
		//데이터 베이스 닫기
		public void close(){
			mDb.close();
		}
		
		//테이블 내의 데이터 추가(insert)
		public long insertEntry (String number, long date, String message){
			ContentValues values = new ContentValues();
			values.put("number", number);
			values.put("date", date);
			values.put("message", message);
			return mDb.insert(DATABASE_TABLE, null, values);
		}
		
		//테이블 내의 데이터 수정(update)
		public boolean updateEntry(long rowID, String number, long date, String message){
			ContentValues values = new ContentValues();
			values.put("number", number);
			values.put("date", date);
			values.put("message", message);
			return mDb.update(DATABASE_TABLE, values, "id="+rowID, null)>0;
		}
		
		//테이블 내의 데이터 삭제(delete)
		public boolean deleteEntry(long rowID){
			return mDb.delete(DATABASE_TABLE, "id="+rowID, null)>0;
		}
		
		//테이블 내의 데이터 질의(query)
		public Cursor getAllEntries(){
			return mDb.query(DATABASE_TABLE, new String[]{"id", "number", "date", "message"}, null, null, null, null, null,null);
		}
		
		public Cursor getEntry(long rowID) throws SQLException{
			Cursor mCursor = mDb.query(DATABASE_TABLE, new String[]{"id","number", "date", "message"}, "id="+rowID, null, null, null, null,null);
			if (mCursor != null) mCursor.moveToFirst();
			return mCursor;
			
		}
}
