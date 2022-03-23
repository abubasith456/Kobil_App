package com.example.kobilapp.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.example.kobilapp.ActivationActivity;
import com.example.kobilapp.SdkListener;
import com.example.kobilapp.model.StatusCode;
import com.example.kobilapp.model.StatusModel;
import com.kobil.midapp.ast.api.AstSdk;
import com.kobil.midapp.ast.sdk.AstSdkFactory;

import java.util.logging.LogRecord;

public class InitActivityViewModel extends AndroidViewModel {

    public ObservableField<Boolean> progressBarVisibility = new ObservableField<>();
    public ObservableField<Boolean> startButtonVisibility = new ObservableField<>();

    public InitActivityViewModel(@NonNull Application application) {
        super(application);
        progressBarVisibility.set(true);
        startButtonVisibility.set(false);
        sdkInit();

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            progressBarVisibility.set(false);
            startButtonVisibility.set(true);
            Log.e("Status check==>", new StatusCode().getStatusCode());
        }, 2000);
    }

    private void sdkInit() {
        try {
            SdkListener listener = new SdkListener();
            AstSdk sdk = AstSdkFactory.getSdk(getApplication(), listener);
            String localization = "en";
            byte[] version = new byte[]{2, 5, 0, 0, 0, 0};
            String appName = "Kobil App";
            sdk.init(localization, version, appName);
        } catch (Exception exception) {
            Log.e("Error==>", exception.getMessage());
        }
    }

    public void onStartClick(View view) {
        Intent intent = new Intent(getApplication(), ActivationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplication().startActivity(intent);
    }

}
