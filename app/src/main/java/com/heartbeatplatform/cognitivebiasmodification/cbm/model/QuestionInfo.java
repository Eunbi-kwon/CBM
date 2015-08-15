package com.heartbeatplatform.cognitivebiasmodification.cbm.model;

/**
 * Created by Naver on 2015-07-05.
 */
public class QuestionInfo {

    private int questionNo;
    private String posSmile;
    private String posAngry;
    private String posX;
    private String isPositive;
    private String arrow;

    public QuestionInfo() {
    }

    public QuestionInfo(int questionNo, String posSmile, String posAngry, String posX, String isPositive) {
        this.questionNo = questionNo;
        this.posSmile = posSmile;
        this.posAngry = posAngry;
        this.posX = posX;
        this.isPositive = isPositive;
    }

    public QuestionInfo(int questionNo, String posSmile, String posAngry, String posX, String isPositive, String arrow) {
        this.questionNo = questionNo;
        this.posSmile = posSmile;
        this.posAngry = posAngry;
        this.posX = posX;
        this.isPositive = isPositive;
        this.arrow = arrow;
    }

    public int getQuestionNo() {
        return questionNo;
    }

    public void setQuestionNo(int questionNo) {
        this.questionNo = questionNo;
    }

    public String getPosSmile() {
        return posSmile;
    }

    public void setPosSmile(String posSmile) {
        this.posSmile = posSmile;
    }

    public String getPosAngry() {
        return posAngry;
    }

    public void setPosAngry(String posAngry) {
        this.posAngry = posAngry;
    }

    public String getPosX() {
        return posX;
    }

    public void setPosX(String posX) {
        this.posX = posX;
    }

    public String getIsPositive() {
        return isPositive;
    }

    public void setIsPositive(String isPositive) {
        this.isPositive = isPositive;
    }

    public String getArrow() {
        return arrow;
    }

    public void setArrow(String arrow) {
        this.arrow = arrow;
    }

    @Override
    public String toString() {
        return "QuestionInfo{" +
                "questionNo=" + questionNo +
                ", posSmile='" + posSmile + '\'' +
                ", posAngry='" + posAngry + '\'' +
                ", posX='" + posX + '\'' +
                ", isPositive='" + isPositive + '\'' +
                ", arrow='" + arrow + '\'' +
                '}';
    }
}
