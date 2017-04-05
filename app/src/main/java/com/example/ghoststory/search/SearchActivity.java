package com.example.ghoststory.search;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.ghoststory.R;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_layout);

        SearchFragment fragment = SearchFragment.newInstance();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.activity_search_layout,fragment)
                .commit();

        new SearchPresenter(SearchActivity.this, fragment);
    }
}