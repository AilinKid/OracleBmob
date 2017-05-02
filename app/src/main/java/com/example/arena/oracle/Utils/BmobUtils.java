package com.example.arena.oracle.Utils;

import android.content.Context;
import android.util.Log;

import com.example.arena.oracle.bean.Grade;
import com.example.arena.oracle.bean.Paper;
import com.example.arena.oracle.bean.Question;

import org.simple.eventbus.EventBus;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.helper.NotificationCompat;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by macbook on 2017/4/19.
 */

public class BmobUtils {

    public static List<Grade> gradeList = new ArrayList<Grade>();
    public static List<Paper> paperList = new ArrayList<Paper>();
    public static List<Question> questionList = new ArrayList<Question>();

    public static void downloadGradeList(Context context, String username){
        BmobQuery<Grade> query = new BmobQuery<Grade>();
        query.addWhereEqualTo("username", username);
        query.setLimit(100);
        query.findObjects(context, new FindListener<Grade>() {
            @Override
            public void onSuccess(List<Grade> list) {
                gradeList = new ArrayList<Grade>();
                for(Grade grade:list){
                    gradeList.add(grade);
                }
                EventBus.getDefault().post(Action.DOWNLOAD_GRADE_LIST);
            }

            @Override
            public void onError(int i, String s) {
                Log.d("成绩查询失败", "成绩查询失败");
                EventBus.getDefault().post(Action.QUERY_ERROR);
            }
        });
    }

    public static void downloadPaperList(Context context){
        BmobQuery<Paper> query = new BmobQuery<Paper>();
        query.order("-joinTime");
        query.setLimit(100);
        query.findObjects(context, new FindListener<Paper>() {
            @Override
            public void onSuccess(List<Paper> list) {
                Log.d("试卷查询","试卷长度为"+list.size());
                paperList = new ArrayList<Paper>();
                for(int i=0; i<list.size(); i++){
                    paperList.add(list.get(i));
                }
                EventBus.getDefault().post(Action.DOWNLOAD_PAPER_LIST);
            }

            @Override
            public void onError(int i, String s) {
                Log.d("试卷查询","试卷查询失败");
                EventBus.getDefault().post(Action.QUERY_ERROR);
            }
        });
    }

    public static void downloadTPaperList(Context context){
        BmobQuery<Paper> query = new BmobQuery<Paper>();
        query.order("joinTime");
        query.findObjects(context, new FindListener<Paper>() {
            @Override
            public void onSuccess(List<Paper> list) {
                paperList = new ArrayList<Paper>();
                Log.d("试卷查询","试卷长度为"+list.size());
                for(int i=0; i<list.size(); i++){
                    paperList.add(list.get(i));
                }
                EventBus.getDefault().post(Action.DOWNLOAD_PAPER_LIST);
            }

            @Override
            public void onError(int i, String s) {
                Log.d("试卷查询","试卷查询失败");
                EventBus.getDefault().post(Action.QUERY_ERROR);
            }
        });
    }

    public static void downloadQuestionList(Context context, String paperName){
        BmobQuery<Question> query = new BmobQuery<Question>();
        query.addWhereEqualTo("paperName", paperName);
        query.setLimit(100);
        query.findObjects(context, new FindListener<Question>() {
            @Override
            public void onSuccess(List<Question> list) {
                questionList = new ArrayList<Question>();
                Log.d("试题查询","试卷长度为"+list.size());
                for(Question question:list){
                    questionList.add(question);
                }
                EventBus.getDefault().post(Action.DOWNLOAD_QUESTION_LIST);
            }

            @Override
            public void onError(int i, String s) {
                Log.d("试题查询", "查询失败");
                EventBus.getDefault().post(Action.QUERY_ERROR);
            }
        });
    }

    public static void saveNewPaper(Context context, Paper paper, List<Question> questions){
        //每个单一题目的保存
        for(int i=0; i<questions.size(); i++){
            Question q = questions.get(i);
            q.save(context, new SaveListener() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(int i, String s) {

                }
            });
        }
        //试卷的保存
        paper.save(context, new SaveListener() {
            @Override
            public void onSuccess() {
                Log.d("保存试卷","保存试卷成功");
                EventBus.getDefault().post(Action.SAVE_PAPER_SUCCESS);
            }

            @Override
            public void onFailure(int i, String s) {
                Log.d("保存试卷", "保存试卷失败");
                EventBus.getDefault().post(Action.SAVE_PAPER_ERROR);
            }
        });
    }

    public static void saveGrade(Context context, Grade grade){
        grade.save(context, new SaveListener() {
            @Override
            public void onSuccess() {
                Log.d("保存成绩", "保存成绩成功");
            }

            @Override
            public void onFailure(int i, String s) {
                Log.d("保存成绩", "保存成绩失败");
            }
        });
    }
}
