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
import com.example.ghoststory.bean.ContentList;
import com.example.ghoststory.db.DbContentList;
import com.example.ghoststory.interfaze.OnRecyclerViewOnClickListener;

import java.util.List;

/**
 * Created by Daniel hunt on 2017/3/25.
 */

public class RecommendationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DbContentList> list;
    private Context context;
    private OnRecyclerViewOnClickListener listener;

    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_LOADMORE = 1;

    static class NormalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;
        private TextView title;
        private TextView desc;
        private OnRecyclerViewOnClickListener listener;


        public NormalViewHolder(View view, OnRecyclerViewOnClickListener listener) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.story_image);
            title = (TextView) view.findViewById(R.id.story_title);
            desc = (TextView) view.findViewById(R.id.story_desc);
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

        public RequestMoreViewHolder(View itemView) {
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
                NormalViewHolder normalViewHolder = new NormalViewHolder(view, listener);
                return normalViewHolder;
            case TYPE_LOADMORE:
                View view1 = LayoutInflater.from(context).inflate(R.layout.request_more_layout,parent,false);
                RequestMoreViewHolder loadingViewHolder = new RequestMoreViewHolder(view1);
                return loadingViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof NormalViewHolder) {
            DbContentList contentItem = list.get(position);
            NormalViewHolder normalViewHolder = (NormalViewHolder)holder;
            if (contentItem.getImg() == null || contentItem.getTitle().equals(null) || contentItem.getDesc().equals(null)) {
                Glide.with(context).load(contentItem.getImg()).asBitmap().centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .placeholder(R.drawable.ic_error_black_24dp)
                        .error(R.drawable.ic_error_black_24dp)
                        .into(normalViewHolder.imageView);
                normalViewHolder.title.setText("数据错误");
                normalViewHolder.desc.setText("数据错误");
            } else {
                Glide.with(context).load(contentItem.getImg()).asBitmap().centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .placeholder(R.drawable.ic_error_black_24dp)
                        .error(R.drawable.ic_error_black_24dp)
                        .into(normalViewHolder.imageView);
                normalViewHolder.title.setText(contentItem.getTitle());
                normalViewHolder.desc.setText(contentItem.getDesc());
            }
        }

    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == list.size()) {
            return TYPE_LOADMORE;
        } else {
            return TYPE_NORMAL;
        }

    }

    public void setItemClickListener(OnRecyclerViewOnClickListener listener) {
        this.listener = listener;
    }
}

