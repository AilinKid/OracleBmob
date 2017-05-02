package com.example.arena.oracle.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.a.a.V;
import com.example.arena.oracle.R;
import com.example.arena.oracle.bean.Grade;

import java.util.List;

/**
 * Created by macbook on 2017/4/24.
 */

public class GradeListAdapter extends BaseAdapter {
    private List<Grade> gradeData;
    private LayoutInflater mInflater;
    private Context context;

    public GradeListAdapter(Context context, List<Grade> gradeData){
        this.gradeData = gradeData;
        this.context = context;
        //获得布局加载器
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return gradeData.size();
    }

    @Override
    public Object getItem(int position) {
        return gradeData.get(position);
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
            convertView = mInflater.inflate(R.layout.grade_list_item, null);
            holder.paperName = (TextView)convertView.findViewById(R.id.tv_paper_name);
            holder.joinTime = (TextView)convertView.findViewById(R.id.tv_paper_time);
            holder.grade = (TextView)convertView.findViewById(R.id.tv_paper_grade);
            convertView.setTag(holder);
        }
        else{
            holder= (ViewHolder) convertView.getTag();
        }
        holder.paperName.setText(gradeData.get(position).getPaperName());
        holder.joinTime.setText(gradeData.get(position).getJoinTime());
        //set 是对String的，会出现资源异常
        holder.grade.setText(gradeData.get(position).getGrade()+".");
        if(gradeData.get(position).getGrade()<60){
            holder.grade.setTextColor(Color.RED);
        }
        return convertView;
    }

    public final class ViewHolder{
        public TextView paperName;
        public TextView joinTime;
        public TextView grade;
    }

    public void refresh(List<Grade> list){
        gradeData = list;
        notifyDataSetChanged();
    }
}
