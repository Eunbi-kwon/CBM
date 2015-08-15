package com.heartbeatplatform.cognitivebiasmodification.cbm.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Naver on 2015-07-05.
 */
public class SQLLteOpenHelperQuestionInfo extends SQLiteOpenHelper{


    public SQLLteOpenHelperQuestionInfo(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // CREATE
        String createSQL = "CREATE TABLE CBM_I_TEST_QUESTION( QUE_NO INTEGER PRIMARY KEY, POS_SMILE TEXT, POS_ANGRY TEXT, POS_X TEXT, IS_POSITIVE TEXT, ARROW TEXT);";
        db.execSQL(createSQL);

        // INSERT INTO
        String insertSQLs [] = {"INSERT INTO CBM_I_TEST_QUESTION VALUES (1,'d','u','d','p','l');" ,
                "INSERT INTO CBM_I_TEST_QUESTION VALUES (2,'u','d','u','p', 'l');" ,
                "INSERT INTO CBM_I_TEST_QUESTION VALUES (3,'d','u','u','n', 'l');" ,
                "INSERT INTO CBM_I_TEST_QUESTION VALUES (4,'d','u','d','p', 'l');" ,
                "INSERT INTO CBM_I_TEST_QUESTION VALUES (5,'u','d','u','p', 'l');" ,
                "INSERT INTO CBM_I_TEST_QUESTION VALUES (6,'d','u','u','n', 'l');" ,
                "INSERT INTO CBM_I_TEST_QUESTION VALUES (7,'u','d','d','n', 'l');" ,
                "INSERT INTO CBM_I_TEST_QUESTION VALUES (8,'d','u','d','p', 'l');" ,
                "INSERT INTO CBM_I_TEST_QUESTION VALUES (9,'u','d','d','n', 'l');" ,
                "INSERT INTO CBM_I_TEST_QUESTION VALUES (10,'u','d','d','n', 'l');"  ,
                "INSERT INTO CBM_I_TEST_QUESTION VALUES (11,'u','d','u','p', 'r');" ,
                "INSERT INTO CBM_I_TEST_QUESTION VALUES (12,'d','u','u','n', 'r');" ,
                "INSERT INTO CBM_I_TEST_QUESTION VALUES (13,'u','d','d','n', 'r');" ,
                "INSERT INTO CBM_I_TEST_QUESTION VALUES (14,'d','u','d','p', 'r');" ,
                "INSERT INTO CBM_I_TEST_QUESTION VALUES (15,'d','u','u','n', 'r');" ,
                "INSERT INTO CBM_I_TEST_QUESTION VALUES (16,'u','d','u','p', 'r');" ,
                "INSERT INTO CBM_I_TEST_QUESTION VALUES (17,'d','u','d','p', 'r');" ,
                "INSERT INTO CBM_I_TEST_QUESTION VALUES (18,'u','d','d','n', 'r');" ,
                "INSERT INTO CBM_I_TEST_QUESTION VALUES (19,'d','u','u','n', 'r');" ,
                "INSERT INTO CBM_I_TEST_QUESTION VALUES (20,'u','d','u','p', 'r');" };

        for (String insertSQL : insertSQLs) {
            db.execSQL(insertSQL);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropSQL1 = "DROP TABLE IF EXISTS CBM_I_TEST_QUESTION";
        db.execSQL(dropSQL1);

        onCreate(db);
    }
}
