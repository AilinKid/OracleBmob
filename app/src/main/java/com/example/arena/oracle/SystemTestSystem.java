package com.example.arena.oracle;

import android.app.Application;

import com.example.arena.oracle.bean.Student;
import com.facebook.drawee.backends.pipeline.Fresco;

import cn.bmob.v3.Bmob;

/**
 * Created by macbook on 2017/4/18.
 */

public class SystemTestSystem extends Application{
    private static Student student;
    public static Student getStudent(){
        return student;
    }

    public static void setStudent(Student s){
        student= s;
    }

    //Bomb初始化 全局保存变量student

    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, "516220a81b04cfed1cb06e575b675192");
        Fresco.initialize(this);
    }
}
