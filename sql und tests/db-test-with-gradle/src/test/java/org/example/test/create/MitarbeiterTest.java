package org.example.test.create;

import org.example.test.DBConnector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sqlite.SQLiteException;

public class MitarbeiterTest {

    private DBConnector dbConnector;

    private String createTableSql =
            "CREATE TABLE Mitarbeiter (" +
                    "Mitarbeiter_id  INTEGER       NOT NULL,"+
                    "Mitarbeiter_nr  INTEGER       NOT NULL UNIQUE CHECK (Mitarbeiter_nr > 0),"+
                    "Passwort        VARCHAR(30)   NOT NULL CHECK (length(Passwort) > 0),"+
                    "PRIMARY KEY (Mitarbeiter_id),"+
                    "FOREIGN KEY (Mitarbeiter_id) REFERENCES Person (Person_id)"+
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
        String insertSql = "INSERT INTO Mitarbeiter VALUES (1, 123456, 'password');";

        dbConnector.executeSql(insertSql);
    }

    @Test (expected = SQLiteException.class)
    public void insert_mitarbeiterNummerIs0_failed() throws Exception {
        String insertSql = "INSERT INTO Mitarbeiter VALUES (1, 0, 'password');";

        dbConnector.executeSql(insertSql);
    }

    @Test (expected = SQLiteException.class)
    public void insert_passwordIsEmpty_failed() throws Exception {
        String insertSql = "INSERT INTO Mitarbeiter VALUES (1, 123456, '');";

        dbConnector.executeSql(insertSql);
    }

    @Test (expected = SQLiteException.class)
    public void updatet_passwordIsEmpty_failed() throws Exception {
        String insertSql = "INSERT INTO Mitarbeiter VALUES (1, 123456, 'password');";
        String updateSql = "UPDATE Mitarbeiter SET Passwort = '' WHERE Mitarbeiter_id;";

        dbConnector.executeSql(insertSql);
        dbConnector.executeSql(updateSql);
    }

    @Test (expected = SQLiteException.class)
    public void insert_mitarbeiterNummerIsNotUnique_failed() throws Exception {
        String insertSql1 = "INSERT INTO Mitarbeiter VALUES (1, 123456, 'password');";
        String insertSql2 = "INSERT INTO Mitarbeiter VALUES (2, 123456, 'password');";

        dbConnector.executeSql(insertSql1);
        dbConnector.executeSql(insertSql2);
    }

    private void createRelatedTables() throws Exception {
        String createTableSql =
                "CREATE TABLE Person (" +
                        "Person_id   INTEGER      NOT NULL CHECK (Person_id >= 0),"+
                        "Vorname     VARCHAR(30)  NOT NULL CHECK (Vorname NOT GLOB '*[0-9]*'),"+
                        "Nachname    VARCHAR(30)  NOT NULL CHECK (Nachname NOT GLOB '*[0-9]*'),"+
                        "PRIMARY KEY(Person_id)"+
                        ");";
        String insertSql1 = "INSERT INTO Person VALUES (1, 'first', 'second');";
        String insertSql2 = "INSERT INTO Person VALUES (2, 'first', 'second');";
        dbConnector.executeSql(createTableSql);
        dbConnector.executeSql(insertSql1);
        dbConnector.executeSql(insertSql2);
    }



}
