package com.bazarket.localdbconnect.Entities;

import com.bazarket.localdbconnect.Entities.Group;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sturlson on 02.07.2016.
 */
// Map<Integer, int[]>(WSimplMapGroup)  <---  Map<int[], int[]> (Group)
public class WSimplMapGroup extends Group {

    private HashMap<Integer, int[]> simpleGroupItemsMap;


    public WSimplMapGroup(int id) {

        super(id);
    }

    public HashMap<Integer, int[]> getSimpleGroupItemsMap(){

        return this.simpleGroupItemsMap;
    }

    public void setSimpleGroupItemsMap(Map<Integer, int[]> simpleGroupItemsMap){
        this.simpleGroupItemsMap = (HashMap<Integer, int[]>) simpleGroupItemsMap;
    }
}
