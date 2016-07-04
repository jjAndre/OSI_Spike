package com.bazarket.localdbconnect.controllers;

/**
 * Created by Sturlson on 14.04.2016.
 */

import java.io.*;
import java.sql.Connection;
import javax.servlet.*;
import javax.servlet.http.*;

import com.bazarket.localdbconnect.Entities.WSimplMapGroup;
import com.bazarket.localdbconnect.Entities.Group;
import com.bazarket.localdbconnect.model.ElementsOperations.MakeMapMooved;
import com.bazarket.localdbconnect.model.ElementsOperations.RemakeMap;
import com.bazarket.localdbconnect.model.ElementsOperations.ReturnPreviousItems;



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


    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Connection connection = (Connection) getServletContext().getAttribute("connection");

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
            transObjectGroup = new Group(idElementInt, connection);
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

         ReturnPreviousItems.returnPreviousAllGroupItemsCoords(transObjectGroup);

            /*HashMap<Integer, int[]> simpleActualItemsGroup = RemakeMap.remake((HashMap<int[], int[]>) transObjectGroup.getActualItemsGroupMap());
            HashMap<Integer, int[]> simplePreviousItemsGroup = RemakeMap.remake((HashMap<int[], int[]>) transObjectGroup.getPreviousItemsGroupMap());*/

            WSimplMapGroup transObjectSimpleGroupWActualItemsMap = RemakeMap.returnSimplifiedTransObjectGroup(transObjectGroup, CHANGE_GROUP_ACTUAL_ITEMS);
            WSimplMapGroup transObjectSimpleGroupWPreviousItemsMap = RemakeMap.returnSimplifiedTransObjectGroup(transObjectGroup, CHANGE_GROUP_PREVIOUS_ITEMS);


            request.setAttribute("oldMapOfXY", transObjectSimpleGroupWPreviousItemsMap.getSimpleGroupItemsMap());
            request.setAttribute("newMapOfXY", transObjectSimpleGroupWActualItemsMap.getSimpleGroupItemsMap());

            RequestDispatcher view = request.getRequestDispatcher("MooveGroup.jsp");
            view.forward(request, response);

        } else
        {
            if (isNewMoveFind == true){
                transObjectGroup = null;
            }



        if (findFocusAgain == true) {
            transObjectGroup.setKonstrElementPreviousCoordinates(new int[]{});


            request.setAttribute("RootGroupActualItemsMap", transObjectGroup.getActualItemsGroupMap());
            request.setAttribute("RootGroupPreviousItemsMap", null);

            RequestDispatcher view = request.getRequestDispatcher("resultDB2.jsp");
            view.forward(request, response);
        }


        //if findFocusAgain == false (it's the MAIN logic branch actually)
        else {

            final int TYPE_GROUP = 1;

    int[] konstrElementCoordsFromDAO = transObjectGroup.receiveKonstrElementCoordsFromDB(idElementInt, TYPE_GROUP);

            System.out.println("Test DbAccessLocForSmth: x: " + konstrElementCoordsFromDAO[0] + ", y: " + konstrElementCoordsFromDAO[1]);


            //We are looking just for the current RootGroup Coordinates
            //RootGroup means the Group which user is mooving in Konstruktor by mouse
            // isMooved == false means that we don't ask to move Group Item elements


            if (isMooved == false) {

                if (transObjectGroup.getActualKonstrElementCoords() == null) {


                    transObjectGroup.setKonstrElementActualCoordinates(transObjectGroup.getId(), TYPE_GROUP, konstrElementCoordsFromDAO, SAVE_TO_DB_FALSE);
                    transObjectGroup.setKonstrElementPreviousCoordinates(null);

                    request.setAttribute("RootGroupActualCoords", transObjectGroup.getActualKonstrElementCoords());

                } else if (transObjectGroup.getActualKonstrElementCoords() != null && transObjectGroup.getKonstrElementPreviousCoordinates() == null) {


                    transObjectGroup.setKonstrElementPreviousCoordinates(transObjectGroup.getActualKonstrElementCoords());

                    //
                    //System.out.println("DbAccessLocForSmth 165: X: " + transObjectGroup.getKonstrElementPreviousCoordinates()[0] + ", Y: " + transObjectGroup.getKonstrElementPreviousCoordinates()[1]);

                    transObjectGroup.setKonstrElementActualCoordinates(idElementInt, TYPE_GROUP, konstrElementCoordsFromDAO, SAVE_TO_DB_TRUE);


                    request.setAttribute("RootGroupActualCoords", transObjectGroup.getActualKonstrElementCoords());
                    request.setAttribute("RootGroupPreviousCoords", transObjectGroup.getKonstrElementPreviousCoordinates());

                } else if (transObjectGroup.getActualKonstrElementCoords() != null && transObjectGroup.getKonstrElementPreviousCoordinates() != null) {

                    //I want to comment the lines below, because of case when I have Previous and Actual coordinates and I want to move Group Items
                    //...lines were deleted

                }

                RequestDispatcher view = request.getRequestDispatcher("resultDB2.jsp");
                view.forward(request, response);


            } else if (isMooved == true) {
                //isMooved == false means that we ask to moove all Items in Group by the same way as we've mooved RootGroup (group whicj consist of theses Items)


                transObjectGroup = MakeMapMooved.moveMapByChangesInCoords(transObjectGroup, new int[]{mooveX, mooveY});
                    /*System.out.println("DbAccessLocForSmth. line 228 .getActualItemsGroupMap().size(): " + transObjectGroup.getActualItemsGroupMap().size());
                    System.out.println("DbAccessLocForSmth. line 229 .getPreviousItemsGroupMap().size(): " + transObjectGroup.getPreviousItemsGroupMap().size());*/

            WSimplMapGroup transObjectSimpleGroupWActualItemsMap = RemakeMap.returnSimplifiedTransObjectGroup(transObjectGroup, CHANGE_GROUP_ACTUAL_ITEMS);

                //Test for file 218? group 3403
                System.out.println("DBAccessLocForSmth.transObjectSimpleGroupWActualItemsMap " + transObjectSimpleGroupWActualItemsMap.getSimpleGroupItemsMap().get(31087)[0]);

            WSimplMapGroup transObjectSimpleGroupWPreviousItemsMap = RemakeMap.returnSimplifiedTransObjectGroup(transObjectGroup, CHANGE_GROUP_PREVIOUS_ITEMS);

                System.out.println("DBAccessLocForSmth.transObjectSimpleGroupWPreviousItemsMap " + transObjectSimpleGroupWPreviousItemsMap.getSimpleGroupItemsMap().get(31087)[0]);




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


