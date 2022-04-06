package com.example.kobilapp.viewModel;

import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.databinding.ObservableField;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.AndroidViewModel;

import com.example.kobilapp.AstOffline;
import com.example.kobilapp.R;
import com.example.kobilapp.SdkListener;
import com.example.kobilapp.fragment.DashboardFragment;
import com.example.kobilapp.fragment.LoginFragment;
import com.example.kobilapp.model.Status;
import com.example.kobilapp.model.StatusMessage;
import com.example.kobilapp.utils.SharedPreference;
import com.example.kobilapp.UpdateApp;
import com.example.kobilapp.utils.Utils;
import com.kobil.midapp.ast.api.AstSdk;
import com.kobil.midapp.ast.api.enums.AstDeviceType;
import com.kobil.midapp.ast.api.enums.AstStatus;
import com.kobil.midapp.ast.sdk.AstSdkFactory;

import java.util.concurrent.Executor;

public class LoginViewModel extends AndroidViewModel {

    public ObservableField<String> pinCode = new ObservableField<>();
    public ObservableField<String> userId = new ObservableField<>();
    public ObservableField<String> pinError = new ObservableField<>();
    public ObservableField<Boolean> pinErrorVisibility = new ObservableField<>();
    private LoginFragment loginFragment;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private Executor executor;
    private ProgressDialog progressdialog;
    private String userIdFromDb;
    private final SdkListener listener = new SdkListener();
    Handler handler;
    private final AstSdk sdk;
    private final AstOffline astOffline;
    private int retryCount = 6;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        handler = new Handler();
        sdk = AstSdkFactory.getSdk(getApplication(), listener);
        astOffline = new AstOffline();
        pinCode.set("");
        pinError.set("");
        pinErrorVisibility.set(false);
//        String check = SharedPreference.getInstance().getValue(loginFragment.getContext(), "fingerPrint");
//        if (check.equals("success")) {
////            bioMetricInit();
//            Log.e("FingerPrint=>", "Assigned");
//        } else {
//            Log.e("FingerPrint=>", "Not assigned");
//        }
    }

    public void getFragment(LoginFragment loginFragment) {
        this.loginFragment = loginFragment;
        userId.set(SharedPreference.getInstance().getValue(loginFragment.getContext(), "userId"));
    }

    public void onPinTextChanged(CharSequence s, int start, int before, int count) {
        try {
            if (s.equals("")) {
                pinErrorVisibility.set(true);
                pinError.set("Please enter the PIN.");
            } else if (s.length() == 8) {
                pinErrorVisibility.set(false);
                pinError.set("");
            } else {
                pinErrorVisibility.set(true);
                pinError.set("Enter 8 digit pin.");
            }
        } catch (Exception e) {
            Log.e("Error=>", e.getMessage());
        }
    }

    public void onLoginClick(View view) {
        try {
            String pin = pinCode.get();
            if (!pin.equals("") && pin.length() == 8) {
                executeLogin("");
            } else if (pin.equals("")) {
                pinErrorVisibility.set(true);
                pinError.set("Please enter the PIN.");
            }
        } catch (Exception e) {
            Log.e("Error=>", e.getMessage());
        }
    }

    public void getUserInfo(String userId) {
        this.userIdFromDb = userId;
    }

    private void executeLogin(String from) {
        try {
            String pin = pinCode.get();
            Utils.getInstance().hideSoftKeyboard(loginFragment.getActivity());
            if (Utils.getInstance().isNetworkConnectionAvailable(loginFragment.getContext())) {
                pinErrorVisibility.set(false);
                pinError.set("");
                char[] pinChar;
                if (from.equals("fingerPrint")) {
                    String value = SharedPreference.getInstance().getValue(loginFragment.getContext(), "pinCode");
                    pinChar = value.toCharArray();
                } else {
                    pinChar = pin.toCharArray();
                }
                String userId = SharedPreference.getInstance().getValue(loginFragment.getContext(), "userId");
                sdk.doLogin(AstDeviceType.VIRTUALDEVICE, pinChar, userId);
                showProcessBar("Logging in please wait...");

                handler.postDelayed(this::showAlertDialog, 9000);
//                handler.postDelayed(() -> {
//
//                    if (StatusMessage.getInstance().getAstStatus() == AstStatus.OK) {
//                        try {
//                            sdk.doSetPropertyRequest(AstDeviceType.VIRTUALDEVICE);
//                            handler.postDelayed(() -> {
//                                String deviceName = SharedPreference.getInstance().getValue(loginFragment.getContext(), "deviceName");
//                                byte[] name = deviceName.getBytes();
//                                int flag = AstPropertyFlags.NONE;
//                                sdk.doSetProperty(AstDeviceType.VIRTUALDEVICE, "KS.DeviceName",
//                                        name,
//                                        AstPropertyType.UTF8STRING,
//                                        AstPropertyOwner.OWNER_DEVICE,
//                                        10, flag);
//                            }, 3000);
//                        } catch (Exception e) {
//                            Log.e("Error=> ", e.getMessage());
//                        }
//                    }
//                }, 5000);

            } else {
                Toast.makeText(getApplication(), "Please check the internet connection!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("Error=> ", e.getMessage());
        }
    }

    private void showAlertDialog() {
        try {
            progressdialog.dismiss();
            AlertDialog.Builder alert = new AlertDialog.Builder(loginFragment.getContext());
            if (StatusMessage.getInstance().getAstStatus() == AstStatus.OK) {
                alert.setMessage("Login successfully.");
                alert.setCancelable(false);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            pinCode.set("");
                            Fragment fragment = DashboardFragment.newInstance();
                            FragmentTransaction transaction = loginFragment.getParentFragmentManager().beginTransaction();
                            transaction.setCustomAnimations(  R.anim.slide_in_right,R.anim.slide_out_left);
                            transaction.replace(R.id.frameLayoutLoginFragmentContainer, fragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                            SharedPreference.getInstance().saveValue(getApplication(), "showFingerId", "true");
                            SharedPreference.getInstance().saveValue(getApplication(), "from", "DashboardFragment");
//                            doRegisterOfflineFunction();
//                            sdk.suspend();
                            setProperty();
                        } catch (Exception e) {
                            Log.e("Error=> ", e.getMessage());
                        }
                    }
                });
            } else if (StatusMessage.getInstance().getAstStatus() == AstStatus.UPDATE_AVAILABLE) {
                alert.setTitle("Update available!");
                alert.setMessage("Do you update your app version?");
                alert.setCancelable(false);
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        doUpdateAppAvailable();
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        pinCode.set("");
                        Fragment fragment = DashboardFragment.newInstance();
                        FragmentTransaction transaction = loginFragment.getParentFragmentManager().beginTransaction();
                        transaction.setCustomAnimations(  R.anim.slide_in_right,R.anim.slide_out_left,R.anim.slide_in_left,R.anim.slide_out_right);
                        transaction.replace(R.id.frameLayoutLoginFragmentContainer, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        SharedPreference.getInstance().saveValue(getApplication(), "showFingerId", "true");
                        SharedPreference.getInstance().saveValue(getApplication(), "from", "DashboardFragment");
                    }
                });
            } else if (StatusMessage.getInstance().getAstStatus() == AstStatus.WRONG_CREDENTIALS) {

                alert.setTitle("Error");
                alert.setMessage("Current PIN is invalid." + "\nAttempt left: " + Status.getInstance().getRetryCount());
                alert.setCancelable(false);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        pinCode.set("");
                    }
                });
            } else if (Status.getInstance().getErrorCode() == 30) {
                alert.setTitle("Error");
                alert.setMessage("Your device has been locked (too many attempts)." + "\nPlease contact our support desk.");
                alert.setCancelable(false);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        pinCode.set("");
                    }
                });
            } else if (Status.getInstance().getErrorCode() == 29) {
                alert.setTitle("Error");
                alert.setMessage("Your device has been locked." + "\nPlease contact our support desk.");
                alert.setCancelable(false);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        pinCode.set("");
                    }
                });
            } else {
                alert.setMessage(StatusMessage.getInstance().getStatusMessage());
                alert.setCancelable(false);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        pinCode.set("");
                    }
                });
            }
            AlertDialog alertDialog = alert.create();
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                }
            });
            alertDialog.show();
        } catch (Exception e) {
            Log.e("Error=> ", e.getMessage());
        }
    }

    private void doRegisterOfflineFunction() {
        try {
            astOffline.setAstOfflineFunction(loginFragment.getContext(), sdk);

        } catch (Exception e) {
            Log.e("Error=> ", e.getMessage());
        }
    }

    private void setProperty() {
        try {

        } catch (Exception e) {
            Log.e("Error=> ", e.getMessage());
        }
    }

    private void doUpdateAppAvailable() {
        try {
            UpdateApp updateApp = new UpdateApp();
            updateApp.getAstUpdate().doOpenInfoUrl(AstDeviceType.VIRTUALDEVICE);
//            loginFragment.requireActivity().finish();
//            System.exit(0);
        } catch (Exception e) {
            Log.e("doUpdateAppError=> ", e.getMessage());
        }
    }

    public void showFingerLogin(LoginFragment loginFragment) {
        BiometricManager biometricManager = BiometricManager.from(loginFragment.getContext());
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
        executor = ContextCompat.getMainExecutor(loginFragment.getContext());
        biometricPrompt = new BiometricPrompt(loginFragment, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Log.e("Fingerprint ==>", "onAuthenticationError: " + errString.toString());
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Log.e("Fingerprint ==>", "onAuthenticationSucceeded: " + result.getAuthenticationType());
                executeLogin("fingerPrint");
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
    }

    private void showProcessBar(String message) {
        progressdialog = new ProgressDialog(loginFragment.getContext(),R.style.MyAlertDialogStyle);
        progressdialog.setMessage(message);
        progressdialog.setCancelable(false);
        progressdialog.show();
    }
}