package com.example.kobilapp.model;

import android.util.Log;

import java.util.List;

public class StatusCode {
private static StatusCode instance;
    //    private String statusCode;
    private List<String> statusCode;

    public static StatusCode getInstance(){
        if (instance==null){
            instance=new StatusCode();
        }
        return instance;
    }


    public List<String> getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(List<String> statusCode) {
        this.statusCode = statusCode;
        Log.e("setStatusCode= ", statusCode.toString());
    }
}
