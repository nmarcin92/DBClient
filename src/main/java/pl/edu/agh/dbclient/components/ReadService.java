package pl.edu.agh.dbclient.components;

import pl.edu.agh.dbclient.connections.DBConnection;
import pl.edu.agh.dbclient.objects.QueryResult;
import pl.edu.agh.dbclient.objects.operations.CreateOperation;
import pl.edu.agh.dbclient.objects.operations.Operation;
import pl.edu.agh.dbclient.objects.operations.ReadOperation;

/**
 * @author mnowak
 */
public interface ReadService {

    public QueryResult executeRead(DBConnection connection, ReadOperation operation);

}
