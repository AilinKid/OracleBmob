package com.example.arena.oracle.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by macbook on 2017/4/18.
 */

public class Paper extends BmobObject {
    private String paperId="";
    private String paperName="";
    private String joinTime="";
    private boolean finishState=false;

    public String getPaperId() {
        return paperId;
    }

    public void setPaperId(String paperId) {
        this.paperId = paperId;
    }

    public String getPaperName() {
        return paperName;
    }

    public void setPaperName(String paperName) {
        this.paperName = paperName;
    }

    public String getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(String joinTime) {
        this.joinTime = joinTime;
    }

    public boolean isFinishState() {
        return finishState;
    }

    public void setFinishState(boolean finishState) {
        this.finishState = finishState;
    }
}
