package org.example.test.create;

import org.example.test.DBConnector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sqlite.SQLiteException;

import java.sql.SQLException;

public class KontoTest {

    private DBConnector dbConnector;

    private String createTableSql =
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
        String insertSql = "INSERT INTO Konto VALUES (1, 1, 'Konto1', '5t', 'sepa');";

        dbConnector.executeSql(insertSql);
    }

    @Test (expected = SQLiteException.class)
    public void insert_incorrectZahlungsziel5p_failed() throws Exception {
        String insertSql = "INSERT INTO Konto VALUES (1, 1, 'Konto1', '5p', 'sepa');";

        dbConnector.executeSql(insertSql);
    }

    @Test (expected = SQLiteException.class)
    public void insert_incorrectZahlungsziel40t_failed() throws Exception {
        String insertSql = "INSERT INTO Konto VALUES (1, 1, 'Konto1', '40t', 'sepa');";

        dbConnector.executeSql(insertSql);
    }

    @Test (expected = SQLiteException.class)
    public void insert_incorrectZahlungsziel4Xt_failed() throws Exception {
        String insertSql = "INSERT INTO Konto VALUES (1, 1, 'Konto1', '4Xt', 'sepa');";

        dbConnector.executeSql(insertSql);
    }

    @Test (expected = SQLiteException.class)
    public void insertAndUpdate_incorrectZahlungsziel5p_failed() throws Exception {
        String insertSql = "INSERT INTO Konto VALUES (1, 1, 'Konto1', '5t', 'sepa');";
        String updateSql = "UPDATE Konto SET  Zahlungsziel = '5p' WHERE Konto_id = 1;";

        dbConnector.executeSql(insertSql);
        dbConnector.executeSql(updateSql);
    }

    @Test (expected = SQLiteException.class)
    public void insertAndUpdate_incorrectZahlungsziel40_failed() throws Exception {
        String insertSql = "INSERT INTO Konto VALUES (1, 1, 'Konto1', '5t', 'sepa');";
        String updateSql = "UPDATE Konto SET  Zahlungsziel = '40t' WHERE Konto_id = 1;";

        dbConnector.executeSql(insertSql);
        dbConnector.executeSql(updateSql);
    }

    private void createRelatedTables() throws Exception {
        String createTableSql =
                "CREATE TABLE Kunde (" +
                        "Kunde_id      INTEGER     NOT NULL CHECK (Kunde_id >= 0),"+
                        "Bezeichnung   VARCHAR(30) NOT NULL,"+
                        "ApothekenSchein BLOB,"+
                        "PRIMARY KEY (Kunde_id)"+
                        ");";
        String insertSql = "INSERT INTO Kunde VALUES (1, 'Kunde1', null);";
        dbConnector.executeSql(createTableSql);
        dbConnector.executeSql(insertSql);
    }

}
