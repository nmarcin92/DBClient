package pl.edu.agh.dbclient.exceptions;

/**
 * @author mnowak
 */
public class ConnectionInitializationException extends DBClientException {

    public ConnectionInitializationException(String msg, Throwable e) {
        super(msg, e);
    }

    public ConnectionInitializationException(String msg) {
        super(msg);
    }
}
