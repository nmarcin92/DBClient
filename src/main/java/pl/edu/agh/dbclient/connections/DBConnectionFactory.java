package pl.edu.agh.dbclient.connections;

import pl.edu.agh.dbclient.objects.UserSession;
import pl.edu.agh.dbclient.connections.strategies.PostgreSQLConnection;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mnowak
 */
public class DBConnectionFactory {

    private static final Map<UserSession, DBConnection> CURRENT_CONNECTIONS = new HashMap<UserSession, DBConnection>();
    private static final Map<DBConnectionType, Class<? extends DBConnection>> CONNECTION_STRATEGIES = new EnumMap<DBConnectionType, Class<? extends DBConnection>>(DBConnectionType.class);

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
            conn.setCredentials(session.getDbCredentials());
            CURRENT_CONNECTIONS.put(session, conn);
            return conn;
        }
    }
}
