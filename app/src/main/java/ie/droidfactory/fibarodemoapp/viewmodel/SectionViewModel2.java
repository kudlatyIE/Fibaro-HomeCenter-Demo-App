package ie.droidfactory.fibarodemoapp.viewmodel;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Base64;
import android.util.Log;


import java.io.IOException;
import java.util.ArrayList;

import ie.droidfactory.fibarodemoapp.model.Section;
import ie.droidfactory.fibarodemoapp.retrofit.FibaroService;
import ie.droidfactory.fibarodemoapp.retrofit.FibaroServiceSection2;
import ie.droidfactory.fibarodemoapp.retrofit.RetrofiteServiceFactory2;
import retrofit.converter.GsonConverter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kudlaty on 2018-02-24.
 */

public class SectionViewModel2 extends ViewModel{

    private static final String TAG = SectionViewModel2.class.getSimpleName();

    private MutableLiveData<ArrayList<Section>> mutableLiveData;

    public LiveData<ArrayList<Section>> getSections(Activity activity, String credentials){
        if(mutableLiveData==null){
            mutableLiveData = new MutableLiveData<>();
            loadSections(activity, credentials);
        }
        return mutableLiveData;
    }

    private void loadSections(Activity activity, String credentials) {


        FibaroServiceSection2  service = RetrofiteServiceFactory2.createService(FibaroServiceSection2.class);
        Call<ArrayList<Section>> call = service.getSection(FibaroService.getCredentials());

        try {

            call.enqueue(new Callback<ArrayList<Section>>() {
                @Override
                public void onResponse(Call<ArrayList<Section>> call, Response<ArrayList<Section>> response) {
                    ArrayList<Section>  list = response.body();
                    Log.d(TAG, "call url: "+call.request().toString());

                    if(list!=null){
                        Log.d(TAG, "response list size: "+list.size());
                        Section.setSectionsList(list);
                        mutableLiveData.setValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Section>> call, Throwable t) {
                    t.printStackTrace();
                    Log.d(TAG, "response fail: "+t.getMessage());
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }


    }


}

