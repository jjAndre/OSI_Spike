package com.bazarket.localdbconnect.web;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.bazarket.localdbconnect.model.*;
import java.sql.*;
import java.util.HashMap;


public class DbAccessLoc extends HttpServlet{
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{

    //
		response.setContentType("text/html");
	//	PrintWriter out = response.getWriter();

		HttpSession session = request.getSession();



		/*if (session.isNew()){
			out.println("This is a new session printed from DbAccessLoc");
		}
		else {
			out.println("Welcome back printed from DbAccessLoc");
		}*/
	//

		MakeConnectionForRS db = (MakeConnectionForRS) getServletContext().getAttribute("connection");

	    //Здесь ругается, если перенаправить запрос из MooveGroup.jsp
		String idElement = request.getParameter("elementId");
		       int idElementInt = Integer.parseInt(idElement);

		String loginFromHtml = request.getParameter("loginEmail");


		//moove to DAO start
		Group group;
		HashMap<Integer,Integer> allGroupItemsList = null;
		try {
			group = (Group) KonstrElementDao.getKonstrElement(idElementInt, 1, true);
			//1 means "group"
			// true - to create the map of elements, which a Group consist of

			allGroupItemsList = group.getElementsMap();
			System.out.println("allGroupItemsList.size() " + allGroupItemsList.size());

		} catch (SQLException e) {
			e.printStackTrace();
		}
		//moove to DAO finish



		/*//у объекта типа FindingAllItemsInGroup мы будем вызывать метод addAllItems (но мы просто достаем элементы группы, фокус не меняем)
			FindingAllItemsInGroup allItems = new FindingAllItemsInGroup(db);

			HashMap<Integer, int[]> allGroupItemsList= null;

			try {
				allItems.addAllItems(idElementInt, null, false);
				allGroupItemsList = allItems.getOldMapOfXY();

			} catch (SQLException e) {
				e.printStackTrace();
			}*/


		//
		session.setAttribute("loginMail", loginFromHtml);
		//

		request.setAttribute("listitems", allGroupItemsList);

		RequestDispatcher view = request.getRequestDispatcher("resultDB.jsp");
		view.forward(request,response);

	}

}