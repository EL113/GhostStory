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


import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private MainFragment mainFragment;
    private StoryTypesFragment storyTypesFragment;
    public static final String ACTION_STORY_TYPES = "com.example.ghost.storyTypes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //设置控件
        initView();

        //检查碎片缓存（碎片缓存都在碎片管理对象中），
        // 可以从碎片管理对象中直接获取缓存的碎片对象，或者直接创建碎片对象
        if (savedInstanceState != null) {
            mainFragment = (MainFragment) getSupportFragmentManager()
                    .getFragment(savedInstanceState, "MainFragment");
            storyTypesFragment = (StoryTypesFragment) getSupportFragmentManager()
                    .getFragment(savedInstanceState,"StoryTypesFragment");
        } else {
            mainFragment = MainFragment.newInstance();
            storyTypesFragment = StoryTypesFragment.newInstance();
        }
        //加载碎片到主页布局中
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
        //给碎片的presenter传递上下文
        new StoryTypesPresenter(MainActivity.this, storyTypesFragment);
        //导航菜单的check事件，通过intent事件的动作来判断
        String action = getIntent().getAction();
        if (action.equals(ACTION_STORY_TYPES)) {
            navigationView.setCheckedItem(R.id.nav_types);
            showTypesFragment();
        } else {
            navigationView.setCheckedItem(R.id.nav_home);
            showMainFragment();
        }
    }
    //设置控件
    private void initView() {
        //设置标题栏
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //添加标题栏的返回按钮
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        }
        //导航菜单的各种点击事件
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
                    case R.id.nav_theme://夜间模式的切换
                        //检测当前主题是否是夜间模式
                        int mode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                        //如果当前模式是夜间模式，则关闭夜间模式
                        if(mode == Configuration.UI_MODE_NIGHT_YES) {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        } else if(mode == Configuration.UI_MODE_NIGHT_NO) {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        }
                        //设置窗口改变的动画效果
                        getWindow().setWindowAnimations(R.style.WindowAnimationFadeInOut);
                        recreate();
                        break;
                    case R.id.sweep_cache:
                        String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
                        time += "000000";
                        List<DbContentList> list = DataSupport.where("isBookmarked = " +
                                "? and time < ?","0",time).find(DbContentList.class);
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

    //显示主碎片，并设置标题内容
    private void showMainFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.show(mainFragment);

        fragmentTransaction.hide(storyTypesFragment);
        fragmentTransaction.commit();

        toolbar.setTitle(getResources().getString(R.string.app_name));
    }
    //显示类型碎片，并设置标题内容
    private void showTypesFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.show(storyTypesFragment);
        fragmentTransaction.hide(mainFragment);
        fragmentTransaction.commit();

        toolbar.setTitle(getResources().getString(R.string.story_types));
    }
    //在退出时，删除头五条没有收藏的数据
    @Override
    protected void onDestroy() {
        super.onDestroy();
        List<DbContentList> recommendationList = DataSupport.where("typeName=? and isBookmarked=?",
                "recommendations","0").limit(5).find(DbContentList.class);
        for (DbContentList item : recommendationList) {
            item.delete();
        }
    }

    //把已经添加好的主碎片和故事列表碎片保存到碎片管理作为缓存中
    @Override
    public void onSaveInstanceState(Bundle outState) {
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
}
