package org.example.test.create;

import org.example.test.DBConnector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sqlite.SQLiteException;

public class AdresseTest {

    private DBConnector dbConnector;

    private String createTableSql =
            "CREATE TABLE Adresse (" +
                    "addr_id   INTEGER       NOT NULL CHECK (addr_id >= 0), "+
                    "Strasse   VARCHAR(200)  NOT NULL CHECK (Strasse NOT GLOB '*[0-9]*'),"+
                    "Hausnr    INTEGER       NOT NULL CHECK (Hausnr >= 0), "+
                    "PLZ       VARCHAR(10)   NOT NULL CHECK (length(PLZ) > 0), "+
                    "Ort       VARCHAR(50)   NOT NULL CHECK (Ort NOT GLOB '*[0-9]*'),"+
                    "PRIMARY KEY (addr_id) "+
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
        String insertSql = "INSERT INTO Adresse VALUES (1, 'str', 2, '11111', 'Ort');";

        dbConnector.executeSql(insertSql);
    }

    @Test (expected = SQLiteException.class)
    public void insert_incorrectStrasse_failed() throws Exception {
        String insertSql = "INSERT INTO Adresse VALUES (1, 'str1', 2, '11111', 'Ort');";

        dbConnector.executeSql(insertSql);
    }

    @Test (expected = SQLiteException.class)
    public void insert_incorrectOrt_failed() throws Exception {
        String insertSql = "INSERT INTO Adresse VALUES (1, 'str', 2, '11111', 'Ort1');";

        dbConnector.executeSql(insertSql);
    }


}
