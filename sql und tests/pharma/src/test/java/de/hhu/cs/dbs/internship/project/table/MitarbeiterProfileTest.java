package de.hhu.cs.dbs.internship.project.table;

import com.alexanderthelen.applicationkit.database.Connection;
import com.alexanderthelen.applicationkit.database.Data;
import de.hhu.cs.dbs.internship.project.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class MitarbeiterProfileTest extends TableTestBase {

    private Connection connection;
    private MitarbeiterProfile mitarbeiterProfile;

    @BeforeEach
    public void setUp() throws Exception {
        connection = prepareMemeoryDbConnection();
        prepareEnvironment(connection);
        mitarbeiterProfile = new MitarbeiterProfile();
    }

    @Test
    void getQuery_forTableView_oneRow() throws SQLException {
        Project.getInstance().getData().put("mitarbeiter_id", 21);
        mitarbeiterProfile.getSelectQueryForTableWithFilter(null);

        ResultSet result = connection.executeQuery("SELECT * FROM Person");

        System.out.println(result.next());
    }

    @Test
    void isAddresseChanged_notChanged() throws SQLException {
        Data data = createTestData("Vvv");

        boolean result = mitarbeiterProfile.isAdresseChanged(data, data);

        System.out.println(result);
        assertFalse(result);
    }

    @Test
    void isPersonChanged_notChanged() throws SQLException {
        Data oldData = createTestData("Vvv");
        Data newData = createTestData("Www");

        boolean result = mitarbeiterProfile.isPersonChanged(oldData, newData);

        System.out.println(result);
        assertTrue(result);
    }

    private Data createTestData(String vName) {
        Data data = new Data();
        data.put("Person.Vorname", vName);
        data.put("Person.Nachname", "lName");
        data.put("Kontakteintrag.TelefonNr", 111);
        data.put("Kontakteintrag.EMail", "ppppp@rr.gg");
        data.put("Adresse.addr_id", 111);
        data.put("Adresse.Strasse", "Strasse");
        data.put("Adresse.Hausnr", 100);
        data.put("Adresse.PLZ", 12345);
        data.put("Adresse.Ort", "SS");
        data.put("CallcenterMitarbeiter.Sprachzertifikat", "EEE");
        data.put("Kundenbetreuer.Fachgebiet", null);
        data.put("Kundenbetreuer.Bezirk", null);
        return  data;
    }

}