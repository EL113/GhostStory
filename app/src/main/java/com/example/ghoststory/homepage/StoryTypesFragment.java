package com.example.ghoststory.homepage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ghoststory.R;
import com.example.ghoststory.adapter.StoryTypesAdapter;
import com.example.ghoststory.bean.StoryType;
import com.example.ghoststory.interfaze.OnRecyclerViewOnClickListener;

import java.util.List;

public class StoryTypesFragment extends Fragment implements StoryTypesContract.View {
    private StoryTypesContract.Presenter presenter;
    private StoryTypesAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.story_type_list, container, false);
        initView(view);
        presenter.start();
        return view;
    }

    @Override
    public void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.story_type_layout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void setPresenter(StoryTypesContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void setTypes(List<StoryType> list) {
        if (adapter == null) {
            adapter = new StoryTypesAdapter(getActivity(), list);
            adapter.setOnRecyclerViewOnClickListener(new OnRecyclerViewOnClickListener() {
                @Override
                public void OnItemClicked(View view, int position) {
                    presenter.chooseType(position);
                }
            });
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    public static StoryTypesFragment newInstance() {
        return new StoryTypesFragment();
    }
}
