package com.example.kobilapp.viewModel;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.kobilapp.LoginActivity;
import com.example.kobilapp.SdkListener;
import com.example.kobilapp.fragment.PinCodeFragment;
import com.example.kobilapp.model.StatusMessage;
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
    private PinCodeFragment pinCodeFragment;

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
                if (StatusMessage.getInstance().getStatus() == 0) {
                    SharedPreference.getInstance().saveInt(getApplication(), "userId", userId);
                    AlertDialog.Builder alert = new AlertDialog.Builder(pinCodeFragment.getContext());
                    alert.setMessage(StatusMessage.getInstance().getMessage());
                    alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getApplication(), "Move to login page", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplication(), LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getApplication().startActivity(intent);
                        }
                    });

                } else if (StatusMessage.getInstance().getStatus() == 12) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(pinCodeFragment.getContext());
                    alert.setMessage(StatusMessage.getInstance().getMessage());
                    alert.setNegativeButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                }
            }
        } catch (Exception e) {
            Log.e("Error=> ", e.getMessage());
        }
    }

    public void getFragment(PinCodeFragment pinCodeFragment) {
        this.pinCodeFragment = pinCodeFragment;
    }
}