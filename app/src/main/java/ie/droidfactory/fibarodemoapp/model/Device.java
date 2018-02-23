package ie.droidfactory.fibarodemoapp.model;

import java.util.ArrayList;

/**
 * Created by kudlaty on 2018-02-18.
 */

public class Device implements FibaroObject{

    private int id;
    private String name;
    private int sectionID;
    private int roomID;
    private int sortOrder;
    private String type;
    private Properties properties;
    private Actions actions;

    public static final String KEY_DOMM="dimmable_light";
    public static final String KEY_BINARY="binary_light";

    private static ArrayList<Device> devicesList;

    public static ArrayList<Device> getDevicesList() {
        return devicesList;
    }

    public static void setDevicesList(ArrayList<Device> devicesList) {
        Device.devicesList = devicesList;
    }

    /**
     * filter for binary and dimmable devices
     * @param oldList
     * @param roomId
     * @return
     */
    public static ArrayList<Device> filerDeviceList(ArrayList<Device> oldList, int roomId){

        ArrayList<Device> list = new ArrayList<>();
        for(Device d: oldList){
            if(d.getType().equals(KEY_BINARY) || d.getType().equals(KEY_DOMM)){
                if(roomId<0) list.add(d);
                else if(d.getRoomID()==roomId) list.add(d);
            }
        }
        return list;
    }
    @Override
    public int getId() {
        return id;
    }
    @Override
    public String getName() {
        return name;
    }

    public int getSectionID() {
        return sectionID;
    }

    public int getRoomID() {
        return roomID;
    }
    @Override
    public int getSortOrder() {
        return sortOrder;
    }

    public String getType() {
        return type;
    }

    public Properties getProperties() {
        return properties;
    }

    public Actions getActions() {
        return actions;
    }
}

/*

    "id": 3,
	"name": "Living room",
	"sectionID": 1,
	"sortOrder": 2,
	"name": "Lampka",
	"roomID": 2,
	"type": "binary_light",
	"properties": {
		"dead": "0",
		"disabled": "0",
		"value": "1"
	},
	"actions": {
		"setValue": 1,
		"turnOff": 0,
		"turnOn": 0
	},
	"sortOrder": 2

 */