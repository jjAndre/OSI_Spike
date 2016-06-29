package com.bazarket.localdbconnect.Entities;

import com.bazarket.localdbconnect.DAO.KonstrElementDao;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * Created by Sturlson on 08.06.2016.
 */
public class KonstruktorElement {
    protected int id;
    protected int type;

    private Connection connection;

    protected int[] actualCoordinates;
    protected int[] previousCoordinates;

    public KonstrElementDao konstrElementDao;

    private static final boolean SAVE_TO_DB_TRUE = true;
    private static final boolean SAVE_TO_DB_FALSE = false;

    //I decide to save the previous as well as the newest coordinates of each Node or Group
    // To save the oldest ones is needed if ww'll decide to turn old Group and its elements back on a previous position


    public KonstruktorElement(int id, Connection connection){
        this.id = id;
        this.connection = connection;
        this.konstrElementDao = new KonstrElementDao(connection);
    }

    public int getId() {
        return this.id;
    }

    public int getType() {
        return type;
    }

    public Connection getConnection(){
        return this.connection;
    }





    //This method existence sence is take data from DB and not to assign  new coordinates (as an object's field value). in object which is invoking int[] getActualKonstrElementCoords
    public int[] receiveKonstrElementCoordsFromDB(int elementId, int elementType){

    int[] justCoords = konstrElementDao.getKonstrElementCoords(elementId, elementType);

    return justCoords;
    }

    public int[] getActualKonstrElementCoords(){
        return this.actualCoordinates;
    }


    public int[] getKonstrElementPreviousCoordinates(){
        return previousCoordinates;
    }


    public void setKonstrElementActualCoordinates(int elementId, int elementType, int[] coordsXY, boolean IS_SAVE_TO_DB){
        this.actualCoordinates = coordsXY;
//Here should be done a save to a Database
        if (IS_SAVE_TO_DB == true){
            konstrElementDao.updateKonstrElementCoords(elementId, elementType, coordsXY);
        }

    }


    public void setKonstrElementPreviousCoordinates(int[] cordsXY){

        this.previousCoordinates = cordsXY;
    }

    public void makeNewActualAndPreviousKonstrElementCoords(int[] konstrElementCoordsFromDAO){
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
    }






}
