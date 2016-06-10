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
    //Сразу инициализируется поле это и создается объект класса ConnectionFactory?
    //За счет статичности объект instance везде (где везде? в рамках класса?) будет иметь одинаковое значение

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
        //Почему, например, не this.instance.createConnection
        //Зачем делать приватным метод createConnection - чтобы можно было только у одного объекта - instance его вызывать?
        return instance.createConnection();
    }


}
