package com.example.kobilapp.viewModel;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle;

import android.util.Log;
import android.view.View;

import com.example.kobilapp.AstListener;
import com.example.kobilapp.MainActivity;
import com.example.kobilapp.R;
import com.example.kobilapp.SdkListener;
import com.example.kobilapp.fragment.InitFragment;
import com.example.kobilapp.fragment.SideMenuFragment;
import com.example.kobilapp.UpdateApp;
import com.example.kobilapp.utils.SharedPreference;
import com.example.kobilapp.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kobil.midapp.ast.api.AstSdk;
import com.kobil.midapp.ast.api.enums.AstDeviceType;
import com.kobil.midapp.ast.api.enums.AstPropertyOwner;
import com.kobil.midapp.ast.api.enums.AstPropertyType;
import com.kobil.midapp.ast.sdk.AstSdkFactory;

import java.util.ArrayList;
import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    //    public ObservableField<Boolean> progressBarVisibility = new ObservableField<>();
//    public ObservableField<Boolean> startButtonVisibility = new ObservableField<>();
    public ObservableField<Boolean> menuVisibility = new ObservableField<>();
//    public ObservableField<Boolean> initScreenVisibility = new ObservableField<>();
//    public ObservableField<Boolean> frameLayoutFragmentVisibility = new ObservableField<>();

    @SuppressLint("StaticFieldLeak")
    private MainActivity activity;
    private final Utils utils = new Utils();
//    private SdkListener listener = new SdkListener();

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        AstListener.getInstance().setAstSdk(application.getApplicationContext());
        menuVisibility.set(true);
        sdkInit();
        registerUpdate();
        tokenFCM();

    }

    public void getActivity(MainActivity activity) {
        this.activity = activity;
    }

    private void sdkInit() {
        try {
//            String localization = "en";
//            byte[] version = new byte[]{2, 5, 0, 0, 0, 0};
//            String appName = getApplication().getString(R.string.app_name);
//            sdk.init(localization, version, appName);
            AstListener.getInstance().initSdk();
        } catch (Exception exception) {
            Log.e("Error==>", exception.getMessage());
        }
    }

    private void registerUpdate() {
        try {
            UpdateApp.getInstance().astUpdate(activity, AstListener.getInstance().getAstSdk());
//            AstUpdate astUpdate = sdk.registerUpdate(new AstUpdateListener() {
//                @Override
//                public void onUpdateBegin(AstDeviceType astDeviceType, AstUpdateStatus astUpdateStatus) {
//                    Log.e("onUpdateBegin", "AstUpdateStatus: " + astUpdateStatus);
//                }
//
//                @Override
//                public void onOpenInfoUrlEnd(AstDeviceType astDeviceType, AstUpdateStatus astUpdateStatus) {
//                    Log.e("onOpenInfoUrlEnd", "AstUpdateStatus: " + astUpdateStatus);
//                }
//
//                @Override
//                public void onUpdateInformationAvailable(AstDeviceType astDeviceType, AstStatus astStatus, String s, String s1, AstUpdateType astUpdateType, int i) {
//                    Log.e("onUpdateInformationAvailable", "Status: " + astStatus + " AstType: " + astDeviceType + " UpdateURL: " + s + " infoUrl: " + s1 + " ExpireIn: " + i);
//                    Status.getInstance().setUpdateUrl(s);
//                    Status.getInstance().setInfoUrl(s1);
//                }
//            });
////            updateListener.astUpdate(astUpdate);
//            listener.setAstUpdate(astUpdate);
        } catch (Exception exception) {
            Log.e("Error==>", exception.getMessage());
        }
    }

    private void tokenFCM() {
        try {
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (!task.isSuccessful()) {
                                Log.e("FCM token", "Fetching FCM registration token failed", task.getException());
                                return;
                            }
                            // Get new FCM registration token
                            String token = task.getResult();
                            Log.e("FCM token", "" + token);
                        }
                    });
        } catch (Exception exception) {
            Log.e("Error==>", exception.getMessage());
        }
    }

    public void showMenu(View view) {
        utils.hideSoftKeyboard(activity);
        Fragment fragment = new SideMenuFragment("LoginFragment");
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left);
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
//    }

}
