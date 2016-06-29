package com.bazarket.localdbconnect.controllers;

import javax.servlet.*;

import com.bazarket.localdbconnect.Info.ConnData;
import com.bazarket.localdbconnect.model.*;
import com.bazarket.localdbconnect.model.Connections.DBConnector;

import java.sql.Connection;


public class DbServletContextListener implements ServletContextListener{
	


	public void contextInitialized(ServletContextEvent event){


		ServletContext sc = event.getServletContext();

		ConnData chiavi = new ConnData();


		String db_url = chiavi.getDb_url();
		String user = chiavi.getUser();
		String pass = chiavi.getPass();

		DBConnector.createConnection(db_url, user, pass);
		System.out.println("Connection established...");

		Connection connection = DBConnector.getConn();

		sc.setAttribute("connection", connection);

		/*Connection connection = ConnectionFactory.getConnection();
		sc.setAttribute("connectionFromConnFacrory",connection);*/
		//sc.setAttribute("connection", connRs);
		//Remake by using ConnectionFactory

		
		/*MakeConnectionForRS connRs = new MakeConnectionForRS(db_url, user, pass);
		sc.setAttribute("connection", connRs);
		//Продублируем
		MakeConnectionForRS connRs2 = new MakeConnectionForRS(db_url, user, pass);
		sc.setAttribute("connection2", connRs2);*/
		
	}
	
	public void contextDestroyed(ServletContextEvent event){
		DBConnector.closeConnection();
	}
	
}