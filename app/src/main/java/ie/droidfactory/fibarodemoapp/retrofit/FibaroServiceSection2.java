package ie.droidfactory.fibarodemoapp.retrofit;

import java.util.ArrayList;

import ie.droidfactory.fibarodemoapp.model.Section;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

import static ie.droidfactory.fibarodemoapp.retrofit.FibaroService.SERVICE_SECTIONS;

/**
 * Created by kudlaty on 2018-02-25.
 */

public interface FibaroServiceSection2 {

    @GET("/api/sections")
    Call<ArrayList<Section>> getSection(@Header("Authorization") String authHeader);
}
