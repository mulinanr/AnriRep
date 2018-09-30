package org.example.test.create;

import org.example.test.DBConnector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sqlite.SQLiteException;

public class AuftragBuchtMedikamentTest {

    private DBConnector dbConnector;

    private String createTableSql =
            "CREATE TABLE AuftragBuchtMedikament (" +
                    "Auftrag_id      INTEGER   NOT NULL,"+
                    "Medikament_id   INTEGER   NOT NULL,"+
                    "Anzahl          INTEGER   NOT NULL CHECK (Anzahl > 0),"+
                    "PRIMARY KEY (Auftrag_id, Medikament_id),"+
                    "FOREIGN KEY (Auftrag_id) REFERENCES Auftrag (Auftrag_id),"+
                    "FOREIGN KEY (Medikament_id)   REFERENCES Medikament (Medikament_id)"+
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
        String insertSql = "INSERT INTO AuftragBuchtMedikament VALUES (1, 1, 10);";

        dbConnector.executeSql(insertSql);
    }

    @Test (expected = SQLiteException.class)
    public void insert_negativeAmount_failed() throws Exception {
        String insertSql = "INSERT INTO AuftragBuchtMedikament VALUES (1, 1, -10);";

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

        String createTableSql9 =
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
        String insertSql10 = "INSERT INTO Auftrag VALUES (1, 1, 1, 'Einkauf', date('now','localtime'), date('now','+5 days'));";

        String createTableSql11 =
                "CREATE TABLE Medikament (" +
                        "Medikament_id   INTEGER       NOT NULL CHECK (Medikament_id >= 0),"+
                        "Produktname     VARCHAR(50)   NOT NULL,"+
                        "Kennzeichnung   VARCHAR(50) COLLATE NOCASE "+
                        "CHECK (Kennzeichnung in ('freiverkäuflich', 'verschreibungspflichtig')),"+
                        "PRIMARY KEY (Medikament_id)"+
                        ");";
        String insertSql12 = "INSERT INTO Medikament VALUES (1, 'Medikament_Name', 'freiverkäuflich');";

        dbConnector.executeSql(createTableSql1);
        dbConnector.executeSql(insertSql2);

        dbConnector.executeSql(createTableSql3);
        dbConnector.executeSql(insertSql4);

        dbConnector.executeSql(createTableSql5);
        dbConnector.executeSql(insertSql6);

        dbConnector.executeSql(createTableSql7);
        dbConnector.executeSql(insertSql8);

        dbConnector.executeSql(createTableSql9);
        dbConnector.executeSql(insertSql10);

        dbConnector.executeSql(createTableSql11);
        dbConnector.executeSql(insertSql12);
    }

}
