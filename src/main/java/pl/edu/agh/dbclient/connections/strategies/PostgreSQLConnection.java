package pl.edu.agh.dbclient.connections.strategies;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.edu.agh.dbclient.objects.UserSession;
import pl.edu.agh.dbclient.connections.DBConnection;
import pl.edu.agh.dbclient.connections.DBConnectionFactory;
import pl.edu.agh.dbclient.connections.DBConnectionType;
import pl.edu.agh.dbclient.connections.DBCredentials;
import pl.edu.agh.dbclient.exceptions.ConnectionInitializationException;
import pl.edu.agh.dbclient.objects.operations.*;
import pl.edu.agh.dbclient.objects.operations.Operation.OperationParameter;
import pl.edu.agh.dbclient.objects.Entity;
import pl.edu.agh.dbclient.objects.EntityAttribute;
import pl.edu.agh.dbclient.objects.EntityRow;
import pl.edu.agh.dbclient.objects.QueryResult;
import pl.edu.agh.dbclient.utils.Configuration;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

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

    @Override
    public QueryResult performRead(ReadOperation operation) {
        QueryResult qr = new QueryResult();

        try {
            initializeConnection();
            switch (operation.getContext()) {
                case ENTITY:
                    return readTableSchema(operation);
                case RECORD:
                    return readRow(operation);
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

    @Override
    public QueryResult performUpdate(UpdateOperation operation) {
        throw new NotImplementedException();
    }

    @Override
    public QueryResult performDelete(DeleteOperation operation) {
        throw new NotImplementedException();
    }

    @Override
    public QueryResult executeCommand(String command) {
        throw new NotImplementedException();
    }

    private QueryResult insertRow(CreateOperation operation) {
        QueryResult qr = new QueryResult();

        StringBuilder queryBuilder = new StringBuilder("INSERT INTO ")
                .append(operation.getEntityName());
        if (!operation.hasParameter(OperationParameter.ENTIRE_RECORD)) {
            queryBuilder.append('(')
                    .append(operation.getParameter(OperationParameter.ATTRIBUTE_NAMES))
                    .append(')');
        }
        queryBuilder.append(" VALUES( ")
                .append(operation.getParameter(OperationParameter.ATTRIBUTE_VALUES))
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

    private QueryResult readRow(ReadOperation operation) {
        QueryResult qr = new QueryResult();
        StringBuilder queryBuilder = new StringBuilder("SELECT ");
        boolean entireRecord = operation.hasParameter(OperationParameter.ENTIRE_RECORD);
        List<String> attributes = entireRecord ? null :
                Arrays.asList(operation.getParameter(OperationParameter.ATTRIBUTE_NAMES).split(","));
        if (operation.hasParameter(OperationParameter.ENTIRE_RECORD)) {
            queryBuilder.append('*');
        } else {
            queryBuilder.append(operation.getParameter(OperationParameter.ATTRIBUTE_NAMES));
        }
        queryBuilder.append(" FROM ").append(operation.getEntityName());

        try {
            qr = readTableSchema(operation);
            ResultSet rs = conn.createStatement().executeQuery(queryBuilder.toString());
            while (rs.next()) {
                EntityRow row = new EntityRow();
                for (EntityAttribute entityAttribute : qr.getEntity().getAttributes()) {
                    if (entireRecord || attributes.contains(entityAttribute.getAttributeName())) {
                        row.getAttributes().put(entityAttribute.getAttributeName(), rs.getString(entityAttribute.getAttributeName()));
                    }
                }
                qr.getEntity().getRows().add(row);
            }
        } catch (SQLException e) {
            LOGGER.error("Error while reading records", e);
            qr.setSuccess(false);
            qr.addError(e.getErrorCode() + " " + e.getMessage());
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

    public static void main(String[] args) {
        DBCredentials credentials = new DBCredentials("postgres", "postgres", "localhost:5432", "postgres");
        try {
            DBConnection conn = DBConnectionFactory.getOrCreateConnection(new UserSession(DBConnectionType.POSTGRESQL, credentials));
            //QueryResult qr = conn.performCreate(new CreateOperation(Operation.OperationContext.ENTITY, "tabela_3"));
            //  QueryResult qr = conn.performRead(new ReadOperation(Operation.OperationContext.ENTITY, "tabela_2"));
            ReadOperation readOperation = new ReadOperation(Operation.OperationContext.RECORD, "tabela_2");
            readOperation.addParameter(OperationParameter.ENTIRE_RECORD);
          //  QueryResult qr = conn.performRead(readOperation);

            ObjectMapper mapper = new ObjectMapper();
            OperationRequest request = new OperationRequest();
            request.setSession(new UserSession(DBConnectionType.POSTGRESQL, credentials));
            request.setOperation(readOperation);

            mapper.writeValue(new File("create_request.json"), request);
            byte[] arr = new byte[10000];
            new FileInputStream(new File("create_request.json")).read(arr);
            OperationRequest req = mapper.readValue(arr, OperationRequest.class);
                    System.out.println("a");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
