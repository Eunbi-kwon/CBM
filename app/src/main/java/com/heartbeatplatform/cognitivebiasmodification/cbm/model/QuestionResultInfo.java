package com.heartbeatplatform.cognitivebiasmodification.cbm.model;

/**
 * Created by Naver on 2015-07-05.
 */
public class QuestionResultInfo {
    private int questionNo;
    private String isPositive;
    private long resultMillsec;
    private int isRight;


    public QuestionResultInfo() {
    }

    public QuestionResultInfo(int questionNo, String isPositive, long resultMillsec, int isRight) {
        this.questionNo = questionNo;
        this.isPositive = isPositive;
        this.resultMillsec = resultMillsec;
        this.isRight = isRight;
    }

    public int getQuestionNo() {
        return questionNo;
    }

    public void setQuestionNo(int questionNo) {
        this.questionNo = questionNo;
    }

    public String getIsPositive() {
        return isPositive;
    }

    public void setIsPositive(String isPositive) {
        this.isPositive = isPositive;
    }

    public long getResultMillsec() {
        return resultMillsec;
    }

    public void setResultMillsec(long resultMillsec) {
        this.resultMillsec = resultMillsec;
    }

    public int getIsRight() {
        return isRight;
    }

    public void setIsRight(int isRight) {
        this.isRight = isRight;
    }

    @Override
    public String toString() {
        return "QuestionResultInfo{" +
                "questionNo=" + questionNo +
                ", isPositive='" + isPositive + '\'' +
                ", resultMillsec=" + resultMillsec +
                ", isRight=" + isRight +
                '}';
    }
}
