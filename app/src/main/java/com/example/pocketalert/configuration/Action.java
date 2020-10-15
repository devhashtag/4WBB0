package com.example.pocketalert.configuration;

public class Action {
    public enum Request {
        REQUEST_ID,
        ID,
        GET_DEVICES_INFORMATION,
        GET_DEVICE_INFORMATION,
        GET_DEVICE_HISTORY,
        GET_DEVICE_LOCATION,
        START_LIVE_LOCATION,
        STOP_LIVE_LOCATION,
        ADD_DEVICE,
        UPDATE_DEVICE_INFORMATION
    }

    public enum Response {
        ID,
        DEVICES_INFORMATION,
        DEVICE_HISTORY,
        DEVICE_LOCATION,
        LIVE_LOCATION,
        ALERT,
        CANCEL_ALERT,
        BAD_REQUEST,
        OK
    }
    
    public enum Control {
       START_SERVICE,
       STOP_SERVICE,
       CONNECTION_ERROR
    }

    private static boolean inEnum(Class c, String value) {
        try {
            Enum.valueOf(c, value);
            return true;
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }

    public static boolean contains(String value) {
        return  inEnum(Request.class, value) ||
                inEnum(Response.class, value) ||
                inEnum(Control.class, value);
    }

    public static boolean isRequest(String value) {
        return inEnum(Request.class, value);
    }

    public static boolean isResponse(String value) {
        return inEnum(Response.class, value);
    }

    public static boolean isControl(String value) {
        return inEnum(Control.class, value);
    }
}
