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
import com.example.kobilapp.databinding.SideMenuFragmentBinding;
import com.example.kobilapp.viewModel.SideMenuViewModel;

public class SideMenuFragment extends Fragment {

    private SideMenuViewModel mViewModel;
    private SideMenuFragmentBinding sideMenuFragmentBinding;
    private String from;

    public SideMenuFragment(String from) {
        this.from = from;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        sideMenuFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.side_menu_fragment, container, false);
        return sideMenuFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SideMenuViewModel.class);
        sideMenuFragmentBinding.setSideMenuViewModel(mViewModel);
        mViewModel.getFragment(this);
    }

}