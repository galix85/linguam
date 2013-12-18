package com.galix.linguam.db;

import java.util.ArrayList;
import java.util.List;

import com.galix.linguam.pojo.TranslatedWord;
import com.galix.linguam.util.WordReferenceUtil.Term;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class TranslationDBAdapter {

	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.COLUMN_TRANSLATION_ID,
			MySQLiteHelper.COLUMN_TRANSLATION_POS,
			MySQLiteHelper.COLUMN_TRANSLATION_SENSE,
			MySQLiteHelper.COLUMN_TRANSLATION_TERM,
			MySQLiteHelper.COLUMN_TRANSLATION_USAGE };

	public TranslationDBAdapter(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public TranslatedWord createTranslation(Term translation) {

		ContentValues values = new ContentValues();

		values.put(MySQLiteHelper.COLUMN_TRANSLATION_POS, translation.getPOS());
		values.put(MySQLiteHelper.COLUMN_TRANSLATION_SENSE,
				translation.getSense());
		values.put(MySQLiteHelper.COLUMN_TRANSLATION_TERM,
				translation.getTerm());
		values.put(MySQLiteHelper.COLUMN_TRANSLATION_USAGE,
				translation.getUsage());

		long insertId = database.insert(MySQLiteHelper.TABLE_TRANSLATION, null,
				values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_TRANSLATION,
				allColumns, MySQLiteHelper.COLUMN_TRANSLATION_ID + " = "
						+ insertId, null, null, null, null);
		cursor.moveToFirst();
		TranslatedWord newTranslation = cursorToTranslate(cursor);
		cursor.close();
		return newTranslation;
	}

	public void deleteTranslation(TranslatedWord translation) {
		long id = translation.getId();
		System.out.println("Comment deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_TRANSLATION,
				MySQLiteHelper.COLUMN_TRANSLATION_ID + " = " + id, null);
	}

	public List<TranslatedWord> getAllTranslates() {
		List<TranslatedWord> translationWords = new ArrayList<TranslatedWord>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_TRANSLATION,
				allColumns, null, null, null, null, null);

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

	private TranslatedWord cursorToTranslate(Cursor cursor) {
		TranslatedWord translation = new TranslatedWord();
		translation.setId((int) cursor.getLong(0));
		translation.setTerm(cursor.getString(1));
		return translation;
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

}