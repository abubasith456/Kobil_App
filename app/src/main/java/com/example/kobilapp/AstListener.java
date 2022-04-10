package com.example.kobilapp;

import android.content.Context;

import com.kobil.midapp.ast.api.AstSdk;
import com.kobil.midapp.ast.sdk.AstSdkFactory;

public class AstListener {

    private static AstSdk astSdk;
    private static AstListener instance;
    private Context context;

    public static AstListener getInstance() {
        if (instance == null) {
            instance = new AstListener();
        }
        return instance;
    }

    public void setAstSdk(Context context) {
        this.context = context;
        astSdk = AstSdkFactory.getSdk(context, SdkListener.getInstance());
    }

    public AstSdk getAstSdk() {
        return astSdk;
    }

    public void initSdk() {
        String localization = "en";
        byte[] version = new byte[]{2, 5, 0, 0, 0, 0};
        String appName = context.getString(R.string.app_name);
        astSdk.init(localization, version, appName);
    }
}
