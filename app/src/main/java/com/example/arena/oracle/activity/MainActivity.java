package com.example.arena.oracle.activity;

import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.arena.oracle.R;
import com.example.arena.oracle.fargment.BlankFragment;
import com.example.arena.oracle.fargment.Grade_History_Fragment;
import com.example.arena.oracle.fargment.MoreFragment;
import com.example.arena.oracle.fargment.Teacher_PaperList_Fragment;


public class MainActivity extends AppCompatActivity implements View.OnClickListener
{


    private FragmentManager fManager;
    private BlankFragment blankFragment;
    private MoreFragment moreFragment;
    private Grade_History_Fragment G_H_fragment;
    private Fragment currentFragment;

    private ImageView ivDiscover;
    private ImageView ivNotification;
    private ImageView ivMessage;
    private ImageView ivMore;

    private RelativeLayout rlDiscover;
    private RelativeLayout rlNotification;
    private RelativeLayout rlMessage;
    private RelativeLayout rlMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        //imageView
        ivDiscover = (ImageView) findViewById(R.id.ivDiscover);
        ivNotification = (ImageView) findViewById(R.id.ivNotification);
        ivMessage = (ImageView) findViewById(R.id.ivMessage);
        ivMore = (ImageView) findViewById(R.id.ivMore);
        //relativeLayout
        rlDiscover = (RelativeLayout) findViewById(R.id.rlDiscover);
        rlNotification = (RelativeLayout) findViewById(R.id.rlNotification);
        rlMessage = (RelativeLayout) findViewById(R.id.rlMessage);
        rlMore = (RelativeLayout) findViewById(R.id.rlMore);

        //ImageView 的颜色滤镜 滤镜自然是上层
        ivDiscover.setColorFilter(0xff0f88eb, PorterDuff.Mode.SRC_IN);
        ivNotification.setColorFilter(0xffd4d4d4, PorterDuff.Mode.SRC_IN);
        ivMessage.setColorFilter(0xffd4d4d4, PorterDuff.Mode.SRC_IN);
        ivMore.setColorFilter(0xffd4d4d4, PorterDuff.Mode.SRC_IN);

        //初始生成discover fragment，并记录
        blankFragment = new BlankFragment();
        fManager = getSupportFragmentManager();
        fManager.beginTransaction().add(R.id.flContent, blankFragment).commit();
        currentFragment = blankFragment;

        rlDiscover.setOnClickListener(this);
        rlNotification.setOnClickListener(this);
        rlMessage.setOnClickListener(this);
        rlMore.setOnClickListener(this);

    }

    public void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        //Log.v("LH", "onSaveInstanceState"+outState);
        //super.onSaveInstanceState(outState);   //将这一行注释掉，阻止activity保存fragment的状态
    }


    @Override
    public void onClick(View v) {
        if (v == rlDiscover) {
            ivDiscover.setColorFilter(0xff0f88eb, PorterDuff.Mode.SRC_IN);
            ivNotification.setColorFilter(0xffd4d4d4, PorterDuff.Mode.SRC_IN);
            ivMessage.setColorFilter(0xffd4d4d4, PorterDuff.Mode.SRC_IN);
            ivMore.setColorFilter(0xffd4d4d4, PorterDuff.Mode.SRC_IN);
            if (blankFragment == null) {
                blankFragment = new BlankFragment();
            }
            else{
                fManager.beginTransaction().remove(blankFragment);
                blankFragment = new BlankFragment();
            }
            addOrShowFragment(fManager.beginTransaction(), blankFragment);
        } else if (v == rlNotification) {
            ivNotification.setColorFilter(0xff0f88eb, PorterDuff.Mode.SRC_IN);
            ivDiscover.setColorFilter(0xffd4d4d4, PorterDuff.Mode.SRC_IN);
            ivMessage.setColorFilter(0xffd4d4d4, PorterDuff.Mode.SRC_IN);
            ivMore.setColorFilter(0xffd4d4d4, PorterDuff.Mode.SRC_IN);
            if(G_H_fragment==null){
                G_H_fragment = new Grade_History_Fragment();
            }
            else{
                //总是刷新
                fManager.beginTransaction().remove(G_H_fragment);
                G_H_fragment = new Grade_History_Fragment();
            }
            addOrShowFragment(fManager.beginTransaction(), G_H_fragment);

        } else if (v == rlMessage) {
            ivDiscover.setColorFilter(0xffd4d4d4, PorterDuff.Mode.SRC_IN);
            ivNotification.setColorFilter(0xffd4d4d4, PorterDuff.Mode.SRC_IN);
            ivMessage.setColorFilter(0xff0f88eb, PorterDuff.Mode.SRC_IN);
            ivMore.setColorFilter(0xffd4d4d4, PorterDuff.Mode.SRC_IN);

        } else if (v == rlMore) {
            ivDiscover.setColorFilter(0xffd4d4d4, PorterDuff.Mode.SRC_IN);
            ivNotification.setColorFilter(0xffd4d4d4, PorterDuff.Mode.SRC_IN);
            ivMessage.setColorFilter(0xffd4d4d4, PorterDuff.Mode.SRC_IN);
            ivMore.setColorFilter(0xff0f88eb, PorterDuff.Mode.SRC_IN);
            if(moreFragment == null){
                moreFragment = new MoreFragment();
            }
            addOrShowFragment(fManager.beginTransaction(),moreFragment);
        }
    }

    private void addOrShowFragment(FragmentTransaction transaction,
                                   Fragment fragment) {
        if (currentFragment == fragment)
            return;

        if (!fragment.isAdded()) { // 如果当前fragment未被添加，则添加到Fragment管理器中
            transaction.hide(currentFragment)
                    .add(R.id.flContent, fragment).commit();    //隐藏当下的fragment，创建新fragment
        } else {
            transaction.hide(currentFragment).show(fragment).commit();   //隐藏当下的fragment，显示对应的fragment
        }
        currentFragment = fragment;    //相当于当期fragment标记
    }

}
