package com.example.ghoststory.homepage;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ghoststory.R;
import com.example.ghoststory.adapter.MainPagerAdapter;

public class MainFragment extends Fragment{
    private RecommendationsFragment recommendationsFrag;
    private BookmarksFragment bookmarksFrag;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

     //onCreate加载和缓存两个小碎片（推荐和收藏），之前MainActivity加载的是两个大碎片（主碎片和故事类型碎片）,
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupChildFragments(savedInstanceState);
    }

    private void setupChildFragments(Bundle savedInstanceState) {
        Context context = getActivity();
        //fragmentManager碎片管理中取出缓存的碎片，否则创建；
        if (savedInstanceState != null) {
            FragmentManager manager = getChildFragmentManager();
            recommendationsFrag = (RecommendationsFragment) manager.getFragment(savedInstanceState, "recommendationsFrag");
            bookmarksFrag = (BookmarksFragment) manager.getFragment(savedInstanceState, "bookmarksFrag");
        } else {
            recommendationsFrag = RecommendationsFragment.newInstance();
            bookmarksFrag = BookmarksFragment.newInstance();
        }
        new RecommendationsPresenter(context, recommendationsFrag);
        new BookmarksPresenter(context, bookmarksFrag);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        initViews(view);
        setHasOptionsMenu(true);
        return view;
    }


    private void initViews(View view) {
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        ViewPager viewPager = view.findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(2);

        MainPagerAdapter adapter = new MainPagerAdapter(
                getChildFragmentManager(),
                recommendationsFrag,
                bookmarksFrag);

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        FragmentManager manager = getChildFragmentManager();
        manager.putFragment(outState, "recommendationsFrag", recommendationsFrag);
        manager.putFragment(outState, "bookmarksFrag", bookmarksFrag);
    }
}
