package com.bazarket.localdbconnect.model.ElementsOperations;

import com.bazarket.localdbconnect.Entities.Group;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sturlson on 20.06.2016.
 */
public class ReturnPreviousItems {
    //Group transferGroup;

    private final static boolean IS_TO_GET_FROM_DB = false;
    private final static boolean SAVE_TO_DB = true;

    public static Group returnPreviousAllGroupItemsCoords(Group transferGroupObject){
        Map<int[], int[]> tempMap;

        tempMap = transferGroupObject.getActualItemsGroupMap();
        transferGroupObject.setActualItemsGroupMap(transferGroupObject.getPreviousItemsGroupMap(), SAVE_TO_DB);
        transferGroupObject.setPreviousItemsGroupMap(tempMap);

    //We also need to save new coordinates to Database





    return transferGroupObject;
    }



}
