package ie.droidfactory.fibarodemoapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.okhttp.Credentials;

import ie.droidfactory.fibarodemoapp.retrofit.FibaroService;
import ie.droidfactory.fibarodemoapp.utils.FibaroSharedPref;

/**
 * Created by kudlaty on 2018-02-28.
 */

public class FragmentLogin extends Fragment{

    private final static String TAG = FragmentLogin.class.getSimpleName();

    private TextView tvOutput,tvInfo;
    private Button btnLogin;
    private CheckBox checkSave;
    private EditText editUserName, editPassword;
    private final static String KEY = "message";

    private FibaroFragmentInterfaces mListener;

    public static FragmentLogin newInstance(String message){
        FragmentLogin fragment = new FragmentLogin();
        Bundle args = new Bundle();
        args.putString(KEY, message);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvOutput = view.findViewById(R.id.text_main_output);
        tvInfo = view.findViewById(R.id.text_main_info);
        btnLogin = view.findViewById(R.id.btn_main_login);
        checkSave = view.findViewById(R.id.checkBox_main_saveCredentials);
        editUserName = view.findViewById(R.id.edit_main_userName);
        editPassword = view.findViewById(R.id.edit_main_password);
        tvInfo.setVisibility(View.INVISIBLE);//temporary invisible...
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle extras = getArguments();
        if(extras!=null) {
            //TODO: error message if was authorization problem in other fragments
            String errorMessage = extras.getString(KEY);
            tvOutput.setText(errorMessage);
            checkSave.setChecked(false);
        }
        //TODO: just save shared instance and invoke callback...
        mListener.setActioneBar(getResources().getString(R.string.title_login), false);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = editUserName.getText().toString();
                String pass = editPassword.getText().toString();
                if(user.length()==0 || pass.length()==0) tvOutput.setText(R.string.username_pass_request);
                else {
                    if(checkSave.isChecked()){
                        FibaroSharedPref.setCredentials(getContext(), createCredentials(user, pass));
                    }
                    FibaroService.setCredentials(createCredentials(user, pass));
                    mListener.loginResponse(true, null);

                }
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = null;
        if (context instanceof Activity) {
            activity = (Activity) context;
        }
        try{
            mListener = (FibaroFragmentInterfaces) activity;
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString()+ "loginCallback Listener is not " +
                    "implemented...");
        }

    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    //not used in fragment..
    private void clearFields(){
        editPassword.setText("");
        editUserName.setText("");
    }

    private String createCredentials(String userName, String pass){
        return Credentials.basic(userName, pass);
    }
}
