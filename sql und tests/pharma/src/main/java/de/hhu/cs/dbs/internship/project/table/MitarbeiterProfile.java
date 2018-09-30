package de.hhu.cs.dbs.internship.project.table;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;
import de.hhu.cs.dbs.internship.project.Project;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MitarbeiterProfile extends Table {


    @Override
    public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
        System.out.println("MitarbeiterProfile-getSelectQueryForTableWithFilter: " + filter);

        int mitarbeiter_id = (int) Project.getInstance().getData().get("mitarbeiter_id");
        String query =
                "SELECT Person.Vorname, Person.Nachname, " +
                        "Kontakteintrag.TelefonNr, Kontakteintrag.EMail, " +
                        "Adresse.addr_id, Adresse.Strasse, Adresse.Hausnr, Adresse.PLZ, Adresse.Ort, " +
                        "EXISTS(SELECT * FROM Kundenbetreuer WHERE Kundenbetreuer.Kundenbetreuer_id = " + mitarbeiter_id + ") as isKundenbetreuer, " +
                        "EXISTS(SELECT * FROM CallcenterMitarbeiter WHERE CallcenterMitarbeiter.CallcenterMitarbeiter_id = "+ mitarbeiter_id + ") as isCallcenterMitarbeiter " +
                        "FROM Mitarbeiter " +
                        "JOIN Person ON Mitarbeiter.Mitarbeiter_id = Person.Person_id " +
                        "JOIN PersonHatKontakteintrag ON Mitarbeiter.Mitarbeiter_id = PersonHatKontakteintrag.Person_id " +
                        "JOIN Kontakteintrag ON PersonHatKontakteintrag.TelefonNr = Kontakteintrag.TelefonNr " +
                        "JOIN Adresse ON Kontakteintrag.addr_id = Adresse.addr_id ";

        if (filter != null && !filter.isEmpty()) {
            query += " WHERE Person.Nachname LIKE '%" + filter + "%'";
        }

        return query;
    }

    @Override
    public String getSelectQueryForRowWithData(Data data) throws SQLException {
        System.out.println("MitarbeiterProfile-getSelectQueryForRowWithData: " + data);
        int mitarbeiter_id = (int) Project.getInstance().getData().get("mitarbeiter_id");
        String query =
                "SELECT Person.Vorname, Person.Nachname, " +
                        "Kontakteintrag.TelefonNr, Kontakteintrag.EMail, Kontakteintrag.Fax, " +
                        "Adresse.addr_id, Adresse.Strasse, Adresse.Hausnr, Adresse.PLZ, Adresse.Ort, " +
                        "CallcenterMitarbeiter.Sprachzertifikat, " +
                        "Kundenbetreuer.Fachgebiet, Kundenbetreuer.Bezirk, " +
                        "Mitarbeiter.Passwort " +
                        "FROM Mitarbeiter " +
                        "JOIN Person ON Mitarbeiter.Mitarbeiter_id = Person.Person_id " +
                        "JOIN PersonHatKontakteintrag ON Mitarbeiter.Mitarbeiter_id = PersonHatKontakteintrag.Person_id " +
                        "LEFT JOIN Kundenbetreuer ON Mitarbeiter.Mitarbeiter_id = Kundenbetreuer.Kundenbetreuer_id " +
                        "LEFT JOIN CallcenterMitarbeiter ON Mitarbeiter.Mitarbeiter_id = CallcenterMitarbeiter.CallcenterMitarbeiter_id " +
                        "JOIN Kontakteintrag ON Kontakteintrag.TelefonNr = " + data.get("Kontakteintrag.TelefonNr") + " " +
                        "JOIN Adresse ON Adresse.addr_id = " + data.get("Adresse.addr_id") + " " +
                        "WHERE Mitarbeiter.Mitarbeiter_id = " + mitarbeiter_id;

        return query;
    }

    @Override
    public void insertRowWithData(Data data) throws SQLException {
        System.out.println("MitarbeiterProfile-insertRowWithData: " + data);
    }

    @Override
    public void updateRowWithData(Data oldData, Data newData) throws SQLException {
        System.out.println("MitarbeiterProfile-updateRowWithData: " + oldData + "  :  " + newData);

        int mitarbeiterId = (int) Project.getInstance().getData().get("mitarbeiter_id");
        int kontakteintragId = (int) oldData.get("Kontakteintrag.TelefonNr");
        int adresseId = (int) oldData.get("Adresse.addr_id");

        updatePasswort(mitarbeiterId, newData);
        updateAdresse(adresseId, newData);
        updateKontakteintrag(kontakteintragId, newData);
        updatePerson(mitarbeiterId, newData);

        processKundenbetreuerChange(mitarbeiterId, oldData, newData);
        processCallcenterMitarbeiterChange(mitarbeiterId, oldData, newData);


    }

    private void updatePasswort(int mitarbeiterId, Data newData) throws SQLException {
        Mitarbeiter mitarbeiter = new Mitarbeiter();
        mitarbeiter.updatePasswort(mitarbeiterId, (String)newData.get("Mitarbeiter.Passwort"));
    }

    @Override
    public void deleteRowWithData(Data data) throws SQLException {
        System.out.println("MitarbeiterProfile-deleteRowWithData: " + data);
    }


    boolean isAdresseChanged(Data oldData, Data newData) throws SQLException {
        boolean notChanged = true;
        notChanged = ((String)oldData.get("Adresse.Strasse")).equals((String)newData.get("Adresse.Strasse"));
        notChanged = notChanged && ((int)oldData.get("Adresse.Hausnr")) == ((int)newData.get("Adresse.Hausnr"));
        notChanged = notChanged && ((int)oldData.get("Adresse.PLZ")) == ((int)newData.get("Adresse.PLZ"));
        notChanged = notChanged && ((String)oldData.get("Adresse.Ort")).equals((String)newData.get("Adresse.Ort"));
        return !notChanged;
    }

    private void updateAdresse(int adresseId, Data newData) throws SQLException {
        PreparedStatement stmt = Project.getInstance().getConnection().prepareStatement("UPDATE Adresse SET Strasse = ?, Hausnr = ?, PLZ = ?, Ort = ? WHERE addr_id = " + adresseId);
        stmt.setObject(1, newData.get("Adresse.Strasse"));
        stmt.setObject(2, newData.get("Adresse.Hausnr"));
        stmt.setObject(3, newData.get("Adresse.PLZ"));
        stmt.setObject(4, newData.get("Adresse.Ort"));
        stmt.executeUpdate();
    }


    private boolean isKontakteintragChanged(Data oldData, Data newData) throws SQLException {
        boolean notChanged = true;
        notChanged = ((String)oldData.get("Kontakteintrag.EMail")).equals((String)newData.get("Kontakteintrag.EMail"));
        notChanged = notChanged && ((int)oldData.get("Kontakteintrag.Fax")) == ((int)newData.get("Kontakteintrag.Fax"));
        return !notChanged;
    }

    private void updateKontakteintrag(int kontakteintragId, Data newData) throws SQLException {
        PreparedStatement stmt = Project.getInstance().getConnection().prepareStatement("UPDATE Kontakteintrag SET EMail = ?, Fax = ? WHERE TelefonNr = " + kontakteintragId);
        stmt.setObject(1, newData.get("Kontakteintrag.EMail"));
        stmt.setObject(2, newData.get("Kontakteintrag.Fax"));
        stmt.executeUpdate();
    }


    boolean isPersonChanged(Data oldData, Data newData) throws SQLException {
        boolean notChanged = true;
        notChanged = ((String)oldData.get("Person.Vorname")).equals((String)newData.get("Person.Vorname"));
        notChanged = notChanged && ((String)oldData.get("Person.Nachname")).equals((String)newData.get("Person.Nachname"));
        return !notChanged;
    }


    private void updatePerson(int mitarbeiterId, Data newData) throws SQLException {
        PreparedStatement stmt = Project.getInstance().getConnection().prepareStatement("UPDATE Person SET Vorname = ?, Nachname = ? WHERE Person_id = " + mitarbeiterId);
        stmt.setObject(1, newData.get("Person.Vorname"));
        stmt.setObject(2, newData.get("Person.Nachname"));
        stmt.executeUpdate();

    }

    private void processKundenbetreuerChange(int mitarbeiterId, Data oldData, Data newData) throws SQLException {
        String oldFachgebiet = (String) oldData.get("Kundenbetreuer.Fachgebiet");
        String newFachgebiet = (String) newData.get("Kundenbetreuer.Fachgebiet");

        String oldBezirk = (String) oldData.get("Kundenbetreuer.Bezirk");
        String newBezirk = (String) newData.get("Kundenbetreuer.Bezirk");

        if((oldFachgebiet == null) && (oldBezirk == null) && (newFachgebiet == null) && (newBezirk == null)) {
            return;
        } else if ((oldFachgebiet == null) && (oldBezirk == null) && (newFachgebiet != null) && (newBezirk != null)) {
            createKundenbetreuer(mitarbeiterId, newData);
        } else if ((oldFachgebiet != null) && (oldBezirk != null) && (newFachgebiet == null) && (newBezirk == null)) {
            deleteKundenbetreuer(mitarbeiterId);
        } else if ((oldFachgebiet.equals(newFachgebiet)) && (oldBezirk.equals(newBezirk))) {
            return;
        } else {
            deleteKundenbetreuer(mitarbeiterId);
            createKundenbetreuer(mitarbeiterId, newData);
        }
    }

    private void createKundenbetreuer(int mitarbeiterId, Data newData) throws SQLException {
        PreparedStatement stmt = Project.getInstance().getConnection().prepareStatement("INSERT INTO Kundenbetreuer VALUES (?, ?, ?)");
        stmt.setObject(1, mitarbeiterId);
        stmt.setObject(2, newData.get("Kundenbetreuer.Fachgebiet"));
        stmt.setObject(3, newData.get("Kundenbetreuer.Bezirk"));
        stmt.executeUpdate();
    }

    private void deleteKundenbetreuer(int mitarbeiterId) throws SQLException {
        PreparedStatement stmt = Project.getInstance().getConnection().prepareStatement("DELETE FROM Kundenbetreuer WHERE Kundenbetreuer_id = ?");
        stmt.setObject(1, mitarbeiterId);
        stmt.executeUpdate();
    }



    private void processCallcenterMitarbeiterChange(int mitarbeiterId, Data oldData, Data newData) throws SQLException {
        String oldSprachzertifikat = (String)oldData.get("CallcenterMitarbeiter.Sprachzertifikat");
        String newSprachzertifikat = (String)newData.get("CallcenterMitarbeiter.Sprachzertifikat");

        if( (oldSprachzertifikat == null) && (newSprachzertifikat == null)) {
            return;
        } else if ( (oldSprachzertifikat == null) && (newSprachzertifikat != null)) {
            createCallcenterMitarbeiter(mitarbeiterId, newData);
        } else if ((oldSprachzertifikat != null) && (newSprachzertifikat == null)) {
            deleteCallcenterMitarbeiter(mitarbeiterId);
        } else if (oldSprachzertifikat.equals(newSprachzertifikat)) {
            return;
        } else {
            deleteCallcenterMitarbeiter(mitarbeiterId);
            createCallcenterMitarbeiter(mitarbeiterId, newData);
        }
    }

    private void createCallcenterMitarbeiter(int mitarbeiterId, Data newData) throws SQLException {
        PreparedStatement stmt = Project.getInstance().getConnection().prepareStatement("INSERT INTO CallcenterMitarbeiter VALUES (?, ?)");
        stmt.setObject(1, mitarbeiterId);
        stmt.setObject(2, newData.get("CallcenterMitarbeiter.Sprachzertifikat"));
        stmt.executeUpdate();
    }

    private void deleteCallcenterMitarbeiter(int mitarbeiterId) throws SQLException {
        PreparedStatement stmt = Project.getInstance().getConnection().prepareStatement("DELETE FROM CallcenterMitarbeiter WHERE CallcenterMitarbeiter_id = ?");
        stmt.setObject(1, mitarbeiterId);
        stmt.executeUpdate();
    }

}
