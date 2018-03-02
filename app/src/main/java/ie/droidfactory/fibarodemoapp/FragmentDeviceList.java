package ie.droidfactory.fibarodemoapp;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import ie.droidfactory.fibarodemoapp.model.Device;
import ie.droidfactory.fibarodemoapp.model.FibaroType;
import ie.droidfactory.fibarodemoapp.model.Room;
import ie.droidfactory.fibarodemoapp.model.Section;
import ie.droidfactory.fibarodemoapp.retrofit.FibaroService;
import ie.droidfactory.fibarodemoapp.viewmodel.DeviceViewModel;
import ie.droidfactory.fibarodemoapp.viewmodel.RoomViewModel;

import static ie.droidfactory.fibarodemoapp.FragmentDeviceDetails.REQUEST_CODE;

/**
 * Created by kudlaty on 2018-02-28.
 */

public class FragmentDeviceList extends Fragment implements FibaroAdapter.DeviceAdapterOnClickHandler{
    private final static String TAG = FragmentDeviceList.class.getSimpleName();

    private final static String KEY="object_index";

    private FibaroFragmentInterfaces mListener;
    private FibaroAdapter mDevicetAdapter;
    private RecyclerView mRecyclerView;
    private DeviceViewModel viewModel;
    private int index;


    public static FragmentDeviceList newInstance(int deviceId){
        FragmentDeviceList fragment = new FragmentDeviceList();
        Bundle args = new Bundle();
        args.putInt(KEY, deviceId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_devices_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mRecyclerView = view.findViewById(R.id.device_recyclerview);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mDevicetAdapter = new FibaroAdapter(getContext(), this, FibaroType.DEVICE);
        mRecyclerView.setAdapter(mDevicetAdapter);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments() != null) {
            index = getArguments().getInt(KEY);
            if(index>=0){

                viewModel = ViewModelProviders.of(this).get(DeviceViewModel.class);
                viewModel.getDevices(getActivity(), FibaroService.getCredentials(), index).observe(this, new Observer<ArrayList<Device>>() {
                    @Override
                    public void onChanged(@Nullable ArrayList<Device> devices) {
                        mDevicetAdapter.swapDevicesList(devices);
                    }
                });
            }else mListener.loginResponse(false, "incorrect room index");

        }else mListener.loginResponse(false, "missed room index");
    }
    @Override
    public void onResume(){
        super.onResume();
        if(index>=0) mListener.setActioneBar(getResources().getString(R.string.title_room_name)+" "+Room.getRoomsList().get(index).getName(), true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==REQUEST_CODE && resultCode== Activity.RESULT_OK) {
            String newValue = data.getStringExtra("value");
            int deviceIndex  = data.getIntExtra("index",-1);
            Log.d(TAG, "onUpdateSuccess value: "+newValue);
            viewModel = ViewModelProviders.of(this).get(DeviceViewModel.class);
            viewModel.updateDevice(deviceIndex, newValue);
            mDevicetAdapter.updateDevice(deviceIndex);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            mListener = (FibaroFragmentInterfaces) context;
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString()+ "loginCallback Listener is not " +
                    "implemented...");
        }

    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(int objectIndex) {
        mListener.onDeviceSelected(objectIndex);
    }

//    @Override
//    public void onUpdateSuccess(int deviceIndex, String newValue) {
//        Log.d(TAG, "onUpdateSuccess value: "+newValue);
//        viewModel = ViewModelProviders.of(this).get(DeviceViewModel.class);
//        viewModel.updateDevice(deviceIndex, newValue);
//        mDevicetAdapter.updateDevice(deviceIndex);
//    }

//    @Override
//    public void onUpdateFail(String errorMessage) {
//        Toast.makeText(getContext(), "Err: "+errorMessage, Toast.LENGTH_LONG).show();
//    }
}
