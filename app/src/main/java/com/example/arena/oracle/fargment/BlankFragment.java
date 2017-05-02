package com.example.arena.oracle.fargment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.arena.oracle.R;
import com.example.arena.oracle.SystemTestSystem;
import com.example.arena.oracle.Utils.Action;
import com.example.arena.oracle.Utils.BmobUtils;
import com.example.arena.oracle.Utils.HttpUtil;
import com.example.arena.oracle.activity.ExamActivity;
import com.example.arena.oracle.adapter.PaperListAdapter;
import com.example.arena.oracle.bean.Grade;
import com.example.arena.oracle.bean.Paper;

import org.json.JSONObject;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public class BlankFragment extends Fragment {

    private ListView paperlist;
    private List<Paper> papers = new ArrayList<Paper>();
    private List<Grade> grades = new ArrayList<Grade>();
    private PaperListAdapter adapter;
    private Context context;

    public BlankFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        EventBus.getDefault().register(this);
        BmobUtils.downloadPaperList(getContext());
        //httpRequest();
        //httpPost();
        //application中只能做一些初始化的操作，不能数据存储，很容易丢失
        //BmobUtils.downloadGradeList(getContext(), SystemTestSystem.getStudent().getUsername());
        BmobUtils.downloadGradeList(getContext(), BmobUser.getCurrentUser(getContext()).getUsername());
        Log.d("username", SystemTestSystem.getStudent().getUsername());

        //findViewById
        paperlist = (ListView)view.findViewById(R.id.lv_paper);
        PaperListAdapter paperListAdapter = new PaperListAdapter(getContext(), papers);
        paperlist.setAdapter(paperListAdapter);


        paperlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //判断网络状态
                if(((ConnectivityManager)getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE))
                        .getActiveNetworkInfo()==null){
                    Toast.makeText(getContext(), "网络异常", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(!papers.get(position).isFinishState()){
                        //未完成
                        Intent intent = new Intent(getActivity(), ExamActivity.class);
                        //只能传基本数据类型
                        intent.putExtra("paperName", papers.get(position).getPaperName());
                        startActivityForResult(intent, 0);
                    }
                    else{
                        //已完成
                    }
                }
            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==0){
            if(resultCode==1){
                //得到序列化参数
                Grade grade = (Grade)data.getSerializableExtra("data");
                BmobUtils.gradeList.add(grade);
                ((PaperListAdapter) paperlist.getAdapter()).notifyDataSetChanged();

            }
        }
    }

    @Subscriber
    private void onReceiveActionEvent(Action action){
       switch(action){
           case DOWNLOAD_PAPER_LIST:
               Log.d("student_paper_list", "onReceiveActionEvent: 获取试卷列表成功");
               papers = BmobUtils.paperList;
               ((PaperListAdapter) paperlist.getAdapter()).refresh1((ArrayList<Paper>) papers);
               break;
           case DOWNLOAD_GRADE_LIST:
               Log.d("student_grade_list", "onReceiveActionEvent: 获取成绩成功");
               grades=BmobUtils.gradeList;
               ((PaperListAdapter) paperlist.getAdapter()).refresh2((ArrayList<Grade>) grades);
               break;
           case QUERY_ERROR:
               Log.d("query_error", "onReceiveActionEvent: 获取试卷列表失败");
       }
    }

    @Override
    public void onPause() {
        super.onPause();
        papers = PaperListAdapter.paperData;
        for(int i=0; i<papers.size(); i++){
            //存入数据库，避免二次查询
            ;
        }
    }


    /*
    以下代码和httpUtil 是用于测试新的后台用的，bmob云后台可不用管
     */
    private void httpRequest(){
        new Thread(){
            public void run(){
                Message msg = new Message();
                msg.what=1;
                String url = "http://10.18.49.253:8080/studentClub/AndroidLogin" ;
                String result = HttpUtil.get(url);
                if(result==null){
                    result="";
                }
                msg.obj = result;
                handler.sendMessage(msg);
            }
        }.start();
    }

    private void httpPost(){
        new Thread(){
            public void run(){
                Message msg = new Message();
                String url = "http://10.18.49.253:8080/studentClub/AndroidLogin";
                String json = "{\"name\":\"arena\"}";
                msg.what=0;
                String result = HttpUtil.post(url, json);
                if(result==null){
                    result="";
                }
                msg.obj = result;
                handler.sendMessage(msg);
            }
        }.start();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Log.d("json", (String)msg.obj);
                    jsonAnalyse((String)msg.obj);
                    break;
                case 0:
                    Log.d("jsonpost",(String) msg.obj);
                    Toast.makeText(getContext(), "post success", Toast.LENGTH_SHORT);
            }
        }
    };


    private void jsonAnalyse(String json){
        try{
            JSONObject weather  = new JSONObject(json);
            //JSONObject detail = (JSONObject) weather.get("");
            //String city = (String) detail.get("city");
            //String cityid = (String)detail.get("cityid");
            String code = (String) weather.get("code");
            //Log.d("jsonCity", city);
            //Log.d("jsonCityid", cityid);
            Log.d("Json","code="+code);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


}
