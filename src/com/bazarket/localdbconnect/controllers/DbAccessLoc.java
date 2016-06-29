package com.bazarket.localdbconnect.controllers;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.bazarket.localdbconnect.DAO.GroupDao;
import com.bazarket.localdbconnect.DAO.KonstrElementDao;
import com.bazarket.localdbconnect.Entities.Group;
//import com.bazarket.localdbconnect.model.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;


public class DbAccessLoc extends HttpServlet{



	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{

		Connection connection = (Connection) getServletContext().getAttribute("connection");


	//	response.setContentType("text/html");
	//	PrintWriter out = response.getWriter();


		HttpSession session = request.getSession();


	//	MakeConnectionForRS db = (MakeConnectionForRS) getServletContext().getAttribute("connection");


		String idElement = request.getParameter("elementId");
		       int idElementInt = Integer.parseInt(idElement);

		String loginFromHtml = request.getParameter("loginEmail");


		//moove to DAO start
		Group newGroup = new Group(idElementInt, connection);
		//boolean IS_TO_GET_FROM_DB = true;

		Map<int[], int[]> allGroupItemsMap = newGroup.receiveActualItemsGroupMapFromDB();
		System.out.println("allGroupItemsList.size() " + allGroupItemsMap.size());


		session.setAttribute("loginMail", loginFromHtml);

		request.setAttribute("groupItemsMap", allGroupItemsMap);

		RequestDispatcher view = request.getRequestDispatcher("resultDB.jsp");
		view.forward(request,response);

	}

}