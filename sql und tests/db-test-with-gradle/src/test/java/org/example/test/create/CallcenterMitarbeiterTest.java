package org.example.test.create;

import org.example.test.DBConnector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sqlite.SQLiteException;

public class CallcenterMitarbeiterTest {

    private DBConnector dbConnector;

    private String createTableSql =
            "CREATE TABLE CallcenterMitarbeiter (" +
                    "CallcenterMitarbeiter_id  INTEGER     NOT NULL,"+
                    "Sprachzertifikat          VARCHAR(30) NOT NULL  CHECK (Sprachzertifikat NOT GLOB '*[0-9]*'),"+
                    "PRIMARY KEY (CallcenterMitarbeiter_id),"+
                    "FOREIGN KEY (CallcenterMitarbeiter_id) REFERENCES Mitarbeiter (Mitarbeiter_id)"+
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
        String insertSql = "INSERT INTO CallcenterMitarbeiter VALUES (1, 'language');";

        dbConnector.executeSql(insertSql);
    }

    @Test (expected = SQLiteException.class)
    public void insert_incorrectLanguage_failed() throws Exception {
        String insertSql = "INSERT INTO CallcenterMitarbeiter VALUES (1, 'language1');";

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

        dbConnector.executeSql(createTableSql1);
        dbConnector.executeSql(insertSql2);

        dbConnector.executeSql(createTableSql3);
        dbConnector.executeSql(insertSql4);

    }
}
