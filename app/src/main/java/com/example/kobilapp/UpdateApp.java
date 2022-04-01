package com.example.kobilapp;

import android.content.Context;
import android.util.Log;

import com.example.kobilapp.SdkListener;
import com.example.kobilapp.model.Status;
import com.kobil.midapp.ast.api.AstSdk;
import com.kobil.midapp.ast.api.AstUpdate;
import com.kobil.midapp.ast.api.AstUpdateListener;
import com.kobil.midapp.ast.api.enums.AstDeviceType;
import com.kobil.midapp.ast.api.enums.AstStatus;
import com.kobil.midapp.ast.api.enums.AstUpdateStatus;
import com.kobil.midapp.ast.api.enums.AstUpdateType;
import com.kobil.midapp.ast.sdk.AstSdkFactory;

public class UpdateApp {

    private static AstUpdate astUpdate;

    public void astUpdate(Context context, AstSdk sdk) {
        astUpdate = sdk.registerUpdate(new AstUpdateListener() {
            @Override
            public void onUpdateBegin(AstDeviceType astDeviceType, AstUpdateStatus astUpdateStatus) {
                Log.e("onUpdateBegin", "AstUpdateStatus: " + astUpdateStatus);
            }

            @Override
            public void onOpenInfoUrlEnd(AstDeviceType astDeviceType, AstUpdateStatus astUpdateStatus) {
                Log.e("onOpenInfoUrlEnd", "AstUpdateStatus: " + astUpdateStatus);
            }

            @Override
            public void onUpdateInformationAvailable(AstDeviceType astDeviceType, AstStatus astStatus, String s, String s1, AstUpdateType astUpdateType, int i) {
                Log.e("onUpdateInformationAvailable", "Status: " + astStatus + " AstType: " + astDeviceType + " UpdateURL: " + s + " infoUrl: " + s1 + " ExpireIn: " + i);
                Status.getInstance().setUpdateUrl(s);
                Status.getInstance().setInfoUrl(s1);
            }
        });
    }

    public AstUpdate getAstUpdate() {
        return astUpdate;
    }
}
