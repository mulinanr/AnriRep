package org.example.test.create;

import org.example.test.DBConnector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sqlite.SQLiteException;

public class PersonTest {

    private DBConnector dbConnector;

    private String createTableSql =
    "CREATE TABLE Person (" +
            "Person_id   INTEGER      NOT NULL CHECK (Person_id >= 0),"+
            "Vorname     VARCHAR(30)  NOT NULL CHECK (Vorname NOT GLOB '*[0-9]*'),"+
            "Nachname    VARCHAR(30)  NOT NULL CHECK (Nachname NOT GLOB '*[0-9]*'),"+
            "PRIMARY KEY(Person_id)"+
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
    public void insert_correct_success() throws Exception {
        String insertSql = "INSERT INTO Person VALUES (1, 'first', 'second');";

        dbConnector.executeSql(insertSql);
    }

    @Test (expected = SQLiteException.class)
    public void insert_incorrectFirstName_failed() throws Exception {
        String insertSql = "INSERT INTO Person VALUES (1, 'first1', 'second');";

        dbConnector.executeSql(insertSql);
    }

    @Test (expected = SQLiteException.class)
    public void insert_incorrectSecondName_failed() throws Exception {
        String insertSql = "INSERT INTO Person VALUES (1, 'first', 'second2');";

        dbConnector.executeSql(insertSql);
    }

    @Test (expected = SQLiteException.class)
    public void insertAndUpdate_incorrectFirstName_failed() throws Exception {
        String insertSql = "INSERT INTO Person VALUES (1, 'first', 'second');";
        String updateSql = "UPDATE Person SET  Vorname = 'name1' WHERE Person_id = 1;";

        dbConnector.executeSql(insertSql);
        dbConnector.executeSql(updateSql);
    }

}