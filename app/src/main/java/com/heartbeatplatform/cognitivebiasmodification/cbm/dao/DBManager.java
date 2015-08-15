package com.heartbeatplatform.cognitivebiasmodification.cbm.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.heartbeatplatform.cognitivebiasmodification.cbm.helper.SQLLteOpenHelperQuestionInfo;
import com.heartbeatplatform.cognitivebiasmodification.cbm.helper.SQLLteOpenHelperQuestionResultInfo;
import com.heartbeatplatform.cognitivebiasmodification.cbm.model.QuestionInfo;
import com.heartbeatplatform.cognitivebiasmodification.cbm.model.QuestionResultInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Naver on 2015-08-15.
 */
public class DBManager {


    // DB관련 상수 선언
    private static final String dbNameInfo = "QuestionInfo.db";
    private static final String dbNameResult = "QuestionResultInfo.db";
    public static final int dbVersion = 1;

    // DB관련 객체 선언
    private SQLLteOpenHelperQuestionInfo dbHelperQuestionInfo; // DB opener
    private SQLLteOpenHelperQuestionResultInfo dbHelperQuestionResultInfo; // DB opener
    private SQLiteDatabase dbQuestionInfo; // DB controller
    private SQLiteDatabase dbQuestionResultInfo; // DB controller

    // 부가적인 객체들
    private Context context;

    // 생성자
    public DBManager(Context context) {
        this.context = context;
        this.dbHelperQuestionInfo = new SQLLteOpenHelperQuestionInfo(context, dbNameInfo, null, dbVersion);
        this.dbHelperQuestionResultInfo = new SQLLteOpenHelperQuestionResultInfo(context, dbNameResult, null, dbVersion);
        dbQuestionInfo = dbHelperQuestionInfo.getWritableDatabase();
        dbQuestionResultInfo = dbHelperQuestionResultInfo.getWritableDatabase();


    }

    public void onUpgrade() {
        dbHelperQuestionInfo.onUpgrade(dbQuestionInfo, 1, 1);
        dbHelperQuestionResultInfo.onUpgrade(dbQuestionResultInfo, 1, 1);
    }

    // 데이터 추가
    public void insertResultInfo(QuestionResultInfo resultInfo) {
        String sql = "INSERT INTO CBM_R_TEST_QUESTION VALUES("
                + resultInfo.getQuestionNo() + ", '"
                + resultInfo.getIsPositive() + "', "
                + resultInfo.getResultMillsec() + ", "
                + resultInfo.getIsRight() +");";

        Log.e("insertResultInfo", "insertResultInfo : " + sql);
        dbQuestionResultInfo.execSQL(sql);
    }

    // 데이터 갱신
    public void updateData(QuestionInfo info, int index) {
        /*String sql = "update " + tableName + " set SSID = '" + info.getSSID()
                + "', capabilities = " + info.getCapabilities()
                + ", passwd = '" + info.getPasswd() + "' where id = " + index
                + ";";
        db.execSQL(sql);*/
    }

    // 데이터 삭제
    public void removeData(int index) {
        /*String sql = "delete from " + tableName + " where id = " + index + ";";
        db.execSQL(sql);*/
    }

    // 데이터 검색
    public QuestionInfo selectQuestionInfo(int index) {
        //String sql = "SELECT * FROM CBM_I_TEST_QUESTION WHERE QUE_NO = "+ index + ";";
        String sql = "SELECT QUE_NO, POS_SMILE, POS_ANGRY, POS_X, IS_POSITIVE, ARROW FROM CBM_I_TEST_QUESTION WHERE QUE_NO = " + index + ";";
        Cursor result = dbQuestionInfo.rawQuery(sql, null);
        // result(Cursor 객체)가 비어 있으면 false 리턴

        if (result.moveToFirst()) {
            QuestionInfo info;
            Log.e("DBManager.java", "Question Info Count  :" + result.getCount() + " / " + result.toString());
            info = new QuestionInfo(result.getInt(0), result.getString(1),
                    result.getString(2), result.getString(3), result.getString(4), result.getString(5));

            result.close();
            return info;
        }
        result.close();
        return null;
    }

    // 데이터 검색
    public QuestionResultInfo selectQuestionResultInfo(int index) {
        String sql = "SELECT * FROM CBM_R_TEST_QUESTION WHERE QUE_NO = "+ index + ";";
        Cursor result = dbQuestionResultInfo.rawQuery(sql, null);
        // result(Cursor 객체)가 비어 있으면 false 리턴

        if (result.moveToFirst()) {
            QuestionResultInfo info;
            info = new QuestionResultInfo(result.getInt(0), result.getString(1),
                    result.getInt(2), result.getInt(3));

            result.close();
            return info;
        }
        result.close();
        return null;
    }

    // 데이터 검색.
    public List<QuestionResultInfo> selectQuestionResultInfoAll() {
        String sql = "SELECT * FROM CBM_R_TEST_QUESTION;";
        Cursor result = dbQuestionResultInfo.rawQuery(sql, null);
        // result(Cursor 객체)가 비어 있으면 false 리턴

        List<QuestionResultInfo> infoList = new ArrayList<QuestionResultInfo>();

        if (result.moveToFirst()) {
            while (result.moveToNext()) {
                QuestionResultInfo info;
                info = new QuestionResultInfo(result.getInt(0), result.getString(1),
                        result.getInt(2), result.getInt(3));
                infoList.add(info);
            }
        }
        result.close();
        return infoList;
    }

    // 데이터 삭제
    public void removeQuestionResultInfo() {
        String sql = "DELETE FROM CBM_R_TEST_QUESTION ;";
        dbQuestionResultInfo.execSQL(sql);
    }


}
