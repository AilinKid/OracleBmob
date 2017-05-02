package com.example.arena.oracle.fargment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arena.oracle.R;
import com.example.arena.oracle.Utils.Action;
import com.example.arena.oracle.Utils.BmobUtils;
import com.example.arena.oracle.activity.ReviewActivity;
import com.example.arena.oracle.adapter.GradeListAdapter;
import com.example.arena.oracle.bean.Grade;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import cn.bmob.v3.BmobUser;


public class Grade_History_Fragment extends Fragment {

    private List<Grade> gradeList = new ArrayList<Grade>();
    private ListView listView;



    public Grade_History_Fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        EventBus.getDefault().register(this);
        View view =  inflater.inflate(R.layout.fragment_grade__history_, container, false);
        listView = (ListView)view.findViewById(R.id.lv_history);
        BmobUtils.downloadGradeList(getContext(), BmobUser.getCurrentUser(getContext()).getUsername());
        GradeListAdapter adapter = new GradeListAdapter(getContext(), gradeList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(((ConnectivityManager)getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE))
                        .getActiveNetworkInfo()==null){
                    Toast.makeText(getContext(), "网络异常", Toast.LENGTH_SHORT).show();
                }
                else {
                    //历史回顾
                    Intent intent = new Intent(getActivity(), ReviewActivity.class);

                    for(int i=0; i<gradeList.size(); i++){
                        if(gradeList.get(i).getPaperName().equals(((TextView)view.findViewById(R.id.tv_paper_name)).getText())){
                            //找到相应的成绩对象
                            intent.putExtra("Grade", gradeList.get(i));
                            break;
                        }
                    }
                    startActivity(intent);
                }
            }
        });

        return view;
    }

    @Subscriber
    private void onReceiveActionEvent(Action action){
        switch (action){
            case DOWNLOAD_GRADE_LIST:
                Log.d("获取成绩列表成功","获取成绩列表成功");
                gradeList = BmobUtils.gradeList;
                ((GradeListAdapter)listView.getAdapter()).refresh(gradeList);
                break;
            case QUERY_ERROR:
                break;
        }

    }


}
