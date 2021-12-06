import java.sql.*;
import java.util.Locale;

/*
 * Written by: Amber Garcia
 * public class RunSQL
 * Runs stored procedures and regular queries via string. Will also construct a ListTableModel for the JTable during execution in run_query();
 * */

/**
 * Note, the class only supports:
 * ✓ A single stored procedure with either no tables returned or ONE table returned.
 * ✓ A single select statement with ONE table.
 */
public class RunSQL {
    private int GroupMemberId;
    private ConnectDatabase connectionUrl;
    private ListTableModel model;
    private String runSQLResponse;

    RunSQL(String beginning, String serverPort, String current_database, String user, String password) {
        connectionUrl = new ConnectDatabase(beginning, serverPort, current_database, user, password);
    }

    public void run_query(String query) {
        if(connectionUrl.getUser() == null || connectionUrl.getPassword() == null){
            runSQLResponse = "No username or password set. Query not sent to SSMS.";
            model = null;
        }else{
            String SQL = query;
            System.out.println(connectionUrl.getConnectionUrl());
            try (Connection con = DriverManager.getConnection(connectionUrl.getConnection());
                 Statement stmt = con.createStatement();) {

                if(!query.toUpperCase(Locale.ROOT).contains("USE")){
                    runSQLResponse = "Query must include a USE statement.";
                }else{
                    // Runs queries with empty or full results sets.
                    if(!query.toUpperCase(Locale.ROOT).contains("SELECT")){
                        if(query.toUpperCase(Locale.ROOT).contains("EXEC")) {
                            // Immediately runs stored procedures.
                            if (query.contains("--rs")) {
                                runSQLResponse = "Successfully retrieved result set.";
                                CallableStatement cStmt = con.prepareCall(query);
                                ResultSet rs = cStmt.executeQuery();
                                model = ListTableModel.createModelFromResultSet(rs);

                            } else {
                                runSQLResponse = "Successfully executed stored procedure.";
                                CallableStatement cStmt = con.prepareCall(query);
                                cStmt.execute();
                            }
                        }else{
                            runSQLResponse = "Statement unable to be processed. Only run stored procedures or selects.";
                        }
                    }else{
                        runSQLResponse = "Successfully retrieved result set.";
                        ResultSet rs = stmt.executeQuery(SQL);
                        model = ListTableModel.createModelFromResultSet(rs);
                    }

                }
            } catch (SQLException e) {
                runSQLResponse = "Error Message: " + e.getMessage();
            }
        }

    }


    public void switchDatabase(String db) {
        connectionUrl.setDatabase(db);
    }
    public ConnectDatabase getConnectionUrl() {
        return connectionUrl;
    }

    public void setConnectionUrl(ConnectDatabase connectionUrl) {
        this.connectionUrl = connectionUrl;
    }
    public ListTableModel getModel(){
        return model;
    }

    public void setGroupMemberId(int groupMemberId) {
        GroupMemberId = groupMemberId;
    }
    public String getRunSQLResponse() {
        return runSQLResponse;
    }
}
