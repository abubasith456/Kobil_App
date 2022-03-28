package com.example.kobilapp.viewModel;

import android.app.Application;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.example.kobilapp.R;
import com.example.kobilapp.databinding.SideMenuFragmentBinding;
import com.example.kobilapp.fragment.ActivationFragment;
import com.example.kobilapp.fragment.ChangePinFragment;
import com.example.kobilapp.fragment.LoginFragment;
import com.example.kobilapp.fragment.SideMenuFragment;
import com.example.kobilapp.utils.SharedPreference;

public class SideMenuViewModel extends AndroidViewModel {
    private SideMenuFragment sideMenuFragment;
    private String from;
    public ObservableField<Boolean> changePinVisibility = new ObservableField<>();
    public ObservableField<Boolean> addUserVisibility = new ObservableField<>();

    public SideMenuViewModel(@NonNull Application application) {
        super(application);

    }

    public void getFragment(SideMenuFragment sideMenuFragment) {
        this.sideMenuFragment = sideMenuFragment;
        from = SharedPreference.getInstance().getValue(sideMenuFragment.getContext(), "from");
        if (from.equals("LoginFragment")) {
            changePinVisibility.set(false);
            addUserVisibility.set(true);
        } else if (from.equals("DashboardFragment")) {
            changePinVisibility.set(true);
        } else if (from.equals("UsersFragment")) {
            changePinVisibility.set(false);
            addUserVisibility.set(true);
        }
    }

    public void closeMenu(View view) {
        try {
            sideMenuFragment.getActivity().getSupportFragmentManager().popBackStackImmediate();
        } catch (Exception e) {
            Log.e("Error=>", e.getMessage());
        }
    }

    public void onAddUserClick(View view) {
        try {
            Log.e("Clicked", "onClicked");
            sideMenuFragment.getActivity().getSupportFragmentManager().popBackStack();
            Fragment fragment = new ActivationFragment();
            FragmentTransaction transaction = sideMenuFragment.getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayoutLoginFragmentContainer, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } catch (Exception e) {
            Log.e("Error=>", e.getMessage());
        }
    }

    public void onChangePinClick(View view) {
        try {
            Fragment fragment = new ChangePinFragment();
            FragmentTransaction transaction = sideMenuFragment.getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayoutForSideMenu, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } catch (Exception e) {
            Log.e("Error=>", e.getMessage());
        }
    }


}