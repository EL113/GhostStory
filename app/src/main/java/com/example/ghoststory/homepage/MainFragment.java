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

/**
 * Created by Daniel hunt on 2017/3/25.
 */

public class MainFragment extends Fragment{
    protected Context context;
    private MainPagerAdapter adapter;

    private TabLayout tabLayout;

    private RecommendationsFragment recommendationsFrag;
    private BookmarksFragment bookmarksFrag;

    private RecommendationsPresenter recommendationsPresenter;
    private BookmarksPresenter bookmarksPresenter;


    public MainFragment() {}

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    /**
     * onCreate加载和缓存两个小碎片（推荐和收藏），之前MainActivity加载的是两个大碎片（主碎片和故事类型碎片）,
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.context = getActivity();

        //fragmentManager碎片管理中取出缓存的碎片，或者重新创建；；
        if (savedInstanceState != null) {
            FragmentManager manager = getChildFragmentManager();
            recommendationsFrag = (RecommendationsFragment) manager.getFragment(savedInstanceState, "recommendationsFrag");
            bookmarksFrag = (BookmarksFragment) manager.getFragment(savedInstanceState, "bookmarksFrag");

        } else {
            recommendationsFrag = RecommendationsFragment.newInstance();
            bookmarksFrag = BookmarksFragment.newInstance();
        }
        recommendationsPresenter = new RecommendationsPresenter(context, recommendationsFrag);
        bookmarksPresenter = new BookmarksPresenter(context, bookmarksFrag);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        initViews(view);

        setHasOptionsMenu(true);

        return view;
    }


    private void initViews(View view) {

        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(2);

        adapter = new MainPagerAdapter(
                getChildFragmentManager(),
                context,
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