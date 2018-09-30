package org.example.test.create;

import org.example.test.DBConnector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MedikamentTest {

    private DBConnector dbConnector;

    private String createTableSql =
            "CREATE TABLE Medikament (" +
                    "Medikament_id   INTEGER       NOT NULL CHECK (Medikament_id >= 0),"+
                    "Produktname     VARCHAR(50)   NOT NULL,"+
                    "Kennzeichnung   VARCHAR(50) COLLATE NOCASE "+
                        "CHECK (Kennzeichnung in ('freiverkäuflich', 'verschreibungspflichtig')),"+
                    "PRIMARY KEY (Medikament_id)"+
                    ");";

    @Before
    public void setUp() throws Exception {
        dbConnector = new DBConnector();
        dbConnector.openConnection();
        dbConnector.executeSql(createTableSql);
    }

    @After
    public void tierDown() throws Exception {
        dbConnector.closeConnection();
    }

    @Test
    public void insert_correctUmlaut_success() throws Exception {
        String insertSql = "INSERT INTO Medikament VALUES (1, 'Medikament_Name', 'freiverkäuflich');";

        dbConnector.executeSql(insertSql);
    }

    @Test
    public void insert_correctCapitalLetters_success() throws Exception {
        String insertSql = "INSERT INTO Medikament VALUES (1, 'Medikament_Name', 'verschreIBungspflichtig');";

        dbConnector.executeSql(insertSql);
    }

}
