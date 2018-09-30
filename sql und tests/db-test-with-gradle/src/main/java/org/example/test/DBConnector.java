package org.example.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;

public class DBConnector {

    static final String JDBC_DRIVER = "org.sqlite.JDBC";
    static final String DB_URL = "jdbc:sqlite::memory:";  // use to test in memory
    //static final String DB_URL = "jdbc:sqlite:pharma.db";  // use to crete db file
    static final String USER = "username";
    static final String PASS = "password";

    private Connection conn = null;
    private Statement stmt = null;

    public void openConnection() throws Exception {

        Class.forName(JDBC_DRIVER);
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
        stmt = conn.createStatement();
        stmt.execute("PRAGMA foreign_keys = ON;");  // to switch Foreign Key constraints on
        System.out.println("Connection to DB opened");

    }

    public void executeSql(String sql) throws Exception {
        stmt.execute(sql);
        System.out.println("SQL is executed: " + sql);
    }

    public ResultSet executeQuery(String sql) throws Exception {
        ResultSet rs = stmt.executeQuery(sql);
        System.out.println("SQL is executed: " + sql);

        return rs;
    }

    public void closeConnection() {

        try {
            if (stmt != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Connection to DB close");
    }

    public ResultSet executeQueryFromFile(String sql) throws Exception {

        StringBuffer sb = readFile(sql);

        ResultSet rs = stmt.executeQuery(sb.toString());
        System.out.println("SQL is executed: " + sb.toString());

        return rs;
    }

    public void initDatabase(String sql, String delimiter) throws Exception {

        StringBuffer sb = readFile(sql);
        String[] inst = sb.toString().split(delimiter);

        for(int i = 0; i<inst.length; i++) {
            if(!inst[i].trim().equals("")) {
                stmt.executeUpdate(inst[i]);
                System.out.println(">> "+inst[i]);
            }
        }

    }

    private StringBuffer readFile(String filename) throws Exception {
        String s = new String();
        StringBuffer sb = new StringBuffer();

        try (FileReader fr = new FileReader(new File(filename));
             BufferedReader br = new BufferedReader(fr);
        ) {

            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
        }

        return sb;
    }

}
