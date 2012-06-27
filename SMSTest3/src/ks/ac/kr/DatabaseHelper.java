package ks.ac.kr;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

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
