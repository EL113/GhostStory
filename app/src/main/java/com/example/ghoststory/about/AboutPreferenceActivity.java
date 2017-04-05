package com.example.ghoststory.about;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.ghoststory.R;

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
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}


