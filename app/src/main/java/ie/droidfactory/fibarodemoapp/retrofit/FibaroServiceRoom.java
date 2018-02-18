package ie.droidfactory.fibarodemoapp.retrofit;

import java.util.ArrayList;

import ie.droidfactory.fibarodemoapp.model.Room;
import retrofit.http.GET;
import rx.Observable;

import static ie.droidfactory.fibarodemoapp.retrofit.FibaroService.SERVICE_ROOMS;

/**
 * Created by kudlaty on 2018-02-18.
 */

public interface FibaroServiceRoom {

    @GET(SERVICE_ROOMS)
    Observable<ArrayList<Room>> getRooms();
}
