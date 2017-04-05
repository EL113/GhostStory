package com.example.ghoststory.detail;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;

import com.example.ghoststory.R;
import com.example.ghoststory.bean.ContentList;
import com.example.ghoststory.bean.DetailResult;
import com.example.ghoststory.db.DbContentList;
import com.example.ghoststory.util.API;
import com.example.ghoststory.util.HttpUtil;
import com.example.ghoststory.util.NetworkState;
import com.example.ghoststory.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by Daniel hunt on 2017/3/30.
 */

public class DetailPresenter implements DetailContract.Presenter{
    private DetailContract.View view;
    private Context context;
    private String idContent;
    private DbContentList item;
    private String now;
    private String textDetail = " ";
    private int allPages;
    private String currentPage = "1";


    public DetailPresenter(Context context, DetailContract.View view) {
        this.context = context;
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void start() {
        List<DbContentList> queryList = DataSupport.where("idContent=?", idContent).find(DbContentList.class);
        item = queryList.get(0);
        if (queryIfTextExist()) {
            textDetail = item.getText();
            currentPage = item.getCurrentPage();
            allPages = Integer.valueOf(item.getMaxPage());
            view.setData(item.getTitle(), item.getImg(), textDetail);
        } else {
            loadPost("1");
        }
    }

    @Override
    public void loadPost(String page) {

        if (NetworkState.networkConnected(context)) {
                getNow();
                String url = API.STORY_CONTENT + "id=" + idContent + API.STORY_PAGE + page + API.STORY_CONTENT_API_ID + now + API.API_SIGN;

                    HttpUtil.sendOkHttpRequest(url, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            textDetail = "Error";
                            view.showError();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                            //执行完try里面的语句后没有执行方法最后面两句，必须在try里面调用view.setData()把数据传递出去；
                            try {
                                String responseText = response.body().string();
                                DetailResult detailResult = Utility.handleStoryDetailData(responseText);
                                allPages = detailResult.getDetailBody().getAllPages();
                                currentPage = detailResult.getDetailBody().getCurrentPage();
                                String queryText = detailResult.getDetailBody().getText();
                                textDetail = textDetail + "\n"+ queryText;

                                DbContentList updateItem = new DbContentList();
                                updateItem.setCurrentPage(currentPage);
                                updateItem.setMaxPage(String.valueOf(allPages));
                                updateItem.setText(textDetail);
                                updateItem.updateAll("idContent=?", idContent);
                                view.setData(item.getTitle(),item.getImg(),textDetail);
                            } catch (Exception e) {
                                e.printStackTrace();
                                textDetail = "Parse Error";
                                view.showError();
                            }
                        }
                    });
                }else {
                textDetail = "please connect network";
                view.showLinkError();
        }
        view.setData(item.getTitle(),item.getImg(),textDetail);

        view.stopLoading();
    }

    @Override
    public void loadMore() {
        int nowPage = Integer.valueOf(currentPage);
        if (nowPage < allPages) {
            loadPost(String.valueOf(nowPage + 1));
        } else {
            view.showMaxPage();
        }
    }

    @Override
    public void setIdContent(String idContent) {
        this.idContent = idContent;
    }

    @Override
    public void addToOrDeleteFromBookmarks() {
        DbContentList updateItem = new DbContentList();
        if (item.getIsBookmarked().equals("1")) {
            updateItem.setIsBookmarked("0");
            updateItem.updateAll("idContent = ?", idContent);

            view.showDeletedFromBookmarks();
        } else {
            updateItem.setIsBookmarked("1");
            updateItem.updateAll("idContent = ?", idContent);

            view.showAddedToBookmarks();
        }
    }

    @Override
    public void copyLink() {

        if (item.getLink().equals("")) {
            view.showCopyTextError();
            return;
        } else {
            ClipboardManager manager = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
            ClipData clipData = null;
            clipData = ClipData.newPlainText("copied link",item.getLink());
            manager.setPrimaryClip(clipData);

            view.showLinkCopied();
        }
    }

    @Override
    public void copyText() {

        if (item.getText().equals("")) {
            view.showCopyTextError();
            return;
        } else {
            ClipboardManager manager = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
            ClipData clipData = null;
            clipData = ClipData.newPlainText("text", item.getText());

            manager.setPrimaryClip(clipData);
            view.showTextCopied();
        }
    }

    @Override
    public void openInBrowser() {
        if (item.getLink().equals("")) {
            view.showLinkError();
            return;
        }

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(item.getLink()));
            context.startActivity(intent);
        }catch (android.content.ActivityNotFoundException ex){
            view.showBrowserNotFoundError();
        }

    }

    @Override
    public void shareAsText() {
        if (item.getLink().equals("")) {
            view.showShareError();
            return;
        }

        try {
            Intent shareIntent = new Intent().setAction(Intent.ACTION_SEND).setType("text/plain");

            String shareText =  "" + item.getTitle() + " "+ item.getLink()+ "\t\t\t" + context.getString(R.string.share_extra);

            shareIntent.putExtra(Intent.EXTRA_TEXT,shareText);
            context.startActivity(Intent.createChooser(shareIntent,context.getString(R.string.share_to)));
        } catch (android.content.ActivityNotFoundException ex){
            view.showShareError();
        }

    }

    @Override
    public boolean queryIfIsBookmarked() {
        if (item.getIsBookmarked().equals("1")) {
            return true;
        } else {
            return false;
        }
    }

    public void getNow() {
        Calendar c = Calendar.getInstance();
        String second =String.valueOf(c.get(Calendar.SECOND)) ;
        int secondNumber=c.get(Calendar.SECOND);
        if (secondNumber >= 0 && secondNumber < 10) {
            second = "0" + second;
        }
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
        String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        int hourNumber=c.get(Calendar.HOUR_OF_DAY);
        if (hourNumber >= 0 && hourNumber < 10) {
            hour = "0"+hour;
        }
        String minute = String.valueOf(c.get(Calendar.MINUTE));
        int minuteNumber=c.get(Calendar.MINUTE);
        if (minuteNumber >= 0 && minuteNumber < 10) {
            minute = "0" + minute;
        }

        now = year + month + date + hour + minute + second;
    }

    public boolean queryIfTextExist() {
        try {
            String text = item.getText();
            if (!text.equals("")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
