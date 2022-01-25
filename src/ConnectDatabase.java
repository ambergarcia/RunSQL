/*
* Written by: Amber Garcia
* public class ConnectDatabase
* Used for storing the connection URL, and reconstructing it for swapping between databases.
* Word of caution, stored procedures and queries MUST be run as seperate batches. Stored procedures take precedence.
* */
public class ConnectDatabase {

    // "jdbc:sqlserver://<server>:<port>;databaseName=[name];user=<user>;password=<password>";
    private String connectionUrl;
    private String beginning;


    private String serverPort;
    private String current_database;

    private String user;
    private String password;

    /*
     * Declares your connection and current database
     */
    ConnectDatabase(String beginning, String serverPort, String current_database, String user, String password){
        connectionUrl =
                beginning + "://" + serverPort
                        + ";databaseName=" + current_database
                        + ";user=" + user
                        + ";password=" + password;
        this.beginning = beginning;
        this.serverPort = serverPort;
        this.current_database = current_database;
        this.user = user;
        this.password = password;
    }

    public void setDatabase(String databaseName){
        current_database = databaseName;
        connectionUrl =
                beginning + "://" + serverPort
                        + ";databaseName=" + databaseName
                        + ";user=" + user
                        + ";password=" + password;
    }
    public void setCredentials(String user, String password){
        this.user = user;
        this.password = password;
        connectionUrl =
                beginning + "://" + serverPort
                        + ";databaseName=" + current_database
                        + ";user=" + user
                        + ";password=" + password;
    }
    public String getConnection() {
        return connectionUrl;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getConnectionUrl() {
        return connectionUrl;
    }

    public String getBeginning() {
        return beginning;
    }

    public String getServerPort() {
        return serverPort;
    }

    public String getCurrent_database() {
        return current_database;
    }
}
