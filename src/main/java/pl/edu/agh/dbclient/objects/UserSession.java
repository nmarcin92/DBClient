package pl.edu.agh.dbclient.objects;

import pl.edu.agh.dbclient.connections.DBConnectionType;
import pl.edu.agh.dbclient.connections.DBCredentials;

/**
 * @author mnowak
 */
public class UserSession {

    private DBConnectionType connectionType;
    private DBCredentials dbCredentials;

    public UserSession(){};

    public UserSession(DBConnectionType connectionType, DBCredentials dbCredentials) {
        this.connectionType = connectionType;
        this.dbCredentials = dbCredentials;
    }

    public DBConnectionType getConnectionType() {
        return connectionType;
    }

    public DBCredentials getDbCredentials() {
        return dbCredentials;
    }

}
