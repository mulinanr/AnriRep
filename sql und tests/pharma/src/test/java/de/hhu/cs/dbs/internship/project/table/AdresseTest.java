package de.hhu.cs.dbs.internship.project.table;

import com.alexanderthelen.applicationkit.database.Connection;
import com.alexanderthelen.applicationkit.database.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AdresseTest extends TableTestBase {

    private  Adresse adresse;
    Connection connection;
    
    @BeforeEach
    public void setUp() throws Exception {
        connection = prepareMemeoryDbConnection();
        prepareEnvironment(connection);
        adresse = new Adresse();
    }


    @Test
    void getSelectQueryForTableWithFilter() {
    }

    @Test
    void getSelectQueryForRowWithData() {
    }

    @Test
    void insertRowWithData_success_oneRowAdded() throws Exception {
        Data data = createTestData();
        int initialCount = countRows();

        adresse.insertRowWithData(data);
        int resultCount = countRows();

        assertEquals(1, (resultCount - initialCount));
    }

    @Test
    void updateRowWithData() {
    }

    @Test
    void deleteRowWithData() {
    }

    private Data createTestData() {
        Data data = new Data();
        data.put("permission", 10);
        data.put("Adresse.Strasse", "Gaten");
        data.put("Adresse.Hausnr", "10");
        data.put("Adresse.PLZ", "12345");
        data.put("Adresse.Ort", "Stadt");
        return  data;
    }

    private int countRows() throws SQLException {
        ResultSet resultSet = connection.executeQuery("SELECT count(*) FROM Adresse");
        return resultSet.getInt(1);
    }

}