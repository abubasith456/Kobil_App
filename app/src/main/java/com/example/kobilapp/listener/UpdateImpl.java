package com.example.kobilapp.listener;

import android.util.Log;

import com.kobil.midapp.ast.api.AstUpdate;

public class UpdateImpl implements Update {

    private GetUpdate getUpdate;

    public UpdateImpl(GetUpdate getUpdate) {
        this.getUpdate = getUpdate;
    }

    @Override
    public void get(AstUpdate astUpdate) {
        if (astUpdate != null) {
            getUpdate.set(astUpdate);
            Log.e("get", "Called");
        }
    }
}
