package com.galix.linguam.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_TRANSLATION = "translation";
	public static final String COLUMN_TRANSLATION_ID = "_id";
	public static final String COLUMN_TRANSLATION_TERM = "term";
	public static final String COLUMN_TRANSLATION_POS = "pos";
	public static final String COLUMN_TRANSLATION_SENSE = "sense";
	public static final String COLUMN_TRANSLATION_USAGE = "usage";
	public static final String COLUMN_TRANSLATION_ORIGINALWORD = "originalword";
	public static final String COLUMN_TRANSLATION_SELECTED = "selected";

	public static final String TABLE_ORIGINALWORD = "originalword";
	public static final String COLUMN_ORIGINALWORD_ID = "_id";
	public static final String COLUMN_ORIGINALWORD_TERM = "term";
	public static final String COLUMN_ORIGINALWORD_POS = "pos";
	public static final String COLUMN_ORIGINALWORD_SENSE = "sense";
	public static final String COLUMN_ORIGINALWORD_USAGE = "usage";

	private static final String DATABASE_NAME = "linguam.db";
	private static final int DATABASE_VERSION = 6;

	// Database creation sql statement
	private static final String CREATE_TABLE_TRANSLATION = "create table "
			+ TABLE_TRANSLATION + "(" + COLUMN_TRANSLATION_ID
			+ " integer primary key autoincrement, " + COLUMN_TRANSLATION_TERM
			+ " text not null" + ", " + COLUMN_TRANSLATION_POS + " text" + ", "
			+ COLUMN_TRANSLATION_SENSE + " text" + ", "
			+ COLUMN_TRANSLATION_USAGE + " text" + ", "
			+ COLUMN_TRANSLATION_ORIGINALWORD + " text" + ", "
			+ COLUMN_TRANSLATION_SELECTED + " integer);";

	// Database creation sql statement
	private static final String CREATE_TABLE_ORIGINALWORD = "create table "
			+ TABLE_ORIGINALWORD + "(" + COLUMN_ORIGINALWORD_ID
			+ " integer primary key autoincrement, " + COLUMN_ORIGINALWORD_TERM
			+ " text not null" + ", " + COLUMN_ORIGINALWORD_POS + " text"
			+ ", " + COLUMN_ORIGINALWORD_SENSE + " text" + ", "
			+ COLUMN_ORIGINALWORD_USAGE + " text);";
	

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
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORIGINALWORD);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSLATION);
		onCreate(db);
	}

}