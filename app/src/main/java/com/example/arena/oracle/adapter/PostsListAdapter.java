package com.example.arena.oracle.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.arena.oracle.R;
import com.example.arena.oracle.bean.Post;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Updated by Arena on 2017/4/3.
 */
public class PostsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Post> posts;


    public PostsListAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_base, parent,
                    false);
            return new ItemViewHolder(view);

    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        if (holder instanceof ItemViewHolder) {
//            if(posts.get(position).getCover() != null){
//                Uri imageUri = Uri.parse(posts.get(position).getCover());
//                ((ItemViewHolder) holder).sdPostCover.setImageURI(imageUri);
//            }else{
//                //没有图片的话，消去占位图片
//                ((ItemViewHolder) holder).sdPostCover.setVisibility(View.GONE);
//            }
//
//            if(posts.get(position).getTeacher().getAvatar() != null){
//                Uri imageUri = Uri.parse(posts.get(position).getAuthor().getAvatar());
//                ((ItemViewHolder) holder).sdAvatar.setImageURI(imageUri);
//            }
//            ((ItemViewHolder) holder).tvAuthor.setText(posts.get(position).getAuthor().getUsername());
//            ((ItemViewHolder) holder).tvCommentCount.setText(posts.get(position).getComments()+"");
//            ((ItemViewHolder) holder).tvTitle.setText(posts.get(position).getTitle());
//            ((ItemViewHolder) holder).tvDescription.setText(posts.get(position).getDescription());
            if (onItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getLayoutPosition();
                        onItemClickListener.onItemClick(holder.itemView, position);
                    }
                });

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int position = holder.getLayoutPosition();
                        onItemClickListener.onItemLongClick(holder.itemView, position);
                        return false;
                    }
                });
            }
        }
    }


    static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView tvAuthor;
        TextView tvCommentCount;
        TextView tvTitle;
        TextView tvDescription;
        SimpleDraweeView sdPostCover;
        SimpleDraweeView sdAvatar;


        public ItemViewHolder(View view) {
            super(view);
            tvAuthor = (TextView) view.findViewById(R.id.tvauthor);
            tvCommentCount = (TextView) view.findViewById(R.id.tvCommentCount);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            tvDescription = (TextView) view.findViewById(R.id.tvDescription);
            sdPostCover = (SimpleDraweeView) view.findViewById(R.id.sdPostCover);
            sdAvatar = (SimpleDraweeView) view.findViewById(R.id.sdAvatar);
        }
    }

}
