package com.example.kobilapp.viewModel;

import android.app.Application;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.example.kobilapp.fragment.SideMenuFragment;
import com.example.kobilapp.utils.SharedPreference;

public class SideMenuViewModel extends AndroidViewModel {
    private SideMenuFragment sideMenuFragment;
    private String from;
    public ObservableField<Boolean> changePinVisibility = new ObservableField<>();


    public SideMenuViewModel(@NonNull Application application) {
        super(application);

    }

    public void getFragment(SideMenuFragment sideMenuFragment) {
        this.sideMenuFragment = sideMenuFragment;
        from = SharedPreference.getInstance().getValue(sideMenuFragment.getContext(), "from");
        if (from.equals("LoginFragment")) {
            changePinVisibility.set(false);
        }else if (from.equals("DashboardFragment")){
            changePinVisibility.set(true);
        }
    }

    public void closeMenu(View view) {
        try {
            Log.e("Pressed:", "Close button");
            sideMenuFragment.getActivity().getSupportFragmentManager().popBackStackImmediate();
        } catch (Exception e) {
            Log.e("Error=>", e.getMessage());
        }
    }

}