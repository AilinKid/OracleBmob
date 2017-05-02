package com.example.arena.oracle.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arena.oracle.R;
import com.example.arena.oracle.adapter.DemoParallaxAdapter;
import com.example.arena.oracle.fargment.DemoParallaxFragment;
import com.example.arena.oracle.wedget.ParallaxPagerTransformer;


/**
 * Updated by Arena on 2017/4/2.
 */

public class GuideActivity extends AppCompatActivity implements View.OnClickListener{


    ViewPager mPager;
    DemoParallaxAdapter mAdapter;
    private TextView tvLogin;
    private TextView tvRegister;

    /**
     * Called when the activity is firstly created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //登录注册界面
        setContentView(R.layout.activity_guide);
        //透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //沉浸式状态栏
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        mPager = (ViewPager) findViewById(R.id.pager);
        tvLogin = (TextView) findViewById(R.id.tvLogin);
        tvRegister = (TextView) findViewById(R.id.tvRegister);
        mPager.setBackgroundColor(0xFF000000);

        //id传入对viewpager变换
        ParallaxPagerTransformer pt = new ParallaxPagerTransformer((R.id.image));
        //pt.setBorder(20);
        //pt.setSpeed(0.2f);
        mPager.setPageTransformer(false, pt);

        //获取viewpager的adapter
        mAdapter = new DemoParallaxAdapter(getSupportFragmentManager());
        mAdapter.setPager(mPager); //only for this transformer

        Bundle bNina = new Bundle();
        bNina.putInt("image", R.mipmap.intro_1);
        bNina.putString("name", "在线考试，\n与你随时随地");
        DemoParallaxFragment pfNina = new DemoParallaxFragment();
        pfNina.setArguments(bNina);

        Bundle bNiju = new Bundle();
        bNiju.putInt("image", R.mipmap.intro_2);
        bNiju.putString("name", "轻松做题，\n解决不懂的困惑");
        DemoParallaxFragment pfNiju = new DemoParallaxFragment();
        pfNiju.setArguments(bNiju);

        Bundle bYuki = new Bundle();
        bYuki.putInt("image", R.mipmap.intro_3);
        bYuki.putString("name", "不懂就问，\n亲近你我的距离");
        DemoParallaxFragment pfYuki = new DemoParallaxFragment();
        pfYuki.setArguments(bYuki);

        //将fragment添加到viewpager的adapter
        mAdapter.add(pfNina);
        mAdapter.add(pfNiju);
        mAdapter.add(pfYuki);
        mPager.setAdapter(mAdapter);

        tvLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v == tvLogin){
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();

        }else if(v == tvRegister){
            //Toast.makeText(this, "正去注册", Toast.LENGTH_SHORT).show();
            Log.d("GuideActivity", "正去注册");
            Intent intent = new Intent(this,RegisterActivity.class);
            startActivity(intent);
        }
    }
}
