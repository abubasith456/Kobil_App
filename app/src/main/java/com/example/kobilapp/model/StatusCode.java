package com.example.kobilapp.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StatusCode {
    private static StatusCode instance;
    private List<String> statusCode;
    private int errorCode;

    public static StatusCode getInstance() {
        if (instance == null) {
            instance = new StatusCode();
        }
        return instance;
    }

    public List<String> getStatusCode() {
        if (statusCode==null){
            statusCode=new ArrayList<>();
            statusCode.add("0");
            Log.e("getStatusCode: ", ""+"Zero added");
        }
        Log.e("getStatusCode: ", ""+statusCode.toString());
        return statusCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void getStatusCode(List<String> statusCode) {
        this.statusCode = statusCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
