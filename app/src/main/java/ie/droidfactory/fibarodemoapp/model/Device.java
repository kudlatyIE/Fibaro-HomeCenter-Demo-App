package ie.droidfactory.fibarodemoapp.model;

import java.util.ArrayList;

/**
 * Created by kudlaty on 2018-02-18.
 */

public class Device {

    private int id;
    private String name;
    private int sectionID;
    private int roomID;
    private int sortOrder;
    private String type;
    private Properties properties;
    private Actions actions;

    private static final String KEY_DOMM="dimmable_light";
    private static final String KEY_BINARY="binary_light";

    private static ArrayList<Device> devicesList;

    public static ArrayList<Device> getDevicesList() {
        return devicesList;
    }

    public static void setDevicesList(ArrayList<Device> devicesList) {
        Device.devicesList = devicesList;
    }

    //test only!
    public static String printList(){
        String result="DEVICES:";
        if(devicesList!=null){
            for (Device d: devicesList){
                result = result.concat("\nID:\t"+d.getId()
                        +"\nNAME:\t"+d.getName()
                        +"\nTYPE:\t"+d.getType()
                        +"\nPROP_DEAD:\t"+d.getProperties().getDead()
                        +"\nPROP_DISABLE:\t"+d.getProperties().getDisabled()
                        +"\nPROP_VALUE:\t"+d.getProperties().getValue());
            }
        }else result = result.concat(" is NULL");

        return result;
    }

    public static ArrayList<Device> filerDeviceList(ArrayList<Device> oldList){

        ArrayList<Device> list = new ArrayList<>();
        for(Device d: oldList){
            if(d.getType().equals(KEY_BINARY) || d.getType().equals(KEY_DOMM)){
                list.add(d);
            }
        }
        return list;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getSectionID() {
        return sectionID;
    }

    public int getRoomID() {
        return roomID;
    }

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