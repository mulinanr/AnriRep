package de.hhu.cs.dbs.internship.project.table;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;
import de.hhu.cs.dbs.internship.project.Project;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Staffelpreis extends Table {
    @Override
    public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
        System.out.println("Staffelpreis-getSelectQueryForTableWithFilter: " + filter);

        String sql = "SELECT * FROM Staffelpreis ";
        if (filter != null && !filter.isEmpty()) {
            sql += " WHERE Medikament_id LIKE '%" + filter + "%'";
        }

        return sql;
    }

    @Override
    public String getSelectQueryForRowWithData(Data data) throws SQLException {
        System.out.println("Staffelpreis-getSelectQueryForRowWithData: " + data);

        String sql = "SELECT * FROM Staffelpreis WHERE Staffelpreis_id = " + data.get("Staffelpreis.Staffelpreis_id");
        return sql;
    }

    @Override
    public void insertRowWithData(Data data) throws SQLException {
        System.out.println("Staffelpreis-insertRowWithData: " + data);

        if ((Integer) Project.getInstance().getData().get("permission") <= 1) {
            throw new SQLException("Nicht die notwendigen Rechte.");
        }

        PreparedStatement preparedStatement = Project.getInstance().getConnection().prepareStatement("INSERT INTO Staffelpreis(Medikament_id, Anzahl, EinVerkauf, Preis) VALUES (?, ?, ?, ?)");
        preparedStatement.setObject(1, data.get("Staffelpreis.Medikament_id"));
        preparedStatement.setObject(2, data.get("Staffelpreis.Anzahl"));
        preparedStatement.setObject(3, data.get("Staffelpreis.EinVerkauf"));
        preparedStatement.setObject(4, data.get("Staffelpreis.Preis"));


        preparedStatement.executeUpdate();
    }

    @Override
    public void updateRowWithData(Data oldData, Data newData) throws SQLException {
        System.out.println("Staffelpreis-updateRowWithData: " + oldData + "  :  " + newData);

        if ((Integer) Project.getInstance().getData().get("permission") <= 1) {
            throw new SQLException("Nicht die notwendigen Rechte.");
        }

        PreparedStatement preparedStatement = Project.getInstance().getConnection()
                .prepareStatement(("UPDATE Staffelpreis SET Medikament_id = ?, Anzahl = ?, EinVerkauf = ?, Preis = ?  WHERE Staffelpreis_id = ?"));
        preparedStatement.setObject(1, newData.get("Staffelpreis.Medikament_id"));
        preparedStatement.setObject(2, newData.get("Staffelpreis.Anzahl"));
        preparedStatement.setObject(3, newData.get("Staffelpreis.EinVerkauf"));
        preparedStatement.setObject(4, newData.get("Staffelpreis.Preis"));
        preparedStatement.setObject(5, oldData.get("Staffelpreis.Staffelpreis_id"));

        preparedStatement.executeUpdate();
    }

    @Override
    public void deleteRowWithData(Data data) throws SQLException {
        System.out.println("Staffelpreis-deleteRowWithData: " + data);

        if ((Integer) Project.getInstance().getData().get("permission") <= 1) {
            throw new SQLException("Nicht die notwendigen Rechte.");
        }

        PreparedStatement preparedStatement = Project.getInstance().getConnection().prepareStatement("DELETE FROM Staffelpreis WHERE Staffelpreis_id = ?");
        preparedStatement.setObject(1, data.get("Staffelpreis.Staffelpreis_id"));
        preparedStatement.executeUpdate();
    }
}
