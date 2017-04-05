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

    public static StoryListFragment newInstance() {
        return new StoryListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_story_list_layout, container, false);
        initView(view);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            boolean isSlidingToLast = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                // 当不滚动时，如果既不滚动也滚动到最后了就继续加载后一页的数据；
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 获取最后一个完全显示的item position
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = manager.getItemCount();

                    // 判断是否滚动到底部并且是向下滑动,就加载更多
                    if (lastVisibleItem == (totalItemCount - 1) && isSlidingToLast) {
                        presenter.loadMore();
                    }
                }

                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                isSlidingToLast = dy > 0;
            }
        });
        presenter.start();
        return view;
    }

    @Override
    public void initView(View view) {
        refresh = (SwipeRefreshLayout) view.findViewById(R.id.frag_story_list_refresh);
        recyclerView = (RecyclerView) view.findViewById(R.id.frag_story_list_recycler);

        refresh.setColorSchemeResources(R.color.colorPrimary);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refresh();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

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
