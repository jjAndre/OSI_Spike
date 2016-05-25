package com.bazarket.localdbconnect.model;

/**
 * Created by Sturlson on 21.04.2016.
 */

import java.sql.ResultSet;
import java.sql.SQLException;


public class FindingElementFocus {

    private MakeConnectionForRS db;
    //private int elementID;
    //private String userEmail;



//user email нам в принципе не нужен, т.к. координаты группы (в отличие от фокуса файла) не зависят от юзера
// каждый юзер с правами редактирования может изменить свойства группы - это по всей видимости

    //Может быть нам нужен юзер для того, чтобы разрешить или не разрешить сделать записи в базе данных

    //Наверное не нужен elementID в качестве параметра для Конструктора
    public FindingElementFocus(MakeConnectionForRS db) {

        this.db = db;
        //this.elementID = elementID;
        //this.userEmail = userEmail;

    }

    /*public List<Integer> getAllItems(){
        return allItems;
    };*/


    public int[] getCurrentElementFocus(int elementId, int elementType) throws SQLException {
    //public int[] getCurrentGroupFocus(int rootGroupId, String userEmail) throws SQLException {
        //int user_id = -1;
        //int field_id = -1;
        int[] elementFocus;

        int focusX=0;
        int focusY=0;

        //ResultSet rsGetUserId = null;
        //ResultSet rsGetFileId = null;
        ResultSet rsGetXY = null;


        String qToGetXY;
        if (elementType == 1) {
            qToGetXY = "SELECT * FROM c_groups where id = ";
        }
        else qToGetXY = "SELECT * FROM c_nodes where id = ";


        String sqlCommandToGetXY = qToGetXY + elementId;


        rsGetXY = db.runSql(sqlCommandToGetXY);
        while (rsGetXY.next()){
            focusX = rsGetXY.getInt("x");
            focusY = rsGetXY.getInt("y");
        }

        elementFocus = new int[]{focusX, focusY};

        return elementFocus;
    }

    public void setCurrentElementFocus(int elementId, int elementType, int[] focusXY) throws SQLException {


        //int focusX=0;
        //int focusY=0;

        //Мы же не хотим получать фокус, мы хотим его установить. Может нам объект ResultSet и не нужен вовсе
        //ResultSet rsGetXY = null;


        //String qToGetXY;
        String qToSetXY;

        if (elementType == 1) {
            //qToSetXY = "UPDATE c_groups set x = -783, y = -799 where id = 3345";
            String qToSetXY1 = "UPDATE c_groups set x = ";
            String qToSetXY2 = ", y = ";
            String qToSetXY3 = " where id = ";
            qToSetXY = qToSetXY1 + focusXY[0] + qToSetXY2 + focusXY[1] + qToSetXY3 + elementId;



            //qToGetXY = "SELECT * FROM c_groups where id = ";
        }
        else {
            //qToGetXY = "SELECT * FROM c_nodes where id = ";
            String qToSetXY1 = "UPDATE c_nodes set x = ";
            String qToSetXY2 = ", y = ";
            String qToSetXY3 = " where id = ";
            qToSetXY = qToSetXY1 + focusXY[0] + qToSetXY2 + focusXY[1] + qToSetXY3 + elementId;
        }


        //String sqlCommandToGetXY = qToGetXY + elementId;

        //rsGetXY = db.runSql(sqlCommandToGetXY);

        //System.out.println("qToSetXY " + qToSetXY);
        //db.runSql(qToSetXY);
        db.startSql(qToSetXY);
    }




}
