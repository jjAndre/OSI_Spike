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


    //���� ���� ���������� ��� ������� item_id (key) ��� XY(value)
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

//����� ������� ���� ����� ����� �������������� � ������� ����� �������� ��� ���������� ������ (����� ����� ���� �����)
    //����� �� ���� ��� ����� ������ ��������� �����?

    //������� ������� �����, ������� ������ ��������� ����� ������
    // 1) ������� ��� items, �������� � ���� ������ � �� ���������� � ���� HashMap oldMapOfXY
    // 2) ����� ������� ����� ���� newMapOfXY, ���� � ����� ������� ����� int[focusMove]
    // 3) ����� �������� ��������� ���������� � ���� (�.�. ���������� ����������� ����� ���� ���������, �������� � ������)


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
        //item_type ����� ���� ������� ���� 0 ���� 1, item_id ����� ���� ������ ������ ����
        int item_id = -1;
        int item_type = -1;

        //�� ��������� �� ���� ��������� ������ (rootElementId)
        while (rs.next()) {
            item_id = rs.getInt("item_id");
            item_type = rs.getInt("item_type");

            //Old
            //oldMapOfXY.put(item_id, currentXY);

            //����� ���������
            int[] newXYFromOldMap = oldMap.get(item_id);
            //���� ���������� ����� ����� ������, �� ��-������ ���������� ������ ���� (��� ��������� ���� isToSaveToDb)
            new FindingElementFocus(db).setCurrentElementFocus(item_id, item_type, newXYFromOldMap);

            int[] oldXYFromNewMap = newMap.get(item_id);

            System.out.println("item_id: " + item_id + "  newXYFromOldMap: " + Arrays.toString(newXYFromOldMap));
            System.out.println("item_id: " + item_id + "  oldXYFromNewMap: " + Arrays.toString(oldXYFromNewMap));

            //System.out.println("newXYFromOldMap" + Arrays.toString(newXYFromOldMap));
            //System.out.println("oldXYFromNewMap" + Arrays.toString(oldXYFromNewMap));

            //�������� ���� ���� ������� �����
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

    //���� ����� ����� ��� ������ ���� ��������� ������
    //���������:
    // rootElementId - ������, ��� ������� ����� ��������� ������ ���������
    // focusMove - ����� �� ������� ����� �������� ��� �������� ������
    // isToSaveToDB - ������������ ������ �� ������ ��������� ������
    // (������ �������, ���� ���� focusMove), �� ��� ���� ������������� ������ ������ ����� ��������� ��������� ������

    public void addAllItems(int rootElementId, int[] focusMove, boolean isToSaveToDB) throws SQLException {

        //��� ����� �� ������ ������ ������������ NodeDao.getNode() ��� ���� ����� ���� �������� KonstruktorElemennDao.getElement()
        ResultSet rs = null;

        String myQuery = "SELECT item_id, item_type FROM c_groups_items where group_id = ";

        String sqlCommand = myQuery + rootElementId;

        try {
            rs = db.runSql(sqlCommand);

        } catch (SQLException se) {
            se.printStackTrace();
        }
            //item_type ����� ���� ������� ���� 0 ���� 1, item_id ����� ���� ������ ������ ����
            int item_id = -1;
            int item_type = -1;

            while (rs.next()) {
                item_id = rs.getInt("item_id");
                item_type = rs.getInt("item_type");

                int[] currentXY = new FindingElementFocus(db).getCurrentElementFocus(item_id,item_type);

                //����������� ������ �������
                oldMapOfXY.put(item_id, currentXY);
                int[] newCurrentXY;

                //��� ��� ������ � ���� ����� ���������
                if (isToSaveToDB == true){
                    newCurrentXY = new int[]{currentXY[0]+focusMove[0], currentXY[1]+focusMove[1]};

                    newMapOfXY.put(item_id, newCurrentXY);

                    //������� ������ � ���� ������ ����� ��������, �.�. �������������� x, y where item_id, item_type
                    new FindingElementFocus(db).setCurrentElementFocus(item_id, item_type, newCurrentXY);

                }
                else {

                }

                // item_type == 1 ��� ������
                if (item_type == 0){
                }

                else if (item_type == 1) {


                    int current_item_id = item_id;

                    //��� ����� ����� �������� FindingFroupFocus ��� !!!FindingFileFocus
                    // ����� ���������� � �������� ��

                    addAllItems(current_item_id, focusMove, isToSaveToDB);


                }
            }
        //return allItems;
        //return newMapOfXY;
    }
}

