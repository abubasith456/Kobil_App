package com.example.kobilapp;

import android.util.Log;

import com.kobil.midapp.ast.api.AstUpdate;
import com.kobil.midapp.ast.api.enums.AstDeviceType;

public interface UpdateListener {

    public void astUpdate(AstUpdate astUpdate);

}
