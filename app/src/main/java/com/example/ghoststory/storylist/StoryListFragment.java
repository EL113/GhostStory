package com.example.ghoststory.storylist;

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
import com.example.ghoststory.homepage.StoryListContract;
import com.example.ghoststory.interfaze.OnRecyclerViewOnClickListener;

import java.util.List;

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
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean isSlidingToLast = false;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = manager.getItemCount();

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

    @Override
    public void initView(View view) {
        refresh = (SwipeRefreshLayout) view.findViewById(R.id.frag_story_list_refresh);
        recyclerView = (RecyclerView) view.findViewById(R.id.frag_story_list_recycler);

        refresh.setColorSchemeResources(R.color.colorPrimary);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.start();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void showError(final String error) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(recyclerView, error,Snackbar.LENGTH_SHORT)
                    .setAction(getResources().getString(R.string.retry), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            presenter.start();
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
    public void showLoading() {
        refresh.setRefreshing(true);
    }

    @Override
    public void stopLoading() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void setPresenter(StoryListContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
