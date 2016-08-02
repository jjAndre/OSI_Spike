package com.bazarket.localdbconnect.DAO;

import com.bazarket.localdbconnect.Entities.Group;
import com.bazarket.localdbconnect.Entities.KonstruktorElement;
import com.bazarket.localdbconnect.Entities.Node;
import com.bazarket.localdbconnect.model.Connections.DBConnector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Sturlson on 08.06.2016.
 */
public class GroupDao extends KonstrElementDao {
    //The lines below is inherited from KonstrElementDao (...executed in super(connection))
    //private static Connection connection;
    private static Statement statement;

    //private static ResultSet rs;

    private final static boolean INCLUDING_ITEMS_YES = true;
    private final static boolean INCLUDING_ITEMS_NO = false;
    private final static int TYPE_GROUP = 1;
    private final static int TYPE_NODE = 0;

    //private HashMap<int[],int[]> itemsFromGroupMap = new HashMap<>();
    private HashSet<KonstruktorElement> itemsFromGroupSet;




    public GroupDao(Connection connection) {
        super(connection);
        //The line below is executed in super(connection)
        //this.connection = connection;
    }


    //Here we are filling up a group with it's coordinates (and with creating this group items set)
public Group getByIdFilledUpGroup(Group newGroup, boolean INCLUDING_ITEMS){
        //Group newGroup = new Group(groupId);
        int groupId = newGroup.getId();
    itemsFromGroupSet = new HashSet<>();

   System.out.println("GroupDao.getByField:48: groupId: " + groupId);


    //KonstrElementDao.getKonstrElementCoords()...
    //SQL has to execute in this method...
    int[] groupCoordsArray = getKonstrElementCoords("SELECT * FROM c_groups where id = " + groupId);

        newGroup.setCoordinateX(groupCoordsArray[0]);
        newGroup.setCoordinateY(groupCoordsArray[1]);

        //To make or not group Items set
        if (INCLUDING_ITEMS == INCLUDING_ITEMS_YES){
            try {

    makeGroupItemsSet(groupId);

            } catch (SQLException e) {
                e.printStackTrace();
            }
            //Set<KonstruktorElement> konstruktorElements = new HashSet<>();

            newGroup.setGroupActualItemsSet(itemsFromGroupSet);


        }
        else if(INCLUDING_ITEMS == INCLUDING_ITEMS_NO) {

        }

    return newGroup;
    }








    //We can return Mapfrom this method because here is the Recursion
    private void makeGroupItemsSet(int rootGroupId) throws SQLException {

        //Do we really need to clear the set here? We've already cleared it early and this method includes a recursion - we have to fill up itemsFromGroupSet with data
        //itemsFromGroupSet.clear();
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


                if (item_type == TYPE_GROUP){

        int[] groupCoordsArray = getKonstrElementCoords("SELECT * FROM c_groups where id = " + item_id);

                    Group tempGroup = new Group(item_id);
                    tempGroup.setCoordinateX(groupCoordsArray[0]);
                    tempGroup.setCoordinateY(groupCoordsArray[1]);
                itemsFromGroupSet.add(tempGroup);

                    //itemsFromGroupMap.put(new int[]{item_id, item_type}, groupCoordsArray);

    //RECURSION
            makeGroupItemsSet(item_id);
                }
                // TYPE_NODE
                else if(item_type == TYPE_NODE) {
                    int[] nodeCoordsArray = getKonstrElementCoords("SELECT * FROM c_nodes where id = " + item_id);
                    Node tempNode = new Node(item_id);
                    tempNode.setCoordinateX(nodeCoordsArray[0]);
                    tempNode.setCoordinateY(nodeCoordsArray[1]);

                itemsFromGroupSet.add(tempNode);

                    //itemsFromGroupMap.put(new int[]{item_id,item_type}, nodeCoordsArray);
                }
                //System.out.println("GroupDao:143: itemsFromGroupSet.size() " + itemsFromGroupSet.size());
            }
    }

    public void saveGroupActualItemsCoordsToDB(Group transObjectGroup){
    //private void setActualItemsGroupMap(Map<int[],int[]> hashMapToAssign, boolean IS_TO_SAVE_TO_DB){

        HashSet<KonstruktorElement> tempHashSet = transObjectGroup.getGroupActualItemsSet();

        //System.out.println("GroupDao.saveGroupActualItemsCoordsToDB: 153: tempHashSet.size()" + tempHashSet.size());

            for(KonstruktorElement s : tempHashSet){
                /*int[] elementIdNTypeArray = elementSet.getKey();
                int[] elementCoordsArray = elementSet.getValue();*/

        //System.out.println("Group.java 74: elementIdNTypeArray[0]: " + elementIdNTypeArray[0] + ", elementIdNTypeArray[1]: " + elementIdNTypeArray[1]);
        //System.out.println("Group.java 75: elementCoordsArray X: " + elementCoordsArray[0] + ", elementCoordsArray Y: " + elementCoordsArray[1]);

    //KonstrElementDao.updateKonstrElementCoords(...)

                //System.out.println("GroupDao: 162: s.getId() " + s.getId() + " s.getType() " + s.getType() + " s.getActualKonstrElementCoords(): X: " + s.getActualKonstrElementCoords()[0] + " Y: " + s.getActualKonstrElementCoords()[1] );
                /*System.out.println("GroupDao: 162: s.getId() " + s.getId());
                System.out.println("GroupDao: 162: s.getType() " + s.getType());
                System.out.println("GroupDao: 162: s.getCoordinateX(): X: " + s.getCoordinateX());
                System.out.println("GroupDao: 162: s.s.getCoordinateY(): Y: " + s.getCoordinateY() );*/
    updateKonstrElementCoords(s.getId(), s.getType(), new int[]{s.getCoordinateX(), s.getCoordinateY()});

            }


    }



//Was disabled on July 16, 2016
    //It was placed in Group.java before
    /*public void saveGroupActualItemsMapToDB(Group transObjectGroup){
    //private void setActualItemsGroupMap(Map<int[],int[]> hashMapToAssign, boolean IS_TO_SAVE_TO_DB){

        HashMap<int[], int[]> tempHashMap = transObjectGroup.getActualItemsGroupMap();

        //

            Set<Map.Entry<int[], int[]>> setFromMapToChange  = tempHashMap.entrySet();

            for(Map.Entry<int[], int[]> elementSet : setFromMapToChange){
                int[] elementIdNTypeArray = elementSet.getKey();
                int[] elementCoordsArray = elementSet.getValue();

        //System.out.println("Group.java 74: elementIdNTypeArray[0]: " + elementIdNTypeArray[0] + ", elementIdNTypeArray[1]: " + elementIdNTypeArray[1]);
        //System.out.println("Group.java 75: elementCoordsArray X: " + elementCoordsArray[0] + ", elementCoordsArray Y: " + elementCoordsArray[1]);
            updateKonstrElementCoords(elementIdNTypeArray[0], elementIdNTypeArray[1], elementCoordsArray);

            }


    }*/

    //Was disabled on July 14, 2016
    /*private Group returnActualItemsGroupMap(Group group){
        //HashMap<int[], int[]> actualItemsGroupMap;

        //We need in GroupDao object because at the same time we have to use VOID makeGroupItems (in the reason of recursion), so we need in "itemsFromGroupMap" GroupDAO field
        //THE ALTERNATIVE: to create GroupDAO object in a service method and invoke there a .makeGroupItemsMap method (It can  be even a non static method)
        //GroupDao groupDao = new GroupDao();


        try {

            //this.itemsFromGroupMap.clear();
            itemsFromGroupMap.clear();

    makeGroupItemsMap(group.getId());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        group.setActualItemsGroupMap(itemsFromGroupMap);

        return group;
    }*/

    /*//We can return Mapfrom this method because here is the Recursion
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
    }*/

}