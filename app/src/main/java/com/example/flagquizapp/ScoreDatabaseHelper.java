package com.example.flagquizapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ScoreDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "scores.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_SCORES = "scores";

    public ScoreDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SCORES_TABLE = "CREATE TABLE " + TABLE_SCORES + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT, "
                + "score INTEGER)";
        db.execSQL(CREATE_SCORES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORES);
        onCreate(db);
    }

    // Method to add a new score to the database
    public void addScore(String userName, int score) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", userName); // Add the user's name
        values.put("score", score);    // Add the score

        db.insert(TABLE_SCORES, null, values); // Insert the new score
        db.close(); // Close the database
    }

    // Method to delete all scores from the database
    public void deleteAllScores() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_SCORES);
        db.close();
    }
}
