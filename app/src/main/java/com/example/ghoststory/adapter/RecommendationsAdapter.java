package com.example.ghoststory.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.ghoststory.R;
import com.example.ghoststory.db.DbContentList;
import com.example.ghoststory.interfaze.OnRecyclerViewOnClickListener;

import java.util.List;

public class RecommendationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DbContentList> list;
    private Context context;
    private OnRecyclerViewOnClickListener listener;

    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_LOAD_MORE = 1;

    static class NormalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView title;
        private TextView desc;
        private OnRecyclerViewOnClickListener listener;


        NormalViewHolder(View view, OnRecyclerViewOnClickListener listener) {
            super(view);
            title = view.findViewById(R.id.story_title);
            desc = view.findViewById(R.id.story_desc);
            this.listener = listener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                //实现预设接口的动作
                listener.OnItemClicked(v, getLayoutPosition());
            }
        }
    }

    public class RequestMoreViewHolder extends RecyclerView.ViewHolder{
        RequestMoreViewHolder(View itemView) {
            super(itemView);
        }
    }

    public RecommendationsAdapter(Context context, List<DbContentList> contentList) {
        this.context = context;
        list = contentList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_NORMAL:
                View view = LayoutInflater.from(context).inflate(R.layout.story_list_item, parent, false);
                return new NormalViewHolder(view, listener);
            case TYPE_LOAD_MORE:
                View view1 = LayoutInflater.from(context).inflate(R.layout.request_more_layout,parent,false);
                return new RequestMoreViewHolder(view1);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NormalViewHolder) {
            DbContentList contentItem = list.get(position);
            NormalViewHolder normalViewHolder = (NormalViewHolder)holder;
            normalViewHolder.title.setText(contentItem.getTitle());
            normalViewHolder.desc.setText(contentItem.getDesc());
        }
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == list.size()) {
            return TYPE_LOAD_MORE;
        } else {
            return TYPE_NORMAL;
        }

    }

    public void setItemClickListener(OnRecyclerViewOnClickListener listener) {
        this.listener = listener;
    }
}

