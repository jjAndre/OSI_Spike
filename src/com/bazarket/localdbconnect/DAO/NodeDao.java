package com.bazarket.localdbconnect.DAO;

//import com.bazarket.localdbconnect.Entities.Node;
import com.bazarket.localdbconnect.model.Connections.DBConnector;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Sturlson on 08.06.2016.
 */
public class NodeDao extends KonstrElementDao {
    private static Connection connection;
    private static Statement statement;

    public NodeDao(Connection connection){
        super(connection);
    }


/*    public static Node getNode(int elementId) throws SQLException{


        Node node = null;



        try {
            connection = DBConnector.getConn();
            statement = connection.createStatement();
            //rs = statement.executeQuery(query);

            //...



            Node newNode = new Node();

            *//*String sqlCommandToGetXY = "SELECT * FROM c_nodes where id = " + elementId;

                int[] arrXYfromRS = getKonstrElementCoords(sqlCommandToGetXY);


                newNode.setCoordinates(arrXYfromRS[0], arrXYfromRS[1]);
*//*
            node = newNode;



        } finally {
            //DbUtil.close(rs);

            //We should have to close connection (and may be statement when ServletContext is destroyed)
            *//*DbUtil.close(statement);
            DbUtil.close(connection);*//*

        }
        return node;

    }*/
}
