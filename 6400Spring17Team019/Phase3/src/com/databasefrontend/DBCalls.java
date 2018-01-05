package com.databasefrontend;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by adityasahai on 22/03/17.
 */
public class DBCalls {

    private sqlCall_enum callid;
    private Statement st;
    private ResultSet rs;
    private Connection connection;

    DBCalls(sqlCall_enum callid) {
        this.callid = callid;
    }

    DBCalls(sqlCall_enum callid, String[] params) {
        this.callid = callid;
        this.callid.setParams(params);
    }

    TableData executeReadCall() {
        try {
            DBInterface db = new DBInterface();
            connection = db.connect();
            if (connection == null) {
                throw new SQLException();
            }
            st = connection.createStatement();
            int numQueries = this.callid.getQueryCount();
            String[] queries = this.callid.getQueries();
            for (int y = 0; y < numQueries - 1; y++) {
                // Execute all queries but the last one
                st.execute(queries[y]);
            }
            // Execute last query
            boolean hasResults = st.execute(queries[numQueries - 1]);
            rs = null;
            TableData td = null;
            while (hasResults) {
                rs = st.getResultSet();
                if (!rs.isBeforeFirst()) {
                    System.out.println("Null Value returned for query " + this.callid.toString());
                    return null;
                }
                ResultSetMetaData rsmd = rs.getMetaData();
                int j = 0;
                String[] colNames = new String[rsmd.getColumnCount()];
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    colNames[j++] = rsmd.getColumnName(i);
                }
                List<String[]> list = new ArrayList<>();
                AtomicInteger index = new AtomicInteger();
                while (rs.next()) {
                    String[] row = new String[rsmd.getColumnCount()];
                    index.set(0);
                    for (int colindex = 1; colindex <= rsmd.getColumnCount(); colindex++) {
                        row[index.getAndIncrement()] = rs.getString(colindex);
                    }
                    list.add(row);
                }
                hasResults = st.getMoreResults();

                td = new TableData();
                Object[][] data = new Object[list.size()][];
                Iterator<String[]> iterator = list.iterator();
                int k = 0;
                while (iterator.hasNext()) {
                    data[k++] = iterator.next();
                }
                td.setNumRows(list.size());
                td.setData(data);
                td.setColumnNames(colNames);
            }
            cleanup();
            return td;
        } catch (java.sql.SQLTimeoutException e) {
            System.out.println("Database Access Timed out! " + e.getMessage());
            return null;
        } catch (java.sql.SQLException e) {
            System.out.println("Database Error! " + e.getMessage());
            return null;
        }
    }

    public int executeWriteCall() {
        try {
            DBInterface db = new DBInterface();
            Connection connection = db.connect();
            Statement st = connection.createStatement();
            int numQueries = this.callid.getQueryCount();
            String[] queries = this.callid.getQueries();
            for (int y = 0; y < numQueries; y++) {
                st.execute(queries[y]);
            }
            cleanup();
        } catch (java.sql.SQLTimeoutException e) {
            System.out.println("Database Access Timed out! " + e.toString());
            return -1;

        } catch (java.sql.SQLException e) {
            System.out.println("Database Error! " + e.toString());
            return -1;
        }
        return 0;
    }

    private void cleanup() throws java.sql.SQLException {
        if (this.rs != null) { this.rs.close(); }
        if (this.st != null) { this.st.close(); }
        if (this.connection != null) { this.connection.close(); }
    }
}