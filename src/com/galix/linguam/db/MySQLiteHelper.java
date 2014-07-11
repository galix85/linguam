package com.galix.linguam.db;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.galix.linguam.LinguamApplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	public static final String CSV_SOURCE = "LanguageMapping.csv";
	
	public static final String TABLE_TRANSLATION = "translation";
	public static final String COLUMN_TRANSLATION_ID = "_id";
	public static final String COLUMN_TRANSLATION_TERM = "term";
	public static final String COLUMN_TRANSLATION_POS = "pos";
	public static final String COLUMN_TRANSLATION_SENSE = "sense";
	public static final String COLUMN_TRANSLATION_USAGE = "usage";
	public static final String COLUMN_TRANSLATION_ORIGINALWORD = "originalword";
	public static final String COLUMN_TRANSLATION_SELECTED = "selected";
	public static final String COLUMN_TRANSLATION_LEVEL = "level";

	public static final String TABLE_ORIGINALWORD = "originalword";
	public static final String COLUMN_ORIGINALWORD_ID = "_id";
	public static final String COLUMN_ORIGINALWORD_TERM = "term";
	public static final String COLUMN_ORIGINALWORD_POS = "pos";
	public static final String COLUMN_ORIGINALWORD_SENSE = "sense";
	public static final String COLUMN_ORIGINALWORD_USAGE = "usage";
	
	public static final String TABLE_STATISTICS = "statitstics";
	public static final String COLUMN_STATISTICS_SCORE = "score";

	public static final String TABLE_LANGUAGE = "language";
	public static final String COLUMN_LANGUAGE_ID = "_id";
	public static final String COLUMN_LANGUAGE_FROM = "langFrom";
	public static final String COLUMN_LANGUAGE_TO = "langTo";
	public static final String COLUMN_LANGUAGE_TITLE = "title";
	public static final String COLUMN_LANGUAGE_SUBTITLE = "subtitle";
	public static final String COLUMN_LANGUAGE_ACTIVE = "isActive";
	public static final String COLUMN_LANGUAGE_SELECTED = "isSelected";
	public static final String COLUMN_LANGUAGE_IMG_SRC = "imgSrc";
	
	private static final String DATABASE_NAME = "linguam.db";
	private static final int DATABASE_VERSION = 13;

	// Database creation sql statement
	private static final String CREATE_TABLE_TRANSLATION = "create table "
			+ TABLE_TRANSLATION + "(" + COLUMN_TRANSLATION_ID
			+ " integer primary key autoincrement, " + COLUMN_TRANSLATION_TERM
			+ " text not null" + ", " + COLUMN_TRANSLATION_POS + " text" + ", "
			+ COLUMN_TRANSLATION_SENSE + " text" + ", "
			+ COLUMN_TRANSLATION_USAGE + " text" + ", "
			+ COLUMN_TRANSLATION_ORIGINALWORD + " text" + ", "
			+ COLUMN_TRANSLATION_SELECTED + " integer" + ", "
			+ COLUMN_TRANSLATION_LEVEL + " integer);";

	// Database creation sql statement
	private static final String CREATE_TABLE_ORIGINALWORD = "create table "
			+ TABLE_ORIGINALWORD + "(" + COLUMN_ORIGINALWORD_ID
			+ " integer primary key autoincrement, " + COLUMN_ORIGINALWORD_TERM
			+ " text not null" + ", " + COLUMN_ORIGINALWORD_POS + " text"
			+ ", " + COLUMN_ORIGINALWORD_SENSE + " text" + ", "
			+ COLUMN_ORIGINALWORD_USAGE + " text);";
	
	// Database creation sql statement
	private static final String CREATE_TABLE_STATISTICS = "create table "
				+ TABLE_STATISTICS + "(" + COLUMN_STATISTICS_SCORE
				+ " integer);";
	
	// Database creation sql statement
	private static final String CREATE_TABLE_LANGUAGE = "create table "
			+ TABLE_LANGUAGE + "(" + COLUMN_LANGUAGE_ID
			+ " integer primary key autoincrement, " 
			+ COLUMN_LANGUAGE_FROM + " text" + ", "
			+ COLUMN_LANGUAGE_TO + " text" + ", "
			+ COLUMN_LANGUAGE_TITLE + " text" + ", "
			+ COLUMN_LANGUAGE_SUBTITLE + " text" + ", "
			+ COLUMN_LANGUAGE_IMG_SRC + " text" + ", "
			+ COLUMN_LANGUAGE_SELECTED + " text" + ", "
			+ COLUMN_LANGUAGE_ACTIVE + " integer);";	
		
	/**
	 * Constructor should be private to prevent direct instantiation. make call
	 * to static factory method "getInstance()" instead.
	 */
	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(CREATE_TABLE_TRANSLATION);
		database.execSQL(CREATE_TABLE_ORIGINALWORD);
		database.execSQL(CREATE_TABLE_STATISTICS);
		database.execSQL(CREATE_TABLE_LANGUAGE);
		initLanguageTable(database);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORIGINALWORD);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSLATION);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATISTICS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LANGUAGE);
		onCreate(db);
	}
	
	public void initLanguageTable(SQLiteDatabase db) {
        
		try {
				
				AssetManager assetManager = LinguamApplication.getContext().getAssets();
				InputStream csvStream = assetManager.open(MySQLiteHelper.CSV_SOURCE);
				BufferedReader br = new BufferedReader(new InputStreamReader(csvStream, "UTF-8"));
				
			    String reader = "";
			    int count = 1;
			    while ((reader = br.readLine()) != null){
			        
			    	String[] rowData = reader.split(",");
			        
			        ContentValues values = new ContentValues();
			        values.put(MySQLiteHelper.COLUMN_LANGUAGE_ID, count);
			        values.put(MySQLiteHelper.COLUMN_LANGUAGE_FROM, rowData[0]);//from
			        values.put(MySQLiteHelper.COLUMN_LANGUAGE_TO, rowData[1]);//to
			        values.put(MySQLiteHelper.COLUMN_LANGUAGE_TITLE, rowData[2]);//title
			        values.put(MySQLiteHelper.COLUMN_LANGUAGE_SUBTITLE, rowData[3]);//subtitle
			        values.put(MySQLiteHelper.COLUMN_LANGUAGE_ACTIVE, rowData[4]);//active
			        values.put(MySQLiteHelper.COLUMN_LANGUAGE_SELECTED, rowData[5]);//selected
			        values.put(MySQLiteHelper.COLUMN_LANGUAGE_IMG_SRC, rowData[6]);//img_src
			        
			        db.insert(MySQLiteHelper.TABLE_LANGUAGE,
							null, values);
			        
			        count ++; //Increment count
			    }
			    csvStream.close();
			    
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
}