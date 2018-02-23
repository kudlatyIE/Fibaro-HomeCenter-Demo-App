package ie.droidfactory.fibarodemoapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.ArrayList;

import ie.droidfactory.fibarodemoapp.model.Room;
import ie.droidfactory.fibarodemoapp.model.Section;
import ie.droidfactory.fibarodemoapp.retrofit.FibaroService;
import ie.droidfactory.fibarodemoapp.retrofit.FibaroServiceRoom;
import ie.droidfactory.fibarodemoapp.retrofit.RetrofitServiceFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by kudlaty on 2018-02-22.
 */

public class RoomViewModel extends ViewModel {

    private static final String TAG = SectionViewModel.class.getSimpleName();

    private MutableLiveData<ArrayList<Room>> mutableLiveData;

    public LiveData<ArrayList<Room>> getRooms( String credentials, int sectionId){
        if(mutableLiveData==null){
            mutableLiveData = new MutableLiveData<>();
            loadRooms(credentials, sectionId);
        }
        return mutableLiveData;
    }


    private void loadRooms(String credentials, final int objectIndex){
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
                        Log.d(TAG, "ERROR: "+e.getMessage());
                    }

                    @Override
                    public void onNext(ArrayList<Room> rooms) {
                        Room.setRoomsList(rooms, Section.getSestionsList().get(objectIndex).getId());
                    }
                });
    }

}
