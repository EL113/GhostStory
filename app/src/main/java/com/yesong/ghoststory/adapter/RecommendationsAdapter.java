package com.yesong.ghoststory.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.yesong.ghoststory.R;
import com.yesong.ghoststory.db.DbContentList;
import com.yesong.ghoststory.interfaze.OnRecyclerViewOnClickListener;

import java.util.List;

public class RecommendationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DbContentList> list;
    private Context context;
    private View.OnClickListener refreshListener;
    private OnRecyclerViewOnClickListener listener;

    public static final int TYPE_NORMAL = 0;  //正常数据项
    public static final  int TYPE_LOADING = 1; //加载中
    public static final  int TYPE_NO_MORE = 2;   //没有更多数据
    public static final  int TYPE_EMPTY_ERROR = 3; //第一页数据为空错误
    public static final  int TYPE_NETWORK_ERROR = 4;   //网络错误

    public RecommendationsAdapter(Context context, List<DbContentList> contentList) {
        this.context = context;
        list = contentList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case TYPE_NORMAL:
                View view = LayoutInflater.from(context).inflate(R.layout.story_list_item, parent, false);
                viewHolder = new NormalViewHolder(view, listener);
                break;
            case TYPE_NETWORK_ERROR:
                View view3 = LayoutInflater.from(context).inflate(R.layout.network_error_page, parent, false);
                viewHolder = new NetworkErrorPageViewHolder(view3, refreshListener);
                break;
            case TYPE_LOADING:
                View view1 = LayoutInflater.from(context).inflate(R.layout.request_loading_layout,parent,false);
                viewHolder = new OtherViewHolder(view1);
                break;
            case TYPE_EMPTY_ERROR:
                View view2 = LayoutInflater.from(context).inflate(R.layout.empty_error_page, parent, false);
                viewHolder = new OtherViewHolder(view2);
                break;
            case TYPE_NO_MORE:
                View view4 = LayoutInflater.from(context).inflate(R.layout.no_more_item_layout, parent, false);
                viewHolder = new OtherViewHolder(view4);
                break;
            default:
                break;
        }
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NormalViewHolder) {
            DbContentList contentItem = list.get(position);
            NormalViewHolder normalViewHolder = (NormalViewHolder)holder;
            normalViewHolder.title.setText(contentItem.getTitle());
            normalViewHolder.desc.setText(contentItem.getDesc());
            normalViewHolder.author.setText(contentItem.getAuthor());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getItemType();
    }

    public void setItemClickListener(OnRecyclerViewOnClickListener listener) {
        this.listener = listener;
    }

    public void setOnRefreshClickListener(View.OnClickListener listener) {
        this.refreshListener = listener;
    }

    private class NormalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView title;
        private TextView author;
        private TextView desc;
        private OnRecyclerViewOnClickListener listener;

        NormalViewHolder(View view, OnRecyclerViewOnClickListener listener) {
            super(view);
            title = view.findViewById(R.id.story_title);
            desc = view.findViewById(R.id.story_desc);
            author = view.findViewById(R.id.story_author);
            this.listener = listener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                //点击阅读
                listener.OnItemClicked(v, getLayoutPosition());
            }
        }
    }

    private class NetworkErrorPageViewHolder extends RecyclerView.ViewHolder {
        private Button button;

        NetworkErrorPageViewHolder(View view, View.OnClickListener listener) {
            super(view);
            button = view.findViewById(R.id.error_button);
            button.setOnClickListener(listener);
        }
    }

    private class OtherViewHolder extends RecyclerView.ViewHolder{
        OtherViewHolder(View itemView) {
            super(itemView);
        }
    }
}

