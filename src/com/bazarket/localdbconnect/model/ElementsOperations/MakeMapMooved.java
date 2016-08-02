package com.bazarket.localdbconnect.model.ElementsOperations;

import com.bazarket.localdbconnect.DAO.GroupDao;
import com.bazarket.localdbconnect.DAO.KonstrElementDao;
import com.bazarket.localdbconnect.Entities.Group;
import com.bazarket.localdbconnect.Entities.KonstruktorElement;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
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

    /*private final static int TYPE_GROUP = 1;
    private final static boolean GET_DATA_FROM_DB_TRUE = true;
    private final static boolean GET_DATA_FROM_DB_FALSE = false;
    */

    private final static boolean INCLUDING_ITEMS_YES = true;
    private final static boolean SAVE_TO_DB_YES = true;

    private static GroupDao groupDao;


// method .moveMapByChangesInCoords(...) is used to move items' coords
// if method is used then items' actual coords ARE RESAVED
// But if we want to move map but not want to resave (just get new coordinates - for example the oldest ones to show in jsp)
// we should'off use the alternative to .getFromDatabaseSetAndSaveNewKonstrElementActualCoords
// or this method shoul have a parameter - to_save_or_not_to_SAVE
//


    //IS_SAVE_TO_DB = false in case we want to get Group with remaked coordinates, but don't want to save them to DB
    public static Group moveMapByChangesInCoords(Group transObjectGroup, int[] handMadeCoordsMove, Connection connection, boolean IS_SAVE_TO_DB){
        //why do we use int[] handMadeCoordsMoove as a parameter, because it can be assign in HTML form (or taken as the difference between RootGroup actual and previous coords)
        //We have 2 types of coordinates - 1) of RootGroup (Group as Entity) and 2) this group's Itemes (they are Groups and Nodes, KonstrElement type)

        groupDao = new GroupDao(connection);
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
            //We can't save last moovement before we take new coordinates and comapare them with the oldest ones
            //We need check if actual coordinates saved in object are the same as coordinates in DB.
            //Actually this if block is already this case

        transObjectGroup.setKonstrElementPreviousCoordinates(transObjectGroup.getActualKonstrElementCoords());

    //
    transObjectGroup = (Group) groupDao.getFromDatabaseAndSetNewKonstrElementActualCoords(transObjectGroup);
                //I guess this method should have an option to save to DB or not
                //Here we chouldn't save, but only get actual coords from DB
           //Because we can call moveMapByChangesInCoords from DBAccessLocForSmth only for... ???
                //transObjectGroup.receiveKonstrElementCoordsFromDB(groupId, TYPE_GROUP);
            }
            //for both 2 cases (rootGroupPreviousCoords == null or != null)
            changeInRootGroupCoords = new int[]{rootGroupActualCoords[0] - rootGroupPreviousCoords[0], rootGroupActualCoords[1] - rootGroupPreviousCoords[1]};
            //System.out.println("X: " + changeInRootGroupCoords[0] + ", Y: " + changeInRootGroupCoords[1]);


        }
        //CASE2: Coordinates ARE assigned in HTML(JSP) form

        else {
        //else if (mooveX != 0 && mooveY != 0){
            changeInRootGroupCoords = handMadeCoordsMove;
        }


//Should we set these coords as a lastMove?
//LastMove should be setted after making an update to DB
//transObjectGroup.setLastCoordinatesMove(changeInRootGroupCoords);


            //We want to take Items Map of our Group (transObjectGroup) --> Items Set
            //HashMap<int[], int[]> newActualGroupItems = groupDao.returnActualItemsGroupMap(transObjectGroup).getActualItemsGroupMap();
          transObjectGroup = groupDao.getByIdFilledUpGroup(transObjectGroup, INCLUDING_ITEMS_YES);
            //transObjectGroup = groupDao.returnActualItemsGroupMap(transObjectGroup);


            //HashMap<int[], int[]> newActualGroupItems = (HashMap<int[], int[]>) transObjectGroup.receiveActualItemsGroupMapFromDB();
            //System.out.println("MakeMApMooved 84. actualGroupItems.size())" + newActualGroupItems.size());


            //We are changing
        transObjectGroup = changeCoordsOfGroupItemsSet(transObjectGroup, changeInRootGroupCoords);

//CHECKING July 29, 2016
        /*HashSet<KonstruktorElement> checkHashSet = transObjectGroup.getGroupActualItemsSet();

        for (KonstruktorElement k : checkHashSet) {
            System.out.println("MakeMapMooved: 128: Id: " + k.getId());
            System.out.println("MakeMapMooved: 129: X: " + k.getCoordinateX());
            System.out.println("MakeMapMooved: 130: Y: " + k.getCoordinateY());
        }*/
//
                //Then we want to change Group's Items Map and returng the map changed --> Change Group's Items Set (modify coords of each <KonstrElements>)
            //HashMap<int[], int[]> modifiedActualGroupItems = changeCoordsInMap(transObjectGroup.getActualItemsGroupMap(), changeInRootGroupCoords, connection);
                //System.out.println("MakeMApMooved 88. actualGroupItems.size())" + actualGroupItems.size());


    //    transObjectGroup.setPreviousItemsGroupMap(transObjectGroup.getActualItemsGroupMap());
            //transObjectGroup.setActualItemsGroupMap(modifiedActualGroupItems);

        if (SAVE_TO_DB_YES) {
            groupDao.saveGroupActualItemsCoordsToDB(transObjectGroup);
            //transObjectGroup.setActualItemsGroupMap(actualGroupItems, SAVE_TO_DB);
            transObjectGroup.setLastCoordinatesMove(changeInRootGroupCoords);
        }


        //current coordinates mus be set as previous (after creating the new ones mooving by HTML mooves in X,Y)


        return transObjectGroup;
    }


    private static Group changeCoordsOfGroupItemsSet(Group group, int[] changeInRootGroupCoords){
    //private static HashSet<KonstruktorElement> changeCoordsOfGroupItemsSet(Set<KonstruktorElement> setToChange, int[] changeInRootGroupCoords){

        HashSet<KonstruktorElement> changedCoordsSet = new HashSet<>();
        HashSet<KonstruktorElement> setToChange = group.getGroupActualItemsSet();

 //Here we can use only change in coords equals to last moove
 //Actually it may not even exist
        //int[] coordsMoove = group.getLastCoordinatesMove();
        int[] coordsMoove = changeInRootGroupCoords;
        System.out.println("MakeMapMooved: 156: X: " + coordsMoove[0] + ", Y: " + coordsMoove[1]);

        for (KonstruktorElement s : setToChange) {
            s.setCoordinateX(s.getCoordinateX() + coordsMoove[0]);
            s.setCoordinateY(s.getCoordinateY() + coordsMoove[1]);
            changedCoordsSet.add(s);
        }

        group.setGroupActualItemsSet(changedCoordsSet);

    //We need to save new coordinates to DB. Where we should better to do this action???
    //Especially in case, if we refuse from previousActualItemsMap

    return group;
    }


//Disabled on July 15, 2016
    /*private static HashMap<int[], int[]> changeCoordsInMap(HashMap<int[], int[]> mapToChange, int[] changeInRootGroupCoords, Connection connection){

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
    }*/
}
