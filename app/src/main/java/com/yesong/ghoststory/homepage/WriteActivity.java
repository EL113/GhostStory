package com.yesong.ghoststory.homepage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yesong.ghoststory.R;
import com.yesong.ghoststory.bean.CommonResult;
import com.yesong.ghoststory.bean.ContentList;
import com.yesong.ghoststory.db.DbContentList;
import com.yesong.ghoststory.detail.DetailActivity;
import com.yesong.ghoststory.util.APIUtil;
import com.yesong.ghoststory.util.HttpUtil;
import com.yesong.ghoststory.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WriteActivity extends AppCompatActivity {
    private EditText title;
    private EditText author;
    private EditText content;
    private DbContentList contentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        initView();
    }

    private void initView(){
        Toolbar toolbar = findViewById(R.id.write_toolbar);
        author = findViewById(R.id.write_author);
        title = findViewById(R.id.write_title);
        content = findViewById(R.id.content);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String type = intent.getStringExtra("type");
        if (id != null && type != null)
            fillData(id, type);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_write, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_send){
            publishStory();
        } else {
            finish();
        }
        return true;
    }

    private void publishStory() {
        String authorId = getAuthorId();
        String storyTitle = title.getText().toString();
        String storyAuthor = author.getText().toString();
        String storyContent = content.getText().toString();

        String storyMessage = emptyCheck(storyTitle, storyAuthor, storyContent);
        if (storyMessage != null) {
            Toast.makeText(WriteActivity.this, storyMessage, Toast.LENGTH_SHORT).show();
            return;
        }

        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("title", storyTitle);
        jsonObject.addProperty("author", storyAuthor);
        jsonObject.addProperty("content", storyContent);
        jsonObject.addProperty("authorId", authorId);
        jsonObject.addProperty("type", "yc");
        jsonObject.addProperty("content", storyContent);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

        HttpUtil.post(APIUtil.publishStory(), requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                CommonResult result = Utility.resolveResponse(responseText, CommonResult.class);
                if (result.getCode() != 0) {
                    Toast.makeText(WriteActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                    return;
                }

                String contentString = jsonObject.get("content").getAsString();
                if (contentItem == null){
                    contentItem = new DbContentList();
                }
                contentItem.setIdContent(result.getResult());
                contentItem.setTitle(jsonObject.get("title").getAsString());
                contentItem.setAuthor(jsonObject.get("author").getAsString());
                contentItem.setDesc(contentString.length() > 50 ? contentString.substring(0, 50) : contentString);
                contentItem.setTypeName("yc");
                contentItem.setText(jsonObject.get("content").getAsString());
                contentItem.setAuthorId(jsonObject.get("authorId").getAsString());
                contentItem.setAuditStatus("0");
                contentItem.save();
                finish();
            }
        });
    }

    private String emptyCheck(String storyTitle, String storyAuthor, String storyContent) {
        String storyMessage = null;
        if (storyTitle.isEmpty())
            storyMessage = "标题不能为空";

        if (storyAuthor.isEmpty())
            storyMessage = "作者不能为空";

        if (storyContent.isEmpty())
            storyMessage = "内容不能为空";

        return storyMessage;
    }

    private String getAuthorId(){
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        String authorId = pref.getString("authorId", null);
        if (authorId == null){
            authorId = saveAuthorId();
        }
        return authorId;
    }

    private String saveAuthorId(){
        String authorId = String.valueOf(System.currentTimeMillis());
        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
        editor.putString("authorId", authorId);
        editor.apply();
        return  authorId;
    }

    private void fillData(String id, String type) {
        contentItem = DataSupport.where("idContent = ? and typeName = ?", id, type).find(DbContentList.class).get(0);
        title.setText(contentItem.getTitle());
        author.setText(contentItem.getAuthor());
        content.setText(contentItem.getText());
    }
}
