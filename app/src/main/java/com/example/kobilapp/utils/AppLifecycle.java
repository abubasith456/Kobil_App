package com.example.kobilapp.utils;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

public class AppLifecycle extends Application implements LifecycleObserver {

    @Override
    public void onCreate() {
        super.onCreate();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        Log.e("App on=> ", "Background");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        // App in foreground
        Log.e("App on=> ", "Foreground");
    }

}
