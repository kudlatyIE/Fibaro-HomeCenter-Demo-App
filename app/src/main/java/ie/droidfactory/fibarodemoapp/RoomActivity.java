package ie.droidfactory.fibarodemoapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

import ie.droidfactory.fibarodemoapp.model.FibaroType;
import ie.droidfactory.fibarodemoapp.model.Room;
import ie.droidfactory.fibarodemoapp.retrofit.FibaroService;
import ie.droidfactory.fibarodemoapp.retrofit.FibaroServiceRoom;
import ie.droidfactory.fibarodemoapp.retrofit.FibaroServiceSection;
import ie.droidfactory.fibarodemoapp.retrofit.RetrofitServiceFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class RoomActivity extends AppCompatActivity implements FibaroAdapter.DeviceAdapterOnClickHandler{

    private static final String TAG = RoomActivity.class.getSimpleName();
    public static final String ROOM_ID="room_id";
    private FibaroAdapter mFibaroAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        //TODO: use ID to filter rooms for selected section
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            int id = extras.getInt(RoomActivity.ROOM_ID);
            Log.d(TAG, "received  section ID: "+id);
        }else Log.d(TAG, "missing bundle section ID");

        mRecyclerView = (RecyclerView) findViewById(R.id.room_recyclerview);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mFibaroAdapter = new FibaroAdapter(this, this, FibaroType.ROOM);
        mRecyclerView.setAdapter(mFibaroAdapter);
        getRoom(FibaroService.getCredentials());
    }

    @Override
    public void onClick(int objectId) {
        Intent intent = new Intent(RoomActivity.this, DevicesListActivity.class);
        intent.putExtra(ROOM_ID, objectId);
        startActivity(intent);
    }

    private void getRoom(String credentials){
        FibaroServiceRoom service = RetrofitServiceFactory.createRetrofitService(FibaroServiceRoom.class, FibaroService.SERVICE_ENDPOINT, credentials);

        service.getRooms()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<Room>>(){

                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "completed, filtered sections list size: "+Room.getRoomsList());
                        mFibaroAdapter.swapDevicesList(Room.getRoomsList());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d(TAG, "ERROR: "+e.getMessage());
                        Intent intent = new Intent();
                        intent.putExtra("error", e.getMessage());
                        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }

                    @Override
                    public void onNext(ArrayList<Room> rooms) {
                        Log.d(TAG, "downloaded rooms list size: "+rooms.size());

                        Room.setRoomsList(rooms);
                        Log.d(TAG, Room.printList());
                    }
                });
    }
}
