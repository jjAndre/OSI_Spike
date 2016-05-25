package com.bazarket.localdbconnect.model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Sturlson on 21.04.2016.
 */
public class FindingFileFocus {

    private MakeConnectionForRS db;
    private int targetGroupID;
    private String userEmail;

    public FindingFileFocus(MakeConnectionForRS db, int targetGroupID, String userEmail) {
        this.db = db;
        this.targetGroupID = targetGroupID;
        this.userEmail = userEmail;

    }


    public int[] getCurrentFileFocus(int rootGroupId, String userEmail) throws SQLException {
        int user_id = -1;
        int field_id = -1;
        int[] fileFocus;

        int focusX=0;
        int focusY=0;

        ResultSet rsGetUserId = null;
        ResultSet rsGetFileId = null;
        ResultSet rsGetXY = null;


        //Нам нужен юзер, т.к. под каждого юзера с правами для редактирования создается свой вью и фокус файла в Конструкторе
        String qToGetUserId = "SELECT id FROM users where email = "; //
        String sqlCommandToGetUserId = qToGetUserId + "\"" + userEmail + "\"";
        //String sqlCommandToGetUserId = "SELECT id FROM users where email = \"il.centro.aperto@gmail.com\"";


        rsGetUserId = db.runSql(sqlCommandToGetUserId);
        while (rsGetUserId.next()){
            user_id = rsGetUserId.getInt("id");
        }

        //Нам нужен fileid, чтобы забрать координаты x,y фокуса файла - для этого вообще надо бы сделать отдельный метод
        String qToGetFileId = "SELECT c_files.id FROM c_files join c_groups on c_files.uid = c_groups.file_uid where c_groups.id = ";
        String sqlCommandToGetFileId = qToGetFileId + rootGroupId;


        rsGetFileId = db.runSql(sqlCommandToGetFileId);
        while (rsGetFileId.next()){
            field_id = rsGetFileId.getInt("id");
        }

        String qToGetXY1 = "SELECT * FROM c_files_settings where file_id = ";
        String qToGetXY2 = " and editor_id = ";
        //String qToGetXY = "SELECT * FROM c_files_settings where file_id = 218 and editor_id = 13;";
        String sqlCommandToGetXY = qToGetXY1+field_id +qToGetXY2+user_id;


        rsGetXY = db.runSql(sqlCommandToGetXY);
        while (rsGetXY.next()){
            focusX = rsGetXY.getInt("x");
            focusY = rsGetXY.getInt("y");
        }

        fileFocus = new int[]{focusX, focusY};

        return fileFocus;
    }
}
