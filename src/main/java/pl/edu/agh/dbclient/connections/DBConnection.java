package pl.edu.agh.dbclient.connections;

import org.apache.log4j.Logger;
import pl.edu.agh.dbclient.objects.operations.CreateOperation;
import pl.edu.agh.dbclient.objects.operations.DeleteOperation;
import pl.edu.agh.dbclient.objects.operations.ReadOperation;
import pl.edu.agh.dbclient.objects.operations.UpdateOperation;
import pl.edu.agh.dbclient.objects.QueryResult;

/**
 * @author mnowak
 */
public interface DBConnection {

    static final Logger LOGGER = Logger.getLogger(DBConnection.class);

    public void setCredentials(DBCredentials credentials);

    public QueryResult performCreate(CreateOperation operation);

    public QueryResult performRead(ReadOperation operation);

    public QueryResult performUpdate(UpdateOperation operation);

    public QueryResult performDelete(DeleteOperation operation);

    public QueryResult executeCommand(String command);

}
