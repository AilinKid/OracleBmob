package com.example.arena.oracle.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.arena.oracle.R;
import com.example.arena.oracle.SystemTestSystem;
import com.example.arena.oracle.Utils.HttpUtil;
import com.example.arena.oracle.bean.Paper;
import com.example.arena.oracle.bean.Student;

import org.json.JSONObject;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;


/**
 * Updated by Arena on 2017/4/2.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etNumber;
    private EditText etPassword;

    private Button btnLogin;
    private ImageView ivUserIcon;
    private ImageView ivLock;
    private TextView tvForgetPassword;
    private ProgressDialog waitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        etPassword = (EditText) findViewById(R.id.etPassWord);
        etNumber = (EditText) findViewById(R.id.etNumber);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        ivUserIcon = (ImageView) findViewById(R.id.ivUserIcon);
        ivLock = (ImageView) findViewById(R.id.ivLock);
        tvForgetPassword = (TextView) findViewById(R.id.tvForgetPassword);

        //ivUserIcon.setColorFilter(0x60000000, PorterDuff.Mode.SRC_IN);
        //ivLock.setColorFilter(0x60000000, PorterDuff.Mode.SRC_IN);
        btnLogin.setOnClickListener(this);
        tvForgetPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnLogin) {
            //用户登录
            final String username = etNumber.getText().toString();
            final String password = etPassword.getText().toString();
            final String type = "1";
            Log.d("登录",username+password);

            waitDialog = new ProgressDialog(this);
            waitDialog.setMessage("登录中");
            waitDialog.setCancelable(false);
            waitDialog.show();

            //httpRequest_forLogin(username, password, type);




            BmobUser.loginByAccount(this, username, password, new LogInListener<Student>() {


                @Override
                public void done(Student student, BmobException e) {
                    if(student != null){
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        Log.d("LoginActivity", "登录成功");
                        SystemTestSystem.setStudent(student);
                        if(student.getIdentity()==1){    //学生登录
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                        if(student.getIdentity()==2){    //教师登录
                            Intent intent = new Intent(LoginActivity.this, Main2Activity.class);
                            startActivity(intent);
                        }
                        waitDialog.dismiss();
                        finish();
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "账号或者密码错误", Toast.LENGTH_SHORT).show();
                        waitDialog.dismiss();
                    }
                }
            });


        } else if (v == tvForgetPassword) {


        }
    }



}
