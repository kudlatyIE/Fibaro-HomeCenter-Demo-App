package ie.droidfactory.fibarodemoapp;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


import java.util.ArrayList;

import ie.droidfactory.fibarodemoapp.model.Device;
import ie.droidfactory.fibarodemoapp.model.FibaroType;
import ie.droidfactory.fibarodemoapp.model.Room;
import ie.droidfactory.fibarodemoapp.retrofit.FibaroService;
import ie.droidfactory.fibarodemoapp.utils.FibaroSharedPref;
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

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //use ID to filter devices for selected room
        Bundle extras = getIntent().getExtras();
        int roomIndex = -1;
        if(extras!=null){
            roomIndex = extras.getInt(RoomActivity.ROOM_INDEX);
            actionBar.setTitle(getResources().getString(R.string.title_room_name)+" "+Room.getRoomsList().get(roomIndex).getName());
        }else Log.d(TAG, "missing bundle room ID");

        mRecyclerView = findViewById(R.id.device_recyclerview);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mDevicetAdapter = new FibaroAdapter(this, this, FibaroType.DEVICE);
        mRecyclerView.setAdapter(mDevicetAdapter);

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

                    viewModel = ViewModelProviders.of(this).get(DeviceViewModel.class);
                    viewModel.updateDevice(FibaroService.getCredentials(), itemUpdated, value);
                    mDevicetAdapter.updateDevice(itemUpdated);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_basic, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_item_info:
                startActivity(new Intent(this, InfoActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.menu_item_logout:
                FibaroSharedPref.setCredentials(this, null);
                startActivity(new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;

        }
        return super.onOptionsItemSelected(item);
    }

}
