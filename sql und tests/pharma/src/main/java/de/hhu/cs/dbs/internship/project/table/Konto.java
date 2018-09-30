package de.hhu.cs.dbs.internship.project.table;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;
import de.hhu.cs.dbs.internship.project.Project;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Konto extends Table {

    @Override
    public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
        System.out.println("Konto-getSelectQueryForTableWithFilter: " + filter);

        String sql = "SELECT Konto_id, Konto.Bezeichnung, Zahlungsziel, Zahlungsart, Kunde.Kunde_id, Kunde.Bezeichnung " +
                "FROM Konto " +
                "JOIN Kunde ON Konto.Kunde_id = Kunde.Kunde_id";

        if (filter != null && !filter.isEmpty()) {
            sql += " WHERE Kunde.Bezeichnung LIKE '%" + filter + "%'";
        }
        System.out.println(sql);

        return sql;
    }

    @Override
    public String getSelectQueryForRowWithData(Data data) throws SQLException {
        System.out.println("Konto-getSelectQueryForRowWithData: " + data);

        String sql = "SELECT Konto_id, Kunde_id, Bezeichnung, Zahlungsziel, Zahlungsart FROM Konto WHERE Konto_id=" + data.get("Konto.Konto_id");
        System.out.println(sql);
        return sql;
    }

    @Override
    public void insertRowWithData(Data data) throws SQLException {
        System.out.println("Konto-insertRowWithData: " + data);

        if ((Integer) Project.getInstance().getData().get("permission") <= 1) {
            throw new SQLException("Nicht die notwendigen Rechte.");
        }

        PreparedStatement preparedStatement = Project.getInstance().getConnection().prepareStatement("INSERT INTO Konto (Kunde_id, Bezeichnung, Zahlungsziel, Zahlungsart) VALUES (?, ?, ?, ?)");
        preparedStatement.setObject(1, data.get("Konto.Kunde_id"));
        preparedStatement.setObject(2, data.get("Konto.Bezeichnung"));
        preparedStatement.setObject(3, data.get("Konto.Zahlungsziel"));
        preparedStatement.setObject(4, data.get("Konto.Zahlungsart"));
        preparedStatement.executeUpdate();
    }

    @Override
    public void updateRowWithData(Data oldData, Data newData) throws SQLException {
        System.out.println("Konto-updateRowWithData: " + oldData + "  :  " + newData);

        if ((Integer) Project.getInstance().getData().get("permission") <= 1) {
            throw new SQLException("Nicht die notwendigen Rechte.");
        }

        PreparedStatement preparedStatement = Project.getInstance().getConnection()
                .prepareStatement(("UPDATE Konto SET Bezeichnung = ?, Zahlungsziel = ?, Zahlungsart = ?  WHERE Konto_id = ?"));
        preparedStatement.setObject(1, newData.get("Konto.Bezeichnung"));
        preparedStatement.setObject(2, newData.get("Konto.Zahlungsziel"));
        preparedStatement.setObject(3, newData.get("Konto.Zahlungsart"));
        preparedStatement.setObject(4, oldData.get("Konto.Konto_id"));
        preparedStatement.executeUpdate();
    }

    @Override
    public void deleteRowWithData(Data data) throws SQLException {
        System.out.println("Konto-deleteRowWithData: " + data);

        if ((Integer) Project.getInstance().getData().get("permission") <= 1) {
            throw new SQLException("Nicht die notwendigen Rechte.");
        }

        PreparedStatement preparedStatement = Project.getInstance().getConnection().prepareStatement("DELETE FROM Konto WHERE Konto_id = ?");
        preparedStatement.setObject(1, data.get("Konto.Konto_id"));
        preparedStatement.executeUpdate();

    }
}
