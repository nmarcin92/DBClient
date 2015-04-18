package pl.edu.agh.dbclient.exceptions;

/**
 * @author mnowak
 */
public class DBClientException extends Exception {

    public DBClientException(String msg, Throwable e) {
        super(msg, e);
    }

    public DBClientException(String msg) {
        super(msg);
    }

}
