package ie.droidfactory.fibarodemoapp.retrofit;

import android.accounts.AuthenticatorException;

import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.Proxy;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by kudlaty on 2018-02-18.
 */

public class RetrofitServiceFactory {

    public static <T> T createRetrofitService(final Class<T> interfaceClass, final String endPoint, final String credentials) {

        Authenticator authenticator = new Authenticator() {
            int mCounter = 0;
            @Override
            public Request authenticate(Proxy proxy, Response response) throws IOException {
                if(mCounter++>0){
//                    throw new FibaroAuthenticationException(FibaroException.INVALID_LOGIN);
                    return null;
                }
                return response.request().newBuilder().header("Authorization", credentials ).build();
            }

            @Override
            public Request authenticateProxy(Proxy proxy, Response response) throws IOException {
                return null;
            }

        };

        final RestAdapter restAdapter = new RestAdapter.Builder()
                .setClient(new OkClient(new OkHttpClient().setAuthenticator(authenticator)))
                .setEndpoint(endPoint)
                .build();

        T service = restAdapter.create(interfaceClass);

        return service;
    }
}
