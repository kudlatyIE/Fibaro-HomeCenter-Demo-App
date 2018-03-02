package ie.droidfactory.fibarodemoapp;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ie.droidfactory.fibarodemoapp.model.Device;
import ie.droidfactory.fibarodemoapp.model.FibaroType;
import ie.droidfactory.fibarodemoapp.model.Room;
import ie.droidfactory.fibarodemoapp.retrofit.FibaroService;
import ie.droidfactory.fibarodemoapp.retrofit.FibaroServiceAction;
import ie.droidfactory.fibarodemoapp.retrofit.RetrofitServiceFactory;
import ie.droidfactory.fibarodemoapp.viewmodel.DeviceViewModel;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kudlaty on 2018-02-28.
 */

public class FragmentDeviceDetails extends Fragment {

    private final static String TAG = FragmentDeviceDetails.class.getSimpleName();

    public static final int REQUEST_CODE = 234;
    private final static String KEY="object_index";
    private TextView tvName, tvType, tvValue;
    private FloatingActionButton fabOK;
    private Switch switchBinary;
    private NumberPicker numberPicker;
    private int devId=-1;
    private int deviceIndex = -1, index;
    private String value;
    private final static int PICKER_MAX = 100, PICKER_MIN=0;
    private final static String VALUE_ON="ON", VALUE_OFF="OFF";

    private FibaroFragmentInterfaces mListener;
//    private OnUpdateDeviceListener onUpdateDeviceListener;

    public static FragmentDeviceDetails newInstance(FragmentDeviceList fragmentDeviceList, int deviceIndex){
        FragmentDeviceDetails fragment = new FragmentDeviceDetails();
        if(fragmentDeviceList!=null) fragment.setTargetFragment(fragmentDeviceList, REQUEST_CODE);
        Bundle args = new Bundle();
        args.putInt(KEY, deviceIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_device_details, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvName = view.findViewById(R.id.device_details_text_name);
        tvType = view.findViewById(R.id.device_details_text_type);
        tvValue = view.findViewById(R.id.device_details_settings_text_display_value);

        switchBinary = view.findViewById(R.id.device_details_settings_switch_binary);
        numberPicker = view.findViewById(R.id.device_details_settings_picker_number);
        fabOK = view.findViewById(R.id.device_details_settings_fab);
        numberPicker.setVisibility(View.GONE);
        switchBinary.setVisibility(View.GONE);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments() != null) {
            index = getArguments().getInt(KEY);
            if(index>=0){

                deviceIndex = index;
                devId=Device.getDevicesList().get(deviceIndex).getId();
                value = Device.getDevicesList().get(deviceIndex).getProperties().getValue();
                devId = Device.getDevicesList().get(deviceIndex).getId();

                tvName.setText(Device.getDevicesList().get(deviceIndex).getName());
                tvType.setText(Device.getDevicesList().get(deviceIndex).getType());
            }else mListener.loginResponse(false, "incorrect device index");

        }else mListener.loginResponse(false, "missed device index");

        if(Device.getDevicesList().get(deviceIndex).getType().equals(Device.KEY_BINARY)){

            switchBinary.setVisibility(View.VISIBLE);

            if(Device.getDevicesList().get(deviceIndex).getProperties().getValue().equals("0")){
                value=VALUE_OFF;
                switchBinary.setChecked(false);
            }else {
                value = VALUE_ON;
                switchBinary.setChecked(true);
            }


        }else {
            //TODO: numberPicker visible OFF, run setDeviceValueBinary()
            numberPicker.setVisibility(View.VISIBLE);
            numberPicker.setMaxValue(PICKER_MAX);
            numberPicker.setMinValue(PICKER_MIN);
            try {
                numberPicker.setValue(Integer.parseInt(value));
            }catch (NumberFormatException e){
                numberPicker.setValue(0);
            }
            if(numberPicker.getValue()==0) tvValue.setText(VALUE_OFF);
        }
        tvValue.setText(value);


        fabOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Device.getDevicesList().get(deviceIndex).getType().equals(Device.KEY_BINARY)){
                    String binaryValue;
                    if(switchBinary.isChecked()) {
                        binaryValue=FibaroServiceAction.VALUE_TURN_ON;
                        value ="1";
                    }
                    else {
                        binaryValue = FibaroServiceAction.VALUE_TURN_OFF;
                        value="0";
                    }
                    setDeviceValueBinary(FibaroService.getCredentials(), devId, binaryValue);
                }else {
                    value=String.valueOf(numberPicker.getValue());
                    setDeviceValueDimm(FibaroService.getCredentials(), devId, numberPicker.getValue());
                }
            }
        });

        switchBinary.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()) tvValue.setText(VALUE_ON);
                else tvValue.setText(VALUE_OFF);
            }
        });
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldNum, int newNum) {
                tvValue.setText(String.valueOf(newNum));
            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();
        if(index>=0) mListener.setActioneBar(getResources().getString(R.string.title_device_details)+" "+ Device.getDevicesList().get(index).getName(), true);
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

    private void setDeviceValueBinary(String credentials, final int deviceId, final String deviceValue){
        FibaroServiceAction service = RetrofitServiceFactory.createRetrofitService(FibaroServiceAction.class, FibaroService.SERVICE_ENDPOINT, credentials);
        Log.d(TAG, "set BINARY: "+deviceValue);

        service.setActionBinary(deviceId, deviceValue)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>(){

                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "set value, on Completed: "+value);
//                        onUpdateDeviceListener.onUpdateSuccess(deviceIndex, value);
                        getTargetFragment().onActivityResult(REQUEST_CODE, Activity.RESULT_OK, createIntentData(deviceIndex, value));
                        mListener.onDeviceNewValueAction();

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d(TAG, e.getMessage());
//                        onUpdateDeviceListener.onUpdateFail(e.getMessage());
                        Toast.makeText(getContext(), "Err: "+e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(String result) {
                        Log.d(TAG, "action response: "+result);
                    }
                });
    }

    private void setDeviceValueDimm(String credentials, final int deviceId, final int deviceValue){
        FibaroServiceAction service = RetrofitServiceFactory.createRetrofitService(FibaroServiceAction.class, FibaroService.SERVICE_ENDPOINT, credentials);


        service.setActionDimmable(deviceId, FibaroServiceAction.NAME_SET_VALUE, deviceValue)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>(){

                    @Override
                    public void onCompleted() {
//                        onUpdateDeviceListener.onUpdateSuccess(deviceIndex, value);
//                        getActivity().getSupportFragmentManager().popBackStack();
                        getTargetFragment().onActivityResult(REQUEST_CODE, Activity.RESULT_OK, createIntentData(deviceIndex, value));
                        mListener.onDeviceNewValueAction();

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d(TAG, e.getMessage());
//                        onUpdateDeviceListener.onUpdateFail(e.getMessage());
                        Toast.makeText(getContext(), "Err: "+e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(String result) {
                        Log.d(TAG, "action response: "+result);
                    }
                });
    }

    private Intent createIntentData(int index, String value){
        Intent intent = new Intent();
        intent.putExtra("index", index);
        intent.putExtra("value", value);
        return intent;
    }

//    public interface OnUpdateDeviceListener{
//        void onUpdateSuccess(int deviceIndex, String newValue);
//        void onUpdateFail(String errorMessage);
//    }
}
