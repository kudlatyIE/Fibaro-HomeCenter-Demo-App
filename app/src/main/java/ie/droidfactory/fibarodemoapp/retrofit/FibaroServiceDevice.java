package ie.droidfactory.fibarodemoapp.retrofit;

import java.util.ArrayList;

import ie.droidfactory.fibarodemoapp.model.Device;
import retrofit.http.GET;
import rx.Observable;

import static ie.droidfactory.fibarodemoapp.retrofit.FibaroService.SERVICE_DEVICES;

/**
 * Created by kudlaty on 2018-02-18.
 */

public interface FibaroServiceDevice {


    @GET(SERVICE_DEVICES)
    Observable<ArrayList<Device>> getDevices();
}
