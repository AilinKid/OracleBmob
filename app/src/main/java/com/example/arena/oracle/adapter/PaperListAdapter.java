package com.example.arena.oracle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.arena.oracle.R;
import com.example.arena.oracle.Utils.BmobUtils;
import com.example.arena.oracle.bean.Grade;
import com.example.arena.oracle.bean.Paper;
import com.facebook.drawee.view.SimpleDraweeView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by macbook on 2017/4/21.
 */

public class PaperListAdapter extends BaseAdapter{
    public static List<Paper> paperData;
    private List<Grade> gradeList;
    private LayoutInflater mInflater;
    private Context context;

    public PaperListAdapter(Context context, List<Paper> data){
        this.paperData = data;
        this.context = context;
        gradeList = BmobUtils.gradeList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return paperData.size();
    }

    @Override
    public Object getItem(int position) {
        return paperData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.paper_list_item, null);
            holder.paperName = (TextView) convertView.findViewById(R.id.tv_paper_name);
            holder.paperTime = (TextView) convertView.findViewById(R.id.tv_paper_time);
            holder.paperStatus = (TextView) convertView.findViewById(R.id.tv_paper_status);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
        }
        holder.paperName.setText(paperData.get(position).getPaperName());
        holder.paperTime.setText(paperData.get(position).getJoinTime());
        holder.paperStatus.setText("未完成");
        for(int i=0; i<gradeList.size(); i++){
            if(paperData.get(position).getPaperName().equals(gradeList.get(i).getPaperName())){
                paperData.get(position).setFinishState(true);
                holder.paperStatus.setText("已完成");
            }
        }
        return convertView;
    }


    public final class ViewHolder{
        public SimpleDraweeView avatar;
        public TextView paperTime;
        public TextView paperName;
        public TextView paperStatus;
    }

    //更新用的
    public void refresh1(ArrayList<Paper> newPapers){
        this.paperData = newPapers;
        notifyDataSetChanged();
    }

    public void refresh2(ArrayList<Grade> newGrades){
        this.gradeList = newGrades;
        Log.d("成绩长度", "成绩长度"+gradeList.size());
        notifyDataSetChanged();
    }
}
