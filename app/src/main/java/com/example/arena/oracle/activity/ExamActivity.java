package com.example.arena.oracle.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.LocaleDisplayNames;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.arena.oracle.R;
import com.example.arena.oracle.SystemTestSystem;
import com.example.arena.oracle.Utils.Action;
import com.example.arena.oracle.Utils.BmobUtils;
import com.example.arena.oracle.bean.Grade;
import com.example.arena.oracle.bean.Question;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.security.Key;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.R.attr.action;

public class ExamActivity extends AppCompatActivity implements View.OnClickListener{

    private List<Question> questions = new ArrayList<Question>();
    private List<String> answers = new ArrayList<String>();

    private TextView question, id, num, tv_time;

    private RadioGroup singleRadioGroup;
    private RadioButton singleOptionA;
    private RadioButton singleOptionB;
    private RadioButton singleOptionC;
    private RadioButton singleOptionD;

    private Button finishButton;
    private int tag = 0;
    private String paperName;
    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private Grade grade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        EventBus.getDefault().register(this);
        paperName = getIntent().getStringExtra("paperName");
        Log.d("activity_test_paperName",paperName);


        BmobUtils.downloadQuestionList(this, paperName);
        finishButton = (Button)findViewById(R.id.button_finish_test);

        question = (TextView)findViewById(R.id.question);
        id = (TextView)findViewById(R.id.id);
        num = (TextView)findViewById(R.id.tv_num_of_question);
        tv_time = (TextView)findViewById(R.id.tv_time_limit);


        singleRadioGroup = (RadioGroup)findViewById(R.id.single_option_group);
        singleOptionA = (RadioButton)findViewById(R.id.single_option_A);
        singleOptionB = (RadioButton)findViewById(R.id.single_option_B);
        singleOptionC = (RadioButton)findViewById(R.id.single_option_C);
        singleOptionD = (RadioButton)findViewById(R.id.single_option_D);

        finishButton.setOnClickListener(this);

        testTimeThread(15);   //测试时间

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_finish_test:
                if(tag==questions.size()-2){
                    finishButton.setText("交卷");
                }
                if(saveAnswer()){
                    if(tag>=questions.size()-1){
                        Log.d("准备交卷","准备交卷");
                        finishTest();
                        showDialog(grade);
                    }
                    else{
                        tag++;
                        nextQuestion();
                    }
                }
        }
    }

    @Subscriber
    public void onReceiveActionEvent(Action action){
        switch (action){
            case DOWNLOAD_QUESTION_LIST:
                Log.d("获取试卷列表成功", "获取试卷列表成功");
                questions = BmobUtils.questionList;
                Log.d("题目数量", questions.size()+".");
                if(questions.size()==1){
                    finishButton.setText("交卷");
                }
                id.setText((tag+1)+".");
                num.setText((tag+1)+"/"+questions.size());
                question.setText(questions.get(tag).getQuestion());
                singleOptionA.setText(questions.get(tag).getOptionA());
                singleOptionB.setText(questions.get(tag).getOptionB());
                singleOptionC.setText(questions.get(tag).getOptionC());
                singleOptionD.setText(questions.get(tag).getOptionD());
                break;
            case QUERY_ERROR:
                break;
        }
    }

    private boolean saveAnswer(){
        String answer="";
        if(singleOptionA.isChecked()){
            answer="A";
        }
        else if(singleOptionB.isChecked()){
            answer="B";
        }
        else if(singleOptionC.isChecked()){
            answer="C";
        }
        else if(singleOptionD.isChecked()){
            answer="D";
        }
        else{
            return false;
        }
        singleRadioGroup.clearCheck();
        answers.add(answer);
        return true;
    }

    private void nextQuestion(){
        //字符串构造中()多用一组括号，可以用于啊运算
        id.setText((tag+1)+".");
        num.setText((tag+1)+"/"+questions.size());
        question.setText(questions.get(tag).getQuestion());
        singleOptionA.setText(questions.get(tag).getOptionA());
        singleOptionB.setText(questions.get(tag).getOptionB());
        singleOptionC.setText(questions.get(tag).getOptionC());
        singleOptionD.setText(questions.get(tag).getOptionD());
    }

    private void finishTest(){
        int questionNum = questions.size();
        int correctNum=0;
        if(answers.size()>1){
            for(int i=0; i<questionNum; i++){
                if(answers.get(i).equals(questions.get(i).getAnswer())){
                    correctNum++;
                }
            }
        }
        else{
            ;
        }
        Log.d("试卷名称",paperName);
        grade = new Grade();
        grade.setPaperName(paperName);
        grade.setUsername(SystemTestSystem.getStudent().getUsername());
        grade.setJoinTime(df.format(new Date()));
        //保存答案数组，方便回顾
        grade.setAnswers(answers);
        grade.setGrade((correctNum*100)/questionNum);
        BmobUtils.saveGrade(this, grade);
    }

    private void showDialog(final Grade grade){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("交卷后可前往个人信息查看成绩");

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent();
                i.putExtra("data", grade);
                setResult(1,i);
                finish();
            }
        });
        builder.create().show();
    }

    private void showWarnDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("退出自动提交成绩");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveAnswer();
                finishTest();
                Intent i = new Intent();
                i.putExtra("data", grade);
                setResult(1, i);
                finish();
            }
        });
        builder.create().show();
    }

    private void showTimeDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("考试时间结束");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveAnswer();
                finishTest();
                Intent i = new Intent();
                i.putExtra("data", grade);
                setResult(1, i);
                finish();
            }
        });
        builder.create().show();
    }

    private void testTimeThread(final int time){
        //处理时间限制
        tv_time.setText("时间："+time+":00");
        new Thread(){
            public void run(){
                int t = time;
                int s = 60;
                for(int i=0; i<time; i++){
                    for(int j=0; j<60; j++){
                        try {
                            //一秒钟更新一次
                            sleep(1000);
                        }
                        catch(InterruptedException e){
                            e.printStackTrace();
                        }
                        if(s==60){
                            t--;
                        }
                        s--;
                        if(s==0){
                            s=60;
                        }
                        Message msg = new Message();
                        msg.what = 0;
                        msg.arg1 = t;
                        msg.arg2 = s;
                        handler.sendMessage(msg);
                    }
                }
                //设置时间
                Message msg = new Message();
                msg.what=1;
                msg.arg1=t;
                handler.sendMessage(msg);
            }
        }.start();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    String s= null;
                    if(msg.arg2==60){
                        s="00";
                    }
                    else{
                        s=msg.arg2+" ";
                    }
                    tv_time.setText("时间："+msg.arg1+":"+s);
                    break;
                case 1:
                    showTimeDialog();
                    break;
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            showWarnDialog();
            return false;
        }
        else if(keyCode == KeyEvent.KEYCODE_HOME){
            showWarnDialog();
            return false;
        }
        else{
            return super.onKeyDown(keyCode, event);
        }
    }
}
