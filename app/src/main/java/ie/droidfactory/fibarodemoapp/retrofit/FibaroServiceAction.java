package ie.droidfactory.fibarodemoapp.retrofit;

import ie.droidfactory.fibarodemoapp.model.Actions;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

import static ie.droidfactory.fibarodemoapp.retrofit.FibaroService.SERVICE_CALL_ACTION;

/**
 * Created by kudlaty on 2018-02-18.
 */

public interface FibaroServiceAction {

    String DEVICE_ID="deviceID";
    String NAME="name";
    String VALUE_DIMMABLE="arg1";
    String VALUE_TURN_ON = "turnOn";
    String VALUE_TURN_OFF = "turnOff";

    @GET(SERVICE_CALL_ACTION)
    Observable<Actions> setActionBinary(@Query(DEVICE_ID)String deviceId, @Query(NAME)String valueName);

    @GET(SERVICE_CALL_ACTION)
    Observable<Actions> setActionDimmable(@Query(DEVICE_ID)String deviceId, @Query(NAME)String valueName, @Query(VALUE_DIMMABLE) String value);
}
