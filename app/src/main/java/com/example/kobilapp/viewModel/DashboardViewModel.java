package com.example.kobilapp.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.example.kobilapp.SdkListener;
import com.kobil.midapp.ast.api.AstSdk;
import com.kobil.midapp.ast.api.enums.AstInformationResultStatus;
import com.kobil.midapp.ast.api.information.AstSsmsInformation;
import com.kobil.midapp.ast.sdk.AstSdkFactory;

public class DashboardViewModel extends AndroidViewModel {

    private AstSdk sdk;
    private SdkListener listener = new SdkListener();
    private AstSsmsInformation astSsmsInformation;
    private AstInformationResultStatus astInformationResultStatus;

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        sdk = AstSdkFactory.getSdk(getApplication(), listener);
        astSsmsInformation = (AstSsmsInformation) sdk.getInformation().getSsmsInformation().getSsmsNode();
        astInformationResultStatus = astSsmsInformation.getSsmsNode().getResultStatus();
        String getResult=astSsmsInformation.getSsmsNode().getResult();
    }
}