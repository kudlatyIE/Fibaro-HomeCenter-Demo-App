package ie.droidfactory.fibarodemoapp.retrofit;

import java.util.ArrayList;

import ie.droidfactory.fibarodemoapp.model.Section;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import rx.Observable;

import static ie.droidfactory.fibarodemoapp.retrofit.FibaroService.SERVICE_SECTIONS;

/**
 * Created by kudlaty on 2018-02-18.
 */

public interface FibaroServiceSection {


    @retrofit.http.GET(SERVICE_SECTIONS)
    Observable<ArrayList<Section>> getSection();
//    @GET(SERVICE_SECTIONS)
//    Call<ArrayList<Section>> getSection(@Header("Authorization") String authHeader);
}