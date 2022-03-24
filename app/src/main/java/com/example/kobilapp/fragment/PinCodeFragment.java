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
import com.example.kobilapp.databinding.PincodeFragmentBinding;
import com.example.kobilapp.viewModel.PinCodeViewModel;

public class PinCodeFragment extends Fragment {

    public PinCodeFragment() {

    }

    public PinCodeFragment(String userId, char[] activationCode) {
        this.userId = userId;
        this.activationCode = activationCode;
    }

    private String userId;
    private char[] activationCode;
    private PinCodeViewModel mViewModel;
    private PincodeFragmentBinding pincodeFragmentBinding;

    public static PinCodeFragment newInstance() {
        return new PinCodeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        pincodeFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.pincode_fragment, container, false);
        return pincodeFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PinCodeViewModel.class);
        pincodeFragmentBinding.setPinCodeViewModel(mViewModel);
        mViewModel.getValue(userId,activationCode);
        mViewModel.getFragment(this);
        // TODO: Use the ViewModel
    }

}