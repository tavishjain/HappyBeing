package io.github.abhishek.happybeing;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MyDatabaseHelper extends SQLiteOpenHelper implements BaseColumns {
    public static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "entries.db";

    // Table name
    public static final String TABLE_ENTRYS = "entries";

    //Columns names
    private static final String _ID = BaseColumns._ID;
    private static final String KEY_TEXT = "text";
    private static final String KEY_PHOTO = "photo";
    private static final String KEY_DATE_TIME = "date";
    private static final String KEY_FEALING = "fealing";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_SHAKESCORE = "shakescore";

    public MyDatabaseHelper(Context context, String name,
                            SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create the table

        String CREATE_TABLE_ENTRY = "CREATE TABLE " + TABLE_ENTRYS  + "("
                + _ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + KEY_TEXT + " TEXT, "
                + KEY_PHOTO+ " BLOB, "
                + KEY_DATE_TIME + " TEXT, "
                + KEY_FEALING + " INTEGER,"
                + KEY_SHAKESCORE + " INTEGER,"
                + KEY_LOCATION+ " TEXT )";
        Log.e("string create table","ok");
        db.execSQL(CREATE_TABLE_ENTRY);
        //Toast.makeText(myContent, "Database built", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {//update the database
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRYS);
        onCreate(db);
    }
    //add entry
    public int addEntry(Entry entry) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e("my tag","adding entry with fealing"+entry.getFealing());
        ContentValues values = new ContentValues();
        values.put(KEY_TEXT, entry.getText());
        values.put(KEY_PHOTO, entry.getPhoto());
        values.put(KEY_DATE_TIME, entry.getDate_Time());
        values.put(KEY_FEALING, entry.getFealing());
        values.put(KEY_SHAKESCORE, entry.getShakescore());
        values.put(KEY_LOCATION, entry.getLocation());
        // Inserting Row
        long entry_id=db.insert(TABLE_ENTRYS, null, values);
        // Closing database connection
        db.close();
        return  (int)entry_id;
    }
    //get entry from table
    public Entry getEntry(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor =  db.rawQuery( "select * from "+TABLE_ENTRYS+" where "+_ID+"="+id+"", null );

        Entry e= new Entry();
        while(cursor.moveToNext()) {
            e.setId(cursor.getInt(cursor.getColumnIndex(_ID)));
            e.setText(cursor.getString(cursor.getColumnIndex(KEY_TEXT)));
            e.setPhoto(cursor.getBlob(cursor.getColumnIndex(KEY_PHOTO)));
            e.setDate_Time(cursor.getString(cursor.getColumnIndex(KEY_DATE_TIME)));
            e.setFealing(cursor.getInt(cursor.getColumnIndex(KEY_FEALING)));
            e.setShakescore(cursor.getInt(cursor.getColumnIndex(KEY_SHAKESCORE)));
            e.setLocation(cursor.getString(cursor.getColumnIndex(KEY_LOCATION)));

        }
        // Closing database connection
        db.close();
        return e;
    }

    //Get all entry by cursor
    public List<Entry> getAllEntry() {
        List<Entry> entryList = new ArrayList<Entry>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_ENTRYS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Entry entry = new Entry();
                entry.setId(Integer.parseInt(cursor.getString(0)));
                entry.setText(cursor.getString(1));
                entry.setPhoto(cursor.getBlob(2));
                entry.setDate_Time(cursor.getString(3));
                entry.setFealing(cursor.getInt(4));
                entry.setShakescore(cursor.getInt(5));
                entry.setLocation(cursor.getString(6));
                // Adding entry to list
                entryList.add(entry);
            } while (cursor.moveToNext());
        }
        // return entry list
        return entryList;
    }

    //provides to DiaryActivity reverse order listView (from latest to oldest)
    public List<Entry> getAllReverseEntry() {
        List<Entry> entryList = new ArrayList<Entry>();
        String selectQuery = "SELECT * FROM " + TABLE_ENTRYS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToLast()) {
            do {
                Entry entry = new Entry();
                entry.setId(Integer.parseInt(cursor.getString(0)));
                entry.setText(cursor.getString(1));
                entry.setPhoto(cursor.getBlob(2));
                entry.setDate_Time(cursor.getString(3));
                entry.setFealing(cursor.getInt(4));
                entry.setShakescore(cursor.getInt(5));
                entry.setLocation(cursor.getString(6));

                entryList.add(entry);
            } while (cursor.moveToPrevious());
        }
        return entryList;
    }
}