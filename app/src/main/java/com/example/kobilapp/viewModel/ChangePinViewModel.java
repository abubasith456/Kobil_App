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
import androidx.lifecycle.AndroidViewModel;

import com.example.kobilapp.R;
import com.example.kobilapp.SdkListener;
import com.example.kobilapp.fragment.ChangePinFragment;
import com.example.kobilapp.model.Status;
import com.example.kobilapp.model.StatusMessage;
import com.example.kobilapp.utils.Utils;
import com.kobil.midapp.ast.api.AstSdk;
import com.kobil.midapp.ast.api.enums.AstConfirmation;
import com.kobil.midapp.ast.api.enums.AstDeviceType;
import com.kobil.midapp.ast.api.enums.AstStatus;
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
    boolean valid = true;
    private AstConfirmation astConfirmation;

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
        if (StatusMessage.getInstance().getAstStatus() == AstStatus.OK) {
            astConfirmation = AstConfirmation.OK;
        } else {
            astConfirmation = AstConfirmation.CANCEL;
        }
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
                sdk.doPinChange(AstDeviceType.VIRTUALDEVICE, astConfirmation, currentPIN, newPIN);
                showProcessBar("Changing PIN.....");
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    progressdialog.dismiss();
                    alert = new AlertDialog.Builder(changePinFragment.getActivity());
                    if (StatusMessage.getInstance().getAstStatus() == AstStatus.OK) {
                        alert.setMessage("PIN changed successfully.");
                        alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                changePinFragment.getActivity().getSupportFragmentManager().popBackStackImmediate();
                            }
                        });
                    } else if (StatusMessage.getInstance().getAstStatus() == AstStatus.INVALID_PIN || StatusMessage.getInstance().getAstStatus() == AstStatus.INVALID_STATE) {
                        alert.setTitle("Error");
                        alert.setMessage("PIN is incorrect. " + "\nAttempt left: " + Status.getInstance().getRetryCount());
                        alert.setCancelable(false);
                        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                    } else if (Status.getInstance().getErrorCode() == 30) {
                        alert.setTitle("Error");
                        alert.setMessage("Your device has been locked (too many attempts)." + "\nPlease contact our support desk.");
                        alert.setCancelable(false);
                        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                    } else if (Status.getInstance().getErrorCode() == 29) {
                        alert.setTitle("Error");
                        alert.setMessage("Your device has been locked." + "\nPlease contact our support desk.");
                        alert.setCancelable(false);
                        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                    } else {
                        alert = new AlertDialog.Builder(changePinFragment.getActivity());
                        alert.setMessage(StatusMessage.getInstance().getStatusMessage());
                        alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        alertDialog = alert.create();
                        alertDialog.show();
                    }
                    alertDialog = alert.create();
                    alertDialog.show();
                }, 2000);
            }
        } catch (Exception e) {
            Log.e("Error=>", e.getMessage());
        }

    }

    private void showProcessBar(String message) {
        progressdialog = new ProgressDialog(changePinFragment.getActivity(), R.style.MyAlertDialogStyle);
        progressdialog.setMessage(message);
        progressdialog.setCancelable(false);
        progressdialog.show();
    }

    public Boolean isValidate() {
        if (currentPin.get().equals("")) {
            currentPinErrorVisibility.set(true);
            currentPinError.set("Please enter the current PIN.");
            valid = false;
        } else {
            currentPinErrorVisibility.set(false);
            currentPinError.set("");
            valid = true;
        }
        if (newConfirmPin.get().equals("")) {
            newConfirmPinErrorVisibility.set(true);
            newConfirmPinError.set("Please enter the new confirm PIN.");
            valid = false;
        } else {
            currentPinErrorVisibility.set(false);
            currentPinError.set("");
            valid = true;
        }
        return valid;
    }

    public void onPinTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() == 8) {
            newPinError2.set("");
            newPinError2Visibility.set(false);
            valid = true;
        } else if (s.length() > 8) {
            newPinError2.set("");
            newPinError2Visibility.set(false);
            valid = true;
        } else {
            newPinError2.set("Minimum 8 digits.");
            newPinError2Visibility.set(true);
            valid = false;
        }
        if (s.length() != 0) {
            newPinError1.set("");
            newPinError1Visibility.set(false);
            valid = true;
        } else {
            newPinError1.set("PIN cannot empty.");
            newPinError1Visibility.set(true);
            valid = false;
        }
    }

    public void onConfirmPinTextChanged(CharSequence s, int start, int before, int count) {
        String confirmPin = s.toString();
        if (newPin.get().equals(confirmPin)) {
            newConfirmPinErrorVisibility.set(true);
            newConfirmPinError.set("PIN matched!");
            valid = true;
        } else {
            newConfirmPinErrorVisibility.set(true);
            newConfirmPinError.set("PIN not matched!");
            valid = false;
        }
    }


}