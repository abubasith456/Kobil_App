package com.example.kobilapp.viewModel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.AndroidViewModel;

import com.example.kobilapp.LoginActivity;
import com.example.kobilapp.R;
import com.example.kobilapp.fragment.LoginFragment;

public class LoginActivityViewModel extends AndroidViewModel {

    public ObservableField<Boolean> fieldVisibility = new ObservableField<>();
    public ObservableField<Boolean> menuVisibility = new ObservableField<>();

    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
        fieldVisibility.set(true);
        menuVisibility.set(false);
    }

    public void setLoginFragment(LoginActivity loginActivity) {
        Fragment fragment = new LoginFragment();
        FragmentTransaction transaction = loginActivity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayoutLoginFragmentContainer, fragment);
        transaction.commit();
    }

    public void showMenu(View view) {
        fieldVisibility.set(false);
        menuVisibility.set(true);
    }

    public void closeMenu(View view) {
        fieldVisibility.set(true);
        menuVisibility.set(false);
    }
}
