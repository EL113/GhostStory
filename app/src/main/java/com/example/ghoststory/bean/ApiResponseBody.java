package com.example.ghoststory.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Daniel hunt on 2017/3/26.
 */

public class ApiResponseBody {
    @SerializedName("ret_code")
    private int bodyCode;

    private Pagebean pagebean;

    @Override
    public String toString() {
        return "ApiResponseBody [bodyCode=" + bodyCode + ",pagebean=" + pagebean + "]";
    }

    public int getBodyCode() {
        return bodyCode;
    }

    public void setBodyCode(int bodyCode) {
        this.bodyCode = bodyCode;
    }

    public Pagebean getPagebean() {
        return pagebean;
    }

    public void setPagebean(Pagebean pagebean) {
        this.pagebean = pagebean;
    }
}
