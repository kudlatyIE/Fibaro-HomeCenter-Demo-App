package ie.droidfactory.fibarodemoapp;

/**
 * Created by kudlaty on 2018-02-28.
 */

public interface FibaroFragmentInterfaces {

    void loginResponse(boolean loginSuccess, String message);
    void onSectionSelected(int sectionIndex);
    void onRoomSelected(int roomIndex);
    void onDeviceSelected(int deviceIndex);
    void onDeviceNewValueAction();
    void setActioneBar(String title, boolean isHomeUp);
}
