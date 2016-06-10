package com.bazarket.localdbconnect.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.bazarket.localdbconnect.model.ConnectionFactory;

/**
 * Created by Sturlson on 08.06.2016.
 */
public class NodeDao {
    private Connection connection;
    private Statement statement;

    public NodeDao(){}

    //����� getNode ����� ����� ����� ������������
    public Node getNode(int nodeId) throws SQLException{
        //�������� ���������� ���������, �������� ��� KonstruktorElement (����� ��� ���� ���� DAO �������?)
        //��� Group ����� ������ String query
        String query = null;
        //String query = "SELECT * FROM c_nodes WHERE id=" + nodeId;
        ResultSet rs = null;
        Node node = null;

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
        } finally {
            //���� ������� rs, statement, connection
        }
        return node;
    }
}
