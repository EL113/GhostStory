package com.example.ghoststory.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.ghoststory.homepage.BookmarksFragment;
import com.example.ghoststory.homepage.RecommendationsFragment;

/**
 * Created by Daniel hunt on 2017/3/25.
 */

public class MainPagerAdapter extends FragmentPagerAdapter {
    private String[] titles;
    private final Context context;

    private RecommendationsFragment recommendationsFrag;
    private BookmarksFragment bookmarksFrag;

    public RecommendationsFragment getRecommendationsFrag () {
        return recommendationsFrag;
    }

    public BookmarksFragment getBookmarksFrag () {
        return bookmarksFrag;
    }

    public MainPagerAdapter(FragmentManager fm,
                            Context context,
                            RecommendationsFragment recommendationsFrag,
                            BookmarksFragment bookmarksFrag
    ) {
        super(fm);
        this.context = context;
        titles = new String[] {"推荐", "收藏"};

        this.recommendationsFrag = recommendationsFrag;
        this.bookmarksFrag = bookmarksFrag;

    }

    @Override
    public Fragment getItem(int position) {
        if (position == 1){
            return bookmarksFrag;
        }
        return recommendationsFrag;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}