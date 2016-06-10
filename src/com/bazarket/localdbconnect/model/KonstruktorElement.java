package com.bazarket.localdbconnect.model;

import java.util.ArrayList;

/**
 * Created by Sturlson on 08.06.2016.
 */
public abstract class KonstruktorElement {
    private int id;
    private int coordinateX;
    private int coordinateY;

    public void setCoordinates(int newX, int newY){
        this.coordinateX = newX;
        this.coordinateY = newY;

    }


}
