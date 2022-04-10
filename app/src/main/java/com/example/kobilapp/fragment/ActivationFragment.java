package com.example.kobilapp.fragment;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kobilapp.R;
import com.example.kobilapp.databinding.ActivationFragmentBinding;
import com.example.kobilapp.viewModel.ActivationViewModel;

public class ActivationFragment extends Fragment {

    private ActivationViewModel mViewModel;
    private ActivationFragmentBinding activationFragmentBinding;

    public static ActivationFragment newInstance() {
        return new ActivationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        activationFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.activation_fragment, container, false);
        return activationFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ActivationViewModel.class);
        activationFragmentBinding.setActivationViewModel(mViewModel);
        mViewModel.getFragment(getActivity());
        activationFragmentBinding.editTextUserName.setTransformationMethod(new PasswordTransformationMethod());

        // TODO: Use the ViewModel
    }

}