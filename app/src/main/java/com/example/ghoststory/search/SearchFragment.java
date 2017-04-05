package com.example.ghoststory.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.ghoststory.R;
import com.example.ghoststory.adapter.BookmarksAdapter;
import com.example.ghoststory.db.DbContentList;
import com.example.ghoststory.interfaze.OnRecyclerViewOnClickListener;

import java.util.List;

/**
 * Created by Daniel hunt on 2017/4/1.
 */

public class SearchFragment extends Fragment implements SearchContract.View{
    private SearchContract.Presenter presenter;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private BookmarksAdapter adapter;

    public SearchFragment() {
    }

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
        ((SearchActivity)(getActivity())).setSupportActionBar(toolbar);
        ((SearchActivity)(getActivity())).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((SearchActivity) (getActivity())).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        searchView = (SearchView) view.findViewById(R.id.search_view);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        searchView.setIconified(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void setPresenter(SearchContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
