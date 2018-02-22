package ie.droidfactory.fibarodemoapp;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import ie.droidfactory.fibarodemoapp.model.Device;
import ie.droidfactory.fibarodemoapp.model.Room;
import ie.droidfactory.fibarodemoapp.retrofit.FibaroService;
import ie.droidfactory.fibarodemoapp.retrofit.FibaroServiceAction;
import ie.droidfactory.fibarodemoapp.retrofit.FibaroServiceDevice;
import ie.droidfactory.fibarodemoapp.retrofit.RetrofitServiceFactory;
import ie.droidfactory.fibarodemoapp.viewmodel.DeviceViewModel;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class DeviceDetailsActivity extends AppCompatActivity {

    private static final String TAG = DeviceDetailsActivity.class.getSimpleName();

    private TextView tvInfo;
    private Button btnError, btnSuccess;
    private int devId=-1;
    private int deviceIndex = -1;
    private String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_details);
        tvInfo = findViewById(R.id.device_acction_text_info);
        btnError = findViewById(R.id.device_acction_btn_error);
        btnSuccess = findViewById(R.id.device_acction_btn_success);

        //TODO: parse device ID to String!!!


        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            deviceIndex = extras.getInt(DevicesListActivity.DEVICE_INDEX);
            devId=Device.getDevicesList().get(deviceIndex).getId();
            Log.d(TAG, "bundle - dev ID: "+ devId);
            Log.d(TAG, "bundle - dev NAME: "+ Device.getDevicesList().get(deviceIndex).getName());

        }

        MyButton btn = new MyButton();
        btnSuccess.setOnClickListener(btn);
        btnError.setOnClickListener(btn);

        //TODO: TEST actions for BINARY and DIMMABLE devices!!!!

    }

    class MyButton implements View.OnClickListener{
        Intent intent = new Intent();

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.device_acction_btn_error:
                    intent.putExtra("error", "button error");
                    break;
                case R.id.device_acction_btn_success:
                    //TODO: testing
                    if(Device.getDevicesList().get(deviceIndex).getType().equals(Device.KEY_BINARY)){
                        value = "0"; //setAction "turnOff"
                    }else value = "77";
                    //TODO: test index out of bound
                    intent.putExtra("index", deviceIndex);
                    intent.putExtra("value",value);

                    break;
            }
            intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }


    private void setDeviceValueBinary(String credentials, final String deviceId, final String deviceValue){
        FibaroServiceAction service = RetrofitServiceFactory.createRetrofitService(FibaroServiceAction.class, FibaroService.SERVICE_ENDPOINT, credentials);


        service.setActionBinary(deviceId, deviceValue)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>(){

                    @Override
                    public void onCompleted() {
                       //TODO: set some notification and update Device list!!!
                        tvInfo.setText("Device: "+Device.getDevicesList().get(devId).getName()+" value: "+deviceValue);

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
                        Log.d(TAG, "received string response size: "+result.length());
                        Log.d(TAG, "action response: "+result);
                    }
                });
    }
}
