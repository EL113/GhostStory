package com.example.ghoststory.homepage;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.ghoststory.R;
import com.example.ghoststory.about.AboutPreferenceActivity;
import com.example.ghoststory.db.DbContentList;
import com.example.ghoststory.service.CacheService;
import com.example.ghoststory.settings.SettingsPreferenceActivity;

import org.litepal.crud.DataSupport;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private MainFragment mainFragment;
    private StoryTypesFragment storyTypesFragment;
    public static final String ACTION_STORY_TYPES = "com.example.ghost.storyTypes";
    private double time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        //从缓存中加载碎片数据或直接创建，并把碎片放进碎片管理中，方便之后隐藏和显示碎片；
        if (savedInstanceState != null) {
            mainFragment = (MainFragment) getSupportFragmentManager()
                    .getFragment(savedInstanceState, "MainFragment");
            storyTypesFragment = (StoryTypesFragment) getSupportFragmentManager()
                    .getFragment(savedInstanceState,"StoryTypesFragment");
        } else {
            mainFragment = MainFragment.newInstance();
            storyTypesFragment = StoryTypesFragment.newInstance();
        }

        if (!mainFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.layout_fragment, mainFragment, "MainFragment")
                    .commit();
        }

        if (!storyTypesFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.layout_fragment, storyTypesFragment, "StoryTypesFragment")
                    .commit();
        }

        new StoryTypesPresenter(MainActivity.this, storyTypesFragment);



        String action = getIntent().getAction();
        if (action.equals(ACTION_STORY_TYPES)) {
            navigationView.setCheckedItem(R.id.nav_types);
            showTypesFragment();
        } else {
            navigationView.setCheckedItem(R.id.nav_home);
            showMainFragment();
        }

    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        showMainFragment();
                        break;
                    case R.id.nav_types:
                        showTypesFragment();
                        break;
                    case R.id.nav_about:
                        startActivity(new Intent(MainActivity.this, AboutPreferenceActivity.class));
                        break;
                    case R.id.nav_theme:
                        int mode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

                        if(mode == Configuration.UI_MODE_NIGHT_YES) {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        } else if(mode == Configuration.UI_MODE_NIGHT_NO) {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        }
                        getWindow().setWindowAnimations(R.style.WindowAnimationFadeInOut);
                        recreate();
                        break;
                    case R.id.sweep_cache:
                        getNow();
                        List<DbContentList> list = DataSupport.where("isBookmarked = ? and time < ?","0",String.valueOf(time)).find(DbContentList.class);
                        for (DbContentList deleteItem : list) {
                            deleteItem.delete();
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }


    private void showMainFragment() {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.show(mainFragment);
        ;
        fragmentTransaction.hide(storyTypesFragment);
        fragmentTransaction.commit();

        toolbar.setTitle(getResources().getString(R.string.app_name));
    }

    private void showTypesFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.show(storyTypesFragment);
        fragmentTransaction.hide(mainFragment);
        fragmentTransaction.commit();

        toolbar.setTitle(getResources().getString(R.string.story_types));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        List<DbContentList> recommendationList = DataSupport.where("typeName=? and isBookmarked=?", "recommendations","0").limit(5).find(DbContentList.class);
        for (DbContentList item : recommendationList) {
            item.delete();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
        if (mainFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "MainFragment", mainFragment);
        }

        if (storyTypesFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "StoryTypesFragment", storyTypesFragment);
        }
    }
    //activity中的菜单项必须返回false，不然碎片中的菜单项的onOptionsItemSelected将会失效；
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        return false;
    }

    public void getNow() {
        Calendar c = Calendar.getInstance();

        String year = String.valueOf(c.get(Calendar.YEAR));
        String month = String.valueOf(c.get(Calendar.MONTH)+1);
        int monthNumber = c.get(Calendar.MONTH)+1;
        if (monthNumber >= 0 && monthNumber < 10) {
            month = "0" + month;
        }
        String date = String.valueOf(c.get(Calendar.DATE));
        int dateNumber=c.get(Calendar.DATE);
        if (dateNumber >= 0 && dateNumber < 10) {
            date = "0" + date;
        }

        time = Double.valueOf(year + month + date + "000000") ;
    }
}
