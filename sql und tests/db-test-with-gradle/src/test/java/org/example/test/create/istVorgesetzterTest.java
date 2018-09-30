package org.example.test.create;

import org.example.test.DBConnector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sqlite.SQLiteException;

public class istVorgesetzterTest {

    private DBConnector dbConnector;

    private String createTableSql =
            "CREATE TABLE istVorgesetzter (" +
                    "Vorgesetzter INTEGER NOT NULL," +
                    "Untergebener INTEGER NOT NULL," +
                    "PRIMARY KEY (Vorgesetzter, Untergebener)," +
                    "FOREIGN KEY (Vorgesetzter) REFERENCES Mitarbeiter (Mitarbeiter_id)," +
                    "FOREIGN KEY (Untergebener) REFERENCES Mitarbeiter (Mitarbeiter_id)" +
                    ")";

    @Before
    public void setUp() throws Exception {
        dbConnector = new DBConnector();
        dbConnector.openConnection();
        dbConnector.executeSql(createTableSql);

        addRelatedData();
    }

    @After
    public void tierDown() throws Exception {
        dbConnector.closeConnection();
    }

    @Test
    public void insert_correct_success() throws Exception {
        String insertSql = "INSERT INTO istVorgesetzter VALUES (1, 2);";

        dbConnector.executeSql(insertSql);
    }

    @Test (expected = SQLiteException.class)
    public void insert_unknownMitarbeiter_failed() throws Exception {
        String insertSql = "INSERT INTO istVorgesetzter VALUES (1, 3);";

        dbConnector.executeSql(insertSql);
    }


    private void addRelatedData() throws Exception {
        String createTableSql1 =
                "CREATE TABLE Person (" +
                        "Person_id   INTEGER      NOT NULL CHECK (Person_id >= 0),"+
                        "Vorname     VARCHAR(30)  NOT NULL CHECK (Vorname NOT GLOB '*[0-9]*'),"+
                        "Nachname    VARCHAR(30)  NOT NULL CHECK (Nachname NOT GLOB '*[0-9]*'),"+
                        "PRIMARY KEY(Person_id)"+
                        ");";
        String insertSql2 = "INSERT INTO Person VALUES (1, 'first', 'second');";
        String insertSql3 = "INSERT INTO Person VALUES (2, 'first', 'second');";

        String createTableSql4 =
                "CREATE TABLE Mitarbeiter (" +
                        "Mitarbeiter_id  INTEGER       NOT NULL,"+
                        "Mitarbeiter_nr  INTEGER       NOT NULL CHECK (Mitarbeiter_nr > 0),"+
                        "Passwort        VARCHAR(30)   NOT NULL CHECK(length(Passwort) > 0),"+
                        "PRIMARY KEY (Mitarbeiter_id),"+
                        "FOREIGN KEY (Mitarbeiter_id) REFERENCES Person (Person_id)"+
                        ");";
        String insertSql5 = "INSERT INTO Mitarbeiter VALUES (1, 123456, 'password');";
        String insertSql6 = "INSERT INTO Mitarbeiter VALUES (2, 123457, 'password');";

        dbConnector.executeSql(createTableSql1);
        dbConnector.executeSql(insertSql2);
        dbConnector.executeSql(insertSql3);

        dbConnector.executeSql(createTableSql4);
        dbConnector.executeSql(insertSql5);
        dbConnector.executeSql(insertSql6);
    }

}
