package pl.edu.agh.dbclient.components;

import pl.edu.agh.dbclient.connections.DBConnection;
import pl.edu.agh.dbclient.objects.QueryResult;
import pl.edu.agh.dbclient.objects.operations.OperationRequest;

/**
 * @author mnowak
 */
public interface CreateService {

	public int someMethod();

	public QueryResult executeCreate(DBConnection connection, OperationRequest operationRequest);
}
