package com.example.arena.oracle.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.arena.oracle.R;
import com.example.arena.oracle.bean.Student;
import com.example.arena.oracle.permission.PermissionListener;
import com.example.arena.oracle.permission.PermissionManager;
import com.facebook.drawee.view.SimpleDraweeView;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by Arena on 2017/4/11.
 */
public class EditInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_PICK_IMAGE = 1023;
    private static final int REQUEST_CODE_CAPTURE_CAMEIA = 1022;

    private ImageView ivEditAvatar;
    private ImageView iv_change_name;
    private ImageView iv_change_password;
    private ImageView iv_change_number;
    private ImageView iv_change_class;
    private ImageView iv_change_type;

    private EditText et_change_name;
    private EditText et_change_password;
    private EditText et_change_number;
    private EditText et_change_class;
    private Spinner sp_change_Indentity;

    private SimpleDraweeView sdEditAvatar;

    private ImageButton ibtnEditInfoBack;
    private ImageButton ibtnEditInfoDone;
    private PermissionManager permissionManager;

    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;
    private Student myUser;
    private ScrollView svEditInfo;
    private String avatarUri;
    private int indentity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        //透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        myUser = BmobUser.getCurrentUser(this, Student.class);

        //find ImageView
        ivEditAvatar = (ImageView) findViewById(R.id.ivEditAvatar);
        iv_change_type = (ImageView) findViewById(R.id.iv_change_type);
        iv_change_name = (ImageView) findViewById(R.id.iv_change_name);
        iv_change_password = (ImageView) findViewById(R.id.iv_change_password);
        iv_change_class = (ImageView) findViewById(R.id.iv_change_class);
        iv_change_number = (ImageView) findViewById(R.id.iv_change_number);
        svEditInfo = (ScrollView) findViewById(R.id.svEditInfo);

        //find EditText
        et_change_name = (EditText) findViewById(R.id.et_change_name);
        et_change_password = (EditText)findViewById(R.id.et_change_password);
        et_change_class = (EditText) findViewById(R.id.et_change_class);
        et_change_number = (EditText) findViewById(R.id.et_change_number);

        //find others
        sdEditAvatar = (SimpleDraweeView) findViewById(R.id.sdEditAvatar);
        ibtnEditInfoBack = (ImageButton) findViewById(R.id.ibtnEditInfoBack);
        ibtnEditInfoDone = (ImageButton) findViewById(R.id.ibtnEditInfoDone);

        sp_change_Indentity = (Spinner) findViewById(R.id.spEditIndentity);

        iv_change_name.setColorFilter(0x60000000, PorterDuff.Mode.SRC_IN);
        ivEditAvatar.setColorFilter(0x60000000, PorterDuff.Mode.SRC_IN);
        iv_change_class.setColorFilter(0x60000000, PorterDuff.Mode.SRC_IN);
        iv_change_number.setColorFilter(0x60000000, PorterDuff.Mode.SRC_IN);
        iv_change_password.setColorFilter(0x60000000, PorterDuff.Mode.SRC_IN);
        iv_change_type.setColorFilter(0x60000000, PorterDuff.Mode.SRC_IN);

        data_list = new ArrayList<String>();
        data_list.add("老师");
        data_list.add("学生");

        //适配器
        arr_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_change_Indentity.setAdapter(arr_adapter);

//        if (myUser.getAvatar() != null) {
//            Uri imageUri = Uri.parse(myUser.getAvatar());
//            avatarUri = myUser.getAvatar();
//            sdEditAvatar.setImageURI(imageUri);
//        }
        et_change_name.setText(myUser.getName());
        et_change_password.setText("");
        et_change_class.setText(myUser.getmClass());
        et_change_number.setText(myUser.getUsername());
        if(myUser.getIdentity()==2){
            sp_change_Indentity.setSelection(0);
        }
        else{
            sp_change_Indentity.setSelection(1);
        }

        ibtnEditInfoBack.setOnClickListener(this);
        ibtnEditInfoDone.setOnClickListener(this);
        sdEditAvatar.setOnClickListener(this);
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
            final BmobFile bmobFile = new BmobFile(new File(getRealFilePath(uri)));
            bmobFile.uploadblock(this, new UploadFileListener() {
                @Override
                public void onSuccess() {
                    //bmobFile.getFileUrl(context)--返回的上传文件的完整地址
                    Toast.makeText(EditInfoActivity.this, "上传文件成功:" + bmobFile.getFileUrl(EditInfoActivity.this), Toast.LENGTH_SHORT).show();
                    avatarUri = bmobFile.getFileUrl(EditInfoActivity.this);
                    Uri imageUri = Uri.parse(avatarUri);
                    sdEditAvatar.setImageURI(imageUri);
                }

                @Override
                public void onProgress(Integer value) {
                    // 返回的上传进度（百分比）
                }

                @Override
                public void onFailure(int code, String msg) {
                    Toast.makeText(EditInfoActivity.this, "上传文件失败：" + msg, Toast.LENGTH_SHORT).show();
                }
            });
        } else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {
            //insertBitmap(mCurrentPhotoFile.getAbsolutePath());
        }
    }

    @Override
    public void onClick(View v) {
        if (v == ibtnEditInfoBack) {
            this.finish();
        } else if (v == ibtnEditInfoDone) {
            if (et_change_name.getText().toString() == null || et_change_name.getText().toString().equals("")) {
                Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if (sp_change_Indentity.getSelectedItem().toString().equals("老师")) {
                indentity = 2;
            } else {
                indentity = 1;
            }

            Student editMyUser = new Student();
            //editMyUser.setAvatar(avatarUri);
            editMyUser.setmClass(et_change_class.getText().toString());
            editMyUser.setName(et_change_name.getText().toString());
            editMyUser.setUsername(et_change_number.getText().toString());
            editMyUser.setIdentity(indentity);


            editMyUser.update(this, myUser.getObjectId(), new UpdateListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(EditInfoActivity.this, "更新用户信息成功", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(int i, String s) {
                    Toast.makeText(EditInfoActivity.this, "更新用户信息失败" + s, Toast.LENGTH_SHORT).show();
                }
            });

        } else if (v == sdEditAvatar) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                permissionManager = PermissionManager.with(EditInfoActivity.this)
                        //添加权限请求码
                        .addRequestCode(REQUEST_CODE_PICK_IMAGE)
                        //设置权限，可以添加多个权限
                        .permissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                        //设置权限监听器
                        .setPermissionsListener(new PermissionListener() {

                            @Override
                            public void onGranted() {
                                //当权限被授予时调用
                                Toast.makeText(EditInfoActivity.this, "Camera Permission granted", Toast.LENGTH_LONG).show();
                                // 打开系统相册
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");// 相片类型
                                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
                            }

                            @Override
                            public void onDenied() {
                                //用户拒绝该权限时调用
                                Toast.makeText(EditInfoActivity.this, "ReadSDCard Permission denied", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onShowRationale(String[] permissions) {
                                //当用户拒绝某权限时并点击`不再提醒`的按钮时，下次应用再请求该权限时，需要给出合适的响应（比如,给个展示对话框来解释应用为什么需要该权限）
                                Snackbar.make(svEditInfo, "需要读取内存卡权限去拍照", Snackbar.LENGTH_INDEFINITE)
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

        }
    }
}
