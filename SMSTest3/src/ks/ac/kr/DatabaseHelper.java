package ks.ac.kr;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

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
	public DatabaseHelper(Context context, String name, CursorFactory factory,int version) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w("MY_TAG", "Upgrading DB from version"+ oldVersion + "to"+newVersion + ", which will destroy all old data");
		
		db.execSQL(DATABASE_TABLE_DROP);
		onCreate(db);
	}
}
