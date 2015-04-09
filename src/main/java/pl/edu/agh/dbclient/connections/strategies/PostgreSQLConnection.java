package pl.edu.agh.dbclient.connections.strategies;

import com.google.common.base.Joiner;
import pl.edu.agh.dbclient.UserSession;
import pl.edu.agh.dbclient.connections.DBConnection;
import pl.edu.agh.dbclient.connections.DBConnectionFactory;
import pl.edu.agh.dbclient.connections.DBConnectionType;
import pl.edu.agh.dbclient.connections.DBCredentials;
import pl.edu.agh.dbclient.exceptions.ConnectionInitializationException;
import pl.edu.agh.dbclient.operations.Operation;
import pl.edu.agh.dbclient.operations.ReadOperation;
import pl.edu.agh.dbclient.results.Entity;
import pl.edu.agh.dbclient.results.EntityAttribute;
import pl.edu.agh.dbclient.results.QueryResult;
import pl.edu.agh.dbclient.operations.CreateOperation;
import pl.edu.agh.dbclient.utils.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author mnowak
 */
public class PostgreSQLConnection implements DBConnection {

    private static final int CONNECTION_TIMEOUT = Configuration.getInteger("postgres.timeout", 1000);
    private static final String URL_PREFIX = "jdbc:postgresql://";
    private static final String JDBC_DRIVER = "org.postgresql.Driver";

    private DBCredentials credentials;
    private Connection conn;

    @Override
    public void setCredentials(DBCredentials credentials) {
        this.credentials = credentials;
    }

    @Override
    public QueryResult performCreate(CreateOperation operation) {
        QueryResult qr = new QueryResult();

        try {
            initializeConnection();
            switch (operation.getContext()) {
                case ENTITY:
                    return createTable(operation);
                case RECORD:
                    return insertRow(operation);
                default:
                    qr.setSuccess(false);
                    qr.addError("Unsupported operation context");
                    break;
            }

        } catch (Exception e) {
            LOGGER.error("Error while performing create operation", e);
            qr.setSuccess(false);
            qr.addError(e.getMessage());
        }

        return qr;
    }

    private QueryResult insertRow(CreateOperation operation) {
        QueryResult qr = new QueryResult();

        StringBuilder queryBuilder = new StringBuilder("INSERT INTO ")
                .append(operation.getEntityName());
        if (!operation.hasParameter(CreateOperation.CreateParameter.ENTIRE_RECORD)) {
            queryBuilder.append('(')
                    .append(operation.getParameter(CreateOperation.CreateParameter.ATTRIBUTE_NAMES))
                    .append(')');
        }
        queryBuilder.append(" VALUES( ")
                .append(operation.getParameter(CreateOperation.CreateParameter.ATTRIBUTE_VALUES))
                .append(")");
        try {
            conn.createStatement().executeUpdate(queryBuilder.toString());
        } catch (SQLException e) {
            LOGGER.error("Error while inserting row", e);
            qr.setSuccess(false);
            qr.addError(e.getErrorCode() + ": " + e.getMessage());
        }
        return qr;
    }

    @Override
    public QueryResult performRead(ReadOperation operation) {
        QueryResult qr = new QueryResult();

        try {
            initializeConnection();
            switch (operation.getContext()) {
                case ENTITY:
                    return readTableSchema(operation);
                default:
                    qr.setSuccess(false);
                    qr.addError("Unsupported operation context");
                    break;
            }
        } catch (Exception e) {
            LOGGER.error("Error while reading");
            qr.setSuccess(false);
            qr.addError(e.getMessage());
        }

        return qr;
    }

    private QueryResult readTableSchema(ReadOperation operation) {
        QueryResult qr = new QueryResult();
        Entity entity = new Entity(operation.getEntityName());

        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM INFORMATION_SCHEMA.COLUMNS")
                .append(" WHERE TABLE_NAME='").append(operation.getEntityName()).append("'");
        try {
            ResultSet rs = conn.createStatement().executeQuery(queryBuilder.toString());
            while (rs.next()) {
                EntityAttribute attribute = new EntityAttribute(rs.getString("column_name"));
                attribute.setDataType(rs.getString("data_type"));
                entity.getAttributes().add(attribute);
            }
            qr.setEntity(entity);

        } catch (SQLException e) {
            LOGGER.error("Error while reading table schema", e);
            qr.setSuccess(false);
            qr.addError("Reading table schema failed.");
        }

        return qr;
    }

    private void initializeConnection() throws SQLException, ConnectionInitializationException {
        if (conn == null || !conn.isValid(CONNECTION_TIMEOUT)) {
            try {
                Class.forName(JDBC_DRIVER);
            } catch (ClassNotFoundException e) {
                LOGGER.error("Driver class not found", e);
                throw new ConnectionInitializationException("Driver class not found", e);
            }
            conn = DriverManager.getConnection(URL_PREFIX + credentials.getUrl() + "/" + credentials.getDatabaseName(), credentials.getUsername(), credentials.getPassword());
        }
    }

    private QueryResult createTable(CreateOperation operation) {
        QueryResult qr = new QueryResult();
        qr.setEntity(new Entity(operation.getEntityName()));

        StringBuilder queryBuilder = new StringBuilder("CREATE TABLE ")
                .append(operation.getEntityName())
                .append("( )");
        try {
            conn.createStatement().executeUpdate(queryBuilder.toString());
        } catch (SQLException e) {
            LOGGER.error("Error while creating table", e);
            qr.setSuccess(false);
            qr.addError(e.getErrorCode() + ": " + e.getMessage());
        }

        return qr;
    }

}
