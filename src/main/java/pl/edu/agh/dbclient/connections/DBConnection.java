package pl.edu.agh.dbclient.connections;

import org.apache.log4j.Logger;
import pl.edu.agh.dbclient.exceptions.DBClientException;
import pl.edu.agh.dbclient.objects.operations.*;
import pl.edu.agh.dbclient.objects.QueryResult;

/**
 * @author mnowak
 */
public interface DBConnection {

    public void setCredentials(DBCredentials credentials);

    public QueryResult performCreate(CreateOperation operation) throws DBClientException;

    public QueryResult performRead(ReadOperation operation) throws DBClientException;

    public QueryResult performUpdate(UpdateOperation operation) throws DBClientException;

    public QueryResult performDelete(DeleteOperation operation) throws DBClientException;

    public QueryResult executeCommand(CommandOperation command) throws DBClientException;

}
