package org.example.test.query;

import org.example.test.DBConnector;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.ResultSet;

import static org.junit.Assert.assertEquals;

public class Query3Test {

    private DBConnector dbConnector;

    private String querySql = "SELECT * FROM Person;";

    @Before
    public void setUp() throws Exception {
        dbConnector = new DBConnector();
        dbConnector.openConnection();

        dbConnector.initDatabase("create.sql", "----");
        dbConnector.initDatabase("insert.sql", ";");
    }

    @After
    public void tierDown() throws Exception {
        dbConnector.closeConnection();
    }

    @Test
    public void query_resultAsExpected_success() throws Exception {
        int count = 0;
        ResultSet rs = dbConnector.executeQueryFromFile("query3.sql");


        while (rs.next()) {
            count++;
            System.out.println("Result: " +
                    rs.getInt(1) + " " +
                    rs.getInt(2) + " " +
                    rs.getDouble(3));// + " " +
        }

        System.out.println(count + " records found");
        assertEquals("One Auftrag only", 1, count);
    }
}
