package com.example.arena.oracle.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.arena.oracle.R;
import com.example.arena.oracle.Utils.Action;
import com.example.arena.oracle.Utils.BmobUtils;
import com.example.arena.oracle.bean.Grade;
import com.example.arena.oracle.bean.Paper;
import com.example.arena.oracle.bean.Question;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.List;

import cn.bmob.v3.BmobUser;

public class ReviewActivity extends AppCompatActivity implements View.OnClickListener{

    private List<Question> questionList = null;
    private TextView num, id, question;
    private RadioGroup review_radio_group;
    private RadioButton radioButtonA, radioButtonB, radioButtonC, radioButtonD;
    private Button nextButton;
    private String username;
    private Grade grade;
    private int tag=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        EventBus.getDefault().register(this);
        //intent传递对象，必须要实现序列化接口
        grade = (Grade) getIntent().getSerializableExtra("Grade");
        BmobUtils.downloadQuestionList(this,grade.getPaperName());
        username = BmobUser.getCurrentUser(this).getUsername();

        //FindViewById
        num = (TextView) findViewById(R.id.review_num);
        id = (TextView) findViewById(R.id.review_id);
        question = (TextView) findViewById(R.id.review_question);

        review_radio_group = (RadioGroup) findViewById(R.id.review_option_group);
        radioButtonA = (RadioButton) findViewById(R.id.review_option_A);
        radioButtonB = (RadioButton) findViewById(R.id.review_option_B);
        radioButtonC = (RadioButton) findViewById(R.id.review_option_C);
        radioButtonD = (RadioButton) findViewById(R.id.review_option_D);

        nextButton = (Button)findViewById(R.id.button_next);

        nextButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_next:
                Log.d("log","nextbutton");
                if(tag==questionList.size()-2){
                    nextButton.setText("结束");
                }
                if(tag==questionList.size()-1){
                    finish();
                }
                else{
                    tag++;
                    NextQuestion();
                }
                break;
        }

    }


    @Subscriber
    private void onReceiveActionEvent(Action action){
        switch (action){
            case DOWNLOAD_QUESTION_LIST:
                questionList = BmobUtils.questionList;
                Log.d("试卷下载成功","试卷长度"+questionList.size());
                if(questionList.size()==1){
                    nextButton.setText("结束");
                }
                id.setText((tag+1)+".");
                num.setText((tag+1)+"/"+questionList.size());
                question.setText(questionList.get(tag).getQuestion());
                radioButtonA.setText(questionList.get(tag).getOptionA());
                radioButtonB.setText(questionList.get(tag).getOptionB());
                radioButtonC.setText(questionList.get(tag).getOptionC());
                radioButtonD.setText(questionList.get(tag).getOptionD());

                if(grade.getAnswers().size()>0){
                    //防止tag 越界
                    if(grade.getAnswers().size()>tag) {

                        if(grade.getAnswers().get(tag).equals(questionList.get(tag).getAnswer())){
                            if(grade.getAnswers().get(tag).equals("A")){
                                radioButtonA.setTextColor(Color.GREEN);
                                radioButtonA.setChecked(true);
                            }
                            else if(grade.getAnswers().get(tag).equals("B")){
                                radioButtonB.setTextColor(Color.GREEN);
                                radioButtonB.setChecked(true);
                            }
                            else if(grade.getAnswers().get(tag).equals("C")){
                                radioButtonC.setTextColor(Color.GREEN);
                                radioButtonC.setChecked(true);
                            }
                            else if(grade.getAnswers().get(tag).equals("D")){
                                radioButtonD.setTextColor(Color.GREEN);
                                radioButtonD.setChecked(true);
                            }
                        }
                        else{
                            //错误答案
                            if(grade.getAnswers().get(tag).equals("A")){
                                radioButtonA.setTextColor(Color.RED);
                                radioButtonA.setChecked(true);
                            }
                            else if(grade.getAnswers().get(tag).equals("B")){
                                radioButtonB.setTextColor(Color.RED);
                                radioButtonB.setChecked(true);
                            }
                            else if(grade.getAnswers().get(tag).equals("C")){
                                radioButtonC.setTextColor(Color.RED);
                                radioButtonC.setChecked(true);
                            }
                            else if(grade.getAnswers().get(tag).equals("D")){
                                radioButtonD.setTextColor(Color.RED);
                                radioButtonD.setChecked(true);
                            }
                            //正确答案
                            if(questionList.get(tag).getAnswer().equals("A")){
                                radioButtonA.setTextColor(Color.GREEN);
                            }
                            else if(questionList.get(tag).getAnswer().equals("B")){
                                radioButtonB.setTextColor(Color.GREEN);
                            }
                            else if(questionList.get(tag).getAnswer().equals("C")){
                                radioButtonC.setTextColor(Color.GREEN);
                            }
                            else if(questionList.get(tag).getAnswer().equals("D")){
                                radioButtonD.setTextColor(Color.GREEN);
                            }
                        }
                    }
                    else{
                        //正确答案
                        if(questionList.get(tag).getAnswer().equals("A")){
                            radioButtonA.setTextColor(Color.GREEN);
                        }
                        else if(questionList.get(tag).getAnswer().equals("B")){
                            radioButtonB.setTextColor(Color.GREEN);
                        }
                        else if(questionList.get(tag).getAnswer().equals("C")){
                            radioButtonC.setTextColor(Color.GREEN);
                        }
                        else if(questionList.get(tag).getAnswer().equals("D")){
                            radioButtonD.setTextColor(Color.GREEN);
                        }
                    }
                }
                else{

                    //正确答案
                    if(questionList.get(tag).getAnswer().equals("A")){
                        radioButtonA.setTextColor(Color.GREEN);
                    }
                    else if(questionList.get(tag).getAnswer().equals("B")){
                        radioButtonB.setTextColor(Color.GREEN);
                    }
                    else if(questionList.get(tag).getAnswer().equals("C")){
                        radioButtonC.setTextColor(Color.GREEN);
                    }
                    else if(questionList.get(tag).getAnswer().equals("D")){
                        radioButtonD.setTextColor(Color.GREEN);
                    }
                }

                break;
            case QUERY_ERROR:
                break;
        }
    }

    private void NextQuestion(){
        clearColor();
        review_radio_group.clearCheck();
        id.setText((tag+1)+".");
        num.setText((tag+1)+"/"+questionList.size());
        question.setText(questionList.get(tag).getQuestion());
        radioButtonA.setText(questionList.get(tag).getOptionA());
        radioButtonB.setText(questionList.get(tag).getOptionB());
        radioButtonC.setText(questionList.get(tag).getOptionC());
        radioButtonD.setText(questionList.get(tag).getOptionD());

        if(grade.getAnswers().size()>0){
            //防止tag 越界
            if(grade.getAnswers().size()>tag) {

                if(grade.getAnswers().get(tag).equals(questionList.get(tag).getAnswer())){
                    if(grade.getAnswers().get(tag).equals("A")){
                        radioButtonA.setTextColor(Color.GREEN);
                        radioButtonA.setChecked(true);
                    }
                    else if(grade.getAnswers().get(tag).equals("B")){
                        radioButtonB.setTextColor(Color.GREEN);
                        radioButtonB.setChecked(true);
                    }
                    else if(grade.getAnswers().get(tag).equals("C")){
                        radioButtonC.setTextColor(Color.GREEN);
                        radioButtonC.setChecked(true);
                    }
                    else if(grade.getAnswers().get(tag).equals("D")){
                        radioButtonD.setTextColor(Color.GREEN);
                        radioButtonD.setChecked(true);
                    }
                }
                else{
                    //错误答案
                    if(grade.getAnswers().get(tag).equals("A")){
                        radioButtonA.setTextColor(Color.RED);
                        radioButtonA.setChecked(true);
                    }
                    else if(grade.getAnswers().get(tag).equals("B")){
                        radioButtonB.setTextColor(Color.RED);
                        radioButtonB.setChecked(true);
                    }
                    else if(grade.getAnswers().get(tag).equals("C")){
                        radioButtonC.setTextColor(Color.RED);
                        radioButtonC.setChecked(true);
                    }
                    else if(grade.getAnswers().get(tag).equals("D")){
                        radioButtonD.setTextColor(Color.RED);
                        radioButtonD.setChecked(true);
                    }
                    //正确答案
                    if(questionList.get(tag).getAnswer().equals("A")){
                        radioButtonA.setTextColor(Color.GREEN);
                    }
                    else if(questionList.get(tag).getAnswer().equals("B")){
                        radioButtonB.setTextColor(Color.GREEN);
                    }
                    else if(questionList.get(tag).getAnswer().equals("C")){
                        radioButtonC.setTextColor(Color.GREEN);
                    }
                    else if(questionList.get(tag).getAnswer().equals("D")){
                        radioButtonD.setTextColor(Color.GREEN);
                    }
                }
            }
            else{
                //正确答案
                if(questionList.get(tag).getAnswer().equals("A")){
                    radioButtonA.setTextColor(Color.GREEN);
                }
                else if(questionList.get(tag).getAnswer().equals("B")){
                    radioButtonB.setTextColor(Color.GREEN);
                }
                else if(questionList.get(tag).getAnswer().equals("C")){
                    radioButtonC.setTextColor(Color.GREEN);
                }
                else if(questionList.get(tag).getAnswer().equals("D")){
                    radioButtonD.setTextColor(Color.GREEN);
                }
            }
        }
        else{

            //正确答案
            if(questionList.get(tag).getAnswer().equals("A")){
                radioButtonA.setTextColor(Color.GREEN);
            }
            else if(questionList.get(tag).getAnswer().equals("B")){
                radioButtonB.setTextColor(Color.GREEN);
            }
            else if(questionList.get(tag).getAnswer().equals("C")){
                radioButtonC.setTextColor(Color.GREEN);
            }
            else if(questionList.get(tag).getAnswer().equals("D")){
                radioButtonD.setTextColor(Color.GREEN);
            }
        }
    }

    private void clearColor(){
        radioButtonA.setTextColor(Color.BLACK);
        radioButtonB.setTextColor(Color.BLACK);
        radioButtonC.setTextColor(Color.BLACK);
        radioButtonD.setTextColor(Color.BLACK);

    }
}
