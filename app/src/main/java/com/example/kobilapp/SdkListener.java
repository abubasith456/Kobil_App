package com.example.kobilapp;

import android.util.Log;

import com.example.kobilapp.model.Status;
import com.example.kobilapp.model.StatusMessage;
import com.kobil.midapp.ast.api.AstSdkListener;
import com.kobil.midapp.ast.api.AstUpdate;
import com.kobil.midapp.ast.api.enums.AstCheckPinReason;
import com.kobil.midapp.ast.api.enums.AstConfigParameter;
import com.kobil.midapp.ast.api.enums.AstConfirmationType;
import com.kobil.midapp.ast.api.enums.AstConnectionState;
import com.kobil.midapp.ast.api.enums.AstContextType;
import com.kobil.midapp.ast.api.enums.AstDeviceType;
import com.kobil.midapp.ast.api.enums.AstInformationKey;
import com.kobil.midapp.ast.api.enums.AstMessageType;
import com.kobil.midapp.ast.api.enums.AstPinReason;
import com.kobil.midapp.ast.api.enums.AstPropertyOwner;
import com.kobil.midapp.ast.api.enums.AstPropertySynchronizationDirection;
import com.kobil.midapp.ast.api.enums.AstPropertyType;
import com.kobil.midapp.ast.api.enums.AstStatus;
import com.kobil.midapp.ast.api.enums.AstUrlBlockedReason;

import java.util.ArrayList;
import java.util.List;

public class SdkListener implements AstSdkListener {

    AstUpdate astUpdate;
//
////    public AstUpdate getAstUpdate() {
////        return astUpdate;
////    }

    public void setAstUpdate(AstUpdate astUpdate) {
        this.astUpdate = astUpdate;
//        astUpdate.doStartUpdate(AstDeviceType.VIRTUALDEVICE);
        astUpdate.doOpenInfoUrl(AstDeviceType.VIRTUALDEVICE);
//        Log.e("setAstUpdate", "Called");

    }

//    public void doUpdate(){
//        astUpdate.doStartUpdate(AstDeviceType.VIRTUALDEVICE);
//    }
//
//    public void doOpenInfoUrl(){
//        astUpdate.doOpenInfoUrl(AstDeviceType.VIRTUALDEVICE);
//    }

    @Override
    public void onActivationBegin(AstDeviceType astDeviceType) {
        Log.e("AstSDKCallback", "onActivationBegin() called.");
    }

    @Override
    public void onActivationEnd(AstDeviceType astDeviceType, AstStatus astStatus) {
        Log.e("AstSDKCallback", "onActivationEnd() called ==> " + astStatus);
        StatusMessage.getInstance().setStatus(astStatus);
    }

    @Override
    public void onLoginBegin(AstDeviceType astDeviceType, List<String> list) {
        Log.e("AstSDKCallback", "onLoginBegin() called ==> " + list);
        Status.getInstance().setList(list);
    }

    @Override
    public void onLoginEnd(AstDeviceType astDeviceType, AstStatus astStatus, String s, String s1, int i, int i1) {
        StatusMessage.getInstance().setStatus(astStatus);
        Log.e("AstSDKCallback", "onLoginEnd() called ==> " + astStatus);
        Log.e("AstSDKCallback", "onLoginEnd() called Login otp ==> " + s);
        Log.e("AstSDKCallback", "onLoginEnd() called userId ==> " + s1);
        Log.e("AstSDKCallback", "onLoginEnd() called Retry counter ==> " + i);
        if(astStatus.equals(AstStatus.UPDATE_AVAILABLE)){
            astUpdate.doOpenInfoUrl(AstDeviceType.VIRTUALDEVICE);
        }

    }

    @Override
    public void onReActivationEnd(AstStatus astStatus) {
        Log.e("AstSDKCallback", "onReActivationEnd() called ==> " + astStatus);
    }

    @Override
    public void onPinChangeBegin(AstDeviceType astDeviceType, AstStatus astStatus) {
        Log.e("AstSDKCallback", "onPinChangeBegin() called ==> " + astStatus);
        StatusMessage.getInstance().setStatus(astStatus);
    }

    @Override
    public void onPinChangeEnd(AstDeviceType astDeviceType, AstStatus astStatus, int i) {
        Log.e("AstSDKCallback", "onPinChangeEnd() called ==> " + astStatus);
        StatusMessage.getInstance().setStatus(astStatus);
    }

    @Override
    public void onServerConnection(AstDeviceType astDeviceType, AstConnectionState astConnectionState) {
        Log.e("AstSDKCallback", "onServerConnection() called ==> " + astConnectionState);

    }

    @Override
    public void onDeviceConnection(AstDeviceType astDeviceType, AstConnectionState astConnectionState) {
        Log.e("AstSDKCallback", "onDeviceConnection() called ==> " + astConnectionState);
    }

    @Override
    public void onTransactionBlockBegin(AstDeviceType astDeviceType, int i) {

    }

    @Override
    public void onTransactionBlockEnd(AstDeviceType astDeviceType) {

    }

    @Override
    public void onTransactionBegin(AstDeviceType astDeviceType, String s, AstConfirmationType astConfirmationType) {

    }

    @Override
    public void onTransactionEnd(AstDeviceType astDeviceType, AstStatus astStatus) {

    }

    @Override
    public void onDisplayMessage(AstDeviceType astDeviceType, String s, AstMessageType astMessageType) {

    }

    @Override
    public void onInfoHardwareDisplayMessageBegin() {

    }

    @Override
    public void onInfoHardwareDisplayMessageEnd() {

    }

    @Override
    public void onInfoHardwareTransactionBegin() {

    }

    @Override
    public void onInfoHardwareTransactionEnd() {

    }

    @Override
    public void onPinRequiredBegin(AstDeviceType astDeviceType, AstPinReason astPinReason) {

    }

    @Override
    public void onPinRequiredEnd(AstDeviceType astDeviceType, AstStatus astStatus, int i) {

    }

    @Override
    public void onAlert(AstDeviceType astDeviceType, int i, int i1) {
        Log.e("AstSDKCallback", "onAlert(Subsystem " + i + ", ErrorCode " + i1 + ")called.");
        Status.getInstance().setErrorCode(i1);
    }

    @Override
    public Object getAppConfigParameter(AstConfigParameter astConfigParameter) {
        try {
            switch (astConfigParameter) {
                case USE_DEVICE_NAME_SOFTWARE:
                    return false;
                case USE_DEVICE_NAME_HARDWARE:
                    return false;
                case WHITELIST:
                    List<String> whiteList = new ArrayList<String>();
                    whiteList.add("http://astqamaintenance.kobil.com:10005/.*");
                    return whiteList;
                case CONNECTION_RETRY_COUNTER:
                    return 5;
                case CONNECTION_RETRY_INTERVAL:
                    return 5;
                case SERVER_BUSY_TIMEOUT:
                    return 30;
                case CONFIG_BUNDLE:
                    return "";
                case CERTIFICATE_POLICY:
                    return "software";
                case WEB_VIEW_ERROR_PAGE:
                    return "http://www.google.de";
                case ALLOWED_HOST_DEVICES:
                    return ".*";
                case ALLOW_OFFLINE_PIN_VERIFICATION:
                    return false;
                case BLUETOOTH_DISABLE_TIMEOUT:
                    return 30;
                default:
                    break;
            }
        } catch (Exception e) {
            Log.i("AstAppConfig", "getAppConfigParameter: " + e.getMessage());
        }
        return "";
    }

    @Override
    public void onCertificateDataAvailable(AstDeviceType astDeviceType, byte[] bytes) {
//        Log.e("onCertificateDataAvailable", "Called: " + Arrays.toString(bytes));
    }

    @Override
    public void onUrlBlocked(String s, AstUrlBlockedReason astUrlBlockedReason) {

    }

    @Override
    public void onTransportPinBegin(AstCheckPinReason astCheckPinReason) {

    }

    @Override
    public void onTransportPinEnd(AstStatus astStatus) {

    }

    @Override
    public void onPinUnblockBegin() {

    }

    @Override
    public void onPinUnblockEnd(AstStatus astStatus, int i) {

    }

    @Override
    public void onSetPropertyBegin(AstDeviceType astDeviceType, AstStatus astStatus) {

    }

    @Override
    public void onSetPropertyEnd(AstDeviceType astDeviceType, AstStatus astStatus, int i, int i1) {

    }

    @Override
    public void onGetPropertyBegin(AstDeviceType astDeviceType, AstStatus astStatus) {

    }

    @Override
    public void onGetPropertyEnd(AstDeviceType astDeviceType, AstStatus astStatus,
                                 byte[] bytes, AstPropertyType astPropertyType, int i, int i1) {

    }

    @Override
    public void onInformationAvailable(AstContextType astContextType, AstInformationKey
            astInformationKey, Object o) {

    }

    @Override
    public void onReport(AstDeviceType astDeviceType, int i) {
        Log.i("AstSDKCallback", "onReport(ErrorCode" + i + ") called.");
    }

    @Override
    public void onDeactivateEnd(AstStatus astStatus, List<String> list) {
        Log.e("AstSDKCallback", "onDeactivateEnd " + list.toString());
        Status.getInstance().setList(list);

    }

    @Override
    public void onDetectHwDevicesEnd(AstStatus astStatus, List<String> list) {

    }

    @Override
    public void onConnectHwDeviceEnd(AstStatus astStatus) {

    }

    @Override
    public void onDisconnectHwDeviceEnd(AstStatus astStatus) {

    }

    @Override
    public void appExit(int i) {

    }

    @Override
    public void onRegisterOfflineFunctionsEnd(AstStatus astStatus) {

    }

    @Override
    public void onPropertySynchronization(String s, AstPropertyOwner
            astPropertyOwner, AstPropertySynchronizationDirection
                                                  astPropertySynchronizationDirection, AstStatus astStatus) {

    }

    @Override
    public void onBusyBegin() {

    }

    @Override
    public void onBusyEnd() {

    }

    @Override
    public void onSetUserIdEnd(AstStatus astStatus) {

    }

    @Override
    public void onRegisterMessagingEnd(AstStatus astStatus) {

    }
}
