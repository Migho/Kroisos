package Bookkeeping;

import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SQLManager {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/bookkeeping_TKOaly";

    static final String USER = "local";
    static final String PASS = "1234";  //:-)
    static Statement stmt = null;
    static Connection conn = null;

    public static ResultSet runSQLQuery(String statement, Object[] parameters) {
        try {
            return getPreparedStatement(statement, parameters).executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int runSQLUpdate(String statement, Object[] parameters) {
        try {
            return getPreparedStatement(statement, parameters).executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private static PreparedStatement getPreparedStatement(String statement, Object[] parameters) {
        try {
            if(stmt == null) connect();
            else if(stmt.isClosed()) connect();
            PreparedStatement stmt = conn.prepareStatement(statement);
            for(int i=0; i<parameters.length; i++) {
                stmt.setObject(i + 1, parameters[i]);
            }
            //System.out.println(stmt.toString());
            return stmt;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static int connect() {
        try {
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 4: Execute a query
            stmt = conn.createStatement();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    public static int close() {
        try{
            if(stmt != null) stmt.close();
            if(conn != null) conn.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            } catch(SQLException se2){
                System.out.println(se2);
            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        return 0;
    }
}
