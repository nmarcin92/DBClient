package pl.edu.agh.dbclient.connections;

/**
 * @author mnowak
 */
public class DBCredentials {

    private String username, password;
    private String url;
    private String databaseName;

    public DBCredentials() {
    }

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
