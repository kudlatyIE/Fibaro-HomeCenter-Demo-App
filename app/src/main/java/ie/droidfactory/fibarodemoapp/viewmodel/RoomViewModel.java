package ie.droidfactory.fibarodemoapp.viewmodel;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

import ie.droidfactory.fibarodemoapp.model.Room;
import ie.droidfactory.fibarodemoapp.model.Section;
import ie.droidfactory.fibarodemoapp.retrofit.FibaroService;
import ie.droidfactory.fibarodemoapp.retrofit.FibaroServiceRoom;
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

public class RoomViewModel extends ViewModel {

    private static final String TAG = RoomViewModel.class.getSimpleName();

    private MutableLiveData<ArrayList<Room>> mutableLiveData;

    public LiveData<ArrayList<Room>> getRooms(Activity activity, String credentials, int sectionId){
        if(mutableLiveData==null){
            mutableLiveData = new MutableLiveData<>();
            loadRooms(activity, credentials, sectionId);
        }
        return mutableLiveData;
    }


    private void loadRooms(final Activity activity, String credentials, final int objectIndex){
        FibaroServiceRoom service = RetrofitServiceFactory.createRetrofitService(FibaroServiceRoom.class, FibaroService.SERVICE_ENDPOINT, credentials);

        service.getRooms()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<Room>>(){

                    @Override
                    public void onCompleted() {
                        mutableLiveData.setValue(Room.getRoomsList());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d(TAG, "response fail: "+e.getMessage());
                        Intent intent = new Intent();
                        intent.putExtra("error", e.getMessage());
                        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
                        activity.setResult(Activity.RESULT_OK, intent);
                        activity.finish();
                    }

                    @Override
                    public void onNext(ArrayList<Room> rooms) {
                        Room.setRoomsList(rooms, Section.getSectionsList().get(objectIndex).getId());
                    }
                });
    }

}
