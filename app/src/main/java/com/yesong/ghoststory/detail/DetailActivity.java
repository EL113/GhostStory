package com.yesong.ghoststory.detail;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;

import com.yesong.ghoststory.R;
import com.yesong.ghoststory.db.DbContentList;

import org.litepal.crud.DataSupport;

public class DetailActivity extends AppCompatActivity {
    private DetailFragment detailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_layout);

        Intent intent = getIntent();
        String idContent = intent.getStringExtra("idContent");
        String typeName = intent.getStringExtra("typeName");
        DbContentList item = DataSupport.where("idContent=? and typeName = ?", idContent, typeName).findFirst(DbContentList.class);

        if (savedInstanceState != null) {
            detailFragment = (DetailFragment) getSupportFragmentManager()
                    .getFragment(savedInstanceState, "DetailFragment");
        } else {
            detailFragment = new DetailFragment();
        }

        DetailPresenter presenter = new DetailPresenter(DetailActivity.this, detailFragment);
        presenter.setIdContent(item);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.detail_frame, detailFragment)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (detailFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "DetailFragment", detailFragment);
        }
    }
}
