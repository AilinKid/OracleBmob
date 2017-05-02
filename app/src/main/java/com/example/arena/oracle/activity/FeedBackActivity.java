package com.example.arena.oracle.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.arena.oracle.R;
import com.example.arena.oracle.SystemTestSystem;
import com.example.arena.oracle.bean.Feedback;
import com.example.arena.oracle.bean.Student;


import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Arena on 2017/4/9.
 */
public class FeedBackActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText etFeedback;
    private Button btnSendFeedback;
    private ImageButton ibtnFeedbackBack;
    private Student myUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        //透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        //myUser = BmobUser.getCurrentUser(this,Student.class);
        myUser = SystemTestSystem.getStudent();
        etFeedback = (EditText)findViewById(R.id.etFeedBack);
        btnSendFeedback = (Button)findViewById(R.id.btnSendFeedback);
        ibtnFeedbackBack = (ImageButton)findViewById(R.id.ibtnFeedbackBack);

        btnSendFeedback.setOnClickListener(this);
        ibtnFeedbackBack.setOnClickListener(this);

    }


        @Override
        public void onClick (View v){

            if(v == btnSendFeedback){
                Feedback fb = new Feedback();
                fb.setContent(etFeedback.getText().toString());
                fb.setAuthor(myUser);
                fb.save(this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(FeedBackActivity.this,"发送成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(FeedBackActivity.this,"发送失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }else if(v == ibtnFeedbackBack){
                this.finish();
            }

        }
    }
