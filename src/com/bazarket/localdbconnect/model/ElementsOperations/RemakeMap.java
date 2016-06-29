package com.bazarket.localdbconnect.model.ElementsOperations;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Sturlson on 26.06.2016.
 */
public class RemakeMap {

    public static HashMap<Integer, int[]> remake(HashMap<int[], int[]> mapToRemake) {

        HashMap<Integer, int[]> remakedMap = new HashMap<>();

        Set<Map.Entry<int[], int[]>> setFromMapToRemake  = mapToRemake.entrySet();

        for(Map.Entry<int[], int[]> elementSet : setFromMapToRemake) {

            int[] elementIdNTypeArray = elementSet.getKey();
        System.out.println("RemakeMap.21.elementIdNTypeArray[0]: " + elementIdNTypeArray[0]);
            int[] elementCoordsArray = elementSet.getValue();
        System.out.println("RemakeMap.23.elementCoordsArray X: " + elementCoordsArray[0] + ", Y: " + elementCoordsArray[1]);
            remakedMap.put(elementIdNTypeArray[0], elementCoordsArray);
        }


    return remakedMap;
    }
}
