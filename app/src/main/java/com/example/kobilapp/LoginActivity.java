package com.example.kobilapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.example.kobilapp.databinding.ActivityLoginBinding;
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
        String from = getIntent().getStringExtra("from");
        if (from!=null){
            if (from.equals("pinCodeFragment")) {
                loginActivityViewModel.setDashboardFragment(this);
            } else {
                loginActivityViewModel.setLoginFragment(this);
            }
        }else {
            loginActivityViewModel.setLoginFragment(this);
        }
    }
}