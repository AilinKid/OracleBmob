package com.example.arena.oracle.bean;

/**
 * Created by macbook on 2017/4/17.
 */

public class Post {
    private int teacherId;
    private String title;
    private String description;

    public int getTeacher() {
        return teacherId;
    }

    public void setTeacher(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
