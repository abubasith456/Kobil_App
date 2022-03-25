package com.example.kobilapp.model;

import android.util.Log;

import com.kobil.midapp.ast.api.enums.AstStatus;

public class StatusMessage {
    private static StatusMessage instance;
    private String statusMessage;

    public static StatusMessage getInstance() {
        if (instance == null) {
            instance = new StatusMessage();
        }
        return instance;
    }

    public String getStatus() {
        return statusMessage;
    }

    public void setStatus(AstStatus astStatus) {
        switch (astStatus) {
            case OK:
                statusMessage = "ok";
                break;
            case FAILED:
                statusMessage = "Failed!";
                break;
            case WRONG_CREDENTIALS:
                statusMessage = "Invalid credential!.";
                break;
            case INVALID_PIN:
                statusMessage = "Invalid pin";
                break;
            case LOCKED_USER:
                statusMessage = "User locked";
                break;
            case PIN_BLOCKED:
                statusMessage = "Your PIN is blocked.";
                break;
            case REGISTER_APP:
                statusMessage = "Please register your app.";
                break;
            case LOGIN_REQUIRED:
                statusMessage = "Login required";
                break;
            case ACCESS_DENIED:
                statusMessage = "Access denied!.";
                break;
            case UPDATE_AVAILABLE:
                statusMessage = "Update available!.";
                break;
            case NOT_REACHABLE:
                statusMessage = "Server not reachable";
                break;
            case ACTIVATION_CODE_EXPIRED:
                statusMessage = "Activation code expired";
                break;
            case APP_REGISTERED:
                statusMessage = "App registered successfully";
                break;
            case INVALID_STATE:
                statusMessage = "Invalid state!";
                break;
            case INVALID_KEY:
                statusMessage = "Invalid key!";
                break;
            case USER_ID_ALREADY_EXISTS:
                statusMessage = "User ID already exist";
                break;
            case ALREADY_CREATED:
                statusMessage = "Already created!";
                break;
            case INVALID_PARAMETER:
                statusMessage = "Invalid parameter!";
                break;
            default:
                statusMessage = "";
        }
        Log.e("StatusMessageClass=>", statusMessage);
    }

}
