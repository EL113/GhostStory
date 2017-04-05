package com.example.ghoststory.detail;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.ghoststory.R;

public class DetailActivity extends AppCompatActivity {
    private DetailFragment detailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_layout);

        if (savedInstanceState != null) {
            detailFragment = (DetailFragment) getSupportFragmentManager().getFragment(savedInstanceState, "DetailFragment");
        } else {
            detailFragment = new DetailFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.detail_frame, detailFragment).commit();
        }

        Intent intent = getIntent();
        DetailPresenter presenter = new DetailPresenter(DetailActivity.this, detailFragment);
        presenter.setIdContent(intent.getStringExtra("idContent"));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (detailFragment.isAdded()) {
            getSupportFragmentManager().getFragment(outState, "DetailFragment");
        }
    }
}
