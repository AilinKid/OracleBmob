package com.example.arena.oracle.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Aena on 2017/3/25.
 */
public class Feedback extends BmobObject{

    private Student author;
    private String content;

    public Student getAuthor() {
        return author;
    }

    public void setAuthor(Student author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
