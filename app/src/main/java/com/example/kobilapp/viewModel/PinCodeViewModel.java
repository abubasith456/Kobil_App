package com.example.kobilapp.viewModel;

import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.DialogInterface;

import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;

import android.os.Handler;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.ObservableField;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.AndroidViewModel;

import com.example.kobilapp.R;
import com.example.kobilapp.SdkListener;
import com.example.kobilapp.db.AppDatabase;
import com.example.kobilapp.fragment.LoginFragment;
import com.example.kobilapp.fragment.UsersFragment;
import com.example.kobilapp.model.Status;
import com.example.kobilapp.model.StatusMessage;
import com.example.kobilapp.utils.UpdateApp;
import com.example.kobilapp.model.Users;
import com.example.kobilapp.utils.SharedPreference;
import com.kobil.midapp.ast.api.AstSdk;
import com.kobil.midapp.ast.api.enums.AstDeviceType;
import com.kobil.midapp.ast.sdk.AstSdkFactory;

import java.util.List;
import java.util.concurrent.Executor;

public class PinCodeViewModel extends AndroidViewModel {

    public ObservableField<String> pin = new ObservableField<>();
    public ObservableField<String> confirmPin = new ObservableField<>();
    public ObservableField<String> confirmPinError = new ObservableField<>();
    public ObservableField<String> pinError1 = new ObservableField<>();
    public ObservableField<String> pinError2 = new ObservableField<>();
    public ObservableField<Boolean> confirmPinErrorVisibility = new ObservableField<>();
    public ObservableField<Boolean> pinError1Visibility = new ObservableField<>();
    public ObservableField<Boolean> pinError2Visibility = new ObservableField<>();
    private String userId;
    private char[] activationCode;
    private FragmentActivity pinCodeFragment;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private Executor executor;
    private ProgressDialog progressdialog;

    public PinCodeViewModel(@NonNull Application application) {
        super(application);
        pin.set("");
        confirmPin.set("");
        confirmPinError.set("");
        confirmPinErrorVisibility.set(false);
        pinError1Visibility.set(true);
        pinError2Visibility.set(true);
        pinError1.set("PIN cannot empty.");
        pinError2.set("Minimum 8 digits.");
    }

    public void getFragment(FragmentActivity pinCodeFragment) {
        this.pinCodeFragment = pinCodeFragment;
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

    public void onConfirmPinTextChanged(CharSequence s, int start, int before, int count) {
        String confirmPin = s.toString();
        if (pin.get().equals(confirmPin)) {
            confirmPinErrorVisibility.set(false);
            confirmPinError.set("");
        } else {
            confirmPinErrorVisibility.set(true);
            confirmPinError.set("PIN not matched!");
        }
    }

    public void afterPinTextChanged(Editable editable) {

    }

    public void onSetPinClick(View view) {
        try {
            if (isValidate()) {
                showFingerPrintAccess();
            }
        } catch (Exception e) {
            Log.e("Error=> ", e.getMessage());
        }
    }

    private Boolean isValidate() {
        boolean valid = true;
        if (pin.get().equals("")) {
            pinError1Visibility.set(true);
            pinError1.set("Please enter the PIN.");
            valid = false;
        }
        if (confirmPin.get().equals("")) {
            confirmPinErrorVisibility.set(true);
            confirmPinError.set("Please enter the PIN.");
            valid = false;
        }
        return valid;
    }


    private void showFingerPrintAccess() {
        try {

            BiometricManager biometricManager = BiometricManager.from(pinCodeFragment);
            switch (biometricManager.canAuthenticate()) {
                case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                    Toast.makeText(getApplication(), "Device does not have Fingerprint", Toast.LENGTH_SHORT).show();
                    break;

                case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                    Toast.makeText(getApplication(), "Not working", Toast.LENGTH_SHORT).show();
                    break;

                case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                    Toast.makeText(getApplication(), "No finger print assigned", Toast.LENGTH_SHORT).show();
                    break;

                case BiometricManager.BIOMETRIC_STATUS_UNKNOWN:
                    Toast.makeText(getApplication(), "Unknown person", Toast.LENGTH_SHORT).show();
                    break;
            }
            executor = ContextCompat.getMainExecutor(pinCodeFragment);
            biometricPrompt = new BiometricPrompt(pinCodeFragment, executor, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                    Log.e("Fingerprint ==>", "onAuthenticationError: " + errString.toString());
                    executeActivation("cancelled");
                }

                @Override
                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    Log.e("Fingerprint ==>", "onAuthenticationSucceeded: " + result.getAuthenticationType());
                    executeActivation("success");
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    Log.e("Fingerprint ==>", "onAuthenticationFailed");
                }
            });
            promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Test app")
                    .setDescription("Use your fingerprint to unlock")
                    .setDeviceCredentialAllowed(true)
                    .build();

            biometricPrompt.authenticate(promptInfo);

        } catch (Exception e) {
            Log.e("Error fingerPrint=>", e.getMessage());
        }
    }

    private void executeActivation(String fingerPrintStatus) {
        try {
            showProcessBar("Activating Please wait...");
            if (fingerPrintStatus.equals("success")) {
                SharedPreference.getInstance().saveValue(getApplication(), "fingerPrint", "success");
            } else {
                SharedPreference.getInstance().saveValue(getApplication(), "fingerPrint", "cancelled");
            }
            SdkListener listener = new SdkListener();
            AstSdk sdk = AstSdkFactory.getSdk(getApplication(), listener);
            char[] pinCode = pin.get().toCharArray();
            sdk.doActivation(AstDeviceType.VIRTUALDEVICE, pinCode, userId, activationCode);
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                if (StatusMessage.getInstance().getStatus().equals("ok")) {
                    progressdialog.dismiss();
                    Log.e("executeActivation", "Called==> true part");
                    SharedPreference.getInstance().saveValue(getApplication(), "userId", userId);
                    SharedPreference.getInstance().saveValue(getApplication(), "pinCode", pin.get());
                    AlertDialog.Builder alert = new AlertDialog.Builder(pinCodeFragment);
                    alert.setMessage(StatusMessage.getInstance().getStatus());
                    alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            List<String> value = Status.getInstance().getList();
                            Log.e("Value:=>", "" + value.size());
                            if (value.size() >= 2) {
                                Fragment fragment = new UsersFragment();
                                FragmentTransaction transaction = pinCodeFragment.getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.frameLayoutLoginFragmentContainer, fragment);
                                transaction.commit();
                                SharedPreference.getInstance().saveValue(getApplication(), "from", "LoginFragment");
                            } else {
                                Fragment fragment = new LoginFragment();
                                FragmentTransaction transaction = pinCodeFragment.getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.frameLayoutLoginFragmentContainer, fragment);
                                transaction.commit();
                                SharedPreference.getInstance().saveValue(getApplication(), "from", "LoginFragment");
                            }
//                            addToDb(userId, pin.get());
                        }
                    });
                    AlertDialog alertDialog = alert.create();
                    alertDialog.show();

                } else if (StatusMessage.getInstance().getStatus().equals("Update necessary!.")) {
                    progressdialog.dismiss();
                    Log.e("executeActivation", "Called==> false part");
                    SharedPreference.getInstance().saveValue(getApplication(), "fingerPrint", "cancelled");
                    AlertDialog.Builder alert = new AlertDialog.Builder(pinCodeFragment);
                    alert.setTitle("Mandatory update available!");
                    alert.setMessage("Do you update your app version?");
                    alert.setCancelable(false);
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                                UpdateApp updateApp = new UpdateApp();
                                updateApp.getAstUpdate().doOpenInfoUrl(AstDeviceType.VIRTUALDEVICE);
                                pinCodeFragment.finish();
                                System.exit(0);
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            pinCodeFragment.finish();
                            System.exit(0);
                        }
                    });
                    AlertDialog alertDialog = alert.create();
                    alertDialog.show();
                } else {
                    progressdialog.dismiss();
                    Log.e("executeActivation", "Called==> false part");
                    SharedPreference.getInstance().saveValue(getApplication(), "fingerPrint", "cancelled");
                    AlertDialog.Builder alert = new AlertDialog.Builder(pinCodeFragment);
                    alert.setMessage(StatusMessage.getInstance().getStatus());
                    if (StatusMessage.getInstance().getStatus().equals("Update necessary!.")) {
                        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                pinCodeFragment.finish();
                                System.exit(0);
                            }
                        });
                    }
                    alert.setNegativeButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (StatusMessage.getInstance().getStatus().equals("Update necessary!.")) {
                                UpdateApp updateApp = new UpdateApp();
                                updateApp.getAstUpdate().doOpenInfoUrl(AstDeviceType.VIRTUALDEVICE);
                            }
                        }
                    });
                    AlertDialog alertDialog = alert.create();
                    alertDialog.show();
                }
            }, 4000);
        } catch (Exception e) {
            Log.e("Error fingerPrint=>", e.getMessage());
        }
    }

    private void addToDb(String userId, String pin) {
        try {
            AppDatabase db = AppDatabase.getDbInstance(this.getApplication());
            Users users = new Users();
            users.user_id = userId;
            users.pin_code = pin;
            db.userDao().insertUsers(users);
            Log.e("Room DB", "Data added.");
        } catch (Exception e) {
            Log.e("Error=> ", e.getMessage());
        }
    }

    private void showProcessBar(String message) {
        progressdialog = new ProgressDialog((pinCodeFragment));
        progressdialog.setMessage(message);
        progressdialog.show();
    }
}