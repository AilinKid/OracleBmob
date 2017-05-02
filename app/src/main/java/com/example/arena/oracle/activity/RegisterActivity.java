package com.example.arena.oracle.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.arena.oracle.R;
import com.example.arena.oracle.bean.Student;
import com.example.arena.oracle.permission.PermissionListener;
import com.example.arena.oracle.permission.PermissionManager;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.listener.SaveListener;


/**
 * Updated by Arena on 2017/4/2.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int REQUEST_CODE_PICK_IMAGE = 1023;
    private static final int REQUEST_CODE_CAPTURE_CAMEIA = 1022;
    private SimpleDraweeView sdRegAvatar;

    private List<String> data_list;
    private PermissionManager permissionManager;
    private ArrayAdapter<String> arr_adapter;
    private ScrollView svRegister;


    private EditText etUsername;
    private EditText etClass;
    private EditText etPassword;
    private EditText etNumber;
    private Spinner spIdentity;



    private Button btnRegister;
    private ImageButton ibtnRegBack;
    private String avatarUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_register);

        //透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        sdRegAvatar = (SimpleDraweeView) findViewById(R.id.sdRegAvatar);
        spIdentity = (Spinner) findViewById(R.id.spIdentity);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etRegPassWord);
        etClass = (EditText) findViewById(R.id.etClass);
        etNumber = (EditText) findViewById(R.id.etNumber);

        svRegister = (ScrollView) findViewById(R.id.svRegister);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        ibtnRegBack = (ImageButton) findViewById(R.id.ibtnRegBack);

        //数据
        data_list = new ArrayList<String>();
        data_list.add("老师");
        data_list.add("学生");

        //适配器
        arr_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spIdentity.setAdapter(arr_adapter);


        btnRegister.setOnClickListener(this);
        sdRegAvatar.setOnClickListener(this);
        ibtnRegBack.setOnClickListener(this);
    }


    /**
     * 根据Uri获取图片文件的绝对路径
     */
    public String getRealFilePath(final Uri uri) {
        if (null == uri) {
            return null;
        }

        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = getContentResolver().query(uri,
                    new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_PICK_IMAGE:
                permissionManager.onPermissionResult(permissions, grantResults);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            Uri uri = data.getData();
            sdRegAvatar.setImageURI(uri);
            //异步将图片上传 something to do

        }

    }

    @Override
    public void onClick(View v) {
        if (v == sdRegAvatar) {
            Log.v("red", "fffff");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                permissionManager = PermissionManager.with(RegisterActivity.this)
                        //添加权限请求码
                        .addRequestCode(REQUEST_CODE_PICK_IMAGE)
                        //设置权限，可以添加多个权限
                        .permissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                        //设置权限监听器
                        .setPermissionsListener(new PermissionListener() {

                            @Override
                            public void onGranted() {
                                //当权限被授予时调用
                                Toast.makeText(RegisterActivity.this, "Camera Permission granted", Toast.LENGTH_LONG).show();
                                // 打开系统相册
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");// 相片类型
                                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
                            }

                            @Override
                            public void onDenied() {
                                //用户拒绝该权限时调用
                                Toast.makeText(RegisterActivity.this, "ReadSDCard Permission denied", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onShowRationale(String[] permissions) {
                                //当用户拒绝某权限时并点击`不再提醒`的按钮时，下次应用再请求该权限时，需要给出合适的响应（比如,给个展示对话框来解释应用为什么需要该权限）
                                Snackbar.make(svRegister, "需要读取内存卡权限去拍照", Snackbar.LENGTH_INDEFINITE)
                                        .setAction("ok", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                //必须调用该`setIsPositive(true)`方法
                                                permissionManager.setIsPositive(true);
                                                permissionManager.request();
                                            }
                                        }).show();
                            }
                        })
                        //请求权限
                        .request();
            } else {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");// 相片类型
                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
            }
        } else if (v == btnRegister) {
            String name = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            String mclass = etClass.getText().toString();
            String number = etNumber.getText().toString();

            if (name.equals("") || name == null) {
                Toast.makeText(RegisterActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.equals("") || password == null) {
                Toast.makeText(RegisterActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }

            final ProgressDialog waitDialog = new ProgressDialog(this);
            waitDialog.setMessage("注册中...");
            waitDialog.setCancelable(false);
            waitDialog.show();

            Student student = new Student();
            if(spIdentity.getSelectedItem().toString().equals("老师")){
                student.setIdentity(2);
            }
            else{
                student.setIdentity(1);
            }
            student.setUsername(number);
            student.setPassword(password);
            student.setmClass(mclass);
            student.setName(name);


            student.signUp(this, new SaveListener() {
                @Override
                public void onSuccess() {
                    waitDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFailure(int i, String s) {
                    waitDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "注册失败",Toast.LENGTH_SHORT).show();
                }
            });
            Log.d("注册", name+student.getmClass()+student.getIdentity()+password);
            //something to do 实体化对象， 邮箱验证什么的
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);

        }else if(v == ibtnRegBack){
            this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
