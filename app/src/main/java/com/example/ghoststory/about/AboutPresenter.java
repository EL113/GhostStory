package com.example.ghoststory.about;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;

import com.example.ghoststory.R;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Daniel hunt on 2017/4/1.
 */

public class AboutPresenter implements AboutContract.Presenter {
    private AboutContract.View view;
    private Context context;
    private SharedPreferences sp;

    public AboutPresenter(Context context, AboutContract.View view) {
        this.context = context;
        this.view = view;
        this.view.setPresenter(this);
        sp = context.getSharedPreferences("user_settings",MODE_PRIVATE);
    }

    @Override
    public void donate() {
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setTitle(context.getString(R.string.donate));
        dialog.setMessage(context.getString(R.string.donate_content));
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.postive), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 将指定账号添加到剪切板
                // add the alipay account to clipboard
                ClipboardManager manager = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("text", context.getString(R.string.donate_account));
                manager.setPrimaryClip(clipData);
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(R.string.negative), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }

    @Override
    public void feedback() {
        try {
            Uri uri = Uri.parse(context.getString(R.string.send_to));
            Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
            intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.mail_topic));
            intent.putExtra(Intent.EXTRA_TEXT,
                    context.getString(R.string.device_model) + Build.MODEL + "\n"
                            + context.getString(R.string.sdk_version) + Build.VERSION.RELEASE + "\n"
                            + context.getString(R.string.version));
            context.startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            view.showFeedbackError();

        }
    }

    @Override
    public void followOnGithub() {
        try{
            context.startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse( context.getString(R.string.github_url))));
        } catch (android.content.ActivityNotFoundException ex){
            view.showBrowserNotFoundError();
        }
    }

    @Override
    public void rate() {
        try {
            Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex){
            view.showRateError();
        }

    }

    @Override
    public void start() {

    }
}
