package de.hhu.cs.dbs.internship.project.table;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;
import de.hhu.cs.dbs.internship.project.Project;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AuftragBuchtMedikament extends Table {

    @Override
    public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
        System.out.println("AuftragBuchtMedikament-getSelectQueryForTableWithFilter: " + filter);

        String sql = "SELECT * FROM AuftragBuchtMedikament ";

        if (filter != null && !filter.isEmpty()) {
            sql += " WHERE AuftragBuchtMedikament.Auftrag_id LIKE '%" + filter + "%'";
        }

        return sql;
    }

    @Override
    public String getSelectQueryForRowWithData(Data data) throws SQLException {
        System.out.println("AuftragBuchtMedikament-getSelectQueryForRowWithData: " + data);

        String sql = "SELECT Auftrag_id, Medikament_id, Anzahl " +
                "FROM AuftragBuchtMedikament " +
                "WHERE Auftrag_id = " + data.get("AuftragBuchtMedikament.Auftrag_id") + "" +
                " AND Medikament_id = " + data.get("AuftragBuchtMedikament.Medikament_id");
        System.out.println(sql);
        return sql;
    }

    @Override
    public void insertRowWithData(Data data) throws SQLException {
        System.out.println("AuftragBuchtMedikament-insertRowWithData: " + data);

        if ((Integer) Project.getInstance().getData().get("permission") <= 1) {
            throw new SQLException("Nicht die notwendigen Rechte.");
        }

        int auftragId = (int)data.get("AuftragBuchtMedikament.Auftrag_id");
        int medikamentId = (int)data.get("AuftragBuchtMedikament.Medikament_id");

        if(!(istScheinGebraucht(medikamentId) && kundeHastSchein(auftragId))) {
            throw new SQLException("Nicht erlaubt!!");
        }

        if(istZuSpaet(auftragId)) {
            throw new SQLException("Es ist zu spät!!");
        }

        PreparedStatement preparedStatement = Project.getInstance().getConnection().prepareStatement("INSERT INTO AuftragBuchtMedikament (Auftrag_id, Medikament_id, Anzahl) VALUES (?, ?, ?)");
        preparedStatement.setObject(1, data.get("AuftragBuchtMedikament.Auftrag_id"));
        preparedStatement.setObject(2, data.get("AuftragBuchtMedikament.Medikament_id"));
        preparedStatement.setObject(3, data.get("AuftragBuchtMedikament.Anzahl"));

        preparedStatement.executeUpdate();
    }

    @Override
    public void updateRowWithData(Data oldData, Data newData) throws SQLException {
        System.out.println("AuftragBuchtMedikament-updateRowWithData: " + oldData + "  :  " + newData);

        if ((Integer) Project.getInstance().getData().get("permission") <= 1) {
            throw new SQLException("Nicht die notwendigen Rechte.");
        }

        int auftragId = (int)newData.get("AuftragBuchtMedikament.Auftrag_id");
        int medikamentId = (int)newData.get("AuftragBuchtMedikament.Medikament_id");

        if(!(istScheinGebraucht(medikamentId) && kundeHastSchein(auftragId))) {
            throw new SQLException("Nicht erlaubt!!");
        }

        if(istZuSpaet(auftragId)) {
            throw new SQLException("Es ist zu spät!!");
        }

        PreparedStatement preparedStatement = Project.getInstance().getConnection().prepareStatement("UPDATE AuftragBuchtMedikament SET Anzahl = ? WHERE Auftrag_id = ? AND Medikament_id = ?");
        preparedStatement.setObject(1, newData.get("AuftragBuchtMedikament.Anzahl"));
        preparedStatement.setObject(2, oldData.get("AuftragBuchtMedikament.Auftrag_id"));
        preparedStatement.setObject(3, oldData.get("AuftragBuchtMedikament.Medikament_id"));

        preparedStatement.executeUpdate();
    }

    @Override
    public void deleteRowWithData(Data data) throws SQLException {
        System.out.println("AuftragBuchtMedikament-deleteRowWithData: " + data);

        if ((Integer) Project.getInstance().getData().get("permission") <= 1) {
            throw new SQLException("Nicht die notwendigen Rechte.");
        }

        Integer auftragId = null;
        Integer medikamentId = null;

        if (data.containsKey("AuftragBuchtMedikament.Auftrag_id")) {
            auftragId = (Integer) data.get("AuftragBuchtMedikament.Auftrag_id");
        }

        if (data.containsKey("AuftragBuchtMedikament.Medikament_id")) {
            medikamentId = (Integer) data.get("AuftragBuchtMedikament.Medikament_id");
        }

        PreparedStatement preparedStatement;
        if ((auftragId !=null ) && (medikamentId != null)) {
            preparedStatement = Project.getInstance().getConnection().prepareStatement("DELETE FROM AuftragBuchtMedikament WHERE Auftrag_id = ? AND Medikament_id = ?");
            preparedStatement.setObject(1, auftragId);
            preparedStatement.setObject(2, medikamentId);
        } else if ((auftragId !=null ) && (medikamentId == null)) {
            preparedStatement = Project.getInstance().getConnection().prepareStatement("DELETE FROM AuftragBuchtMedikament WHERE Auftrag_id = ?");
            preparedStatement.setObject(1, auftragId);
        } else if ((auftragId ==null ) && (medikamentId != null)) {
            preparedStatement = Project.getInstance().getConnection().prepareStatement("DELETE FROM AuftragBuchtMedikament WHERE Medikament_id = ?");
            preparedStatement.setObject(1, medikamentId);
        } else {
            throw new SQLException("Nicht zu löschen");
        }
        preparedStatement.executeUpdate();
    }


    private boolean istScheinGebraucht(int medikamentId) {
        String sql = "SELECT * " +
                "FROM Medikament " +
                "WHERE Medikament_id = " + medikamentId + " " +
                "AND Kennzeichnung = 'verschreibungspflichtig'";

        try {
            Statement stmt = Project.getInstance().getConnection().createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);
            return  resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    private boolean kundeHastSchein(int auftragId) {

        String sql = "SELECT * " +
                "FROM Kunde " +
                "JOIN Konto ON Kunde.Kunde_id = Konto.Kunde_id " +
                "JOIN Auftrag ON Konto.Konto_id = Auftrag.Konto_id " +
                "WHERE ApothekenSchein IS NOT NULL " +
                "AND Auftrag.Auftrag_id = " + auftragId;

        try {
            Statement stmt = Project.getInstance().getConnection().createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);
            return  resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean istZuSpaet(int auftragId) {
        String sql = "SELECT * " +
                "FROM Auftrag " +
                "WHERE Auftrag.Liefertermin < date('now','localtime') " +
                "AND Auftrag_id = " + auftragId;

        try {
            Statement stmt = Project.getInstance().getConnection().createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);
            return  resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;


    }
}
