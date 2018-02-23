package ie.droidfactory.fibarodemoapp;

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

import java.util.ArrayList;

import ie.droidfactory.fibarodemoapp.model.FibaroType;
import ie.droidfactory.fibarodemoapp.model.Room;
import ie.droidfactory.fibarodemoapp.model.Section;
import ie.droidfactory.fibarodemoapp.retrofit.FibaroService;
import ie.droidfactory.fibarodemoapp.utils.FibaroSharedPref;
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
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        int sectionId=-1;
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
             sectionId = extras.getInt(SectionActivity.SECTION_INDEX);
             actionBar.setTitle(getResources().getString(R.string.title_section_name)+" "+Section.getSestionsList().get(sectionId).getName());
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
