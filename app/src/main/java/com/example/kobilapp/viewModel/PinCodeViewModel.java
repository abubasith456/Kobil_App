package com.example.kobilapp.viewModel;

import android.app.Application;
import android.text.Editable;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.kobilapp.SdkListener;
import com.example.kobilapp.utils.SharedPreference;
import com.kobil.midapp.ast.api.AstSdk;
import com.kobil.midapp.ast.api.enums.AstDeviceType;
import com.kobil.midapp.ast.api.enums.AstStatus;
import com.kobil.midapp.ast.sdk.AstSdkFactory;

import java.util.Arrays;

public class PinCodeViewModel extends AndroidViewModel {

    public MutableLiveData<String> pin = new MutableLiveData<>();
    public MutableLiveData<String> confirmPin = new MutableLiveData<>();
    public ObservableField<String> confirmPinError = new ObservableField<>();
    public ObservableField<String> pinError1 = new ObservableField<>();
    public ObservableField<String> pinError2 = new ObservableField<>();
    public ObservableField<Boolean> confirmPinErrorVisibility = new ObservableField<>();
    public ObservableField<Boolean> pinError1Visibility = new ObservableField<>();
    public ObservableField<Boolean> pinError2Visibility = new ObservableField<>();
    private String userId;
    private char[] activationCode;

    public PinCodeViewModel(@NonNull Application application) {
        super(application);
        pin.setValue("");
        confirmPin.setValue("");
        confirmPinError.set("");
        confirmPinErrorVisibility.set(false);
        pinError1Visibility.set(true);
        pinError2Visibility.set(true);
        pinError1.set("PIN cannot empty.");
        pinError2.set("Minimum 8 digits.");
    }

    public void getValue(String userId, char[] activationCode) {
        this.userId = userId;
        this.activationCode = activationCode;

    }

    public void onPinTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() == 8) {
            pinError2.set("");
            pinError2Visibility.set(false);
        } else if (s.length() > 8) {
            pinError2.set("");
            pinError2Visibility.set(false);
        } else {
            pinError2.set("Minimum 8 digits.");
            pinError2Visibility.set(true);
        }
        if (s.length() != 0) {
            pinError1.set("");
            pinError1Visibility.set(false);
        } else {
            pinError1.set("PIN cannot empty.");
            pinError1Visibility.set(true);
        }

    }

    public void afterPinTextChanged(Editable editable) {

    }

    public void onSetPinClick(View view) {
        try {
            if (pin.getValue() != null && confirmPin.getValue() != null) {
                SdkListener listener = new SdkListener();
                AstSdk sdk = AstSdkFactory.getSdk(getApplication(), listener);
                char[] pinCode = pin.getValue().toCharArray();
                sdk.doActivation(AstDeviceType.VIRTUALDEVICE, pinCode, userId, activationCode);
                SharedPreference.getInstance().saveInt(getApplication(), "userId", userId);
            }
        } catch (Exception e) {
            Log.e("Error=> ", e.getMessage());
        }
    }
}