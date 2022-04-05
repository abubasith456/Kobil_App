package com.example.kobilapp.viewModel;

import android.app.Application;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.example.kobilapp.SdkListener;
import com.example.kobilapp.model.StatusMessage;
import com.kobil.midapp.ast.api.AstSdk;
import com.kobil.midapp.ast.api.enums.AstDeviceType;
import com.kobil.midapp.ast.api.enums.AstInformationResultStatus;
import com.kobil.midapp.ast.api.enums.AstPropertyOwner;
import com.kobil.midapp.ast.api.enums.AstStatus;
import com.kobil.midapp.ast.api.information.AstSsmsInformation;
import com.kobil.midapp.ast.api.information.result.AstSsmsNodeInformationResult;
import com.kobil.midapp.ast.sdk.AstSdkFactory;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class DashboardViewModel extends AndroidViewModel {

    public ObservableField<String> deviceName = new ObservableField<>();
    private AstSdk sdk;
    private SdkListener listener = new SdkListener();
    private AstSsmsNodeInformationResult astSsmsNodeInformationResult;
    private AstInformationResultStatus astInformationResultStatus;
    private Handler handler;

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        handler = new Handler();
        sdk = AstSdkFactory.getSdk(getApplication(), listener);
        astSsmsNodeInformationResult = sdk.getInformation().getSsmsInformation().getSsmsNode();
        astInformationResultStatus = astSsmsNodeInformationResult.getResultStatus();
        String getResult = astSsmsNodeInformationResult.getResult();
        Log.e("Node ID of connected SSMS", getResult);
//        astInformationResultStatus = astSsmsInformation.getSsmsNode().getResultStatus();
        getProperty();
    }

    private void getProperty() {
        try {
            sdk.doGetPropertyRequest(AstDeviceType.VIRTUALDEVICE);

            handler.postDelayed(() -> {
                if (StatusMessage.getInstance().getAstStatus() == AstStatus.OK) {
                    sdk.doGetProperty(AstDeviceType.VIRTUALDEVICE, "KS.DeviceName", AstPropertyOwner.OWNER_DEVICE);
                    handler.postDelayed(() -> {
                        byte[] resultValue = StatusMessage.getInstance().getPropertyValue();
                        String deviceName = new String(resultValue, StandardCharsets.UTF_8);
                        this.deviceName.set("Device Name: "+deviceName);
                    }, 2000);
                }
            }, 1000);
        } catch (Exception e) {
            Log.e("Error=> ", e.getMessage());
        }
    }
}