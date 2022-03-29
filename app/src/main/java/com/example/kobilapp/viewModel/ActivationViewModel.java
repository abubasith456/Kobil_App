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

    public MutableLiveData<String> userId = new MutableLiveData<>();
    public MutableLiveData<String> activationCode = new MutableLiveData<>();
    public ObservableField<String> userIdError = new ObservableField<>();
    public ObservableField<String> activationCodeError = new ObservableField<>();
    public ObservableField<Boolean> userIdErrorVisibility = new ObservableField<>();
    public ObservableField<Boolean> activationErrorVisibility = new ObservableField<>();
    private FragmentActivity activity;

    public ActivationViewModel(@NonNull Application application) {
        super(application);
        userId.setValue("");
        activationCode.setValue("");
        userIdErrorVisibility.set(false);
        activationErrorVisibility.set(false);
    }

    public void getFragment(FragmentActivity activity) {
        this.activity = activity;
    }

    public void onActivationClick(View view) {
        try {
            String id = userId.getValue();
            char[] actCode = Objects.requireNonNull(activationCode.getValue()).toCharArray();
            Log.e("print code=>", Arrays.toString(actCode));
//            SdkListener listener = new SdkListener();
//            AstSdk sdk = AstSdkFactory.getSdk(getApplication(), listener);
//            sdk.doActivation();
            if (validate()) {
                Fragment fragment = new PinCodeFragment(id, actCode);
                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayoutLoginFragmentContainer, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }


        } catch (Exception e) {
            Log.e("Error==> ", e.getMessage());
        }
    }

    private boolean validate() {
        Boolean valid = true;
        String id = userId.getValue();
        char[] actCode = Objects.requireNonNull(activationCode.getValue()).toCharArray();
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