package ie.droidfactory.fibarodemoapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ie.droidfactory.fibarodemoapp.model.Info;
import ie.droidfactory.fibarodemoapp.viewmodel.InfoViewModel;

/**
 * Created by kudlaty on 2018-02-28.
 */

public class FragmentInfo extends Fragment {

    private TextView tvSerialNum, tvMac, tvSoftVer, tvZwaveVer, tvBeta, tvServerStat, tvInfoTitle;

    private FibaroFragmentInterfaces mListener;

    public static FragmentInfo newInstance(){
        return new FragmentInfo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvInfoTitle = view.findViewById(R.id.info_text_title);
        tvSerialNum = view.findViewById(R.id.info_text_serialNo_value);
        tvMac = view.findViewById(R.id.info_text_mac_value);
        tvSoftVer = view.findViewById(R.id.info_text_softVareVer_value);
        tvZwaveVer = view.findViewById(R.id.info_text_zwaveVersion_value);
        tvBeta = view.findViewById(R.id.info_text_beta_value);
        tvServerStat = view.findViewById(R.id.info_text_serverStatus_value);


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

    @Override
    public void onResume(){
        super.onResume();
        mListener.setActioneBar(getResources().getString(R.string.title_info), true);
    }

    private void setInfo(Info info){
        if(info==null){
            tvInfoTitle.setText(R.string.data_not_available);
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

}
