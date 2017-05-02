package com.example.arena.oracle.fargment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.arena.oracle.R;
import com.example.arena.oracle.SystemTestSystem;
import com.example.arena.oracle.activity.AccountSettingActivity;
import com.example.arena.oracle.activity.EditInfoActivity;
import com.example.arena.oracle.activity.FeedBackActivity;
import com.example.arena.oracle.activity.GuideActivity;
import com.example.arena.oracle.bean.Student;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;


import cn.bmob.v3.BmobUser;


/**
 * Created by Arena on 2017/3/29.
 */
public class MoreFragment extends Fragment implements View.OnClickListener {

    private Context context;
    private ImageView ivMoreMyPost;
    private ImageView ivMoreReport;
    private ImageButton rlMoreEditInfo;
    private RelativeLayout rlExit;
    private RelativeLayout rlAccountSetting;
    private RelativeLayout rlFeedback;
    private RelativeLayout rlMyLikes;
    private RelativeLayout rlMyPosts;
    private TextView tvMoreName;
    private SimpleDraweeView sdMoreAvatar;

    private Student currentUser= SystemTestSystem.getStudent();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        Fresco.initialize(context);
        //myUser = BmobUser.getCurrentUser(context, MyUser.class);
        //直接从applicattion中提取用户资料
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        ivMoreMyPost = (ImageView) view.findViewById(R.id.ivMoreMyPost);
        ivMoreReport = (ImageView) view.findViewById(R.id.ivMoreReport);

        rlMoreEditInfo = (ImageButton) view.findViewById(R.id.rlMoreEditInfo);
        rlMyLikes = (RelativeLayout) view.findViewById(R.id.rlMyLikes);
        rlMyPosts = (RelativeLayout) view.findViewById(R.id.rlMyPosts);
        rlFeedback = (RelativeLayout) view.findViewById(R.id.rlFeedback);

        rlAccountSetting = (RelativeLayout) view.findViewById(R.id.rlAccountSetting);
        rlExit = (RelativeLayout) view.findViewById(R.id.rlExit);

        tvMoreName = (TextView) view.findViewById(R.id.tvMoreName);
        sdMoreAvatar = (SimpleDraweeView) view.findViewById(R.id.sdMoreAvatar);

        ivMoreMyPost.setColorFilter(0x60000000, PorterDuff.Mode.SRC_IN);
        ivMoreReport.setColorFilter(0x60000000, PorterDuff.Mode.SRC_IN);
        //if(.getAvatar() != null) {
        //    Uri imgUri = Uri.parse(myUser.getAvatar());
        //    sdMoreAvatar.setImageURI(imgUri);
        //}
        tvMoreName.setText("亲爱的"+currentUser.getName()+"，您好。");

        rlMoreEditInfo.setOnClickListener(this);
        rlMyLikes.setOnClickListener(this);
        rlMyPosts.setOnClickListener(this);
        rlFeedback.setOnClickListener(this);
        rlExit.setOnClickListener(this);
        rlAccountSetting.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == rlMoreEditInfo) {
            Intent intent = new Intent(context, EditInfoActivity.class);
            startActivity(intent);

        }
        if (v == rlMyLikes) {
            //Intent intent = new Intent(context, MyLikesActivity.class);
            //startActivity(intent);
        }
        if (v == rlMyPosts) {
            //Intent intent = new Intent(context, MyPostsActivity.class);
            //startActivity(intent);
        }
        if (v == rlExit) {
            new AlertDialog.Builder(context)
                    .setTitle("提示")
                    .setMessage("是否退出当前账户？")
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            BmobUser.logOut(context);   //清除缓存用户对象
                            SystemTestSystem.setStudent(null);
                            Intent intent = new Intent(context, GuideActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        }
        if(v == rlFeedback){
            Intent intent = new Intent(context, FeedBackActivity.class);
            startActivity(intent);
        }
        if(v == rlAccountSetting){
            Intent intent = new Intent(context, AccountSettingActivity.class);
            startActivity(intent);
        }

    }
}
