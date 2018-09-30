package org.example.test.create;

import org.example.test.DBConnector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sqlite.SQLiteException;

public class PersonHatKontakteintragTest {

    private DBConnector dbConnector;

    private String createTableSql =
            "CREATE TABLE PersonHatKontakteintrag (" +
                    "TelefonNr INTEGER NOT NULL,"+
                    "Person_id INTEGER  NOT NULL,"+
                    "PRIMARY KEY (TelefonNr),"+
                    "FOREIGN KEY (TelefonNr) REFERENCES Kontakteintrag (TelefonNr),"+
                    "FOREIGN KEY (Person_id) REFERENCES Person (Person_id)"+
                    ");";

    private String createTriggerSql =
            "CREATE TRIGGER person_kontakt_trig AFTER insert ON PersonHatKontakteintrag "+
                    "BEGIN "+
                    "SELECT RAISE(FAIL, \"Kontakteintrag schon gemacht\") "+
                    "FROM KundeHatKontakteintrag "+
                    "WHERE TelefonNr = NEW.TelefonNr; "+
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
        String insertSql = "INSERT INTO PersonHatKontakteintrag VALUES (2, 1);";

        dbConnector.executeSql(insertSql);
    }

    @Test (expected = SQLiteException.class)
    public void insert_kontaktEintragAlreadyUsed_failed() throws Exception {
        String insertSql = "INSERT INTO PersonHatKontakteintrag VALUES (1, 1);";

        dbConnector.executeSql(insertSql);
    }

    private void createRelatedTables() throws Exception {
        String createTableSql1 =
                "CREATE TABLE Adresse (" +
                        "addr_id   INTEGER       NOT NULL CHECK (addr_id >= 0), "+
                        "Strasse   VARCHAR(200)  NOT NULL CHECK (Strasse NOT GLOB '*[0-9]*'),"+
                        "Hausnr    INTEGER       NOT NULL CHECK (Hausnr >= 0), "+
                        "PLZ       VARCHAR(10)   NOT NULL CHECK (length(PLZ) > 0), "+
                        "Ort       VARCHAR(50)   NOT NULL CHECK (Ort NOT GLOB '*[0-9]*'),"+
                        "PRIMARY KEY (addr_id) "+
                        ");";
        String insertSql2 = "INSERT INTO Adresse VALUES (2, 'str', 2, '11111', 'Ort');";

        String createTableSql3 =
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
        String insertSql4 = "INSERT INTO Kontakteintrag VALUES (1, 2, 'aaa@bbb.cc', 3);";
        String insertSql4a = "INSERT INTO Kontakteintrag VALUES (2, 2, 'aaa@bbb.cc', 3);";

        String createTableSql5 =
                "CREATE TABLE Kunde (" +
                        "Kunde_id      INTEGER     NOT NULL CHECK (Kunde_id >= 0),"+
                        "Bezeichnung   VARCHAR(30) NOT NULL,"+
                        "ApothekenSchein BLOB,"+
                        "PRIMARY KEY (Kunde_id)"+
                        ");";
        String insertSql6 = "INSERT INTO Kunde VALUES (1, 'first', 0x0123456789ABCDEF);";

        String createTableSql7 =
                "CREATE TABLE Person (" +
                        "Person_id   INTEGER      NOT NULL CHECK (Person_id >= 0),"+
                        "Vorname     VARCHAR(30)  NOT NULL CHECK (Vorname NOT GLOB '*[0-9]*'),"+
                        "Nachname    VARCHAR(30)  NOT NULL CHECK (Nachname NOT GLOB '*[0-9]*'),"+
                        "PRIMARY KEY(Person_id)"+
                        ");";
        String insertSql8 = "INSERT INTO Person VALUES (1, 'first', 'second');";

        String createTableSql9 =
                "CREATE TABLE KundeHatKontakteintrag (" +
                        "TelefonNr   INTEGER   NOT NULL,"+
                        "Kunde_id    INTEGER   NOT NULL,"+
                        "PRIMARY KEY (TelefonNr),"+
                        "FOREIGN KEY (TelefonNr) REFERENCES Kontakteintrag (TelefonNr),"+
                        "FOREIGN KEY (Kunde_id) REFERENCES Kunde (Kunde_id)"+
                        ");";
        String insertSql10 = "INSERT INTO KundeHatKontakteintrag VALUES (1, 1);";



        dbConnector.executeSql(createTableSql1);
        dbConnector.executeSql(insertSql2);

        dbConnector.executeSql(createTableSql3);
        dbConnector.executeSql(insertSql4);
        dbConnector.executeSql(insertSql4a);

        dbConnector.executeSql(createTableSql5);
        dbConnector.executeSql(insertSql6);

        dbConnector.executeSql(createTableSql7);
        dbConnector.executeSql(insertSql8);

        dbConnector.executeSql(createTableSql9);
        dbConnector.executeSql(insertSql10);
    }

}
