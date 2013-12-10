package com.galix.linguam.model;

import java.util.ArrayList;
import java.util.List;

import com.galix.linguam.controller.ParseJSONWordReference.Term;
import com.galix.linguam.pojo.OriginalWord;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class OriginalWordDBAdapter {

  // Database fields
  private SQLiteDatabase database;
  private MySQLiteHelper dbHelper;
  private String[] allColumns = {
      MySQLiteHelper.COLUMN_ORIGINALWORD_ID,MySQLiteHelper.COLUMN_ORIGINALWORD_POS,
      MySQLiteHelper.COLUMN_ORIGINALWORD_SENSE,MySQLiteHelper.COLUMN_ORIGINALWORD_TERM,
      MySQLiteHelper.COLUMN_ORIGINALWORD_USAGE};

  public OriginalWordDBAdapter(Context context) {
    dbHelper = new MySQLiteHelper(context);
  }

  public void open() throws SQLException {
    database = dbHelper.getWritableDatabase();
  }

  public void close() {
    dbHelper.close();
  }

  public OriginalWord createOriginalWord(Term originalWord) {
    
	ContentValues values = new ContentValues();
	
    values.put(MySQLiteHelper.COLUMN_ORIGINALWORD_POS, originalWord.getPOS());
    values.put(MySQLiteHelper.COLUMN_ORIGINALWORD_SENSE, originalWord.getSense());
    values.put(MySQLiteHelper.COLUMN_ORIGINALWORD_TERM, originalWord.getTerm());
    values.put(MySQLiteHelper.COLUMN_ORIGINALWORD_USAGE, originalWord.getUsage());
    
    long insertId = database.insert(MySQLiteHelper.TABLE_ORIGINALWORD, null,
        values);
    Cursor cursor = database.query(MySQLiteHelper.TABLE_ORIGINALWORD,
        allColumns, MySQLiteHelper.COLUMN_ORIGINALWORD_ID + " = " + insertId, null,
        null, null, null);
    cursor.moveToFirst();
    OriginalWord newOriginalWord = cursorToOriginalWord(cursor);
    cursor.close();
    return newOriginalWord;
  }

  public void deleteComment(OriginalWord originalWord) {
    long id = originalWord.getId();
    System.out.println("Comment deleted with id: " + id);
    database.delete(MySQLiteHelper.TABLE_ORIGINALWORD, MySQLiteHelper.COLUMN_ORIGINALWORD_ID
        + " = " + id, null);
  }

  public List<OriginalWord> getAllComments() {
    List<OriginalWord> originalWords = new ArrayList<OriginalWord>();

    Cursor cursor = database.query(MySQLiteHelper.TABLE_ORIGINALWORD,
        allColumns, null, null, null, null, null);

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
    originalWord.setTerm(cursor.getString(1));
    return originalWord;
  }
} 