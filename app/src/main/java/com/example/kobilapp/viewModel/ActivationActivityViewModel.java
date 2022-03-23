package com.example.kobilapp.viewModel;

import android.app.Application;
import android.app.FragmentManager;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.AndroidViewModel;

import com.example.kobilapp.ActivationActivity;
import com.example.kobilapp.R;
import com.example.kobilapp.fragment.ActivationFragment;

public class ActivationActivityViewModel extends AndroidViewModel {

    private ActivationActivity activationActivity;
    public ObservableField<Boolean> menuVisibility = new ObservableField<>();
    public ObservableField<Boolean> fieldVisibility = new ObservableField<>();

    public ActivationActivityViewModel(@NonNull Application application) {
        super(application);
        menuVisibility.set(false);
        fieldVisibility.set(true);
    }

    public void showActivationPage(ActivationActivity activationActivity) {
        this.activationActivity = activationActivity;
        Fragment fragment = new ActivationFragment();
        FragmentTransaction transaction = activationActivity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayoutFragmentContainer, fragment);
        transaction.commit();
    }

    public void showMenu(View view) {
        menuVisibility.set(true);
        fieldVisibility.set(false);
    }

    public void closeMenu(View view) {
        menuVisibility.set(false);
        fieldVisibility.set(true);
    }


}
