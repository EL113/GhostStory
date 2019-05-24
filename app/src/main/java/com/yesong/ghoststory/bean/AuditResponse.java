package com.yesong.ghoststory.bean;

import java.util.List;

public class AuditResponse {
    private int code;

    private List<String> resultList;

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public List<String> getResultList() {
        return resultList;
    }

    public void setResultList(List<String> resultList) {
        this.resultList = resultList;
    }
}
