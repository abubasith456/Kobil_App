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
import com.example.kobilapp.databinding.ChangePinFragmentBinding;
import com.example.kobilapp.viewModel.ChangePinViewModel;

public class ChangePinFragment extends Fragment {

    private ChangePinViewModel mViewModel;
    private ChangePinFragmentBinding changePinFragmentBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        changePinFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.change_pin_fragment, container, false);
        return changePinFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ChangePinViewModel.class);
        changePinFragmentBinding.setChangePinViewModel(mViewModel);
        mViewModel.getFragment(this);

    }

}