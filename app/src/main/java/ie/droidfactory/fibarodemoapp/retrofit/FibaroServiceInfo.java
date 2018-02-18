package ie.droidfactory.fibarodemoapp.retrofit;

import ie.droidfactory.fibarodemoapp.model.Info;
import retrofit.http.GET;
import rx.Observable;

import static ie.droidfactory.fibarodemoapp.retrofit.FibaroService.SERVICE_INFO;

/**
 * Created by kudlaty on 2018-02-18.
 */

public interface FibaroServiceInfo {

    @GET(SERVICE_INFO)
    Observable<Info> getInfo();
}