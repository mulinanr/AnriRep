package org.example.test.create;

import org.example.test.DBConnector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AnsprechpartnerTest {

    private DBConnector dbConnector;

    private String createTableSql =
            "CREATE TABLE Ansprechpartner (" +
                    "Ansprechpartner_id  INTEGER       NOT NULL CHECK (Ansprechpartner_id >= 0), " +
                    "Kunde_id            INTEGER       NOT NULL," +
                    "Fachabteilung       VARCHAR(30)   NOT NULL," +
                    "PRIMARY KEY (Ansprechpartner_id),"+
                    "FOREIGN KEY (Ansprechpartner_id) REFERENCES Person (Person_id),"+
                    "FOREIGN KEY (Kunde_id) REFERENCES Kunde (Kunde_id)"+
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

    @Test
    public void insert_correct_success() throws Exception {
        String insertSql = "INSERT INTO Ansprechpartner VALUES (1, 1, 'abteilung1');";

        dbConnector.executeSql(insertSql);
    }


    private void createRelatedTables() throws Exception {
        String createTableSql1 =
                "CREATE TABLE Kunde (" +
                        "Kunde_id      INTEGER     NOT NULL CHECK (Kunde_id >= 0),"+
                        "Bezeichnung   VARCHAR(30) NOT NULL,"+
                        "ApothekenSchein BLOB,"+
                        "PRIMARY KEY (Kunde_id)"+
                        ");";
        String insertSql2 = "INSERT INTO Kunde VALUES (1, 'first', 0x0123456789ABCDEF);";

        String createTableSql3 =
                "CREATE TABLE Person (" +
                        "Person_id   INTEGER      NOT NULL CHECK (Person_id >= 0),"+
                        "Vorname     VARCHAR(30)  NOT NULL CHECK (Vorname NOT GLOB '*[0-9]*'),"+
                        "Nachname    VARCHAR(30)  NOT NULL CHECK (Nachname NOT GLOB '*[0-9]*'),"+
                        "PRIMARY KEY(Person_id)"+
                        ");";
        String insertSql4 = "INSERT INTO Person VALUES (1, 'first', 'second');";

        dbConnector.executeSql(createTableSql1);
        dbConnector.executeSql(insertSql2);
        dbConnector.executeSql(createTableSql3);
        dbConnector.executeSql(insertSql4);
    }

}
