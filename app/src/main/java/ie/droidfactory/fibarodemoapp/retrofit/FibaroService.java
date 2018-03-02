package ie.droidfactory.fibarodemoapp.retrofit;

/**
 * Created by kudlaty on 2018-02-18.
 */

public class FibaroService {

    public final static String SERVICE_ENDPOINT = "http://styx.fibaro.com:9999/api";
    public final static String SERVICE_CALL_ACTION = "/callAction";
    public final static String SERVICE_DEVICES = "/devices";
    public final static String SERVICE_INFO = "/settings/info";
    public final static String SERVICE_ROOMS = "/rooms";
    public final static String SERVICE_SECTIONS = "/sections";

    private static String userName, pass, credentials;

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        FibaroService.userName = userName;
    }

    public static String getPass() {
        return pass;
    }

    public static void setPass(String pass) {
        FibaroService.pass = pass;
    }

    public static String getCredentials() {
        return credentials;
    }

    public static void setCredentials(String credentials) {
        FibaroService.credentials = credentials;
    }
}
