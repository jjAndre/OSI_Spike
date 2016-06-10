package com.bazarket.localdbconnect.model;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.bazarket.localdbconnect.model.ConnData;

/**
 * Created by Sturlson on 08.06.2016.
 */
public class ConnectionFactory {

    private static ConnectionFactory instance = new ConnectionFactory();
    //����� ���������������� ���� ��� � ��������� ������ ������ ConnectionFactory?
    //�� ���� ����������� ������ instance ����� (��� �����? � ������ ������?) ����� ����� ���������� ��������

    static ConnData chiavi = new ConnData();

    private static final String URL = chiavi.getDb_url();;
    private static final String USER = chiavi.getUser();
    private static final String PASSWORD = chiavi.getPass();
    private static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";



    private ConnectionFactory(){
        try {
            Class.forName(DRIVER_CLASS);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Connection createConnection(){
        Connection connection = null;
    try {
        connection = DriverManager.getConnection(URL,USER,PASSWORD);
    } catch (SQLException e){
        System.out.println("ERRoR: Unable to connect to Database");
    }
        return connection;

    }

    public static Connection getConnection(){
        //������, ��������, �� this.instance.createConnection
        //����� ������ ��������� ����� createConnection - ����� ����� ���� ������ � ������ ������� - instance ��� ��������?
        return instance.createConnection();
    }


}
