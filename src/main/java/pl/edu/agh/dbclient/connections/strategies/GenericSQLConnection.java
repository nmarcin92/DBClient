package pl.edu.agh.dbclient.connections.strategies;

import com.google.common.base.Joiner;
import org.apache.log4j.Logger;
import pl.edu.agh.dbclient.WebAppConstants;
import pl.edu.agh.dbclient.connections.DBConnection;
import pl.edu.agh.dbclient.connections.DBCredentials;
import pl.edu.agh.dbclient.exceptions.ConnectionInitializationException;
import pl.edu.agh.dbclient.exceptions.DBClientException;
import pl.edu.agh.dbclient.exceptions.DatabaseException;
import pl.edu.agh.dbclient.objects.Entity;
import pl.edu.agh.dbclient.objects.EntityAttribute;
import pl.edu.agh.dbclient.objects.EntityRow;
import pl.edu.agh.dbclient.objects.QueryResult;
import pl.edu.agh.dbclient.objects.operations.*;
import pl.edu.agh.dbclient.utils.Utils;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author mnowak
 */
public abstract class GenericSQLConnection implements DBConnection {

    private static final Logger LOGGER = Logger.getLogger(GenericSQLConnection.class);

    protected DBCredentials credentials;
    protected Connection conn;

    protected abstract int getConnectionTimeout();

    protected abstract String getUrlPrefix();

    protected abstract String getJdbcDriver();

    @Override
    public void setCredentials(DBCredentials credentials) {
        this.credentials = credentials;
    }

    @Override
    public QueryResult performCreate(CreateOperation operation) throws DBClientException {
        initializeConnection();
        switch (operation.getContext()) {
            case ENTITY:
                return createTable(operation);
            case RECORD:
                return insertRow(operation);
            default:
                LOGGER.error("Unsupported operation context: " + operation.getContext());
                throw new DatabaseException(WebAppConstants.UNSUPPORTED_OPERATION_CONTEXT_ERROR);
        }
    }

    @Override
    public QueryResult performRead(ReadOperation operation) throws DBClientException {
        initializeConnection();
        switch (operation.getContext()) {
            case ENTITY:
                return readTableSchema(operation);
            case RECORD:
                return readRow(operation);
            default:
                LOGGER.error("Unsupported operation context: " + operation.getContext());
                throw new DatabaseException(WebAppConstants.UNSUPPORTED_OPERATION_CONTEXT_ERROR);
        }
    }

    @Override
    public QueryResult performUpdate(UpdateOperation operation) {
        throw new NotImplementedException();
    }

    @Override
    public QueryResult performDelete(DeleteOperation operation) {
        throw new NotImplementedException();
    }

    @Override
    public QueryResult executeCommand(CommandOperation operation) {
        throw new NotImplementedException();
    }

    private QueryResult insertRow(CreateOperation operation) throws DatabaseException {
        QueryResult qr = new QueryResult();

        StringBuilder queryBuilder = new StringBuilder("INSERT INTO ")
                .append(operation.getEntityName());
        if (!operation.hasParameter(Operation.OperationParameter.ENTIRE_RECORD)) {
            queryBuilder.append('(')
                    .append(Joiner.on(", ").join(operation.getAttributes().keySet()))
                    .append(')');
        }
        queryBuilder.append(" VALUES( ")
                .append(Utils.joinWithQuotationMarks(operation.getAttributes().values()))
                .append(")");
        try {
            LOGGER.info("Executing SQL statement: " + queryBuilder.toString());
            conn.createStatement().executeUpdate(queryBuilder.toString());
        } catch (SQLException e) {
            LOGGER.error("Error while inserting row", e);
            throw new DatabaseException(e.getMessage());
        }
        return qr;
    }

    private QueryResult readRow(ReadOperation operation) throws DatabaseException {
        StringBuilder queryBuilder = new StringBuilder("SELECT ");
        boolean entireRecord = operation.hasParameter(Operation.OperationParameter.ENTIRE_RECORD);
        if (operation.hasParameter(Operation.OperationParameter.ENTIRE_RECORD)) {
            queryBuilder.append('*');
        } else {
            queryBuilder.append(Joiner.on(", ").join(operation.getAttributeNames()));
        }
        queryBuilder.append(" FROM ").append(operation.getEntityName());

        try {
            QueryResult qr = readTableSchema(operation);
            ResultSet rs = conn.createStatement().executeQuery(queryBuilder.toString());
            while (rs.next()) {
                EntityRow row = new EntityRow();
                for (EntityAttribute entityAttribute : qr.getEntity().getAttributes()) {
                    if (entireRecord || operation.getAttributeNames().contains(entityAttribute.getAttributeName())) {
                        row.getAttributes().put(entityAttribute.getAttributeName(), rs.getString(entityAttribute.getAttributeName()));
                    }
                }
                qr.getEntity().getRows().add(row);
            }
            return qr;
        } catch (SQLException e) {
            LOGGER.error("Error while reading records", e);
            throw new DatabaseException(e.getMessage());
        }

    }


    private QueryResult readTableSchema(ReadOperation operation) throws DatabaseException {
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
            throw new DatabaseException(e.getMessage());
        }

        return qr;
    }

    private void initializeConnection() throws ConnectionInitializationException {
        try {
            if (conn == null || !conn.isValid(getConnectionTimeout())) {

                Class.forName(getJdbcDriver());
                conn = DriverManager.getConnection(getUrlPrefix() + credentials.getUrl() + "/" + credentials.getDatabaseName(), credentials.getUsername(), credentials.getPassword());
            }
        } catch (ClassNotFoundException e) {
            LOGGER.error("Driver class not found", e);
            throw new ConnectionInitializationException(WebAppConstants.CONNECTION_INITIALIZATION_ERROR);
        } catch (SQLException e) {
            LOGGER.error("Connection initialization failed", e);
            throw new ConnectionInitializationException(WebAppConstants.BAD_CREDENTIALS_ERROR);
        }

    }

    private QueryResult createTable(CreateOperation operation) throws DatabaseException {
        QueryResult qr = new QueryResult();
        qr.setEntity(new Entity(operation.getEntityName()));

        StringBuilder queryBuilder = new StringBuilder("CREATE TABLE ")
                .append(operation.getEntityName())
                .append("( )");
        try {
            conn.createStatement().executeUpdate(queryBuilder.toString());
        } catch (SQLException e) {
            LOGGER.error("Error while creating table", e);
            throw new DatabaseException(e.getMessage());
        }

        return qr;
    }


}
