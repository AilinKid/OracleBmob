package com.example.arena.oracle.fargment;

import android.app.usage.UsageEvents;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.arena.oracle.R;
import com.example.arena.oracle.Utils.Action;
import com.example.arena.oracle.Utils.BmobUtils;
import com.example.arena.oracle.adapter.TPaperListAdapter;
import com.example.arena.oracle.bean.Paper;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

public class Teacher_PaperList_Fragment extends Fragment {

    private ListView paperList;
    private List<Paper> papers = new ArrayList<Paper>();
    private TPaperListAdapter adapter;
    private Context context;

    public Teacher_PaperList_Fragment() {
        // Required empty public constructor
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_teacher__paper_list_, container, false);
        EventBus.getDefault().register(this);
        BmobUtils.downloadTPaperList(getContext());
        paperList = (ListView) view.findViewById(R.id.lv_paper_teacher);
        return view;
    }

    @Subscriber
    private void onReceiveActionEvent(Action action){
        Log.d("received","receivedsssss");
        switch (action){
            case DOWNLOAD_PAPER_LIST:
                papers = BmobUtils.paperList;
                Log.d("获取试卷成功", "获取试卷成功"+papers.size());
                TPaperListAdapter adapter = new TPaperListAdapter(getContext(), papers);
                paperList.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                break;
            case QUERY_ERROR:
                Log.d("获取试卷失败", "获取试卷失败");
                break;
        }
    }

}
