package pl.edu.agh.dbclient.components;

import pl.edu.agh.dbclient.exceptions.DBClientException;
import pl.edu.agh.dbclient.objects.QueryResult;
import pl.edu.agh.dbclient.objects.operations.*;

/**
 * @author mnowak
 */
public interface OperationService {

    public QueryResult executeCreate(CreateOperation operation) throws DBClientException;

    public QueryResult executeRead(ReadOperation operation) throws DBClientException;

    public QueryResult executeUpdate(UpdateOperation operation) throws DBClientException;

    public QueryResult executeDelete(DeleteOperation operation) throws DBClientException;

    public QueryResult executeCommand(CommandOperation operation) throws DBClientException;

}
