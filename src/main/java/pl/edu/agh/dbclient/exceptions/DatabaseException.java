package pl.edu.agh.dbclient.exceptions;

/**
 * @author mnowak
 */
public class DatabaseException extends DBClientException {

    public DatabaseException(String msg, Throwable e) {
        super(msg, e);
    }

    public DatabaseException(String msg) {
        super(msg);
    }
}
