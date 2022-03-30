package com.example.kobilapp.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Status {
    private static Status instance;
    private List<String> statusCode;
    private int errorCode;
    private String updateUrl = null;
    private String infoUrl = null;

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
        Log.e("getList Called", "" + statusCode.toString());
        return statusCode;
    }

    //Error code
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    //Update url
    public String getUpdateUrl() {
        return updateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }

    public String getInfoUrl() {
        return infoUrl;
    }

    public void setInfoUrl(String infoUrl) {
        this.infoUrl = infoUrl;
    }
}
