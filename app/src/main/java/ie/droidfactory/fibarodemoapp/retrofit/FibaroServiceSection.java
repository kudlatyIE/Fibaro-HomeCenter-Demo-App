package ie.droidfactory.fibarodemoapp.retrofit;

import java.util.ArrayList;

import ie.droidfactory.fibarodemoapp.model.Section;
import retrofit.http.GET;
import rx.Observable;

import static ie.droidfactory.fibarodemoapp.retrofit.FibaroService.SERVICE_SECTIONS;

/**
 * Created by kudlaty on 2018-02-18.
 */

public interface FibaroServiceSection {

    @GET(SERVICE_SECTIONS)
    Observable<ArrayList<Section>> getSection();
}