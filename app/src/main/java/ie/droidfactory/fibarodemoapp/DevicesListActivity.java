package ie.droidfactory.fibarodemoapp;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
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
import ie.droidfactory.fibarodemoapp.viewmodel.DeviceViewModel;

public class DevicesListActivity extends AppCompatActivity implements FibaroAdapter.DeviceAdapterOnClickHandler{

    private static final String TAG = DevicesListActivity.class.getSimpleName();
    public static final String DEVICE_INDEX="device_index";
    public static final int REQUEST_CODE = 345;
    private FibaroAdapter mDevicetAdapter;
    private RecyclerView mRecyclerView;
    private DeviceViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices_list);

        //TODO: use ID to filter devices for selected room
        Bundle extras = getIntent().getExtras();
        int roomIndex = -1;
        if(extras!=null){
            roomIndex = extras.getInt(RoomActivity.ROOM_INDEX);
            Log.d(TAG, "received  room ID: "+roomIndex);
        }else Log.d(TAG, "missing bundle room ID");

        mRecyclerView = (RecyclerView) findViewById(R.id.device_recyclerview);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mDevicetAdapter = new FibaroAdapter(this, this, FibaroType.DEVICE);
        mRecyclerView.setAdapter(mDevicetAdapter);

//        getDevices(FibaroService.getCredentials(), roomIndex);
        viewModel = ViewModelProviders.of(this).get(DeviceViewModel.class);
        viewModel.getDevices(FibaroService.getCredentials(), roomIndex).observe(this, new Observer<ArrayList<Device>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Device> devices) {
                mDevicetAdapter.swapDevicesList(devices);
            }
        });


    }

    @Override
    public void onClick(int objectIndex) {
//        Toast.makeText(getApplicationContext(), "clicked at dev ID: "+objectIndex, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(DevicesListActivity.this, DeviceDetailsActivity.class);
        intent.putExtra(DEVICE_INDEX, objectIndex);
        startActivityForResult(intent, REQUEST_CODE);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {
                String error = data.getStringExtra("error");
                int itemUpdated = data.getIntExtra("index",-1);
                String value = data.getStringExtra("value");
                if(error!=null && error.length()!=0){
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(), "return Item updated index:"+itemUpdated+" value: "+value, Toast.LENGTH_SHORT).show();
//                    mDevicetAdapter.swapDevicesList(Device.getDevicesList());
                    //TODO: implement item update - AHTUNG: default index = -1
                    viewModel = ViewModelProviders.of(this).get(DeviceViewModel.class);
                    viewModel.updateDevice(FibaroService.getCredentials(), itemUpdated, value);
                    mDevicetAdapter.updateDevice(itemUpdated);
                }

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // do nothing

            }
        }
    }




}
