package ie.droidfactory.fibarodemoapp.model;

import java.util.ArrayList;

/**
 * Created by kudlaty on 2018-02-18.
 */

public class Section implements FibaroObject{

    private int id;
    private String name;
    private int sortOrder;

    private Section(int id, String name, int sortOrder){
        this.id=id;
        this.name=name;
        this.sortOrder=sortOrder;
    }
    private static ArrayList<Section> sectionsList;

    public static void addSection(Section section){
        if(sectionsList==null){
            sectionsList = new ArrayList<>();
        }
        sectionsList.add(section);
    }

    public static ArrayList<Section> getSectionsList(){
        return sectionsList;
    }
    public static void setSectionsList(ArrayList<Section> list){
        sectionsList=list;
    }

    //test - for all rooms
    private static void addShowAllItem(){
        if(sectionsList!=null){
            sectionsList.add(new Section(-1, "show all rooms", -1));
        }
    }


    //test only!
    public static String printList(){
        String result="SECTIONS:";
        if(sectionsList!=null){
            for (Section s: sectionsList){
                result = result.concat("\nID:\t"+s.getId()
                        +"\nNAME:\t"+s.getName()
                        +"\nORDER:\t"+s.getSortOrder());
            }
        }else result = result.concat(" is NULL");

        return result;
    }
    @Override
    public int getId() {
        return id;
    }
    @Override
    public String getName() {
        return name;
    }
    @Override
    public int getSortOrder() {
        return sortOrder;
    }
}

/*
        "id": 1,
        "name": "Ground floor",
        "sortOrder": 1

*/


