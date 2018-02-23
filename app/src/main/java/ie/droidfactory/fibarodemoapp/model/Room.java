package ie.droidfactory.fibarodemoapp.model;

import java.util.ArrayList;

/**
 * Created by kudlaty on 2018-02-18.
 */

public class Room implements FibaroObject{

    private int id;
    private String name;
    private int sectionID;
    private int sortOrder;

    private static ArrayList<Room> roomsList;

    private Room(int id, String name, int sectionId, int sortOrder){
        this.id=id;
        this.name=name;
        this.sectionID=sectionId;
        this.sortOrder=sortOrder;
    }




    public static void setRoomsList(ArrayList<Room> roomsList, int sectionID) {
        if(sectionID<0) Room.roomsList = roomsList;
        else {
            ArrayList<Room> list = new ArrayList<>();
            for (Room r: roomsList){
                if(r.getSectionID()==sectionID) list.add(r);
            }
            Room.roomsList=list;
        }
        //test only to display all rooms
//        addRoom(new Room(-1, "show all devices", -1, -1));

    }



    private static void addRoom(Room room){
        if(roomsList==null) roomsList = new ArrayList<>();
        roomsList.add(room);
    }
    public static ArrayList<Room> getRoomsList(){
        return roomsList;
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
    @Override
    public int getSortOrder() {
        return sortOrder;
    }
}

/*
    "id": 3,
	"name": "Living room",
	"sectionID": 1,
	"sortOrder": 2

 */

