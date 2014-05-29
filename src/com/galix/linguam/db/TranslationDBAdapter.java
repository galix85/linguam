package com.galix.linguam.db;

import java.util.ArrayList;
import java.util.List;

import com.galix.linguam.pojo.PairWord;
import com.galix.linguam.pojo.Term;
import com.galix.linguam.pojo.TranslatedWord;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class TranslationDBAdapter {

	private static final String TAG = "Linguam: Translation Adapter";
	
	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.COLUMN_TRANSLATION_ID,
			MySQLiteHelper.COLUMN_TRANSLATION_POS,
			MySQLiteHelper.COLUMN_TRANSLATION_SENSE,
			MySQLiteHelper.COLUMN_TRANSLATION_TERM,
			MySQLiteHelper.COLUMN_TRANSLATION_USAGE,
			MySQLiteHelper.COLUMN_TRANSLATION_ORIGINALWORD,
			MySQLiteHelper.COLUMN_TRANSLATION_SELECTED,
			MySQLiteHelper.COLUMN_TRANSLATION_LEVEL};

	public TranslationDBAdapter(Context context) {
		dbHelper = new MySQLiteHelper(context);
		database = dbHelper.getWritableDatabase(); 
	}

	public TranslatedWord createTranslation(Term translation,boolean selected,String originalword) {

		ContentValues values = new ContentValues();
		
		values.put(MySQLiteHelper.COLUMN_TRANSLATION_POS, translation.getPOS() == null ? "Empty Pos" : translation.getPOS());
		values.put(MySQLiteHelper.COLUMN_TRANSLATION_SENSE,
				translation.getSense() == null ? "Empty Sense" : translation.getSense());
		values.put(MySQLiteHelper.COLUMN_TRANSLATION_TERM,
				translation.getTerm());
		values.put(MySQLiteHelper.COLUMN_TRANSLATION_USAGE,
				translation.getUsage() == null ? "Empty Usage" : translation.getUsage());
		values.put(MySQLiteHelper.COLUMN_TRANSLATION_ORIGINALWORD,
				originalword);
		values.put(MySQLiteHelper.COLUMN_TRANSLATION_SELECTED,
			    selected ? 1 : 0);
		values.put(MySQLiteHelper.COLUMN_TRANSLATION_LEVEL,1);

		if (!exist(translation.getTerm(),translation.getPOS(),translation.getSense())){
			
			long insertId = database.insert(MySQLiteHelper.TABLE_TRANSLATION, null,
					values);
			Cursor cursor = database.query(MySQLiteHelper.TABLE_TRANSLATION,
					allColumns, MySQLiteHelper.COLUMN_TRANSLATION_ID + " = "
							+ insertId, null, null, null, null);
			cursor.moveToFirst();
			TranslatedWord newTranslation = cursorToTranslate(cursor);
			cursor.close();
			Log.v(TAG, "New word saved (" + translation.getTerm() +")");
			
			return newTranslation;
			
		}else{
			
			return null;
		
		}
	}

	public void deleteTranslation(TranslatedWord translation) {
		long id = translation.getId();
		database.delete(MySQLiteHelper.TABLE_TRANSLATION,
				MySQLiteHelper.COLUMN_TRANSLATION_ID + " = " + id, null);
	}

	public List<TranslatedWord> getAllTranslates() {
		
		List<TranslatedWord> translationWords = new ArrayList<TranslatedWord>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_TRANSLATION,
				allColumns, null, null, null, null,null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			TranslatedWord translation = cursorToTranslate(cursor);
			translationWords.add(translation);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return translationWords;
	}


	public boolean exist(String term, String pos, String sense) {
	    
		boolean result = false;
		
    	String selection = "term=? AND pos=? AND sense=?";
    	String[] selectionArgs = {term,pos,sense};
    	String tableName = MySQLiteHelper.TABLE_TRANSLATION;
    	Cursor c = database.query(tableName, null, selection, selectionArgs, null, null, null);
        if (c != null && c.getCount() != 0) {
            result = true;
        }else{
	        result = false;
        }
        c.close();
        return result;

	}
	
	private TranslatedWord cursorToTranslate(Cursor cursor) {
		TranslatedWord translation = new TranslatedWord();
		translation.setId((int) cursor.getLong(0));
		translation.setTerm(cursor.getString(1));
		return translation;
	}
	
	public TranslatedWord getTranslateByWord(String originalword){
		
		List<TranslatedWord> translationWords = new ArrayList<TranslatedWord>();
		TranslatedWord translation = new TranslatedWord();
		
		String selection = "originalword=? AND selected=?";
    	String[] selectionArgs = {originalword,"1"};
    	String tableName = MySQLiteHelper.TABLE_TRANSLATION;
    	
    	Cursor cursor = database.query(tableName, null, selection, selectionArgs, null, null, null);
    	if (cursor != null && cursor.getCount() > 0){
    		cursor.moveToFirst();
	    	while (!cursor.isAfterLast()) {
				translation = cursorToTranslate(cursor);
				translationWords.add(translation);
				cursor.moveToNext();
			}
	    	
	    	// make sure to close the cursor
			cursor.close();

			return translationWords.get(0);
    	}else{
    		cursor.close();
          	return null;
    	}
		
		
		
	}
	
	
	public ArrayList<String> getPaddingWordList(String originalWord, int nWords) {

		ArrayList<String> paddingWordList = new ArrayList<String>();
		
		String selection = "originalword!=? AND selected=?";
    	String[] selectionArgs = {originalWord,"0"};
    	String tableName = MySQLiteHelper.TABLE_TRANSLATION;
    	
    	Cursor cursor = database.query(tableName, null, selection, selectionArgs, null, null, "RANDOM() limit "+nWords );
    	if (cursor.moveToFirst()){
    		cursor.moveToFirst();
    		int count = 0;
    		do {
    			if (!paddingWordList.contains(cursor.getString(1) ))
    				paddingWordList.add(cursor.getString(1));
    				count ++;    			
    		} while(cursor.moveToNext() && count < nWords);
			
			return paddingWordList;
			
    	}else{
    		return null;
    	}
    	
	}
	
	public ArrayList<PairWord> getPairWord() {

		ArrayList<PairWord> translationWords = new ArrayList<PairWord>();

		
		String selection = "selected=? AND level<?";
    	String[] selectionArgs = {"1","6"};
    	String tableName = MySQLiteHelper.TABLE_TRANSLATION;
    	
    	Cursor cursor = database.query(tableName, null, selection, selectionArgs, null, null, null);
    	if (cursor.moveToFirst()){
    		  do {
    			  PairWord pair = new PairWord();
    			  pair.setOriginalWord(cursor.getString(5));
    			  pair.setTranslateWord(cursor.getString(1));
    			  pair.setLevel(cursor.getInt(7));
    	          translationWords.add(pair);
    	     } while(cursor.moveToNext());
			
			return translationWords;
    	}else{
    		return null;
    	}
    	
	}

	public void updateLevel(String word, int level){
		
		ContentValues cv = new ContentValues();
		cv.put(MySQLiteHelper.COLUMN_TRANSLATION_LEVEL,level);
		database.update(MySQLiteHelper.TABLE_TRANSLATION,cv, MySQLiteHelper.COLUMN_TRANSLATION_TERM +" like '"+ word +"' AND " + 
		MySQLiteHelper.COLUMN_TRANSLATION_SELECTED +" = "+ 1 ,null);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}
}