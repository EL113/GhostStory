package com.example.ghoststory.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Daniel hunt on 2017/3/30.
 */

public class DetailResult {
    public int getDetailResponseCode() {
        return detailResponseCode;
    }

    public void setDetailResponseCode(int detailResponseCode) {
        this.detailResponseCode = detailResponseCode;
    }

    public DetailBody getDetailBody() {
        return detailBody;
    }

    public void setDetailBody(DetailBody detailBody) {
        this.detailBody = detailBody;
    }

    @SerializedName("showapi_res_code")
    private int detailResponseCode;

    @SerializedName("showapi_res_body")
    private DetailBody detailBody;
}
