package org.example.test.query;

import org.example.test.DBConnector;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.ResultSet;

import static org.junit.Assert.assertEquals;

public class Query2Test {

    private DBConnector dbConnector;

/*

SELECT Medikament_id,
	(SELECT SUM(lineprice)
		FROM (
		WITH RECURSIVE cnt(m, x, y, z) AS (
			VALUES(0, 0, 10, 0)
			UNION ALL
			SELECT (
				SELECT MAX(Anzahl) FROM Staffelpreis WHERE Medikament_id = mm.Medikament_id AND EinVerkauf = 'Verkauf' AND Anzahl < y) as m,
				y / (SELECT MAX(Anzahl) FROM Staffelpreis WHERE Medikament_id = mm.Medikament_id AND EinVerkauf = 'Verkauf' AND Anzahl < y),
				y % (SELECT MAX(Anzahl) FROM Staffelpreis WHERE Medikament_id = mm.Medikament_id AND EinVerkauf = 'Verkauf' AND Anzahl < y),
				(SELECT Preis FROM Staffelpreis WHERE Medikament_id = mm.Medikament_id AND EinVerkauf = 'Verkauf' AND Anzahl = (
					SELECT MAX(Anzahl) FROM Staffelpreis WHERE Medikament_id = mm.Medikament_id AND EinVerkauf = 'Verkauf' AND Anzahl < y )
				)
			FROM cnt
			WHERE  y > 0)
		SELECT y, x, m, z, (x * m * z) as lineprice
			FROM cnt )) as vp,
	(SELECT SUM(lineprice)
		FROM (
		WITH RECURSIVE cnt(m, x, y, z) AS (
			VALUES(0, 0, 10, 0)
			UNION ALL
			SELECT (
				SELECT MAX(Anzahl) FROM Staffelpreis WHERE Medikament_id = mm.Medikament_id AND EinVerkauf = 'Einkauf' AND Anzahl < y) as m,
				y / (SELECT MAX(Anzahl) FROM Staffelpreis WHERE Medikament_id = mm.Medikament_id AND EinVerkauf = 'Einkauf' AND Anzahl < y),
				y % (SELECT MAX(Anzahl) FROM Staffelpreis WHERE Medikament_id = mm.Medikament_id AND EinVerkauf = 'Einkauf' AND Anzahl < y),
				(SELECT Preis FROM Staffelpreis WHERE Medikament_id = mm.Medikament_id AND EinVerkauf = 'Einkauf' AND Anzahl = (
					SELECT MAX(Anzahl) FROM Staffelpreis WHERE Medikament_id = mm.Medikament_id AND EinVerkauf = 'Einkauf' AND Anzahl < y )
				)
			FROM cnt
			WHERE  y > 0)
		SELECT y, x, m, z, (x * m * z) as lineprice
		FROM cnt )) as ep
	FROM Medikament mm
	WHERE (vp BETWEEN 10 AND 15
		OR vp BETWEEN 10 AND 15);

* */


    private String querySql2 = "SELECT MAX(Anzahl) " +
            "FROM Staffelpreis " +
            "WHERE Medikament_id = 23 " +
            "AND EinVerkauf = 'Verkauf'" +
            "AND Anzahl < 7 " +
            ";";

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
        ResultSet rs = dbConnector.executeQueryFromFile("query2.sql");


        while (rs.next()) {
            count++;
            System.out.println("Result: " + rs.getInt(1) + " " + rs.getDouble(2) + " " + rs.getDouble(3));

        }

        System.out.println(count + " records found");
        assertEquals("One Medikament only", 1, count);
    }
}
