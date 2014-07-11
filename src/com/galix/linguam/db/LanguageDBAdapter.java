package com.galix.linguam.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.galix.linguam.pojo.Language;


public class LanguageDBAdapter {

private static final String TAG = "Linguam: LanguageDBAdapter";
	
	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.COLUMN_LANGUAGE_ID,
			MySQLiteHelper.COLUMN_LANGUAGE_FROM,
			MySQLiteHelper.COLUMN_LANGUAGE_TO,
			MySQLiteHelper.COLUMN_LANGUAGE_TITLE,
			MySQLiteHelper.COLUMN_LANGUAGE_SUBTITLE,
			MySQLiteHelper.COLUMN_LANGUAGE_SELECTED,
			MySQLiteHelper.COLUMN_LANGUAGE_ACTIVE,
			MySQLiteHelper.COLUMN_LANGUAGE_IMG_SRC };

	public LanguageDBAdapter(Context context) {
		dbHelper = new MySQLiteHelper(context);
	    database = dbHelper.getWritableDatabase(); 
	}

	public void deleteLanguage(Language language) {
		/*
		long id = language.get();
		System.out.println("Comment deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_ORIGINALWORD,
				MySQLiteHelper.COLUMN_ORIGINALWORD_ID + " = " + id, null);*/
	
	}

	public List<Language> getActiveLanguage() {
		
		List<Language> languageList = new ArrayList<Language>();

		String selection = "isActive=?";
    	String[] selectionArgs = {"1"};
		
		Cursor cursor = database.query(MySQLiteHelper.TABLE_LANGUAGE,
				allColumns, selection,selectionArgs, null, null,null);

		cursor.moveToFirst();
		
		while (!cursor.isAfterLast()) {
			Language language = cursorToLanguage(cursor);
			languageList.add(language);
			cursor.moveToNext();
		}
		
		// make sure to close the cursor
		cursor.close();
		return languageList;
	}
	
	public List<Language> getAllLanguage() {
		
		List<Language> languageList = new ArrayList<Language>();
		Cursor cursor = database.query(MySQLiteHelper.TABLE_LANGUAGE,
				allColumns, null,null, null, null,null);

		cursor.moveToFirst();
		
		while (!cursor.isAfterLast()) {
			Language language = cursorToLanguage(cursor);
			languageList.add(language);
			cursor.moveToNext();
		}
		
		// make sure to close the cursor
		cursor.close();
		return languageList;
	}
	
	public void setActiveLanguage(String id) {
		
		String selection = "_id=?";
    	String[] selectionArgs = {id};
    	
    	ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_LANGUAGE_ACTIVE, "1");
		
		database.update(MySQLiteHelper.TABLE_LANGUAGE, values, selection, selectionArgs);
	}
	
	public void setSelectedLanguage(String id) {
		
		ContentValues values = new ContentValues();
		
    	String selection_noSelected = "_id<>?";
    	String[] selectionArgs_noSelected = {id};
    	
    	//Set to false old selected language
    	values.put(MySQLiteHelper.COLUMN_LANGUAGE_SELECTED, "0");
		database.update(MySQLiteHelper.TABLE_LANGUAGE, values, selection_noSelected, selectionArgs_noSelected);	
		
		// Set new selected language
		String selection = "_id=?";
    	String[] selectionArgs = {id};

		values.put(MySQLiteHelper.COLUMN_LANGUAGE_SELECTED, "1");
		database.update(MySQLiteHelper.TABLE_LANGUAGE, values, selection, selectionArgs);
	}

	public Language getSelectedLanguage() {
		
		Language language = new Language();
    	
    	String selection = "isSelected=?";
    	String[] selectionArgs = {"1"};
    	
    	Cursor cursor = database.query(MySQLiteHelper.TABLE_LANGUAGE, null, selection, selectionArgs, null, null, null);
    	if (cursor != null && cursor.getCount() > 0){
    		cursor.moveToFirst();
    		language.setId(cursor.getString(1));
	    	language.setLangFrom(cursor.getString(1));
	    	language.setLangTo(cursor.getString(2));
	    	language.setTitle(cursor.getString(4));
	    	language.setSubtitle(cursor.getString(5));
			//Log.v(TAG, "Getting current score: " + score);
	    	// make sure to close the cursor
			cursor.close();
			return language;
    	}else{
    		cursor.close();
          	return null;
    	}
	}
	
	private Language cursorToLanguage(Cursor cursor) {
		
		Language language = new Language();
		language.setId(cursor.getString(0)); //Id
		language.setLangFrom(cursor.getString(1)); //From
		language.setLangTo(cursor.getString(2)); //To
		language.setTitle(cursor.getString(3)); //Title
		language.setSubtitle(cursor.getString(4)); //Subtitle
		language.setIsActive(cursor.getString(5)); //Active
		language.setImgSrc(cursor.getString(6)); //Img
		return language;
		
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

}