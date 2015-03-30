package pl.edu.agh.dbclient.connections.strategies;

import pl.edu.agh.dbclient.connections.DBConnection;
import pl.edu.agh.dbclient.connections.DBCredentials;
import pl.edu.agh.dbclient.connections.QueryResult;
import pl.edu.agh.dbclient.operations.CreateOperation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author mnowak
 */
public class PostgreSQLConnection implements DBConnection {

    private static final int CONNECTION_TIMEOUT = 1000;

    private DBCredentials credentials;
    private Connection conn;

    @Override
    public void setCredentials(DBCredentials credentials) {
        this.credentials = credentials;
    }

    @Override
    public QueryResult performCreate(CreateOperation operation) {
        try {
            initializeConnection();
            switch (operation.getType()) {
                case ENTITY:
                    return createTable(operation);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void initializeConnection() throws SQLException {
        if (conn == null || !conn.isValid(CONNECTION_TIMEOUT)) {
            conn = DriverManager.getConnection(credentials.getUrl(), credentials.getUsername(), credentials.getPassword());
        }
    }

    private QueryResult createTable(CreateOperation operation) {
        return null;
    }
}
