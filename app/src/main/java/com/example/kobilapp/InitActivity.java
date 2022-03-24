package com.example.kobilapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.kobilapp.databinding.ActivityInitBinding;
import com.example.kobilapp.utils.AppLifecycle;
import com.example.kobilapp.viewModel.InitActivityViewModel;
import com.kobil.midapp.ast.api.AstSdk;
import com.kobil.midapp.ast.sdk.AstSdkFactory;

public class InitActivity extends AppCompatActivity {

    private ActivityInitBinding activityInitBinding;
    private InitActivityViewModel initActivityViewModel;
    private AppLifecycle appLifecycle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_init);
        activityInitBinding = DataBindingUtil.setContentView(this, R.layout.activity_init);
        initActivityViewModel = ViewModelProviders.of(this).get(InitActivityViewModel.class);
        activityInitBinding.setInitViewModel(initActivityViewModel);
        appLifecycle=new AppLifecycle();

    }

    @Override
    protected void onStart() {
        super.onStart();
//        appLifecycle.onStart();
    }


    @Override
    protected void onStop() {
        super.onStop();
//        appLifecycle.onStop();
    }
}