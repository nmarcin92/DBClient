package pl.edu.agh.dbclient.connections.strategies;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Collections2;
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

import java.sql.*;
import java.util.*;

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
                return readRows(operation);
            default:
                LOGGER.error("Unsupported operation context: " + operation.getContext());
                throw new DatabaseException(WebAppConstants.UNSUPPORTED_OPERATION_CONTEXT_ERROR);
        }
    }

    @Override
    public QueryResult performUpdate(UpdateOperation operation) throws DBClientException {
        initializeConnection();
        switch (operation.getContext()) {
            case ENTITY:
                return updateTable(operation);
            case RECORD:
                return updateRows(operation);
            default:
                LOGGER.error("Unsupported operation context: " + operation.getContext());
                throw new DatabaseException(WebAppConstants.UNSUPPORTED_OPERATION_CONTEXT_ERROR);
        }
    }

    private QueryResult updateRows(UpdateOperation operation) throws DatabaseException {
        QueryResult qr = new QueryResult();
        StringBuilder queryBuilder = new StringBuilder("UPDATE ").append(operation.getEntityName()).append(" (");
        queryBuilder.append(" ) SET ");
        queryBuilder.append(Joiner.on(", ").join(Collections2.transform(operation.getUpdated().getAttributes().entrySet(), new Function<Map.Entry, String>() {
            @Override
            public String apply(Map.Entry input) {
                return input.getKey() + " = " + input.getValue();
            }
        })));
        queryBuilder.append(" WHERE id = ").append(operation.getId());
        try {
            conn.createStatement().execute(queryBuilder.toString());
        } catch (SQLException e) {
            LOGGER.error("Error during updating row", e);
            throw new DatabaseException(e.getMessage());
        }

        return qr;
    }

    private QueryResult updateTable(UpdateOperation operation) throws DatabaseException {
        QueryResult qr = new QueryResult();
        boolean first = true;
        StringBuilder queryBuilder = new StringBuilder("ALTER TABLE ").append(operation.getEntityName()).append(" ");
        if (!Utils.isEmptyCollection(operation.getToAdd())) {
            queryBuilder.append(Joiner.on(", ").join(Collections2.transform(operation.getToAdd(), new Function<EntityAttribute, String>() {
                @Override
                public String apply(EntityAttribute input) {
                    return "ADD " + input.getAttributeName() + " " + input.getDataType();
                }
            })));
            first = false;
        }

        if (!Utils.isEmptyCollection(operation.getToDelete())) {
            queryBuilder.append(first ? " " : ", ");
            queryBuilder.append(Joiner.on(", ").join(Collections2.transform(operation.getToDelete(), new Function<EntityAttribute, String>() {
                @Override
                public String apply(EntityAttribute input) {
                    return "DROP COLUMN " + input.getAttributeName();
                }
            })));
            first = false;
        }
        if (!Utils.isEmptyCollection(operation.getToModify())) {
            queryBuilder.append(first ? " " : ", ");
            queryBuilder.append(Joiner.on(", ").join(Collections2.transform(operation.getToModify(), new Function<EntityAttribute, String>() {
                @Override
                public String apply(EntityAttribute input) {
                    return "ALTER COLUMN " + input.getAttributeName() + " TYPE " + input.getDataType();
                }
            })));
        }

        try {
            LOGGER.info(queryBuilder.toString());
            conn.createStatement().execute(queryBuilder.toString());
        } catch (SQLException e) {
            LOGGER.error("Error while updating table", e);
            throw new DatabaseException(e.getMessage());
        }

        return qr;
    }

    @Override
    public QueryResult performDelete(DeleteOperation operation) throws DBClientException {
        initializeConnection();
        switch (operation.getContext()) {
            case ENTITY:
                return deleteTable(operation);
            case RECORD:
                return deleteRows(operation);
            default:
                LOGGER.error("Unsupported operation context: " + operation.getContext());
                throw new DatabaseException(WebAppConstants.UNSUPPORTED_OPERATION_CONTEXT_ERROR);
        }

    }

    private QueryResult deleteRows(DeleteOperation operation) throws DBClientException {
        QueryResult qr = new QueryResult();
        StringBuilder queryBuilder = new StringBuilder("DELETE FROM ");
        if (operation.getPreconditions().size() > 0) {
            queryBuilder.append(" WHERE ").append(Joiner.on(" AND ").join(operation.getPreconditions()));
        }
        try {
            conn.createStatement().execute(qr.toString());
        } catch (SQLException e) {
            LOGGER.error("Error while deleting row", e);
            throw new DatabaseException(e.getMessage());
        }
        return qr;
    }

    @Override
    public QueryResult executeCommand(CommandOperation operation) throws DBClientException {
        initializeConnection();
        QueryResult qr = new QueryResult();
        Entity entity = new Entity("Query result");
        qr.setEntity(entity);
        try {
            if (operation.isHasResult()) {
                ResultSet resultSet = conn.createStatement().executeQuery(operation.getQuery());
                while (resultSet.next()) {
                    EntityRow row = new EntityRow();
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    for (int i = 1; i <= metaData.getColumnCount(); ++i) {
                        String value = resultSet.getString(i);
                        String key = metaData.getColumnName(i);
                        if (i == 0) {
                            entity.getAttributes().add(new EntityAttribute(key));
                        }
                        row.getAttributes().put(key, value);

                    }
                    entity.getRows().add(row);
                }
            } else {
                conn.createStatement().execute(operation.getQuery());
            }
        } catch (SQLException e) {
            LOGGER.error("Error during performing custom query", e);
            throw new DatabaseException(e.getMessage());
        }
        return qr;
    }

    private QueryResult deleteTable(DeleteOperation operation) throws DatabaseException {
        QueryResult qr = new QueryResult();
        String queryString = "DROP TABLE " + operation.getEntityName();
        try {
            conn.createStatement().execute(queryString);
        } catch (SQLException e) {
            LOGGER.error("Error while deleting table", e);
            throw new DatabaseException(e.getMessage());
        }
        return qr;
    }

    private QueryResult insertRow(CreateOperation operation) throws DatabaseException {
        QueryResult qr = new QueryResult();
        StringBuilder queryBuilder = new StringBuilder("INSERT INTO ").append(operation.getEntityName());
        List<String> keys = new ArrayList<>(operation.getAttributes().keySet());
        if (!operation.hasParameter(Operation.OperationParameter.ENTIRE_RECORD)) {
            queryBuilder.append('(')
                    .append(Joiner.on(", ").join(keys))
                    .append(')');
        }
        queryBuilder.append(" VALUES( ");
        queryBuilder.append(Joiner.on(",").join(Collections.nCopies(operation.getAttributes().size(), "? "))).append(" )");

        try {
            PreparedStatement statement = conn.prepareStatement(queryBuilder.toString());
            int paramCount = 1;
            for (String k : keys) {
                statement.setString(paramCount, operation.getAttributes().get(k));
                ++paramCount;
            }
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Error while inserting row", e);
            throw new DatabaseException(e.getMessage());
        }
        return qr;
    }

    private QueryResult readRows(ReadOperation operation) throws DatabaseException {
        StringBuilder queryBuilder = new StringBuilder("SELECT ");
        boolean entireRecord = operation.hasParameter(Operation.OperationParameter.ENTIRE_RECORD);
        if (operation.hasParameter(Operation.OperationParameter.ENTIRE_RECORD)) {
            queryBuilder.append('*');
        } else {
            queryBuilder.append(Joiner.on(",").join(operation.getAttributeNames()));
        }
        queryBuilder.append(" FROM ").append(operation.getEntityName());

        try {
            QueryResult qr = readTableSchema(operation);
            Iterator<EntityAttribute> iterator = qr.getEntity().getAttributes().iterator();
            while (iterator.hasNext()) {
                if (!operation.getAttributeNames().contains(iterator.next().getAttributeName())) {
                    iterator.remove();
                }
            }

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

        List<String> parts = Splitter.on(".").splitToList(operation.getEntityName());
        String tableNameStripped = parts.get(parts.size() - 1);
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM INFORMATION_SCHEMA.COLUMNS" +
                    " WHERE TABLE_NAME = ? ");
            statement.setString(1, tableNameStripped);
            ResultSet rs = statement.executeQuery();
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
