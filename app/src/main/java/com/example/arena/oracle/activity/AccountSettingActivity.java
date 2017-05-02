package com.example.arena.oracle.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.arena.oracle.R;
import com.example.arena.oracle.SystemTestSystem;
import com.example.arena.oracle.bean.Student;
import com.facebook.drawee.backends.pipeline.Fresco;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

/*
Created by Arena in 2017/4/17
 */

public class AccountSettingActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText etOriginPassword;
    private EditText etNewPassword;
    private EditText etRePassword;
    private Button btnConfirm;
    private Student currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountsetting);
        currentUser = BmobUser.getCurrentUser(this, Student.class);

        etOriginPassword = (EditText) findViewById(R.id.etOriginPassword);
        etNewPassword = (EditText) findViewById(R.id.etNewPassword);
        etRePassword = (EditText) findViewById(R.id.etRePassword);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);

        btnConfirm.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if (v == btnConfirm) {
            String OriginPassword;
            String NewPassword;
            String RePassword;
            OriginPassword = etOriginPassword.getText().toString();
            NewPassword = etNewPassword.getText().toString();
            RePassword = etRePassword.getText().toString();
            Log.d("密码", OriginPassword+","+NewPassword+","+RePassword);
            if (!NewPassword.equals(RePassword)) {
                Toast.makeText(this, "两次输入的密码不一样，请重新输入", Toast.LENGTH_SHORT).show();
                return;
            }
            if (NewPassword.isEmpty()) {
                Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            BmobUser.updateCurrentUserPassword(this, OriginPassword, NewPassword, new UpdateListener() {

                @Override
                public void onSuccess() {
                    // TODO Auto-generated method stub
                    Log.i("smile", "密码修改成功，请重新登录");
                    BmobUser.logOut(AccountSettingActivity.this);   //清除缓存用户对象
                    SystemTestSystem.setStudent(null);
                    Toast.makeText(AccountSettingActivity.this, "密码修改成功，请重新登录", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AccountSettingActivity.this, GuideActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onFailure(int code, String msg) {
                    // TODO Auto-generated method stub
                    Log.i("smile", "密码修改失败：" + msg + "(" + code + ")");
                }
            });
            //Log.i("smile", "密码修改成功，请重新登录");
            //BmobUser.logOut(AccountSettingActivity.this);   //清除缓存用户对象
            //Intent intent = new Intent(AccountSettingActivity.this, GuideActivity.class);
            //startActivity(intent);
        }

    }
}
