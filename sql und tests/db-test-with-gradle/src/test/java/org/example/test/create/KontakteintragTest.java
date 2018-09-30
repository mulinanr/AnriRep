package org.example.test.create;

import org.example.test.DBConnector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sqlite.SQLiteException;

public class KontakteintragTest {

    private DBConnector dbConnector;

    private String createTableSql =
            "CREATE TABLE Kontakteintrag (" +
                    "TelefonNr   INTEGER   NOT NULL CHECK (TelefonNr > 0),"+
                    "addr_id     INTEGER   NOT NULL,"+
                    "EMail       VARCHAR(200) CHECK ((length(EMail) > 0) " +
                        "AND (EMail GLOB '?*@?*.?*') " +
                        "AND (EMail NOT GLOB '?*@?*.*[0-9]*')),"+
                    "Fax         INTEGER CHECK (Fax >= 0),"+
                    "PRIMARY KEY (TelefonNr),"+
                    "FOREIGN KEY (addr_id) REFERENCES Adresse (addr_id)"+
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
        String insertSql = "INSERT INTO Kontakteintrag VALUES (1, 2, 'aaa@bbb.cc', 3);";

        dbConnector.executeSql(insertSql);
    }

    @Test (expected = SQLiteException.class)
    public void insert_firstEmailPartEmpty_failed() throws Exception {
        String insertSql = "INSERT INTO Kontakteintrag VALUES (1, 2, '@bbb.cc', 3);";

        dbConnector.executeSql(insertSql);
    }

    @Test (expected = SQLiteException.class)
    public void insert_secondEmailPartEmpty_failed() throws Exception {
        String insertSql = "INSERT INTO Kontakteintrag VALUES (1, 2, 'aaa@.cc', 3);";

        dbConnector.executeSql(insertSql);
    }

    @Test (expected = SQLiteException.class)
    public void insert_thirdEmailPartEmpty_failed() throws Exception {
        String insertSql = "INSERT INTO Kontakteintrag VALUES (1, 2, 'aaa@bbb.', 3);";

        dbConnector.executeSql(insertSql);
    }

    @Test (expected = SQLiteException.class)
    public void insert_noThirdEmailPart_failed() throws Exception {
        String insertSql = "INSERT INTO Kontakteintrag VALUES (1, 2, 'aaa@bbb', 3);";

        dbConnector.executeSql(insertSql);
    }

    private void createRelatedTables() throws Exception {
        String createTableSql =
                "CREATE TABLE Adresse (" +
                        "addr_id   INTEGER       NOT NULL CHECK (addr_id >= 0), "+
                        "Strasse   VARCHAR(200)  NOT NULL CHECK (Strasse NOT GLOB '*[0-9]*'),"+
                        "Hausnr    INTEGER       NOT NULL CHECK (Hausnr >= 0), "+
                        "PLZ       VARCHAR(10)   NOT NULL CHECK (length(PLZ) > 0), "+
                        "Ort       VARCHAR(50)   NOT NULL CHECK (Ort NOT GLOB '*[0-9]*'),"+
                        "PRIMARY KEY (addr_id) "+
                        ");";
        String insertSql = "INSERT INTO Adresse VALUES (2, 'str', 2, '11111', 'Ort');";
        dbConnector.executeSql(createTableSql);
        dbConnector.executeSql(insertSql);
    }

}
