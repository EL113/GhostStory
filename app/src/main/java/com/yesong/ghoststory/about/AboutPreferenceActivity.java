package com.yesong.ghoststory.about;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.yesong.ghoststory.R;

public class AboutPreferenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_preference_layout);

        initView();

        AboutFragment fragment = new AboutFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.about_container, fragment).commit();

        new AboutPresenter(AboutPreferenceActivity.this, fragment);
    }

    public void initView() {
        setSupportActionBar((Toolbar)findViewById(R.id.about_toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}


