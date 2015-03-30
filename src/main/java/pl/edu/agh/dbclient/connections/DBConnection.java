package pl.edu.agh.dbclient.connections;

import pl.edu.agh.dbclient.operations.CreateOperation;

/**
 * @author mnowak
 */
public interface DBConnection {

    public void setCredentials(DBCredentials credentials);

    public QueryResult performCreate(CreateOperation operation);


}
