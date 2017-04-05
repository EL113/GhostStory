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
import com.example.ghoststory.bean.StoryType;
import com.example.ghoststory.interfaze.OnRecyclerViewOnClickListener;

import java.util.List;

/**
 * Created by Daniel hunt on 2017/3/27.
 */

public class StoryTypesAdapter extends RecyclerView.Adapter<StoryTypesAdapter.ViewHolder> {

    private List<StoryType> list;
    private OnRecyclerViewOnClickListener listener;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView typeName;
        private ImageView typeImageView;
        private OnRecyclerViewOnClickListener listener;

        public ViewHolder(View view, OnRecyclerViewOnClickListener listener) {
            super(view);
            typeName = (TextView) view.findViewById(R.id.type_title);
            typeImageView = (ImageView) view.findViewById(R.id.type_image);
            this.listener = listener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.OnItemClicked(v, getLayoutPosition());
        }
    }

    public StoryTypesAdapter(Context context, List<StoryType> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnRecyclerViewOnClickListener(OnRecyclerViewOnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StoryType item = list.get(position);
        holder.typeName.setText(item.getTitle());
        Glide.with(context).load(Integer.valueOf(item.getImg())).centerCrop().into(holder.typeImageView);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.story_type_item,parent, false);
        ViewHolder viewHolder = new ViewHolder(view,listener);
        return viewHolder;
    }
}
