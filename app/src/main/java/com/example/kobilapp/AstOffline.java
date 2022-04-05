package com.example.kobilapp;

import android.content.Context;
import android.util.Log;

import com.kobil.midapp.ast.api.AstOfflineFunctions;
import com.kobil.midapp.ast.api.AstOfflineFunctionsListener;
import com.kobil.midapp.ast.api.AstSdk;
import com.kobil.midapp.ast.api.enums.AstStatus;

public class AstOffline {

    private static AstOfflineFunctions astOfflineFunctions;

    public AstOffline(){

    }

    public AstOfflineFunctions getAstFunction(Context context, AstSdk astSdk) {
        astOfflineFunctions = astSdk.doRegisterOfflineFunctions(new AstOfflineFunctionsListener() {
            @Override
            public void onProvidePinBegin() {
                Log.e("AstOfflineFunctions", "onProvidePinBegin called");
            }

            @Override
            public void onProvidePinEnd(AstStatus astStatus, int i, int i1) {
                Log.e("AstOfflineFunctions", "onProvidePinEnd called ==> Status: " + astStatus);
            }

            @Override
            public void onGenerateOtpEnd(AstStatus astStatus, String s) {
                Log.e("AstOfflineFunctions", "onGenerateOtpEnd called ==> Status: " + astStatus + " String: " + s);
            }

            @Override
            public void onGenerateSecureSequenceEnd(AstStatus astStatus, byte[] bytes) {
                Log.e("AstOfflineFunctions", "onGenerateSecureSequenceEnd called ==> Status: " + astStatus + " Bytes: " + bytes.toString());
            }

            @Override
            public void onGetReSynchronizationDataEnd(AstStatus astStatus, String s, int i) {
                Log.e("AstOfflineFunctions", "onGetReSynchronizationDataEnd called ==> Status: " + astStatus + " tring: " + s);
            }
        });
        return astOfflineFunctions;
    }

}
