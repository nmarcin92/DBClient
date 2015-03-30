package pl.edu.agh.dbclient.connections;

/**
 * @author mnowak
 */
public class DBCredentials {

    private final String username, password;
    private final String url;

    public DBCredentials(String username, String password, String url) {
        this.username = username;
        this.password = password;
        this.url = url;
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
}
