package de.hhu.cs.dbs.internship.project.table;

import com.alexanderthelen.applicationkit.Application;
import com.alexanderthelen.applicationkit.database.Connection;
import com.alexanderthelen.applicationkit.database.Data;
import de.hhu.cs.dbs.internship.project.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class MitarbeiterTest extends TableTestBase {

    private Connection connection;
    private Mitarbeiter mitarbeiter;

    @BeforeEach
    public void setUp() throws Exception {
        connection = prepareMemeoryDbConnection();
        prepareEnvironment(connection);
        mitarbeiter = new Mitarbeiter();
    }

    @Test
    void isCorrectLogin_corectCredentials_true() throws Exception {

        boolean result = mitarbeiter.isCorrectLogin(connection, "person21@aaa.bb", "password");

        assertTrue(result);
    }

    @Test
    void isCorrectLogin_incorectCredentials_false() throws Exception {

        boolean result = mitarbeiter.isCorrectLogin(connection, "person11@aaa.bb", "password");

        assertFalse(result);
    }

    @Test
    void getSelectQueryForTableWithFilter() {
    }

    @Test
    void getSelectQueryForRowWithData() {
    }

    @Test
    void insertMitarbeirer_success_oneAdded() throws Exception {
        Data data = createTestData("aaa.bbb@eee.gg");
        int initPerson = countRows(connection, "Person");
        int initMitarbeiter = countRows(connection, "Mitarbeiter");
        int initAdresse = countRows(connection, "Adresse");
        int initKontakteintrag = countRows(connection, "Kontakteintrag");
        int initPersonHatKontakteintrag = countRows(connection, "PersonHatKontakteintrag");

        mitarbeiter.insertRowWithData(data);

        int resultPerson = countRows(connection, "Person");
        int resultMitarbeiter = countRows(connection, "Mitarbeiter");
        int resultAdresse = countRows(connection, "Adresse");
        int resultKontakteintrag = countRows(connection, "Kontakteintrag");
        int resultPersonHatKontakteintrag = countRows(connection, "PersonHatKontakteintrag");

        System.out.println(resultPerson + " - " + initPerson);
        System.out.println(resultMitarbeiter + " - " + initMitarbeiter);
        System.out.println(resultAdresse + " - " + initAdresse);
        System.out.println(resultKontakteintrag + " - " + initKontakteintrag);
        System.out.println(resultPersonHatKontakteintrag + " - " + initPersonHatKontakteintrag);

        assertEquals(1, (resultPerson - initPerson));
        assertEquals(1, (resultMitarbeiter - initMitarbeiter));
        assertEquals(1, (resultAdresse - initAdresse));
        assertEquals(1, (resultKontakteintrag - initKontakteintrag));
        assertEquals(1, (resultPersonHatKontakteintrag - initPersonHatKontakteintrag));
    }

    @Test
    void insertMitarbeirer_failed_noOneAdded() throws Exception {
        Data data = createTestData("-----");
        Exception exception = null;

        int initPerson = countRows(connection, "Person");
        int initMitarbeiter = countRows(connection, "Mitarbeiter");
        int initAdresse = countRows(connection, "Adresse");
        int initKontakteintrag = countRows(connection, "Kontakteintrag");
        int initPersonHatKontakteintrag = countRows(connection, "PersonHatKontakteintrag");

        try {
            mitarbeiter.insertRowWithData(data);
        } catch (SQLException e) {
            exception = e;
        }

        int resultPerson = countRows(connection, "Person");
        int resultMitarbeiter = countRows(connection, "Mitarbeiter");
        int resultAdresse = countRows(connection, "Adresse");
        int resultKontakteintrag = countRows(connection, "Kontakteintrag");
        int resultPersonHatKontakteintrag = countRows(connection, "PersonHatKontakteintrag");

        System.out.println(resultPerson + " - " + initPerson);
        System.out.println(resultMitarbeiter + " - " + initMitarbeiter);
        System.out.println(resultAdresse + " - " + initAdresse);
        System.out.println(resultKontakteintrag + " - " + initKontakteintrag);
        System.out.println(resultPersonHatKontakteintrag + " - " + initPersonHatKontakteintrag);
        System.out.println(exception.getMessage());

        assertEquals(0, (resultPerson - initPerson));
        assertEquals(0, (resultMitarbeiter - initMitarbeiter));
        assertEquals(0, (resultAdresse - initAdresse));
        assertEquals(0, (resultKontakteintrag - initKontakteintrag));
        assertEquals(0, (resultPersonHatKontakteintrag - initPersonHatKontakteintrag));
    }

    @Test
    void updateRowWithData() {
    }

    @Test
    void deleteRowWithData() {
    }

    private Data createTestData(String email) {
        Data data = new Data();
        data.put("firstName", "fMane");
        data.put("lastName", "lName");
        data.put("eMail", email);
        data.put("password1", "ppppp");
        data.put("password2", "ppppp");
        data.put("street", "Strasse");
        data.put("houseNumber", "100");
        data.put("zipCode", "12345");
        data.put("city", "Stadt");
        return  data;
    }

}