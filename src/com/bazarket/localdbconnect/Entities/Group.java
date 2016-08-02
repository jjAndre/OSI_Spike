package com.bazarket.localdbconnect.Entities;


//import com.bazarket.localdbconnect.DAO.GroupDao;
//import com.bazarket.localdbconnect.DAO.KonstrElementDao;

import java.sql.Connection;
import java.util.*;

/**
 * Created by Sturlson on 08.06.2016.
 */
public class Group extends KonstruktorElement {
    //private static Connection connection;



    //Due to DbAccessLoc and DbAccessLocForSmth we need in group elements coordinates as well as elements' ids and types


    //we don't use static modifier because we permanentally use transGroupObject
    private HashMap<int[], int[]> previousItemsGroupMap;
    private HashMap<int[], int[]> actualItemsGroupMap;

//Move to full operations with entities
    //private Set<KonstruktorElement> groupPreviousItemsSet;
    private HashSet<KonstruktorElement> groupActualItemsSet;
    //

    //private GroupDao groupDao;

    /*private final static int GROUP_TYPE = 1;
    private final static boolean TO_SAVE_TO_DB_TRUE = true;*/



    public Group(int id){
        super(id);
    }

    public Group(Group groupToClone){
        super(groupToClone.getId());
        this.groupActualItemsSet = groupToClone.getGroupActualItemsSet();

    }





    /*public Group(int id, Connection connection){
        super(id, connection);
        groupDao  = new GroupDao(connection);
        //this.groupId = super.id;
        //this.id = id;
    }*/

    public void setGroupActualItemsSet(HashSet<KonstruktorElement> konstruktorElements){
        this.groupActualItemsSet = konstruktorElements;
    }

    public HashSet<KonstruktorElement> getGroupActualItemsSet(){

        return groupActualItemsSet;
    }


//It was disabled on July 15, 2016
/*    public HashMap<int[], int[]> getActualItemsGroupMap(){
        return actualItemsGroupMap;
    }

    public void setActualItemsGroupMap(Map<int[], int[]> mapToAssign){
        this.actualItemsGroupMap = (HashMap<int[], int[]>) mapToAssign;
    }


    public HashMap<int[],int[]> getPreviousItemsGroupMap(){

        return previousItemsGroupMap;
    }

    //I made it private to drive in a Group.groupActualItemsSet(Set<KonstruktorElement> )
    public void setPreviousItemsGroupMap(Map<int[],int[]> hashMapToAssign){
        this.previousItemsGroupMap = (HashMap<int[], int[]>) hashMapToAssign;
    }*/


    //this is the method for creating Group's Items Map
//Commented on July 12, 2016
    /*private Map<int[],int[]> receiveActualItemsGroupMapFromDB(){

        HashMap<int[], int[]> newActualItemsGroupMap = (HashMap<int[], int[]>) groupDao.returnActualItemsGroupMap(id);
        return newActualItemsGroupMap;

    }*/



    /*public void setGroupActualCoordinates(int[] coordsXY){

        setKonstrElementActualCoordinates(id, GROUP_TYPE, coordsXY, TO_SAVE_TO_DB_TRUE);
    }

    public void setGroupPreviousCoordninates(int[] coordsXY){
        setKonstrElementPreviousCoordinates(coordsXY);
    }*/




//Commented on July 12, 2016
    /*private void setActualItemsGroupMap(Map<int[],int[]> hashMapToAssign, boolean IS_TO_SAVE_TO_DB){
        this.actualItemsGroupMap = hashMapToAssign;
        //
        if (IS_TO_SAVE_TO_DB == true){
            Set<Map.Entry<int[], int[]>> setFromMapToChange  = hashMapToAssign.entrySet();

            for(Map.Entry<int[], int[]> elementSet : setFromMapToChange){
                int[] elementIdNTypeArray = elementSet.getKey();
                int[] elementCoordsArray = elementSet.getValue();

        //System.out.println("Group.java 74: elementIdNTypeArray[0]: " + elementIdNTypeArray[0] + ", elementIdNTypeArray[1]: " + elementIdNTypeArray[1]);
        //System.out.println("Group.java 75: elementCoordsArray X: " + elementCoordsArray[0] + ", elementCoordsArray Y: " + elementCoordsArray[1]);
            konstrElementDao.updateKonstrElementCoords(elementIdNTypeArray[0], elementIdNTypeArray[1], elementCoordsArray);

            }

        }
    }*/





}
