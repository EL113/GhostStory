package com.example.ghoststory.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ghoststory.R;
import com.example.ghoststory.db.DbContentList;
import com.example.ghoststory.homepage.BookmarksFragment;
import com.example.ghoststory.interfaze.OnRecyclerViewOnClickListener;

import java.util.List;

/**
 * Created by Daniel hunt on 2017/3/27.
 */

public class BookmarksAdapter extends RecyclerView.Adapter<BookmarksAdapter.ViewHolder> {
    private Context context;
    private List<DbContentList> list;
    private OnRecyclerViewOnClickListener listener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;
        private TextView title;
        private TextView desc;
        private OnRecyclerViewOnClickListener listener;

        public ViewHolder(View view, final OnRecyclerViewOnClickListener listener) {
            super(view);
            title = (TextView) view.findViewById(R.id.story_title);
            desc = (TextView) view.findViewById(R.id.story_desc);
            imageView = (ImageView) view.findViewById(R.id.story_image);
            this.listener = listener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.OnItemClicked(v,getLayoutPosition());
        }
    }

    public BookmarksAdapter(Context context, List<DbContentList> lists) {
        this.context = context;
        list = lists;
    }

    public void setOnRecyclerViewOnClickListener(OnRecyclerViewOnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.story_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, listener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DbContentList item = list.get(position);
        holder.title.setText(item.getTitle());
        holder.desc.setText(item.getDesc());
        Glide.with(context).load(item.getImg()).asBitmap().centerCrop().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
