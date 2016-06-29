package com.bazarket.localdbconnect.DAO;

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
    private static Statement statement;

    private final static int GROUP_TYPE = 1;
    private final static int NODE_TYPE = 0;



    public KonstrElementDao(Connection connection){
        this.connection = connection;
    }


    //methods from GroupDao and NodeDao invoke getKonstrElementCoords(String query)
    //Here is a query instead of elementId because queries for groups and nodes are differ


//May be to make it protected (users should use KonstruktorElement.methods?)
    public int[] getKonstrElementCoords(int elementId, int elementType){

        int[] elementCoordsXY = new int[0];

        if (elementType == GROUP_TYPE){
            elementCoordsXY = getKonstrElementCoords("SELECT * FROM c_groups where id = " + elementId);
        }
        else if (elementType == NODE_TYPE){
            elementCoordsXY = getKonstrElementCoords("SELECT * FROM c_nodes where id = " + elementId);
        }

    return elementCoordsXY;
    }


    //GroupDAO and NodeDAO use method getKonstrElementCoords(String query)
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
                focusY = rs.getInt("y");
            }

            elementCoordsXY = new int[]{focusX, focusY};

        } catch (SQLException e){

        }
        return elementCoordsXY;

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
