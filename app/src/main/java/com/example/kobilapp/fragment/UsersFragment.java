package com.example.kobilapp.fragment;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kobilapp.R;
import com.example.kobilapp.adapter.UsersAdapter;
import com.example.kobilapp.databinding.UsersFragmentBinding;
import com.example.kobilapp.model.Users;
import com.example.kobilapp.viewModel.UsersViewModel;

import java.util.List;

public class UsersFragment extends Fragment {

    private UsersViewModel mViewModel;
    private UsersFragmentBinding usersFragmentBinding;
    private UsersAdapter usersAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        usersFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.users_fragment, container, false);
        return usersFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(UsersViewModel.class);
        usersAdapter=new UsersAdapter();
        usersAdapter.getFragment(this);
        mViewModel.getUsersList().observe(getViewLifecycleOwner(), new Observer<List<Users>>() {
            @Override
            public void onChanged(List<Users> users) {
                usersAdapter.getUsers(users);
                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
                usersFragmentBinding.recyclerViewUsers.setLayoutManager(linearLayoutManager);
                usersFragmentBinding.recyclerViewUsers.setHasFixedSize(true);
                usersFragmentBinding.recyclerViewUsers.setAdapter(usersAdapter);
            }
        });
    }

}