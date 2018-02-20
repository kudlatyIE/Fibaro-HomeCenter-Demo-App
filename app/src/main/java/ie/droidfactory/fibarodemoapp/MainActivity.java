package ie.droidfactory.fibarodemoapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.okhttp.Credentials;

import ie.droidfactory.fibarodemoapp.model.Info;
import ie.droidfactory.fibarodemoapp.retrofit.FibaroService;
import ie.droidfactory.fibarodemoapp.retrofit.FibaroServiceInfo;
import ie.droidfactory.fibarodemoapp.retrofit.RetrofitServiceFactory;
import ie.droidfactory.fibarodemoapp.utils.FibaroSharedPref;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int REQUEST_CODE = 234;

    private TextView tvOutput,tvInfo;
    private Button btnLogin;
    private CheckBox checkSave;
    private EditText editUserName, editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvInfo = findViewById(R.id.text_main_info);
        tvOutput = findViewById(R.id.text_main_output);
        btnLogin = findViewById(R.id.btn_main_login);
        checkSave = findViewById(R.id.checkBox_main_saveCredentials);
        editUserName = findViewById(R.id.edit_main_userName);
        editPassword = findViewById(R.id.edit_main_password);

        if(isInternetOn(this))getInfo();
        else tvOutput.setText("please turn on internet connection");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = editUserName.getText().toString();
                String pass = editPassword.getText().toString();
                if(user.length()==0 || pass.length()==0) tvOutput.setText("user name or password can't be empty");
                else {
                    if(checkSave.isChecked()){
                        FibaroSharedPref.setCredentials(MainActivity.this, createCredentials(user, pass));
                    }else {
                        FibaroService.setCredentials(createCredentials(user, pass));
                    }
                    Intent intent = new Intent(MainActivity.this, DevicesListActivity.class);
                    startActivityForResult(intent, REQUEST_CODE);
                }
            }
        });

    }
    @Override
    protected void onRestart() {
        super.onRestart();
//        clearFields();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("error");
                tvOutput.setText(result);

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // do nothing

            }
            clearFields();
        }
    }

    private void clearFields(){
        editPassword.setText("");
        editUserName.setText("");
    }

    private void getInfo(){
        FibaroServiceInfo service = RetrofitServiceFactory.createRetrofitService(FibaroServiceInfo.class, FibaroService.SERVICE_ENDPOINT, null);
        service.getInfo()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Info>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted...");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        tvOutput.setText(e.getMessage());

                    }

                    @Override
                    public void onNext(Info info) {
                        Log.d(TAG, "onNext...");
                        tvOutput.setText(info.toString());

                    }
                });
    }

    private String createCredentials(String userName, String pass){
        return Credentials.basic(userName, pass);
    }

    private boolean isInternetOn(Context context){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}
