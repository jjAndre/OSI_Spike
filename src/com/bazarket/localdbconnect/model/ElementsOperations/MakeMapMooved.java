package com.bazarket.localdbconnect.model.ElementsOperations;

import com.bazarket.localdbconnect.DAO.KonstrElementDao;
import com.bazarket.localdbconnect.Entities.Group;
import com.bazarket.localdbconnect.Entities.KonstruktorElement;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Sturlson on 22.06.2016.
 */
//We need save changed Coords after mooving them

//In this method we can define right moove (Actual - Previous of RootGroup Or made in forms)
//Then we should fix Actual (and ??make new Previous from old Actual??) coordinates, save new Actual Coordinates to DB
//We can use Gorup.setActualItemsGroupMap(HashMap<>) to save to DB
//After that we can return transObjectGroup to DbAccesLocForSmth

public class MakeMapMooved {
    private static int groupId;
    //private static HashMap<int[], int[]> actualGroupItems;

    //I can't do in moveMapByChangesInCoords smth like  this.transObjectGroup = transObjectGroup; because method is static
    //private static Group transObjectGroup;

    private final static int TYPE_GROUP = 1;
    private final static boolean GET_DATA_FROM_DB_TRUE = true;
    private final static boolean GET_DATA_FROM_DB_FALSE = false;
    private final static boolean SAVE_TO_DB = true;



//why use int[] handMadeCoordsMoove as a parameter because of it can be assign in HTML form (or taken as the difference between RootGroup actual and previous coords)
//We have 2 types of coordinates - 1) of RootGroup (Group as Entity) and 2) this group's Itemes (they are Groups and Nodes, KonstrElement type)


    public static Group moveMapByChangesInCoords(Group transObjectGroup, int[] handMadeCoordsMove){
        //we also have to create Actual and Previous RootGroup's Items maps

        //System.out.println("MakeMapMooved.java. transObjectGroup.getId(): " + transObjectGroup.getId());


        groupId = transObjectGroup.getId();

        int mooveX = handMadeCoordsMove[0];
        //System.out.println("mooveX: " + mooveX);
        int mooveY = handMadeCoordsMove[1];
        //System.out.println("mooveY: " + mooveX);

        int[] changeInRootGroupCoords = new int[0];

        int[] rootGroupActualCoords = transObjectGroup.getActualKonstrElementCoords();
        //System.out.println("transObjectGroup.getActualKonstrElementCoords(): X: " + rootGroupActualCoords[0] + ", Y: " + rootGroupActualCoords[1]);
        int[] rootGroupPreviousCoords = transObjectGroup.getKonstrElementPreviousCoordinates();

        //CASE1: Coordinates are NOT assigned in HTML(JSP) form
        if (mooveX == 0 && mooveY == 0) {

            if (rootGroupPreviousCoords == null){
                transObjectGroup.setKonstrElementPreviousCoordinates(transObjectGroup.getActualKonstrElementCoords());

                transObjectGroup.receiveKonstrElementCoordsFromDB(groupId, TYPE_GROUP);
            }
            //for both 2 cases (rootGroupPreviousCoords == null or != null)
            changeInRootGroupCoords = new int[]{rootGroupActualCoords[0] - rootGroupPreviousCoords[0], rootGroupActualCoords[1] - rootGroupPreviousCoords[1]};
            System.out.println("X: " + changeInRootGroupCoords[0] + ", Y: " + changeInRootGroupCoords[1]);

        }
        //CASE2: Coordinates ARE assigned in HTML(JSP) form

        else {
        //else if (mooveX != 0 && mooveY != 0){
            changeInRootGroupCoords = handMadeCoordsMove;
        }
        /*else {
            System.out.println("Smth strange has happened in MakeMApMooved 77");
            System.out.println("mooveX: " + mooveX);
            System.out.println("mooveY: " + mooveY);
        }*/

        //We want to take Items Map of our Group (transObjectGroup)
        HashMap<int[], int[]> newActualGroupItems = (HashMap<int[], int[]>) transObjectGroup.receiveActualItemsGroupMapFromDB();
        //System.out.println("MakeMApMooved 84. actualGroupItems.size())" + newActualGroupItems.size());

        //When we want to change this map
        HashMap<int[], int[]> actualGroupItems = changeCoordsInMap(newActualGroupItems, changeInRootGroupCoords, transObjectGroup.getConnection());
        //System.out.println("MakeMApMooved 88. actualGroupItems.size())" + actualGroupItems.size());

        transObjectGroup.setActualItemsGroupMap(actualGroupItems, SAVE_TO_DB);
        //current coordinates mus be set as previous (after creating the new ones mooving by HTML mooves in X,Y)
        transObjectGroup.setPreviousItemsGroupMap(newActualGroupItems);

        return transObjectGroup;
    }

    public static HashMap<int[], int[]> changeCoordsInMap(HashMap<int[], int[]> mapToChange, int[] changeInRootGroupCoords, Connection connection){

        //The line below (if use instead of code block below) causes java.util.ConcurrentModificationException
        //HashMap<int[], int[]> changedCoordsMap = mapToChange;
        HashMap<int[], int[]> changedCoordsMap = new HashMap<>();
        Set<Map.Entry<int[], int[]>> setFromMapToChange  = mapToChange.entrySet();

        for(Map.Entry<int[], int[]> elementSet : setFromMapToChange) {

            int[] elementIdNTypeArray = elementSet.getKey();
            int[] elementCoordsArray = elementSet.getValue();
        changedCoordsMap.put(elementIdNTypeArray, elementCoordsArray);
        }

        //Set<Map.Entry<int[], int[]>> setFromMapToChange  = mapToChange.entrySet();

        for(Map.Entry<int[], int[]> elementSet : setFromMapToChange){

            int[] elementIdNTypeArray = elementSet.getKey();
            int[] elementCoordsArray = elementSet.getValue();
            int[] changedElementCoordsArray = new int[]{elementCoordsArray[0] + changeInRootGroupCoords[0], elementCoordsArray[1] + changeInRootGroupCoords[1]};

        changedCoordsMap.remove(elementIdNTypeArray);
        changedCoordsMap.put(elementIdNTypeArray, changedElementCoordsArray);

//Here we have to save to DB (using GroupDAO methods)


        KonstrElementDao konstrElementDao = new KonstrElementDao(connection);
        konstrElementDao.updateKonstrElementCoords(elementIdNTypeArray[0],elementIdNTypeArray[1], changedElementCoordsArray);

        }

        return changedCoordsMap;
    }
}
