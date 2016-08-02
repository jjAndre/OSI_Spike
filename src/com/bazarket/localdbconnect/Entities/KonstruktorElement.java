package com.bazarket.localdbconnect.Entities;

//import com.bazarket.localdbconnect.DAO.KonstrElementDao;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * Created by Sturlson on 08.06.2016.
 */
public class KonstruktorElement {
    protected int id;
    //protected int type;

//    private Connection connection;

    //Disabled on July 15, 2016
    protected int[] actualCoordinates;
    protected int[] previousCoordinates;

    //public KonstrElementDao konstrElementDao;

    private static final boolean SAVE_TO_DB_TRUE = true;
    private static final boolean SAVE_TO_DB_FALSE = false;

    private static final int TYPE_GROUP = 1;
    private static final int TYPE_NODE = 0;


    private int coordinateX;
    private int coordinateY;
    private  int[] lastCoordinatesMove;



    //I decide to save the previous as well as the newest coordinates of each Node or Group
    // To save the oldest ones is needed if ww'll decide to turn old Group and its elements back on a previous position


    public KonstruktorElement(int id){
        this.id = id;
    }

    /*public KonstruktorElement(int id, Connection connection){
        this.id = id;
        this.connection = connection;
        this.konstrElementDao = new KonstrElementDao(connection);
    }*/

    /*public Connection getConnection(){
        return this.connection;
    }*/

    public int getId() {
        return this.id;
    }

    public int getType() {
        if (this instanceof Group){
        return TYPE_GROUP;
        }
        else if (this instanceof Node){

        return TYPE_NODE;
        }
        else {return -66666;}

    }

    public void setCoordinateX(int coordX){
        this.coordinateX = coordX;
    }
    public int getCoordinateX(){
        return this.coordinateX;
    }

    public void setCoordinateY(int coordY){
        this.coordinateY = coordY;
    }
    public int getCoordinateY(){
        return this.coordinateY;
    }

    public void setLastCoordinatesMove(int[] lastCoordinatesMove){
        this.lastCoordinatesMove = lastCoordinatesMove;
    }
    public int[] getLastCoordinatesMove(){return this.lastCoordinatesMove;}



    //This method existence sence is take data from DB and not to assign  new coordinates (as an object's field value). in object which is invoking int[] getActualKonstrElementCoords
    /*public int[] receiveKonstrElementCoordsFromDB(int elementId, int elementType){

    int[] justCoords = konstrElementDao.getKonstrElementCoords(elementId, elementType);

    return justCoords;
    }*/


//Disabled on July 15, 2016

    public int[] getActualKonstrElementCoords(){
        return this.actualCoordinates;
    }

    public void setKonstrElementActualCoordinates(int[] coordsXY){

        this.actualCoordinates = coordsXY;
    }


    public int[] getKonstrElementPreviousCoordinates(){
        return previousCoordinates;
    }

    public void setKonstrElementPreviousCoordinates(int[] coordsXY){

        this.previousCoordinates = coordsXY;
    }


//This method is not allowed to be in the Entity class
    /*public void setKonstrElementActualCoordinates(int elementId, int elementType, int[] coordsXY, boolean IS_SAVE_TO_DB){
        this.actualCoordinates = coordsXY;

        if (IS_SAVE_TO_DB == true){
            konstrElementDao.updateKonstrElementCoords(elementId, elementType, coordsXY);
        }

    }*/






//These methods should got to Model layer
    /*public void makeNewActualAndPreviousKonstrElementCoords(int[] konstrElementCoordsFromDAO){
        if (this.actualCoordinates != null){
            this.setKonstrElementPreviousCoordinates(this.getActualKonstrElementCoords());
            //this.actualCoordinates = KonstrElementDao.getKonstrElementCoords(elementId, elementType);
            setKonstrElementActualCoordinates(this.getId(), this.getType(), konstrElementCoordsFromDAO,SAVE_TO_DB_TRUE);

        }
    }

    //We have to save elements (nodes and groups simulteneously with getting back the list of elements' ids and types)
    public void returnPreviousKonstrElementCoords(){
        int[] tempCoordinates = this.actualCoordinates;
        setKonstrElementActualCoordinates(this.getId(), this.getType(), this.getKonstrElementPreviousCoordinates(), SAVE_TO_DB_TRUE);
        setKonstrElementPreviousCoordinates(tempCoordinates);
    }*/






}
