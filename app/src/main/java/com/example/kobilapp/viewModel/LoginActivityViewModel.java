package com.example.kobilapp.viewModel;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.AndroidViewModel;

import com.example.kobilapp.LoginActivity;
import com.example.kobilapp.R;
import com.example.kobilapp.fragment.DashboardFragment;
import com.example.kobilapp.fragment.LoginFragment;
import com.example.kobilapp.utils.SharedPreference;

public class LoginActivityViewModel extends AndroidViewModel {

    public ObservableField<Boolean> fieldVisibility = new ObservableField<>();
    public ObservableField<Boolean> menuVisibility = new ObservableField<>();
    public ObservableField<Boolean> fingerIdVisibility = new ObservableField<>();
    public ObservableField<Boolean> fingerIdSwitch = new ObservableField<>();
    private LoginActivity loginActivity;

    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
        fieldVisibility.set(true);
        menuVisibility.set(false);
        fingerIdVisibility.set(false);
    }

    public void getActivity(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
    }

    public void setLoginFragment(LoginActivity loginActivity) {
        Fragment fragment = new LoginFragment();
        FragmentTransaction transaction = loginActivity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayoutLoginFragmentContainer, fragment);
        transaction.commit();
    }

    public void setDashboardFragment(LoginActivity loginActivity, String fingerPrintStatus) {
        fingerIdVisibility.set(true);
        Fragment fragment = DashboardFragment.newInstance();
        FragmentTransaction transaction = loginActivity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayoutLoginFragmentContainer, fragment);
        transaction.commitNow();
    }

    public void showMenu(View view) {
        fieldVisibility.set(false);
        menuVisibility.set(true);
    }

    public void closeMenu(View view) {
        fieldVisibility.set(true);
        menuVisibility.set(false);
    }

//    public void onCheckedChanged(Boolean isChecked) {
//        if (isChecked) {
//            AlertDialog.Builder builder
//                    = new AlertDialog.Builder(loginActivity);
//            builder.setTitle("Enter the PIN");
//            final View customLayout = loginActivity.getLayoutInflater().inflate(R.layout.custom_layout, null);
//            builder.setView(customLayout);
//            builder.setCancelable(false);
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    // send data from the
//                    // AlertDialog to the Activity
//                    EditText editText = customLayout.findViewById(R.id.editText);
//                    String pin = SharedPreference.getInstance().getValue(loginActivity, "pinCode");
//
//                    Toast.makeText(loginActivity, "E: " + editText.getText().toString() + " P: " + pin, Toast.LENGTH_SHORT).show();
//
//                    if (pin.equals(editText.getText().toString())) {
//                        fingerIdSwitch.set(true);
//                        SharedPreference.getInstance().saveInt(loginActivity, "fingerPrint", "success");
//                    } else {
//                        fingerIdSwitch.set(false);
//                    }
//                }
//            }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    fingerIdSwitch.set(false);
//                }
//            });
//            AlertDialog dialog = builder.create();
//            dialog.show();
//        } else {
//            SharedPreference.getInstance().saveInt(loginActivity, "fingerPrint", "cancelled");
//            Log.e("alert finger called=> ", "Called");
//            fingerIdSwitch.set(false);
//        }
//    }

    public void checkMenuItems() {
        //Finger layout
        String value = SharedPreference.getInstance().getValue(getApplication(), "showFingerId");
        Log.e("fingerPrintID=>", "status: " + value);
        if (value.equals("true")) {
            fingerIdVisibility.set(true);
        } else {
            fingerIdVisibility.set(false);
        }

        //Switch value
        String check = SharedPreference.getInstance().getValue(getApplication(), "fingerPrint");
        if (check.equals("success")) {
            fingerIdSwitch.set(true);
        }
    }


}
