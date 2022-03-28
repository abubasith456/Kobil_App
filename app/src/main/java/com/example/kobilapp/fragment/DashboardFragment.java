package com.example.kobilapp.fragment;

import androidx.activity.OnBackPressedCallback;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.kobilapp.R;
import com.example.kobilapp.databinding.DashboardFragmentBinding;
import com.example.kobilapp.viewModel.DashboardViewModel;

public class DashboardFragment extends Fragment {

    private DashboardViewModel mViewModel;
    private DashboardFragmentBinding dashboardFragmentBinding;

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        dashboardFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.dashboard_fragment, container, false);
        return dashboardFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        // TODO: Use the ViewModel
//        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                Toast.makeText(getActivity(), "Back pressed", Toast.LENGTH_SHORT).show();
//                Intent exitIntent = new Intent(Intent.ACTION_MAIN);
//                exitIntent.addCategory(Intent.CATEGORY_HOME);
//                exitIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(exitIntent);
//            }
//        });
    }

}