package pl.edu.agh.dbclient.connections;

/**
 * @author mnowak
 */
public class DBCredentials {

    private final String username, password;
    private final String url;
    private final String databaseName;

    public DBCredentials(String username, String password, String url, String databaseName) {
        this.username = username;
        this.password = password;
        this.url = url;
        this.databaseName = databaseName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getUrl() {
        return url;
    }

    public String getDatabaseName() {
        return databaseName;
    }
}
