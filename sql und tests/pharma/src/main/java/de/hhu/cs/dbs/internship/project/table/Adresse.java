package de.hhu.cs.dbs.internship.project.table;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;
import de.hhu.cs.dbs.internship.project.Project;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Adresse extends Table {

    @Override
    public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
        System.out.println("Adresse-getSelectQueryForTableWithFilter: " + filter);

        String sql = "SELECT * " +
                "FROM Adresse ";

        if (filter != null && !filter.isEmpty()) {
            sql += " WHERE PLZ LIKE '%" + filter + "%'";
        }
        System.out.println(sql);

        return sql;
    }

    @Override
    public String getSelectQueryForRowWithData(Data data) throws SQLException {
        System.out.println("Adresse-insertRowWithData: " + data);

        String sql = "SELECT * FROM Adresse WHERE addr_id=" + data.get("Adresse.addr_id");
        System.out.println(sql);
        return sql;
    }

    @Override
    public void insertRowWithData(Data data) throws SQLException {
        System.out.println("Adresse-insertRowWithData: " + data);

        if ((Integer) Project.getInstance().getData().get("permission") <= 1) {
            throw new SQLException("Nicht die notwendigen Rechte.");
        }

        PreparedStatement preparedStatement = Project.getInstance().getConnection().prepareStatement("INSERT INTO Adresse (Strasse, Hausnr, PLZ, Ort) VALUES (?, ?, ?, ?)");
        preparedStatement.setObject(1, data.get("Adresse.Strasse"));
        preparedStatement.setObject(2, data.get("Adresse.Hausnr"));
        preparedStatement.setObject(3, data.get("Adresse.PLZ"));
        preparedStatement.setObject(4, data.get("Adresse.Ort"));
        preparedStatement.executeUpdate();
    }

    @Override
    public void updateRowWithData(Data oldData, Data newData) throws SQLException {
        System.out.println("Adresse-updateRowWithData: " + oldData + "  :  " + newData);

        PreparedStatement preparedStatement = Project.getInstance().getConnection()
                .prepareStatement(("UPDATE Adresse SET Strasse = ?, Hausnr = ?, PLZ = ?, Ort = ?  WHERE addr_id = ?"));
        preparedStatement.setObject(1, newData.get("Adresse.Strasse"));
        preparedStatement.setObject(2, newData.get("Adresse.Hausnr"));
        preparedStatement.setObject(3, newData.get("Adresse.PLZ"));
        preparedStatement.setObject(4, newData.get("Adresse.Ort"));
        preparedStatement.setObject(5, oldData.get("Adresse.addr_id"));
        preparedStatement.executeUpdate();

    }

    @Override
    public void deleteRowWithData(Data data) throws SQLException {
        System.out.println("Adresse-deleteRowWithData: " + data);

        PreparedStatement preparedStatement = Project.getInstance().getConnection().prepareStatement("DELETE FROM Adresse WHERE addr_id = ?");
        preparedStatement.setObject(1, data.get("Adresse.addr_id"));
        preparedStatement.executeUpdate();
    }
}
