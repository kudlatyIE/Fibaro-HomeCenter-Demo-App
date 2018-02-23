package ie.droidfactory.fibarodemoapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import ie.droidfactory.fibarodemoapp.model.Info;
import ie.droidfactory.fibarodemoapp.utils.FibaroSharedPref;
import ie.droidfactory.fibarodemoapp.viewmodel.InfoViewModel;

public class InfoActivity extends AppCompatActivity {

    private TextView tvSerialNum, tvMac, tvSoftVer, tvZwaveVer, tvBeta, tvServerStat, tvInfoTitle;
//    private TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
//        setContentView(R.layout.info_testing_layout);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getResources().getString(R.string.menu_item_info));

        tvInfoTitle = findViewById(R.id.info_text_title);
        tvSerialNum = findViewById(R.id.info_text_serialNo_value);
        tvMac = findViewById(R.id.info_text_mac_value);
        tvSoftVer = findViewById(R.id.info_text_softVareVer_value);
        tvZwaveVer = findViewById(R.id.info_text_zwaveVersion_value);
        tvBeta = findViewById(R.id.info_text_beta_value);
        tvServerStat = findViewById(R.id.info_text_serverStatus_value);


        if(Info.getInfo()!=null) setInfo(Info.getInfo());
        else {
            InfoViewModel infoViewModel = ViewModelProviders.of(this).get(InfoViewModel.class);
            infoViewModel.getInfo().observe(this, new Observer<Info>() {
                @Override
                public void onChanged(@Nullable Info info) {
                    setInfo(info);
                }
            });
        }
    }


    private void setInfo(Info info){
        if(info==null){
            tvInfoTitle.setText(R.string.data_not_available);
//            tvInfoTitle.setText(R.string.data_not_available);
        }else {
            tvServerStat.setText(String.valueOf(info.getServerStatus()));
            tvBeta.setText(String.valueOf(info.isBeta()));
            tvZwaveVer.setText(info.getZwaveVersion());
            tvSoftVer.setText(info.getSoftVersion());
            tvMac.setText(info.getMac());
            tvSerialNum.setText(info.getSerialNumber());
            tvInfoTitle.setText("Home Center info");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_basic, menu);
        menu.findItem(R.id.menu_item_info).setVisible(false);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_item_logout:
                FibaroSharedPref.setCredentials(this, null);
                startActivity(new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
