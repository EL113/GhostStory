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
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ghoststory.R;

/**
 * Created by Daniel hunt on 2017/3/30.
 */

public class DetailFragment extends Fragment implements DetailContract.View {
    private DetailContract.Presenter presenter;
    private TextView detailText;
    private ImageView imageView;
    private CollapsingToolbarLayout toolbarLayout;
    private SwipeRefreshLayout refreshLayout;
    private NestedScrollView scrollView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_detail, container, false);

        initView(view);

        setHasOptionsMenu(true);

        presenter.start();

        view.findViewById(R.id.detail_toolbar).setOnClickListener(new View.OnClickListener() {
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

        return view;
    }

    @Override
    public void setData(final String title, final String image,final String text) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                toolbarLayout.setTitle(title);
                detailText.setText(stringFilter(text));
                Glide.with(getActivity()).load(image).asBitmap().centerCrop().into(imageView);
            }
        });
    }

    @Override
    public void initView(View view) {
        toolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.detail_collapsing_toolbar);
        detailText = (TextView) view.findViewById(R.id.detail_text);
        imageView = (ImageView) view.findViewById(R.id.detail_image);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.detail_refresh);
        scrollView = (NestedScrollView) view.findViewById(R.id.scroll_view);

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        if (scrollView.getChildAt(0).getMeasuredHeight() <= scrollView.getScrollY() + scrollView.getHeight()) {
                            presenter.loadMore();
                        }
                        break;
                }
                return false;
            }
        });

        refreshLayout.setColorSchemeResources(R.color.colorPrimary);

        DetailActivity activity = (DetailActivity) getActivity();
        activity.setSupportActionBar((Toolbar) view.findViewById(R.id.detail_toolbar));
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
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
                final BottomSheetDialog dialog = new BottomSheetDialog(getActivity());

                View view = getActivity().getLayoutInflater().inflate(R.layout.menu_more_dialog, null);

                if (presenter.queryIfIsBookmarked()) {
                    ((TextView) view.findViewById(R.id.bookmarks_text)).setText(R.string.action_delete_from_bookmarks);
                    ((ImageView) view.findViewById(R.id.bookmarks_image))
                            .setImageResource(R.drawable.ic_star_border_black_24dp);
                }

                view.findViewById(R.id.layout_bookmark).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        presenter.addToOrDeleteFromBookmarks();
                    }
                });

                view.findViewById(R.id.layout_copy_link).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        presenter.copyLink();
                    }
                });

                view.findViewById(R.id.layout_open_in_browser).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        presenter.openInBrowser();
                    }
                });

                view.findViewById(R.id.layout_copy_text).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        presenter.copyText();
                    }
                });

                // shareAsText the content as text
                view.findViewById(R.id.layout_share_text).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        presenter.shareAsText();
                    }
                });
                dialog.setContentView(view);
                dialog.show();
            break;
        }
        return true;
    }

    @Override
    public void showError() {
        Snackbar.make(imageView, R.string.internet_error, Snackbar.LENGTH_INDEFINITE).setAction(R.string.retry, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.start();
            }
        }).show();
    }

    @Override
    public void showAddedToBookmarks() {
        Snackbar.make(imageView, R.string.added_to_bookmarks, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showBrowserNotFoundError() {
        Snackbar.make(imageView, R.string.no_browser_found,Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showCopyTextError() {
        Snackbar.make(imageView, R.string.copied_to_clipboard_failed, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showDeletedFromBookmarks() {
        Snackbar.make(imageView, R.string.deleted_from_bookmarks, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showLinkError() {
        Snackbar.make(imageView, R.string.link_error, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showShareError() {
        Snackbar.make(imageView,R.string.share_error,Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showTextCopied() {
        Snackbar.make(imageView, R.string.copied_to_clipboard, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showLinkCopied() {
        Snackbar.make(imageView, R.string.link_copied,Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(DetailContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void showLoading() {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void stopLoading() {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void showMaxPage() {
        Snackbar.make(imageView, R.string.max_page_error,Snackbar.LENGTH_LONG).show();
    }

    public String stringFilter(String text) {
        text = text.replace("&nbsp;","  ").replace("shoye_336();"," ");
        return text;
    }
}
