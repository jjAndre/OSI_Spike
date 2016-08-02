package com.bazarket.localdbconnect.controllers;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.bazarket.localdbconnect.DAO.GroupDao;
import com.bazarket.localdbconnect.DAO.KonstrElementDao;
import com.bazarket.localdbconnect.Entities.Group;
import com.bazarket.localdbconnect.Entities.WSimplMapGroup;
import com.bazarket.localdbconnect.model.ElementsOperations.RemakeMap;
//import com.bazarket.localdbconnect.model.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;


public class DbAccessLoc extends HttpServlet{

	//Can be here a groupDao field?
	private final static boolean INCLUDING_ITEMS_YES = true;


	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{

		Connection connection = (Connection) getServletContext().getAttribute("connection");

		GroupDao groupDao = new GroupDao(connection);


	//	response.setContentType("text/html");
	//	PrintWriter out = response.getWriter();


		HttpSession session = request.getSession();


	//	MakeConnectionForRS db = (MakeConnectionForRS) getServletContext().getAttribute("connection");


		String idElement = request.getParameter("elementId");
		       int idElementInt = Integer.parseInt(idElement);

		String loginFromHtml = request.getParameter("loginEmail");


		Group newTransObjectGroup = new Group(idElementInt);
		//


// I need in actual items from group
		newTransObjectGroup = groupDao.getByIdFilledUpGroup(newTransObjectGroup, INCLUDING_ITEMS_YES);

		//Map<int[], int[]> allGroupItemsMap = newGroup.receiveActualItemsGroupMapFromDB();
		//System.out.println("allGroupItemsList.size() " + allGroupItemsMap.size());

		WSimplMapGroup simpleTransObjectGroup = RemakeMap.returnSimplifiedTransObjectGroup(newTransObjectGroup);


		session.setAttribute("loginMail", loginFromHtml);

		request.setAttribute("groupItemsMap", simpleTransObjectGroup.getSimpleGroupItemsMap());

		RequestDispatcher view = request.getRequestDispatcher("resultDB.jsp");
		view.forward(request,response);

	}

}