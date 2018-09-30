package org.example.test.create;

import org.example.test.DBConnector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StaffelpreisTest {

    private DBConnector dbConnector;

    private String createTableSql =
            "CREATE TABLE Staffelpreis (" +
                    "Staffelpreis_id   INTEGER     NOT NULL CHECK (Staffelpreis_id >= 0),"+
                    "Medikament_id     INTEGER     NOT NULL,"+
                    "Anzahl            INTEGER     NOT NULL CHECK (Anzahl > 0),"+
                    "EinVerkauf        VARCHAR(50) NOT NULL CHECK (EinVerkauf IN ('Einkauf', 'Verkauf')),"+
                    "Preis             DOUBLE      NOT NULL, "+
                    "PRIMARY KEY (Staffelpreis_id),"+
                    "FOREIGN KEY (Medikament_id) REFERENCES Medikament (Medikament_id)"+
                    ");";

    @Before
    public void setUp() throws Exception {
        dbConnector = new DBConnector();
        dbConnector.openConnection();
        dbConnector.executeSql(createTableSql);

        createRelatedTables();
    }

    @After
    public void tierDown() throws Exception {
        dbConnector.closeConnection();
    }

    //round(Preis, 2)
    @Test
    public void insert_correct_success() throws Exception {
        String insertSql = "INSERT INTO Staffelpreis VALUES (1, 1, 5, 'Einkauf', 123.50);";

        dbConnector.executeSql(insertSql);
    }

    @Test

    public void insert_FirstPartPriceEmpty_failed() throws Exception {
        String insertSql = "INSERT INTO Staffelpreis VALUES (1, 1, 5, 'Einkauf', .45);";

        dbConnector.executeSql(insertSql);
    }

    @Test //(expected = SQLiteException.class)
    public void insert_LastPartPriceEmpty_failed() throws Exception {
        String insertSql = "INSERT INTO Staffelpreis VALUES (1, 1, 5, 'Einkauf', 123.);";

        dbConnector.executeSql(insertSql);
    }

    @Test //(expected = SQLiteException.class)
    public void insert_NoLastPartPrice_failed() throws Exception {
        String insertSql = "INSERT INTO Staffelpreis VALUES (1, 1, 5, 'Einkauf', 123);";

        dbConnector.executeSql(insertSql);
    }


    @Test //(expected = SQLiteException.class)
    public void insert_OnePositionLastPartPrice_failed() throws Exception {
        String insertSql = "INSERT INTO Staffelpreis VALUES (1, 1, 5, 'Einkauf', 123.4);";

        dbConnector.executeSql(insertSql);
    }

    @Test //(expected = SQLiteException.class)
    public void insert_ThreePositionLastPartPrice_failed() throws Exception {
        String insertSql = "INSERT INTO Staffelpreis VALUES (1, 1, 5, 'Einkauf', 123.456);";

        dbConnector.executeSql(insertSql);
    }

    private void createRelatedTables() throws Exception {
        String createTableSql =
                "CREATE TABLE Medikament (" +
                        "Medikament_id   INTEGER       NOT NULL CHECK (Medikament_id >= 0),"+
                        "Produktname     VARCHAR(50)   NOT NULL,"+
                        "Kennzeichnung   VARCHAR(50) COLLATE NOCASE "+
                        "CHECK (Kennzeichnung in ('freiverkäuflich', 'verschreibungspflichtig')),"+
                        "PRIMARY KEY (Medikament_id)"+
                        ");";
        String insertSql = "INSERT INTO Medikament VALUES (1, 'Medikament_Name', 'freiverkäuflich');";

        dbConnector.executeSql(createTableSql);
        dbConnector.executeSql(insertSql);
    }

}
