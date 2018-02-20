package ie.droidfactory.fibarodemoapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import ie.droidfactory.fibarodemoapp.model.Device;
import ie.droidfactory.fibarodemoapp.model.FibaroType;
import ie.droidfactory.fibarodemoapp.retrofit.FibaroService;
import ie.droidfactory.fibarodemoapp.retrofit.FibaroServiceDevice;
import ie.droidfactory.fibarodemoapp.retrofit.RetrofitServiceFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class DevicesListActivity extends AppCompatActivity implements FibaroAdapter.DeviceAdapterOnClickHandler{

    private static final String TAG = DevicesListActivity.class.getSimpleName();
    private FibaroAdapter mDevicetAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices_list);

        //TODO: use ID to filter devices for selected room
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            int id = extras.getInt(RoomActivity.ROOM_ID);
            Log.d(TAG, "received  room ID: "+id);
        }else Log.d(TAG, "missing bundle room ID");

        mRecyclerView = (RecyclerView) findViewById(R.id.device_recyclerview);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mDevicetAdapter = new FibaroAdapter(this, this, FibaroType.DEVICE);
        mRecyclerView.setAdapter(mDevicetAdapter);

        getDevices(FibaroService.getCredentials());
    }

    @Override
    public void onClick(int objectId) {
        Toast.makeText(getApplicationContext(), "clicked at dev ID: "+objectId, Toast.LENGTH_SHORT).show();
    }

    private void getDevices(String credentials){
        FibaroServiceDevice service = RetrofitServiceFactory.createRetrofitService(FibaroServiceDevice.class, FibaroService.SERVICE_ENDPOINT, credentials);

        //TODO: implement filter for binary & dimmable light
        service.getDevices()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<Device>>(){

                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "completed, filtered devices list size: "+Device.getDevicesList().size());

                        mDevicetAdapter.swapDevicesList(Device.getDevicesList());

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d(TAG, e.getMessage());
//                        Intent intent = new Intent();
//                        intent.putExtra("error", e.getMessage());
//                        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
//                        setResult(Activity.RESULT_OK, intent);
//                        finish();
                    }

                    @Override
                    public void onNext(ArrayList<Device> devices) {
                        Log.d(TAG, "downloaded devices list size: "+devices.size());
                        Device.setDevicesList(Device.filerDeviceList(devices));
                    }
                });
    }


}
