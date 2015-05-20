package pl.edu.agh.dbclient.connections;

import org.apache.log4j.Logger;
import pl.edu.agh.dbclient.WebAppConstants;
import pl.edu.agh.dbclient.connections.strategies.MongoDBConnection;
import pl.edu.agh.dbclient.connections.strategies.MySQLConnection;
import pl.edu.agh.dbclient.connections.strategies.PostgreSQLConnection;
import pl.edu.agh.dbclient.exceptions.ConnectionInitializationException;
import pl.edu.agh.dbclient.objects.UserSession;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mnowak
 */
public class DBConnectionFactory {

    private static final Logger LOGGER = Logger.getLogger(DBConnectionFactory.class);
    private static final Map<UserSession, DBConnection> CURRENT_CONNECTIONS = new HashMap<>();
    private static final Map<DBConnectionType, Class<? extends DBConnection>> CONNECTION_STRATEGIES = new EnumMap<>(DBConnectionType.class);

    static {
        CONNECTION_STRATEGIES.put(DBConnectionType.POSTGRESQL, PostgreSQLConnection.class);
        CONNECTION_STRATEGIES.put(DBConnectionType.MYSQL, MySQLConnection.class);
        CONNECTION_STRATEGIES.put(DBConnectionType.MONGODB, MongoDBConnection.class);
    }

    public static DBConnection getOrCreateConnection(UserSession session) throws ConnectionInitializationException {
        try {
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
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.error("Connection class instantation exception", e);
            throw new ConnectionInitializationException(WebAppConstants.CONNECTION_INITIALIZATION_ERROR);
        }
    }
}
