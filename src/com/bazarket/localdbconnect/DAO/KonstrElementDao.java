package com.bazarket.localdbconnect.DAO;

import com.bazarket.localdbconnect.Entities.KonstruktorElement;
import com.bazarket.localdbconnect.model.Connections.DBConnector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Sturlson on 08.06.2016.
 */
public class KonstrElementDao {


    //protected static Connection connection;
    protected static Connection connection;
    //protected static Connection connection = DBConnector.getConn();;
//    private static Statement statement = connection.createStatement();
    private Statement statement;

    private final static int GROUP_TYPE = 1;
    private final static int NODE_TYPE = 0;
    private final static int ACTUAL_COORDINATES = 1;
    private final static int PREVIOUS_COORDINATES = 2;




    public KonstrElementDao(Connection connection){
        this.connection = connection;
    }


    //methods from GroupDao and NodeDao invoke getKonstrElementCoords(String query)
    //Here is a query instead of elementId because queries for groups and nodes are differ


//May be to make it protected (users should use KonstruktorElement.methods?)
    public KonstruktorElement getFromDatabaseAndSetNewKonstrElementActualCoords(KonstruktorElement konstruktorElement){

        int[] elementCoordsXY = new int[0];
        int elementId = konstruktorElement.getId();
        int elementType = konstruktorElement.getType();

        System.out.println("KonstrElementDAO: 47: elementType " + elementType);

        if (elementType == GROUP_TYPE){
            elementCoordsXY = getKonstrElementCoords("SELECT * FROM c_groups where id = " + elementId);
        }
        else if (elementType == NODE_TYPE){
            elementCoordsXY = getKonstrElementCoords("SELECT * FROM c_nodes where id = " + elementId);
        }

        System.out.println("KonstrElementDAO: 54: elementId " + elementId);
        System.out.println("KonstrElementDAO: 55: Element X " + elementCoordsXY[0]);
        System.out.println("KonstrElementDAO: 56: Element Y " + elementCoordsXY[1]);

        //Q1: why where are used setKonstrElementActualCoordinates and setKonstrElementPreviousCoordinates?
        //A1: I've just taken these coordinates from DB, so I have to return KonstrElementObject, where actual and previous coordinates can differ from just recieved ones
    //    if (WHAT_COORDINATATES_TO_GET == ACTUAL_COORDINATES){
            //Did I (and should I) updated coords in DB?

    konstruktorElement.setKonstrElementActualCoordinates(elementCoordsXY);

    /*if (IS_SAVE_TO_DB == true) {
        updateKonstrElementCoords(elementId, elementType, elementCoordsXY);
    }*/
    //    }
//Q2: However should I here do invoke setKonstrElementPreviousCoordinates?
//Did I recieve new coordinates from DB? (I suppose I shouldn't recieve them)
        /*else if (WHAT_COORDINATATES_TO_GET == PREVIOUS_COORDINATES){
            konstruktorElement.setKonstrElementPreviousCoordinates(elementCoordsXY);
        }*/



    return konstruktorElement;
    }


    //GroupDAO and NodeDAO use method getKonstrElementCoords(String query)
    // int[] is a 2-dimensial array {focusX, focusY}
    protected int[] getKonstrElementCoords(String query){
        int focusX = 0;
        int focusY = 0;
        int[] elementCoordsXY = new int[0];
        ResultSet rs = null;

        try {
//            connection = DBConnector.getConn();

            statement = connection.createStatement();
            //

            rs = statement.executeQuery(query);

            while (rs.next()) {
                focusX = rs.getInt("x");
        System.out.println("KonstrElementDao.getKonstrElementCoords: 101: FocusX " + focusX);
                focusY = rs.getInt("y");
        System.out.println("KonstrElementDao.getKonstrElementCoords: 101: FocusY " + focusY);
            }

            elementCoordsXY = new int[]{focusX, focusY};

        } catch (SQLException e){

        }
        return elementCoordsXY;

    }

    // I'm thinking about taking out coordsXY as a parameter from this method


//Commented on July 28, 2016.
    public KonstruktorElement setKonstrElementActualCoordinates(KonstruktorElement konstruktorElement, boolean IS_SAVE_TO_DB){
//    public KonstruktorElement setKonstrElementActualCoordinates(KonstruktorElement konstruktorElement, int[] coordsXY, boolean IS_SAVE_TO_DB){

//        konstruktorElement.setKonstrElementActualCoordinates(coordsXY);
        int[] coordsXY = konstruktorElement.getActualKonstrElementCoords();

        if (IS_SAVE_TO_DB == true){
            updateKonstrElementCoords(konstruktorElement.getId(), konstruktorElement.getType(), coordsXY);
        }

        return konstruktorElement;

    }



//May be to make it protected (users should use KonstruktorElement.methods?)
    public void updateKonstrElementCoords(int elementId, int elementType, int[] rootKonstrElementNewCoords){


        String qToSetXY1 = null;

        if (elementType == GROUP_TYPE) {
            //qToSetXY = "UPDATE c_groups set x = -783, y = -799 where id = 3345";
            qToSetXY1 = "UPDATE c_groups set x = ";
        }

        else if (elementType == NODE_TYPE){
            qToSetXY1 = "UPDATE c_nodes set x = ";
        }

        String qToSetXY2 = ", y = ";
        String qToSetXY3 = " where id = ";
        String qToSetXY = qToSetXY1 + rootKonstrElementNewCoords[0] + qToSetXY2 + rootKonstrElementNewCoords[1] + qToSetXY3 + elementId;
    System.out.println("KonstrElementDao: 150: Command to update element coords" + qToSetXY);



        updateKonstrElementCoords(qToSetXY);

        System.out.println("KonstrElementDAO.java 95: qToSetXY:  " + qToSetXY);

    }


    protected void updateKonstrElementCoords(String query) {



        try {
//          connection = DBConnector.getConn();

            statement = connection.createStatement();

            statement.executeUpdate(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    }
