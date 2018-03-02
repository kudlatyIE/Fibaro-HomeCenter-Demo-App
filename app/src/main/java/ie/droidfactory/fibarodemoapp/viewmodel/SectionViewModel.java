package ie.droidfactory.fibarodemoapp.viewmodel;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

import ie.droidfactory.fibarodemoapp.FibaroFragmentInterfaces;
import ie.droidfactory.fibarodemoapp.model.Section;
import ie.droidfactory.fibarodemoapp.retrofit.FibaroService;
import ie.droidfactory.fibarodemoapp.retrofit.FibaroServiceSection;
import ie.droidfactory.fibarodemoapp.retrofit.RetrofitServiceFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

/**
 * Created by kudlaty on 2018-02-22.
 */

public class SectionViewModel extends ViewModel {

    private static final String TAG = SectionViewModel.class.getSimpleName();

    private MutableLiveData<ArrayList<Section>> mutableLiveData;

    public LiveData<ArrayList<Section>> getSections(Activity activity, String credentials, FibaroFragmentInterfaces callback){
        if(mutableLiveData==null){
            mutableLiveData = new MutableLiveData<>();
            loadSections(activity, credentials, callback);
        }
        return mutableLiveData;
    }

    private void loadSections(final Activity activity, String credentials, final FibaroFragmentInterfaces callback){

        FibaroServiceSection service = RetrofitServiceFactory.createRetrofitService(FibaroServiceSection.class, FibaroService.SERVICE_ENDPOINT, credentials);

        service.getSection()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<Section>>(){

                    @Override
                    public void onCompleted() {
                        mutableLiveData.setValue(Section.getSectionsList());

                    }
                    //TODO: add observer for authorisation fail - in future...
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d(TAG, "ERROR: "+e.getMessage());
//                        Intent intent = new Intent();
//                        intent.putExtra("error", e.getMessage());
//                        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
//                        activity.setResult(Activity.RESULT_OK, intent);
//                        activity.finish();
                        //TODO: check if need to finisg Section Activity....?
                        callback.loginResponse(false, e.getMessage());

                    }

                    @Override
                    public void onNext(ArrayList<Section> sections) {
                        Section.setSectionsList(sections);
                    }
                });
    }


}
