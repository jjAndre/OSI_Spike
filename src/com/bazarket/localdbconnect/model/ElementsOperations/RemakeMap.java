package com.bazarket.localdbconnect.model.ElementsOperations;

import com.bazarket.localdbconnect.Entities.KonstruktorElement;
import com.bazarket.localdbconnect.Entities.WSimplMapGroup;
import com.bazarket.localdbconnect.Entities.Group;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Sturlson on 26.06.2016.
 */
public class RemakeMap {
    private final static int CHANGE_GROUP_ACTUAL_ITEMS = 1;
    private final static int CHANGE_GROUP_PREVIOUS_ITEMS = 2;


    public static WSimplMapGroup returnSimplifiedTransObjectGroup(Group transObjectGroup){
    //public static WSimplMapGroup returnSimplifiedTransObjectGroup(Group transObjectGroup, int WHAT_GROUP_ITEMS_TO_CHANGE, Connection connection){



    //makeWIntsMapFromGroupItemsSet returns HashMap<Integer, int[]>

        HashMap<Integer, int[]> tempHashMap =  makeWIntsMapFromGroupItemsSet(transObjectGroup.getGroupActualItemsSet());

        WSimplMapGroup transObjectSimpleGroup = new WSimplMapGroup(transObjectGroup.getId());



        transObjectSimpleGroup.setSimpleGroupItemsMap(tempHashMap);



        /*if (WHAT_GROUP_ITEMS_TO_CHANGE == CHANGE_GROUP_ACTUAL_ITEMS){

            transObjectSimpleGroup.setSimpleGroupItemsMap(remake(transObjectGroup.getActualItemsGroupMap()));
        }
        else if (WHAT_GROUP_ITEMS_TO_CHANGE == CHANGE_GROUP_PREVIOUS_ITEMS){

            transObjectSimpleGroup.setSimpleGroupItemsMap(remake(transObjectGroup.getPreviousItemsGroupMap()));
        }*/

        //What we are doing here?
        //We are taking Map, 31087 is an Id of Node is included in its turn to 3404 group, file 218
     //System.out.println("Remake.transObjectSimpleGroup.getSimpleGroupItemsMap(). ONLY FOR GROUP 3404 " + transObjectSimpleGroup.getSimpleGroupItemsMap().get(31087)[0]);

        return transObjectSimpleGroup;

    }





    //I want to return simplified Group with .ActualItemsGroupMap(Integer, int[])
    private static HashMap<Integer, int[]> makeWIntsMapFromGroupItemsSet (Set<KonstruktorElement> groupItems){

        HashMap<Integer, int[]> wIntsMap = new HashMap<>();

        for (KonstruktorElement s : groupItems){
            //int[] tempInt = new int[0];

            wIntsMap.put(s.getId(), new int[]{s.getCoordinateX(), s.getCoordinateY()});

        }


        return wIntsMap;
    }




    private static HashMap<Integer, int[]> remake(Map<int[], int[]> mapToRemake) {

        HashMap<Integer, int[]> remakedMap = new HashMap<>();

        Set<Map.Entry<int[], int[]>> setFromMapToRemake  = mapToRemake.entrySet();

        for(Map.Entry<int[], int[]> elementSet : setFromMapToRemake) {

            int[] elementIdNTypeArray = elementSet.getKey();
                //System.out.println("RemakeMap.21.elementIdNTypeArray[0]: " + elementIdNTypeArray[0]);
            int[] elementCoordsArray = elementSet.getValue();
                //System.out.println("RemakeMap.23.elementCoordsArray X: " + elementCoordsArray[0] + ", Y: " + elementCoordsArray[1]);
            remakedMap.put(elementIdNTypeArray[0], elementCoordsArray);
        }


    return remakedMap;
    }
}
