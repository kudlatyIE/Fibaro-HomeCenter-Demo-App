package ie.droidfactory.fibarodemoapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

import ie.droidfactory.fibarodemoapp.model.Device;
import ie.droidfactory.fibarodemoapp.model.FibaroType;
import ie.droidfactory.fibarodemoapp.model.Section;
import ie.droidfactory.fibarodemoapp.retrofit.FibaroService;
import ie.droidfactory.fibarodemoapp.retrofit.FibaroServiceDevice;
import ie.droidfactory.fibarodemoapp.retrofit.FibaroServiceSection;
import ie.droidfactory.fibarodemoapp.retrofit.RetrofitServiceFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class SectionActivity extends AppCompatActivity implements FibaroAdapter.DeviceAdapterOnClickHandler{

    private static final String TAG = SectionActivity.class.getSimpleName();
    public static final String SECTION_ID="section_id";
    private FibaroAdapter mFibaroAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);

        mRecyclerView = (RecyclerView) findViewById(R.id.section_recyclerview);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mFibaroAdapter = new FibaroAdapter(this, this, FibaroType.SECTION);
        mRecyclerView.setAdapter(mFibaroAdapter);

        getSections(FibaroService.getCredentials());
    }

    @Override
    public void onClick(int objectId) {
        Intent intent = new Intent(SectionActivity.this, RoomActivity.class);
        intent.putExtra(SECTION_ID, objectId);
        startActivity(intent);
    }

    private void getSections(String credentials){
        FibaroServiceSection service = RetrofitServiceFactory.createRetrofitService(FibaroServiceSection.class, FibaroService.SERVICE_ENDPOINT, credentials);

        service.getSection()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<Section>>(){

                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "completed, filtered sections list size: "+Section.getSestionsList());
                        mFibaroAdapter.swapDevicesList(Section.getSestionsList());
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
                    public void onNext(ArrayList<Section> sections) {
                        Log.d(TAG, "downloaded devices list size: "+sections.size());

                        Section.setSectionsList(sections);
                        Log.d(TAG, Section.printList());
                    }
                });
    }
}
