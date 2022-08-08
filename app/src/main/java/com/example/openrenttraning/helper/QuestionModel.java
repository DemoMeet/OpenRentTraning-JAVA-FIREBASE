package com.example.openrenttraning.helper;

public class QuestionModel {
    String question, postingtime;
    Boolean sss, has;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getPostingtime() {
        return postingtime;
    }

    public void setPostingtime(String postingtime) {
        this.postingtime = postingtime;
    }

    public Boolean getSss() {
        return sss;
    }

    public void setSss(Boolean sss) {
        this.sss = sss;
    }

    public Boolean getHas() {
        return has;
    }

    public void setHas(Boolean has) {
        this.has = has;
    }

    public QuestionModel(String question, String postingtime, Boolean sss, Boolean has) {
        this.question = question;
        this.postingtime = postingtime;
        this.sss = sss;
        this.has = has;
    }
}
