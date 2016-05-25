package com.bazarket.localdbconnect.web;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.bazarket.localdbconnect.model.*;
import java.sql.*;
import java.util.HashMap;
import java.util.List;

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



	//takeFocus1
	//takeFocus2


	//у объекта типа FindingAllItemsInGroup мы будем вызывать метод addAllItems (но мы просто достаем элементы группы, фокус не меняем)
			FindingAllItemsInGroup allItems = new FindingAllItemsInGroup(db);
	//FindingAllItemsInGroup allItems = new FindingAllItemsInGroup(db, idElementInt, loginFromHtml);

		//List<Integer> allGroupItemsList = null;
		HashMap<Integer, int[]> allGroupItemsList= null;

		try {
			//allGroupItemsList = allItems.addAllItems(idElementInt,false,null);
			allItems.addAllItems(idElementInt, null, false);
			allGroupItemsList = allItems.getOldMapOfXY();

		} catch (SQLException e) {
			e.printStackTrace();
		}


		//
		session.setAttribute("loginMail", loginFromHtml);
		//

		request.setAttribute("listitems", allGroupItemsList);

		RequestDispatcher view = request.getRequestDispatcher("resultDB.jsp");
		view.forward(request,response);

	}

}