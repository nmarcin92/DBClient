package pl.edu.agh.dbclient.connections;

import pl.edu.agh.dbclient.operations.CreateOperation;
import pl.edu.agh.dbclient.operations.ReadOperation;
import pl.edu.agh.dbclient.results.QueryResult;

/**
 * @author mnowak
 */
public interface DBConnection {

    public void setCredentials(DBCredentials credentials);

    public QueryResult performCreate(CreateOperation operation);

    public QueryResult performRead(ReadOperation operation);
}
