package com.bazarket.localdbconnect.model;


import java.util.HashMap;

/**
 * Created by Sturlson on 08.06.2016.
 */
public class Group extends KonstruktorElement {

    private HashMap<Integer, Integer> elementsMap;

    public void setGroupElementsIdsMap(HashMap<Integer, Integer> elementsMap){
        this.elementsMap = elementsMap;
    }

    public HashMap<Integer, Integer> getElementsMap(){
        return this.elementsMap;
    }
}
