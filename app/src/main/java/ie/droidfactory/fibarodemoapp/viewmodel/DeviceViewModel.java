package ie.droidfactory.fibarodemoapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.ArrayList;

import ie.droidfactory.fibarodemoapp.model.Device;
import ie.droidfactory.fibarodemoapp.model.Room;
import ie.droidfactory.fibarodemoapp.retrofit.FibaroService;
import ie.droidfactory.fibarodemoapp.retrofit.FibaroServiceDevice;
import ie.droidfactory.fibarodemoapp.retrofit.RetrofitServiceFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kudlaty on 2018-02-22.
 */

public class DeviceViewModel extends ViewModel{

    private final static String TAG = DeviceViewModel.class.getSimpleName();

    private MutableLiveData<ArrayList<Device>> mutableLiveData;

    public LiveData<ArrayList<Device>> getDevices(String credentials, int deviceId){
        if(mutableLiveData==null){
            mutableLiveData = new MutableLiveData<>();
            loadDevices(credentials, deviceId);
        }
        return mutableLiveData;
    }

    public void updateDevice(String credenials, int deviceIndex, String value){
        mutableLiveData.getValue().get(deviceIndex).getProperties().setValue(value);
    }



    private void loadDevices(String credentials, final int objectIndex){
        FibaroServiceDevice service = RetrofitServiceFactory.createRetrofitService(FibaroServiceDevice.class, FibaroService.SERVICE_ENDPOINT, credentials);

        service.getDevices()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<Device>>(){

                    @Override
                    public void onCompleted() {
                        mutableLiveData.setValue(Device.getDevicesList());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(ArrayList<Device> devices) {
                        Device.setDevicesList(Device.filerDeviceList(devices, Room.getRoomsList().get(objectIndex).getId()));
                    }
                });
    }
}
