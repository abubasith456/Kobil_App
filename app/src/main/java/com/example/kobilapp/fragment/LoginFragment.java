package com.example.kobilapp.fragment;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kobilapp.R;
import com.example.kobilapp.databinding.LoginFragmentBinding;
import com.example.kobilapp.viewModel.LoginViewModel;

public class LoginFragment extends Fragment {

    private LoginViewModel mViewModel;
    private LoginFragmentBinding loginFragmentBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        loginFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.login_fragment, container, false);
        return loginFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loginFragmentBinding.setLoginViewModel(mViewModel);
    }

}