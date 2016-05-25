package com.bazarket.localdbconnect.web;

/**
 * Created by Sturlson on 14.04.2016.
 */

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.bazarket.localdbconnect.model.*;
import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class DbAccessLocForSmth extends HttpServlet {
    private int[] position1;
    private int[] position2;
    private int[] changeInPosition;
    private HashMap<Integer, int[]> oldMapOfXY;
    private HashMap<Integer, int[]> newMapOfXY;
    /*private Set<Map.Entry<Integer, int[]>> oldSetOfXY;
    private Set<Map.Entry<Integer, int[]>> newSetOfXY;*/

    //������ ����� ����, ����� ������ JSP ����� ������������ ���� ���������,
    // ��� ����� �������� (���������� ������ ���������� ��������)
    private int idElementInt;
    private int typeElementInt;

    /*private boolean isMooved;
    private  boolean isToSaveToDB;*/

    //public MakeConnectionForRS db2;

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //Some new code


        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();


        MakeConnectionForRS db2 = (MakeConnectionForRS) getServletContext().getAttribute("connection2");

        //���� ����� ����: ��� ��� � ������ ���� � ������, ��

        String idElement = null;
        String typeElement = null;

        try {
            idElement = request.getParameter("elementId");
            idElementInt = Integer.parseInt(idElement);

            typeElement = request.getParameter("elementType");
            typeElementInt = Integer.parseInt(typeElement);

        } catch (Exception e) {

        }


        //String loginFromHtml = request.getParameter("loginEmail");


        //������������� ��� ������ � ������� ������
        //������� ����� �� ��������� ������
        String loginFromHtml = (String) session.getAttribute("loginMail");
        System.out.println("loginFromHtml " + loginFromHtml);




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


        try {
            isMoovedStr = request.getParameter("isMooved");
            isMooved = Boolean.parseBoolean(isMoovedStr);


            reStoreStr = request.getParameter("reStore");
            reStore = Boolean.parseBoolean(reStoreStr);

            findFocusStr = request.getParameter("findFocusAgain");
            findFocusAgain = Boolean.parseBoolean(findFocusStr);

            mooveXStr = request.getParameter("mooveX");
            mooveX = Integer.parseInt(mooveXStr);

            mooveYStr = request.getParameter("mooveY");
            mooveY = Integer.parseInt(mooveYStr);

        } catch (Exception e) {
        }




        //��� � ������ ������������ �� ������ ���� �������� � ����
        // � �������� � �������������� � ������� ������ mooveGroupItemsByMap
        if (reStore == true){
            try {
                System.out.println("mooveX in the Beginning of reStore iF_block in DbAccesLocForSmth: " + mooveX);
                System.out.println("mooveY in the Beginning of reStore iF_block in DbAccesLocForSmth: " + mooveY);

                System.out.println("position1 in the Beginning of reStore iF_block in DbAccesLocForSmth: " + Arrays.toString(position1));
                System.out.println("position2 in the Beginning of reStore iF_block in DbAccesLocForSmth: " + Arrays.toString(position2));

                //� ������ ������� ��� ���� ������� � ������ (��� ������ ������ addAllItems ��� mooveGroupItemsByMap)
                //position1 = new int[]{position1[0] + mooveX, position1[1] + mooveY};

                FindingAllItemsInGroup allAllItems = new FindingAllItemsInGroup(db2);

                //out.println("idElementInt " + idElementInt);

                //������ ��� ���, ���� ���������� ������ ��� � ������� ������ mooveGroupItemsByMap ��� ������ ���� ���������� (����� ���),
                // � ��� ����� � ���� �� ���������
                allAllItems.clearMapsOfXY();
                allAllItems.mooveGroupItemsByMap(idElementInt, oldMapOfXY, newMapOfXY);


                System.out.println("mooveX in the End of reStore iF_block in DbAccesLocForSmth: " + mooveX);
                System.out.println("mooveY in the End of reStore iF_block in DbAccesLocForSmth: " + mooveY);

                System.out.println("position1 in the End of reStore iF_block in DbAccesLocForSmth: " + Arrays.toString(position1));
                System.out.println("position2 in the End of reStore iF_block in DbAccesLocForSmth: " + Arrays.toString(position2));

                //out.println("trouble is in DbAccessLocForSmth");

                //� ������ mooveGroupItemsByMap �� ���� �� ������������� ����

                oldMapOfXY = allAllItems.getOldMapOfXY();
                newMapOfXY = allAllItems.getNewMapOfXY();

                /*//Caused by transition from Scriptlets to JSTL
                oldSetOfXY = oldMapOfXY.entrySet();
                newSetOfXY = newMapOfXY.entrySet();

                request.setAttribute("newSetOfXY", oldSetOfXY);
                request.setAttribute("newSetOfXY", newSetOfXY);*/

                request.setAttribute("oldMapOfXY", oldMapOfXY);
                request.setAttribute("newMapOfXY", newMapOfXY);
                //
                //session.getAttribute("loginMail");

                RequestDispatcher view = request.getRequestDispatcher("MooveGroup.jsp");
                view.forward(request, response);



            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        if (findFocusAgain == true) {
            position1 = null;
            position2 = null;


            request.setAttribute("focusXY1", null);
            request.setAttribute("focusXY2", null);

            RequestDispatcher view = request.getRequestDispatcher("resultDB2.jsp");
            view.forward(request, response);
        }


        //��� ������ findFocusAgain == false
        else {


            FindingElementFocus elementFocus = new FindingElementFocus(db2);

            int[] focusXY = {0, 0};

            try {
                focusXY = elementFocus.getCurrentElementFocus(idElementInt, typeElementInt);
                //focusXY = allItems.getCurrentFileFocus(idGroupInt, loginFromHtml);

            } catch (SQLException e) {
                e.printStackTrace();
            }



            //���� ������ ������� ������� ������
            //if (isMooved == false && findFocusAgain == false){

            if (isMooved == false) {


                if (position1 == null) {

                    position1 = focusXY;
                    request.setAttribute("focusXY1", position1);

                    RequestDispatcher view = request.getRequestDispatcher("resultDB2.jsp");
                    view.forward(request, response);

                } else if (position1 != null && position2 == null) {
                    position2 = focusXY;

                    request.setAttribute("focusXY1", position1);
                    request.setAttribute("focusXY2", position2);

                    RequestDispatcher view = request.getRequestDispatcher("resultDB2.jsp");
                    view.forward(request, response);
                }

                //����� ������ ���� �����-�� �������������� �������, �������� �� ������ ����������� �������� ������

                else if (position2 != null && position1 != null) {
                    position1 = focusXY;
                    position2 = null;
                    request.setAttribute("focusXY1", position1);
                    request.setAttribute("focusXY2", null);

                    RequestDispatcher view = request.getRequestDispatcher("resultDB2.jsp");
                    view.forward(request, response);
                }


            } else if (isMooved == true) {

                FindingAllItemsInGroup allItems = new FindingAllItemsInGroup(db2);

                //���� ����� ������� (mooveX != null && mooveY != null), �� ��� ��� ���� ������ ��-�������
                //���� ��������������� �����, ��� ����� ���� �� ���� ����� (��� ������ ������)

                //������-�� ������� ��� �����: mooveXStr == ""
                if ((mooveXStr == null || mooveXStr == "")&& mooveYStr == null) {


                    //
                    if (position1 != null && position2 == null && idElement != null) {
                        position2 = focusXY;

                    }

                    changeInPosition = new int[]{position2[0] - position1[0], position2[1] - position1[1]};

                    try {

                        //
                        //out.println(idElementInt + " " + Arrays.toString(changeInPosition) + " ");

                        allItems.addAllItems(idElementInt, changeInPosition, true);


                        //out.println("trouble is in DbAccessLocForSmth");

                        oldMapOfXY = allItems.getOldMapOfXY();
                        newMapOfXY = allItems.getNewMapOfXY();

                        /*//Caused by transition from Scriptlets to JSTL
                        oldSetOfXY = oldMapOfXY.entrySet();
                        newSetOfXY = newMapOfXY.entrySet();

                        request.setAttribute("oldSetOfXY", oldSetOfXY);
                        request.setAttribute("newSetOfXY", newSetOfXY);*/

                request.setAttribute("oldMapOfXY", oldMapOfXY);
                request.setAttribute("newMapOfXY", newMapOfXY);

                        RequestDispatcher view = request.getRequestDispatcher("MooveGroup.jsp");
                        view.forward(request, response);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                //����� ����� ��������� ����� �� mooveX � mooveY
                else if (mooveXStr != null && mooveYStr != null){

                    changeInPosition = new int[]{mooveX, mooveY};
                    try {

                        //
                        //out.println(idElementInt + " " + Arrays.toString(changeInPosition) + " ");

                        allItems.addAllItems(idElementInt, changeInPosition, true);


                        out.println("trouble is in DbAccessLocForSmth");

                        oldMapOfXY = allItems.getOldMapOfXY();
                        newMapOfXY = allItems.getNewMapOfXY();

                        /*//Caused by transition from Scriptlets to JSTL
                        oldSetOfXY = oldMapOfXY.entrySet();
                        newSetOfXY = newMapOfXY.entrySet();

                        request.setAttribute("oldSetOfXY", oldSetOfXY);
                        request.setAttribute("newSetOfXY", newSetOfXY);*/

                        request.setAttribute("oldMapOfXY", oldMapOfXY);
                        request.setAttribute("newMapOfXY", newMapOfXY);

                     RequestDispatcher view = request.getRequestDispatcher("MooveGroup.jsp");
                        view.forward(request, response);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                //
                }

                else {
                    System.out.println("Smth strange has happened in DbAccessLocForSmth 283");
                    System.out.println("mooveXStr " + mooveXStr);
                    System.out.println("mooveYStr " + mooveYStr);
                }


            }

        }


    }
}

        /*if (session.getAttribute("focusXY1") == null){
            session.setAttribute("focusXY1", focusXY);
            out.println("focusXY1 is determinated");
        }
        else if (session.getAttribute("focusXY1") != null && session.getAttribute("focusXY2") == null){
            session.setAttribute("focusX2", focusXY);
            out.println("focusXY2 is determinated");
        }
        else if (session.getAttribute("focusXY1") != null && session.getAttribute("focusXY2") != null){
            session.setAttribute("focusX1", focusXY);
            session.setAttribute("focusX2", null);
            out.println("focusXY1, focusXY2 are determinated");
        }
        else {
            out.println("Some mystical shit has happened in DbAccessLocForSmth");
        }*/






        /*RequestDispatcher view = request.getRequestDispatcher("resultDB2.jsp");
        view.forward(request, response);*/



