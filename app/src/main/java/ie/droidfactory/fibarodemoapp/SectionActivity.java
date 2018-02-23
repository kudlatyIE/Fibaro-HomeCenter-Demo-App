package ie.droidfactory.fibarodemoapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;

import ie.droidfactory.fibarodemoapp.model.FibaroType;
import ie.droidfactory.fibarodemoapp.model.Section;
import ie.droidfactory.fibarodemoapp.retrofit.FibaroService;
import ie.droidfactory.fibarodemoapp.utils.FibaroSharedPref;
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
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(getResources().getString(R.string.title_sestions_all));

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
    @Override
    public void onBackPressed() {
        new ExitDialog(this).showExit().show();
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
                new ExitDialog(this).showExit().show();
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
