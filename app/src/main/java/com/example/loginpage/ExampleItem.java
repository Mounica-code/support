package com.example.loginpage;

public class ExampleItem {

    private String ques;
    private String Ans;

    public ExampleItem() {
    }

    public ExampleItem(String ques, String ans) {
        this.ques = ques;
        Ans = ans;
    }

    public String getQues() {
        return ques;
    }

    public void setQues(String ques) {
        this.ques = ques;
    }

    public String getAns() {
        return Ans;
    }

    public void setAns(String ans) {
        Ans = ans;
    }
}

