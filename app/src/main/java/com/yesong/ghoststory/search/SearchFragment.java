package com.yesong.ghoststory.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.yesong.ghoststory.R;
import com.yesong.ghoststory.adapter.BookmarksAdapter;
import com.yesong.ghoststory.db.DbContentList;
import com.yesong.ghoststory.interfaze.OnRecyclerViewOnClickListener;

import java.util.List;


public class SearchFragment extends Fragment implements SearchContract.View{
    private SearchContract.Presenter presenter;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private BookmarksAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_search, container, false);
        initView(view);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                presenter.loadResults(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                presenter.loadResults(newText);
                return true;
            }
        });
        return view;
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

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void stopLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void initView(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.search_toolbar);
        SearchActivity activity = (SearchActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }

        searchView = (SearchView) view.findViewById(R.id.search_view);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        searchView.setIconified(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
    }

    @Override
    public void setPresenter(SearchContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }
}
