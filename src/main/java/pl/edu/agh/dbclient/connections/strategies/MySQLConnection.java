package pl.edu.agh.dbclient.connections.strategies;

import org.apache.log4j.Logger;
import pl.edu.agh.dbclient.utils.Configuration;

public class MySQLConnection extends GenericSQLConnection {

    private static final Logger LOGGER = Logger.getLogger(MySQLConnection.class);
    private static final int CONNECTION_TIMEOUT = Configuration.getInteger("mysql.timeout", 1000);
    private static final String URL_PREFIX = "jdbc:mysql://";
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";


    @Override
    protected int getConnectionTimeout() {
        return CONNECTION_TIMEOUT;
    }

    @Override
    protected String getUrlPrefix() {
        return URL_PREFIX;
    }

    @Override
    protected String getJdbcDriver() {
        return JDBC_DRIVER;
    }
}
