package ie.droidfactory.fibarodemoapp;

import android.app.Activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;


import ie.droidfactory.fibarodemoapp.model.Device;
import ie.droidfactory.fibarodemoapp.retrofit.FibaroService;
import ie.droidfactory.fibarodemoapp.retrofit.FibaroServiceAction;
import ie.droidfactory.fibarodemoapp.retrofit.RetrofitServiceFactory;
import ie.droidfactory.fibarodemoapp.utils.FibaroSharedPref;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class DeviceDetailsActivity extends AppCompatActivity {

    private static final String TAG = DeviceDetailsActivity.class.getSimpleName();

    private TextView tvName, tvType, tvValue;
    private FloatingActionButton fabOK;
    private Switch switchBinary;
    private NumberPicker numberPicker;
    private int devId=-1;
    private int deviceIndex = -1;
    private String value;
    private final static int PICKER_MAX = 100, PICKER_MIN=0;
    private final static String VALUE_ON="ON", VALUE_OFF="OFF";

//    private ActivityMainBinding deviceDetailBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_details);

//        deviceDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_device_details);
//        deviceDetailBinding..

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getResources().getString(R.string.title_device_details));

        tvName = findViewById(R.id.device_details_text_name);
//        tvLocation = findViewById(R.id.device_details_text_location);
        tvType = findViewById(R.id.device_details_text_type);
        tvValue = findViewById(R.id.device_details_settings_text_display_value);

        switchBinary = findViewById(R.id.device_details_settings_switch_binary);
        numberPicker = findViewById(R.id.device_details_settings_picker_number);
        fabOK = findViewById(R.id.device_details_settings_fab);
        numberPicker.setVisibility(View.GONE);
        switchBinary.setVisibility(View.GONE);

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            deviceIndex = extras.getInt(DevicesListActivity.DEVICE_INDEX);
            devId=Device.getDevicesList().get(deviceIndex).getId();
            value = Device.getDevicesList().get(deviceIndex).getProperties().getValue();
            devId = Device.getDevicesList().get(deviceIndex).getId();

            tvName.setText(Device.getDevicesList().get(deviceIndex).getName());
//            tvLocation.setText(String.valueOf(Device.getDevicesList().get(deviceIndex).getRoomID()));
            tvType.setText(Device.getDevicesList().get(deviceIndex).getType());

        }

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


    private void setDeviceValueBinary(String credentials, final int deviceId, final String deviceValue){
        FibaroServiceAction service = RetrofitServiceFactory.createRetrofitService(FibaroServiceAction.class, FibaroService.SERVICE_ENDPOINT, credentials);


        service.setActionBinary(deviceId, deviceValue)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>(){

                    @Override
                    public void onCompleted() {

                        Intent intent = new Intent();
                        intent.putExtra("index", deviceIndex);
                        intent.putExtra("value",value);
                        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
                        setResult(Activity.RESULT_OK, intent);
                        finish();

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d(TAG, e.getMessage());
                        Intent intent = new Intent();
                        intent.putExtra("error", e.getMessage());
                        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
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
                        Intent intent = new Intent();
                        intent.putExtra("index", deviceIndex);
                        intent.putExtra("value",value);
                        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
                        setResult(Activity.RESULT_OK, intent);
                        finish();

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d(TAG, e.getMessage());
                        Intent intent = new Intent();
                        intent.putExtra("error", e.getMessage());
                        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }

                    @Override
                    public void onNext(String result) {
                        Log.d(TAG, "action response: "+result);
                    }
                });
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
