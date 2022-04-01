package com.example.kobilapp.viewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.example.kobilapp.SdkListener;
import com.kobil.midapp.ast.api.AstSdk;
import com.kobil.midapp.ast.api.enums.AstInformationResultStatus;
import com.kobil.midapp.ast.api.information.AstSsmsInformation;
import com.kobil.midapp.ast.api.information.result.AstSsmsNodeInformationResult;
import com.kobil.midapp.ast.sdk.AstSdkFactory;

public class DashboardViewModel extends AndroidViewModel {

    private AstSdk sdk;
    private SdkListener listener = new SdkListener();
    private AstSsmsNodeInformationResult astSsmsNodeInformationResult;
    private AstInformationResultStatus astInformationResultStatus;

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        sdk = AstSdkFactory.getSdk(getApplication(), listener);
        astSsmsNodeInformationResult = sdk.getInformation().getSsmsInformation().getSsmsNode();
        astInformationResultStatus = astSsmsNodeInformationResult.getResultStatus();
        String getResult = astSsmsNodeInformationResult.getResult();
        Log.e("Node ID of connected SSMS", getResult);
//        astInformationResultStatus = astSsmsInformation.getSsmsNode().getResultStatus();

    }
}