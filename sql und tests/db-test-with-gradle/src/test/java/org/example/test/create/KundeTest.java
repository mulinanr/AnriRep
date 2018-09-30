package org.example.test.create;

import org.example.test.DBConnector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class KundeTest {

    private DBConnector dbConnector;

    private String createTableSql =
            "CREATE TABLE Kunde (" +
                    "Kunde_id      INTEGER     NOT NULL CHECK (Kunde_id >= 0),"+
                    "Bezeichnung   VARCHAR(30) NOT NULL,"+
                    "ApothekenSchein BLOB,"+
                    "PRIMARY KEY (Kunde_id)"+
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
        String insertSql = "INSERT INTO Kunde VALUES (1, 'first', 0x0123456789ABCDEF);";

        dbConnector.executeSql(insertSql);
    }

}
