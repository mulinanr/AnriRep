package org.example.test.query;

import org.example.test.DBConnector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;

import static org.junit.Assert.assertEquals;

public class Query5Test {

    private DBConnector dbConnector;
/*

SELECT *
    FROM Kunde
    WHERE Kunde_id NOT IN (
            SELECT Kunde.Kunde_id
                FROM Konto
                JOIN Kunde ON Konto.Kunde_id = Kunde.Kunde_id
                WHERE Zahlungsart = 'Lastschrift'
                    OR Zahlungsart = 'Überweisung'
        );

* */


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
        ResultSet rs = dbConnector.executeQueryFromFile("query5.sql");


        while (rs.next()) {
            count++;
            System.out.println("Kunde_id: " + rs.getInt(1));
        }

        System.out.println(count + " records found");
        assertEquals("Four Kunde only", 4, count);
    }
}
