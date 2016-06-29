package com.bazarket.localdbconnect.DAO;

import com.bazarket.localdbconnect.model.Connections.DBConnector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sturlson on 08.06.2016.
 */
public class GroupDao extends KonstrElementDao {
    //private static Connection connection;
    private static Statement statement;
    //private static ResultSet rs;

    private Map<int[],int[]> itemsFromGroupMap = new HashMap<>();




    public GroupDao(Connection connection) {
        super(connection);
    }


    //public GroupDao(){}

    public Map<int[], int[]> returnActualItemsGroupMap(int rootGroupId){
        Map<int[], int[]> actualItemsGroupMap;

        //We need in GroupDao object because at the same time we have to use VOID makeGroupItems (in the reason of recursion), so we need in "itemsFromGroupMap" GroupDAO field
        //THE ALTERNATIVE: to create GroupDAO object in a service method and invoke there a .makeGroupItemsMap method (It can  be even a non static method)
        //GroupDao groupDao = new GroupDao();


        try {

            this.itemsFromGroupMap.clear();
            makeGroupItemsMap(rootGroupId);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        actualItemsGroupMap = this.itemsFromGroupMap;

        return actualItemsGroupMap;
    }



    //We can return Mapfrom this method because here is the Recursion
    private void makeGroupItemsMap(int rootGroupId) throws SQLException {

        itemsFromGroupMap.clear();
        ResultSet rs = null;

        String sqlCommand = "SELECT item_id, item_type FROM c_groups_items where group_id = " + rootGroupId;


        int item_id = -1;
        int item_type = -1;

        try {

            //connection = DBConnector.getConn();
            statement = connection.createStatement();
            //

            rs = statement.executeQuery(sqlCommand);
        } catch (SQLException se) {
            se.printStackTrace();
        }

            while (rs.next()) {
                item_id = rs.getInt("item_id");
                item_type = rs.getInt("item_type");


                if (item_type == 1){

                    int[] groupCoordsArray = getKonstrElementCoords("SELECT * FROM c_groups where id = " + item_id);
                    itemsFromGroupMap.put(new int[]{item_id, item_type}, groupCoordsArray);

    //RECURSION
                makeGroupItemsMap(item_id);
                }
                //
                else {
                    int[] nodeCoordsArray = getKonstrElementCoords("SELECT * FROM c_nodes where id = " + item_id);
                    itemsFromGroupMap.put(new int[]{item_id,item_type}, nodeCoordsArray);
                }
            }
    }

}