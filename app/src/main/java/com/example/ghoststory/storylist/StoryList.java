package com.example.ghoststory.storylist;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.ghoststory.R;
import com.example.ghoststory.db.DbContentList;
import com.example.ghoststory.service.CacheService;

import org.litepal.crud.DataSupport;

import java.util.List;

public class StoryList extends AppCompatActivity{
    private Toolbar toolbar;
    private String title;
    private String typeId;
    private StoryListFragment storyListFragment;
    private StoryListPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_list_layout);

        Intent dataIntent = getIntent();
        title = dataIntent.getStringExtra("title");
        typeId = dataIntent.getStringExtra("typeId");

        if (title == null || typeId == null) {
            return;
        }

        toolbar = (Toolbar) findViewById(R.id.story_list_toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState != null) {
            storyListFragment = (StoryListFragment) getSupportFragmentManager()
                    .getFragment(savedInstanceState, "StoryListFragment");
        } else {
            storyListFragment = StoryListFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frag_story_list, storyListFragment, "StoryListFragment")
                    .commit();
            }

        presenter = new StoryListPresenter(StoryList.this, storyListFragment);

        presenter.setTypeId(typeId);

        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        List<DbContentList> recommendationList = DataSupport.where("typeName=? and isBookmarked=?", typeId,"0").limit(5).find(DbContentList.class);
        for (DbContentList item : recommendationList) {
            item.delete();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (storyListFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState,"StoryListFragment",storyListFragment);
        }
    }
}
