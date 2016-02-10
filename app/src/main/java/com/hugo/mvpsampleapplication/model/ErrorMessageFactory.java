package com.hugo.mvpsampleapplication.model;

import android.util.Log;

/**
 * Created by hugo on 1/29/16.
 */
public class ErrorMessageFactory {

    private final static String GENERIC_ERROR = "Application error";

    public ErrorMessageFactory() {

    }

    public static String createErrorMessage(Exception exception) {
        String errorMessage = GENERIC_ERROR;
        Log.d("ErrorMessageFactory", "Error type: " + exception.getMessage());
        return errorMessage;
    }
}
