package de.hhu.cs.dbs.internship.project.table;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;
import de.hhu.cs.dbs.internship.project.Project;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Kunde extends Table {

    @Override
    public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
        System.out.println("Kunde-getSelectQueryForTableWithFilter: " + filter);

        String sql = "SELECT Kunde_id, Bezeichnung, (ApothekenSchein IS NOT NULL) as Bescheinigung FROM Kunde ";
        return sql;
    }

    @Override
    public String getSelectQueryForRowWithData(Data data) throws SQLException {
        System.out.println("Kunde-getSelectQueryForRowWithData: " + data);

        String sql = "SELECT Kunde_id, Bezeichnung, 'Schein' as Bescheinigung FROM Kunde WHERE Kunde_id=" + data.get("Kunde.Kunde_id");
        System.out.println(sql);
        return sql;
    }

    @Override
    public void insertRowWithData(Data data) throws SQLException {
        System.out.println("Kunde-insertRowWithData: " + data);

        if ((Integer) Project.getInstance().getData().get("permission") <= 1) {
            throw new SQLException("Nicht die notwendigen Rechte.");
        }

        PreparedStatement preparedStatement = Project.getInstance().getConnection().prepareStatement("INSERT INTO Kunde(Kunde_id, Bezeichnung, Aphothekenschein) VALUES (?, ?, ?)");
        preparedStatement.setObject(1, data.get("Kunde.Kunde_id"));
        preparedStatement.setObject(2, data.get("Kunde.Bezeichnung"));
        preparedStatement.setObject(3, data.get("Kunde.Aphothekenschein"));
        preparedStatement.executeUpdate();
    }

    @Override
    public void updateRowWithData(Data oldData, Data newData) throws SQLException {
        System.out.println("Kunde-updateRowWithData: " + oldData + "  :  " + newData);

        if ((Integer) Project.getInstance().getData().get("permission") <= 1) {
            throw new SQLException("Nicht die notwendigen Rechte.");
        }

        PreparedStatement preparedStatement = Project.getInstance().getConnection()
                .prepareStatement(("UPDATE Kunde SET Bezeichnung = ?, ApothekenSchein = ?  WHERE Kunde_id = ?"));
        preparedStatement.setObject(1, newData.get("Kunde.Bezeichnung"));
        preparedStatement.setObject(2, readFile((String)newData.get(".Bescheinigung")));
        preparedStatement.setObject(3, newData.get("Kunde.Kunde_id"));
        preparedStatement.executeUpdate();
    }

    @Override
    public void deleteRowWithData(Data data) throws SQLException {
        System.out.println("Kunde-deleteRowWithData: " + data);

        if ((Integer) Project.getInstance().getData().get("permission") <= 1) {
            throw new SQLException("Nicht die notwendigen Rechte.");
        }

        if(istNichtAusgeliefert((int) data.get("Kunde.Kunde_id"))) {
            throw new SQLException("Der Auftrag ist noch nicht ausgeliefert!!");
        }

        deleteAuftrag((int)data.get("Kunde.Kunde_id"));
        deleteKonto((int)data.get("Kunde.Kunde_id"));
        deleteAnsprechpartner((int)data.get("Kunde.Kunde_id"));
        deleteKundenbetreuerBetreutKunde((int)data.get("Kunde.Kunde_id"));
        deleteKundeHatKontakteintrag((int)data.get("Kunde.Kunde_id"));

        PreparedStatement preparedStatement = Project.getInstance().getConnection().prepareStatement("DELETE FROM Kunde WHERE  Kunde_id = ?");
        preparedStatement.setObject(1, data.get("Kunde.Kunde_id"));
        preparedStatement.executeUpdate();
    }


    private void deleteAuftrag(int kundeId) throws SQLException {
        List<Integer> auftragIds = retrieveAuftragIds(kundeId);
        Table auftrag = new Auftrag();

        for(Integer auftragId : auftragIds) {
            Data data = new Data();
            data.put("Auftrag.Auftrag_id", auftragId);
            auftrag.deleteRowWithData(data);
        }
    }

    private void deleteKonto(int kundeId) throws SQLException {
        List<Integer> kontoIds = retrieveKontoIds(kundeId);
        Table konto = new Konto();

        for(Integer kontoId : kontoIds) {
            Data data = new Data();
            data.put("Konto.Konto_id", kontoId);
            konto.deleteRowWithData(data);
        }
    }

    private void deleteAnsprechpartner(int kundeId) throws SQLException {
        List<Integer> ansprechpartnerIds = retrieveAnsprechpartnerIds(kundeId);
        Table ansprechpartner = new Ansprechpartner();

        for(Integer ansprechpartnerId : ansprechpartnerIds) {
            Data data = new Data();
            data.put("Ansprechpartner.Ansprechpartner_id", ansprechpartnerId);
            ansprechpartner.deleteRowWithData(data);
        }
    }

    private void deleteKundenbetreuerBetreutKunde(int kundeId) throws SQLException {
        Data data = new Data();
        data.put("KundenbetreuerBetreutKunde.Kunde_id", new Integer(kundeId));

        Table kundenbetreuerBetreutKunde = new KundenbetreuerBetreutKunde();
        kundenbetreuerBetreutKunde.deleteRowWithData(data);
    }

    private void deleteKundeHatKontakteintrag(int kundeId) throws SQLException {
        Data data = new Data();
        data.put("KundeHatKontakteintrag.Kunde_id", new Integer(kundeId));

        Table kundeHatKontakteintrag = new KundeHatKontakteintrag();
        kundeHatKontakteintrag.deleteRowWithData(data);
    }

    private List<Integer> retrieveAuftragIds(int kundeId) throws SQLException {
        List<Integer> auftragIds = new ArrayList<>();

        String sql = "SELECT Auftrag_id FROM Auftrag " +
                "JOIN Konto ON Auftrag.Konto_id = Konto.Konto_id " +
                "WHERE Konto.Kunde_id = " + kundeId;

        try (Statement stmt = Project.getInstance().getConnection().createStatement();){
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Integer auftragId = rs.getInt("Auftrag_id");
                auftragIds.add(auftragId);
            }
        }

        return  auftragIds;
    }

    private List<Integer> retrieveKontoIds(int kundeId) throws SQLException {
        List<Integer> kontoIds = new ArrayList<>();
        String sql = "SELECT Konto_id FROM Konto WHERE Konto.Kunde_id = " + kundeId;

        try (Statement stmt = Project.getInstance().getConnection().createStatement();){
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Integer kontoId = rs.getInt("Konto_id");
                kontoIds.add(kontoId);
            }
        }

        return  kontoIds;
    }


    private List<Integer> retrieveAnsprechpartnerIds(int kundeId) throws SQLException {
        List<Integer> ansprechpartnerIds = new ArrayList<>();
        String sql = "SELECT Ansprechpartner_id FROM Ansprechpartner WHERE Ansprechpartner.Kunde_id = " + kundeId;

        try (Statement stmt = Project.getInstance().getConnection().createStatement();){
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Integer kontoId = rs.getInt("Ansprechpartner_id");
                ansprechpartnerIds.add(kontoId);
            }
        }

        return  ansprechpartnerIds;
    }


    private boolean istNichtAusgeliefert(int kundeId) {

        String sql = "SELECT * " +
                "FROM Konto " +
                "JOIN Auftrag ON Konto.Konto_id = Auftrag.Konto_id " +
                "WHERE Auftrag.Liefertermin > date('now','localtime') " +
                "AND Konto.Kunde_id = " + kundeId;

        try {
            Statement stmt = Project.getInstance().getConnection().createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);
            return  resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    private byte[] readFile(String file) {
        byte[] blob = null;

        File f;
        try {
            f = new File(file);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             FileInputStream fis = new FileInputStream(f);) {
            byte[] buffer = new byte[1024];
            for (int len; (len = fis.read(buffer)) != -1;) {
                bos.write(buffer, 0, len);
            }

            blob = bos != null ? bos.toByteArray() : null;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return blob;
    }
}
