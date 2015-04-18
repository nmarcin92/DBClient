package pl.edu.agh.dbclient.components.impl;

import org.springframework.stereotype.Component;
import pl.edu.agh.dbclient.components.ReadService;
import pl.edu.agh.dbclient.connections.DBConnection;
import pl.edu.agh.dbclient.objects.QueryResult;
import pl.edu.agh.dbclient.objects.operations.CreateOperation;
import pl.edu.agh.dbclient.objects.operations.ReadOperation;

/**
 * @author mnowak
 */
@Component
public class ReadServiceImpl implements ReadService {

    @Override
    public QueryResult executeRead(DBConnection connection, ReadOperation operation) {
        return connection.performRead(operation);
    }
}
