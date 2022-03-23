package com.example.kobilapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.example.kobilapp.databinding.ActivityActivationBinding;
import com.example.kobilapp.viewModel.ActivationActivityViewModel;

public class ActivationActivity extends AppCompatActivity {

    private ActivationActivityViewModel activationActivityViewModel;
    private ActivityActivationBinding activityActivationBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_activation);
        activityActivationBinding = DataBindingUtil.setContentView(this, R.layout.activity_activation);
        activationActivityViewModel = ViewModelProviders.of(this).get(ActivationActivityViewModel.class);
        activityActivationBinding.setActivationViewModel(activationActivityViewModel);
        activationActivityViewModel.showActivationPage(this);
    }
}