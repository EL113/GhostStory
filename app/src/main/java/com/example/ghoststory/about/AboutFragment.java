package com.example.ghoststory.about;

import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.ghoststory.R;


public class AboutFragment extends PreferenceFragmentCompat implements AboutContract.View {
    private AboutContract.Presenter presenter;
    private Toolbar toolbar;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.frag_about);

        initView(getView());

        findPreference("rate").setOnPreferenceClickListener(new android.support.v7.preference.Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(android.support.v7.preference.Preference preference) {
                presenter.rate();
                return false;
            }
        });


        findPreference("follow_me_on_github").setOnPreferenceClickListener(new android.support.v7.preference.Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(android.support.v7.preference.Preference preference) {
                presenter.followOnGithub();
                return false;
            }
        });

        findPreference("feedback").setOnPreferenceClickListener(new android.support.v7.preference.Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(android.support.v7.preference.Preference preference) {
                presenter.feedback();
                return false;
            }
        });

        findPreference("donate").setOnPreferenceClickListener(new android.support.v7.preference.Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(android.support.v7.preference.Preference preference) {
                presenter.donate();
                return false;
            }
        });
    }

    @Override
    public void showBrowserNotFoundError() {
        Snackbar.make(toolbar, "没有找到浏览器", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showFeedbackError() {
        Snackbar.make(toolbar, "没有找到邮件应用", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showRateError() {
        Snackbar.make(toolbar,"没有市场应用",Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void initView(View view) {
        toolbar = (Toolbar) getActivity().findViewById(R.id.about_toolbar);
    }

    @Override
    public void setPresenter(AboutContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }
}
