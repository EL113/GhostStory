package com.yesong.ghoststory.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yesong.ghoststory.R;
import com.yesong.ghoststory.db.DbContentList;
import com.yesong.ghoststory.interfaze.OnRecyclerViewOnClickListener;

import java.util.List;

public class BookmarksAdapter extends RecyclerView.Adapter<BookmarksAdapter.ViewHolder> {
    private Context context;
    private List<DbContentList> list;
    private OnRecyclerViewOnClickListener listener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView title;
        private TextView desc;
        private TextView author;
        private TextView auditStatus;
        private OnRecyclerViewOnClickListener listener;

        ViewHolder(View view, OnRecyclerViewOnClickListener listener) {
            super(view);
            title = view.findViewById(R.id.story_title);
            desc = view.findViewById(R.id.story_desc);
            author = view.findViewById(R.id.story_author);
            auditStatus = view.findViewById(R.id.story_audit);
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
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DbContentList item = list.get(position);
        holder.title.setText(item.getTitle());
        holder.desc.setText(item.getDesc());
        holder.author.setText(item.getAuthor());
        holder.auditStatus.setVisibility(View.VISIBLE);
        if ("0".equals(item.getAuditStatus())){
            holder.auditStatus.setText("审核中");
        } else if ("1".equals(item.getAuditStatus())){
            holder.auditStatus.setText("通过");
        } else if ("2".equals(item.getAuditStatus())) {
            holder.auditStatus.setText("未通过");
        } else {
            holder.auditStatus.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
