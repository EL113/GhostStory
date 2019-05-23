package com.yesong.ghoststory.homepage;

import com.yesong.ghoststory.bean.CommonResult;
import com.yesong.ghoststory.detail.DetailContract;
import com.yesong.ghoststory.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CommonCallback implements Callback {

    DetailContract.View view;

    public CommonCallback(DetailContract.View view){
        this.view = view;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        e.printStackTrace();
        view.showError();
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        String responseText = response.body().string();
        CommonResult result = Utility.resolveResponse(responseText, CommonResult.class);
        if (result.getCode() == null || result.getCode() != 0){
            view.showError();
        }
    }
}
