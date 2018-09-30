package org.example.test.query;

import org.example.test.DBConnector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;

import static org.junit.Assert.assertEquals;

public class Query4Test {

    private DBConnector dbConnector;

/*

SELECT Konto.Konto_id
FROM Auftrag
JOIN Konto ON Auftrag.Konto_id = Konto.Konto_id
ORDER BY Erstellungsdatum ASC
LIMIT 1;

* */

    private String querySql = "SELECT Konto.Konto_id " +
            "FROM Auftrag " +
            "JOIN Konto ON Auftrag.Konto_id = Konto.Konto_id " +
            "ORDER BY Erstellungsdatum ASC " +
            "LIMIT 1;";

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
        ResultSet rs = dbConnector.executeQuery(querySql);


        while (rs.next()) {
            count++;
            System.out.println("Konto_id: " + rs.getInt(1));
        }

        System.out.println(count + " records found");
        assertEquals("One Konto only", 1, count);
    }
}
