package ie.droidfactory.fibarodemoapp.retrofit;

/**
 * not used now...
 * Created by kudlaty on 2018-02-19.
 */

public class FibaroException extends Exception{
    FibaroException(String message){
        super(message);
    }
    static String INVALID_LOGIN = "invalid user name or password";
    static String EXTERNAL_SERVER_ERROR = "external server error";
}
class FibaroAuthenticationException extends FibaroException{
    FibaroAuthenticationException(String message){
        super(message);

    }
}
