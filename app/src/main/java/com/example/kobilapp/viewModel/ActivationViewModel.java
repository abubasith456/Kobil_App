package com.example.kobilapp.viewModel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.kobilapp.R;
import com.example.kobilapp.SdkListener;
import com.example.kobilapp.fragment.ActivationFragment;
import com.example.kobilapp.fragment.PinCodeFragment;
import com.example.kobilapp.utils.SharedPreference;
import com.kobil.midapp.ast.api.AstSdk;
import com.kobil.midapp.ast.sdk.AstSdkFactory;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

public class ActivationViewModel extends AndroidViewModel {

    public ObservableField<String> userId = new ObservableField<>();
    public ObservableField<String> activationCode = new ObservableField<>();
    public ObservableField<String> userIdError = new ObservableField<>();
    public ObservableField<String> activationCodeError = new ObservableField<>();
    public ObservableField<Boolean> userIdErrorVisibility = new ObservableField<>();
    public ObservableField<Boolean> activationErrorVisibility = new ObservableField<>();
    private FragmentActivity activity;

    public ActivationViewModel(@NonNull Application application) {
        super(application);
        userId.set("");
        activationCode.set("");
        userIdErrorVisibility.set(false);
        activationErrorVisibility.set(false);
    }

    public void getFragment(FragmentActivity activity) {
        this.activity = activity;
    }

    public void onActivationClick(View view) {
        try {
            String id = userId.get();
            char[] actCode = Objects.requireNonNull(activationCode.get()).toCharArray();
            Log.e("print code=>", Arrays.toString(actCode));
//            SdkListener listener = new SdkListener();
//            AstSdk sdk = AstSdkFactory.getSdk(getApplication(), listener);
//            sdk.doActivation();
            if (validate()) {
                Fragment fragment = new PinCodeFragment(id, actCode);
                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(  R.anim.slide_in_right,R.anim.slide_out_left,R.anim.slide_in_left,R.anim.slide_out_right);
                transaction.replace(R.id.frameLayoutForSideMenu, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                resetFields();
            }
        } catch (Exception e) {
            Log.e("Error==> ", e.getMessage());
        }
    }

    private void resetFields() {
        userId.set("");
        userIdError.set("");
        userIdErrorVisibility.set(false);
        activationCode.set("");
        activationCodeError.set("");
        activationErrorVisibility.set(false);
    }

    private boolean validate() {
        Boolean valid = true;
        String id = userId.get();
        char[] actCode = Objects.requireNonNull(activationCode.get()).toCharArray();
        Log.e("print code=>", Arrays.toString(actCode));
        try {
            if (id.equals("")) {
                userIdErrorVisibility.set(true);
                userIdError.set("Enter the username.");
                valid = false;
            } else {
                userIdErrorVisibility.set(false);
                userIdError.set("");
            }
            if (actCode.length == 0) {
                activationErrorVisibility.set(true);
                activationCodeError.set("Enter the activation code.");
                valid = false;
            } else {
                activationErrorVisibility.set(false);
                activationCodeError.set("");
            }
        } catch (Exception e) {
            Log.e("Error==> ", e.getMessage());
        }
        return valid;
    }


}