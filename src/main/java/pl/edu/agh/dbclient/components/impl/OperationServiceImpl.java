package pl.edu.agh.dbclient.components.impl;

import org.springframework.stereotype.Component;
import pl.edu.agh.dbclient.components.OperationService;
import pl.edu.agh.dbclient.connections.DBConnection;
import pl.edu.agh.dbclient.connections.DBConnectionFactory;
import pl.edu.agh.dbclient.exceptions.DBClientException;
import pl.edu.agh.dbclient.objects.QueryResult;
import pl.edu.agh.dbclient.objects.operations.*;

/**
 * @author mnowak
 */
@Component
public class OperationServiceImpl implements OperationService {

    @Override
    public QueryResult executeCreate(CreateOperation operation) throws DBClientException {
        DBConnection connection = DBConnectionFactory.getOrCreateConnection(operation.getUserSession());
        return connection.performCreate(operation);
    }

    @Override
    public QueryResult executeRead(ReadOperation operation) throws DBClientException {
        DBConnection connection = DBConnectionFactory.getOrCreateConnection(operation.getUserSession());
        return connection.performRead(operation);
    }

    @Override
    public QueryResult executeUpdate(UpdateOperation operation) throws DBClientException {
        DBConnection connection = DBConnectionFactory.getOrCreateConnection(operation.getUserSession());
        return connection.performUpdate(operation);
    }

    @Override
    public QueryResult executeDelete(DeleteOperation operation) throws DBClientException {
        DBConnection connection = DBConnectionFactory.getOrCreateConnection(operation.getUserSession());
        return connection.performDelete(operation);
    }

    @Override
    public QueryResult executeCommand(CommandOperation operation) throws DBClientException {
        DBConnection connection = DBConnectionFactory.getOrCreateConnection(operation.getUserSession());
        return connection.executeCommand(operation);
    }
}
