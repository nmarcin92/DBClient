package pl.edu.agh.dbclient.components;

import org.springframework.stereotype.Component;

import pl.edu.agh.dbclient.connections.DBConnection;
import pl.edu.agh.dbclient.objects.QueryResult;
import pl.edu.agh.dbclient.objects.operations.CreateOperation;
import pl.edu.agh.dbclient.objects.operations.OperationRequest;

/**
 * @author mnowak
 */
@Component
public class CreateServiceImpl implements CreateService {

	@Override
	public int someMethod() {
		return 42;
	}

	@Override
	public QueryResult executeCreate(DBConnection connection, OperationRequest operationRequest) {
		return connection.performCreate((CreateOperation) operationRequest.getOperation());
	}
}
