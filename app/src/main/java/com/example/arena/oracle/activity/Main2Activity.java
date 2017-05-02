package com.example.arena.oracle.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.arena.oracle.R;
import com.example.arena.oracle.Utils.Action;
import com.example.arena.oracle.Utils.BmobUtils;
import com.example.arena.oracle.adapter.TPaperListAdapter;
import com.example.arena.oracle.bean.Paper;
import com.example.arena.oracle.fargment.AddPaperFragment;
import com.example.arena.oracle.fargment.BlankFragment;
import com.example.arena.oracle.fargment.MoreFragment;
import com.example.arena.oracle.fargment.Teacher_PaperList_Fragment;
import com.facebook.drawee.backends.pipeline.Fresco;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.List;

import static com.example.arena.oracle.fargment.BlankFragment.*;


public class Main2Activity extends AppCompatActivity implements View.OnClickListener{


    private FragmentManager fManager;
    private Teacher_PaperList_Fragment T_P_Fragment;
    private AddPaperFragment A_P_Fragment;
    private MoreFragment moreFragment;
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
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
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
        T_P_Fragment = new Teacher_PaperList_Fragment();
        fManager = getSupportFragmentManager();
        fManager.beginTransaction().add(R.id.flContent, T_P_Fragment).commit();
        currentFragment = T_P_Fragment;

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
            if (T_P_Fragment == null) {
                T_P_Fragment = new Teacher_PaperList_Fragment();
            }
            else{
                fManager.beginTransaction().remove(T_P_Fragment);
                T_P_Fragment = new Teacher_PaperList_Fragment();
            }
            addOrShowFragment(fManager.beginTransaction(), T_P_Fragment);

        } else if (v == rlNotification) {
            ivNotification.setColorFilter(0xff0f88eb, PorterDuff.Mode.SRC_IN);
            ivDiscover.setColorFilter(0xffd4d4d4, PorterDuff.Mode.SRC_IN);
            ivMessage.setColorFilter(0xffd4d4d4, PorterDuff.Mode.SRC_IN);
            ivMore.setColorFilter(0xffd4d4d4, PorterDuff.Mode.SRC_IN);
            //我要每次都能更新fragment，就不重用了
            if(A_P_Fragment==null){
                A_P_Fragment = new AddPaperFragment();
            }
            else{
                fManager.beginTransaction().remove(A_P_Fragment);
                A_P_Fragment = new AddPaperFragment();
            }
            addOrShowFragment(fManager.beginTransaction(), A_P_Fragment);

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


    public void refreshTolist(){
        ivDiscover.setColorFilter(0xff0f88eb, PorterDuff.Mode.SRC_IN);
        ivNotification.setColorFilter(0xffd4d4d4, PorterDuff.Mode.SRC_IN);
        ivMessage.setColorFilter(0xffd4d4d4, PorterDuff.Mode.SRC_IN);
        ivMore.setColorFilter(0xffd4d4d4, PorterDuff.Mode.SRC_IN);
        if (T_P_Fragment == null) {
            T_P_Fragment = new Teacher_PaperList_Fragment();
        }
        else{
            fManager.beginTransaction().remove(T_P_Fragment);
            T_P_Fragment = new Teacher_PaperList_Fragment();
        }
        addOrShowFragment(fManager.beginTransaction(), T_P_Fragment);
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

    @Subscriber
    private void onReceiveActionEvent(Action action) {
        Log.d("received", "received");
    }


    //捕捉手机回退
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            ExitDialog(Main2Activity.this).show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private Dialog ExitDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("系统信息");
        builder.setMessage("确定要退出程序吗?");
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                    }
                });
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
        return builder.create();
    }
}
