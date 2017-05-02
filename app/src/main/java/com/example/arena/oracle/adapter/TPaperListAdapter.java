package com.example.arena.oracle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.arena.oracle.R;
import com.example.arena.oracle.bean.Paper;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nineoldandroids.view.ViewHelper;

import java.util.List;

/**
 * Created by macbook on 2017/4/19.
 */

public class TPaperListAdapter extends BaseAdapter{

    private List<Paper> paperData;
    private LayoutInflater mInflater;
    private Context context;

    public TPaperListAdapter(Context context, List<Paper> data){
        this.paperData = data;
        this.context = context;
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
        ViewHolder Holder = null;
        if(convertView==null){
            Holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_paper_list_teacher,null);
            Holder.avatar = (SimpleDraweeView)convertView.findViewById(R.id.sdTeacherAvatar);
            Holder.paperContent = (TextView) convertView.findViewById(R.id.tvPaperContent);
            Holder.paperTime = (TextView) convertView.findViewById(R.id.tvPaperTime);
            Holder.paperTitle = (TextView)convertView.findViewById(R.id.tvPaperTitle);
            convertView.setTag(Holder);
        }
        else {
            Holder = (ViewHolder)convertView.getTag();
        }
        //Holder.paperContent.setText();
        Holder.paperTime.setText(paperData.get(position).getJoinTime());
        Holder.paperTitle.setText(paperData.get(position).getPaperName());
        return convertView;
    }


    public final class ViewHolder{
        public SimpleDraweeView avatar;
        public TextView paperContent;
        public TextView paperTitle;
        public TextView paperTime;
    }
}
