package com.example.ghoststory.bean;

import com.example.ghoststory.db.DbContentList;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import okhttp3.ResponseBody;

/**
 * Created by Daniel hunt on 2017/3/25.
 */

public class RequestResult {
    @SerializedName("showapi_res_error")
    private String responseError;

    @SerializedName("showapi_res_code")
    private int responseCode;

    @SerializedName("showapi_res_body")
    private ApiResponseBody responseBody;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public ApiResponseBody getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(ApiResponseBody responseBody) {
        this.responseBody = responseBody;
    }

    public String getResponseError() {
        return responseError;
    }

    public void setResponseError(String responseError) {
        this.responseError = responseError;
    }

    @Override
    public String toString() {
        return "RequestResult [responseError=" + responseError + ",responseCode=" + responseCode
                + ",responseBody=" + responseBody + "]";
    }
}
