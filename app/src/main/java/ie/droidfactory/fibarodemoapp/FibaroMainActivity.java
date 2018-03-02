package ie.droidfactory.fibarodemoapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import ie.droidfactory.fibarodemoapp.model.Section;
import ie.droidfactory.fibarodemoapp.retrofit.FibaroService;
import ie.droidfactory.fibarodemoapp.utils.FibaroSharedPref;

public class FibaroMainActivity extends AppCompatActivity implements FibaroFragmentInterfaces{

    private final static String TAG = FibaroMainActivity.class.getSimpleName();
    private final static String TAG_FRAGMENT_LOGIN="fragment_login",
                                TAG_FRAGMENT_SECTION="fragment_section",
                                TAG_FRAGMENT_ROOM="fragment_room",
                                TAG_FRAGMENT_DEVICES_LIST="fragment_dev_list",
                                TAG_FRAGMENT_DEVICE_DETAILS="fragment_dev_details",
                                TAG_FRAGMENT_INFO="fragment_info",
                                TAG_STACK="my_fragment";

    public final static String FRAGMENT_TAG="fragment";
    private static String mainFragmentTag = TAG_FRAGMENT_INFO;
    private Fragment mainFragment;
    private FragmentTransaction ft;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fibaro_main);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(getResources().getString(R.string.title_login));

        if(isInternetOn(this)){
            String credentials = FibaroSharedPref.getCredentials(this);
            if(credentials!=null){
                FibaroService.setCredentials(credentials);
                //TODO: start Section Fragment
                mainFragmentTag = TAG_FRAGMENT_SECTION;

            }else{
                //TODO: start Login Fragment
                mainFragmentTag = TAG_FRAGMENT_LOGIN;
            }
            if (savedInstanceState == null) loadFragments(mainFragmentTag, null, -1);
        }else{
            //TODO: display warning: no internet connection...
        }
    }

    private void loadFragments(String key, String message, int objectId){

        if(findViewById(R.id.main_fragment_container)!=null) {

            ft = getSupportFragmentManager().beginTransaction();
            mainFragment = setMainFragment(key, message, objectId);
            ft.add(R.id.main_fragment_container, mainFragment, key).addToBackStack(TAG_STACK).commit();
            getSupportFragmentManager().executePendingTransactions();

        }
    }

    private Fragment setMainFragment(String key, String message, int objectIndex){
//        Toast.makeText(this, "set fragment: "+key, Toast.LENGTH_SHORT).show();
        Fragment frag;
        switch (key){
            case TAG_FRAGMENT_LOGIN:
                frag = FragmentLogin.newInstance(message);
                break;
            case TAG_FRAGMENT_SECTION:
                frag = FragmentSection.newInstance();
                break;
            case TAG_FRAGMENT_ROOM:
                frag = FragmentRoom.newInstance(objectIndex);
                break;
            case TAG_FRAGMENT_DEVICES_LIST:
                frag = FragmentDeviceList. newInstance(objectIndex);
                break;
            case TAG_FRAGMENT_DEVICE_DETAILS:
                frag = FragmentDeviceDetails.newInstance(getDevListFragment(),objectIndex);
                break;
            case TAG_FRAGMENT_INFO:
                frag = FragmentInfo.newInstance();
                break;
            default:
                frag = FragmentLogin.newInstance("invalid TAG");
                break;
        }
        return frag;
    }

    @Override
    public void loginResponse(boolean loginSuccess, String message) {
        //TODO: if success==true start Section Fragment, else start Login Fragment
        if(loginSuccess){
            Log.d(TAG, "login success..");
            loadFragments(TAG_FRAGMENT_SECTION, null, -1);
        }else {
            Log.d(TAG, "login fail: "+message);
            getSupportFragmentManager().popBackStack(TAG_STACK, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            loadFragments(TAG_FRAGMENT_LOGIN, message, -1);
        }
    }

    @Override
    public void onSectionSelected(int sectionIndex) {
        //TODO: start Room Fragment with selected section ID
        Log.d(TAG, "onSectionSelected clicked index: "+sectionIndex);
        loadFragments(TAG_FRAGMENT_ROOM,null, sectionIndex);

    }

    @Override
    public void onRoomSelected(int roomIndex) {
        //TODO: start Device Fragment with selected room ID
        Log.d(TAG, "onRoomSelected clicked index: "+roomIndex);
        loadFragments(TAG_FRAGMENT_DEVICES_LIST, null, roomIndex);
//        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onDeviceSelected(int deviceIndex) {
        //TODO: start DeviceDetails Fragment with selected device ID
        loadFragments(TAG_FRAGMENT_DEVICE_DETAILS, null, deviceIndex);
//        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onDeviceNewValueAction() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void setActioneBar(String title, boolean isHomeUp) {
        actionBar.setDisplayHomeAsUpEnabled(isHomeUp);
        actionBar.setTitle(title);
    }


    private boolean isInternetOn(Context context){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_fragment_container);
        if(fragment instanceof FragmentSection) new ExitDialog(this).showExit().show();
        else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().executePendingTransactions();
                getSupportFragmentManager().findFragmentById(R.id.main_fragment_container).onResume();
            }else new ExitDialog(this).showExit().show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_basic, menu);
        return true;
    }

    private FragmentDeviceList getDevListFragment(){
        return (FragmentDeviceList) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_DEVICES_LIST);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                    getSupportFragmentManager().popBackStack();
                    getSupportFragmentManager().executePendingTransactions();
                    getSupportFragmentManager().findFragmentById(R.id.main_fragment_container).onResume();

                }
                else new ExitDialog(this).showExit().show();
                break;
            case R.id.menu_item_info:
                FragmentInfo frag = (FragmentInfo) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_INFO);
                if(!(frag!=null && frag.isVisible())){
                    loadFragments(TAG_FRAGMENT_INFO, null, -1);
                    actionBar.setTitle(getResources().getString(R.string.title_login));
                }
                break;
            case R.id.menu_item_logout:
                FibaroSharedPref.setCredentials(this, null);
                //TODO:clear all fragments and start LoginFragment

                getSupportFragmentManager().popBackStack(TAG_STACK, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                ft = getSupportFragmentManager().beginTransaction();
                mainFragment = setMainFragment(TAG_FRAGMENT_LOGIN, null, -1);
                ft.add(R.id.main_fragment_container, mainFragment, TAG_FRAGMENT_LOGIN).addToBackStack(TAG_STACK).commit();
                getSupportFragmentManager().executePendingTransactions();
                actionBar.setTitle(getResources().getString(R.string.title_login));
                break;

        }
        return super.onOptionsItemSelected(item);
    }

}
