package pl.edu.agh.dbclient.connections;

import pl.edu.agh.dbclient.UserSession;
import pl.edu.agh.dbclient.connections.strategies.PostgreSQLConnection;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mnowak
 */
public class DBConnectionFactory {

    private static final Map<UserSession, DBConnection> CURRENT_CONNECTIONS = new HashMap<UserSession, DBConnection>();
    private static final Map<DBConnectionType, Class<? extends DBConnection>> CONNECTION_STRATEGIES = new HashMap<DBConnectionType, Class<? extends DBConnection>>();

    static {
        CONNECTION_STRATEGIES.put(DBConnectionType.POSTGRESQL, PostgreSQLConnection.class);
    }

    public static DBConnection getOrCreateConnection(UserSession session) throws IllegalAccessException, InstantiationException {
        if (CURRENT_CONNECTIONS.containsKey(session)) {
            return CURRENT_CONNECTIONS.get(session);
        } else {
            Class<? extends DBConnection> connectionClass = CONNECTION_STRATEGIES.get(session.getConnectionType());
            if (connectionClass == null) {
                throw new UnsupportedOperationException("This connection type is not supported.");
            }
            DBConnection conn = connectionClass.newInstance();
            CURRENT_CONNECTIONS.put(session, conn);
            return conn;
        }
    }
}
