package com.example.arena.oracle.bean;

import cn.bmob.v3.BmobObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by macbook on 2017/4/18.
 */

public class Grade extends BmobObject implements Serializable {
    private String username="";
    private String paperName="";
    private int grade=0;
    private String joinTime="";
    private List<String> answers;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPaperName() {
        return paperName;
    }

    public void setPaperName(String paperName) {
        this.paperName = paperName;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(String joinTime) {
        this.joinTime = joinTime;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }
}
