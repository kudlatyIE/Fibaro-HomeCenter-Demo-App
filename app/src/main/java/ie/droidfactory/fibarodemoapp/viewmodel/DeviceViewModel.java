package ie.droidfactory.fibarodemoapp.viewmodel;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

import ie.droidfactory.fibarodemoapp.model.Device;
import ie.droidfactory.fibarodemoapp.model.Room;
import ie.droidfactory.fibarodemoapp.retrofit.FibaroService;
import ie.droidfactory.fibarodemoapp.retrofit.FibaroServiceDevice;
import ie.droidfactory.fibarodemoapp.retrofit.RetrofitServiceFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

/**
 * Created by kudlaty on 2018-02-22.
 */

public class DeviceViewModel extends ViewModel{

    private final static String TAG = DeviceViewModel.class.getSimpleName();

    private MutableLiveData<ArrayList<Device>> mutableLiveData;

    public LiveData<ArrayList<Device>> getDevices(Activity activity, String credentials, int deviceId){
        if(mutableLiveData==null){
            mutableLiveData = new MutableLiveData<>();
            loadDevices(activity, credentials, deviceId);
        }
        return mutableLiveData;
    }

    public void updateDevice(int deviceIndex, String value){
        mutableLiveData.getValue().get(deviceIndex).getProperties().setValue(value);
    }



    private void loadDevices(final Activity activity, String credentials, final int objectIndex){
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
                        Log.d(TAG, "ERROR: "+e.getMessage());
                        Intent intent = new Intent();
                        intent.putExtra("error", e.getMessage());
                        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
                        activity.setResult(Activity.RESULT_OK, intent);
                        activity.finish();
                    }

                    @Override
                    public void onNext(ArrayList<Device> devices) {
                        Device.setDevicesList(Device.filerDeviceList(devices, Room.getRoomsList().get(objectIndex).getId()));
                    }
                });
    }

}
