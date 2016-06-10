package com.bazarket.localdbconnect.model;

import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Sturlson on 11.04.2016.
 */
public class FindingAllItemsInGroup {

    private MakeConnectionForRS db;
    //private int targetGroupID;
    //private String userEmail;
    //private List<Integer> allItems = new ArrayList<Integer>();


    //Хочу сюда записывать для каждого item_id (key) его XY(value)
    private HashMap<Integer, int[]> oldMapOfXY = new HashMap<>();
    private HashMap<Integer, int[]> newMapOfXY = new HashMap<>();


    public FindingAllItemsInGroup(MakeConnectionForRS db) {
        this.db = db;
        //this.targetGroupID = targetGroupID;
        //this.userEmail = userEmail;
    }

    public HashMap<Integer, int[]> getOldMapOfXY(){

        return oldMapOfXY;
    }

    public HashMap<Integer, int[]> getNewMapOfXY(){

        return newMapOfXY;
    }

    public void clearMapsOfXY(){
        this.oldMapOfXY.clear();
        this.newMapOfXY.clear();
    }

//Можно сделать этот метод более функциональным и сделать опцию сдвинуть все компоненты группы (сдвиг нужно тоже взять)
    //Может не надо для этого делать отдельный класс?

    //Слишком сложный метод, который делает несколько вещей стразу
    // 1) достает все items, входящие в одну группу и их координаты в виде HashMap oldMapOfXY
    // 2) может создать новую мапу newMapOfXY, если в метод передан сдвиг int[focusMove]
    // 3) может записать измененые координаты в Базу (т.е. фактически осуществить сдвиг всех элементов, входящих в группу)


    public void mooveGroupItemsByMap(int rootElementId, HashMap<Integer, int[]> oldMap, HashMap<Integer, int[]> newMap) throws SQLException {

        /*oldMapOfXY = newMap;
        newMapOfXY = oldMap;*/

        ResultSet rs = null;

        /*oldMapOfXY.clear();
        newMapOfXY.clear();*/

        String myQuery = "SELECT item_id, item_type FROM c_groups_items where group_id = ";

        String sqlCommand = myQuery + rootElementId;

        try {
            rs = db.runSql(sqlCommand);

        } catch (SQLException se) {
            se.printStackTrace();
        }
        //item_type может быть кажется либо 0 либо 1, item_id может быть только больше нуля
        int item_id = -1;
        int item_type = -1;

        //Мы пробегаем по всем элементам группы (rootElementId)
        while (rs.next()) {
            item_id = rs.getInt("item_id");
            item_type = rs.getInt("item_type");

            //Old
            //oldMapOfXY.put(item_id, currentXY);

            //Нужно свопануть
            int[] newXYFromOldMap = oldMap.get(item_id);
            //Если происходит вызов этого метода, то по-любому происходит запись пока (нет параметра типа isToSaveToDb)
            new FindingElementFocus(db).setCurrentElementFocus(item_id, item_type, newXYFromOldMap);

            int[] oldXYFromNewMap = newMap.get(item_id);

            System.out.println("item_id: " + item_id + "  newXYFromOldMap: " + Arrays.toString(newXYFromOldMap));
            System.out.println("item_id: " + item_id + "  oldXYFromNewMap: " + Arrays.toString(oldXYFromNewMap));

            //System.out.println("newXYFromOldMap" + Arrays.toString(newXYFromOldMap));
            //System.out.println("oldXYFromNewMap" + Arrays.toString(oldXYFromNewMap));

            //Строчкой ниже пока выходит косяк
            newMapOfXY.put(item_id, newXYFromOldMap);
            oldMapOfXY.put(item_id, oldXYFromNewMap);


            new FindingElementFocus(db).setCurrentElementFocus(item_id, item_type, newXYFromOldMap);


            if (item_type == 0) {

            } else if (item_type == 1) {

                int current_item_id = item_id;


                mooveGroupItemsByMap(current_item_id, oldMap, newMap);
            }
        }
    }

    //public HashMap<Integer, int[]> addAllItems(int rootElementId, int[] focusMove, boolean isToSaveToDB) throws SQLException {

    //Этот метод нужен для обхода всех элементов группы
    //Параметры:
    // rootElementId - группа, для которой нужно составить список элементов
    // focusMove - фокус на который нужно сдвинуть все элементы группы
    // isToSaveToDB - определяется делать ли запись изменения фокуса
    // (вообще конечно, если есть focusMove), то уже есть необходимость делать запись новых координат элементов группы

    public void addAllItems(int rootElementId, int[] focusMove, boolean isToSaveToDB) throws SQLException {

        //Вот здесь мы должны начать использовать NodeDao.getNode() или даже может быть написать KonstruktorElemennDao.getElement()
        ResultSet rs = null;

        String myQuery = "SELECT item_id, item_type FROM c_groups_items where group_id = ";

        String sqlCommand = myQuery + rootElementId;

        try {
            rs = db.runSql(sqlCommand);

        } catch (SQLException se) {
            se.printStackTrace();
        }
            //item_type может быть кажется либо 0 либо 1, item_id может быть только больше нуля
            int item_id = -1;
            int item_type = -1;

            while (rs.next()) {
                item_id = rs.getInt("item_id");
                item_type = rs.getInt("item_type");

                int[] currentXY = new FindingElementFocus(db).getCurrentElementFocus(item_id,item_type);

                //Считываются старые позиции
                oldMapOfXY.put(item_id, currentXY);
                int[] newCurrentXY;

                //Код для записи в базу новых координат
                if (isToSaveToDB == true){
                    newCurrentXY = new int[]{currentXY[0]+focusMove[0], currentXY[1]+focusMove[1]};

                    newMapOfXY.put(item_id, newCurrentXY);

                    //Сделать запись в базу данных новых координа, т.е. засетапдейтить x, y where item_id, item_type
                    new FindingElementFocus(db).setCurrentElementFocus(item_id, item_type, newCurrentXY);

                }
                else {

                }

                // item_type == 1 это группа
                if (item_type == 0){
                }

                else if (item_type == 1) {


                    int current_item_id = item_id;

                    //Вот здесь нужно вызывать FindingFroupFocus или !!!FindingFileFocus
                    // Брать координаты и изменять их

                    addAllItems(current_item_id, focusMove, isToSaveToDB);


                }
            }
        //return allItems;
        //return newMapOfXY;
    }
}

