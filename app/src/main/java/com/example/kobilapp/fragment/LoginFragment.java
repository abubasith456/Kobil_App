package com.example.kobilapp.fragment;

import androidx.activity.OnBackPressedCallback;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.kobilapp.R;
import com.example.kobilapp.databinding.LoginFragmentBinding;
import com.example.kobilapp.utils.SharedPreference;
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
        mViewModel.getFragment(this);

        //For fingerprint access
        String check = SharedPreference.getInstance().getValue(getContext(), "fingerPrint");
        if (check.equals("success")) {
            mViewModel.showFingerLogin(this);
        } else {
            Log.e("FingerPrint=>", "Not assigned");
        }

        //Back press handle..
        String from = SharedPreference.getInstance().getValue(getContext(), "from");
        if (from.equals("LoginFragment")) {
            requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    Toast.makeText(getActivity(), "Back pressed", Toast.LENGTH_SHORT).show();
                    Intent exitIntent = new Intent(Intent.ACTION_MAIN);
                    exitIntent.addCategory(Intent.CATEGORY_HOME);
                    exitIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(exitIntent);
                }
            });
        } else if (from.equals("AdapterToLoginFragment")) {
            requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    requireActivity().getSupportFragmentManager().popBackStack();
                }
            });
        }


    }

}