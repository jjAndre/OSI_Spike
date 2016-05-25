package com.bazarket.localdbconnect.model;

import java.sql.*;


//Это класс-аттрибут. Его вызывает Listener, чтобы установить соединение с базой данных
//Как использовать один connection для более чем одного запроса? Нужно обязательно будет закрыть соединение
// закрыть может быть в конце сервлета где-нибудь
//Listener назначает экземпляр класса MakeConnection в качестве аттрибута контекста

public class MakeConnectionForRS{
	
	private Connection conn = null;
	//private Statement stmt = null;
	
		
		//Наверное не надо все же в параметры зафигачивать sqlCommand
	public MakeConnectionForRS(String url, String user_name, String password){
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
			
			this.conn = DriverManager.getConnection(url, user_name, password);
					
			
			} catch (SQLException se){
				//Handle errors for JDBC
				se.printStackTrace();			
			} catch (Exception e){
				//Handle errors for Class.forName
				e.printStackTrace();
			}
	}
        
    public Connection getConnection(){
			return this.conn;
		}
		
	public ResultSet runSql(String sql)	throws SQLException {
		
		Connection conn1 = this.getConnection();
		Statement sta = conn1.createStatement();
		
		//Statement sta = conn.createStatement();
		//Statement sta = this.getConnection().createStatement();
		
		return sta.executeQuery(sql);
	        }

	//Записал на всякий случай отдельный метод startSql и в нем определил conn2
	//Проверить эту ботву
	public void startSql(String sql)	throws SQLException {

		Connection conn2 = this.getConnection();
		Statement sta = conn2.createStatement();

		//Statement sta = conn.createStatement();
		//Statement sta = this.getConnection().createStatement();

		sta.executeUpdate(sql);
	}
		
		}
	
	
	
	
	
