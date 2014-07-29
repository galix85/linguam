package com.galix.linguam.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class StatisticDBAdapter {

	private static final String TAG = "Linguam: Statistics Adapter";
	
	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	//private String[] allColumns = { MySQLiteHelper.COLUMN_STATISTICS_SCORE};

	public StatisticDBAdapter(Context context) {
		dbHelper = new MySQLiteHelper(context);
		database = dbHelper.getWritableDatabase(); 
	}

	public void updateScore (int score,int refLang) {
		//database.execSQL("INSERT OR REPLACE INTO " + MySQLiteHelper.TABLE_STATISTICS + "  ("+ MySQLiteHelper.COLUMN_STATISTICS_SCORE +") VALUES ("+score+")");
	
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_STATISTICS_SCORE, score);
		values.put(MySQLiteHelper.COLUMN_STATISTICS_REF_LANG, refLang);
		
		if (!exist()){
			database.insertWithOnConflict(MySQLiteHelper.TABLE_STATISTICS, null, values, SQLiteDatabase.CONFLICT_IGNORE);
			Log.v(TAG, "Score insterted to: " + score);
		}else{
			database.update(MySQLiteHelper.TABLE_STATISTICS, values, MySQLiteHelper.COLUMN_STATISTICS_REF_LANG +" = "+ refLang, null);
			Log.v(TAG, "Score updated to: " + score);
		}
		
		
	}

	public int getScore(int refLang){
		
		int score = 0;
    	String tableName = MySQLiteHelper.TABLE_STATISTICS;
    	
    	String selection = MySQLiteHelper.COLUMN_STATISTICS_REF_LANG+"=?";
    	String[] selectionArgs = {String.valueOf(refLang)};
    	
    	Cursor cursor = database.query(tableName, null, selection, selectionArgs, null, null, null);
    	if (cursor != null && cursor.getCount() > 0){
    		cursor.moveToFirst();
	    	score = cursor.getInt(0);
			Log.v(TAG, "Getting current score: " + score);
	    	// make sure to close the cursor
			cursor.close();
			return score;
    	}else{
    		cursor.close();
          	return score;
    	}
	
	}
	
	public boolean exist() {
	    
		boolean result = false;
		
    	String tableName = MySQLiteHelper.TABLE_STATISTICS;
    	
    	Cursor c = database.query(tableName, null, null, null, null, null, null);
        if (c != null && c.getCount() != 0) {
            result = true;
        }else{
	        result = false;
        }
        c.close();
        return result;

	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}
}