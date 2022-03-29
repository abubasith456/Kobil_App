package com.example.kobilapp.viewModel;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.example.kobilapp.MainActivity;
import com.example.kobilapp.R;
import com.example.kobilapp.SdkListener;
import com.example.kobilapp.databinding.SideMenuFragmentBinding;
import com.example.kobilapp.fragment.ActivationFragment;
import com.example.kobilapp.fragment.ChangePinFragment;
import com.example.kobilapp.fragment.InitFragment;
import com.example.kobilapp.fragment.LoginFragment;
import com.example.kobilapp.fragment.SideMenuFragment;
import com.example.kobilapp.utils.SharedPreference;
import com.kobil.midapp.ast.api.AstSdk;
import com.kobil.midapp.ast.sdk.AstSdkFactory;

public class SideMenuViewModel extends AndroidViewModel {
    private SideMenuFragment sideMenuFragment;
    private String from;
    public ObservableField<Boolean> changePinVisibility = new ObservableField<>();
    public ObservableField<Boolean> addUserVisibility = new ObservableField<>();
    public ObservableField<Boolean> deleteUserVisibility = new ObservableField<>();
    private ProgressDialog progressdialog;

    public SideMenuViewModel(@NonNull Application application) {
        super(application);
        deleteUserVisibility.set(false);
    }

    public void getFragment(SideMenuFragment sideMenuFragment) {
        this.sideMenuFragment = sideMenuFragment;
        from = SharedPreference.getInstance().getValue(sideMenuFragment.getContext(), "from");
        if (from.equals("LoginFragment")) {
            changePinVisibility.set(false);
            addUserVisibility.set(true);
            deleteUserVisibility.set(true);
        } else if (from.equals("ActivationFragment")) {
            changePinVisibility.set(false);
            addUserVisibility.set(false);
            deleteUserVisibility.set(false);
        } else if (from.equals("DashboardFragment")) {
            changePinVisibility.set(true);
            deleteUserVisibility.set(false);
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

    public void onDeleteUserClick(View view) {
        try {
            Log.e("Clicked", "onDeleteUserClick");
            String username = SharedPreference.getInstance().getValue(sideMenuFragment.getContext(), "userId");
            AlertDialog.Builder builder = new AlertDialog.Builder(sideMenuFragment.getContext());
            builder.setMessage("Are you sure do you want to delete " + username + " user?");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    showProcessBar("Deleting user....");
                    deleteUser(username);
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        progressdialog.dismiss();
                        sideMenuFragment.getActivity().getSupportFragmentManager().popBackStack();
                        Fragment fragment = new InitFragment();
                        FragmentTransaction transaction = sideMenuFragment.getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frameLayoutForSideMenu, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        SharedPreference.getInstance().saveValue(sideMenuFragment.getContext(), "from", "DashboardFragment");
                    }, 3000);
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

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

    private void deleteUser(String user_id) {
        try {
            SdkListener listener = new SdkListener();
            AstSdk sdk = AstSdkFactory.getSdk(sideMenuFragment.getContext(), listener);
            sdk.doDeactivate(user_id);
            Log.e("deleteUser:", "User deleted " + user_id);
        } catch (Exception e) {
            Log.e("Error:", e.getMessage());
        }
    }

    private void showProcessBar(String message) {
        progressdialog = new ProgressDialog((sideMenuFragment.getContext()));
        progressdialog.setMessage(message);
        progressdialog.setCancelable(false);
        progressdialog.show();
    }

}