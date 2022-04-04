package com.example.kobilapp.model;

import android.util.Log;

import com.kobil.midapp.ast.api.enums.AstStatus;

public class StatusMessage {
    private static StatusMessage instance;
//    private String statusMessage;
    private AstStatus astStatus;

    public static StatusMessage getInstance() {
        if (instance == null) {
            instance = new StatusMessage();
        }
        return instance;
    }

    public AstStatus getAstStatus() {
        return astStatus;
    }

    public void setAstStatus(AstStatus astStatus) {
        this.astStatus = astStatus;
        Log.e("setAstStatus =>", String.valueOf(astStatus));

    }

    public String getStatusMessage() {
        Log.e("getStatusMessage =>", String.valueOf(astStatus));
        switch (astStatus) {
            case OK:
                return "ok";

            case FAILED:
                return "Failed!";

            case WRONG_CREDENTIALS:
                return "Invalid credential!.";

            case INVALID_PIN:
                return "Invalid pin";

            case LOCKED_USER:
                return "User locked";

            case PIN_BLOCKED:
                return "Your PIN is blocked.";

            case REGISTER_APP:
                return "Please register your app.";

            case LOGIN_REQUIRED:
                return "Login required";

            case ACCESS_DENIED:
                return "Access denied!.";

            case UPDATE_AVAILABLE:
                return "Update available!.";

            case UPDATE_NECESSARY:
                return "Update necessary!.";

            case NOT_REACHABLE:
                return "Server not reachable";

            case ACTIVATION_CODE_EXPIRED:
                return "Activation code expired";

            case APP_REGISTERED:
                return "App registered successfully";

            case INVALID_STATE:
                return "Invalid state!";

            case INVALID_KEY:
                return "Invalid key!";

            case USER_ID_ALREADY_EXISTS:
                return "User ID already exist";

            case ALREADY_CREATED:
                return "Already created!";

            case INVALID_PARAMETER:
                return "Invalid parameter!";

            default:
                return "";
        }

    }

}
