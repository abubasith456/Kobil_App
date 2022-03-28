package com.example.kobilapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.example.kobilapp.databinding.ActivityMainBinding;
import com.example.kobilapp.utils.AppLifecycle;
import com.example.kobilapp.viewModel.MainActivityViewModel;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding activityMainBinding;
    private MainActivityViewModel mainActivityViewModel;
    private AppLifecycle appLifecycle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_init);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        activityMainBinding.setInitViewModel(mainActivityViewModel);
//        activityInitBinding.setInitViewModel(mainActivityViewModel);
        mainActivityViewModel.getActivity(this);
        appLifecycle = new AppLifecycle();

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