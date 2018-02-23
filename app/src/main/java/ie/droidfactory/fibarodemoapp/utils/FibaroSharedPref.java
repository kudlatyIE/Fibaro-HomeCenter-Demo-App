package ie.droidfactory.fibarodemoapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * save userName & password basic authentication credentials
 * Created by kudlaty on 2018-02-19.
 */

public class FibaroSharedPref {

    private final static String TAG = FibaroSharedPref.class.getSimpleName();

    private static SharedPreferences settings;
    private static SharedPreferences.Editor editor;

    private final static String PREF="MY_SETTINGS";
    public static final String KEY_CREDENTIALS = "credentials";

    public static void setCredentials(Context context, String credentials){
        settings = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        editor = settings.edit();
        if(null==credentials) editor.clear();
        else editor.putString(KEY_CREDENTIALS, credentials);
        editor.apply();
    }

    public static String getCredentials(Context context){
        settings = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return settings.getString(KEY_CREDENTIALS, null);
    }


}
