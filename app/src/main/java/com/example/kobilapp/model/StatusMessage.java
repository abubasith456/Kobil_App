package com.example.kobilapp.model;

import android.util.Log;

import com.kobil.midapp.ast.api.enums.AstStatus;

public class StatusMessage {
    private static StatusMessage instance;
    private int statusMessage;

    public static StatusMessage getInstance() {
        if (instance == null) {
            instance = new StatusMessage();
        }
        return instance;
    }

    public int getStatus() {
        return statusMessage;
    }

    public String getMessage() {
        String message = "";
        if (statusMessage == 0) {
            return message = "Activation and pin code successfully initiated.";
        } else if (statusMessage == 12) {
            return message = "Invalid credential please check your activation code.";
        } else if (statusMessage == 50) {
            return message = "Failed";
        } else if (statusMessage == 6) {
            return message = "Invalid pin";
        } else if (statusMessage == 17) {
            return message = "User locked";
        } else if (statusMessage == 40) {
            return message = "Your PIN is blocked.";
        } else if (statusMessage == 29) {
            return message = "Please register your app.";
        } else if (statusMessage == 1) {
            return message = "Update available!.";
        } else if (statusMessage == 51) {
            return message = "Login required";
        } else if (statusMessage == 3) {
            return message = "App registered successfully.";
        } else if (statusMessage == 15) {
            return message = "Activation code expired.";
        } else if (statusMessage == 39) {
            return message = "Not reachable";
        }
        return message;
    }


    public void setStatus(AstStatus astStatus) {
        switch (astStatus) {
            case OK:
                statusMessage = 0;
                break;
            case FAILED:
                statusMessage = 50;
                break;
            case WRONG_CREDENTIALS:
                statusMessage = 12;
                break;
            case INVALID_PIN:
                statusMessage = 6;
                break;
            case LOCKED_USER:
                statusMessage = 17;
                break;
            case PIN_BLOCKED:
                statusMessage = 40;
                break;
            case REGISTER_APP:
                statusMessage = 29;
                break;
            case LOGIN_REQUIRED:
                statusMessage = 51;
                break;
            case ACCESS_DENIED:
                statusMessage = 41;
                break;
            case UPDATE_AVAILABLE:
                statusMessage = 1;
                break;
            case NOT_REACHABLE:
                statusMessage = 39;
                break;
            case ACTIVATION_CODE_EXPIRED:
                statusMessage = 15;
                break;
            case APP_REGISTERED:
                statusMessage = 3;
                break;
            default:
                statusMessage = Integer.parseInt(null);
        }
        Log.e("StatusMessageClass=>", String.valueOf(statusMessage));
    }

}
