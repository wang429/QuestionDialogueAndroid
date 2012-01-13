package jcw.question.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class Database {
	private SQLiteDatabase db;
	private final Context context;
	private final DBHelper dbhelper;

	public Database(Context c) {
		context = c;
		dbhelper = new DBHelper(context, Constants.DATABASE_NAME, null,
				Constants.DATABASE_VERSION);
	}

	public void close() {
		db.close();
	}
	
	public void open() throws SQLiteException{
		try{
			db = dbhelper.getWritableDatabase();
		}
		catch(SQLiteException ex){
			Log.v("Open database exception caught", ex.getMessage());
			db = dbhelper.getReadableDatabase();
		}
	}
	
	public long insertQuestion(String[] questions){
		try{
			ContentValues newTaskValue = new ContentValues();
			newTaskValue.put(Constants.QUESTIONTYPE_ID, Integer.parseInt(questions[0]));
			newTaskValue.put(Constants.QUESTION, questions[1]);
			newTaskValue.put(Constants.ANSWER, questions[2]);
			newTaskValue.put(Constants.DISTRACTOR1, questions[3]);
			newTaskValue.put(Constants.DISTRACTOR2, questions[4]);
			return db.insert(Constants.TABLE_NAME, null, newTaskValue);
		} catch(SQLiteException ex){
			Log.v("Insert into database exception caught", ex.getMessage());
			return -1;
		}
	}
	
	public Cursor getQuestions(){
		Cursor c = db.query(Constants.TABLE_NAME, null, null, null, null, null, null);
		return c;
	}
}
