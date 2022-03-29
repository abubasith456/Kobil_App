package com.example.kobilapp.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.AndroidViewModel;

import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.example.kobilapp.MainActivity;
import com.example.kobilapp.R;
import com.example.kobilapp.SdkListener;
import com.example.kobilapp.db.AppDatabase;
import com.example.kobilapp.fragment.ActivationFragment;
import com.example.kobilapp.fragment.InitFragment;
import com.example.kobilapp.fragment.LoginFragment;
import com.example.kobilapp.fragment.SideMenuFragment;
import com.example.kobilapp.fragment.UsersFragment;
import com.example.kobilapp.model.StatusCode;
import com.example.kobilapp.model.Users;
import com.example.kobilapp.utils.SharedPreference;
import com.example.kobilapp.utils.Utils;
import com.kobil.midapp.ast.api.AstSdk;
import com.kobil.midapp.ast.api.AstUpdateEventListener;
import com.kobil.midapp.ast.api.AstUpdateListener;
import com.kobil.midapp.ast.api.enums.AstDeviceType;
import com.kobil.midapp.ast.api.enums.AstStatus;
import com.kobil.midapp.ast.api.enums.AstUpdateStatus;
import com.kobil.midapp.ast.api.enums.AstUpdateType;
import com.kobil.midapp.ast.sdk.AstSdkFactory;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

//    public ObservableField<Boolean> progressBarVisibility = new ObservableField<>();
//    public ObservableField<Boolean> startButtonVisibility = new ObservableField<>();
//    public ObservableField<Boolean> fieldVisibility = new ObservableField<>();
//    public ObservableField<Boolean> initScreenVisibility = new ObservableField<>();
//    public ObservableField<Boolean> frameLayoutFragmentVisibility = new ObservableField<>();

    private MainActivity activity;
    private Utils utils = new Utils();

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        sdkInit();
        registerUpdate();
//        initScreenVisibility.set(true);
////        frameLayoutFragmentVisibility.set(false);
//        progressBarVisibility.set(true);
//        startButtonVisibility.set(false);
//        fieldVisibility.set(true);
//        Handler handler = new Handler();
//        handler.postDelayed(() -> {
//            progressBarVisibility.set(false);
//            startButtonVisibility.set(true);
//        }, 3000);

    }

    private void registerUpdate() {
        try {
            SdkListener listener = new SdkListener();
            AstSdk sdk = AstSdkFactory.getSdk(getApplication(), listener);
            sdk.registerUpdate(new AstUpdateListener() {
                @Override
                public void onUpdateBegin(AstDeviceType astDeviceType, AstUpdateStatus astUpdateStatus) {
                    Log.e("onUpdateBegin", "AstUpdateStatus: " + astUpdateStatus);
                }

                @Override
                public void onOpenInfoUrlEnd(AstDeviceType astDeviceType, AstUpdateStatus astUpdateStatus) {
                    Log.e("onOpenInfoUrlEnd", "AstUpdateStatus: " + astUpdateStatus);
                }

                @Override
                public void onUpdateInformationAvailable(AstDeviceType astDeviceType, AstStatus astStatus, String s, String s1, AstUpdateType astUpdateType, int i) {
                    Log.e("onUpdateInformationAvailable", "Status: " + astStatus + " AstType: " + astDeviceType + " ExpireIn: " + i);
                }
            });
        } catch (Exception exception) {
            Log.e("Error==>", exception.getMessage());
        }
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
        utils.hideSoftKeyboard(activity);
        Fragment fragment = new SideMenuFragment("LoginFragment");
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayoutForSideMenu, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void showInitFragment() {
        Fragment fragment = new InitFragment();
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayoutForSideMenu, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

//    public void onStartClick(View view) {
//        frameLayoutFragmentVisibility.set(true);
//        initScreenVisibility.set(false);
//        AppDatabase db = AppDatabase.getDbInstance(activity);
////        List<Users> usersList=db.userDao().getAllUsers();
//        List<String> value = StatusCode.getInstance().getList();
////        Log.e("RoomDb value:",usersList.toString());
//        if (value.size()>=2){
//            Fragment fragment = new UsersFragment();
//            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.frameLayoutLoginFragmentContainer, fragment);
//            transaction.commit();
//            SharedPreference.getInstance().saveValue(getApplication(), "from", "UsersFragment");
//        }
//        else if (value != null && !value.get(0).equals("0")) {
//            Fragment fragment = new LoginFragment();
//            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.frameLayoutLoginFragmentContainer, fragment);
//            transaction.commit();
//            SharedPreference.getInstance().saveValue(getApplication(), "from", "LoginFragment");
//            SharedPreference.getInstance().saveValue(getApplication(), "userId", value.get(0));
//        }
////        else if (usersList.size()>=2){
////            Fragment fragment = new UsersFragment();
////            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
////            transaction.replace(R.id.frameLayoutLoginFragmentContainer, fragment);
////            transaction.commit();
////            SharedPreference.getInstance().saveValue(getApplication(), "from", "UsersFragment");
////        }
//        else {
//            Fragment fragment = new ActivationFragment();
//            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.frameLayoutLoginFragmentContainer, fragment);
//            transaction.commit();
//            SharedPreference.getInstance().saveValue(getApplication(), "from", "ActivationFragment");
//        }
//
//    }

}
