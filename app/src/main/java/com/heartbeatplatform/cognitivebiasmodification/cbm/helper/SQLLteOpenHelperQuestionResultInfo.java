package com.heartbeatplatform.cognitivebiasmodification.cbm.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Naver on 2015-07-05.
 */
public class SQLLteOpenHelperQuestionResultInfo extends SQLiteOpenHelper{


    public SQLLteOpenHelperQuestionResultInfo(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createSQL = "CREATE TABLE CBM_R_TEST_QUESTION( QUE_NO INTEGER, IS_POSITIVE TEXT, RSLT_MILLSEC INTEGER, IS_RIGHT INTEGER);";
        db.execSQL(createSQL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       String dropSQL = "DROP TABLE IF EXISTS CBM_R_TEST_QUESTION";
        db.execSQL(dropSQL);

        onCreate(db);
    }
}
