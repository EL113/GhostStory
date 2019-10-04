package com.yesong.ghoststory.detail;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yesong.ghoststory.R;
import com.yesong.ghoststory.db.DbContentList;
import com.yesong.ghoststory.homepage.WriteActivity;

public class DetailFragment extends Fragment implements DetailContract.View {
    private DetailContract.Presenter presenter;
    private TextView detailText;
    private NestedScrollView scrollView;
    private Toolbar toolbar;
    private TextView thumbUp;
    private TextView thumbDown;
    private TextView collection;
    private TextView author;
    private ImageView thumbDownImg;
    private ImageView collectionImg;
    private ImageView thumbUpImg;
    private DbContentList contentItem;
    private String authorId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_detail, container, false);
        initView(view);
        presenter.start();
        SharedPreferences pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        authorId = pref.getString("authorId", null);
        setHasOptionsMenu(true);
        return view;
    }

    //修改数据库数据，修改ui数据
    @Override
    public void setData(final DbContentList contentItem) {
        this.contentItem = contentItem;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                toolbar.setTitle(contentItem.getTitle());
                detailText.setText(stringFilter(contentItem.getText()));
                thumbUp.setText(String.valueOf(contentItem.getThumbUp()));
                thumbUpImg.setImageResource(contentItem.getIsThumbUp() == 0 ?
                        R.drawable.baseline_thumb_up_white_24 : R.drawable.baseline_thumb_up_black_24);
                thumbDown.setText(String.valueOf(contentItem.getIsThumbDown()));
                thumbDownImg.setImageResource(contentItem.getThumbDown() == 0 ?
                        R.drawable.baseline_thumb_down_white_24 : R.drawable.baseline_thumb_down_black_24);
                collection.setText(String.valueOf(contentItem.getCollectionCount()));
                collectionImg.setImageResource(contentItem.getIsBookmarked() == 0 ?
                        R.drawable.baseline_turned_in_not_black_24 : R.drawable.baseline_turned_in_black_24);
                author.setText(contentItem.getAuthor());
            }
        });
        contentItem.save();
    }

    @Override
    public void initView(View view) {
        detailText = view.findViewById(R.id.detail_text);
        scrollView = view.findViewById(R.id.scroll_view);
        toolbar = view.findViewById(R.id.detail_toolbar);
        thumbUp = view.findViewById(R.id.thumb_up_count);
        thumbUpImg = view.findViewById(R.id.thumb_up_img);
        thumbDown = view.findViewById(R.id.thumb_down_count);
        thumbDownImg = view.findViewById(R.id.thumb_down_img);
        collection = view.findViewById(R.id.collect_count);
        collectionImg = view.findViewById(R.id.collect_img);
        author = view.findViewById(R.id.author_text);

        thumbUpImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (contentItem.getIsThumbUp() == 0){
                    contentItem.increaseThumbUp();
                } else {
                    contentItem.decreaseThumbUp();
                }
                presenter.setThumbUp(contentItem);

                if (contentItem.getIsThumbDown() == 1){
                    contentItem.decreaseThumbDown();
                    presenter.setThumbDown(contentItem);
                }

                setData(contentItem);
            }
        });

        thumbDownImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (contentItem.getIsThumbDown() == 0){
                    contentItem.increaseThumbDown();
                } else {
                    contentItem.decreaseThumbDown();
                }
                presenter.setThumbDown(contentItem);

                if (contentItem.getIsThumbUp() == 1){
                    contentItem.decreaseThumbUp();
                    presenter.setThumbUp(contentItem);
                }
                setData(contentItem);
            }
        });

        collectionImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (contentItem.getIsBookmarked() == 0){
                    contentItem.bookmark();
                } else {
                    contentItem.cancelBookmark();
                }
                presenter.setCollection(contentItem);
                setData(contentItem);
            }
        });

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY,
                                       int oldScrollX, int oldScrollY) {
                if (v.getChildAt(0).getMeasuredHeight() <=
                        v.getScrollY() + v.getHeight() && scrollY > oldScrollY) {
                    presenter.loadMore();
                }
            }
        });

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.smoothScrollTo(0,0);
            }
        });

        DetailActivity activity = (DetailActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
            actionBar.setTitle("this is title");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (contentItem == null)
            Log.d("yesongdh", "onCreateOptionsMenu: item is null");

        if (contentItem.getAuthorId() == null)
            Log.d("yesongdh", "onCreateOptionsMenu: authorid is null");

        if (contentItem.getAuthorId() != null && contentItem.getAuthorId().equals(authorId))
            inflater.inflate(R.menu.menu_delete, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            getActivity().finish();
        } else if (item.getItemId() == R.id.action_delete){
            presenter.deleteStory();
            getActivity().finish();
        } else if (item.getItemId() == R.id.action_edit){
            Intent intent = new Intent(getActivity(), WriteActivity.class);
            intent.putExtra("id", contentItem.getIdContent());
            intent.putExtra("type", contentItem.getTypeName());
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void showError() {
        Snackbar.make(toolbar, R.string.internet_error, Snackbar.LENGTH_INDEFINITE).setAction(R.string.retry, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.start();
            }
        }).show();
    }

    @Override
    public void setPresenter(DetailContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showMaxPage() {
        Toast.makeText(getActivity(), R.string.max_page_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setContentItem(DbContentList contentItem) {
        this.contentItem = contentItem;
    }

    private String stringFilter(String text) {
        text = text.replace("&nbsp;","  ").replace("shoye_336();"," ");
        return text;
    }
}
