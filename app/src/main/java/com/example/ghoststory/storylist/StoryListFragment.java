package com.example.ghoststory.storylist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ghoststory.R;
import com.example.ghoststory.adapter.RecommendationsAdapter;
import com.example.ghoststory.db.DbContentList;
import com.example.ghoststory.detail.DetailActivity;
import com.example.ghoststory.interfaze.OnRecyclerViewOnClickListener;

import java.util.List;

/**
 * Created by Daniel hunt on 2017/3/28.
 */

public class StoryListFragment extends Fragment implements StoryListContract.View {
    private SwipeRefreshLayout refresh;
    private RecyclerView recyclerView;
    private StoryListContract.Presenter presenter;
    private RecommendationsAdapter adapter;

    //单例设计模式
    public static StoryListFragment newInstance() {
        return new StoryListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_story_list_layout, container, false);
        initView(view);
        //添加recyclerview的滑动监听
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            //是否滑动到最后一项，以便加载更多
            boolean isSlidingToLast = false;
            //滑动状态改变时
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                // 如果新状态是不滑动，并且最后一个完全显示的item的位置编号等于总item数-1就说明滑动到底部了
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 获取最后一个完全显示的item 的位置和所有的item数量
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = manager.getItemCount();

                    // 判断最后一个完全显示的item的位置编号是否等于总item数-1，并且是向下滑动
                    if (lastVisibleItem == (totalItemCount - 1) && isSlidingToLast) {
                        presenter.loadMore(); //是就说明滑动到底部，可以加载更多
                    }
                }

                super.onScrollStateChanged(recyclerView, newState);
            }
            //通过滑动时的dy正负来判断是否是向下滑动
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                isSlidingToLast = dy > 0;
            }
        });
        presenter.start();
        return view;
    }

    /**
     * 实例化视图上的控件，并设置各种监听
     * @param view 加载的视图
     */
    @Override
    public void initView(View view) {
        refresh = (SwipeRefreshLayout) view.findViewById(R.id.frag_story_list_refresh);
        recyclerView = (RecyclerView) view.findViewById(R.id.frag_story_list_recycler);

        //设置向下刷新时时出现的progressbar的颜色
        refresh.setColorSchemeResources(R.color.colorPrimary);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refresh();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    //显示出错
    @Override
    public void showError() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(recyclerView,getResources().getString(R.string.loaded_fail),Snackbar.LENGTH_SHORT)
                    .setAction(getResources().getString(R.string.retry), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            presenter.refresh();
                        }
                    }).show();
            }
        });
    }
    //加载数据，向adapter里面填充数据
    @Override
    public void showResults(final List<DbContentList> lists) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (adapter == null) {
                    adapter = new RecommendationsAdapter(getActivity(), lists);
                    adapter.setItemClickListener(new OnRecyclerViewOnClickListener() {
                        @Override
                        public void OnItemClicked(View view, int position) {
                            presenter.startReading(position);
                        }
                    });
                    recyclerView.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
    //通知控件刷新状态已经改变，可以开始刷新了
    @Override
    public void startLoading() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refresh.post(new Runnable() {
                    @Override
                    public void run() {
                        refresh.setRefreshing(true);
                    }
                });
            }
        });

    }
    //通知控件刷新状态已经改变，停止刷新
    @Override
    public void stopLoading() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refresh.post(new Runnable() {
                    @Override
                    public void run() {
                        refresh.setRefreshing(false);
                    }
                });
            }
        });
    }

    @Override
    public void setPresenter(StoryListContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
