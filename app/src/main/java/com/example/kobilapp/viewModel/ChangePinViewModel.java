package com.example.kobilapp.viewModel;

import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.AndroidViewModel;

import com.example.kobilapp.R;
import com.example.kobilapp.SdkListener;
import com.example.kobilapp.fragment.ChangePinFragment;
import com.example.kobilapp.fragment.LoginFragment;
import com.example.kobilapp.model.StatusMessage;
import com.example.kobilapp.utils.SharedPreference;
import com.example.kobilapp.utils.Utils;
import com.kobil.midapp.ast.api.AstSdk;
import com.kobil.midapp.ast.api.enums.AstConfirmation;
import com.kobil.midapp.ast.api.enums.AstDeviceType;
import com.kobil.midapp.ast.sdk.AstSdkFactory;

public class ChangePinViewModel extends AndroidViewModel {

    public ObservableField<String> currentPin = new ObservableField<>();
    public ObservableField<String> newPin = new ObservableField<>();
    public ObservableField<String> newConfirmPin = new ObservableField<>();
    public ObservableField<String> currentPinError = new ObservableField<>();
    public ObservableField<String> newPinError1 = new ObservableField<>();
    public ObservableField<String> newPinError2 = new ObservableField<>();
    public ObservableField<String> newConfirmPinError = new ObservableField<>();
    public ObservableField<Boolean> currentPinErrorVisibility = new ObservableField<>();
    public ObservableField<Boolean> newPinError1Visibility = new ObservableField<>();
    public ObservableField<Boolean> newPinError2Visibility = new ObservableField<>();
    public ObservableField<Boolean> newConfirmPinErrorVisibility = new ObservableField<>();
    private ChangePinFragment changePinFragment;
    private SdkListener listener;
    private AstSdk sdk;
    private ProgressDialog progressdialog;
    private AlertDialog.Builder alert;
    private AlertDialog alertDialog;

    public ChangePinViewModel(@NonNull Application application) {
        super(application);
        currentPin.set("");
        newPin.set("");
        newConfirmPin.set("");
        newPinError1Visibility.set(true);
        newPinError2Visibility.set(true);
        newPinError1.set("PIN cannot empty.");
        newPinError2.set("Minimum 8 digits.");
        listener = new SdkListener();
        sdk = AstSdkFactory.getSdk(getApplication(), listener);
        sdk.doPinChangeRequest(AstDeviceType.VIRTUALDEVICE);
    }

    public void getFragment(ChangePinFragment changePinFragment) {
        this.changePinFragment = changePinFragment;
    }

    public void onBackPressed(View view) {
        try {
            changePinFragment.getActivity().getSupportFragmentManager().popBackStackImmediate();
        } catch (Exception e) {
            Log.e("Error=>", e.getMessage());
        }
    }

    public void onChangePinClick(View view) {
        try {
            if (isValidate()) {
                Utils.getInstance().hideSoftKeyboard(changePinFragment.getActivity());
                char[] currentPIN = currentPin.get().toCharArray();
                char[] newPIN = newPin.get().toCharArray();
                sdk.doPinChange(AstDeviceType.VIRTUALDEVICE, AstConfirmation.OK, currentPIN, newPIN);
                showProcessBar("Changing PIN.....");
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    progressdialog.dismiss();
                    if (StatusMessage.getInstance().getStatus().equals("ok")) {
                        alert = new AlertDialog.Builder(changePinFragment.getActivity());
                        alert.setMessage(StatusMessage.getInstance().getStatus());
                        alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                changePinFragment.getActivity().getSupportFragmentManager().popBackStackImmediate();
                            }
                        });
                        alertDialog = alert.create();
                        alertDialog.show();
                    } else {
                        alert = new AlertDialog.Builder(changePinFragment.getActivity());
                        alert.setMessage(StatusMessage.getInstance().getStatus());
                        alert.setNegativeButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        alertDialog = alert.create();
                        alertDialog.show();
                    }
                }, 2000);
            }
        } catch (Exception e) {
            Log.e("Error=>", e.getMessage());
        }

    }

    private void showProcessBar(String message) {
        progressdialog = new ProgressDialog(changePinFragment.getActivity());
        progressdialog.setMessage(message);
        progressdialog.setCancelable(false);
        progressdialog.show();
    }

    public Boolean isValidate() {
        Boolean valid = true;

        if (currentPin.get().equals("")) {
            currentPinErrorVisibility.set(true);
            currentPinError.set("Please enter the current PIN.");
        } else {
            currentPinErrorVisibility.set(false);
            currentPinError.set("");
        }
        if (newConfirmPin.get().equals("")) {
            newConfirmPinErrorVisibility.set(true);
            newConfirmPinError.set("Please enter the new confirm PIN.");
        } else {
            currentPinErrorVisibility.set(false);
            currentPinError.set("");
        }

        return valid;
    }

    public void onPinTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() == 8) {
            newPinError2.set("");
            newPinError2Visibility.set(false);
        } else if (s.length() > 8) {
            newPinError2.set("");
            newPinError2Visibility.set(false);
        } else {
            newPinError2.set("Minimum 8 digits.");
            newPinError2Visibility.set(true);
        }
        if (s.length() != 0) {
            newPinError1.set("");
            newPinError1Visibility.set(false);
        } else {
            newPinError1.set("PIN cannot empty.");
            newPinError1Visibility.set(true);
        }
    }

    public void onConfirmPinTextChanged(CharSequence s, int start, int before, int count) {
        String confirmPin = s.toString();
        if (newPin.get().equals(confirmPin)) {
            newConfirmPinErrorVisibility.set(true);
            newConfirmPinError.set("PIN matched!");
        } else {
            newConfirmPinErrorVisibility.set(true);
            newConfirmPinError.set("PIN not matched!");
        }
    }


}