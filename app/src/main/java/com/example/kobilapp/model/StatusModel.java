package com.example.kobilapp.model;

import com.kobil.midapp.ast.api.enums.AstStatus;

public class StatusModel {
    private static StatusModel instance;
    private String statusMessage;
    private String statusCode;

//    private StatusModel() {
//    }

//    public static StatusModel getInstance() {
//        //instantiate a new CustomerLab if we didn't instantiate one yet
//        if (instance == null) {
//            instance = new StatusModel();
//        }
//        return instance;
//    }

    public String getStatus() {
        return statusMessage;
    }


    public StatusModel(AstStatus astStatus) {
        switch (astStatus) {
            case OK:
                statusMessage = "Successfully Activated";
                break;
            case FAILED:
                statusMessage = "Activation failed";
                break;
            case INVALID_PIN:
                statusMessage = "Invalid pin";
                break;
            case LOCKED_USER:
                statusMessage = "User locked";
                break;
            case PIN_BLOCKED:
                statusMessage = "Pin blocked";
                break;
            case REGISTER_APP:
                statusMessage = "Register app";
                break;
            case LOGIN_REQUIRED:
                statusMessage = "Login required";
                break;
            case ACCESS_DENIED:
                statusMessage = "Access denied";
                break;
            case UPDATE_AVAILABLE:
                statusMessage = "Update available";
                break;
            case NOT_REACHABLE:
                statusMessage = "Not reachable";
                break;
            case ACTIVATION_CODE_EXPIRED:
                statusMessage = "Activation code expired";
                break;
            case APP_REGISTERED:
                statusMessage = "App registered";
                break;
        }
    }

}
