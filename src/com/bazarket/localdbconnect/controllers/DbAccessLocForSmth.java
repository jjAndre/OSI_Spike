package com.bazarket.localdbconnect.controllers;

/**
 * Created by Sturlson on 14.04.2016.
 */

import java.io.*;
import java.sql.Connection;
import java.util.HashSet;
import javax.servlet.*;
import javax.servlet.http.*;

import com.bazarket.localdbconnect.DAO.GroupDao;
import com.bazarket.localdbconnect.Entities.KonstruktorElement;
import com.bazarket.localdbconnect.Entities.WSimplMapGroup;
import com.bazarket.localdbconnect.Entities.Group;
import com.bazarket.localdbconnect.model.ElementsOperations.MakeMapMooved;
import com.bazarket.localdbconnect.model.ElementsOperations.RemakeMap;


public class DbAccessLocForSmth extends HttpServlet {

        //idElement and typeElement are the special fields for different JSPs (which can use this Servlet and find the last used rootElement fastly
        // without entering new values in HTML forms)
        // I guess we should'of take these fields as KonstrElement.fields
        private int idElementInt;
        //private int typeElementInt;



        private final static boolean SAVE_TO_DB_TRUE = true;
        private final static boolean SAVE_TO_DB_FALSE = false;

        private final static int CHANGE_GROUP_ACTUAL_ITEMS = 1;
        private final static int CHANGE_GROUP_PREVIOUS_ITEMS = 2;


        private static Group transObjectGroup;
        private static GroupDao groupDao;


    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Connection connection = (Connection) getServletContext().getAttribute("connection");
        groupDao = new GroupDao(connection);


        String idElement = null;
        //String typeElement = null;

        try {
            idElement = request.getParameter("elementId");
            idElementInt = Integer.parseInt(idElement);


//            typeElement = request.getParameter("elementType");
//            typeElementInt = Integer.parseInt(typeElement);

        } catch (Exception e) {

        }

//I want to create transObjectGroup only once when I take idElement from HTML form
        if (idElement != null && idElement != "") {
            transObjectGroup = new Group(idElementInt);
            System.out.println("transObjectGroup was created");
            System.out.println("transObjectGroup.getId(): " + transObjectGroup.getId());
        }


        String isMoovedStr;
        Boolean isMooved = null;

        String reStoreStr;
        Boolean reStore = null;

        String findFocusStr;
        Boolean findFocusAgain = null;

        String mooveXStr = null;
        int mooveX = 0;

        String mooveYStr = null;
        int mooveY = 0;

        String isNewMoveFindStr;
        Boolean isNewMoveFind = null;


        try {


            isMoovedStr = request.getParameter("isMooved");
            isMooved = Boolean.parseBoolean(isMoovedStr);


            reStoreStr = request.getParameter("reStore");
            reStore = Boolean.parseBoolean(reStoreStr);
            //System.out.println("reStore parameter is read from HTML and it's equal to: " + reStore);

            findFocusStr = request.getParameter("findFocusAgain");
            findFocusAgain = Boolean.parseBoolean(findFocusStr);

            isNewMoveFindStr = request.getParameter("newGroupMove");
            isNewMoveFind = Boolean.parseBoolean(isNewMoveFindStr);

            mooveXStr = request.getParameter("mooveX");
            mooveX = Integer.parseInt(mooveXStr);

            mooveYStr = request.getParameter("mooveY");
            mooveY = Integer.parseInt(mooveYStr);

        } catch (Exception e) {
        }


        if (reStore == true) {
            // here we have to change the actual coordinates with the previous ones

            //! We must clone transObjectGroup here
            Group prevTransObjectGroup = new Group(transObjectGroup);
            Group actTransObjectGroup = transObjectGroup;

            //System.out.println("DbAccessLocForSmth: 123: reStore == true block. I'm here");

            //System.out.println("DbAccessLocForSmth: 123: reStore == true block. X,Y: " + transObjectGroup.getLastCoordinatesMove()[0] + ", "+ transObjectGroup.getLastCoordinatesMove()[1]);

            int[] revertedCoords = new int[]{transObjectGroup.getLastCoordinatesMove()[0]*(-1), transObjectGroup.getLastCoordinatesMove()[1]*(-1)};

            System.out.println("DbAccessLocForSmth: 128: \"reStore == true\" block. X,Y: " + revertedCoords[0] +", " + revertedCoords[1]);
                //System.out.println("DbAccessLocForSmth: 130: reStore == true block. PreviousCoords BEFORE reverting the coords: X,Y: " + prevTransObjectGroup.getLastCoordinatesMove()[0] + ", "+ prevTransObjectGroup.getLastCoordinatesMove()[1]);

                /*HashSet<KonstruktorElement> checkHashSet = prevTransObjectGroup.getGroupActualItemsSet();

                for (KonstruktorElement k : checkHashSet) {
                    //System.out.println("MakeMapMooved: 128: Id: " + k.getId());
                    System.out.println("PreviousCoords BEFORE reverting the coords");
                    System.out.println("DbAccessLocForSmth: 138: X: " + k.getCoordinateX());
                    System.out.println("DbAccessLocForSmth: 139: Y: " + k.getCoordinateY());
                }*/

            actTransObjectGroup = MakeMapMooved.moveMapByChangesInCoords(actTransObjectGroup, revertedCoords, connection, SAVE_TO_DB_TRUE);

                /*HashSet<KonstruktorElement> checkHashSet2 = prevTransObjectGroup.getGroupActualItemsSet();

                for (KonstruktorElement k : checkHashSet2) {
                    //System.out.println("MakeMapMooved: 128: Id: " + k.getId());
                    System.out.println("PreviousCoords AFTER reverting the coords");
                    System.out.println("DbAccessLocForSmth: 150: X: " + k.getCoordinateX());
                    System.out.println("DbAccessLocForSmth: 151: Y: " + k.getCoordinateY());
                }*/

            //Here we already have actual and previous items Maps
            //We have to make the transition from Maps to Set<KonstruktorElement> on the Entities' management way
            // So we should have here Actual Items Set and last coordinates moove
            //...work in progress -> isMoove CASE
            WSimplMapGroup transObjectWActualItemsMapSimpleGroup = RemakeMap.returnSimplifiedTransObjectGroup(actTransObjectGroup);

            WSimplMapGroup transObjectWPreviousItemsMapSimpleGroup = RemakeMap.returnSimplifiedTransObjectGroup(prevTransObjectGroup);


            request.setAttribute("oldMapOfXY", transObjectWPreviousItemsMapSimpleGroup.getSimpleGroupItemsMap());
            request.setAttribute("newMapOfXY", transObjectWActualItemsMapSimpleGroup.getSimpleGroupItemsMap());

            RequestDispatcher view = request.getRequestDispatcher("MooveGroup.jsp");
            view.forward(request, response);

        } else if (reStore == false)
        {
        //To check this condition
            if (isNewMoveFind == true){transObjectGroup = null;}



        if (findFocusAgain == true) {
            transObjectGroup.setKonstrElementPreviousCoordinates(new int[]{});

            //may be it'll be better to ask the DB about new actual coordinates
            request.setAttribute("RootGroupActualCoords", transObjectGroup.getActualKonstrElementCoords());
            request.setAttribute("RootGroupPreviousCoords", null);

            //Disabled on July 16, 2016
            /*request.setAttribute("RootGroupActualItemsMap", transObjectGroup.getActualItemsGroupMap());
            request.setAttribute("RootGroupPreviousItemsMap", null);*/

            RequestDispatcher view = request.getRequestDispatcher("resultDB2.jsp");
            view.forward(request, response);
        }


        //if findFocusAgain == false (it's the MAIN logic branch actually)
        else if(findFocusAgain == false) {

            final int TYPE_GROUP = 1;
            //We are looking just for the current RootGroup Coordinates
            //RootGroup means the Group which user is mooving in Konstruktor by mouse
            // isMooved == false means that we don't ask to move Group Item elements


            if (isMooved == false) {

                if (transObjectGroup.getActualKonstrElementCoords() == null) {

         //           final int ACTUAL_COORDINATES = 1;
         //           final int PREVIOUS_COORDINATES = 2;

        //I have to use Dao method only in case of I don't have coords already set or must replace coords by new ones from DB
        //Actually I don't take from or save  previous coords to DB because there is no place in my DB to storage them

                    transObjectGroup = (Group) groupDao.getFromDatabaseAndSetNewKonstrElementActualCoords(transObjectGroup);
                    transObjectGroup.setKonstrElementPreviousCoordinates(null);

                    request.setAttribute("RootGroupActualCoords", transObjectGroup.getActualKonstrElementCoords());

                } else if (transObjectGroup.getActualKonstrElementCoords() != null && transObjectGroup.getKonstrElementPreviousCoordinates() == null) {


                    transObjectGroup.setKonstrElementPreviousCoordinates(transObjectGroup.getActualKonstrElementCoords());

                    //
                    //System.out.println("DbAccessLocForSmth 165: X: " + transObjectGroup.getKonstrElementPreviousCoordinates()[0] + ", Y: " + transObjectGroup.getKonstrElementPreviousCoordinates()[1]);

                    transObjectGroup = (Group) groupDao.getFromDatabaseAndSetNewKonstrElementActualCoords(transObjectGroup);
//                    groupDao.setKonstrElementActualCoordinates(transObjectGroup,konstrElementCoordsFromDAO,SAVE_TO_DB_TRUE);



                    request.setAttribute("RootGroupActualCoords", transObjectGroup.getActualKonstrElementCoords());
                    request.setAttribute("RootGroupPreviousCoords", transObjectGroup.getKonstrElementPreviousCoordinates());

                } else if (transObjectGroup.getActualKonstrElementCoords() != null && transObjectGroup.getKonstrElementPreviousCoordinates() != null) {

                    //I want to comment the lines below, because of case when I have Previous and Actual coordinates and I want to move Group Items
                    //...lines were deleted

                }

                RequestDispatcher view = request.getRequestDispatcher("resultDB2.jsp");
                view.forward(request, response);


            } else if (isMooved == true) {
                //isMooved == false means that we ask to moove all Items in Group by the same way as we've mooved RootGroup (group which consist of these Items)


                    //I'm sending the array with HTML parameters to the method bellow, however mooveX and mooveY could be nulls
                    //mooveX, mooveY are the HTML parameters values in the new int[]{mooveX, mooveY} expression

                transObjectGroup = MakeMapMooved.moveMapByChangesInCoords(transObjectGroup, new int[]{mooveX, mooveY}, connection, SAVE_TO_DB_TRUE);

                    /*System.out.println("DbAccessLocForSmth. line 228 .getActualItemsGroupMap().size(): " + transObjectGroup.getActualItemsGroupMap().size());
                    System.out.println("DbAccessLocForSmth. line 229 .getPreviousItemsGroupMap().size(): " + transObjectGroup.getPreviousItemsGroupMap().size());*/

            WSimplMapGroup transObjectSimpleGroupWActualItemsMap = RemakeMap.returnSimplifiedTransObjectGroup(transObjectGroup);

        //I have some doubts about the sign (+ or -) of transObjectGroup.getLastCoordinatesMove()
        //Take "-" sign, because I need to revert previous changes
                int[] revertedCoords = new int[]{transObjectGroup.getLastCoordinatesMove()[0]*(-1), transObjectGroup.getLastCoordinatesMove()[1]*(-1)};
                Group tempTransObjectGroup = MakeMapMooved.moveMapByChangesInCoords(transObjectGroup, revertedCoords, connection, SAVE_TO_DB_FALSE);

                //Test for file 218? group 3403
                //System.out.println("DBAccessLocForSmth.transObjectSimpleGroupWActualItemsMap " + transObjectSimpleGroupWActualItemsMap.getSimpleGroupItemsMap().get(31087)[0]);

            WSimplMapGroup transObjectSimpleGroupWPreviousItemsMap = RemakeMap.returnSimplifiedTransObjectGroup(tempTransObjectGroup);

                //System.out.println("DBAccessLocForSmth.transObjectSimpleGroupWPreviousItemsMap " + transObjectSimpleGroupWPreviousItemsMap.getSimpleGroupItemsMap().get(31087)[0]);

                request.setAttribute("newMapOfXY", transObjectSimpleGroupWActualItemsMap.getSimpleGroupItemsMap());
                request.setAttribute("oldMapOfXY", transObjectSimpleGroupWPreviousItemsMap.getSimpleGroupItemsMap());

                System.out.println("DBAccessLocForSmth220.transObjectSimpleGroupWActualItemsMap " + transObjectSimpleGroupWActualItemsMap.getSimpleGroupItemsMap().get(31087)[0]);
                System.out.println("DBAccessLocForSmth220.transObjectSimpleGroupWPreviousItemsMap " + transObjectSimpleGroupWPreviousItemsMap.getSimpleGroupItemsMap().get(31087)[0]);

                RequestDispatcher view = request.getRequestDispatcher("MooveGroup.jsp");
                view.forward(request, response);


            }

        }
    }


    }
}


