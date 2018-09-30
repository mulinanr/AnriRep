package org.example.test.create;

import org.example.test.DBConnector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sqlite.SQLiteException;

public class AuftragTest {

    private DBConnector dbConnector;

    private String createTableSql =
            "CREATE TABLE Auftrag (" +
                    "Auftrag_id        INTEGER     NOT NULL CHECK (Auftrag_id >= 0),"+
                    "Konto_id          INTEGER     NOT NULL,"+
                    "Mitarbeiter_id    INTEGER     NOT NULL,"+
                    "EinVerkauf        VARCHAR(50) NOT NULL CHECK (EinVerkauf IN ('Einkauf', 'Verkauf')),"+
                    "Erstellungsdatum  DATE        NOT NULL DEFAULT (date('now','localtime')),"+
                    "Liefertermin      DATE DEFAULT (date('now', '+7 days')),"+
                    "PRIMARY KEY (Auftrag_id),"+
                    "FOREIGN KEY (Mitarbeiter_id) REFERENCES Mitarbeiter (Mitarbeiter_id),"+
                    "FOREIGN KEY (Konto_id) REFERENCES Konto (Konto_id)"+
                    ");";

    private String createTriggerSql =
            "CREATE TRIGGER auftrag_trig AFTER insert ON Auftrag "+
                    "BEGIN "+
                        "update Auftrag SET Erstellungsdatum = datetime('now','localtime') WHERE Auftrag_id = NEW.Auftrag_id; "+
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
    public void insert_correctEinkauf_success() throws Exception {
        String insertSql = "INSERT INTO Auftrag VALUES (1, 1, 1, 'Einkauf', date('now','-7 days'), date('now','-5 days'));";

        dbConnector.executeSql(insertSql);
    }

    @Test
    public void insert_correctVerkauf_success() throws Exception {
        String insertSql = "INSERT INTO Auftrag VALUES (1, 1, 1, 'Verkauf', date('now','-7 days'), date('now','-5 days'));";

        dbConnector.executeSql(insertSql);
    }

    @Test (expected = SQLiteException.class)
    public void insert_incorrectEinVerkauf_failed() throws Exception {
        String insertSql = "INSERT INTO Auftrag VALUES (1, 1, 1, 'Geschenk', date('now','-7 days'), date('now','-5 days'));";

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
                "CREATE TABLE Kunde (" +
                        "Kunde_id      INTEGER     NOT NULL CHECK (Kunde_id >= 0),"+
                        "Bezeichnung   VARCHAR(30) NOT NULL,"+
                        "ApothekenSchein BLOB,"+
                        "PRIMARY KEY (Kunde_id)"+
                        ");";
        String insertSql6 = "INSERT INTO Kunde VALUES (1, 'Kunde1', null);";

        String createTableSql7 =
                "CREATE TABLE Konto (" +
                        "Konto_id      INTEGER     NOT NULL CHECK (Konto_id >= 0),"+
                        "Kunde_id      INTEGER     NOT NULL,"+
                        "Bezeichnung   VARCHAR(30) NOT NULL,"+
                        "Zahlungsziel  VARCHAR(3) NOT NULL "+
                        "CHECK((substr(Zahlungsziel,-1) in ('t', 'T'))"+
                        "AND (" +
                        "               ((length(Zahlungsziel) == 2) " +
                        "               AND (CAST(substr(Zahlungsziel, 1, 3) AS INTEGER) > 0))"+
                        "OR ((length(Zahlungsziel) == 3) " +
                        "               AND (CAST(substr(Zahlungsziel, 1, 3) AS INTEGER) > 10) " +
                        "               AND (CAST(substr(Zahlungsziel, 1, 3) AS INTEGER) < 31)))" +
                        "           ),"+
                        "Zahlungsart   VARCHAR(30) NOT NULL,"+
                        "PRIMARY KEY (Konto_id),"+
                        "FOREIGN KEY (Kunde_id) REFERENCES Kunde( Kunde_id)"+
                        ");";
        String insertSql8 = "INSERT INTO Konto VALUES (1, 1, 'Konto1', '5t', 'sepa');";

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
