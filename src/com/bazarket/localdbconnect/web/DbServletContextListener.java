package com.bazarket.localdbconnect.web;

import javax.servlet.*;
import javax.servlet.http.HttpSession;

import com.bazarket.localdbconnect.model.*;


public class DbServletContextListener implements ServletContextListener{
	
	
	public void contextInitialized(ServletContextEvent event){


		ServletContext sc = event.getServletContext();

		ConnData chiavi = new ConnData();
		

		String db_url = chiavi.getDb_url();


		String user = chiavi.getUser();


		String pass = chiavi.getPass();

		
		MakeConnectionForRS connRs = new MakeConnectionForRS(db_url, user, pass);
		sc.setAttribute("connection", connRs);
		//Продублируем
		MakeConnectionForRS connRs2 = new MakeConnectionForRS(db_url, user, pass);
		sc.setAttribute("connection2", connRs2);
		
	}
	
	public void contextDestroyed(ServletContextEvent event){
		
	}
	
}