package com.example.openrenttraning.helper;

public class AnswerModel {
    String question, postingtime, addingtime, answer;
    Boolean sss;

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

    public String getAddingtime() {
        return addingtime;
    }

    public void setAddingtime(String addingtime) {
        this.addingtime = addingtime;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Boolean getSss() {
        return sss;
    }

    public void setSss(Boolean sss) {
        this.sss = sss;
    }

    public AnswerModel(String question, String postingtime, String addingtime, String answer, Boolean sss) {
        this.question = question;
        this.postingtime = postingtime;
        this.addingtime = addingtime;
        this.answer = answer;
        this.sss = sss;
    }
}
