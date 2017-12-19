package com.example.ghoststory.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.ghoststory.homepage.BookmarksFragment;
import com.example.ghoststory.homepage.RecommendationsFragment;

public class MainPagerAdapter extends FragmentPagerAdapter {
    private String[] titles;

    private RecommendationsFragment recommendationsFrag;
    private BookmarksFragment bookmarksFrag;

    public MainPagerAdapter(FragmentManager fm,
                            RecommendationsFragment recommendationsFrag,
                            BookmarksFragment bookmarksFrag) {
        super(fm);
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
