package ie.droidfactory.fibarodemoapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

import ie.droidfactory.fibarodemoapp.model.FibaroType;
import ie.droidfactory.fibarodemoapp.model.Room;
import ie.droidfactory.fibarodemoapp.retrofit.FibaroService;
import ie.droidfactory.fibarodemoapp.viewmodel.RoomViewModel;

public class RoomActivity extends AppCompatActivity implements FibaroAdapter.DeviceAdapterOnClickHandler{

    private static final String TAG = RoomActivity.class.getSimpleName();
    public static final String ROOM_INDEX ="room_index";
    private FibaroAdapter mFibaroAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        //TODO: use ID to filter rooms for selected section
        int sectionId=-1;
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
             sectionId = extras.getInt(SectionActivity.SECTION_INDEX);
            Log.d(TAG, "received  section ID: "+sectionId);
        }else Log.d(TAG, "missing bundle section ID");

        mRecyclerView = findViewById(R.id.room_recyclerview);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mFibaroAdapter = new FibaroAdapter(this, this, FibaroType.ROOM);
        mRecyclerView.setAdapter(mFibaroAdapter);

        RoomViewModel viewModel = ViewModelProviders.of(this).get(RoomViewModel.class);
        viewModel.getRooms(FibaroService.getCredentials(), sectionId).observe(this, new Observer<ArrayList<Room>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Room> rooms) {
                mFibaroAdapter.swapDevicesList(rooms);
            }
        });
    }

    @Override
    public void onClick(int objectIndex) {
        Intent intent = new Intent(RoomActivity.this, DevicesListActivity.class);
        intent.putExtra(ROOM_INDEX, objectIndex);
        startActivity(intent);
    }


}
