package com.caocm.android.quiz.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by caocm_000 on 12/10/2015.
 */
public class QuizDatabaseHelper extends SQLiteOpenHelper{

    private static String DB_NAME = "quizz";
    private static int DB_VERSION = 1;

    public QuizDatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE QUESTIONS (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "QUSTIONBODY TEXT," +
                "OPTIONA TEXT," +
                "OPTIONB TEXT," +
                "OPTIONC TEXT," +
                "OPTIOND TEXT," +
                "OPTIONE TEXT," +
                "ANSWER INTEGER" +
                ");");
        long qid = insertQustion(db, "The Android documentation describes an Activity as \"a single, focused thing that the user can do.\" Which one of the following statements best expresses why this statement might be somewhat ambiguous today?"
        , "Creative designers can put lots of data on one screen and it works just fine."
        , "Touchable objects only have to be around 40-50 pixels large for human fingers to reliably select them. So you should put as many objects as possible on an Activity's user interface."
        , "Creating Activities at runtime is time consuming, so for the best performance you should have as few as possible."
        , "Some devices, such as Tablets, are large enough to accommodate multiple screenfuls of data at one time."
        , null
        ,3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void deleteQuestions(SQLiteDatabase db){
        db.delete("QUESTIONS", null, null);

    }
    public long insertQustion(SQLiteDatabase db, String body, String optionA, String optionB, String optionC, String optionD, String optionE, int ANSWER){
        ContentValues qustion = new ContentValues();
        qustion.put("QUSTIONBODY", body);
        qustion.put("OPTIONA", optionA);
        qustion.put("OPTIONB", optionB);
        qustion.put("OPTIONC", optionC);
        qustion.put("OPTIOND", optionD);
        qustion.put("ANSWER", ANSWER);
        return db.insert("QUESTIONS", null, qustion );
    }

}
