package com.example.kobilapp.viewModel;

import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.inputmethodservice.KeyboardView;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.kobilapp.AstUpdate;
import com.example.kobilapp.LoginActivity;
import com.example.kobilapp.R;
import com.example.kobilapp.SdkListener;
import com.example.kobilapp.fragment.DashboardFragment;
import com.example.kobilapp.fragment.LoginFragment;
import com.example.kobilapp.model.StatusCode;
import com.example.kobilapp.model.StatusMessage;
import com.example.kobilapp.utils.SharedPreference;
import com.example.kobilapp.utils.Utils;
import com.kobil.midapp.ast.api.AstSdk;
import com.kobil.midapp.ast.api.enums.AstDeviceType;
import com.kobil.midapp.ast.api.enums.AstStatus;
import com.kobil.midapp.ast.sdk.AstSdkFactory;

public class LoginViewModel extends AndroidViewModel {

    public ObservableField<String> pinCode = new ObservableField<>();
    public ObservableField<String> userId = new ObservableField<>();
    public ObservableField<String> pinError = new ObservableField<>();
    public ObservableField<Boolean> pinErrorVisibility = new ObservableField<>();
    private LoginFragment loginFragment;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        pinCode.set("");
        pinError.set("");
        pinErrorVisibility.set(false);
    }

    public void getFragment(LoginFragment loginFragment) {
        this.loginFragment = loginFragment;
        userId.set(SharedPreference.getInstance().getValue(loginFragment.getContext(), "userId"));
    }

    public void onPinTextChanged(CharSequence s, int start, int before, int count) {
        try {
            if (s.length() == 8) {
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
                Utils.getInstance().hideSoftKeyboard(loginFragment.getActivity());
                if (Utils.getInstance().isNetworkConnectionAvailable(loginFragment.getContext())) {
                    pinErrorVisibility.set(false);
                    pinError.set("");
                    char[] pinChar = pin.toCharArray();
                    String userId = SharedPreference.getInstance().getValue(loginFragment.getContext(), "userId");
                    SdkListener listener = new SdkListener();
                    AstSdk sdk = AstSdkFactory.getSdk(getApplication(), listener);
                    sdk.doLogin(AstDeviceType.VIRTUALDEVICE, pinChar, userId);
                    ProgressDialog progressdialog = new ProgressDialog((loginFragment.getContext()));
                    progressdialog.setMessage("Please Wait....");
                    progressdialog.setCancelable(false);
                    progressdialog.show();
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        try {
                            progressdialog.dismiss();
                            showDialog();


//                            if (StatusMessage.getInstance().getStatus().equals("ok")) {
//                                progressdialog.dismiss();
//                                AlertDialog.Builder alert = new AlertDialog.Builder(loginFragment.getContext());
//                                alert.setMessage("Success!");
//                                alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        pinCode.set("");
//                                        Fragment fragment = DashboardFragment.newInstance();
//                                        FragmentTransaction transaction = loginFragment.getParentFragmentManager().beginTransaction();
//                                        transaction.replace(R.id.frameLayoutLoginFragmentContainer, fragment);
//                                        transaction.commit();
//                                    }
//                                });
//                                AlertDialog alertDialog = alert.create();
//                                alertDialog.show();
//                            } else if (StatusCode.getInstance().getErrorCode() == 29) {
//                                progressdialog.dismiss();
//                                AlertDialog.Builder alert = new AlertDialog.Builder(loginFragment.getContext());
//                                alert.setMessage("Something wrong please!");
//                                alert.setNegativeButton("ok", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        pinCode.set("");
//                                    }
//                                });
//                                AlertDialog alertDialog = alert.create();
//                                alertDialog.show();
//                            } else {
//                                progressdialog.dismiss();
//                            }
                        } catch (Exception e) {
                            Log.e("Error=> ", e.getMessage());
                        }
                    }, 4000);
                } else {
                    Toast.makeText(getApplication(), "Please check the internet connection!", Toast.LENGTH_SHORT).show();
                }
            } else if (pin.equals("")) {
                pinErrorVisibility.set(true);
                pinError.set("Enter the pin");
            }
        } catch (Exception e) {
            Log.e("Error=>", e.getMessage());
        }
    }

    private void showDialog() {
        Toast.makeText(getApplication(),
                "Error code => " + StatusCode.getInstance().getErrorCode() + " " +
                        "Status =>" + StatusMessage.getInstance().getStatus(), Toast.LENGTH_SHORT).show();
        AlertDialog.Builder alert = new AlertDialog.Builder(loginFragment.getContext());
        alert.setMessage(StatusMessage.getInstance().getStatus());
        alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    if (StatusMessage.getInstance().getStatus().equals("ok")) {
                        pinCode.set("");
                        Fragment fragment = DashboardFragment.newInstance();
                        FragmentTransaction transaction = loginFragment.getParentFragmentManager().beginTransaction();
                        transaction.replace(R.id.frameLayoutLoginFragmentContainer, fragment);
                        transaction.commit();
                    }
                } catch (Exception e) {
                    Log.e("Error=> ", e.getMessage());
                }
            }
        });
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }


}