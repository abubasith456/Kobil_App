package com.example.kobilapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.kobilapp.databinding.ActivityMainBinding;
import com.example.kobilapp.fragment.ActivationFragment;
import com.example.kobilapp.utils.AppLifecycle;
import com.example.kobilapp.viewModel.MainActivityViewModel;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding activityMainBinding;
    MainActivityViewModel mainActivityViewModel;
    private AppLifecycle appLifecycle;
    private ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_init);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        activityMainBinding.setInitViewModel(mainActivityViewModel);
        mainActivityViewModel.getActivity(this);
        mainActivityViewModel.showInitFragment();
        getLifecycle().addObserver(new AppLifecycle(getApplicationContext()));
    }

    @Override
    public void onConfigurationChanged(@NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
/*        Log.e("App state", "onPause");
        Handler handler = new Handler();
        handler.postDelayed(() -> {

        }, 1000);*/
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}