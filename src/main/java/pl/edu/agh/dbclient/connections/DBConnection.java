package pl.edu.agh.dbclient.connections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.dbclient.operations.CreateOperation;
import pl.edu.agh.dbclient.operations.ReadOperation;
import pl.edu.agh.dbclient.results.QueryResult;

/**
 * @author mnowak
 */
public interface DBConnection {

    static final Logger LOGGER = LoggerFactory.getLogger(DBConnection.class);

    public void setCredentials(DBCredentials credentials);

    public QueryResult performCreate(CreateOperation operation);

    public QueryResult performRead(ReadOperation operation);

}
