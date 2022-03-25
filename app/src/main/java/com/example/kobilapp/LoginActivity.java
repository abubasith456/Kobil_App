package com.example.kobilapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.example.kobilapp.databinding.ActivityLoginBinding;
import com.example.kobilapp.utils.SharedPreference;
import com.example.kobilapp.viewModel.LoginActivityViewModel;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding activityLoginBinding;
    private LoginActivityViewModel loginActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        loginActivityViewModel = ViewModelProviders.of(this).get(LoginActivityViewModel.class);
        activityLoginBinding.setLoginActivityViewModel(loginActivityViewModel);
        loginActivityViewModel.getActivity(this);
        String from = getIntent().getStringExtra("from");
        String fingerPrintStatus = getIntent().getStringExtra("fingerPrintStatus");
//        if (from != null) {
//            if (from.equals("pinCodeFragment")) {
//                SharedPreference.getInstance().saveInt(getApplicationContext(), "showFingerId", "true");
//                loginActivityViewModel.setDashboardFragment(this, fingerPrintStatus);
//            } else {
//                SharedPreference.getInstance().saveInt(getApplicationContext(), "showFingerId", "false");
//                loginActivityViewModel.setLoginFragment(this);
//            }
//        } else {
            loginActivityViewModel.setLoginFragment(this);
            SharedPreference.getInstance().saveInt(getApplicationContext(), "showFingerId", "false");
//        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        loginActivityViewModel.checkMenuItems();
    }
}