package com.example.arena.oracle.bean;

import cn.bmob.v3.BmobUser;

/**
 * Created by macbook on 2017/4/18.
 */

public class Student extends BmobUser {
    //数据建立时候默认值如下
    private String name="";
    private String mClass="";
    private int identity=1;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getmClass() {
        return mClass;
    }

    public void setmClass(String mClass) {
        this.mClass = mClass;
    }

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }
}
