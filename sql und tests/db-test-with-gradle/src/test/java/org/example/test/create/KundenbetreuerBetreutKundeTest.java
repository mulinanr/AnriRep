package org.example.test.create;

import org.example.test.DBConnector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class KundenbetreuerBetreutKundeTest {

    private DBConnector dbConnector;

    private String createTableSql =
            "CREATE TABLE KundenbetreuerBetreutKunde (" +
                    "Kunde_id            INTEGER   NOT NULL,"+
                    "Kundenbetreuer_id   INTEGER   NOT NULL,"+
                    "PRIMARY KEY (Kunde_id, Kundenbetreuer_id),"+
                    "FOREIGN KEY (Kunde_id)          REFERENCES Kunde (Kunde_id),"+
                    "FOREIGN KEY (Kundenbetreuer_id) REFERENCES Kundenbetreuer (Kundenbetreuer_id)"+
                    ");";

    private String createTriggerSql =
            "CREATE TRIGGER zu_viele_kunden BEFORE INSERT ON KundenbetreuerBetreutKunde "+
                    "BEGIN "+
                    "SELECT RAISE(FAIL, \"zu_viele_kunden\") "+
                    "FROM (SELECT count(*) AS c "+
                        "FROM KundenbetreuerBetreutKunde "+
                        "WHERE Kundenbetreuer_id = NEW.Kundenbetreuer_id) "+
                    "WHERE c > 2; "+
                    "END;";

    @Before
    public void setUp() throws Exception {
        dbConnector = new DBConnector();
        dbConnector.openConnection();
        dbConnector.executeSql(createTableSql);
        dbConnector.executeSql(createTriggerSql);

        createRelatedTables();
    }

    @After
    public void tierDown() throws Exception {
        dbConnector.closeConnection();
    }

    @Test
    public void insert_correct_success() throws Exception {
        String insertSql = "INSERT INTO KundenbetreuerBetreutKunde VALUES (1, 1);";

        dbConnector.executeSql(insertSql);
    }

    private void createRelatedTables() throws Exception {
        String createTableSql1 =
                "CREATE TABLE Person (" +
                        "Person_id   INTEGER      NOT NULL CHECK (Person_id >= 0)," +
                        "Vorname     VARCHAR(30)  NOT NULL CHECK (Vorname NOT GLOB '*[0-9]*')," +
                        "Nachname    VARCHAR(30)  NOT NULL CHECK (Nachname NOT GLOB '*[0-9]*')," +
                        "PRIMARY KEY(Person_id)" +
                        ");";
        String insertSql2 = "INSERT INTO Person VALUES (1, 'first', 'second');";

        String createTableSql3 =
                "CREATE TABLE Mitarbeiter (" +
                        "Mitarbeiter_id  INTEGER       NOT NULL," +
                        "Mitarbeiter_nr  INTEGER       NOT NULL UNIQUE CHECK (Mitarbeiter_nr > 0)," +
                        "Passwort        VARCHAR(30)   NOT NULL CHECK (length(Passwort) > 0)," +
                        "PRIMARY KEY (Mitarbeiter_id)," +
                        "FOREIGN KEY (Mitarbeiter_id) REFERENCES Person (Person_id)" +
                        ");";
        String insertSql4 = "INSERT INTO Mitarbeiter VALUES (1, 111111, 'password');";

        String createTableSql5 =
                "CREATE TABLE Kundenbetreuer (" +
                        "Kundenbetreuer_id   INTEGER     NOT NULL," +
                        "Fachgebiet          VARCHAR(30) NOT NULL," +
                        "Bezirk              VARCHAR(30) NOT NULL CHECK(length(Bezirk) >= 0)," +
                        "PRIMARY KEY (Kundenbetreuer_id)," +
                        "FOREIGN KEY (Kundenbetreuer_id) REFERENCES Mitarbeiter (Mitarbeiter_id)" +
                        ");";
        String insertSql6 = "INSERT INTO Kundenbetreuer VALUES (1, 'fach1', 'b1d');";

        String createTableSql7 =
                "CREATE TABLE Kunde (" +
                        "Kunde_id      INTEGER     NOT NULL CHECK (Kunde_id >= 0),"+
                        "Bezeichnung   VARCHAR(30) NOT NULL,"+
                        "ApothekenSchein BLOB,"+
                        "PRIMARY KEY (Kunde_id)"+
                        ");";
        String insertSql8 = "INSERT INTO Kunde VALUES (1, 'first', 0x0123456789ABCDEF);";

        dbConnector.executeSql(createTableSql1);
        dbConnector.executeSql(insertSql2);

        dbConnector.executeSql(createTableSql3);
        dbConnector.executeSql(insertSql4);

        dbConnector.executeSql(createTableSql5);
        dbConnector.executeSql(insertSql6);

        dbConnector.executeSql(createTableSql7);
        dbConnector.executeSql(insertSql8);
    }


}
