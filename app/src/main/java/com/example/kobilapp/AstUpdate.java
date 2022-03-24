package com.example.kobilapp;

import com.kobil.midapp.ast.api.AstUpdateEventListener;
import com.kobil.midapp.ast.api.enums.AstDeviceType;
import com.kobil.midapp.ast.api.enums.AstStatus;
import com.kobil.midapp.ast.api.enums.AstUpdateStatus;
import com.kobil.midapp.ast.api.enums.AstUpdateType;

public class AstUpdate implements AstUpdateEventListener {
    @Override
    public boolean onUpdateBegin(AstDeviceType astDeviceType, AstUpdateStatus astUpdateStatus) {
        return false;
    }

    @Override
    public boolean onOpenInfoUrlEnd(AstDeviceType astDeviceType, AstUpdateStatus astUpdateStatus) {
        return false;
    }

    @Override
    public boolean onUpdateInformationAvailable(AstDeviceType astDeviceType, AstStatus astStatus, String s, String s1, AstUpdateType astUpdateType, int i) {
        return false;
    }
}
