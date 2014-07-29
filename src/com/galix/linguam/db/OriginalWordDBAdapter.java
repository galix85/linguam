package com.galix.linguam.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.galix.linguam.pojo.OriginalWord;
import com.galix.linguam.pojo.Term;

public class OriginalWordDBAdapter {

	private static final String TAG = "Linguam: Original Adapter";
	
	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.COLUMN_ORIGINALWORD_ID,
			MySQLiteHelper.COLUMN_ORIGINALWORD_POS,
			MySQLiteHelper.COLUMN_ORIGINALWORD_SENSE,
			MySQLiteHelper.COLUMN_ORIGINALWORD_TERM,
			MySQLiteHelper.COLUMN_ORIGINALWORD_USAGE };

	public OriginalWordDBAdapter(Context context) {
	
		dbHelper = new MySQLiteHelper(context);
	    database = dbHelper.getWritableDatabase(); 
	
	}

	public OriginalWord createOriginalWord(Term originalWord,int refLang) {

		ContentValues values = new ContentValues();

		values.put(MySQLiteHelper.COLUMN_ORIGINALWORD_POS,
				originalWord.getPOS() == null ? "Empty Pos" : originalWord.getPOS());
		values.put(MySQLiteHelper.COLUMN_ORIGINALWORD_SENSE,
				originalWord.getSense() == null ? "Empty Sense" : originalWord.getSense());
		values.put(MySQLiteHelper.COLUMN_ORIGINALWORD_TERM,
				originalWord.getTerm());
		values.put(MySQLiteHelper.COLUMN_ORIGINALWORD_USAGE,
				originalWord.getUsage() == null ? "Empty Usage" : originalWord.getUsage());
		values.put(MySQLiteHelper.COLUMN_STATISTICS_REF_LANG,refLang);
		//
		if (!exist(originalWord.getTerm(),originalWord.getPOS(),originalWord.getSense())){
			
			long insertId = database.insert(MySQLiteHelper.TABLE_ORIGINALWORD,
					null, values);
			Cursor cursor = database.query(MySQLiteHelper.TABLE_ORIGINALWORD,
					allColumns, MySQLiteHelper.COLUMN_ORIGINALWORD_ID + " = "
							+ insertId, null, null, null, null);
			
			cursor.moveToFirst();
			OriginalWord newOriginalWord = cursorToOriginalWord(cursor);
			cursor.close();
			Log.v(TAG, "New word saved (" + originalWord.getTerm() +")");
			return newOriginalWord;
		
		}else{
			
			return null;
		
		}
	}

	public void deleteOriginalWord(OriginalWord originalWord) {
		
		long id = originalWord.getId();
		System.out.println("Comment deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_ORIGINALWORD,
				MySQLiteHelper.COLUMN_ORIGINALWORD_ID + " = " + id, null);
	
	}

	//TODO: Add param del lang
	public List<OriginalWord> getAllOriginalWords(int refLang) {
		
		List<OriginalWord> originalWords = new ArrayList<OriginalWord>();
    	
		String selection = MySQLiteHelper.COLUMN_ORIGINALWORD_REF_LANG+"=?";
    	String[] selectionArgs = {String.valueOf(refLang)};
				
		Cursor cursor = database.query(MySQLiteHelper.TABLE_ORIGINALWORD,
				allColumns, selection, selectionArgs, null, null, null);

		cursor.moveToFirst();
		
		while (!cursor.isAfterLast()) {
			OriginalWord originalWord = cursorToOriginalWord(cursor);
			originalWords.add(originalWord);
			cursor.moveToNext();
		}
		
		// make sure to close the cursor
		cursor.close();
		return originalWords;
	}

	private OriginalWord cursorToOriginalWord(Cursor cursor) {
		
		OriginalWord originalWord = new OriginalWord();
		originalWord.setId((int) cursor.getLong(0));
		originalWord.setTerm(cursor.getString(3));
		return originalWord;
		
	}
	
	public boolean exist(String term, String pos, String sense) {
		    
			boolean result = false;
			
	    	String selection = "term=? AND pos=? AND sense=?";
	    	String[] selectionArgs = {term,pos,sense};
	    	String tableName = MySQLiteHelper.TABLE_ORIGINALWORD;
	    	Cursor c = database.query(tableName, null, selection, selectionArgs, null, null, null);
	        if (c != null && c.getCount() != 0) {
	        	Log.v(TAG, "Exist");
	            result = true;
	        }else{
	        	Log.v(TAG, "Not Exist");
		        result = false;
		        
	        }
	        c.close();
	        return result;

	}
		
	//TODO: Add param del lang
	public List<OriginalWord> getWordByLevel(int level,int limit,int refLang) {
	    
		List<OriginalWord> originalWords = new ArrayList<OriginalWord>();

		/*String selection = MySQLiteHelper.COLUMN_ORIGINALWORD_REF_LANG+"=?";
    	String[] selectionArgs = {String.valueOf(refLang)};*/
		
		Cursor cursor = database.query(MySQLiteHelper.TABLE_ORIGINALWORD,
				allColumns, null, null, null, null, String.valueOf(limit));

		cursor.moveToFirst();
		
		while (!cursor.isAfterLast()) {
			OriginalWord originalWord = cursorToOriginalWord(cursor);
			originalWords.add(originalWord);
			cursor.moveToNext();
		}
		
		// make sure to close the cursor
		cursor.close();
		return originalWords;

}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

}