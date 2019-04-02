package com.example.whowantstobeamillionaire;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

class SQLiteScore extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dbScore";
    private static final String TABLE_NAME = "Score";
    private static final String COLUMN_NAME_ID = "id";
    private static final String COLUMN_NAME_POINT= "point";
    private static final String COLUMN_NAME_MONEY = "money";
    private static final String COLUMN_NAME_TIME = "time";
    private static final int DATABASE_VERSION = 1;

    public SQLiteScore(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create scores table for database
        String createTable =
                "CREATE TABLE " +
                        TABLE_NAME + " ( " +
                        COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_NAME_MONEY + " TEXT, " +
                        COLUMN_NAME_TIME + " TEXT, " +
                        COLUMN_NAME_POINT + " INTEGER ); ";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    //add new score to database
    public void addScore(Success success) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_MONEY, success.getMoney());
        values.put(COLUMN_NAME_POINT, success.getPoint());
        values.put(COLUMN_NAME_TIME, success.getTime());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    //delete all scores from database
    public void deleteAllScores() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

    public ArrayList<Success> getAllScores() {
        ArrayList<Success> successArrayList = new ArrayList<>();

        String sql_command = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql_command,null);

        while(cursor.moveToNext()){
            Long id = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_ID));
            String money = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_MONEY));
            int point = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_POINT));
            String time = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TIME));

            Success success = new Success(id, money, Integer.parseInt(time), point);
            successArrayList.add(success);
        }

        cursor.close();
        db.close();

        return successArrayList;
    }

    //retrieve all scores from database
    public ArrayList<Success> getAllScoresHighest() {
        ArrayList<Success> successArrayList = new ArrayList<>();

        String sql_command = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_NAME_POINT + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql_command,null);

        while(cursor.moveToNext()){
            Long id = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_ID));
            String money = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_MONEY));
            int point = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_POINT));
            String time = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TIME));

            Success success = new Success(id,money,Integer.parseInt(time),point);
            successArrayList.add(success);
        }

        cursor.close();
        db.close();

        return successArrayList;
    }
}