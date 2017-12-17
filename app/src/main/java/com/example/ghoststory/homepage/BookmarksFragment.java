package com.example.ghoststory.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.ghoststory.R;
import com.example.ghoststory.adapter.BookmarksAdapter;
import com.example.ghoststory.db.DbContentList;
import com.example.ghoststory.interfaze.OnRecyclerViewOnClickListener;
import com.example.ghoststory.search.SearchActivity;

import java.util.List;

/**
 * Created by Daniel hunt on 2017/3/25.
 */

public class BookmarksFragment extends Fragment implements BookmarksContract.View {
    private BookmarksContract.Presenter presenter;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private BookmarksAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mian_list, container, false);
        initView(view);
        setHasOptionsMenu(true);

        presenter.start();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.checkForFreshData();
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_bookmarks, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.bookmarks_search) {
            startActivity(new Intent(getActivity(),SearchActivity.class));
        }
        return true;
    }

    public BookmarksFragment() {
    }


    public static BookmarksFragment newInstance() {
        return new BookmarksFragment();
    }

    @Override
    public void showResults(List<DbContentList> list) {
            if (adapter == null) {
                adapter = new BookmarksAdapter(getActivity(), list);
                adapter.setOnRecyclerViewOnClickListener(new OnRecyclerViewOnClickListener() {
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

    @Override
    public void showLoading() {
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void stopLoading() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void initView(View view) {
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        refreshLayout.setColorSchemeResources(R.color.colorPrimary);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void setPresenter(BookmarksContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void dataError() {
        Snackbar.make(recyclerView, R.string.no_data,Snackbar.LENGTH_SHORT).setAction(R.string.retry, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.loadResults();
            }
        }).show();
    }
}
