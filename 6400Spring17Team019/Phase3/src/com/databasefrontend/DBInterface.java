/* References:
 http://stackoverflow.com/questions/192078/how-do-i-get-the-size-of-a-java-sql-resultset
  */
package com.databasefrontend;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Date;

/**
 * Created by Goutham on 3/14/2017.
 */

public class DBInterface {
    private final String url;
    private final String user;
    private final String password;
    private Connection connection;

    public static String[] serviceNames = new String[]{"Food Bank", "Soup Kitchen", "Shelter", "Food Pantry"};
    public static int numServices = 4; // Global variable with number of services

    public DBInterface() {
        System.out.println("DB being initiated");
        url = "jdbc:postgresql://localhost/cs6400_sp17_team019";
        user = "postgres";
        password = "a";
    }

    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL Database");
        } catch (org.postgresql.util.PSQLException e) {
            System.out.println("Fatal Error ! " + e.getMessage());
            return null;
        } catch(SQLException e) {
            System.out.println(e.getMessage());
            return null;
        } catch(Exception e) {
            System.out.println("Error! " + e.getMessage());
            return null;
        }
        return conn;
    }
}
