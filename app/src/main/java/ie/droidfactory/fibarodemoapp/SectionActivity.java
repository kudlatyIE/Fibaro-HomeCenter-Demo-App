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
import ie.droidfactory.fibarodemoapp.model.Section;
import ie.droidfactory.fibarodemoapp.retrofit.FibaroService;
import ie.droidfactory.fibarodemoapp.viewmodel.SectionViewModel;

public class SectionActivity extends AppCompatActivity implements FibaroAdapter.DeviceAdapterOnClickHandler{

    private static final String TAG = SectionActivity.class.getSimpleName();
    public static final String SECTION_INDEX ="section_index";
    private FibaroAdapter mFibaroAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);

        mRecyclerView = findViewById(R.id.section_recyclerview);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mFibaroAdapter = new FibaroAdapter(this, this, FibaroType.SECTION);
        mRecyclerView.setAdapter(mFibaroAdapter);

        SectionViewModel viewModel = ViewModelProviders.of(this).get(SectionViewModel.class);
        viewModel.getSections(this, FibaroService.getCredentials()).observe(this, new Observer<ArrayList<Section>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Section> sections) {
                mFibaroAdapter.swapDevicesList(sections);
            }

            @Override
            protected void finalize() throws Throwable {
                super.finalize();
            }
        });
    }

    @Override
    public void onClick(int objectIndex) {
        Intent intent = new Intent(SectionActivity.this, RoomActivity.class);
        intent.putExtra(SECTION_INDEX, objectIndex);
        startActivity(intent);
    }

}
