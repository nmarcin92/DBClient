package pl.edu.agh.dbclient;

import pl.edu.agh.dbclient.connections.DBConnectionType;
import pl.edu.agh.dbclient.connections.DBCredentials;

/**
 * @author mnowak
 */
public class UserSession {

    private final DBConnectionType connectionType;
    private final DBCredentials credentials;

    public UserSession(DBConnectionType connectionType, DBCredentials credentials) {
        this.connectionType = connectionType;
        this.credentials = credentials;
    }

    public DBConnectionType getConnectionType() {
        return connectionType;
    }

    public DBCredentials getDBCredentials() {
        return credentials;
    }

}
