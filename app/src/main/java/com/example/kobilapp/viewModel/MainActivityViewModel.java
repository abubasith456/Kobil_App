package com.example.kobilapp.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.AndroidViewModel;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.example.kobilapp.ActivationActivity;
import com.example.kobilapp.MainActivity;
import com.example.kobilapp.R;
import com.example.kobilapp.SdkListener;
import com.example.kobilapp.fragment.ActivationFragment;
import com.example.kobilapp.fragment.LoginFragment;
import com.example.kobilapp.fragment.SideMenuFragment;
import com.example.kobilapp.model.StatusCode;
import com.example.kobilapp.utils.SharedPreference;
import com.kobil.midapp.ast.api.AstSdk;
import com.kobil.midapp.ast.sdk.AstSdkFactory;

public class MainActivityViewModel extends AndroidViewModel {

    public ObservableField<Boolean> progressBarVisibility = new ObservableField<>();
    public ObservableField<Boolean> startButtonVisibility = new ObservableField<>();
    public ObservableField<Boolean> fieldVisibility = new ObservableField<>();
    public ObservableField<Boolean> initScreenVisibility = new ObservableField<>();
    public ObservableField<Boolean> frameLayoutFragmentVisibility = new ObservableField<>();
    private MainActivity activity;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        sdkInit();
        initScreenVisibility.set(true);
        frameLayoutFragmentVisibility.set(false);
        progressBarVisibility.set(true);
        startButtonVisibility.set(false);
        fieldVisibility.set(true);
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            progressBarVisibility.set(false);
            startButtonVisibility.set(true);
        }, 3000);

    }

    public void getActivity(MainActivity activity) {
        this.activity = activity;
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

    public void showMenu(View view) {
        Fragment fragment = new SideMenuFragment("LoginFragment");
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayoutForSideMenu, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void onStartClick(View view) {
        frameLayoutFragmentVisibility.set(true);
        initScreenVisibility.set(false);
        String value = StatusCode.getInstance().getStatusCode().get(0);
        if (value.equals("200")) {
            Fragment fragment = new LoginFragment();
            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayoutLoginFragmentContainer, fragment);
            transaction.commit();
            SharedPreference.getInstance().saveInt(getApplication(), "from", "LoginFragment");
        } else {
            Fragment fragment = new ActivationFragment();
            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayoutLoginFragmentContainer, fragment);
            transaction.commit();
        }

    }

}
