package ie.droidfactory.fibarodemoapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import ie.droidfactory.fibarodemoapp.model.Info;
import ie.droidfactory.fibarodemoapp.retrofit.FibaroService;
import ie.droidfactory.fibarodemoapp.retrofit.FibaroServiceInfo;
import ie.droidfactory.fibarodemoapp.retrofit.RetrofitServiceFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kudlaty on 2018-02-23.
 */

public class InfoViewModel extends ViewModel {

    private final static String TAG = InfoViewModel.class.getSimpleName();

    private MutableLiveData<Info> mutableLiveData;

    public LiveData<Info> getInfo(){
        if(mutableLiveData==null){
            mutableLiveData = new MutableLiveData<>();
            loadInfo();
        }
        return mutableLiveData;
    }

    private void loadInfo(){
        FibaroServiceInfo service = RetrofitServiceFactory.createRetrofitService(FibaroServiceInfo.class, FibaroService.SERVICE_ENDPOINT, null);
        service.getInfo()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Info>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted...");
                        mutableLiveData.setValue(Info.getInfo());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d(TAG, e.getMessage());

                    }

                    @Override
                    public void onNext(Info info) {
                        Info.setInfo(info);
                    }
                });
    }
}
