package de.hhu.cs.dbs.internship.project.table;

import com.alexanderthelen.applicationkit.database.Connection;
import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;
import de.hhu.cs.dbs.internship.project.Project;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Mitarbeiter extends Table {

    public boolean isCorrectLogin(Connection connection, String eMail, String password) throws SQLException {
        String query =
                "SELECT * FROM Mitarbeiter " +
                "JOIN PersonHatKontakteintrag ON Mitarbeiter.Mitarbeiter_id = PersonHatKontakteintrag.Person_id " +
                "JOIN Kontakteintrag ON PersonHatKontakteintrag.TelefonNr = Kontakteintrag.TelefonNr " +
                "WHERE Kontakteintrag.EMail = '" + eMail + "' " +
                "AND Mitarbeiter.Passwort = '" + passwordHash(password) + "'";
        ResultSet resultSet = connection.executeQuery(query);

        if (resultSet.next()) {
            int mitarbeiterId = resultSet.getInt("Mitarbeiter_id");
            Project.getInstance().getData().put("mitarbeiter_id", mitarbeiterId);
            Project.getInstance().getData().put("permission", 2);
            return true;
        }

        return false;
    }

    @Override
    public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
        return null;
    }

    @Override
    public String getSelectQueryForRowWithData(Data data) throws SQLException {
        return null;
    }

    @Override
    public void insertRowWithData(Data data) throws SQLException {
        int adresseId;
        int kontakteintragId;

        int personId = createPersonAndGetId(data);

        try {
            createMitarbeiter(data, personId);
        } catch (Exception e) {
            deletePerson(personId);
            throw e;
        }

        try {
            adresseId = createAdresseAndGetId(data);
        } catch (Exception e) {
            deleteMitarbeiter(personId);
            deletePerson(personId);
            throw e;
        }

        try {
            kontakteintragId = createKontakteintragAndGetId(data, adresseId);
        } catch (Exception e) {
            deleteMitarbeiter(personId);
            deletePerson(personId);
            deleteAdresse(adresseId);
            throw e;
        }

        try {
            createPersonHatKontakteintrag(personId, kontakteintragId);
        } catch (Exception e) {
            deleteMitarbeiter(personId);
            deletePerson(personId);
            deleteKontakteintrag(kontakteintragId);
            deleteAdresse(adresseId);
            throw e;
        }

        System.out.println();
    }


    @Override
    public void updateRowWithData(Data oldData, Data newData) throws SQLException {

    }

    @Override
    public void deleteRowWithData(Data data) throws SQLException {

    }

    private int createPersonAndGetId(Data data) throws SQLException {
        PreparedStatement stmt = Project.getInstance().getConnection().prepareStatement("INSERT INTO Person (Vorname, Nachname) VALUES (?, ?)");
        stmt.setObject(1, data.get("firstName"));
        stmt.setObject(2, data.get("lastName"));
        stmt.executeUpdate();
        return (int) stmt.getGeneratedKeys().getInt(1);
    }

    private void deletePerson(long personId) throws SQLException {
        PreparedStatement stmt = Project.getInstance().getConnection().prepareStatement("DELETE FROM Person WHERE Person_id = ?");
        stmt.setObject(1, personId);
        stmt.executeUpdate();
    }

    private void createMitarbeiter(Data data, long personId) throws SQLException {
        String password = (String) data.get("password1");
        PreparedStatement stmt = Project.getInstance().getConnection().prepareStatement("INSERT INTO Mitarbeiter VALUES (?, ?, ?)");
        stmt.setObject(1, personId);
        stmt.setObject(2, personId);
        stmt.setObject(3, passwordHash(password));
        stmt.executeUpdate();
    }

    private void deleteMitarbeiter(long mitarbeiterId) throws SQLException {
        PreparedStatement stmt = Project.getInstance().getConnection().prepareStatement("DELETE FROM Mitarbeiter WHERE Mitarbeiter_id = ?");
        stmt.setObject(1, mitarbeiterId);
        stmt.executeUpdate();
    }

    private int createAdresseAndGetId(Data data) throws SQLException {
        PreparedStatement stmt = Project.getInstance().getConnection().prepareStatement("INSERT INTO Adresse(Strasse, Hausnr, PLZ, Ort) VALUES (?, ?, ?, ?)");
        stmt.setObject(1, data.get("street"));
        stmt.setObject(2, data.get("houseNumber"));
        stmt.setObject(3, data.get("zipCode"));
        stmt.setObject(4, data.get("city"));
        stmt.executeUpdate();
        return (int) stmt.getGeneratedKeys().getInt(1);
    }

    private void deleteAdresse(long adresseId) throws SQLException {
        PreparedStatement stmt = Project.getInstance().getConnection().prepareStatement("DELETE FROM Adresse WHERE addr_id = ?");
        stmt.setObject(1, adresseId);
        stmt.executeUpdate();
    }

    private int createKontakteintragAndGetId(Data data, long adresseId) throws SQLException {
        PreparedStatement stmt = Project.getInstance().getConnection().prepareStatement("INSERT INTO Kontakteintrag (TelefonNr, addr_id, EMail, Fax) VALUES (?, ?, ?, ?)");
        stmt.setObject(1, Integer.parseInt((String)data.get("telefonNr")));
        stmt.setObject(2, adresseId);
        stmt.setObject(3, data.get("eMail"));
        stmt.setObject(4, null);
        stmt.executeUpdate();
        return (int) stmt.getGeneratedKeys().getInt(1);
    }

    private void deleteKontakteintrag(long kontakteintragId) throws SQLException {
        PreparedStatement stmt = Project.getInstance().getConnection().prepareStatement("DELETE FROM Kontakteintrag WHERE TelefonNr = ?");
        stmt.setObject(1, kontakteintragId);
        stmt.executeUpdate();
    }

    private void createPersonHatKontakteintrag(long personId, long kontakteintragId) throws SQLException {
        PreparedStatement stmt = Project.getInstance().getConnection().prepareStatement("INSERT INTO PersonHatKontakteintrag (TelefonNr, Person_id) VALUES (?, ?)");
        stmt.setObject(1, kontakteintragId);
        stmt.setObject(2, personId);
        stmt.executeUpdate();
    }

    public void updatePasswort(int mitarbeiterId, String password) throws SQLException {
        PreparedStatement stmt = Project.getInstance().getConnection().prepareStatement("UPDATE Mitarbeiter SET Passwort = ? WHERE Mitarbeiter_id = " + mitarbeiterId);
        stmt.setObject(1, passwordHash(password));
        stmt.executeUpdate();
    }

    private String passwordHash(String password) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

        return bytesToHex(encodedhash);
    }

    private String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        String result = hexString.toString();
        return hexString.toString();
    }
}
