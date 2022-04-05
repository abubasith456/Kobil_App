package com.example.kobilapp.utils;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.example.kobilapp.SdkListener;
import com.kobil.midapp.ast.api.AstSdk;
import com.kobil.midapp.ast.sdk.AstSdkFactory;

import java.lang.reflect.Field;

public class AppLifecycle extends Application implements LifecycleObserver {

    private Context context;
    private final SdkListener listener = new SdkListener();
    private final AstSdk sdk;

    public AppLifecycle(Context context) {
        this.context = context;
        sdk = AstSdkFactory.getSdk(context, listener);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate() {
        super.onCreate();
        Log.e("App state on=> ", "onCreate");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        // App in foreground
        Log.e("App state on=> ", "onStart");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        sdk.resume();
//        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
        SharedPreference.getInstance().saveValue(context, "deviceName", Build.MODEL);
        SharedPreference.getInstance().saveValue(context, "deviceOSVersion", String.valueOf(Build.VERSION.SDK_INT));
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        SharedPreference.getInstance().saveValue(context, "deviceIPAddress", ip);
        Log.e("IP address", ip);
        Log.e("App state on=> ", "onResume");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        Log.e("App state on=> ", "onPause");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        // App in background
        sdk.suspend();
        Log.e("App state on=> ", "onStop");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        Log.e("App state on=> ", "onDestroy");
    }

}
