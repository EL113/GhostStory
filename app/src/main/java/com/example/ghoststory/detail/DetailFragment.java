package com.example.ghoststory.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CollapsingToolbarLayout;
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

import com.bumptech.glide.Glide;
import com.example.ghoststory.R;

public class DetailFragment extends Fragment implements DetailContract.View {
    private DetailContract.Presenter presenter;
    private TextView detailText;
    private SwipeRefreshLayout refreshLayout;
    private NestedScrollView scrollView;
    private Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_detail, container, false);
        initView(view);
        setHasOptionsMenu(true);
        presenter.start();
        return view;
    }

    @Override
    public void setData(final String title, final String image,final String text) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                toolbar.setTitle(title);
                detailText.setText(stringFilter(text));
            }
        });
    }

    @Override
    public void initView(View view) {
        detailText = view.findViewById(R.id.detail_text);
        refreshLayout = view.findViewById(R.id.detail_refresh);
        scrollView = view.findViewById(R.id.scroll_view);
        toolbar = view.findViewById(R.id.detail_toolbar);

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

        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.smoothScrollTo(0,0);
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.start();
            }
        });

        DetailActivity activity = (DetailActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_more,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                break;
            case R.id.action_more:
                setupBottomSheetDialog();
                break;
        }
        return true;
    }

    private void setupBottomSheetDialog() {
        final BottomSheetDialog dialog = new BottomSheetDialog(getActivity());

        View view = getActivity().getLayoutInflater().
                inflate(R.layout.menu_more_dialog, null);

        //收藏与取消收藏
        boolean isBookmarked = presenter.queryIfIsBookmarked();
        if (isBookmarked) {
            ((ImageView) view.findViewById(R.id.bookmarks_image))
                    .setImageResource(R.drawable.ic_star_black_24dp);
            ((TextView) view.findViewById(R.id.bookmarks_text))
                    .setText(R.string.action_delete_from_bookmarks);
        } else {
            ((ImageView) view.findViewById(R.id.bookmarks_image))
                    .setImageResource(R.drawable.ic_star_border_black_24dp);
            ((TextView) view.findViewById(R.id.bookmarks_text))
                    .setText(R.string.action_add_to_bookmarks);
        }

        view.findViewById(R.id.layout_bookmark).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                presenter.addToOrDeleteFromBookmarks();
            }
        });

        view.findViewById(R.id.layout_copy_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                presenter.copyText();
            }
        });
        dialog.setContentView(view);
        dialog.show();
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
    public void showAddedToBookmarks() {
        Snackbar.make(toolbar, R.string.added_to_bookmarks, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showCopyTextError() {
        Snackbar.make(toolbar, R.string.copied_to_clipboard_failed, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showDeletedFromBookmarks() {
        Snackbar.make(toolbar, R.string.deleted_from_bookmarks, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showLinkError() {
        Snackbar.make(toolbar, R.string.link_error, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showTextCopied() {
        Snackbar.make(toolbar, R.string.copied_to_clipboard, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(DetailContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void showLoading() {
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void stopLoading() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void showMaxPage() {
        Toast.makeText(getActivity(), R.string.max_page_error, Toast.LENGTH_SHORT).show();
    }

    private String stringFilter(String text) {
        text = text.replace("&nbsp;","  ").replace("shoye_336();"," ");
        return text;
    }
}
