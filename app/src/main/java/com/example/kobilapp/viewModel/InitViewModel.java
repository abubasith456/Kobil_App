package com.example.kobilapp.viewModel;

import android.app.Application;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.example.kobilapp.R;
import com.example.kobilapp.SdkListener;
import com.example.kobilapp.db.AppDatabase;
import com.example.kobilapp.fragment.ActivationFragment;
import com.example.kobilapp.fragment.InitFragment;
import com.example.kobilapp.fragment.LoginFragment;
import com.example.kobilapp.fragment.UsersFragment;
import com.example.kobilapp.model.StatusCode;
import com.example.kobilapp.utils.SharedPreference;
import com.kobil.midapp.ast.api.AstSdk;
import com.kobil.midapp.ast.sdk.AstSdkFactory;

import java.util.List;

public class InitViewModel extends AndroidViewModel {

    public ObservableField<Boolean> progressBarVisibility = new ObservableField<>();
    public ObservableField<Boolean> startButtonVisibility = new ObservableField<>();
    private InitFragment initFragment;

    public InitViewModel(@NonNull Application application) {
        super(application);
//        sdkInit();
        progressBarVisibility.set(true);
        startButtonVisibility.set(false);
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            progressBarVisibility.set(false);
            startButtonVisibility.set(true);
        }, 3000);
    }

    public void getFragment(InitFragment initFragment) {
        this.initFragment = initFragment;
    }

//    private void sdkInit() {
//        try {
//            SdkListener listener = new SdkListener();
//            AstSdk sdk = AstSdkFactory.getSdk(getApplication(), listener);
//            String localization = "en";
//            byte[] version = new byte[]{2, 5, 0, 0, 0, 0};
//            String appName = "Kobil App";
//            sdk.init(localization, version, appName);
//        } catch (Exception exception) {
//            Log.e("Error==>", exception.getMessage());
//        }
//    }

    public void onStartClick(View view) {
//        AppDatabase db = AppDatabase.getDbInstance();
//        List<Users> usersList=db.userDao().getAllUsers();
        List<String> value = StatusCode.getInstance().getList();
//        Log.e("RoomDb value:",usersList.toString());
        if (value.size() >= 2) {
            Fragment fragment = new UsersFragment();
            FragmentTransaction transaction = initFragment.getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayoutLoginFragmentContainer, fragment);
            transaction.commit();
            SharedPreference.getInstance().saveValue(getApplication(), "from", "UsersFragment");
            initFragment.getActivity().getSupportFragmentManager().popBackStack();
        } else if (value != null && !value. get(0).equals("0")) {
            Fragment fragment = new LoginFragment();
            FragmentTransaction transaction = initFragment.getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayoutLoginFragmentContainer, fragment);
            transaction.commit();
            SharedPreference.getInstance().saveValue(getApplication(), "from", "LoginFragment");
            SharedPreference.getInstance().saveValue(getApplication(), "userId", value.get(0));
            initFragment.getActivity().getSupportFragmentManager().popBackStack();
        }
        else {
            Fragment fragment = new ActivationFragment();
            FragmentTransaction transaction = initFragment.getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayoutLoginFragmentContainer, fragment);
            transaction.commit();
            SharedPreference.getInstance().saveValue(getApplication(), "from", "ActivationFragment");
            initFragment.getActivity().getSupportFragmentManager().popBackStack();
        }

    }
}