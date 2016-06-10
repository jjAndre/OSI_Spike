package com.bazarket.localdbconnect.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sturlson on 08.06.2016.
 */
public class KonstrElementDao {
    private static Connection connection;
    private static Statement statement;
    //private static ResultSet rs;

    private static HashMap<Integer,Integer> elementsIdsFromGroup = new HashMap<>();

    public KonstrElementDao(){}

    //public KonstruktorElement getKonstrElement(int elementId, int elementType) throws SQLException{
    public static KonstruktorElement getKonstrElement(int elementId, int elementType, boolean isToTakeGroupIdsList) throws SQLException{


        //String query = "SELECT * FROM c_nodes WHERE id=" + nodeId;
        ResultSet rs = null;

        KonstruktorElement konstrElement = null;



        try {
            /*connection = ConnectionFactory.getConnection();
            statement = connection.createStatement();*/
            //rs = statement.executeQuery(query);

            //...

            if (elementType == 0){

                String sqlCommandToGetXY = "SELECT * FROM c_nodes where id = " + elementId;

                int[] arrXYfromRS = getKonstrElement(sqlCommandToGetXY);

                Node newNode = new Node();
                newNode.setCoordinates(arrXYfromRS[0], arrXYfromRS[1]);

                konstrElement = newNode;

            }
            else if (elementType == 1){

                //getKonstrElement() is static so we're not going to create KonstrElementDao objects and we may be not needed to clear elementsIdsFromGroup map
                elementsIdsFromGroup.clear();

                String sqlCommandToGetXY = "SELECT * FROM c_groups where id = " + elementId;


                int[] arrXYfromRS = getKonstrElement(sqlCommandToGetXY);

                Group newGroup = new Group();
                newGroup.setCoordinates(arrXYfromRS[0], arrXYfromRS[1]);



                //Такое условие, т.к. может быть и не надо будет для каких-то задач получать список подэлементов
                if (isToTakeGroupIdsList){

                    //HashMap<Integer, Integer> mapToGetElements = new HashMap<Integer, Integer>();
                    //Пустую мапу засылаем в метод, надо что-то засылать, т.к в рекурсии потом мы уже будем засылать не пустую мапу при встрече групп

                    //elementsIdsFromGroup = getGroupElementsIdsMap(elementId);
                    makeGroupElementsIdsMap(elementId);
                    //Заполняем мапу

                    // делаем ее свойством сущности группы
                    newGroup.setGroupElementsIdsMap(elementsIdsFromGroup);
                }

                konstrElement = newGroup;
            }
        } finally {
            //Надо закрыть rs, statement, connection
            //DbUtil.close(rs);
            DbUtil.close(statement);
            DbUtil.close(connection);

        }
        return konstrElement;

    }

    private static int[] getKonstrElement(String query){
        int focusX = 0;
        int focusY = 0;
        int[] arrXY = new int[0];
        ResultSet rs = null;

        try {
            //Пробую поставить это сюда
            connection = ConnectionFactory.getConnection();
            statement = connection.createStatement();
            //

            rs = statement.executeQuery(query);

            while (rs.next()) {
                focusX = rs.getInt("x");
                focusY = rs.getInt("y");
            }

            arrXY = new int[]{focusX, focusY};

        } catch (SQLException e){

        }
        /*finally {
            //Надо закрыть rs, statement, connection
            DbUtil.close(rs);
            DbUtil.close(statement);
            DbUtil.close(connection);

        }*/
        return arrXY;

    }


    private static void makeGroupElementsIdsMap(int rootElementId) throws SQLException {
    //private static HashMap<Integer, Integer> getGroupElementsIdsMap(int rootElementId) throws SQLException {
        //ArrayList<Integer> groupElementsIds = new ArrayList<Integer>();


        ResultSet rs = null;
        //rs = null;

        String sqlCommand = "SELECT item_id, item_type FROM c_groups_items where group_id = " + rootElementId;

        //
        System.out.println("sqlCommand in KonstrElementDao 117: " + sqlCommand);

        int item_id = -1;
        int item_type = -1;

        try {

            //Пробую поставить это сюда
            connection = ConnectionFactory.getConnection();
            statement = connection.createStatement();
            //

            rs = statement.executeQuery(sqlCommand);
        } catch (SQLException se) {
            se.printStackTrace();
        } /*finally {
            //Надо бы закрыть rs, statement, connection
            DbUtil.close(rs);
            DbUtil.close(statement);
            DbUtil.close(connection);

        }*/


            while (rs.next()) {
                item_id = rs.getInt("item_id");
                item_type = rs.getInt("item_type");

                System.out.println("item_id: " + item_id + ", item_type: " + item_type);

                elementsIdsFromGroup.put(item_id, item_type);

                if (item_type == 1){

                    makeGroupElementsIdsMap(item_id);
                }

            }
        //DbUtil.close(rs);





        //return elementsIdsFromGroup;
    }
}
