package com.example.kobilapp.model;

import java.util.ArrayList;
import java.util.List;

public class Status {
    private static Status instance;
    private List<String> statusCode;
    private int errorCode;
    private String updateUrl = null;
    private String infoUrl = null;
    private int retryCount = 5;

    public static Status getInstance() {
        if (instance == null) {
            instance = new Status();
        }
        return instance;
    }

    //User List
    public void setList(List<String> statusCode) {
        if (statusCode.size() == 0) {
            statusCode = null;
        }
        this.statusCode = statusCode;
    }

    public List<String> getList() {
        if (statusCode == null) {
            statusCode = new ArrayList<>();
            statusCode.add("0");
        }
        return statusCode;
    }

    //Error code
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    //Update URL
    public String getUpdateUrl() {
        return updateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }

    //Update URL info
    public String getInfoUrl() {
        return infoUrl;
    }

    public void setInfoUrl(String infoUrl) {
        this.infoUrl = infoUrl;
    }

    //Retry code
    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }
}
