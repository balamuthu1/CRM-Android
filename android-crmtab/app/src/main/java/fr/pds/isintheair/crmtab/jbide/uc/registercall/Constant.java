package fr.pds.isintheair.crmtab.jbide.uc.registercall;

/**
 * Create by jdatour
 * Modified by tlacouque on 06/03/2016
 * <p/>
 * Class used to store url with static constant
 */
public class Constant {

    private static Boolean popupdisplayed = false;

    // url for rest server
    //public static final String REST_URL = "http://192.168.20.3:8082";
    //url for mock client
    //public static final String BASE_URL = "http://192.168.20.3:8082/api/";

    //url for mock client
    public static final String REST_URL = "http://192.168.1.68:8080/server-rest/";

    public static String WEBSOCKET_CALL_ENDPOINT = "ws://192.168.20.3:8084/call";
    //public static String WEBSOCKET_CALL_ENDPOINT     = "ws://192.168.1.1:8084/call";
    public static String WEBSOCKET_CALENDAR_ENDPOINT = "ws://192.168.20.3:8084/calendar";
    //public static String WEBSOCKET_CALENDAR_ENDPOINT = "ws://192.168.1.1:8084/calendar";


    public static Boolean isPopUpDisplayed() {

        if (popupdisplayed == null) {
            popupdisplayed = false;
        }
        return popupdisplayed;

    }

    public static void setPopUpDisplayed(boolean state) {

        popupdisplayed = state;
    }

}


