package com.yesong.ghoststory.bean;

import java.util.List;

public class AuditResponse {
    private int code;

    private List<ContentList> result;

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public List<ContentList> getResult() {
        return result;
    }

    public void setResult(List<ContentList> result) {
        this.result = result;
    }
}
