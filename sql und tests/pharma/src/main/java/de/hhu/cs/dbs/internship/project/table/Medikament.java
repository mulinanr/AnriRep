package de.hhu.cs.dbs.internship.project.table;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;
import de.hhu.cs.dbs.internship.project.Project;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Medikament extends Table {

    @Override
    public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
        System.out.println("Medikament-getSelectQueryForTableWithFilter: " + filter);

        String sql = "SELECT * FROM Medikament ";
        if (filter != null && !filter.isEmpty()) {
            sql += " WHERE Medikament.Produktname LIKE '%" + filter + "%'";
        }

        return sql;
    }

    @Override
    public String getSelectQueryForRowWithData(Data data) throws SQLException {
        System.out.println("Medikament-getSelectQueryForRowWithData: " + data);

        String sql = null;
        if ((data.get("Medikament.Medikament_id") == null)) {
            sql = "SELECT Medikament.Medikament_id, Medikament.Produktname, Medikament.Kennzeichnung, " +
                    "Staffelpreis.EinVerkauf, Staffelpreis.Anzahl, Staffelpreis.Preis " +
                    "FROM Medikament " +
                    "JOIN Staffelpreis ON Medikament.Medikament_id = Staffelpreis.Medikament_id " +
                    "WHERE Medikament.Medikament_id=" + data.get("Medikament.Medikament_id");
        } else {
            sql = "SELECT Medikament_id, Produktname, Kennzeichnung " +
                    "FROM Medikament " +
                    "WHERE Medikament.Medikament_id=" + data.get("Medikament.Medikament_id");
        }

        System.out.println(sql);
        return sql;
    }

    @Override
    public void insertRowWithData(Data data) throws SQLException {
        System.out.println("Medikament-insertRowWithData: " + data);

        if ((Integer) Project.getInstance().getData().get("permission") <= 1) {
            throw new SQLException("Nicht die notwendigen Rechte.");
        }

        int medikamentId = createMedikament(data);

        Staffelpreis staffelpreis = new Staffelpreis();
        try {
            data.put("Staffelpreis.Medikament_id", medikamentId);
            staffelpreis.insertRowWithData(data);
        } catch (SQLException e) {
            data.put("Medikament.Medikament_id", medikamentId);
            deleteRowWithData(data);
            throw e;
        }
    }

    private int createMedikament(Data data) throws SQLException {
        PreparedStatement stmt = Project.getInstance().getConnection().prepareStatement("INSERT INTO Medikament(Medikament_id, Produktname, Kennzeichnung) VALUES (?, ?, ?)");
        stmt.setObject(1, data.get("Medikament.Medikament_id"));
        stmt.setObject(2, data.get("Medikament.Produktname"));
        stmt.setObject(3, data.get("Medikament.Kennzeichnung"));
        stmt.executeUpdate();
        return stmt.getGeneratedKeys().getInt(1);
    }

    @Override
    public void updateRowWithData(Data oldData, Data newData) throws SQLException {
        System.out.println("Medikament-updateRowWithData: " + oldData + "  :  " + newData);

        if ((Integer) Project.getInstance().getData().get("permission") <= 1) {
            throw new SQLException("Nicht die notwendigen Rechte.");
        }

        PreparedStatement preparedStatement = Project.getInstance().getConnection()
                .prepareStatement(("UPDATE Medikament SET Produktname = ?, Kennzeichnung = ?  WHERE Medikament_id = ?"));
        preparedStatement.setObject(1, newData.get("Medikament.Produktname"));
        preparedStatement.setObject(2, newData.get("Medikament.Kennzeichnung"));
        preparedStatement.setObject(3, newData.get("Medikament.Medikament_id"));

        preparedStatement.executeUpdate();
    }

    @Override
    public void deleteRowWithData(Data data) throws SQLException {
        System.out.println("Medikament-deleteRowWithData: " + data);

        if ((Integer) Project.getInstance().getData().get("permission") <= 1) {
            throw new SQLException("Nicht die notwendigen Rechte.");
        }

        deleteStaffelPrice(data);

        PreparedStatement preparedStatement = Project.getInstance().getConnection().prepareStatement("DELETE FROM Medikament WHERE Medikament_id = ?");
        preparedStatement.setObject(1, data.get("Medikament.Medikament_id"));
        preparedStatement.executeUpdate();

    }

    private void deleteStaffelPrice(Data data) throws SQLException {
        List<Integer> staffelIds = retrieveStaffelIds((int) data.get("Medikament.Medikament_id"));
        Table staffelpreis = new Staffelpreis();

        for(Integer staffelId : staffelIds) {
            data.put("Staffelpreis.Staffelpreis_id", staffelId);
            staffelpreis.deleteRowWithData(data);
        }
    }

    private List<Integer> retrieveStaffelIds(int medikamentId) throws SQLException {
        List<Integer> staffelIds = new ArrayList<>();

        String sql = "SELECT Staffelpreis_id FROM Staffelpreis " +
                "WHERE Staffelpreis.Medikament_id = " + medikamentId;

        try (Statement stmt = Project.getInstance().getConnection().createStatement();){
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Integer staffelId = rs.getInt("Staffelpreis_id");
                staffelIds.add(staffelId);
            }
        }

        return  staffelIds;
    }
}
